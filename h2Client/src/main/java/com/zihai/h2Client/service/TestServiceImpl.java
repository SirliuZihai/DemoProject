package com.zihai.h2Client.service;

import com.google.gson.Gson;
import com.zihai.h2Client.controller.TestController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Transactional
@Service
public class TestServiceImpl implements TestService {
    private static final Logger LOGGER = LoggerFactory.getLogger(TestController.class);

    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Override
    public List doSomething() {
        Map<String,Object> obj = jdbcTemplate.queryForList("select  * from visitor where name = 'LYZ'").get(0);
        jdbcTemplate.update("update visitor set remark_company = ? where id = ?","22",obj.get("id"));
        LOGGER.info(new Gson().toJson(jdbcTemplate.queryForList("select  * from visitor where name = 'LYZ'").get(0)));
        throw new RuntimeException("sf");
        //return jdbcTemplate.queryForList("select  * from visitor where name = 'LYZ'");
    }
}
