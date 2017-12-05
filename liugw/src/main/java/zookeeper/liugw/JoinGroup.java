package zookeeper.liugw;

import java.io.IOException;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooDefs.Ids;

public class JoinGroup extends ConnectionWatcher
{
    public JoinGroup() {
        super("localhost:2181", 10);
        // TODO Auto-generated constructor stub
    }



    public void join(String groupName,String memberName) throws KeeperException, InterruptedException
    {
        String path="/"+groupName+"/"+memberName;
        System.out.println("Creating path=" + path);
        String createdPath=zk.create(path, null, Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
        System.out.println("Created:"+createdPath);
    }
    
    
    
    public static void main(String[] args) throws InterruptedException, IOException, KeeperException 
    {
//    	System.out.println(args[0] + "|" + args[1] + "|" + args[2] + "|");
        JoinGroup joinGroup = new JoinGroup();
        joinGroup.connect("localhost"); // args[0]
        joinGroup.join("config","format"); // args[1], args[2]
        
        //stay alive until process is killed or thread is interrupted
        Thread.sleep(Long.MAX_VALUE);
    }
}