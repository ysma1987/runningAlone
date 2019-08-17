package com.ysma.ppt;

import com.ysma.ppt.cache.CacheConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.FilterType;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling//schedule
@EnableCaching //caffeine
@EnableAspectJAutoProxy(exposeProxy = true)//AopContext.currentProxy()
@ComponentScan(basePackages = {"com.ysma.ppt"},
		excludeFilters = {
			@ComponentScan.Filter(value = CacheConfig.class, type= FilterType.ASSIGNABLE_TYPE)})
public class PptApplication {

	public static void main(String[] args) {
		SpringApplication.run(PptApplication.class, args);
	}

}
