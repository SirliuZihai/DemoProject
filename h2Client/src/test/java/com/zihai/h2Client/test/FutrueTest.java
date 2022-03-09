package com.zihai.h2Client.test;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.*;

public class FutrueTest {
    private static volatile Integer count = 0;
    static LinkedBlockingQueue<Integer> queue = new LinkedBlockingQueue<>();

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        FutureTest();
    }
    static void FutureTest(){
        CompletableFuture<String> f = new CompletableFuture<>();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                f.complete("hahajing");
            }
        }).start();
        try {
            System.out.println(f.get(5,TimeUnit.SECONDS));
            System.out.println(f.get(5,TimeUnit.SECONDS));
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        }
    }
    void TestCyclic(){
        CyclicBarrier cyclicBarrier = new CyclicBarrier(5);
        for (int i = 0; i < 5; i++) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        cyclicBarrier.await(); // 等待其它线程
                        for (; ; ) {
                            Thread.sleep(10);
                            System.out.println(Thread.currentThread().getName() + "====" + queue.take());
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (BrokenBarrierException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }
    }
}

