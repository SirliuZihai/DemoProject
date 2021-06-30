package com.zihai.h2Client.test;

public class MathTest {
    public static void main(String[] args) {
        MathTest mathTest = new MathTest();
        for(int i=0;i<100;i++){
            if(mathTest.chu2(i)!=+mathTest.chu2Shift(i))
                System.out.println(mathTest.chu2(i)+" and " +mathTest.chu2Shift(i));

        }
    }

    private int chu2(int i){
        return i/2;
    }
    private int chu2Shift(int i){
        return i>>1;
    }
}
