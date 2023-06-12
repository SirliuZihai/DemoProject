package com.zihai.h2Client.Listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.event.ApplicationFailedEvent;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.boot.context.event.ApplicationStartingEvent;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class SpringListener implements ApplicationListener<ApplicationEvent> {
    private static final Logger LOGGER = LoggerFactory.getLogger(SpringListener.class);

    @Override
    @Async
    public void onApplicationEvent(ApplicationEvent event) {
        if (event instanceof NetWorkEvent) {
            NetWorkEvent netWorkEvent = (NetWorkEvent) event;
            netWorkEvent.handle();//绑定注册到云端
        }
        if (event instanceof ApplicationStartingEvent) {
            LOGGER.info("ApplicationStartingEvent start");
        }
        if (event instanceof ApplicationReadyEvent) {
            LOGGER.info("ApplicationReadyEvent start");
        }
        if (event instanceof ApplicationFailedEvent) {
            LOGGER.info("ApplicationFailedEvent start");
        }
    }

}
