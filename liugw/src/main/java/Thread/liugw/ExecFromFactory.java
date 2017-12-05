package Thread.liugw;

import Thread.liugw.ThreadRunnabl;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

public class ExecFromFactory 
{

	public static class DaemonThreadFactory implements ThreadFactory 
	{
	    public Thread newThread(Runnable r) 
	    {
	       Thread t = new Thread(r);
	       t.setDaemon(true);
	       return t;
	    }
	}
	
	public static class MaxPriorityThreadFactory implements ThreadFactory 
	{
	    public Thread newThread(Runnable r) 
	    {
	       Thread t = new Thread(r);
	       t.setPriority(Thread.MAX_PRIORITY);
	       return t;
	    }
	}
	
	public static class MinPriorityThreadFactory implements ThreadFactory 
	{
	    public Thread newThread(Runnable r) 
	    {
	       Thread t = new Thread(r);
	       t.setPriority(Thread.MIN_PRIORITY);
	       return t;
	    }
	}
	
	
	public static void main(String[] args) throws Exception 
	{
	       ExecutorService defaultExec = Executors.newCachedThreadPool();
	       ExecutorService daemonExec = Executors.newCachedThreadPool(new DaemonThreadFactory());
	       ExecutorService maxPriorityExec = Executors.newCachedThreadPool(new MaxPriorityThreadFactory());
	       ExecutorService minPriorityExec = Executors.newCachedThreadPool(new MinPriorityThreadFactory());
	       for (int i = 0; i < 10; i++)
	       {
	           daemonExec.execute(new ThreadRunnabl(i));
	       }
	       
	       for (int i = 10; i < 20; i++)
	       {
	           if (i == 19)
	              maxPriorityExec.execute(new ThreadRunnabl(i));
	           else if (i == 10)
	              minPriorityExec.execute(new ThreadRunnabl(i));
	           else
	              defaultExec.execute(new ThreadRunnabl(i));
	       }
	    }
}
