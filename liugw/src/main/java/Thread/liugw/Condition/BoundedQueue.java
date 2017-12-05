package Thread.liugw.Condition;

import java.util.Collection;
import java.util.Random;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.apache.commons.lang.builder.ToStringBuilder;

public class BoundedQueue<T> 
{

	private Object[] items;
	
	// 添加的下标 ， 删除的下标， 和数组当前的数量
	private int addIndex, removeIndex, count;
	
	private Lock lock = new ReentrantLock();
	private Condition notEmpty = lock.newCondition();
	private Condition notFull = lock.newCondition();
	
	public BoundedQueue(int size)
	{
		items = new Object[size];
	}
	
	// 写入元素， 如果数组已经满， 则进入等待
	public <T extends A> void add(T t) throws InterruptedException
	{
		lock.lock();
		try{
			while(count == items.length)
			{
				notFull.await();
			}
			
			items[addIndex] = t;
			
			// 有界队列写到结尾处进行位置重置，从头开始写，循环使用。 
			if (++addIndex == items.length)
			{
				addIndex = 0;
			}
			count++;
			// 发起写入通知给等待的读进程
			notEmpty.signal();
//			System.out.println("add finish. signal send!");
//			Thread.sleep(2000);
		}finally{
			lock.unlock();
		}
	}
	
	// 删除元素并返回删除的元素。 队列为空时等待
	@SuppressWarnings("unchecked")
	public T remove() throws InterruptedException
	{
		lock.lock();
		try{
			while(count==0)
			{
				notEmpty.await();
			}
			
			Object xObject = items[removeIndex];
			
			if (++removeIndex == items.length)
			{
				removeIndex = 0;
			}
			
			--count;
			notFull.signal();
			return (T)xObject;
		}finally{
			lock.unlock();
		}
	}
	
	static class A{
		private String string;
		public A(String string)
		{
			this.string = string;
		}
		
		public String toString()
		{
			return "content:"+string;
		}
	}
	
	static class AddRunnale implements Runnable
	{
		Object queue ;
		public AddRunnale(Object queue)
		{
			this.queue = queue;
		}
		public void run()
		{
			Random random = new Random();

			try {
				while(true)
				{
					String string = "ddddd" + random.nextInt(100);
//					((BoundedQueue)queue).add(string);
					((BoundedQueue)queue).add(new A(string));
				}
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	static class RemoveRunnale implements Runnable
	{
		Object queue ;
		public RemoveRunnale(Object queue)
		{
			this.queue = queue;
		}
		public void run()
		{
			
			try 
			{
				while(true)
				{
					Object object = ((BoundedQueue)queue).remove();
					System.out.println("remove :" + object);
				}
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public static void main(String[] args)
	{
		BoundedQueue<A> boundedQueue = new BoundedQueue(5);
		AddRunnale addrunable = new AddRunnale(boundedQueue);
		Thread addTread = new Thread(addrunable, "addTread");
		
		Thread delTread = new Thread(new RemoveRunnale(boundedQueue), "delTread");
		
		addTread.start();
		delTread.start();
	}
}
