package com.zihai.h2Client.springTest;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

@RunWith(SpringRunner.class)
@EnableAutoConfiguration
@ComponentScan("com.zihai")
@SpringBootTest
public class SpringTest {

    @Value("${file.picPath}")
    private String picPath;

    @Value("${test.arg}")
    private String arg;

    @Test
    public void test(){
        System.out.println(arg);
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
    class Entity{
       public Integer i;
        Entity(int i){
            this.i = i;
        }
    }
}
