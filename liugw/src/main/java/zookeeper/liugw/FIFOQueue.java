package zookeeper.liugw;

import java.nio.ByteBuffer;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.data.Stat;

/**
 * 
 * @author liugaowei
 *
 */
public class FIFOQueue extends TestMainClient
{
    public static final Logger logger = Logger.getLogger(FIFOQueue.class);
 
    /**
     * Constructor
     *
     * @param connectString
     * @param root
     */
    FIFOQueue(String connectString, String root) 
    {
        super(connectString);
        this.root = root;
        if (zk != null) 
        {
            try 
            {
                Stat s = zk.exists(root, false);
                if (s == null) 
                {
                    zk.create(root, new byte[0], ZooDefs.Ids.OPEN_ACL_UNSAFE,CreateMode.PERSISTENT);
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

    /**
     * 生产者
     *
     * @param i
     * @return
     */
    boolean produce(int i) throws KeeperException, InterruptedException
    {
        ByteBuffer b = ByteBuffer.allocate(4);
        byte[] value;
        b.putInt(i);
        value = b.array();
        zk.create(root + "/element", value, ZooDefs.Ids.OPEN_ACL_UNSAFE,CreateMode.PERSISTENT_SEQUENTIAL);
        return true;
    }
 
 
    /**
     * 消费者
     *
     * @return
     * @throws KeeperException
     * @throws InterruptedException
     */
    int consume() 
    {
        int retvalue = -1;
        Stat stat = null;
        try
        {
	        while (true) 
	        {
	        	System.out.println("Applying the lock ...");
	            synchronized (mutex) 
	            {
	            	System.out.println("Get the lock success!");
	                List<String> list = zk.getChildren(root, true);
	                System.out.println("list.size()=" + list.size());
	                if (list.size() == 0) 
	                {
	                	System.out.println("Start waiting ...");
	                    mutex.wait();
	                } 
	                else 
	                {
	                	System.out.println("list.get(0)=" + list.get(0));
	                    Integer min = new Integer(list.get(0).substring(7));
	                    for(String s : list)
	                    {
	                        Integer tempValue = new Integer(s.substring(7));
	                        if(tempValue < min) min = tempValue;
	                    }
	                    System.out.println("min=" + min);
	                    byte[] b = zk.getData(root + "/element" + String.format("%010d", min),false, stat);
	                    System.out.println("min=[" + root + "/element" + String.format("%010d", min) + "]");
	                    zk.delete(root + "/element" + String.format("%010d", min), 0);
	                    ByteBuffer buffer = ByteBuffer.wrap(b);
	                    retvalue = buffer.getInt();
	                    
	                }
	            }
	        }
        }catch( KeeperException e )
        {
        	System.out.println("KeeperException");
        	e.printStackTrace();
        }
        catch( InterruptedException e )
        {
        	System.out.println("InterruptedException");
        	e.printStackTrace();
        }
        return retvalue;
    }
 
    @Override
    public void process(WatchedEvent event) 
    {
        super.process(event);
    }
 
    public static void main(String args[]) 
    {
        //启动Server
//        TestMainServer.start();
        String connectString = "localhost:"+TestMainServer.CLIENT_PORT;
 
        FIFOQueue q = new FIFOQueue(connectString, "/app1");
        int i;
        Integer max = new Integer(5);
 
        System.out.println("Producer");
        for (i = 0; i < max; i++)
        {
            try
            {
                q.produce(10 + i);
            } catch (KeeperException e)
            {
                logger.error(e);
            } catch (InterruptedException e)
            {
                logger.error(e);
            }
        }
 
        System.out.println("Consumer");
        for (i = 0; i < max; i++) 
        {
//            try
//            {
                int r = q.consume();
                System.out.println("Item: " + r);
//            } 
//              catch (KeeperException e)
//            {
//                i--;
//                logger.error(e);
//            } catch (InterruptedException e)
//            {
//                logger.error(e);
//            }
        }
 
    }
}
