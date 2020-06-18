package com.zihai.websocket.client;

import org.apache.log4j.Logger;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
@Component
public class InternalTask {
    private static final Logger LOGGER = Logger.getLogger(InternalTask.class);

    @Scheduled(fixedDelay = 5000,initialDelay=10000)
    public void heart(){
        for(int i=1;i<=1000;i++)
            InternalClient.clientEndPoint.sendMessage("庆祝我校一百周年纪念日，我是无敌小huihui哈哈哈哈"+i);
        LOGGER.info("heart finished");
    }
}
