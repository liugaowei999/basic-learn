package Thread.liugw;

import java.util.concurrent.TimeUnit;

/**
 * 注意：
 * 只有一个Daemon 线程运行没有非Dameon线程运行时，JVM会退出，退出时Daemon线程不一定会执行 fininally代码块。
 * @author liugaowei
 *
 */
public class Daemon 
{
	static class DaemonRunner implements Runnable
	{
//		@Override
		public void run()
		{
			try
			{
				System.out.println("DaemonThread running...");
				TimeUnit.SECONDS.sleep(10);
			}
			catch (InterruptedException e) 
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			finally 
			{
				System.out.println("DaemonThread finally run.");
			}
		}
	}

	public static void main( String[] args ) throws InterruptedException
	{
		Thread thread = new Thread( new DaemonRunner(), "DaemonThread" );
		thread.setDaemon(true);
		thread.start();
		
//		TimeUnit.SECONDS.sleep(20);
	}
}
