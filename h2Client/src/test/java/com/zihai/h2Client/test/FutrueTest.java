package com.zihai.h2Client.test;

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
                    Thread.sleep(4000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                f.complete("hahajing");
            }
        }).start();
        try {
            System.out.println(f.get(5, TimeUnit.SECONDS));
            System.out.println(f.get(5, TimeUnit.SECONDS));
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        }
    }

    static void TestCyclic() {
        for (int i = 0; i < 5; i++) {
            queue.add(i);
        }
        CyclicBarrier cyclicBarrier = new CyclicBarrier(5, () -> System.out.println("after Barrier"));
        for (int i = 0; i < 5; i++) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        System.out.println(Thread.currentThread().getName() + "start await");
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

