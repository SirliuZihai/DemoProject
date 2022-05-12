package com.zihai.h2Client.util;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;


public class RequestUtil {
    public static Timer timer = new Timer();

    private static ConcurrentHashMap<Long, CallbackHandler> callbackHandlerMap = new ConcurrentHashMap<>();


    public static void doCallBack(Long requestId, Object... args) {
        try {
            CallbackHandler handle = callbackHandlerMap.remove(requestId);
            if (handle != null)
                handle.callBackhandler(args);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 请求回调注册
     *
     * @param reqId
     * @param handler
     */
    public static void registCallBackHandler(Long reqId, CallbackHandler handler) {
        callbackHandlerMap.put(reqId, handler);
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                doCallBack(reqId);
            }
        }, 30000);
    }


}
