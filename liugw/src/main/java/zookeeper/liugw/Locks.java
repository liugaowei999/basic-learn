package zookeeper.liugw;

//import net.xulingbo.zookeeper.TestMainClient;
//import net.xulingbo.zookeeper.TestMainServer;
import org.apache.log4j.Logger;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.data.Stat;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * locks
 * <p/>
 * Author By: \
 * Created Date: \
 */
public class Locks extends TestMainClient 
{
    public static final Logger logger = Logger.getLogger(Locks.class);
    String myZnode;
    public final static String locks = "1";
    
    public Locks(String connectString, String root) 
    {
    	// super（参数）：调用父类中的某一个构造函数（应该为构造函数中的第一条语句）。
    	// this（参数）：调用本类中另一种形成的构造函数（应该为构造函数中的第一条语句）
    	// 调用super()必须写在子类构造方法的第一行，否则编译不通过。每个子类构造方法的第一条语句，都是隐含地调用super()，如果父类没有这种形式的构造函数，那么在编译的时候就会报错。
        super(connectString);
        System.out.println("Step 1 OK");
        this.root = root;
        if (zk != null) 
        {
            try 
            {
                Stat s = zk.exists(root, false);
                if (s == null) 
                {
                    zk.create(root, new byte[0], ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
                }
            } catch (KeeperException e) 
            {
                logger.error(e);
            } catch (InterruptedException e) 
            {
                logger.error(e);
            }
        }
    }
    void getLock() throws KeeperException, InterruptedException
    {
        List<String> list = zk.getChildren(root, false);
        String[] nodes = list.toArray(new String[list.size()]);
        Collections.sort(list);
        if(myZnode.equals(root+"/"+nodes[0]))
        {
            doAction();
        }
        else
        {
        	String subMyZnode = myZnode.substring(myZnode.lastIndexOf("/") + 1);
        	String waitNode = list.get(Collections.binarySearch(list, subMyZnode) - 1);
//            waitForLock(nodes[0]);
            waitForLock(waitNode);
        }
    }
    void check() throws InterruptedException, KeeperException 
    {
        myZnode = zk.create(root + "/lock_" , new byte[0], ZooDefs.Ids.OPEN_ACL_UNSAFE,CreateMode.EPHEMERAL_SEQUENTIAL);
        getLock();
    }
    void waitForLock(String lower) throws InterruptedException, KeeperException 
    {
        Stat stat = zk.exists(root + "/" + lower,true);
        if(stat != null)
        {
        	System.out.println("Thread-" + Thread.currentThread().getId() + "| " + Thread.currentThread().getName() + "mutex=" + mutex);
        	synchronized (locks) 
        	{
        		synchronized (mutex)
            	{
            		mutex.wait();
            		getLock();
            	}
//            	mutex.wait();
			}
        	

        	
        }
        else
        {
            getLock();
        }
    }
    @Override
    public void process(WatchedEvent event) 
    {
        if(event.getType() == Event.EventType.NodeDeleted)
        {
            System.out.println("得到通知");
            super.process(event);
        }
    }
    /**
     * 执行其他任务
     */
    private void doAction()
    {
        System.out.println("Thread-" + Thread.currentThread().getId() + "| " + Thread.currentThread().getName() + ":同步队列已经得到同步，可以开始执行后面的任务了");
        try 
        {
			Thread.sleep(10000);
			System.out.println("Thread-" + Thread.currentThread().getId() + "| " + Thread.currentThread().getName() + ":任务执行完成。");
			try 
			{
				zk.delete(myZnode, -1);
			} catch (KeeperException e) 
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (InterruptedException e) 
        {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }

    public static void main(String[] args) 
    {
//        TestMainServer.start();
        final String connectString = "localhost:"+TestMainServer.CLIENT_PORT;
        Thread th1 = new Thread()
        {
        	public void run() 
        	{
		        Locks lk = new Locks(connectString, "/locks");
		        try 
		        {
		            lk.check();
		        } catch (InterruptedException e) 
		        {
		            logger.error(e);
		        } catch (KeeperException e) 
		        {
		            logger.error(e);
		        }
		        System.out.println("Thread-" + Thread.currentThread().getId() + ":退出。");
        	}
        };
        
        Thread th2 = new Thread()
        {
        	public void run() 
        	{
		        Locks lk = new Locks(connectString, "/locks");
		        try 
		        {
		            lk.check();
		        } catch (InterruptedException e) 
		        {
		            logger.error(e);
		        } catch (KeeperException e) 
		        {
		            logger.error(e);
		        }
		        System.out.println("Thread-" + Thread.currentThread().getId() + ":退出。");
        	}
        };
        
        th1.start();
        th2.start();
    }
        


}
