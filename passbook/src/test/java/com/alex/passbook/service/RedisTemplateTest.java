package com.alex.passbook.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author wsh
 * @date 2020-08-19
 * <h1>Redis 客户端测试</h1>
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class RedisTemplateTest {

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Test
    public void testRedisTemplate(){
        //redis flushall
//        redisTemplate.execute((RedisCallback<Object>) connection -> {
//            connection.flushAll();
//            return null;
//        });
//        assert redisTemplate.opsForValue().get("name") == null;

        //测试redis
        redisTemplate.opsForValue().set("name", "alex");
        assert redisTemplate.opsForValue().get("name") != null;
        System.out.println(redisTemplate.opsForValue().get("name"));
    }
}
