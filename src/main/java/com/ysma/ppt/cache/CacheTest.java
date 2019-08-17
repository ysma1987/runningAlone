package com.ysma.ppt.cache;

import org.springframework.aop.framework.AopContext;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import java.util.Random;

@Component
public class CacheTest {

	public Integer test(){
		return getSelf().getRandom(100);
	}

	@Cacheable(value = "ppt", key = "#p0")
	public Integer getRandom(Integer size){
		Random random = new Random();
		return random.nextInt(size);
	}

	private CacheTest getSelf(){
		return AopContext.currentProxy() == null ? this : (CacheTest) AopContext.currentProxy();
	}

}