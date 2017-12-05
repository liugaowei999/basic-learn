package Thread.liugw.ReadWriteLock;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class Cache 
{

	static Map<String, Object> map = new HashMap<>();
	static ReentrantReadWriteLock readWriteLock = new ReentrantReadWriteLock();
	
	static Lock readLock = readWriteLock.readLock();
	static Lock writeLock = readWriteLock.writeLock();
	
	// 获取一个key对应的value
	public static final Object get(String key)
	{
		readLock.lock();
		try
		{
			return map.get(key);
		}finally{
			readLock.unlock();
		}
	}
	
	public void readLock()
	{
		readLock.lock();
	}
	
	public void readUnlock()
	{
		readLock.unlock();
	}
	
	public void writeLock()
	{
		writeLock.lock();
	}
	
	public void writeUnlock()
	{
		writeLock.unlock();
	}
	
	// 设置key对应的value，并返回旧的value
	public static final Object put(String key, Object value)
	{
		writeLock.lock();
		try{
			return map.put(key, value);
		}finally {
			writeLock.unlock();
		}
	}
	
	// 清空所有的内容
	public static final void clear()
	{
		writeLock.lock();
		try{
			map.clear();
		}finally {
			writeLock.unlock();
		}
	}
	
	static class ReadRunnable implements Runnable
	{
		String key;
		Cache cache;
		public ReadRunnable(String key, Cache cache)
		{
			this.key = key;
			this.cache = cache;
		}
		public void run()
		{
			cache.readLock();
			System.out.println( Thread.currentThread().getName() + ", Read Value:" +cache.get(key));
			try 
			{
				TimeUnit.SECONDS.sleep(3);
				System.out.println(Thread.currentThread().getName() +" sleep finish!");
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			cache.readUnlock();
		}
	}
	
	static class WriteRunnable implements Runnable
	{
		String key;
		String value;
		Cache cache;
		public WriteRunnable(String key, String value, Cache cache)
		{
			this.key = key;
			this.value = value;
			this.cache = cache;
		}
		public void run()
		{
			cache.writeLock();
			System.out.println( Thread.currentThread().getName() + " , Old Value:" + cache.put(key, value) + ", New value:" + cache.get(key) );
			
			try 
			{
				TimeUnit.SECONDS.sleep(3);
				System.out.println(Thread.currentThread().getName() +" update finish!");
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			cache.writeUnlock();
		}
	}
	
	public static void main(String[] args)
	{
		System.out.printf("%x, %x, %x \n", (1<<16)-1, 1<<16, (65536)>>16);
		Cache cache = new Cache();
		cache.put("name", "mary");
		for( int i=0; i<10;i++ )
		{
			Thread threadread = new Thread(new ReadRunnable("name", cache), "ReadThread_"+i);
			Thread threadwrite = new Thread(new WriteRunnable("name", "stu_" + i, cache), "WriteThread_"+i);
			threadread.start();
//			threadwrite.start();
		}
		
		try 
		{
			TimeUnit.SECONDS.sleep(100);
			System.out.println(Thread.currentThread().getName() +"  finish!");
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
