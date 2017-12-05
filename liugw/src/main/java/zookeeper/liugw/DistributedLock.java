package zookeeper.liugw;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

//import org.apache.kafka.common.metrics.Stat;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.Watcher.Event.KeeperState;
import org.apache.zookeeper.data.Stat;

/**
 * 分布式锁，这个主要得益于ZooKeeper为我们保证了数据的强一致性，即用户只要完全相信每时每刻，
 * zk集群中任意节点（一个zk server）上的相同znode的数据是一定是相同的。锁服务可以分为两类，一个是保持独占，另一个是控制时序。
 * 1. 保持独占，就是所有试图来获取这个锁的客户端，最终只有一个可以成功获得这把 锁。通常的做法是把ZK上的一个znode看作是一把锁，
 *    通过create znode的方式来实现。所有客户端都去创建 /distribute_lock 节点，最终成功创建的那个客户端也即拥有了这把锁。
 * 2. 控制时序，就是所有试图来获取这个锁的客户端，最终都是会被安排执行，只是有 个全局时序了。做法和上面基本类似，只是这里 /distribute_lock 已经预先存在，
 *    客户端在它下面创建临时有序节点。Zk的父节点（/distribute_lock）维持一份sequence,保证子节点创建的时序性， 从而也形成了每个客户端的全局时序。
 *    
 * 注意：DistributedLock 本共享锁示例也可以用于控制时序。 或者 用于 推举leader领导者进程的示例。   主测试进程为ZkTest.java
 * 实现原理： 创建一个 EPHEMERAL_SEQUENTIAL 目录节点，然后调用 getChildren方法获取当前的目录节点列表中最小的目录节点是不是就是自己创建的目录节点，
 * 如果正是自己创建的，那么它就获得了这个锁，如果不是那么它就调用 exists(String path, boolean watch) 方法并监控 Zookeeper 上比自己小一号的目录节点的变化，
 * 直到小1的节点消失时，从而获得锁，释放锁很简单，只要删除前面它自己所创建的目录节点就行了。
 * @author liugaowei
 *
 */
public class DistributedLock implements Lock, Watcher
{
    private ZooKeeper zk;
    private String root = "/locks";//根
    private String lockName;//竞争资源的标志
    private String waitNode;//等待前一个锁
    private String myZnode;//当前锁
    private CountDownLatch latch;//计数器
    CountDownLatch connectedSignal=new CountDownLatch(1);
    private int sessionTimeout = 30000;
    private List<Exception> exception = new ArrayList<Exception>();
     
    /**
     * 创建分布式锁,使用前请确认config配置的zookeeper服务可用
     * @param config 127.0.0.1:2181
     * @param lockName 竞争资源标志,lockName中不能包含单词lock
     */
    public DistributedLock(String config, String lockName)
    {
        this.lockName = lockName;
        // 创建一个与服务器的连接
         try 
         {
            zk = new ZooKeeper(config, sessionTimeout, this);
            connectedSignal.await();
//            ConnectionWatcher connectionWatcher = new ConnectionWatcher(config, sessionTimeout);
//            long start = System.currentTimeMillis();
//            zk = connectionWatcher.connect();
//            long end = (System.currentTimeMillis() - start);
//            System.out.println("Thread " + Thread.currentThread().getId() + " Connect to ZOO server use time:[" + end + "]");
            Stat stat = zk.exists(root, false);
            if(stat == null)
            {
                // 创建根节点
                zk.create(root, new byte[0], ZooDefs.Ids.OPEN_ACL_UNSAFE,CreateMode.PERSISTENT);
            }
        } catch (IOException e) 
        {
            exception.add(e);
        } catch (KeeperException e) 
        {
            exception.add(e);
        } catch (InterruptedException e) 
        {
            exception.add(e);
        }
    }
 
    /**
     * zookeeper节点的监视器
     */
    public void process(WatchedEvent event) 
    {
        if(this.latch != null) 
        {
        	System.out.println("有进程退出， unlock 释放资源 。。。");
            this.latch.countDown(); 
        }
        
        if(event.getState()==KeeperState.SyncConnected)
        {
            connectedSignal.countDown();
        }
    }
     
    public void lock() 
    {
        if(exception.size() > 0)
        {
            throw new LockException(exception.get(0));
        }
        
        try 
        {
            if(this.tryLock())
            {
                System.out.println("Thread " + Thread.currentThread().getId() + " " +myZnode + " get lock true");
                return;
            }
            else
            {
                waitForLock(waitNode, sessionTimeout);//等待锁
            }
        } catch (KeeperException e) 
        {
            throw new LockException(e);
        } catch (InterruptedException e) 
        {
            throw new LockException(e);
        }
    }
 
    public boolean tryLock() 
    {
        try 
        {
            String splitStr = "_lock_";
            if(lockName.contains(splitStr))
                throw new LockException("lockName can not contains \\u000B");
            
            //创建临时子节点
            myZnode = zk.create(root + "/" + lockName + splitStr, new byte[0], ZooDefs.Ids.OPEN_ACL_UNSAFE,CreateMode.EPHEMERAL_SEQUENTIAL);
            System.out.println(myZnode + " is created ");

            //取出所有子节点
            List<String> subNodes = zk.getChildren(root, false);

            //取出所有lockName的锁
            List<String> lockObjNodes = new ArrayList<String>();
            for (String node : subNodes) 
            {
                String _node = node.split(splitStr)[0];
                if(_node.equals(lockName))
                {
                    lockObjNodes.add(node);
                }
            }
            Collections.sort(lockObjNodes);
            System.out.println("myZnode: " + myZnode + "==" + lockObjNodes.get(0));
            if(myZnode.equals(root+"/"+lockObjNodes.get(0)))
            {
                //如果是最小的节点,则表示取得锁
                return true;
            }

            //如果不是最小的节点，找到比自己小1的节点
            String subMyZnode = myZnode.substring(myZnode.lastIndexOf("/") + 1);
            waitNode = lockObjNodes.get(Collections.binarySearch(lockObjNodes, subMyZnode) - 1);
            System.out.println("waitNode: " + waitNode );
        } catch (KeeperException e) 
        {
            throw new LockException(e);
        } catch (InterruptedException e) 
        {
            throw new LockException(e);
        }
        return false;
    }
 
    public boolean tryLock(long time, TimeUnit unit) 
    {
        try 
        {
            if(this.tryLock())
            {
                return true;
            }
            return waitForLock(waitNode,time);
        } catch (Exception e) 
        {
            e.printStackTrace();
        }
        return false;
    }
 
    private boolean waitForLock(String lower, long waitTime) throws InterruptedException, KeeperException 
    {
    	this.latch = new CountDownLatch(1);
        Stat stat = zk.exists(root + "/" + lower,true);
        //判断比自己小一个数的节点是否存在,如果不存在则无需等待锁,同时注册监听
        if(stat != null)
        {
            System.out.println("Thread " + Thread.currentThread().getId() + "mynode:[" + myZnode + "] waiting for [" + root + "/" + lower + "]");
//            this.latch = new CountDownLatch(1);
//            this.latch.await(waitTime, TimeUnit.MILLISECONDS);
            this.latch.await();
            this.latch = null;
            System.out.println("Thread " + Thread.currentThread().getId() + "mynode:[" + myZnode + "] waiting for [" + root + "/" + lower + "] finish. Get lock now.");
            return true;
        }
        else
        {
        	System.out.println("Thread " + Thread.currentThread().getId() + "mynode:[" + myZnode + "] not need to wait [" + root + "/" + lower + "]. Get lock directly.");
        	this.latch = null;
        	return true;
        }
    }
 
    public void unlock() 
    {
        try 
        {
            System.out.println("unlock " + myZnode);
            zk.delete(myZnode,-1);
            myZnode = null;
            zk.close();
        } catch (InterruptedException e) 
        {
            e.printStackTrace();
        } catch (KeeperException e) 
        {
            e.printStackTrace();
        }
    }
 
    public void lockInterruptibly() throws InterruptedException 
    {
        this.lock();
    }
 
    public Condition newCondition() 
    {
        return null;
    }
     
    public class LockException extends RuntimeException 
    {
        private static final long serialVersionUID = 1L;
        public LockException(String e)
        {
            super(e);
        }
        public LockException(Exception e)
        {
            super(e);
        }
    }
 
}