package Thread.liugw;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;

import sun.reflect.generics.tree.VoidDescriptor;

import java.sql.Connection;


public class DbConnectionPoolTest 
{

	static DbConnectionPool dbConnectionPool = new DbConnectionPool(10);
	
	// 保证所有的ConnectionRunner能够同时开始
	static CountDownLatch start = new CountDownLatch(1);
	
	// main 线程将会等待所有ConnectionRunner结束后才能继续执行
	static CountDownLatch end;
	
	
	static class ConnectionRunner implements Runnable
	{
		int count;
		AtomicInteger got;
		AtomicInteger notGot;
		
		public ConnectionRunner(int count, AtomicInteger got, AtomicInteger notGot)
		{
			this.count = count;
			this.got = got;
			this.notGot = notGot;
		}
		
		public void run()
		{
			try
			{
				/**
				 * Causes the current thread to wait until the latch has counted down to zero, 
				 * unless the thread is interrupted. If the current count is zero then this method 
				 * returns immediately. 
				 */
				start.await();
			}
			catch(Exception e)
			{
			}
			
			while( count > 0 )
			{
				try
				{
					// 从线程池中获取连接， 如果1000ms内无法获取到， 将返回null。
					// 分别统计连接获取的数据got和未获取到的数据 notGot
					Connection connection =  dbConnectionPool.fetchConnection(1000);
					if( connection != null )
					{
						try
						{
							connection.createStatement();
							connection.commit();
						}
						finally
						{
							dbConnectionPool.releaseConnection(connection);
							// Atomically increments by one the current value.
							// Returns:the updated value
							got.incrementAndGet();
						}
					}
					else
					{
						notGot.incrementAndGet();
					}
				}
				catch(Exception e)
				{
					e.printStackTrace();
				}
				finally
				{
					count --;
				}
			}
			
			/**
			 * Decrements the count of the latch, releasing all waiting threads if the count reaches zero. 
			 * If the current count is greater than zero then it is decremented. If the new count is zero 
			 * then all waiting threads are re-enabled for thread scheduling purposes. 
			 * 
			 * If the current count equals zero then nothing happens.
			 */
			end.countDown();
		}
	}


	public static void main(String[] args) throws InterruptedException
	{
		// 测试线程数量
		int threadCount = 10;
		end = new CountDownLatch(threadCount);
		
		int count = 20;
		AtomicInteger got = new AtomicInteger();
		AtomicInteger notGot = new AtomicInteger();
		
		for( int i=0; i<threadCount; i++ )
		{
			Thread thread = new Thread( new ConnectionRunner( count, got, notGot), "ConnectionRunnerThread_" + i);
			thread.start();
		}
		
		// 通过start Latch 控制上面启动的线程能够同一个时刻运行，同时去竞争数据库连接。
		start.countDown();
		end.await();
		
		System.out.println("total invoke: " + (threadCount * count));
		System.out.println("got connection: " + got);
		System.out.println("not got connection " + notGot);
	}

}
