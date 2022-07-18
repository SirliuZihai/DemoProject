package com.zihai.h2Client.specialTest;

public class IfTest {
    public static void main(String[] args) {
        judge("Hydra");
    }

    public static void judge(String param) {
        if (param == null ||
                new IfTest() {{
                    IfTest.judge(null);
                }}.equals("Hydra")) {
            System.out.println("step one");
        } else {
            System.out.println("step two");
        }
    }
}