package com.zihai.h2Client.test;


import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import javax.annotation.Resource;

@RunWith(SpringRunner.class)
@EnableAutoConfiguration
@ComponentScan("com.zihai")
@SpringBootTest
public class RedisTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(RedisTest.class);
    @Autowired
    private RedisTemplate<String,String> redisTemplate1;

    @Resource(name="redisOneTemplate")
    private RedisTemplate<String,String> redisTemplate;

    @Test
    public void doTest(){
        redisTemplate1.opsForValue().set("myname","liuyizhi4");
        LOGGER.info(redisTemplate1.opsForValue().get("myname"));
        redisTemplate.opsForValue().set("myname","liuyizhi5");
        LOGGER.info(redisTemplate.opsForValue().get("myname"));
    }

    public static void main(String[] args) {
        JedisPoolConfig config = new JedisPoolConfig();
        config.setMaxTotal(5);
        config.setMaxIdle(2);
        config.setMaxWaitMillis(500);
        config.setTestOnBorrow(true);
        config.setTestOnReturn(true);
        JedisPool jedisPool = new JedisPool(config,"localhost",6379
                ,2000,null,2,null);
        Jedis jedis = jedisPool.getResource();
        jedis.set("myname","yizhi0");
        jedis.close();
        LOGGER.info(jedisPool.getResource().get("myname"));
    }
}
