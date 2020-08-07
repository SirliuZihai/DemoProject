package com.zihai.h2Client.test;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ThreadPoolExecutor;

public class SynTset {
    static final Object luck = new Object();
    static ConcurrentHashMap<String,Integer> lucktMap= new ConcurrentHashMap<>();
    public static void main(String[] args) throws InterruptedException {
        for(int i=0;i<100;i++){
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
        }


    }
}
