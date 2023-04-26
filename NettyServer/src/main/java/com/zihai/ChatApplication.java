package com.zihai;

import com.zihai.websocket.netty.WebSocketChatServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

import javax.annotation.Resource;

/**
 **  程序入口
 */
//@EnableDubbo
@SpringBootApplication
@EnableAutoConfiguration
@ComponentScan("com.zihai")
public class ChatApplication implements ApplicationRunner {

    private static Logger logger = LoggerFactory.getLogger(ChatApplication.class);

    @Resource(name = "webSocketChatServer")
    private WebSocketChatServer webSocketChatServer;


    public static void main(String[] args) {
        SpringApplication.run(ChatApplication.class, args);
        logger.info("netty server start up");
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        try {
            webSocketChatServer.start();
            Thread.currentThread().join();
        } catch (Exception e) {
            logger.error("startup error!", e);
        }
    }
}
