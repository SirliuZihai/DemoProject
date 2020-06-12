package com.zihai.h2Client.test;

public class ClassTest {
    public static int age = 33;
    public static final Object object = new Object();
    public static final Object object2 = new Object();
    public static void main(String[] args) throws InterruptedException {
        for(int i=0;i<10;i++){
            synchronized (object2) {
                Thread th1 = new Thread(){
                    @Override
                    public void run() {
                        synchronized (object){
                            try {
                                System.out.println("th3 run");
                                object.wait();
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            System.out.println("th1 run");
                            synchronized (object2) {
                                object2.notify();
                            }
                        }
                    }
                };
                th1.start();
                Thread th2 = new Thread(){
                    @Override
                    public void run() {
                        System.out.println("th2 run");
                        synchronized (object){
                            object.notify();
                        }
                    }
                };
                th2.start();
                object2.wait();
            }
        }


    }
}
