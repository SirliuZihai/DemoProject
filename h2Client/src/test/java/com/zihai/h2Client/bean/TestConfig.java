package com.zihai.h2Client.bean;

import com.zihai.h2Client.dao.UserMapper;
import com.zihai.h2Client.dto.People;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
@Configuration
public class TestConfig {
    @Autowired
    UserMapper userMapper;

   @Bean
   public People getPeple(){
       People p = new People(userMapper.selectTest(),23);
       return p;
   }
}
