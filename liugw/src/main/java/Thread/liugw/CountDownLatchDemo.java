package Thread.liugw;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.CountDownLatch;
/**
 * CountDownLatch是什么
 * CountDownLatch是在java1.5被引入的，跟它一起被引入的并发工具类还有CyclicBarrier、Semaphore、ConcurrentHashMap和BlockingQueue，
 * 它们都存在于java.util.concurrent包下。
 * CountDownLatch这个类能够使一个线程等待其他线程完成各自的工作后再执行。例如，应用程序的主线程希望在负责启动框架服务的线程已经启动所有的框架服务之后再执行。
 * 
 * CountDownLatch是通过一个计数器来实现的，计数器的初始值为线程的数量。
 * 每当一个线程完成了自己的任务后，计数器的值就会减1。当计数器值到达0时，它表示所有的线程已经完成了任务，然后在闭锁上等待的线程就可以恢复执行任务。
 * 
 * 使用场景：
 * 1. 实现最大的并行性：
 *    有时我们想同时启动多个线程，实现最大程度的并行性。例如，我们想测试一个单例类。如果我们创建一个初始计数为1的CountDownLatch，
 *    并让所有线程都在这个锁上等待，那么我们可以很轻松地完成测试。我们只需调用 一次countDown()方法就可以让所有的等待线程同时恢复执行。
 * 2. 开始执行前等待n个线程完成各自任务：
 *    例如应用程序启动类要确保在处理用户请求前，所有N个外部系统已经启动和运行了。
 * 3. 死锁检测（模拟产生死锁的场景）：一个非常方便的使用场景是，你可以使用n个线程访问共享资源，在每次测试阶段的线程数目是不同的，并尝试产生死锁。
 * @author liugaowei
 *
 */
public class CountDownLatchDemo 
{
	final static SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
    public static void main(String[] args) throws InterruptedException 
    {
    	/**
    	 * public void CountDownLatch(int count) {...}
    	 * 构造器中的计数值（count）实际上就是闭锁需要等待的线程数量。这个值只能被设置一次，而且CountDownLatch没有提供任何机制去重新设置这个计数值。
    	 */
    	CountDownLatch latch=new CountDownLatch(2);//两个工人的协作
    	Worker worker1=new Worker("zhang san", 5000, latch);
    	Worker worker2=new Worker("li si", 8000, latch);
    	worker1.start();//
    	worker2.start();//
    	
    	/**
    	 * 主线程必须在启动其他线程后立即调用CountDownLatch.await()方法。这样主线程的操作就会在这个方法上阻塞，直到其他线程完成各自的任务。
    	 */
    	latch.await();//等待所有工人完成工作
        System.out.println("all work done at "+sdf.format(new Date()));
	}
    
    
    static class Worker extends Thread
    {
    	String workerName; 
    	int workTime;
    	CountDownLatch latch;
    	public Worker(String workerName ,int workTime ,CountDownLatch latch)
    	{
    		 this.workerName=workerName;
    		 this.workTime=workTime;
    		 this.latch=latch;
    	}
    	
    	public void run()
    	{
    		
    		
    		try
    		{
    			System.out.println("Worker "+workerName+" do work begin at "+sdf.format(new Date()));
        		doWork();//工作了
        		System.out.println("Worker "+workerName+" do work complete at "+sdf.format(new Date()));
    		}
    		finally
    		{
    			/**
    			 * 通知机制是通过 CountDownLatch.countDown()方法来完成的；每调用一次这个方法，在构造函数中初始化的count值就减1。
    			 * 所以当N个线程都调 用了这个方法，count的值等于0，然后主线程就能通过await()方法，恢复执行自己的任务。
    			 */
    			latch.countDown();//工人完成工作，计数器减一
    		}

    	}
    	
    	private void doWork()
    	{
    		try 
    		{
				Thread.sleep(workTime);
			} catch (InterruptedException e) 
    		{
				e.printStackTrace();
			}
    	}
    }
    
     
}
