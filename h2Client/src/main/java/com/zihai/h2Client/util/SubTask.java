package com.zihai.h2Client.util;

import org.slf4j.MDC;

import java.util.Map;

public abstract class SubTask implements Runnable{
    Map<String, String> copyOfContextMap = MDC.getCopyOfContextMap();

    @Override
    public void run() {
        MDC.setContextMap(copyOfContextMap);
        action();
    }
    public abstract void action();
}
