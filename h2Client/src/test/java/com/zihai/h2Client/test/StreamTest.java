package com.zihai.h2Client.test;

import com.zihai.h2Client.dto.People;
import com.zihai.h2Client.util.JsonHelp;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class StreamTest {
    public static void main(String[] args) {
        test3();

    }

    static void test1() {
        List<Integer> list = new ArrayList<>();
        list.add(null);
        list.add(1);
        System.out.println(list.stream().filter(e -> e == 1).findFirst().get());
    }

    static void test2() {
        AtomicInteger i = new AtomicInteger(0);
        List<People> list = new ArrayList<>();
        list.add(new People("liu", 2));
        list.add(new People("liu", 3));
        list.add(new People("liu", 4));
        Map<String, String> names = list.stream().map(People::getName).collect(
                Collectors.toMap(e -> e, e -> String.valueOf(i.incrementAndGet()), (v1, v2) -> v2)
        );
        System.out.println(JsonHelp.gson.toJson(names).toString());
    }

    static void test3() {
        AtomicInteger i = new AtomicInteger(0);
        List<People> list = new ArrayList<>();
        list.add(new People("liu", 2));
        list.add(new People("liu", 3));
        list.add(new People("liu", 4));

        System.out.println(JsonHelp.gson.toJson(list));
        List<People> l2 = list.stream().filter(e -> e.getAge() == 3).collect(Collectors.toList());
        l2.remove(l2.get(0));
        System.out.println(JsonHelp.gson.toJson(list));
    }
}

