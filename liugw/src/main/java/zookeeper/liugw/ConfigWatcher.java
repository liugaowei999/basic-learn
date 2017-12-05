package zookeeper.liugw;

import java.io.IOException;

import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.Watcher.Event.EventType;

/**
 * 监控某个目录节点的属性内容发生变化。  (只监控属性内容get方法的内容。 目录下 子目录的变更情况不在监控范围内）
 * @author liugaowei
 *
 */
public class ConfigWatcher implements Watcher
{
    private ActiveKeyValueStore store;

//    @Override
    public void process(WatchedEvent event) 
    {
        if(event.getType()==EventType.NodeDataChanged)
        {
            try
            {
                dispalyConfig();
            }catch(InterruptedException e)
            {
                System.err.println("Interrupted. exiting. ");
                Thread.currentThread().interrupt();
            }catch(KeeperException e)
            {
                System.out.printf("KeeperException:%s. Exiting.\n", e);
            }
            
        }
        
    }
    
    public ConfigWatcher(String hosts) throws IOException, InterruptedException 
    {
        store=new ActiveKeyValueStore();
        store.connect(hosts);
    }
    
    public void dispalyConfig() throws KeeperException, InterruptedException
    {
        String value=store.read(ConfigUpdater.PATH, this);
        System.out.printf("Read %s as %s\n",ConfigUpdater.PATH,value);
    }

    public static void main(String[] args) throws IOException, InterruptedException, KeeperException 
    {
        ConfigWatcher configWatcher = new ConfigWatcher("localhost");
        configWatcher.dispalyConfig();
        //stay alive until process is killed or Thread is interrupted
        Thread.sleep(Long.MAX_VALUE);
    }
}