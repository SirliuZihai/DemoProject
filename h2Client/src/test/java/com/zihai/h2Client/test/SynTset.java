package com.zihai.h2Client.test;

import java.util.concurrent.ConcurrentHashMap;

public class SynTset {
    static final Object luck = new Object();
    static ConcurrentHashMap<String,Integer> countMap= new ConcurrentHashMap<>();
    public static void main(String[] args) {
        countMap.put("test",0);
        Thread t1 = new Thread(){
            @Override
            public void run(){
                for(int i=0;i<100;i++){
                    synchronized (countMap){
                        Integer c = countMap.get("test");
                        countMap.put("test",++c);
                        System.out.println(c);
                    }
                }

            }
        };
        t1.start();
        Thread t2 = new Thread(){
            @Override
            public void run(){
                for(int i=0;i<100;i++){
                    synchronized (countMap){
                        Integer c = countMap.get("test");
                        countMap.put("test",++c);
                        System.out.println(c);
                    }
                }
            }
        };
        t2.start();
    }
}
