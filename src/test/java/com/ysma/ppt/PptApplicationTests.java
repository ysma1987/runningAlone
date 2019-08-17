package com.ysma.ppt;

import com.ysma.ppt.cache.CacheTest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.concurrent.TimeUnit;

/**
 * springboot2 + junit4
 * 测试框架
 */
@RunWith(SpringRunner.class)//导入spring测试框架[2]
@SpringBootTest(classes = PptApplication.class)//提供spring依赖注入
public class PptApplicationTests {

	@Autowired
	private CacheTest cacheTest;

	@Test
	public void testCache() {
		Thread testAopCache = new Thread(()->{
			Integer a = cacheTest.test();
			Integer b = cacheTest.test();
			System.out.println("aop>a:"+a+";b="+b);
			try {
				TimeUnit.SECONDS.sleep(10);
			} catch (InterruptedException e) {
				//等待缓存过期
			}
			Integer c = cacheTest.test();
			Integer d = cacheTest.test();
			System.out.println("aop>c:"+c+";d="+d);

		});
		Thread testCache = new Thread(()->{
			Integer a = cacheTest.getRandom(100);
			Integer b = cacheTest.getRandom(100);
			System.out.println("a:"+a+";b="+b);
			try {
				TimeUnit.SECONDS.sleep(5);
			} catch (InterruptedException e) {
				//等待缓存过期
			}
			Integer c = cacheTest.getRandom(100);
			Integer d = cacheTest.getRandom(100);
			System.out.println("c:"+c+";d="+d);
		});

		try {
			testAopCache.start();
			testAopCache.join();
			/*testCache.start();
			testCache.join();*/
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

	}

}
