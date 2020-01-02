package com.ysma.ppt.cache;

import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.aop.framework.AopContext;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import java.util.Random;

@Component
public class CacheTest {
	public static void main(String[] args) {
		String bs = "{\"name\":\"java\",\"age\":23}";

		String originBytes = Base64.encodeBase64String(bs.getBytes());
		System.out.println(originBytes);
		if (Base64.isBase64(originBytes)) {
			byte[] bytes = Base64.decodeBase64(originBytes);
			System.out.println(new String(bytes));
		}
	}

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