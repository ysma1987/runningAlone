package com.ysma.ppt;

import com.ysma.ppt.cache.CacheConfig;
import com.ysma.ppt.controller.server.SocketServer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.embedded.undertow.UndertowServletWebServerFactory;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.boot.web.servlet.server.ServletWebServerFactory;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.FilterType;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
//启动重试机制
@EnableRetry
//quartz
@EnableScheduling
//caffeine 缓存
@EnableCaching
//async 异步
@EnableAsync
//AopContext.currentProxy() 将代理类暴露给aop切面容器
@EnableAspectJAutoProxy(exposeProxy = true)
//包扫描 可以指定加载某个部分[包含jar内的包] 也可以指定不加载某个部分
@ComponentScan(basePackages = {"com.ysma.ppt"},
		excludeFilters = {
			@ComponentScan.Filter(value = CacheConfig.class, type= FilterType.ASSIGNABLE_TYPE)})

//Servlet、Filter、Listener 可以直接通过 @WebServlet、@WebFilter、@WebListener 注解自动注册，无需其他代码。
@ServletComponentScan
public class PptApplication {

	public static void main(String[] args) {
		SpringApplication.run(PptApplication.class, args);

		//启动socket服务
		SocketServer server = new SocketServer();
		server.startSocketServer(8788);
	}

	//
	@Bean
	public ServletWebServerFactory servletWebServerFactory(){
		Integer cpuNum = Runtime.getRuntime().availableProcessors();
		UndertowServletWebServerFactory factory = new UndertowServletWebServerFactory();
		factory.setIoThreads(cpuNum);
		factory.setWorkerThreads(cpuNum * 4);
		factory.setUseDirectBuffers(true);
		return new UndertowServletWebServerFactory();
	}
}
