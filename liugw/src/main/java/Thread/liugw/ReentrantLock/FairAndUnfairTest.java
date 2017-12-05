package Thread.liugw.ReentrantLock;

import java.awt.List;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.junit.Test;

public class FairAndUnfairTest 
{
	
	private static Lock fairLock = new ReentrantLock2(true);
	private static Lock unfairLock = new ReentrantLock2(false);	
	
	@Test
	public void fair()
	{
		System.out.println("fair");
		testLock(fairLock);
		
		try {
			Thread.sleep(100000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
//	@Test
	public void unfair()
	{
		System.out.println("Unfair");
		testLock(unfairLock);
		
		try {
			Thread.sleep(100000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void testLock(Lock lock)
	{
		for(int i=1; i<6; i++)
		{
			Job job = new Job(lock);
			job.setName("Thread_"+i);
			job.start();
//			try {
//				TimeUnit.SECONDS.sleep(1);
//			} catch (InterruptedException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
		}
	}
	
	private static class Job extends Thread
	{
		private Lock lock;
		public Job(Lock lock)
		{
			this.lock = lock;
		}
		
		public void run()
		{
			while(true){
				lock.lock();
	//			for(int i=0; i<2; i++)
	//			{
					StringBuilder stringBuffer = new StringBuilder();
					for( Thread thread : ((ReentrantLock2)lock).getQueuedThreads() )
					{
						stringBuffer.append(thread.getName() + ",");
					}
					System.out.println("Current Thread Name="+Thread.currentThread().getName() + ",  waiting Thread queue:[" + stringBuffer + "]");
					
					
	//			}
				
	//			System.out.println("unlock");
				
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}finally
				{
					System.out.println("unlock");
					lock.unlock();
				}
			}
		}
	}

	private static class ReentrantLock2 extends ReentrantLock
	{
		public ReentrantLock2(boolean fair)
		{
			super(fair);
		}
		
		public Collection<Thread> getQueuedThreads()
		{
			ArrayList<Thread> arrayList = new ArrayList<Thread>(super.getQueuedThreads());
			Collections.reverse(arrayList);
			return arrayList;
		}
	}
	

	
}
