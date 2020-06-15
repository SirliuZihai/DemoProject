package com.zihai;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

import java.util.Map;
import java.util.Properties;
import java.util.Set;
@SpringBootApplication
@EnableAutoConfiguration
@ComponentScan("com.zihai")
public class App {
    public static void main(String[] args) {
            SpringApplication.run(App.class, args);
    }
}
