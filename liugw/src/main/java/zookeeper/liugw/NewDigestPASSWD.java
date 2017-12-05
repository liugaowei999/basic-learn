package zookeeper.liugw;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.ACL;
import org.apache.zookeeper.data.Id;
import org.apache.zookeeper.server.auth.DigestAuthenticationProvider;

public class NewDigestPASSWD
{
	public void create()
	{
		try 
		{
			//new一个acl
	        List<ACL> acls = new ArrayList<ACL>();
	        
	        //添加第一个id，采用用户名密码形式
	        Id id1 = new Id("digest",DigestAuthenticationProvider.generateDigest("admin:admin"));
	        ACL acl1 = new ACL(ZooDefs.Perms.READ, id1);
	        acls.add(acl1);
	        
	        //添加第二个id，所有用户可读权限
	        Id id2 = new Id("world", "anyone");
	        ACL acl2 = new ACL(ZooDefs.Perms.READ, id2);
	        acls.add(acl2);
	        
	        // Zk用admin认证，创建/test ZNode。
	        ZooKeeper Zk = new ZooKeeper("localhost:2181",2000, null);
	        Zk.addAuthInfo("digest", "admin:admin".getBytes());
        
			Zk.create("/test", "data".getBytes(), acls, CreateMode.PERSISTENT);
		} catch (KeeperException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void delete_normal() 
	{
		// 普通方式连接zk
        try 
        {
        	// Zk用admin认证，创建/test ZNode。
	        ZooKeeper Zk = new ZooKeeper("localhost:2181",2000, null);
	        Zk.addAuthInfo("digest", "admin:123".getBytes());
	        
			Zk.delete("/test", -1);
		} catch (IOException e) 
        {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (KeeperException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void delete_auth() 
	{
		// 普通方式连接zk
        try 
        {
			ZooKeeper Zk = new ZooKeeper("localhost:2181",2000, null);
			Zk.delete("/test", -1);
		} catch (IOException e) 
        {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (KeeperException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
    public static void main(String[] args) throws Exception 
    {
    	NewDigestPASSWD test1 = new NewDigestPASSWD();
    	test1.create();
    	
    	test1.delete_normal();
    	
//    	test1.delete_auth();
        // 删除
//        Zk.delete("/test", -1);
   }
}
