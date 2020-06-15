package com.zihai.websocket.client;

import org.apache.log4j.Logger;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
@Component
public class InternalTask {
    private static final Logger LOGGER = Logger.getLogger(InternalTask.class);

    @Scheduled(fixedRate = 3000,initialDelay=1000)
    public void heart(){
        LOGGER.info("send: 我是hui ");
        InternalClient.clientEndPoint.sendMessage("我是hui");
    }
}
