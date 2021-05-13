package com.zihai.h2Client.test;

import com.zihai.dto.People;
import com.zihai.h2Client.util.JsonHelp;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class StreamTest {
    public static void main(String[] args) {
        AtomicInteger i = new AtomicInteger(0);
        List<People> list = new ArrayList<>();
        list.add(new People("liu",2));
        list.add(new People("liu",3));
        list.add(new People("liu",4));
        Map<String,String> names = list.stream().map(People::getName).collect(
                Collectors.toMap(e->e, e->String.valueOf(i.incrementAndGet()),(v1,v2)->v2)
        );
        System.out.println(JsonHelp.gson.toJson(names).toString());

    }
}

