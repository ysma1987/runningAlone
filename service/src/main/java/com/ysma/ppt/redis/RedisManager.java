package com.ysma.ppt.redis;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import javax.annotation.Resource;
import java.time.Duration;

/**
 * @author ysma 2019-09-17
 */
@Slf4j
@Configuration
@EnableCaching
public class RedisManager extends CachingConfigurerSupport {

    @Value("${cache.default-exp}")
    private Long exps;

    @Resource
    private LettuceConnectionFactory lettuceConnectionFactory;

    @Bean(name = "cacheManager")
    public CacheManager cacheManager() {
        // 配置序列化
        RedisCacheConfiguration config = RedisCacheConfiguration.defaultCacheConfig();
        config.serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(keySerializer()));
        config.serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(valueSerializer()));
        // 设置缓存的默认过期时间
        config.entryTtl(Duration.ofSeconds(exps));
        // 不缓存空值
        config.disableCachingNullValues();
        RedisCacheManager redisCacheManager = RedisCacheManager
                .builder(lettuceConnectionFactory)
                .cacheDefaults(config)
                .transactionAware()
                .build();
        log.info("------> 自定义RedisCacheManager加载完成");
        return redisCacheManager;
    }

    /**自定义模板 RedisTemplate<String, Object>
     * 原生模板 RedisTemplate<Object, Object>
     * */
    @Bean(name = "dxRedisTemplate")
    public RedisTemplate<String, Object> redisTemplate() {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(lettuceConnectionFactory);
        template.setHashKeySerializer(keySerializer());
        template.setKeySerializer(keySerializer());
        template.setValueSerializer(valueSerializer());
        template.setHashValueSerializer(valueSerializer());
        log.info("------> 自定义RedisTemplate加载完成");
        return template;
    }

    private RedisSerializer<String> keySerializer() {
        return new StringRedisSerializer();
    }

    private RedisSerializer<Object> valueSerializer() {
        /*Jackson2JsonRedisSerializer<Object> jsonRedisSerializer =
                new Jackson2JsonRedisSerializer<>(Object.class);
        ObjectMapper om = new ObjectMapper();
        om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        om.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
        jsonRedisSerializer.setObjectMapper(om);
        return jsonRedisSerializer;*/
        /*return new GenericJackson2JsonRedisSerializer();*/

        FastJson2JsonRedisSerializer<Object> serializer = new FastJson2JsonRedisSerializer<>(Object.class);
        ObjectMapper mapper = new ObjectMapper();
        mapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        mapper.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
        serializer.setObjectMapper(mapper);
        return serializer;
    }

}
