package com.zihai.h2Client.test;

import java.util.concurrent.ConcurrentHashMap;

public class MapTest {
    private static ConcurrentHashMap<String,Object> map = new ConcurrentHashMap<>();
    public static void main(String[] args) {
        System.out.println(System.getProperty("server.ip"));

        Thread thread = new Thread(){
            @Override
            public void run(){
                Object obj = new Object();
                map.put("123",obj);
                synchronized (obj){
                    try {
                        obj.wait(3000);
                        System.out.println("do one");
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
       Thread thread1 = new Thread(){
           @Override
           public void run(){
               System.out.println(map.containsKey("123"));
              Object obj2 =  map.get("123");
              synchronized (obj2){
                  System.out.println("go notify");
                  obj2.notify();
              }
           }
       };
       thread.start();
       thread1.start();
    }
}
