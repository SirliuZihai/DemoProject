package zookeeper.lock;

import org.apache.zookeeper.KeeperException;

public class ZkLockTest {
    static Integer count = 0;
    public static void main(String[] args) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    ZkLock zkLock = new ZkLock(null,"/zoo/lock");
                    for(int i=0;i<100;i++){
                        zkLock.enter();
                        count++;
                        System.out.println(Thread.currentThread().getName()+" count=="+count);
                        zkLock.leave();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    ZkLock zkLock = new ZkLock(null,"/zoo/lock");
                    for(int i=0;i<100;i++){
                        zkLock.enter();
                        count++;
                        System.out.println(Thread.currentThread().getName()+" count=="+count);
                        zkLock.leave();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();

    }
}
