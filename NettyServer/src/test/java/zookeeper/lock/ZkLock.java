package zookeeper.lock;

import org.apache.zookeeper.*;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

public class ZkLock implements Watcher {
    private static CountDownLatch connectedSemaphore = new CountDownLatch(1);
    ZooKeeper zk = null;
    String nodePath = null;
    final Object mutex = new Object();

    public ZkLock(String address,String nodePath)
            throws KeeperException, IOException, InterruptedException {
        if(address ==null)
            address = "127.0.0.1:2181";
        this.nodePath = nodePath;
        if(zk == null){
            zk = new ZooKeeper(address, 3000, this);
            //zk.exists(nodePath,true);
            connectedSemaphore.await();
            System.out.println("Finished starting ZK");
        }
    }

    @Override
    public void process(WatchedEvent event) {
        System.out.println(Thread.currentThread().getName()+" "+event.getPath()+" "+event.getType());
        if(event.getType() == Event.EventType.NodeDeleted){
            synchronized (mutex) {
                mutex.notify();
                System.out.println(Thread.currentThread().getName()+" notified");
            }
        }
        if (Event.KeeperState.SyncConnected == event.getState()) {  //zk连接成功通知事件
            if (Event.EventType.None == event.getType() && null == event.getPath()) {
                connectedSemaphore.countDown();
            }
        }
    }
    void enter() throws InterruptedException, KeeperException {
        boolean b = true;
        while (b){
            try {
                zk.create(nodePath, null, ZooDefs.Ids.OPEN_ACL_UNSAFE,
                        CreateMode.PERSISTENT);
                zk.exists(nodePath,this);
                b = false;
            } catch (KeeperException.NodeExistsException e) {
                synchronized (mutex) {
                    System.out.println(Thread.currentThread().getName()+" waited");
                    zk.exists(nodePath,this);
                    mutex.wait();
                    b = true;
                }
            }
        }


    }
    void leave() throws KeeperException, InterruptedException{
        zk.delete(nodePath, -1);
    }
}
