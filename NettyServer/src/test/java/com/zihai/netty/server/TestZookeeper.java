package com.zihai.netty.server;

import org.apache.zookeeper.*;

import java.io.IOException;
import java.util.List;

public class TestZookeeper implements Watcher {
     ZooKeeper zkClient = new ZooKeeper("127.0.0.1:2181",60000,this);

    public TestZookeeper() throws IOException {
    }

    public static void main(String[] args) throws KeeperException, InterruptedException, IOException {

        TestZookeeper test  = new TestZookeeper();
//        zkClient.create("/zoo/data",null/*data*/, ZooDefs.Ids.OPEN_ACL_UNSAFE,CreateMode.PERSISTENT);
        test.zkClient.create("/zoo/data/method",null/*data*/, ZooDefs.Ids.OPEN_ACL_UNSAFE,CreateMode.EPHEMERAL_SEQUENTIAL);
        String key = test.zkClient.create("/zoo/data/method",null/*data*/, ZooDefs.Ids.OPEN_ACL_UNSAFE,CreateMode.EPHEMERAL_SEQUENTIAL);
        //zkClient.delete("/zoo/data/method0000000009");
        test.zkClient.setData(key,"haoren".getBytes(),-1);
        System.out.println("data:"+key+" "+new String(test.zkClient.getData(key,true,null)));
         test.list("zoo/data");
        //zkClient.create("/user",null, CreateMode.PERSISTENT);
        test.zkClient.close();

        System.out.println("###注册成功###");
    }
    public  void list(String groupName) throws KeeperException,
            InterruptedException {
        String path = "/" + groupName;
        List<String> children =zkClient.getChildren(path,true);
        if (children.isEmpty()) {
            System.out.printf("No members in group %s\n", groupName);
            System.exit(1);
        }
        for (String child : children) {
            System.out.println(child);
        }
    }

    @Override
    public void process(WatchedEvent event) {
        System.out.println("process"+event.getPath()+" "+event.getType());
    }
}
