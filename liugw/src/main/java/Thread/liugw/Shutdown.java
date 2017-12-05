package Thread.liugw;

import java.util.concurrent.TimeUnit;

public class Shutdown 
{
	public static void main(String[] args) throws Exception 
	{
		Runner one = new Runner();
		Thread countThread = new Thread(one, "CountThread1");
		countThread.start();
		// 睡眠1秒，main线程对CountThread进行中断，使CountThread能够感知中断而结束
		TimeUnit.SECONDS.sleep(1);
		countThread.interrupt();
		TimeUnit.SECONDS.sleep(2);
		Runner two = new Runner();
		countThread = new Thread(two, "CountThread2");
		countThread.start();
		// 睡眠1秒，main线程对Runner two进行取消，使CountThread能够感知on为false而结束
		TimeUnit.SECONDS.sleep(1);
		two.cancel();
//		one.cancel();
	}
	private static class Runner implements Runnable 
	{
		private long i;
		private volatile boolean on = true;
//		@Override
		public void run() 
		{
			
			while (on && !Thread.currentThread().isInterrupted())
			{
				i++;
//				try {
//					// 不能有sleep 或者wait， 否则 isInterrupted 会被sleep重置为false， 导致死循环
//					TimeUnit.SECONDS.sleep(1);
//				} catch (InterruptedException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
			}
			
			System.out.println(Thread.currentThread().getName() + " Count i = " + i);
		}
		public void cancel() 
		{
			on = false;
		}
	}
}