package com.zihai.h2Client.test;

import org.springframework.scheduling.support.CronSequenceGenerator;

import java.util.Date;
import java.util.TimeZone;

public class CronTest {
    public static void main(String[] args) {
        String Cron = "0 0 21 ? * MON-FRI";
        Cron = "0 0 1 1 1 *";
        CronSequenceGenerator cronSequenceGenerator = new CronSequenceGenerator(Cron, TimeZone.getTimeZone("America/New_York"));
        Date date = new Date();
        for (int i = 0; i < 15; i++) {
            date = cronSequenceGenerator.next(date);//下次执行时间
            System.out.println(date);
        }


    }
}
