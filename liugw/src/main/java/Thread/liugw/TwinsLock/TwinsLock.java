package Thread.liugw.TwinsLock;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.AbstractQueuedSynchronizer;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

/**
 * 自定义同步组建
 * @author liugaowei
 *
 */
public class TwinsLock implements Lock
{
	
	/**
	 * 自定义同步器， 一般同步器作为内部类，进行同步组件的组合。
	 */
	private static class Sync extends AbstractQueuedSynchronizer
	{
		public Sync(int count)
		{
			if(count <= 0)
			{
				throw new IllegalArgumentException("Count must large than zero");
			}
			setState(count);
		}
		
		public int tryAcquireShared(int reduceCount)
		{
//			System.out.println(Thread.currentThread().getName() + " try to get the lock ...");
			for(;;)
			{
//				System.out.println(Thread.currentThread().getName() + " try to get the lock ...");
				int current = getState();
				int newCount = current - reduceCount;
				
				// hasQueuedPredecessors 实现公平锁
				if (newCount<0 || ( !hasQueuedPredecessors() && compareAndSetState(current, newCount) ))
				{
//					if (newCount>=0)
//					{
//						System.out.println(Thread.currentThread().getName() + " get lock success!");
//					}
					return newCount;
				}
			}
		}
		
		public boolean tryReleaseShared(int returnCount)
		{
//			System.out.println(Thread.currentThread().getName() + " try to releasing...");
			for(;;)
			{
				int current = getState();
				int newCount = current + returnCount;
				if (compareAndSetState(current, newCount))
				{
					System.out.println(Thread.currentThread().getName() + " released! " + getState());
					return true;
				}
			}
			
		}
		
		public Condition newCondition()
		{
			return new ConditionObject();
		}
	}
	
	// 支持两个线程同时获取同步状态（共享锁）
	private final Sync sync = new Sync(2);
	
//	@Override
	public void lock() {
		sync.acquireShared(1);
	}

//	@Override
	public void lockInterruptibly() throws InterruptedException {
		sync.acquireSharedInterruptibly(1);
	}

//	@Override
	public boolean tryLock() {
		return sync.tryAcquireShared(1) >= 0;
	}

//	@Override
	public boolean tryLock(long time, TimeUnit unit) throws InterruptedException {
		return sync.tryAcquireSharedNanos(1, unit.toNanos(time));
	}

//	@Override
	public void unlock() {
		sync.releaseShared(1);
	}

//	@Override
	public Condition newCondition() {
		return sync.newCondition();
	}

	
}
