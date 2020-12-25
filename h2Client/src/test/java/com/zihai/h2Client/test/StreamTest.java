package com.zihai.h2Client.test;

import com.zihai.h2Client.util.JsonHelp;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.BinaryOperator;
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
class People{
   private String name;
   private int age;
    public People(String name,int age){
        this.age = age;
        this.name = name;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }
}
