package zookeeper;

import java.io.IOException;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooDefs.Ids;
import org.apache.zookeeper.ZooKeeper;

public class ZKTest 
{
	public final String CLIENT_PORT = "2181";
	public final int CONNECTION_TIMEOUT = 1000;
	
	public void connectZK()
	{
		 // 创建一个与服务器的连接
		 try 
		 {
			ZooKeeper zk = new ZooKeeper("localhost:" + CLIENT_PORT, 
			                              CONNECTION_TIMEOUT, 
			                              new Watcher() 
			 						      { 
								            // 监控所有被触发的事件
								            public void process(WatchedEvent event) 
								            { 
								                System.out.println("已经触发了" + event.getType() + "事件！"); 
								            } 
									      });
			 if( null != zk.exists("/testRootPath/testChildPathTwo", false) )
				 zk.delete("/testRootPath/testChildPathTwo",-1); 
			 
			 if( null != zk.exists("/testRootPath/testChildPathOne", false) )
				 zk.delete("/testRootPath/testChildPathOne",-1); 
//			  删除父目录节点
			 if( null != zk.exists("/testRootPath", false) )
				 zk.delete("/testRootPath",-1); 
			
			// 创建一个目录节点
			 zk.create("/testRootPath", "testRootData12".getBytes(), Ids.OPEN_ACL_UNSAFE,CreateMode.PERSISTENT); 
			 // 创建一个子目录节点
			 zk.create("/testRootPath/testChildPathOne", "testChildDataOne".getBytes(),Ids.OPEN_ACL_UNSAFE,CreateMode.PERSISTENT); 
			 
			 System.out.println(new String(zk.getData("/testRootPath",false,null))); 
			 
			// 取出子目录节点列表
			 System.out.println(zk.getChildren("/testRootPath",true)); 
			 
			 // 修改子目录节点数据
			 zk.setData("/testRootPath/testChildPathOne","modifyChildDataOne".getBytes(),-1); 
			 System.out.println("目录节点状态：["+zk.exists("/testRootPath",true)+"]"); 
			 
			 // 创建另外一个子目录节点
			 zk.create("/testRootPath/testChildPathTwo", "testChildDataTwo".getBytes(), Ids.OPEN_ACL_UNSAFE,CreateMode.PERSISTENT); 
			 System.out.println(new String(zk.getData("/testRootPath/testChildPathTwo",true,null))); 
			 // 删除子目录节点
//			 zk.delete("/testRootPath/testChildPathTwo",-1); 
//			 zk.delete("/testRootPath/testChildPathOne",-1); 
			 // 删除父目录节点
//			 zk.delete("/testRootPath",-1); 
			 
			 zk.close();
		} catch (IOException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		catch (KeeperException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		catch ( InterruptedException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	}

	public static void main(String[] args)
	{
		ZKTest zkTest = new ZKTest();
		zkTest.connectZK();
	}
}
