package com.zihai.h2Client.service.impl;

import com.zihai.h2Client.service.StrategyService;

public class StrategyServiceImpl extends StrategyService {
    public static void main(String[] args) throws InterruptedException {
        StrategyService s = new StrategyServiceImpl();
        s.start();
        Thread.sleep(3000);
        s.resume();
        Thread.sleep(50000);
    }
}
