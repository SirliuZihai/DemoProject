package zookeeper.lock;

import org.apache.zookeeper.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;

public class ZkLock implements Watcher {
    public static ConcurrentHashMap<String,ZkLock> ZkLockMap ;
    static final Logger logger = LoggerFactory.getLogger(ZkLock.class);
    static String address = "127.0.0.1:2181";
    static CountDownLatch connectedSemaphore = new CountDownLatch(1);
    static ZooKeeper zk = null;
    static final Object lcObj = new Object();
    String nodePath = null;

    static{
        if(zk == null){
            try {
                zk = new ZooKeeper(address, 3000, new Watcher() {
                    @Override
                    public void process(WatchedEvent event) {
                        if (Event.KeeperState.SyncConnected == event.getState()) {  //zk连接成功通知事件
                            if (Event.EventType.None == event.getType() && null == event.getPath()) {
                                connectedSemaphore.countDown();
                            }
                        }
                    }
                });
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                connectedSemaphore.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(Thread.currentThread().getName()+" Finished starting ZK");
        }
        ZkLockMap= new ConcurrentHashMap<>();
        try {
            ZkLockMap.put("/zoo/lock",new ZkLock("/zoo/lock"));
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    public ZkLock(String nodePath)
            throws KeeperException, IOException, InterruptedException {
        this.nodePath = nodePath;

    }

    @Override
    public void process(WatchedEvent event) {
        System.out.println(Thread.currentThread().getName()+" "+event.getPath()+" "+event.getType());
        if(event.getType() == Event.EventType.NodeDeleted){
            synchronized (this) {
                this.notify();
                System.out.println(Thread.currentThread().getName()+" notified");
            }
        }
    }
    void enter() throws KeeperException, InterruptedException {
        while (true){
            try {
                String path = zk.create(nodePath, "1".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE,
                        CreateMode.EPHEMERAL_SEQUENTIAL);
                break;
            } catch (KeeperException.NoNodeException e) {
                synchronized (this){
                    zk.exists(nodePath,true);
                    this.wait();
                }
            }
        }

    }
    void leave() throws KeeperException, InterruptedException{
        zk.delete(nodePath,-1);
    }

    @Override
    protected void finalize() throws Throwable {
        zk.close();
        super.finalize();
    }
}
