package com.zihai.h2Client.test;


import com.zihai.h2Client.util.JsonHelp;
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
import javax.persistence.criteria.CriteriaBuilder;
import java.util.List;

@RunWith(SpringRunner.class)
@EnableAutoConfiguration
@ComponentScan("com.zihai")
@SpringBootTest
public class RedisTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(RedisTest.class);
  /*  @Autowired
    private RedisTemplate<String,String> redisTemplate1;*/

    @Resource(name="redisOneTemplate")
    private RedisTemplate<String,Integer> redisTemplate;

    @Resource(name="redisOneTemplate")
    private RedisTemplate<String,String> redisTemplate2;

    @Test
    public void doTestList(){
        redisTemplate2.opsForList().leftPush("mysn","1");
        Long l = redisTemplate2.opsForList().leftPush("mysn","2");
        LOGGER.info("the num == {}",l);
        List list = redisTemplate2.opsForList().range("mysn",0,20);
        LOGGER.info(JsonHelp.gson.toJson(list));
    }


    @Test
    public void doTest() throws InterruptedException {
        redisTemplate.opsForValue().set("count",2001);

        //concurrent test
        Thread t = new Thread(){
            @Override
            public void run() {
                for(int i=0;i<1000;i++){
                    synchronized (redisTemplate){
                        Integer count = redisTemplate.opsForValue().get("count");
                        LOGGER.info("Thread1 count {},i {}= ",count,i);
                        redisTemplate.opsForValue().increment("count",-1);
                    }

                }

            }
        };
        t.start();
        Thread t2 = new Thread(){
            @Override
            public void run() {
                for(int i=0;i<1000;i++){
                    synchronized (redisTemplate) {
                        Integer count = redisTemplate.opsForValue().get("count");
                        LOGGER.info("Thread2 count {},i {}= ",count,i);
                        redisTemplate.opsForValue().increment("count",-1);
                    }
                }

            }
        };
        t2.start();
        Thread.sleep(100000);
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
