package com.zihai.h2Client;

import org.mybatis.spring.annotation.MapperScan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.transaction.annotation.EnableTransactionManagement;


//@EnableTransactionManagement 与 @Configure 关联
@EnableCaching
@MapperScan("com.zihai.h2Client.dao")
@EnableAsync
@SpringBootApplication
public class App {
    public static Logger logger = LoggerFactory.getLogger(App.class);
    public static ConfigurableApplicationContext context;
    public static void main(String[] args) {
        context = SpringApplication.run(App.class, args);
        Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
            @Override
            public void run() {
                logger.info("do hook");;
            }
        }));
        logger.info("App Started");
    }
}
