package com.zihai.websocket.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class InternalTask {
    private static final Logger LOGGER = LoggerFactory.getLogger(InternalTask.class);
    @Autowired
    private WebsocketHandle websocketHandle;

    @Scheduled(fixedDelay = 10000,initialDelay=2000)
    public void heart(){
        LOGGER.info("heart start");
        for(int i=1;i<=10;i++){
            MDC.put("userId","lyz");
            websocketHandle.sendMessage("庆祝我校一百周年纪念日，我是无敌小huihui哈哈哈哈"+i);
        }

        LOGGER.info("heart finished");
    }

}
