package com.zihai.base.service.impl;

import com.zihai.base.service.TestService;
import org.apache.dubbo.config.annotation.Service;

@Service
public class TestServiceIml implements TestService {
    @Override
    public String sayHello() {
        return "goood";
    }
}
