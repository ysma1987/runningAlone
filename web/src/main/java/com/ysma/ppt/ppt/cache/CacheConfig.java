package com.ysma.ppt.ppt.cache;

import com.github.benmanes.caffeine.cache.CacheLoader;
import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.cache.CacheManager;
import org.springframework.cache.caffeine.CaffeineCacheManager;

import java.util.Collections;
import java.util.concurrent.TimeUnit;

/*
@Slf4j
@Configuration
@deprecated 使用配置方式 此方式留待扩展
 配置方式测试见
 @see CacheTest.testCache
*/
@Deprecated
@SuppressWarnings("all")
public class CacheConfig {

    private static final String CACHE_NAME = "channel_agg";

    //配置CacheManager
    /*@Bean(name = "caffeine")*/
    public CacheManager cacheManagerWithCaffeine() {
        CaffeineCacheManager cacheManager = new CaffeineCacheManager();

        /**
         * initialCapacity=[integer]: 初始的缓存空间大小
         * maximumSize=[long]: 缓存的最大条数
         * maximumWeight=[long]: 缓存的最大权重
         * expireAfterAccess=[duration秒]: 最后一次写入或访问后经过固定时间过期
         * expireAfterWrite=[duration秒]: 最后一次写入后经过固定时间过期
         * refreshAfterWrite=[duration秒]: 创建缓存或者最近一次更新缓存后经过固定的时间间隔，刷新缓存
         * weakKeys: 打开key的弱引用
         * weakValues：打开value的弱引用
         * softValues：打开value的软引用
         * recordStats：开发统计功能
         */
        Caffeine caffeine = Caffeine.newBuilder()
                //cache的初始容量值
                .initialCapacity(100)
                //maximumSize用来控制cache的最大缓存数量，maximumSize和maximumWeight(最大权重)不可以同时使用，
                .maximumSize(5000)
                //最后一次写入或者访问后过久过期
                .expireAfterAccess(2, TimeUnit.HOURS)
                //创建或更新之后多久刷新,指定了refreshAfterWrite还需要设置cacheLoader
                .refreshAfterWrite(10, TimeUnit.SECONDS);

        cacheManager.setCaffeine(caffeine);
        cacheManager.setCacheLoader(cacheLoader());
        cacheManager.setCacheNames(Collections.singletonList(CACHE_NAME));//根据名字可以创建多个cache，但是多个cache使用相同的策略
        cacheManager.setAllowNullValues(false);//是否允许值为空
        return cacheManager;
    }

    /**
     * 必须要指定这个Bean，refreshAfterWrite配置属性才生效
     */
    /*@Bean*/
    public CacheLoader<Object, Object> cacheLoader() {
        return new CacheLoader<Object, Object>() {
            @Override
            public Object load(Object key) throws Exception { return null;}

            // 重写这个方法将oldValue值返回回去，进而刷新缓存
            @Override
            public Object reload(Object key, Object oldValue) throws Exception {
                return oldValue;
            }
        };
    }
}
