package com.zihai.h2Client.service;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class AdviceService {
    final static Logger LOGGER = LoggerFactory.getLogger(AdviceService.class);
    public void test(){
        LOGGER.info("test advice");
    }
    @Around("test()")
    Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        String methodName = joinPoint.getSignature().getName();
        LOGGER.info(methodName);
        return joinPoint.proceed();
    }
}
