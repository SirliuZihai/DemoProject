package com.zihai.h2Client.test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class FutrueTest {
    private static Integer count = 0;
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        List<Future<String>> resultList = new ArrayList<Future<String>>();

        ExecutorService pool = Executors.newSingleThreadExecutor();
        for(int i=0;i<10;i++){
            FutureTask<String> futureTask = new FutureTask<>(new Callable<String>() {
                @Override
                public String call() throws Exception {
                    Thread.sleep(1000);
                    synchronized (count){
                        return "ok"+(++count);
                    }
                }
            });
            pool.submit(futureTask);
            resultList.add(futureTask);
        }
        pool.shutdown();
       // 遍历任务的结果
        for (Future<String> fs : resultList) {
            try {
                System.out.println(fs.get()); // 打印各个线程（任务）执行的结果
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
