package com.zihai.h2Client.test;


import com.zihai.h2Client.dto.People;
import com.zihai.h2Client.util.JsonHelp;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.io.ClassPathResource;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisClusterConnection;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.*;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.scripting.support.ResourceScriptSource;
import org.springframework.test.context.junit4.SpringRunner;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.TimeUnit;

@RunWith(SpringRunner.class)
@EnableAutoConfiguration
@ComponentScan("com.zihai.h2Client")
@SpringBootTest
public class RedisTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(RedisTest.class);
    @Autowired
    private RedisTemplate<String,String> redisTemplate1;
    @Autowired
    private People people;

    //@Resource(name="redisOneTemplate")
    private RedisTemplate<String, Integer> redisTemplate;

    @Resource(name = "redisTemplate")
    private RedisTemplate redisTemplate2;

    //@Resource(name="redisOneTemplate")
    private RedisTemplate<String, BigDecimal> redisTemplate3;

    //@Resource(name = "redisClusterTemplate")
    private RedisClusterConnection redisClusterConnection;

    @Autowired
    ThreadPoolTaskExecutor threadPoolTaskExecutor;

    @Test
    public void TestLua() {
        DefaultRedisScript redisScript = new DefaultRedisScript<>();
        redisScript.setScriptSource(new ResourceScriptSource(new ClassPathResource("lua/test.lua")));
        redisTemplate.execute(redisScript, new ArrayList<>(), "vals");
    }

    @Test
    public void TestCluster() {
        LOGGER.info("begingTestCluseter2");
        redisClusterConnection.del("tempHuodong".getBytes());
        for (int i = 0; i < 20; i++) {
            redisClusterConnection.lPush("tempHuodong".getBytes(), (i + "").getBytes());
        }
        redisClusterConnection.lRem("tempHuodong".getBytes(), 1, "18".getBytes());
        List<String> list = new ArrayList<>();
        redisClusterConnection.lRange("tempHuodong".getBytes(),0,-1).stream().forEach(e->{
            list.add(new String(e));
        });
        LOGGER.info(JsonHelp.gson.toJson(list));
       // LOGGER.info(new String(redisClusterConnection.get("liu".getBytes())));
    }
    @Test
    public void TestLock() throws InterruptedException {
        redisTemplate.setEnableTransactionSupport(false);
        CyclicBarrier barrier = new CyclicBarrier(2);
        for(int j=0;j<2;j++){
            threadPoolTaskExecutor.execute(()->{
                        try {
                            LOGGER.info("wating others");
                            barrier.await();
                            LOGGER.info("finish others");
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        } catch (BrokenBarrierException e) {
                            e.printStackTrace();
                        }
                        getLock("mylock");
                        Integer i = redisTemplate.opsForValue().get("luck");
                        if(i==null){
                            redisTemplate.opsForValue().set("luck",1);
                        }else {
                            redisTemplate.opsForValue().set("luck",++i);
                        }
                        LOGGER.info(String.valueOf(redisTemplate.opsForValue().get("luck")));
                        releaseLock("mylock");

                //LOGGER.info(String.valueOf( setLock("luck")));
            });
        }
        Thread.sleep(5000);
        threadPoolTaskExecutor.shutdown();
    }


    @Test
    public void doTestList() {
        //insert a object
        List<String> list = new ArrayList<>();
        list.add("1");
        list.add("2");
        list.add("3");
        list.add("4");
        Collections.shuffle(list);
        redisTemplate2.delete("mylist");
        redisTemplate2.opsForList().rightPushAll("mylist",list);
        List<String> result = redisTemplate2.opsForList().range("mylist",0,999);
        System.out.println(result.toArray());
    }

    @Test
    public void doTestTimeOut() throws InterruptedException {
        List<String> list = new ArrayList<>();
        list.add("1");
        list.add("2");
        list.add("3");
        redisTemplate2.delete("mylist");
        redisTemplate2.opsForList().rightPushAll("mylist",list);
        //redisTemplate2.expire("mylist")
        /*LOGGER.info("the tempplate == "+ redisTemplate1.toString());
        redisTemplate1.opsForValue().set("increTest","1",2,TimeUnit.SECONDS);
        while (redisTemplate1.hasKey("increTest")){
            Long count =redisTemplate1.opsForValue().increment("increTest", 1);
            //redisTemplate1.opsForValue().getOperations().expire("increTest",2,TimeUnit.SECONDS);
            System.out.println(count);
            Thread.sleep(500);
        }*/

        /*redisTemplate3.opsForValue().set("testa",new BigDecimal(32), 1,TimeUnit.SECONDS);
        redisTemplate3.opsForValue().set("testa",new BigDecimal(-100),0);
        while (redisTemplate3.hasKey("testa")){
            LOGGER.info(redisTemplate3.opsForValue().get("testa").toString());
            Thread.sleep(200);
        }
        LOGGER.info("key is delete");*/
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

    private Boolean setLock(String lockKey) {
        SessionCallback<Boolean> sessionCallback = new SessionCallback<Boolean>() {
            List<Object> exec = null;
            @Override
            public Boolean execute(RedisOperations operations) throws DataAccessException {
                operations.multi();
                redisTemplate.opsForValue().setIfAbsent(lockKey,1);
                redisTemplate.expire(lockKey,5, TimeUnit.SECONDS);
                exec = operations.exec();
                if(exec.size() > 0) {
                    return (Boolean) exec.get(0);
                }
                return false;
            }
        };
        return redisTemplate.execute(sessionCallback);
    }

    private void getLock(String lockKey) {
        long waitTime = 10l; //mil
        for(;!setLock(lockKey);){
            LOGGER.info("try lock————————");
            try {
                Thread.sleep(waitTime);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            waitTime = waitTime+10;
        }
    }
    private void releaseLock(String lockKey){
        redisTemplate.delete(lockKey);
    }
}
