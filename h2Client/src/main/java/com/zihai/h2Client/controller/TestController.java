package com.zihai.h2Client.controller;

import com.zihai.h2Client.dto.TestDto;

import com.zihai.h2Client.service.TestService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class TestController {
    private static final Logger LOGGER = LoggerFactory.getLogger(TestController.class);

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private TestService testService;
    @Value("${name}")
    private String name;
    @Value("${age}")
    private Integer age;

    private String mongoUrl;

    @Value("${mongoUrl}")
    public void setMongoUrl(String mongoUrl){
        this.mongoUrl =mongoUrl;
    }

    @RequestMapping(value = "testPost",method = RequestMethod.POST)
    public String testPost(TestDto dto){
        LOGGER.info("testPost"+name+age);
        return "testPost"+dto.getName()+dto.getAge();
    }
    @RequestMapping("getVisitor")
    public List getVisitor(){
        LOGGER.info(name+age);

        try {
            return testService.doSomething();
        } catch (Exception e) {
            return jdbcTemplate.queryForList("select  * from visitor where name = 'LYZ'");
        }
    }
    @RequestMapping("getH2")
    public List getH2(){
        LOGGER.info(name+age);
        return jdbcTemplate.queryForList("select  * from user");
    }
    @RequestMapping("saveH2")
    public String saveH2(){
        jdbcTemplate.execute("INSERT INTO USER (USE_ID,USE_NAME,USE_SEX,USE_AGE,USE_ID_NO,USE_PHONE_NUM,USE_EMAIL,CREATE_TIME,MODIFY_TIME,USE_STATE) VALUES(4,'孙四','2',24,'142323198610051237','12345678912','qe259@165.com',sysdate,sysdate,'0')");
        return "sucess";
    }
}
