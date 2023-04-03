package com.zihai.h2Client.test;

import java.util.ArrayList;
import java.util.Arrays;

public class BaseTest {
    static int i1 = 0;

    //测试 是否支持科令夏令时
    public static void main(String[] args) throws InterruptedException {
        ArrayList<Integer> arrayList = new ArrayList<>();
        int[] arr = {1, 2, 899, 997, 998, 999};
        System.out.println(Arrays.binarySearch(arr, 997));

        System.out.println("woshihui".substring(0, "woshihui".lastIndexOf("kaixin")));

    }
}
