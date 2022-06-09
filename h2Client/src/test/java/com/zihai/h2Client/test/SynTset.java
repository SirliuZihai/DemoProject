package com.zihai.h2Client.test;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

public class SynTset {
    static Logger logger = LoggerFactory.getLogger(SynTset.class);
    static final Object luck = new Object();
    AtomicBoolean canRenit = new AtomicBoolean(true);
    static ConcurrentHashMap<String, Integer> lucktMap = new ConcurrentHashMap<>();

    public static void main(String[] args) throws InterruptedException {
        CountDownLatch countDownLatch = new CountDownLatch(5);
        for (int i = 0; i < 5; i++) {
            new Thread(() -> {
                System.out.println("count down");
                countDownLatch.countDown();
            }).start();
        }
        new Thread(() -> {
            try {
                countDownLatch.await(5, TimeUnit.SECONDS);
                System.out.println("finish waite");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
        Thread.sleep(2000);
        for (; ; ) {
            new Thread(() -> {
                try {
                    countDownLatch.await(5, TimeUnit.SECONDS);
                    System.out.println("finish waite");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }).start();
            Thread.sleep(2000);
        }


      /*
       唤醒全部线程
      SynTset synTset = new SynTset();
        new Thread(() -> {
            synchronized (synTset) {
                try {
                    synTset.wait();
                    System.out.println("111");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
        new Thread(() -> {
            synchronized (synTset) {
                try {
                    synTset.wait();
                    System.out.println("222");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
        Thread.sleep(3000);
        new Thread(() -> {
            synchronized (synTset) {
                synTset.notifyAll();
                System.out.println("333");
            }
        }).start();*/


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


}

