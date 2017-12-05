package Thread.liugw;

import java.sql.Time;
import java.util.concurrent.TimeUnit;

public class Join 
{

	static class Domino implements Runnable
	{
		private Thread thread;
		
		public Domino( Thread thread )
		{
			this.thread = thread;
		}
		
		public void print()
		{
			System.out.println("This is always main thread." + Thread.currentThread().getName());
		}
		
		public void run()
		{
			try 
			{
				System.out.println(Thread.currentThread().getName() + " Waiting [" + thread.getName() + "] terminate." );
				thread.join();
			} catch (InterruptedException e) 
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			System.out.println(Thread.currentThread().getName() + " terminate." );
		}
	}
	
	
	public static void main( String[] args )
	{
		Thread threadPrevious = Thread.currentThread();
		
		for( int i=0; i<10; i++ )
		{
			Domino dominoRunnal = new Domino(threadPrevious){
				public void print()
				{
					System.out.println("Rewirte print()");
				}
			};
			dominoRunnal.print();
			Thread thread = new Thread( dominoRunnal, "Thread_"+i);
			thread.start();
			threadPrevious = thread;
		}
		try 
		{
//			System.out.println("Sleeping 5 seconds ......" );
			TimeUnit.SECONDS.sleep(5);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println(Thread.currentThread().getName() + " terminate." );
	}
	
}
