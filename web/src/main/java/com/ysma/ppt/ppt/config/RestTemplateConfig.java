package com.ysma.ppt.ppt.config;

import com.ysma.ppt.ppt.util.resource.VariableConfig;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

@Configuration
public class RestTemplateConfig {

    @Autowired
    private VariableConfig variableConfig;

    @Bean
    public RestTemplate restTemplate(){
        return new RestTemplate(simpleClientHttpRequestFactory());
    }
 
    @Bean
    public ClientHttpRequestFactory simpleClientHttpRequestFactory(){
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        //超时时间 单位毫秒
        final int timeOut = variableConfig.getRestTemplateTimeOut();
        factory.setConnectTimeout(timeOut);
        factory.setReadTimeout(timeOut);
        return factory;
    }

    @Bean
    public HttpComponentsClientHttpRequestFactory clientHttpRequestFactory(){
        return new HttpComponentsClientHttpRequestFactory(httpClient());
    }

    @Bean
    public HttpClient httpClient() {
        //1.注册http https
        Registry<ConnectionSocketFactory> registry = RegistryBuilder.<ConnectionSocketFactory>create()
                .register("http", PlainConnectionSocketFactory.getSocketFactory())
                .register("https", SSLConnectionSocketFactory.getSocketFactory())
                .build();

        //2.连接池管理器
        PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager(registry);
        //设置整个连接池最大连接数 根据自己的场景决定
        connectionManager.setMaxTotal(50);

        //路由是对maxTotal的细分:某一个服务每次能并行接收的请求数量
        connectionManager.setDefaultMaxPerRoute(5);

        //3.设置config配置
        RequestConfig requestConfig = RequestConfig.custom()
                //连接上服务器(握手成功)的时间，超出该时间抛出connect timeout
                .setConnectTimeout(10_000)
                //服务器返回数据(response)的时间，超过该时间抛出read timeout
                .setSocketTimeout(10_000)
                //从连接池中获取连接的超时时间，超过该时间未拿到可用连接，会抛出org.apache.http.conn.ConnectionPoolTimeoutException: Timeout waiting for connection from pool
                .setConnectionRequestTimeout(1000)
                .build();

        return HttpClientBuilder.create()
                .setConnectionManager(connectionManager)
                .setDefaultRequestConfig(requestConfig)
                .build();
    }
}