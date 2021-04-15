package com.zihai.h2Client.filter;

import com.zihai.h2Client.Listener.FileListener;
import org.apache.commons.io.filefilter.FileFilterUtils;
import org.apache.commons.io.monitor.FileAlterationMonitor;
import org.apache.commons.io.monitor.FileAlterationObserver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.annotation.PreDestroy;
import java.io.File;

@Component
@Order(7)
public class TestFilter  implements ApplicationContextAware,CommandLineRunner {
    private static Logger logger= LoggerFactory.getLogger(TestFilter.class);
    private ApplicationContext applicationContext;
    private FileAlterationMonitor monitor = new FileAlterationMonitor(1000L);
    @Override
    public void run(String... args) throws Exception {
        logger.info(this.getClass().getSimpleName()+" runing………………");
        String path = getClass().getClassLoader().getResource("").getPath()+"/";
        logger.info(path);
        FileAlterationObserver observer = new FileAlterationObserver(new File(path));
        observer.addListener(new FileListener(applicationContext));
        monitor.addObserver(observer);
        monitor.start();
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @PreDestroy
    public void destroy1() throws Exception {
        logger.info("desctroy monitor");
        monitor.stop();
    }

    @Override
    protected void finalize() throws Throwable {
        logger.info("finalize TestFilter");
        super.finalize();
    }
}
