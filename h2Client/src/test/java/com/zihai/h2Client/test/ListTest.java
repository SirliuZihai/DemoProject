package com.zihai.h2Client.test;

import com.google.gson.Gson;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ListTest {

    @Test
    public void doList(){
        List<String> list = new ArrayList<>();
        list.add("liu");
        list.add("yi");

        List<String> list2 = new ArrayList<>();
        list2.add("liu");
        list2.add("huang");
        System.out.println(new Gson().toJson(getDifferenceSet(list, list2)));

    }

    public static List<String> getDifferenceSet(List<String> list1, List<String> list2) {
        List<String> reduce1 = list1.stream().filter(item -> !list2.contains(item)).collect(Collectors.toList());
        return reduce1;

    }

    public static void main(String[] args) throws InterruptedException {
        System.out.println(222);
        Stream.iterate(0, i -> i + 1).limit(205 / 50 + 1).parallel().forEach(integer -> System.out.println(integer));
     /*   Stream.iterate(0, i -> i + 1).limit(205 / 50 + 1).map(i -> {
            //collection.parallelStream().skip(i*batchSize).limit(batchSize).collect(Collectors.toList());
            System.out.println(i);
            return null;
        });*/
        Thread.sleep(50000);
    }

    public static void splitBatch(Collection collection, int batchSize) {


    }
}
