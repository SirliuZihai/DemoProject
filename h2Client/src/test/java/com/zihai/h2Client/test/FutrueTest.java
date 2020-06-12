package com.zihai.h2Client.test;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

public class FutrueTest {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        FutureTask<String> futureTask = new FutureTask<>(new Callable<String>() {
            @Override
            public String call() throws Exception {
                Thread.currentThread().sleep(10000);
                return "ok";
            }
        });
        futureTask.run();
        System.out.println(futureTask.get());
    }
}
