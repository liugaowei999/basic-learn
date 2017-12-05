package zookeeper.liugw;

import java.io.IOException;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import org.apache.zookeeper.KeeperException;

public class ConfigUpdater 
{
    
    public static final String  PATH="/config";
    
    private ActiveKeyValueStore store;
    private Random random=new Random();
    
    public ConfigUpdater(String hosts) throws IOException, InterruptedException 
    {
        store = new ActiveKeyValueStore("localhost:2181",10);
        store.connect(hosts);
    }
    
    public void run() throws InterruptedException, KeeperException
    {
        while(true)
        {
            String value=random.nextInt(100)+"";
            store.write(PATH, value);
            System.out.printf("Set %s to %s\n",PATH,value);
            TimeUnit.SECONDS.sleep(random.nextInt(100));
            
        }
    }
    
    public static void main(String[] args) throws IOException, InterruptedException, KeeperException 
    {
//        CuratorFrameWork
    	// 连接重试
    	while(true)
    	{
    		try
    		{
		        ConfigUpdater configUpdater = new ConfigUpdater(args[0]);
		        configUpdater.run();
    		}
    		catch (KeeperException.SessionExpiredException e) 
    		{
    			// start a new session --- re-connect. 重新建立新的ConfigUpdater对象进行重试。
    			//因为一个会话过期 时，ZooKeeper对象会进入CLOSED状态，此状态下它不能进行重试连接。我们只能将这个异常简单抛出并让拥有着创建一个新实例，
    			//以重试整个 write()方法。
    		}
    		catch (IOException e) 
    		{
    			// 当ZooKeeper对象被创建时，他会尝试连接另一个ZooKeeper服务器。如果连接失败或超时， 那么他会尝试连接集合体中的另一台服务器。
    			// 如果在尝试集合体中的所有服务器之后仍然无法建立连接，它会抛出一个IOException异常
    		}catch (KeeperException e) 
    		{
                // already retried ,so exit 退出重试
                e.printStackTrace();
                break;
            }
    	}
    }
}