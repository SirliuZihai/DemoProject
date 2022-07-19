package com.zihai.h2Client.config;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.File;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

/**
 * 定时、启动清理旧数据
 */
@Component
public class FileClean implements InitializingBean {
    String logPath = "/opt/java/logs";
    LocalDateTime DeleteDay;

    @Override
    public void afterPropertiesSet() throws Exception {
        DeleteDay = LocalDateTime.now().minusDays(7);
        File file = new File(logPath);
        deleteOutTime(file);
    }

    @Scheduled(cron = "1 0 0 * * *")
    public void cleanOldFile() throws Exception {
        afterPropertiesSet();
    }


    public void deleteOutTime(File f) {
        if (f.isDirectory()) {
            for (File f1 : f.listFiles()) {
                deleteOutTime(f1);
            }
            if (f.listFiles().length == 0) {
                f.delete();
                return;
            }
        } else {
            LocalDateTime filedate = LocalDateTime.ofInstant(Instant.ofEpochMilli(new BigDecimal(f.lastModified()).longValue()), ZoneId.systemDefault());
            if (filedate.isBefore(DeleteDay)) {
                f.delete();
            }
        }
    }
}
