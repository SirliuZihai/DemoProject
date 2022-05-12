package com.zihai.h2Client.test;

public class ClassTest2 {
    public static void main(String[] args) {
        Son son = new Son();
        son.setName("daren");
        son.excute();
    }
}

abstract class Father {
    private String name;

    Father() {
        System.out.println("father init");
        new Thread(() -> {
            while (true) {

                excute();
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
    public void excute(){
        System.out.println("do father"+getName());
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
class Son extends Father{
    @Override
    public void excute(){
        System.out.println("do son"+getName());
        // super.excute();
    }
}