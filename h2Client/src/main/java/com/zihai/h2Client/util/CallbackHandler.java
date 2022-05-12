package com.zihai.h2Client.util;

import org.slf4j.MDC;

import java.util.Map;

public abstract class CallbackHandler {
    Map<String, String> copyOfContextMap = MDC.getCopyOfContextMap();

    public void callBackhandler(Object... args) {
        MDC.setContextMap(copyOfContextMap);
        if (args.length > 0) {
            successHandler(args);
        } else {
            timeOutHandler();
        }
    }

    public abstract void successHandler(Object... args);

    public abstract void timeOutHandler();

}