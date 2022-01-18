package com.zihai;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;


@SpringBootApplication
@EnableAutoConfiguration
@ComponentScan("com.zihai")
@EnableScheduling
@EnableAsync
public class App {
    public static void main(String[] args) {
            SpringApplication.run(App.class, args);
    }
}
