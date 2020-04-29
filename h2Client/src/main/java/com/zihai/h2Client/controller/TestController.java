package com.zihai.h2Client.controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
public class TestController {
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @RequestMapping("getH2")
    public List getH2(){
        return jdbcTemplate.queryForList("select  * from user");
    }
    @RequestMapping("saveH2")
    public String saveH2(){
        jdbcTemplate.execute("INSERT INTO USER (USE_ID,USE_NAME,USE_SEX,USE_AGE,USE_ID_NO,USE_PHONE_NUM,USE_EMAIL,CREATE_TIME,MODIFY_TIME,USE_STATE) VALUES(4,'孙四','2',24,'142323198610051237','12345678912','qe259@165.com',sysdate,sysdate,'0')");
        return "sucess";
    }
}
