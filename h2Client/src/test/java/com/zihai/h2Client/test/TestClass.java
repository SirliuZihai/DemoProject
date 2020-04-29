package com.zihai.h2Client.test;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;

public class TestClass {
    public static void main(String[] args) throws IOException {
        TestClass test = new TestClass();
        String path =TestClass.class.getClassLoader().getResource("").getPath()+"db/";
        String context = FileUtils.readFileToString(new File(path+"data.sql"));
        System.out.println(context);
    }
}
