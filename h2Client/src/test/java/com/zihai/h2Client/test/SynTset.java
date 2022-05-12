package com.zihai.h2Client.test;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;

public class SynTset {
    static Logger logger = LoggerFactory.getLogger(SynTset.class);
    static final Object luck = new Object();
    AtomicBoolean canRenit = new AtomicBoolean(true);
    static ConcurrentHashMap<String, Integer> lucktMap = new ConcurrentHashMap<>();
    public static void main(String[] args) throws InterruptedException {
        SynTset synTset = new SynTset();
        new Thread(() -> {
            while (true) {
                synTset.reInit();
            }

        }).start();
        new Thread(() -> {
            while (true) {
                synTset.reInit();
            }
        }).start();
        new Thread(() -> {
            while (true) {
                synTset.reInit();
            }
        }).start();
        synchronized (luck) {
            luck.wait(30000);
        }

      /*  for(int i=0;i<100;i++){
            Object lock = new Object();
            Thread t1 = new Thread(){
                @Override
                public void run(){
                    synchronized (lock){
                        try {
                            Thread.sleep(300);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        lock.notify();
                    }

                }

            };
            t1.start();
            synchronized (lock){
                lock.wait(1);
            }
            System.out.println("curent i ==" + i);
        }*/
    }

    public synchronized void reInit() {
        // 【5、平台接口-初始化链接】
        if (canRenit.get()) {
            canRenit.set(false);
            try {
                logger.info(" enter method");
                Thread.sleep(1000);
            } catch (Exception e) {
            } finally {
                canRenit.set(true);
            }
        }

    }
}
