package com.zihai.h2Client.test;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.task.TaskExecutor;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

@RunWith(SpringRunner.class)
@EnableAutoConfiguration
@ComponentScan("com.zihai.h2Client")
@SpringBootTest
public class CompareTesst {
    final static Logger LOGGER = LoggerFactory.getLogger(CompareTesst.class);
    @Autowired
    TaskExecutor taskExecutor;
    CyclicBarrier cyclicBarrier = new CyclicBarrier(2);

    @Test
    public void compare() throws InterruptedException {
        for (int i = 0; i < 8; i++) {
            taskExecutor.execute(() -> {
                try {
                    cyclicBarrier.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (BrokenBarrierException e) {
                    e.printStackTrace();
                }
                busines();
            });
        }
        Thread.sleep(100000);
    }

    void busines() {
        int sum = 0;
        for (int i = 1; i <= 1000000; i++) {
            sum += i;
        }
        LOGGER.info("{}", sum);
    }
}
