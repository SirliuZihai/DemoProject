package com.zihai.h2Client.aop;

import com.zihai.h2Client.service.AdviceService;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class MyAop {
    final static Logger LOGGER = LoggerFactory.getLogger(MyAop.class);


}
