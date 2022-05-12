package com.zihai.h2Client.work;

import com.zihai.h2Client.util.JsonUtils;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class Choujiang {
    @Test
    public void rand() {
        Random random = new Random();
        for (int i = 0; i < 100; i++) {
            System.out.println(random.nextInt(100));
        }

    }

    @Test
    public void work2() {
        for (int i = 0; i < 5; i++) {
            work();
        }
    }

    @Test
    public void work() {
        ArrayList<String> arr = new ArrayList<>();
        for(int i=0;i<1;i++)
            arr.add("一等奖");
        for(int i=0;i<2;i++)
            arr.add("二等奖");
        for(int i=0;i<6;i++)
            arr.add("三等奖");
        Collections.shuffle(arr);
        System.out.println(JsonUtils.toJsonString(arr));
    }


}
