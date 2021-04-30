package com.zihai.h2Client.test;

public class SubClouse implements AutoCloseable {
    @Override
    public void close() throws Exception {
        System.out.println("do sub close");
    }
}
