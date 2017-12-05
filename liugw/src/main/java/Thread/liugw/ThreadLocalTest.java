package Thread.liugw;

import java.util.Random;
import java.util.concurrent.TimeUnit;


/**
 * ThreadLocal，即线程变量，是一个以ThreadLocal对象为键、任意对象为值的存储结构。这个结构被附带在线程上，
 * 也就是说一个线程可以根据一个ThreadLocal对象查询到绑定在这个线程上的一个值。
 * 可以通过set(T)方法来设置一个值，在当前线程下再通过get()方法获取到原先设置的值。
 * 
 * @author liugaowei
 *
 */
public class ThreadLocalTest 
{
	// 第一次get()方法调用时会进行初始化(如果set方法没有调用)， 每个线程会调用一次
	private static final ThreadLocal<Long> TIME_THREADLOCAL = new ThreadLocal<Long>(){
		protected Long initialValue()
		{
			return System.currentTimeMillis();
		}
	};
	
	public static final void begin()
	{
		TIME_THREADLOCAL.set(System.currentTimeMillis());
	}
	
	public static final long end()
	{
		return System.currentTimeMillis() - TIME_THREADLOCAL.get();
	}
	
	static class RunTest implements Runnable
	{
		public void run()
		{
			ThreadLocalTest.begin();
			try 
			{
				Random random = new Random();
				TimeUnit.SECONDS.sleep(random.nextInt(10)+1);
			} 
			catch (InterruptedException e) 
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			System.out.println(Thread.currentThread().getName() + " Cost: " + ThreadLocalTest.end() + " mills");
		}
	}
	
	public static void main( String[] args ) throws InterruptedException
	{
		ThreadLocalTest.begin();
		
		for( int i=0; i<10; i++)
		{
			Thread thread =  new Thread(new RunTest(), "Thread_"+i);
			thread.start();
		}
		
		TimeUnit.SECONDS.sleep(5);
//		ThreadLocalTest.begin();
		System.out.println(Thread.currentThread().getName() + "Cost: " + ThreadLocalTest.end() + " mills");
		
		
	}
}
