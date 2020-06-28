package com.zihai.netty.server;

import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;

public class TestZookeeperEvent implements Watcher {

    @Override
    public void process(WatchedEvent event) {
        System.out.println(event.getPath()+event.getType());
    }
}
