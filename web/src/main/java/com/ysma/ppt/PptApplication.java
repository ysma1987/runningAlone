package com.ysma.ppt;

import com.ysma.ppt.controller.server.SocketServer;
import com.ysma.ppt.redis.CacheConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.FilterType;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;
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
@EnableElasticsearchRepositories(basePackages = "com.ysma.ppt.es.repository")
public class PptApplication {

	public static void main(String[] args) {
		/**
		 * Springboot整合Elasticsearch&redis 在项目启动前设置一下的属性，防止报错
		 * 解决netty冲突后初始化client时还会抛出异常
		 * java.lang.IllegalStateException: availableProcessors is already set to [4], rejecting [4]
		 */
		System.setProperty("es.set.netty.runtime.available.processors", "false");

		SpringApplication.run(PptApplication.class, args);

		//启动socket服务
		SocketServer server = new SocketServer();
		server.startSocketServer(8788);
	}

}
