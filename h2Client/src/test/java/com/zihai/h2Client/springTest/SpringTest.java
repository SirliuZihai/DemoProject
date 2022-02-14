package com.zihai.h2Client.springTest;

import com.zihai.h2Client.core.entity.Oplog;
import com.zihai.h2Client.core.entity.Product;
import com.zihai.h2Client.dao.ds1.ProductMapper;
import com.zihai.h2Client.dao.ds2.OplogMapper;
import com.zihai.h2Client.dto.People;
import com.zihai.h2Client.util.JsonHelp;
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
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.concurrent.CountDownLatch;

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

    @Test
    public void config(){
        LOGGER.info(JsonHelp.gson.toJson(people));
    }

    @Test
    public void test(){
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
        for(int i=0 ;i<100;i++){
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
