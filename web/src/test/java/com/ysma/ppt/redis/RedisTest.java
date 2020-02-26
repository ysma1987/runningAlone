package com.ysma.ppt.redis;

import com.ysma.ppt.BaseTest;
import com.ysma.ppt.PptApplication;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import java.nio.charset.StandardCharsets;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = PptApplication.class)
public class RedisTest extends BaseTest {

    /**按类型装载*/
    @Autowired
    @Qualifier("redisTemplate") //springboot2-redis原生
    private RedisTemplate<Object, Object> redisTemplate;

    /**提供更细粒度的装载*/
    @Autowired
    @Qualifier("dxRedisTemplate")
    private RedisTemplate<String, Object> dxRedisTemplate;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Test
    public void testDiv(){
        dxRedisTemplate.opsForValue().setIfAbsent("ysma2", "yes");
        System.out.println(dxRedisTemplate.opsForValue().get("ysma2"));
        boolean flag = dxRedisTemplate.delete("ysma2");
        System.out.println("===>"+flag);
        System.out.println("===>"+dxRedisTemplate.getValueSerializer().getClass().getName());
        System.out.println("===>"+redisTemplate.getValueSerializer().getClass().getName());
    }

    @Test
    public void testWR(){
        stringRedisTemplate.opsForValue().setIfAbsent("hello", "world");
        System.out.println(stringRedisTemplate.opsForValue().get("hello"));
        boolean flag = stringRedisTemplate.delete("hello");
        System.out.println("===>"+flag);
    }

    @Test
    public void testTtl(){
        redisTemplate.delete("ysmaStr");
        Long ttl = redisTemplate.getExpire("ysmaStr");
        System.out.println(ttl);
    }

    @Test
    public void testRW(){
        redisTemplate.opsForHash().put("ysma2", "java", "good");

        String result = redisTemplate.<String, String>opsForHash().get("ysma2", "java");
        System.out.println("=======>"+result);
    }

    @Test
    public void testDel(){
        String key = String.join(":", "two_minutes", "54BAAF57D7DEF234");
        Boolean count = redisTemplate.opsForValue().getOperations().delete(key);
        System.out.println(key+"====》"+count);
    }

    @Test
    public void testLock(){
        final String key = "ysma";
        final String val="test";
        long expTime = 5;
        boolean flag = redisTemplate.execute((RedisCallback<Boolean>) redisConnection -> {
            Object obj = redisConnection.execute("set",
                    getBytes(key),
                    getBytes(val),
                    getBytes("NX"),
                    getBytes("EX"),
                    getBytes(String.valueOf(expTime)));
            return obj != null;
        });
        System.out.println("flag:"+flag);
        flag = redisTemplate.execute((RedisCallback<Boolean>) redisConnection -> {
            Object obj = redisConnection.execute("set",
                    getBytes(key),
                    getBytes(val),
                    getBytes("NX"),
                    getBytes("EX"),
                    getBytes(String.valueOf(expTime)));
            return obj != null;
        });
        System.out.println("flag2="+flag);

        String out = redisTemplate.execute(new RedisCallback<String>() {
            @Override
            public String doInRedis(RedisConnection redisConnection) throws DataAccessException {
                byte[] valB = redisConnection.get(key.getBytes());
                long ttl = redisConnection.ttl(key.getBytes());
                return new String(valB) + ttl;
            }
        });
        System.out.println("out:"+out);
    }

    private byte[] getBytes(String obj){
        return obj.getBytes(StandardCharsets.UTF_8);
    }
}
