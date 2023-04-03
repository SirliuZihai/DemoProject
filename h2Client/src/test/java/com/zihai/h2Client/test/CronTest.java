package com.zihai.h2Client.test;

import org.springframework.scheduling.support.CronSequenceGenerator;

import java.util.Date;
import java.util.TimeZone;
import java.util.concurrent.ScheduledExecutorService;

public class CronTest {
    private ScheduledExecutorService scheduledExecutorService;
    public static void main(String[] args) {
        String Cron = "0 0 21 ? * MON-FRI";

        Cron = "0/30 30-58 8 ? * *";
        Cron = "0 0 9 ? * *";
        CronSequenceGenerator cronSequenceGenerator = new CronSequenceGenerator(Cron, TimeZone.getDefault());
        Date date = new Date();
        for (int i = 0; i < 5; i++) {
            date = cronSequenceGenerator.next(date);//下次执行时间
            System.out.println(date);
        }
    }
}
