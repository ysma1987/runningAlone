package com.ysma.ppt.config.factory;

import com.github.rholder.retry.*;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicReferenceArray;

/**
 * @author ysma
 * socket套接字工厂  futureTask+spinlock的简单应用
 */
@Slf4j
public class SocketFactory {

    //默认30s超时
    private static final Integer DEFAULT_TIME_OUT = 30_000;

    //注意 大小为预期的tcp服务连接数上限 如果tcp服务超时10个 请跟随修改
    private static final Integer TCP_SERVER_MAX = 10;

    private static final Integer WAIT_RELEASE_TIME = 100;

    private static ConcurrentHashMap<String, FutureTask<Socket>> socketPool = new ConcurrentHashMap<>();

    private static AtomicReferenceArray<String> spinLock = new AtomicReferenceArray<>(TCP_SERVER_MAX);

    public static Socket getSocket(final String ipPort, Integer... tcpTimeOut) throws Exception {
        try {
            do{
                FutureTask<Socket> futureTask = socketPool.putIfAbsent(ipPort,
                        new FutureTask<>(new SocketCallable(
                                ipPort,
                                tcpTimeOut.length == 0 ? DEFAULT_TIME_OUT : tcpTimeOut[0])));

                if(futureTask == null){//可以保证高并发下任务执行的幂等性
                    futureTask = socketPool.get(ipPort);
                    futureTask.run();
                    return futureTask.get();
                } else {
                    //等待  socket具有排他性 一组ip:port 只暴露一个链接在外面
                    log.info("SocketFactory.getSocket ipPort:{} 已被占用,等待释放", ipPort);
                    TimeUnit.MILLISECONDS.sleep(WAIT_RELEASE_TIME);
                }
            }while (true);
        } catch (Exception e) {
            log.error("SocketFactory 创建socket异常,ipPort:{}", ipPort, e);
            throw e;
        }
    }

    /**
     * 释放socket
     * @param ipPort tcp ip:port
     * @return true 释放成功; false 释放失败
     */
    public static boolean closeSocket(final String ipPort){
        //1、呼应get 使得新的get需要进行socket创建
        FutureTask<Socket> futureTask = socketPool.remove(ipPort);

        //2.已经关闭的忽略,这里关注未关闭的情况
        if(futureTask != null){
            try {
                Retryer<Boolean> retryEr = RetryerBuilder.<Boolean>newBuilder()
                        .retryIfExceptionOfType(IOException.class)
                        .withStopStrategy(StopStrategies.stopAfterAttempt(3))
                        .build();
                return retryEr.call(() -> {
                    //futureTask的InterruptedException, ExecutionException 不捕获=>不重试
                    Socket socket = futureTask.get();
                    socket.close();
                    return true;
                });
            } catch (ExecutionException | RetryException e) {
                log.error("SocketFactory.closeSocket 关闭连接异常,ipPort:{}", ipPort, e);
                return false;
            } finally {
                int index = Math.floorMod(ipPort.hashCode(), TCP_SERVER_MAX);
                spinLock.compareAndSet(index, ipPort, null);
            }
        }

        return true;
    }

    /**
     * 静态内部类 封装Socket
     * futureTask 具有幂等性: 即高并发下会保证任务只执行一次
     * 使得不同的TCP连接在get处阻塞
     */
    static class SocketCallable implements Callable<Socket>{

        private String ipPort;

        private int timeOut;

        SocketCallable(String ipPort, int timeOut){
            this.ipPort = ipPort;
            this.timeOut = timeOut;
        }

        @Override
        public Socket call() throws Exception {
            //1.创建socket但是不连接
            Socket clientSocket = new Socket();

            //2.确保请求包会尽可能快地发送，而无论包的大小 ==>关闭了Socket的缓冲
            clientSocket.setTcpNoDelay(true);

            //3.超时设置
            clientSocket.setSoTimeout(timeOut);

            //4.接快速释放 close后可快速复用
            clientSocket.setReuseAddress(true);

            //5.进行连接服务器 具有排他性

            //5-1.判断当前是否有连接 有则等待释放 ps: closeSocket方法 删除key的时候并没有立即释放socket
            int index = Math.floorMod(ipPort.hashCode(), TCP_SERVER_MAX);//求模运算  除数为正则结果为正
            while (!spinLock.compareAndSet(index,null, ipPort)){
            }

            //5-2.无则获取
            String[] array = ipPort.split(":");
            SocketAddress socketAddress = new InetSocketAddress(array[0], Integer.valueOf(array[1]));
            clientSocket.connect(socketAddress);

            return clientSocket;
        }
    }
}


