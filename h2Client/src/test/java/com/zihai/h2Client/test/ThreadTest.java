package com.zihai.h2Client.test;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ThreadTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(ThreadTest.class);
    private ThreadLocal<String> mame = new ThreadLocal<>();

    public static void main(String[] args) {


       /* Mythread myt = new Mythread();
        new Thread(myt).start();
        MDC.put("name", "liu");
        new Thread(new SubTask() {
            @Override
            public void action() {
                LOGGER.info("{}", MDC.get("name"));
            }
        }).start();*/
    }
}

class Mythread implements Runnable {
    private static final Logger LOGGER = LoggerFactory.getLogger(Mythread.class);
    private InheritableThreadLocal<String> t = new InheritableThreadLocal<>();
    private ThreadLocal<String> t3 = new ThreadLocal<>();

    @Override
    public void run() {
        t.set("haha");
        t3.set("haha3");
        new Thread(new Runnable() {
            @Override
            public void run() {
                LOGGER.info(t.get() + " " + t3.get());
                t.set("gobu");
            }
        }).start();
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        LOGGER.info(t.get() + " " + t3.get());
    }
}
