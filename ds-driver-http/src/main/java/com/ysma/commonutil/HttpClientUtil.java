package com.ysma.commonutil;

import org.apache.http.*;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.ConnectionKeepAliveStrategy;
import org.apache.http.conn.routing.HttpRoute;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.LayeredConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicHeaderElementIterator;
import org.apache.http.protocol.HTTP;
import org.apache.http.protocol.HttpContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.SSLException;
import java.io.IOException;
import java.io.InterruptedIOException;
import java.net.UnknownHostException;

/**
 * @author xiaowei.xing @Date: 18-4-25 下午3:07
 * @author ysma 2019-10-21
 */
public class HttpClientUtil {

  private static final Logger log = LoggerFactory.getLogger(HttpClientUtil.class);

  private static CloseableHttpClient httpClient = null;

  private static final Object syncLock = new Object();

  public static CloseableHttpClient getHttpClient(String url) {
    String hostname = url.split("/")[2];
    int port = 80;
    if (hostname.contains(":")) {
      String[] arr = hostname.split(":");
      hostname = arr[0];
      port = Integer.parseInt(arr[1]);
    }
    if (httpClient == null) {
      synchronized (syncLock) {
        if (httpClient == null) {
          httpClient = createHttpClient(200, 40, 100, hostname, port);
        }
      }
    }
    return httpClient;
  }

  public static CloseableHttpClient createHttpClient(
      int maxTotal, int maxPerRoute, int maxRoute, String hostname, int port) {
    ConnectionSocketFactory plainsf = PlainConnectionSocketFactory.getSocketFactory();
    LayeredConnectionSocketFactory sslsf = SSLConnectionSocketFactory.getSocketFactory();
    Registry<ConnectionSocketFactory> registry =
        RegistryBuilder.<ConnectionSocketFactory>create()
            .register("http", plainsf)
            .register("https", sslsf)
            .build();
    PoolingHttpClientConnectionManager cm = new PoolingHttpClientConnectionManager(registry);
    // 将最大连接数增加
    cm.setMaxTotal(maxTotal);
    // 将每个路由基础的连接增加
    cm.setDefaultMaxPerRoute(maxPerRoute);
    HttpHost httpHost = new HttpHost(hostname, port);
    // 将目标主机的最大连接数增加
    cm.setMaxPerRoute(new HttpRoute(httpHost), maxRoute);

    // 请求重试处理
    HttpRequestRetryHandler httpRequestRetryHandler =
        new HttpRequestRetryHandler() {
          public boolean retryRequest(
              IOException exception, int executionCount, HttpContext context) {
            if (executionCount >= 5) { // 如果已经重试了5次，就放弃
              log.error("重试5次后放弃, hostname:{}, port:{}", hostname, port);
              return false;
            }
            if (exception instanceof NoHttpResponseException) { // 如果服务器丢掉了连接，那么就重试
              log.error("服务器无响应, hostname:{}, port:{}", hostname, port);
              return false;
            }
            if (exception instanceof SSLException) { // 不要重试SSL握手异常
              log.error("SSL异常, hostname:{}, port:{}", hostname, port);
              return false;
            }
            if (exception instanceof InterruptedIOException) { // 超时
              log.error("InterruptedIOException, hostname:{}, port:{}", hostname, port);
              return false;
            }
            if (exception instanceof UnknownHostException) { // 目标服务器不可达
              log.error("目标服务器不可达, hostname:{}, port:{}", hostname, port);
              return false;
            }

            HttpClientContext clientContext = HttpClientContext.adapt(context);
            HttpRequest request = clientContext.getRequest();
            // 如果请求是幂等的，就再次尝试
            return !(request instanceof HttpEntityEnclosingRequest);
          }
        };
    ConnectionKeepAliveStrategy myStrategy =
        new ConnectionKeepAliveStrategy() {
          public long getKeepAliveDuration(HttpResponse response, HttpContext context) {
            // Honor 'keep-alive' header
            HeaderElementIterator it =
                new BasicHeaderElementIterator(response.headerIterator(HTTP.CONN_KEEP_ALIVE));
            while (it.hasNext()) {
              HeaderElement he = it.nextElement();
              String param = he.getName();
              String value = he.getValue();
              if (value != null && param.equalsIgnoreCase("timeout")) {
                try {
                  return Long.parseLong(value);
                } catch (NumberFormatException ignore) {

                }
              }
            }
            return 1_800_000L;
          }
        };
    return HttpClients.custom()
            .setKeepAliveStrategy(myStrategy)
            .setConnectionManager(cm)
            .setRetryHandler(httpRequestRetryHandler)
            .build();
  }
}
