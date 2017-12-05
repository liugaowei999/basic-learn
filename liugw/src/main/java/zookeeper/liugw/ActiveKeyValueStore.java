package zookeeper.liugw;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.concurrent.TimeUnit;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooDefs.Ids;
import org.apache.zookeeper.data.Stat;

/**
 * 循环执行重试。其中设置了重试的最大次数MAX_RETRIES和两次重试之间的间隔RETRY_PERIOD_SECONDS.
 * @author liugaowei
 *
 */
public class ActiveKeyValueStore extends ConnectionWatcher 
{
    public ActiveKeyValueStore() {
        super("localhost:2181", 10);
    }
    public ActiveKeyValueStore(String host, int TimeOut) {
        super(host, TimeOut);
        // TODO Auto-generated constructor stub
    }

    private static final Charset CHARSET=Charset.forName("UTF-8");
    private static final int MAX_RETRIES = 5; // 重试次数
    private static final long RETRY_PERIOD_SECONDS = 5;
    
    public void write(String path,String value) throws KeeperException, InterruptedException 
    {
    	int retries=0;
    	while(true)
    	{
    		try 
    		{
		        Stat stat = zk.exists(path, false);
		        if(stat==null)
		        {
		            zk.create(path, value.getBytes(CHARSET),Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
//		            zk.getSessionId();
		        }else
		        {
		            zk.setData(path, value.getBytes(CHARSET),-1);
		        }
    		}catch (KeeperException.SessionExpiredException e) 
    		{
    			throw e;
    		}catch (KeeperException e) 
    		{
    			if(retries++==MAX_RETRIES)
    			{
    				throw e;
    			}
    			//sleep then retry
    			TimeUnit.SECONDS.sleep(RETRY_PERIOD_SECONDS);
    		}
    	}
    }
    public String read(String path,Watcher watch) throws KeeperException, InterruptedException
    {
        byte[] data = zk.getData(path, watch, null);
        return new String(data,CHARSET);
        
    }
    
    public static void main(String[] args)
    {
    	ActiveKeyValueStore zk1 = new ActiveKeyValueStore("localhost:2181", 10);
    	try 
    	{
			zk1.connect("localhost:2181");
//			zk1.write("/lgwgrp/p1/e1", "你好，This is lgwgrp-p1-e1");
			String info = zk1.read("/config", null);
			System.out.println(info);
		} catch (IOException e) 
    	{
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) 
    	{
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (KeeperException e) 
    	{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    }
    
}