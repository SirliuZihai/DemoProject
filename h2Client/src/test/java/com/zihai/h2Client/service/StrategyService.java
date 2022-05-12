package com.zihai.h2Client.service;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class StrategyService {
    public static final Logger LOGGER = LoggerFactory.getLogger(StrategyService.class);

    public void start() {
        new Thread(() -> {
            while (true) {
                synchronized (this) {
                    LOGGER.info("wait obj == {}", this);
                    try {
                        this.wait();
                        LOGGER.info("{} be notifid", this);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

    public void resume() {
        synchronized (this) {
            LOGGER.info("notify obj == {}", this);
            this.notifyAll();
        }
    }
}
