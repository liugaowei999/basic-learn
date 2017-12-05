package Thread.liugw;

import java.util.concurrent.TimeUnit;

public class Interrupted 
{
	static Integer aa = 0;

	static class SleepRunner implements Runnable
	{
//		@Override
		public void run()
		{
			while(true)
			{
				try {
					TimeUnit.SECONDS.sleep(10);
					
//					synchronized (aa) {
//						aa.wait();
//					}
					
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
	
	static class BusyRunner implements Runnable
	{
		public void run()
		{
			while (true)
			{
				
			}
		}
	}


	public static void main( String[] args ) throws InterruptedException
	{
		// sleepThread不停的尝试睡眠
		Thread sleepThread = new Thread( new SleepRunner(), "sleepThread");
		sleepThread.setDaemon(true);
		
		// busyThread不停的运行
		Thread busyThread = new Thread( new BusyRunner(), "busyThread");
		busyThread.setDaemon(true);
		
		sleepThread.start();
		busyThread.start();
		
		// 休眠5秒，让sleepThread和busyThread充分运行
		TimeUnit.SECONDS.sleep(5);
		
		sleepThread.interrupt();
		busyThread.interrupt();
		
		System.out.println("sleepThread.isInterrupted()=" + sleepThread.isInterrupted());
		System.out.println("busyThread.isInterrupted()=" + busyThread.isInterrupted());
		TimeUnit.SECONDS.sleep(2);
	}
}
