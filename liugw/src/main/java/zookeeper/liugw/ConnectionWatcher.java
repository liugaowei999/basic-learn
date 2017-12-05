package zookeeper.liugw;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.Watcher.Event.KeeperState;
import org.apache.zookeeper.ZooKeeper;

/**
 * 建立连接到zookeeper服务
 * @author liugaowei
 *
 */
public class ConnectionWatcher implements Watcher
{
    private static final int SESSION_TIMEOUT=5000;
    private int SessionTimeOut = 5000;
    private String host = "";
    
    protected ZooKeeper zk;
    CountDownLatch connectedSignal=new CountDownLatch(1);
    
    public ConnectionWatcher(String host, int TimeOut)
    {
    	SessionTimeOut = TimeOut;
    	this.host = host;
    }
    
    public ZooKeeper connect() throws IOException, InterruptedException
    {
        zk=new ZooKeeper(host, SessionTimeOut, this);
        connectedSignal.await();
        return zk;
    }
    
    public void connect(String host) throws IOException, InterruptedException
    {
        zk=new ZooKeeper(host, SESSION_TIMEOUT, this);
        connectedSignal.await();
    }
    
//    @Override
    public void process(WatchedEvent event) 
    {
        if(event.getState()==KeeperState.SyncConnected)
        {
            connectedSignal.countDown();
        }
    }
    public void close() throws InterruptedException
    {
        zk.close();
    }

}