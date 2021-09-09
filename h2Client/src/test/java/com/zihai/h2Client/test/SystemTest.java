package com.zihai.h2Client.test;

import com.google.gson.Gson;

import java.util.Map;
import java.util.Properties;

//java -Dmyname=liu com.zihai.h2Client.test.SystemTest keli shi hao ren
public class SystemTest {
    public static void main(String[] args) {
        System.out.println(new Gson().toJson(args));
        System.out.println("**************************");
       Properties properties =  System.getProperties();
       for(Map.Entry entry :properties.entrySet()){
           System.out.printf("%s:%s\n",entry.getKey(),entry.getValue());
       }
        System.out.println("**************************");
       for(Map.Entry entry :System.getenv().entrySet()){
           System.out.printf("%s:%s\n",entry.getKey(),entry.getValue());
       }
    }
}
