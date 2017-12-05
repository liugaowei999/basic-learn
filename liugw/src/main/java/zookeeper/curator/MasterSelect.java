package zookeeper.curator;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.leader.LeaderSelector;
import org.apache.curator.framework.recipes.leader.LeaderSelectorListener;
import org.apache.curator.framework.state.ConnectionState;
import org.apache.curator.retry.ExponentialBackoffRetry;

public class MasterSelect {
    
    static String master_path = "/curator_master_path";
    
    public static void main(String[] args) throws InterruptedException {
        System.out.println("dddd");
        CuratorFramework client = CuratorFrameworkFactory.builder()
                .connectString("192.168.213.148:2182")
                .retryPolicy(new ExponentialBackoffRetry(3000, 3)).build();
        client.start();
        LeaderSelector selector = new LeaderSelector(client, master_path, new LeaderSelectorListener() {

            @Override
            public void stateChanged(CuratorFramework arg0, ConnectionState arg1) {
                // TODO Auto-generated method stub
                
            }

            @Override
            public void takeLeadership(CuratorFramework client) throws Exception {
                // TODO Auto-generated method stub
                System.out.println("成为Master角色");
                Thread.sleep(3000);
                System.out.println("完成Master操作，释放Master权利");
            }
        });
        
        selector.autoRequeue();
        selector.start();
        Thread.sleep(Integer.MAX_VALUE);
    }

}
