package com.zihai;

import java.util.Map;
import java.util.Properties;
import java.util.Set;

public class App {
    public static void main(String[] args) {
        Properties pr = System.getProperties();
        for(Map.Entry<Object, Object> entry: pr.entrySet()){
            System.out.println(entry.getKey()+"："+entry.getValue());
        }

    }
}
