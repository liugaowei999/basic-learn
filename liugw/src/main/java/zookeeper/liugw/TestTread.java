package zookeeper.liugw;

import org.apache.log4j.Logger;

public class TestTread  extends Thread
{ 
	public static final Logger logger = Logger.getLogger(LeaderElection.class);
    public TestTread(String name) 
    { 
        super(name); 
    } 

    public void run() 
    { 
    	 String connectString = "localhost:" + TestMainServer.CLIENT_PORT;

         LeaderElection le = new LeaderElection(connectString, "/GroupMembers");
         le.set_processID(getName());
         try {
//             le.findLeader();
             for( int i=0; i<20 ; i++)
             {
            	 le.findLeader();
            	 Thread.sleep(2000);
             }
             System.out.println(getName() + ":exits!!!!!!!");
         } catch (Exception e) 
         {
             logger.error(e);
         }
    } 

    public static void main(String[] args) 
    { 
        Thread t1 = new TestTread("Thread1"); 
        Thread t2 = new TestTread("Thread2"); 
        t1.start(); 
        try {
			Thread.sleep(40000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        t2.start(); 
    } 
}
