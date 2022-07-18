package com.zihai.h2Client.service;

import com.zihai.h2Client.dao.UserMapper;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

@Component
public class TaskService {
    @Resource
    UserMapper userMapper;

    @Scheduled(cron = "0 0 9 * * *")
    @Transactional
    public void schedulWok() {
        userMapper.updateTest();
    }
}
