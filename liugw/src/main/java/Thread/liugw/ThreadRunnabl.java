package Thread.liugw;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ThreadRunnabl implements Runnable 
{
    private int count = 1, number;
 
    public ThreadRunnabl(int num) 
    {
       number = num;
       System.out.println("Create Thread-" + number);
    }
 
    public void run() 
    {
       while (true) 
       {
           System.out.println("Thread-" + number + " run " + count+" time(s)");
           if (++count == 3)
              return;
           
//           try 
//           {
//        	   Thread.sleep(10000);
//           } catch (InterruptedException e) 
//           {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//		   }
       }
    }
    
    public static void main(String[] args)
    {
    	System.out.println(Thread.currentThread().getName() + " Started!");
    	if(1==1){
//    	//使用CachedThreadPool启动线程
    	// 线程池里有很多线程需要同时执行，老的可用线程将被新的任务触发重新执行，如果线程超过60秒内没执行，那么将被终止并从池中删除
//    	创建一个可根据需要创建新线程的线程池，但是在以前构造的线程可用时将重用它们。对于执行很多短期异步任务的程序而言，这些线程池通常可提高程序性能。
    	ExecutorService exec = Executors.newCachedThreadPool();
        for (int i = 0; i < 5; i++)
        {
            exec.execute(new ThreadRunnabl(i));
            
        }
        exec.shutdown();
    	}
        
    	if(1==1){
        // 使用FixedThreadPool启动线程
        // 拥有固定线程数的线程池，如果没有任务执行，那么线程会一直等待; 创建一个可重用固定线程数的线程池，以共享的无界队列方式来运行这些线程。
        ExecutorService exec_fixedpool = Executors.newFixedThreadPool(2);
        for (int i = 6; i < 10; i++)
        {
        	exec_fixedpool.execute(new ThreadRunnabl(i));
        }
        exec_fixedpool.shutdown();
    	}
        
    	if(1==1){
        // 使用SingleThreadExecutor启动线程
    	// 只有一个线程的线程池，因此所有提交的任务是顺序执行; 创建一个使用单个 worker 线程的 Executor，以无界队列方式来运行该线程。
        ExecutorService exec_singlepool = Executors.newSingleThreadExecutor();
        for (int i = 11; i < 15; i++)
        {
        	exec_singlepool.execute(new ThreadRunnabl(i));
        }
        exec_singlepool.shutdown();
    	}
        
        System.out.println(Thread.currentThread().getName() + " Stoped!");
    	
    }
}
