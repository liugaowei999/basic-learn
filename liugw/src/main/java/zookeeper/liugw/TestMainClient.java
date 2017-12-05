package zookeeper.liugw;


import org.apache.log4j.xml.DOMConfigurator;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;

import java.io.IOException;

/**
 * TestMainClient
 * <p/>
 * Author By: junshan
 * Created Date: 2010-9-7 14:11:44
 */
public class TestMainClient implements Watcher 
{
    protected static ZooKeeper zk = null;
    protected static Integer mutex;
    int sessionTimeout = 10000;
    protected String root;
    

    

    public TestMainClient(String connectString) 
    {
        if(zk == null)
        {
            try 
            {

                String configFile = this.getClass().getResource("/").getPath()+"/log4j.xml";
                DOMConfigurator.configure(configFile);
                System.out.println("Thread-" + Thread.currentThread().getId() + ":创建一个新的连接:");
                zk = new ZooKeeper(connectString, sessionTimeout, this);
                mutex = new Integer(-1);
                System.out.println("Thread-" + Thread.currentThread().getId() + ":创建连接成功。");
            } catch (IOException e) 
            {
                zk = null;
            }
        }
    }
    synchronized public void process(WatchedEvent event) 
    {
        synchronized (mutex) 
        {
        	//
        	System.out.println(Thread.currentThread().getName() + ":------ @@@@@ unlocked");
            mutex.notify();
        }
    }
}
