package com.ysma.ppt.config.factory;

import com.ysma.ppt.intf.enums.TcpModelEnum;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.context.annotation.Description;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.SocketException;

@Slf4j
@Description("TCP客户端工具类")
public class SocketFacade {

    //编码格式固定为 utf8
    private static final String ENCODING = "UTF-8";

    /**
     * ip和port以及timeout具有业务属性，通过入参将必填属性从工具类中剥离出去
     *
     * @param ip ecp服务器ip
     * @param port tcp服务器端口
     * @param xml 请求报文
     * @param tcpTimeOut 设置tcp超时时间 【单位毫秒】 默认30s
     * @return 响应报文
     */
    public static String sendMsg(String ip, int port, String xml, TcpModelEnum model, Integer... tcpTimeOut) throws Exception {

        Socket clientSocket;
        //1.设置ip:port为锁key值
        String ipPort = String.format("%s:%s", ip, port);
        try {
            //2.获取socket
            clientSocket = SocketFactory.getSocket(ipPort, tcpTimeOut);

            //5.发起请求 获取结果
            String resp = null;
            switch (model){//因为socket的输入输出流关闭顺序不同 需要区分ESB和外部数据平台
                case CLOSE_BEFORE:
                    resp = reqESB(clientSocket, xml);
                    break;
                case CLOSE_AFTER:
                    resp = reqDirect(clientSocket, xml);
                    break;
                default:
                    log.error("SocketFacade.sendMsg 不支持此TCP请求模式 ipPort:{}, MODEL:{}", ipPort, model);
                    break;
            }

            //5.记录日志 返回结果
            log.debug("SocketFacade.sendMsg ipPort==>{}, req==>{}, model==>{}, res==>{}", ipPort, xml, model, resp);
            return resp;
        } catch (SocketException ex) {
            log.error("SocketFacade.sendMsg 套接字创建失败 ipPort:{} model:{} SocketException", ipPort, model, ex);
            throw ex;
        } catch (IOException ex) {
            log.error("SocketFacade.sendMsg 套接字请求失败 ipPort:{} model:{} IOException", ipPort, model, ex);
            throw ex;
        } catch (Exception ex) {
            log.error("SocketFacade.sendMsg 套接字请求失败 ipPort:{} model:{} Exception", ipPort, model, ex);
            throw ex;
        } finally {
            boolean flag = SocketFactory.closeSocket(ipPort);
            log.debug("SocketFacade.sendMsg 套接字关闭:{}, IPPort:{}", flag, ipPort);
        }
    }

    /**
     * 发起请求，包装响应结果
     * @param clientSocket 套接字
     * @param xml 报文
     * @return 结果
     * @throws IOException ex
     */
    private static String reqDirect(Socket clientSocket,String xml) throws IOException {
        OutputStream out = null;
        InputStream inS = null;
        try {
            //1.发起请求
            out = clientSocket.getOutputStream();
            IOUtils.write(xml.getBytes(ENCODING), out);
            out.flush();
            clientSocket.shutdownOutput();

            //2.获取响应
            inS = clientSocket.getInputStream();
            String resp = IOUtils.toString(inS, ENCODING);
            clientSocket.shutdownInput();
            return resp;
        } finally {
            //3 客户端先关闭 否则获取不到响应结果
            IOUtils.closeQuietly(out);
            IOUtils.closeQuietly(inS);
        }
    }

    /**
     * 发起请求，包装响应结果 forESB[人行征信tcp接口]
     * @param clientSocket 套接字
     * @param xml 报文
     * @return 结果
     * @throws IOException ex
     */
    private static String reqESB(Socket clientSocket, String xml) throws IOException {
        OutputStream out = null;
        InputStream inS = null;
        try {
            //1.发起请求
            out = clientSocket.getOutputStream();
            IOUtils.write(xml.getBytes(ENCODING), out);
            out.flush();

            //2.获取响应
            inS = clientSocket.getInputStream();
            return IOUtils.toString(inS, ENCODING);
        } finally {
            //3. 客户端后关闭 否则获取不到响应
            clientSocket.shutdownInput();
            clientSocket.shutdownOutput();

            IOUtils.closeQuietly(out);
            IOUtils.closeQuietly(inS);
        }
    }

    public static void main(String[] args) {
        Thread threadTest = new Thread(() -> {
            String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><controlData><apiCode desc=\"交易机构-必填\">java</apiCode><transMedium desc=\"渠道号-必填\">good</transMedium><hostIp desc=\"服务调用方IP-必填\">127.0.0.1</hostIp></controlData>";
            try {
                SocketFacade.sendMsg("127.0.0.1", 8788, xml, TcpModelEnum.CLOSE_BEFORE, 10_000);
            } catch (Exception e) {
                log.error("Exception:", e);
            }
        });

        threadTest.start();
    }
}
