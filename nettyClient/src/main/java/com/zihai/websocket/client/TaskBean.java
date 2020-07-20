package com.zihai.websocket.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicInteger;

@Component
public class TaskBean {
    private static final Logger LOGGER = LoggerFactory.getLogger(TaskBean.class);

    @Async
    public String dealMessage(String message,AtomicInteger count) throws InterruptedException {
        synchronized (this){
            LOGGER.info("count: {} cosumer {}",count.incrementAndGet(),message);
            return "";
        }
    }
}
