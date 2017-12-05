package Thread.liugw;

import java.util.LinkedList;

import java.sql.Connection;

public class DbConnectionPool 
{

	private LinkedList<Connection> pool = new LinkedList<Connection>();
	
	public DbConnectionPool( int initialSize )
	{
		if( initialSize > 0 )
		{
			for( int i=0; i<initialSize; i++)
			{
				pool.addLast(DbConnectionDriver.CreateConnection());
			}
		}
	}
	
	public void releaseConnection(Connection connection)
	{
		if( connection != null )
		{
			synchronized(pool)
			{
				pool.addLast(connection);
				// 连接释放后需要进行通知， 这样其他消费者能够感知到连接池中已经归还了一个链接。
				pool.notifyAll();
			}
		}
	}
	
	// 在n毫秒内无法获取到连接， 返回null
	public Connection fetchConnection(long millons) throws InterruptedException
	{
		synchronized(pool)
		{
			// 忽略超时
			if( millons <= 0 )
			{
				while( pool.isEmpty() )
				{
					pool.wait(); // 进入等待队列，等待获取连接
				}
				return pool.removeFirst(); // return and remove the first element from the list.
			}
			else
			{
				long future = System.currentTimeMillis() + millons; // 超时的时间点
				long remains = millons; // 需要等待的毫秒数
				while( pool.isEmpty() && remains > 0 )
				{
					pool.wait(remains);
					remains = System.currentTimeMillis() - future;
				}
				Connection result = null;
				if( !pool.isEmpty() )
				{
					result = pool.removeFirst();
				}
				return result;
			}
		}
	}
}
