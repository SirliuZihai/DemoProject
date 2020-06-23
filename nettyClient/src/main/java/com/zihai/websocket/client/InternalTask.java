package com.zihai.websocket.client;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
@Component
@EnableAsync
@EnableScheduling
public class InternalTask {
    private static final Logger LOGGER = Logger.getLogger(InternalTask.class);
    @Autowired
    private WebsocketHandle websocketHandle;

    @Scheduled(fixedDelay = 50000,initialDelay=10000)
    public void heart(){
        for(int i=1;i<=10000;i++)
            websocketHandle.sendMessage("庆祝我校一百周年纪念日，我是无敌小huihui哈哈哈哈"+i);
        LOGGER.info("heart finished");
    }
}
