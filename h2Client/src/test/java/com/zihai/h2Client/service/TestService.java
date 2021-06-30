package com.zihai.h2Client.service;

import com.zihai.h2Client.annotation.BusMethod;
import com.zihai.h2Client.annotation.BusService;

@BusService
public class TestService {
    @BusMethod("test")
    public String doBus(String arg){
        return "get="+arg;
    }
}
