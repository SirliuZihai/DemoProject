package com.zihai.websocket.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
@Component
@EnableAsync
@EnableScheduling
public class InternalTask {
    private static final Logger LOGGER = LoggerFactory.getLogger(InternalTask.class);
    @Autowired
    private WebsocketHandle websocketHandle;
    @Autowired
    private WebsocketHandle2 websocketHandle2;

    @Scheduled(fixedDelay = 10000,initialDelay=2000)
    public void heart(){
        for(int i=1;i<=10;i++){
            websocketHandle.sendMessage("庆祝我校一百周年纪念日，我是无敌小huihui哈哈哈哈"+i);
            websocketHandle2.sendMessage("庆祝我校一百周年纪念日2，我是无敌小huihui2哈哈哈哈"+i);
        }

        LOGGER.info("heart finished");
    }

}
