package com.zihai.h2Client.test;


import com.zihai.dto.People;
import com.zihai.h2Client.util.JsonHelp;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.test.context.junit4.SpringRunner;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CountDownLatch;

@RunWith(SpringRunner.class)
@EnableAutoConfiguration
@ComponentScan("com.zihai")
@SpringBootTest
public class RedisTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(RedisTest.class);
    @Autowired
    private RedisTemplate<String, String> redisTemplate1;
    @Autowired
    private People people;

    //@Resource(name="redisOneTemplate")
    private RedisTemplate<String, Integer> redisTemplate;

    //@Resource(name="redisOneTemplate")
    private RedisTemplate<String, String> redisTemplate2;

    @Test
    public void doTestList() {
        redisTemplate2.opsForList().leftPush("mysn", "1");
        Long l = redisTemplate2.opsForList().leftPush("mysn", "2");
        LOGGER.info("the num == {}", l);
        List list = redisTemplate2.opsForList().range("mysn", 0, 20);
        LOGGER.info(JsonHelp.gson.toJson(list));
    }

    /**
     * increment
     * @throws InterruptedException
     */
    @Test
    public void doTestIncrement() throws InterruptedException {
        redisTemplate.opsForValue().set("count", 2001);

        //concurrent test
        Thread t = new Thread() {
            @Override
            public void run() {
                for (int i = 0; i < 1000; i++) {
                    synchronized (redisTemplate) {
                        Integer count = redisTemplate.opsForValue().get("count");
                        LOGGER.info("Thread1 count {},i {}= ", count, i);
                        redisTemplate.opsForValue().increment("count", -1);
                    }

                }

            }
        };
        t.start();
        Thread t2 = new Thread() {
            @Override
            public void run() {
                for (int i = 0; i < 1000; i++) {
                    synchronized (redisTemplate) {
                        Integer count = redisTemplate.opsForValue().get("count");
                        LOGGER.info("Thread2 count {},i {}= ", count, i);
                        redisTemplate.opsForValue().increment("count", -1);
                    }
                }

            }
        };
        t2.start();
        Thread.sleep(100000);
    }

    @Test
    public void doTestScan(){

        Set<Object> execute = redisTemplate1.execute(new RedisCallback<Set<Object>>() {
            @Override
            public Set<Object> doInRedis(RedisConnection connection){
                Set<Object> binaryKeys = new HashSet<>();
                Cursor<byte[]> cursor = connection.scan( new ScanOptions.ScanOptionsBuilder().match("num99*").count(10).build());
                while (cursor.hasNext()) {
                    binaryKeys.add(new String(cursor.next()));
                }
                return binaryKeys;
            }
        });
        System.out.println(execute.size());
        System.out.println(execute);
    }

    @Test
    public void doTestThread() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(101);
        for (int i = 0; i <= 2; i++) {
            int finalI = i;
            new Thread(new Runnable() {
                @Override
                public void run() {
                    for (; ; ) {
                        try {
                            System.out.println("go into begin");
                            if (redisTemplate1.hasKey("num" + 1)) {
                                redisTemplate1.opsForValue().increment("num" + finalI, 1);
                            } else {
                                redisTemplate1.opsForValue().set("num" + finalI, String.valueOf(0));
                            }
                            System.out.println(redisTemplate1.opsForValue().get("num" + finalI));
                            Thread.sleep(500);
                        } catch (Exception e) {
                            e.printStackTrace();
                            //System.out.println(e.getMessage());
                        }
                    }

                }
            }).start();
        }
        latch.await();
    }

    @Test
    public void doTestBean(){
        System.out.println(people.getName()+" "+people.getAge());
    }
    public static void main(String[] args) {
        JedisPoolConfig config = new JedisPoolConfig();
        config.setMaxTotal(5);
        config.setMaxIdle(2);
        config.setMaxWaitMillis(500);
        config.setTestOnBorrow(true);
        config.setTestOnReturn(true);
        JedisPool jedisPool = new JedisPool(config, "localhost", 6379
                , 2000, null, 2, null);
        Jedis jedis = jedisPool.getResource();
        jedis.set("myname", "yizhi0");
        jedis.close();
        LOGGER.info(jedisPool.getResource().get("myname"));
    }
}
