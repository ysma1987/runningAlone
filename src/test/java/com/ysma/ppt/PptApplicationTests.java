package com.ysma.ppt;

import com.github.rholder.retry.*;
import com.ysma.ppt.cache.CacheTest;
import com.ysma.ppt.service.GuavaRetryService;
import com.ysma.ppt.service.SpringRetryService;
import com.ysma.ppt.service.async.FutureService;
import com.ysma.ppt.util.ThreadUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
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

	@Autowired
	private FutureService futureService;

	@Autowired
	private SpringRetryService springRetryService;

	@Autowired
	private GuavaRetryService guavaRetryService;

	@Test
	public void testSpringRetry(){
		//TODO 测试spring的重试功能

		springRetryService.retry("gava");
	}

	@Test
	public void testGuavaRetry(){
		//TODO 测试guava的重试功能

		Retryer<String> retryer = RetryerBuilder
				.<String>newBuilder()
				.retryIfExceptionOfType(IllegalArgumentException.class)
				.withStopStrategy(StopStrategies.stopAfterAttempt(2))
				.build();
		try {
			retryer.call(()->{guavaRetryService.retry("guava"); return null;});
		} catch (ExecutionException e) {
			System.out.println("============ExecutionException");
		} catch (RetryException e) {
			System.out.println("============RetryException");
		}
	}

	@Test
	public void testAsync() throws InterruptedException {
		//TODO 测试 CompletableFuture 和ThreadLocal

		ThreadUtil.setString("age:18");
		ThreadUtil.setInheritString("age:19");

		CompletableFuture<String> javaF = futureService.guess("java");
		CompletableFuture<String> phpF = futureService.guessAgain("php");

		CompletableFuture first = CompletableFuture.anyOf(javaF, phpF).whenComplete((r, t) -> {
			if(t != null){
				System.out.println("异常" + t.getMessage());
			} else {
				System.out.println("结果：" + r);
			}
		});
		TimeUnit.SECONDS.sleep(2);
	}

	@Test
	public void testCache() {
		//TODO 测试caffeine缓存
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
