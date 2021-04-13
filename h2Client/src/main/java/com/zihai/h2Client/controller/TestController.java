package com.zihai.h2Client.controller;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.zihai.h2Client.dto.TestDto;
import com.zihai.h2Client.service.TestService;
import com.zihai.h2Client.util.SpringBeanUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Base64;
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


    @RequestMapping(value = "testPost",method = RequestMethod.POST)
    public TestDto testPost(@RequestBody TestDto dto){
        LOGGER.info("testPost"+name+age);
        return dto;
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
    @RequestMapping("attendance-record")
    public void getVipTest(@RequestBody String object) throws IOException {
        String imag = new Gson().fromJson(object,JsonObject.class).get("capture_img").getAsString();
        new FileOutputStream("D:\\out.jpg").write(Base64.getDecoder().decode(imag));
    }
    @GetMapping("apitest2")
    public String apitest(@RequestParam String info) throws InterruptedException {
        LOGGER.info(info);
        Thread.sleep(100);
        LOGGER.info("apitest2 end");
        return info;
    }

    @GetMapping("stopWeb")
    public void stopWeb(){
        LOGGER.info("begin stop");
        SpringApplication.exit(SpringBeanUtil.getApplicationContext());
        LOGGER.info("end stop");
    }
}
