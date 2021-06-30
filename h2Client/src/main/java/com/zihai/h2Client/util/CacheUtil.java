package com.zihai.h2Client.util;

import org.springframework.cache.concurrent.ConcurrentMapCache;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;

public class CacheUtil {
    static Timer timer = new Timer();
    static ConcurrentHashMap<String, Object> cahcheMap = new ConcurrentHashMap<>();

    public static boolean putIfabsent(String key, Object value, long delaytime) {
        if (cahcheMap.containsKey(key))
            return false;
        cahcheMap.put(key, value);
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                cahcheMap.remove(key);
            }
        }, delaytime);
        return true;
    }

    public static void main(String[] args) throws InterruptedException {
        //imer.cancel();
        System.out.println(1);
        timer.cancel();
        ConcurrentMapCache concurrentMapCache = new ConcurrentMapCache("test");
        //concurrentMapCache.put();
        //System.exit(0);
    }

}
