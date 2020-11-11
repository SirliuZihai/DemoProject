package com.zihai.h2Client.springTest;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@EnableAutoConfiguration
@ComponentScan("com.zihai")
@SpringBootTest
public class SpringTest {
    @Value("${file.picPath}")
    private String picPath;

    @Test
    public void test(){
        System.out.println(picPath);
    }
}
