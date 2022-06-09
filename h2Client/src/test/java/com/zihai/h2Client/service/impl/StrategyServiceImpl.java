package com.zihai.h2Client.service.impl;

import com.zihai.h2Client.service.StrategyService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class StrategyServiceImpl extends StrategyService {
    private static final Logger LOGGER = LoggerFactory.getLogger(StrategyServiceImpl.class);

    public static void main(String[] args) throws InterruptedException {
        StrategyServiceImpl s = new StrategyServiceImpl();
        s.start();
        Thread.sleep(1000);
        for (; ; ) {
            s.noti();
            Thread.sleep(1000);
        }

    }

    @Override
    public void excute() {
        try {
            LOGGER.info("do excurte");
        } finally {
            super.strategyWaite();
        }
    }

    public void noti() {
        super.strategyNotify();
    }
}
