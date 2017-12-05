package zookeeper.curator;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.CountDownLatch;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;
import org.apache.curator.retry.ExponentialBackoffRetry;

/**
 * Curator 实现分布式锁
 * final InterProcessMutex lock = new InterProcessMutex(client, master_path);
 * @author liugaowei
 *
 */
public class RecipesLock {

    static String master_path = "/curator_master_path1";
    static CuratorFramework client = CuratorFrameworkFactory.builder()
            .connectString("192.168.213.148:2182")
            .retryPolicy(new ExponentialBackoffRetry(3000, 3)).build();
    
    public static void main(String[] args) throws InterruptedException {
        client.start();
        
        final InterProcessMutex lock = new InterProcessMutex(client, master_path);
        final CountDownLatch down = new CountDownLatch(1);
        for (int i=0; i<30; i++) {
            new Thread(new Runnable() {
                
                @Override
                public void run() {
                    // TODO Auto-generated method stub
                    try {
                        down.await();
                        lock.acquire();
                    }catch(Exception e) {
                        try {
                            throw new Exception(e.getMessage());
                        } catch (Exception e1) {
                            // TODO Auto-generated catch block
                            e1.printStackTrace();
                        }
                    }
                    
                    SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss|SSSS");
                    String orderNo = sdf.format(new Date());
                    System.out.println("生成的订单号：" + orderNo);
                    try {
                        lock.release();
//                        Thread.sleep(Integer.MAX_VALUE);
                    } catch (Exception e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            }).start();
        }
        down.countDown();
        Thread.sleep(Integer.MAX_VALUE);
    }
}
