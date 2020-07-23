package com.zihai.h2Client.test;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;

import static java.time.LocalDateTime.now;

public class CalcuTest {
    public static void main(String[] args) {
        DateTimeFormatter sf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime startTime = LocalDateTime.parse("2020-05-01 00:00:00", sf);
        LocalDateTime endTime = LocalDateTime.parse("2020-05-02 23:30:00", sf);

        Duration duration = Duration.between(startTime, endTime);
        long todayhour = Math.round((duration.getSeconds()+1)/3600);
        System.out.println(duration.getSeconds());
        long f2 = Math.floorDiv(duration.getSeconds(),3600l);
        BigDecimal bigDecimal = new BigDecimal(duration.getSeconds());
        BigDecimal result = bigDecimal.divide(new BigDecimal(3600),BigDecimal.ROUND_HALF_UP);
        System.out.println(result.floatValue());

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        //获取当前月第一天：
        Calendar c = Calendar.getInstance();
        c.set(Calendar.DAY_OF_MONTH,1);//设置为1号,当前日期既为本月第一天
        String first = format.format(c.getTime()).substring(0,10);

        //获取当前月最后一天
        Calendar ca = Calendar.getInstance();
        ca.set(Calendar.DAY_OF_MONTH, ca.getActualMaximum(Calendar.DAY_OF_MONTH));
        String last = format.format(ca.getTime()).substring(0,10);
        LocalDateTime firstDay = LocalDateTime.parse(first+" 00:00:00",sf);
        LocalDateTime lastDay = LocalDateTime.parse(last+" 23:30:00",sf);
        System.out.println(firstDay);
        System.out.println(lastDay);
        Float hour = new BigDecimal(duration.getSeconds()).divide(new BigDecimal(3600),2,BigDecimal.ROUND_HALF_UP).floatValue();
               // .divide(new BigDecimal(24),2,BigDecimal.ROUND_HALF_UP);
                //.multiply(new BigDecimal(9))
        System.out.println(lastDay.getHour());

    }
}
