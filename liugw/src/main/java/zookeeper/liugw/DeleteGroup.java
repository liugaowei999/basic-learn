package zookeeper.liugw;

import java.io.IOException;
import java.util.List;

import org.apache.zookeeper.KeeperException;

/**
 * 删除根节点 以及 下面的子节点。 自动递归删除， 支持多层目录结构自动删除
 * @author liugaowei
 *
 */
public class DeleteGroup extends ConnectionWatcher
{
    public DeleteGroup() {
        super("localhost:2181", 10);
        // TODO Auto-generated constructor stub
    }



    public void delete(String groupName) throws InterruptedException, KeeperException
    {
        String path="/"+groupName;
        List<String> children;
        try 
        {
            children = zk.getChildren(path, false);
            if(children.isEmpty())
            {
            	zk.delete( path, -1);
            	return;
            }
            
            for(String child:children)
            {
            	delete(groupName + "/" + child);
            }
            zk.delete(path, -1);
        } catch (KeeperException.NoNodeException e) 
        {
            System.out.printf("Group %s does not exist\n", groupName);
            System.exit(1);
        }    
    }
    
    
    
    public static void main(String[] args) throws InterruptedException, IOException, KeeperException 
    {
        DeleteGroup deleteGroup = new DeleteGroup();
        deleteGroup.connect(args[0]);
        deleteGroup.delete(args[1]);
        deleteGroup.close();
    }
}