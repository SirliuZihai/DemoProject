package com.zihai.h2Client.controller;

import com.zihai.h2Client.Listener.NetWorkEvent;
import com.zihai.h2Client.dto.Result;
import com.zihai.h2Client.dto.TestDto;
import com.zihai.h2Client.service.TestService;
import com.zihai.h2Client.util.Constant;
import com.zihai.h2Client.util.JsonHelp;
import com.zihai.h2Client.util.SpringBeanUtil;
import com.zihai.spi.service.ExtendService;
import com.zihai.spi.service.SpiService;
import org.apache.commons.io.IOUtils;
import org.beetl.core.lab.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.util.Date;
import java.util.List;

@RestController
public class TestController {
    private static final Logger LOGGER = LoggerFactory.getLogger(TestController.class);

    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private SpiService spiService;
    @Autowired
    private ExtendService extendService;
    @Autowired
    private TestService testService;
    @Value("${name}")
    private String name;
    @Value("${age}")
    private Integer age;


    @RequestMapping(value = {"testPost","testGet"},method = RequestMethod.POST)
    public TestDto testPost(@RequestBody TestDto dto){
        LOGGER.info(JsonHelp.gson.toJson(dto));
        return dto;
    }
    @PostMapping("testArray")
    public String testPost(String userId,@RequestBody List<String> months){
        LOGGER.info("userId=={},mouths={}",userId,JsonHelp.gson.toJson(months));
        return "ok";
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
    @GetMapping("testSql")
    public String testSql(@RequestBody String object) throws IOException {
        return testService.testSql();
    }
    @GetMapping("refresh")
    public String refresh(@RequestBody String object) throws IOException {
        try {
            testService.updateTest();
        }catch (Exception e){
            LOGGER.error(e.getMessage());
        }
        //return SpiFactory.getService().getName();
        return spiService.getName()+"  " + extendService.getLogo();
    }
    @GetMapping("apitest2")
    public Date apitest(@RequestParam Date start) throws InterruptedException {
        LOGGER.info("start={}",start);
        return start;
    }
    @PostMapping("apitest2")
    public TestDto apitest2(@RequestBody TestDto testDto) throws InterruptedException {
        LOGGER.info("start={}",testDto);
        return testDto;
    }

    @GetMapping("stopWeb")
    public String stopWeb(){
        LOGGER.info("begin stop");
        SpringBeanUtil.getApplicationContext().publishEvent(new NetWorkEvent(Constant.LISTENER_EVENT_TYPE.STOP));
        LOGGER.info("end stop");
        return "shutdown ok";
    }

    @GetMapping("converter")
    public TestDto stopWeb2(){
        TestDto dto = new TestDto();
        dto.setMoney(new BigDecimal("1"));
        return dto;
    }
    @PostMapping("getRsouce")
    public void stopWeb2(HttpServletResponse response) throws UnsupportedEncodingException {
        InputStream in = this.getClass().getClassLoader().getResourceAsStream("application.yml");
        response.reset();
        response.setContentType("application/octet-stream");
        // 如果输出的是中文名的文件，在此处就要用URLEncoder.encode方法进行处理
        response.setHeader("Content-Disposition",
                "attachment;filename=" + URLEncoder.encode("配置文件.yml","GBK"));
        try (OutputStream fileOut = response.getOutputStream()) {
            fileOut.write(IOUtils.toByteArray(in));
        }catch (Exception e) {
            e.printStackTrace();
        }
    }
    @GetMapping("test/cache")
    public Result testCache(String id){
        return Result.success(testService.testCache(id));
    }

    @GetMapping("test/cacheEvict")
    public Result testCacheEvict(String id){
        testService.testCacheEvict(id);
        return Result.success("ok");
    }
}
