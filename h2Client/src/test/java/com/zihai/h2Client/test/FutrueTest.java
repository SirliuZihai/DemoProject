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
        List<Future<String>> resultList = new ArrayList<Future<String>>();

        ExecutorService pool = Executors.newFixedThreadPool(5);
        for(int i=0;i<1000;i++){
            FutureTask<String> futureTask = new FutureTask<>(new Callable<String>() {
                @Override
                public String call() throws Exception {
                    //Thread.sleep(1);
                    synchronized (count){
                        System.out.println("ok"+(++count)); ;
                        return "return=="+count;
                    }

                }
            });
            pool.submit(futureTask);
            resultList.add(futureTask);
        }
        pool.shutdown();

     /*   for (int i=1;i<=1000;i++){
            queue.add(i);
        }
        CyclicBarrier cyclicBarrier = new CyclicBarrier(5);
        for (int i = 0; i < 5; i++) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        cyclicBarrier.await(); // 等待其它线程
                        for(;;){
                            Thread.sleep(10);
                            System.out.println(Thread.currentThread().getName()+"===="+queue.take());
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (BrokenBarrierException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }*/
       // 遍历任务的结果
        int count2 = 0;
        Set<String> set = new HashSet<>();
        for (Future<String> fs : resultList) {
            try {
                System.out.println(++count2+"  "+fs.get()+" is contain "+set.contains(fs.get())); // 打印各个线程（任务）执行的结果
                set.add(fs.get());
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                pool.shutdownNow();
                e.printStackTrace();
                return;
            }
        }
    }
}

