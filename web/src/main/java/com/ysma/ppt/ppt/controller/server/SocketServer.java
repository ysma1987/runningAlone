package com.ysma.ppt.ppt.controller.server;

import com.ysma.ppt.ppt.service.NormalService;
import com.ysma.ppt.ppt.util.SpringContextUtil;
import com.ysma.ppt.ppt.util.resource.LocalXmlUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.dom4j.Document;
import org.dom4j.Element;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Set;

/**
 * @author ysma 2019-09-04
 * nio socket服务端
 */
@Slf4j
public class SocketServer {

    private final String ENCODING = "UTF-8";
    //解码buffer
    private Charset cs = Charset.forName(ENCODING);

    //接受数据缓冲区
    private static ByteBuffer rBuffer = ByteBuffer.allocate(1024);

    //选择器
    private static Selector selector;

    /**
     * 开启socket服务
     * @param port 端口号
     */
    public void startSocketServer(int port){
        try {
            //打开通信信道
            ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();

            //设置为非阻塞
            serverSocketChannel.configureBlocking(false);

            //获取套接字
            ServerSocket serverSocket = serverSocketChannel.socket();

            //绑定端口号
            serverSocket.bind(new InetSocketAddress(port));

            //打开监听器[选择器
            selector = Selector.open();

            //将通信信道注册到监听器
            serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);

            //监听器会一直监听，如果客户端有请求就会进入相应的事件处理
            while (true){
                selector.select();//select方法会一直阻塞直到有相关事件发生或超时
                Set<SelectionKey> selectionKeys = selector.selectedKeys();//监听到的事件
                for (SelectionKey key : selectionKeys) {
                    handle(key);
                }
                selectionKeys.clear();//清除处理过的事件
            }
        } catch (IOException e) {
            log.error("SocketServer 开启socket服务 IOException异常", e);
        } catch (Exception e) {
            log.error("SocketServer 开启socket服务 Exception异常", e);
        }
    }

    /**事件处理*/
    private void handle(SelectionKey selectionKey) throws IOException {
        SocketChannel socketChannel;
        StringBuilder requestMsg = new StringBuilder();
        if(selectionKey.isAcceptable()){
            //每有客户端连接，即注册通信信道为可读
            ServerSocketChannel serverSocketChannel = (ServerSocketChannel)selectionKey.channel();
            socketChannel = serverSocketChannel.accept();
            socketChannel.configureBlocking(false);
            socketChannel.register(selector, SelectionKey.OP_READ);
        } else if(selectionKey.isReadable()){
            //触发读操作
            socketChannel = (SocketChannel)selectionKey.channel();
            rBuffer.clear();
            //读取数据 -1结束
            while (socketChannel.read(rBuffer) > 0){
                rBuffer.flip();
                requestMsg.append(String.valueOf(cs.decode(rBuffer).array()));
                rBuffer.clear();
            }

            //处理请求
            String respMsg = handleMsg(requestMsg.toString().trim());

            //返回结果
            ByteBuffer sBuffer = ByteBuffer.allocate(respMsg.getBytes(ENCODING).length);//发送数据缓冲区
            sBuffer.put(respMsg.getBytes(ENCODING));
            sBuffer.flip();
            socketChannel.write(sBuffer);
            socketChannel.close();
        }
    }

    /**
     * 消息处理 耦合了H5网贷的业务逻辑
     * @param reqMsg param
     * @return resp
     */
    private String handleMsg(String reqMsg){
        try {
            Document doc = LocalXmlUtil.parseXml(reqMsg);
            if(doc == null){//1.前置判断
                return wrapError("入参xml解析失败,请检查必填字段或者xml格式");
            } else {
                Element root = doc.getRootElement();
                Element apiCodeE = root.element("apiCode");//服务编码->apiCode
                if(apiCodeE == null || StringUtils.isEmpty(apiCodeE.getStringValue())){//前置判断2
                    return wrapError("入参xml解析失败,请检查必填字段或者xml格式");
                } else {//请求路由
                    String apiCode = apiCodeE.getStringValue();
                    NormalService normalService = SpringContextUtil.getBean(NormalService.class);
                    return normalService.ask(apiCode);
                }
            }
        } catch (Exception e) {
            log.error("SocketServer.handleMsg 消息处理异常, reqMsg:{}", reqMsg, e);
            return wrapError("消息处理失败");
        }
    }

    private String wrapError(String errorMsg){
        Document doc = LocalXmlUtil.getResModel();
        Element root = doc.getRootElement();
        root.element("code").setText("500");
        root.element("message").setText(errorMsg);
        root.element("returnTime").setText(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        return doc.asXML();
    }
}
