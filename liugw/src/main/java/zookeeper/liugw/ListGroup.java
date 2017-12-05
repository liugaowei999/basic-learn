package zookeeper.liugw;

import java.io.IOException;
import java.util.List;

import org.apache.zookeeper.KeeperException;
//import org.apache.zookeeper.ZooKeeper;

/**
 * 输出示例： 
lgwgrp| --------- memeber count:3
lgwgrp/p1| --------- memeber count:2
lgwgrp/p1/e0| --------- memeber count:0
lgwgrp/p1/e1| --------- memeber count:0
lgwgrp/p2| --------- memeber count:1
lgwgrp/p2/e0| --------- memeber count:0
lgwgrp/p3| --------- memeber count:0
 * @author liugaowei
 *
 */
public class ListGroup extends ConnectionWatcher 
{
    public ListGroup( ) {
        super("localhost:2181", 10);
        // TODO Auto-generated constructor stub
    }
    public void list(String groupNmae) throws KeeperException, InterruptedException
    {
        String path = "/"+groupNmae;
        try {
            List<String> children = zk.getChildren(path, false);
            if(children.isEmpty())
            {
//                System.out.printf("No memebers in group %s\n",groupNmae);
//                System.exit(1);
            	System.out.println(groupNmae + "| --------- memeber count:" + children.size());
            	return;
            }
            
            System.out.println(groupNmae + "| --------- memeber count:" + children.size());
            for(String child:children)
            {
//                System.out.println(groupNmae + "/" + child + ":" + children.size());
                list(groupNmae + "/" + child);
            }
        } catch (KeeperException.NoNodeException e) 
        {
            System.out.printf("Group %s does not exist \n", groupNmae);
            System.exit(1);
        } 
    }
    public static void main(String[] args) throws IOException, InterruptedException, KeeperException 
    {
        ListGroup listGroup = new ListGroup();
        listGroup.connect(args[0]);
        listGroup.list(args[1]);
        listGroup.close();
    }
}