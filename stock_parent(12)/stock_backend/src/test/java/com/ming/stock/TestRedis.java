package com.ming.stock;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

/**
 * @Author: Ming
 * @Description TODO
 */
@SpringBootTest
public class TestRedis {
    @Autowired
    private RedisTemplate<String,String> redisTemplate;
    @Test
    public void test01(){
        //存入值
        redisTemplate.opsForValue().set("myName","张飞");
        //获取值
        String myName = redisTemplate.opsForValue().get("myName");
        System.out.println(myName);
    }
}
