package zookeeper.liugw;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.ZooDefs.Ids;
import org.apache.zookeeper.data.Stat;

/**
 * ZooKeeper 实现同步队列。 所有进程全部到达时，才开始工作任务。
 * @author liugaowei
 *
 */
public class Synchronizing extends TestMainClient 
{
    int size;
    String name;
    public static final Logger logger = Logger.getLogger(Synchronizing.class);
 
    /**
     * 构造函数
     *
     * @param connectString 服务器连接
     * @param root 根目录
     * @param size 队列大小
     */
    Synchronizing(String connectString, String root, int size) 
    {
        super(connectString);
        this.root = root;
        this.size = size;
 
        if (zk != null) 
        {
            try 
            {
                Stat s = zk.exists(root, false);
                if (s == null) 
                {
                    zk.create(root, new byte[0], Ids.OPEN_ACL_UNSAFE,CreateMode.PERSISTENT);
                }
            } catch (KeeperException e) 
            {
                logger.error(e);
            } catch (InterruptedException e) 
            {
                logger.error(e);
            }
        }
        try 
        {
            name = new String(InetAddress.getLocalHost().getCanonicalHostName().toString());
        } catch (UnknownHostException e) 
        {
            logger.error(e);
        }
 
    }
 
    /**
     * 加入队列
     *
     * @return
     * @throws KeeperException
     * @throws InterruptedException
     */
 
    void addQueue() throws KeeperException, InterruptedException
    {
    	// 注册对 /start 目录节点的监听事件
        zk.exists(root + "/start",true);
        zk.create(root + "/" + name, new byte[0], Ids.OPEN_ACL_UNSAFE,CreateMode.EPHEMERAL_SEQUENTIAL);
        synchronized (mutex) 
        {
            List<String> list = zk.getChildren(root, false);
            if (list.size() < size) 
            {
                mutex.wait();
            } else 
            {
            	// 进程全部启动后， 临时节点个数等于 设定的进程总数时，创建start节点， 此时监听进程会捕获节点创建事件，并通知注册监听的主进程。
                zk.create(root + "/start", new byte[0], Ids.OPEN_ACL_UNSAFE,CreateMode.PERSISTENT);
            }
        }
    }
 
    @Override
    public void process(WatchedEvent event) 
    {
        if( event.getPath().equals(root + "/start") && event.getType() == Event.EventType.NodeCreated )
        {
            System.out.println("得到通知");
            super.process(event);
            doAction();
        }
    }
 
    /**
     * 执行其他任务
     */
    private void doAction()
    {
        System.out.println("同步队列已经得到同步，可以开始执行后面的任务了");
    }
 
    public static void main(String args[]) 
    {
        //启动Server
        String connectString = "localhost:2181";
        int size = 2;
        Synchronizing b = new Synchronizing(connectString, "/synchronizing", size);
        try
        {
            b.addQueue();
        } catch (KeeperException e)
        {
            logger.error(e);
        } catch (InterruptedException e)
        {
            logger.error(e);
        }
    }
}