package Thread.liugw.ThreadPool;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;



public class DefaultThreadPool<Job extends Runnable> implements ThreadPool<Job>
{
	
	class Worker implements Runnable
	{
		// 是否工作
		private volatile boolean running = true;
		
		public void run()
		{
			while( running )
			{
				Job job = null;
				synchronized( jobs )
				{
					// 如果工作列表是空的， 那么久wait
					while( jobs.isEmpty() )
					{
						try 
						{
							jobs.wait();
						} catch (InterruptedException e) 
						{
							// 外部如果发起对WorkThread的终端操作， 此处捕获到终端信号，主动执行终端， 并返回。
							Thread.currentThread().interrupt();
							return;
						}
					}
					
					// 取出一个job
					job = jobs.removeFirst();
				}
				
				// 执行取出的job
				if( job != null )
				{
					job.run();
				}
			}// end while
		}// end run
		
		public void shutDown()
		{
			running = false;
		}
	}

	// 线程池最大限制数
	private static final int MAX_WORK_NUMBERS = 10;
	
	// 线程池默认的数量
	private static final int DEFAULT_WORK_NUMBERS = 5;
	
	// 线程池最小的数量
	private static final int MIN_WORK_NUMBERS = 1;
	
	// 保存工作列表, 将会向里面不断地插入工作
	private final LinkedList<Job> jobs = new LinkedList<Job>();
	
	// 工作者列表
	private final List<Worker> workers = Collections.synchronizedList(new ArrayList<Worker>());
	
	// 工作者线程的数量
	private int workNum = DEFAULT_WORK_NUMBERS;
	
	// 线程编号 生成
	private AtomicLong threadNum = new AtomicLong();
	
	// 初始化线程工作者
	private void initializeWroks( int num )
	{
		for( int i=0; i<num ; i++ )
		{
			Worker worker = new Worker();
			workers.add(worker);
			Thread thread = new Thread(worker, "ThreadPool-Worker-" + threadNum.incrementAndGet());
			thread.start();
		}
	}
	
	public DefaultThreadPool()
	{
		initializeWroks(DEFAULT_WORK_NUMBERS);
	}
	
	public DefaultThreadPool( int num )
	{
		workNum = num > MAX_WORK_NUMBERS ? MAX_WORK_NUMBERS : num < MIN_WORK_NUMBERS ? MIN_WORK_NUMBERS : num;
		initializeWroks(workNum);
	}
	
//	@Override
	public void execute(Job job) 
	{
		if( job != null )
		{
			// 添加一个工作，然后进行通知
			synchronized(jobs)
			{
				jobs.addLast(job);
				// 此处不用notifyAll(),是为了减少开销， 避免将所有等待队列中的线程全部移入同步队列。
				jobs.notify();
			}
		}
	}
	
	
	public void shutDown()
	{
		for( Worker worker : workers)
		{
			worker.shutDown();
		}
	}
	
	public void addWorkders( int num )
	{
		synchronized( jobs )
		{
			// 限制新增的worker数量不能超过最大值
			if( num + this.workNum > MAX_WORK_NUMBERS )
			{
				num = MAX_WORK_NUMBERS - this.workNum;
			}
			initializeWroks( num );
			this.workNum += num;
		}
	}
	
	public void removeWorkers( int num )
	{
		synchronized(jobs)
		{
			if( num > MAX_WORK_NUMBERS )
			{
				throw new IllegalArgumentException("beyond workNum");
			}
			
			// 安装给定的数量停止worker
			int count = 0;
			while( count < num )
			{
				Worker worker = workers.get(count);
				if( workers.remove(worker))
				{
					worker.shutDown();
					count++;
				}
			}
			this.workNum -= count;
		}
	}
	
	public int getJobSize()
	{
		return jobs.size();
	}
}
