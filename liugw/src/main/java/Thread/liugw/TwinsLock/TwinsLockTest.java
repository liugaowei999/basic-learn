package Thread.liugw.TwinsLock;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;

import org.junit.Test;

public class TwinsLockTest 
{

	@Test
	public void test()
	{
		final Lock lock = new TwinsLock();
		class Worker extends Thread
		{
			public void run()
			{
				while(true)
				{
//					System.out.println(Thread.currentThread().getName() + " try to get the lock ...");
//					try {
//						if (false==lock.tryLock(100, TimeUnit.SECONDS))
//						{
//							System.out.println(Thread.currentThread().getName() + " timeout!");
//							continue;
//						}
//					} catch (InterruptedException e1) {
//						// TODO Auto-generated catch block
//						System.out.println(Thread.currentThread().getName() + " timeout!");
////						e1.printStackTrace();
//					}
//					
					lock.lock();
					try {
						TimeUnit.SECONDS.sleep(1);
						System.out.println(Thread.currentThread().getName() + " get lock success!");
						TimeUnit.SECONDS.sleep(1);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					finally {
//						try {
//							TimeUnit.SECONDS.sleep(5);
//						} catch (InterruptedException e) {
//							// TODO Auto-generated catch block
//							e.printStackTrace();
//						}
						lock.unlock();
//						System.out.println(Thread.currentThread().getName() + " released the lock!");
					}
					
				}
			}
		}
		
		// 启动10个线程
		for(int i=0; i<10; i++)
		{
			Worker worker = new Worker();
			worker.setName("Thread-"+i);
//			worker.setDaemon(true);
			worker.start();
		}
		
		for(;;)
		{
			try {
				TimeUnit.SECONDS.sleep(1);
				System.out.println("============================================================");
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
//	public static void main(String[] args)
//	{
//		TwinsLockTest twinsLockTest = new TwinsLockTest();
//		twinsLockTest.test();
//	}
}
