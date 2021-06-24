package com.zihai.service;

import com.zihai.annotation.BusMethod;
import com.zihai.annotation.BusService;

@BusService
public class TestService {
    @BusMethod("test")
    public String doBus(String arg){
        return "get="+arg;
    }
}
