package com.zihai.h2Client.springTest;

import com.zihai.h2Client.dao.ds1.ProductMapper;
import com.zihai.h2Client.dao.ds2.OplogMapper;
import com.zihai.h2Client.dto.People;
import com.zihai.h2Client.dto.entity.Oplog;
import com.zihai.h2Client.dto.entity.Product;
import com.zihai.h2Client.util.JsonHelp;
import com.zihai.h2Client.util.SubTask;
import io.netty.util.internal.ConcurrentSet;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mybatis.spring.annotation.MapperScan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.data.mongo.MongoRepositoriesAutoConfiguration;
import org.springframework.boot.autoconfigure.data.redis.RedisRepositoriesAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

@EnableScheduling
@RunWith(SpringRunner.class)
@EnableAutoConfiguration(exclude = {RedisRepositoriesAutoConfiguration.class, MongoRepositoriesAutoConfiguration.class })
@ComponentScan("com.zihai.h2Client")
@MapperScan("com.zihai.h2Client.dao")
@SpringBootTest
public class SpringTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(SpringTest.class);

    @Value("${file.picPath}")
    private String picPath;

    //@Autowired
    private OplogMapper oplogMapper;
    //@Autowired
    private ProductMapper productMapper;
    @Autowired
    private People people;
    @Resource
    TaskExecutor taskExecutor;
    @Resource
    ScheduledExecutorService scheduledExecutorService;

    /**
     * 设置系统参数
     */
    @BeforeClass
    public static void setSystemProperty() {
        Properties properties = System.getProperties();
        properties.setProperty("spring.profiles.active", "test");
    }

    @Test
    public void testSchedule() throws InterruptedException {
        ScheduledFuture future = scheduledExecutorService.scheduleWithFixedDelay(() -> System.out.println("mytask"), 1, 3, TimeUnit.SECONDS);
        Thread.sleep(7000);
        future.cancel(true);
        Thread.sleep(50000);
    }

    public static void main(String[] args) {
    }

    @Test
    public void asynTest() throws InterruptedException {
        Set<Integer> s = new ConcurrentSet<>();
        CountDownLatch countDownLatch = new CountDownLatch(100);
        for (int i = 0; i < 100; i++) {
            int finalI = i;
            taskExecutor.execute(new SubTask() {
                @Override
                public void action() {
                    s.add(finalI);
                    countDownLatch.countDown();
                }
            });
        }
        countDownLatch.await(10, TimeUnit.SECONDS);
        System.out.println(s.size());

    }

    @Test
    public void config() {
        LOGGER.info(JsonHelp.gson.toJson(people));
    }

    @Test
    public void test() {
        Oplog oplog = oplogMapper.selectByPrimaryKey(3101);
        System.out.println(JsonHelp.gson.toJson(oplog));

        Product product = productMapper.selectByPrimaryKey(1);
        System.out.println(JsonHelp.gson.toJson(product));
    }


    @Test
    public void test2() throws InterruptedException {
        ThreadPoolTaskScheduler executor = new ThreadPoolTaskScheduler();
        executor.setPoolSize(5);
        executor.setThreadNamePrefix("taskExecutor-");
        executor.setWaitForTasksToCompleteOnShutdown(true);
        executor.setAwaitTerminationSeconds(5);
        executor.initialize();
        CountDownLatch countDownLatch = new CountDownLatch(100);
        for(int i=0 ; i<100; i++){
            String finalI = String.valueOf(i);
            executor.execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(1000);
                        countDownLatch.countDown();
                    } catch (InterruptedException ex) {
                        ex.printStackTrace();
                    }
                    System.out.println("get the integer =="+ finalI);
                }
            });
        }
        countDownLatch.await();
    }
}
