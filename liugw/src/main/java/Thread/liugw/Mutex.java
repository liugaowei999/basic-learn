package Thread.liugw;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.AbstractQueuedSynchronizer;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;


/**
 * 独占锁Mutex是一个自定义同步组件，它在同一时刻只允许一个线程占有锁。
 * Mutex中定义了一个静态内部类，该内部类继承了同步器并实现了独占式获取和释放同步状态。
 * 在tryAcquire(int acquires)方法中，如果经过CAS设置成功（同步状态设置为1），则代表获取了同步状态，
 * 而在tryRelease(int releases)方法中只是将同步状态重置为0。
 * 
 * 用户使用Mutex时并不会直接和内部同步器的实现打交道，而是调用Mutex提供的方法，
 * 在Mutex的实现中，以获取锁的lock()方法为例，只需要在方法实现中调用同步器的模板方法acquire(int args)即可，
 * 当前线程调用该方法获取同步状态失败后会被加入到同步队列中等待，这样就大大降低了实现一个可靠自定义同步组件的门槛。
 * @author liugaowei
 *
 */
public class Mutex implements Lock{
	
	// 静态内部类，自定义同步器
	public static class Sync extends AbstractQueuedSynchronizer {
		// 是否处于占用状态  【重写isHeldExclusively()】
		protected boolean isHeldExclusively() {
			return getState()==1;
		}
		
		// 当状态为0的时候获取锁 。 【重写tryAcquire】
		public boolean tryAcquire(int acquires) {
			if (compareAndSetState(0, 1)) {
				setExclusiveOwnerThread(Thread.currentThread());
				return true;
			}
			return false;
		}
		
		/**
		 * 释放锁，将锁状态设置为0.【重写tryRelease】
		 * 释放锁的前提是已经获得了锁， 所以此处不需要使用CAS操作修改状态
		 */
		public boolean tryRelease(int releases) {
			// 当前线程不含有当前对象的锁(监视器)资源的时候， 抛出非法监视器（锁）状态异常
			if (getState() == 0) {
				throw new IllegalMonitorStateException();
			}
			/** 设置独占线程为null
			 *  Sets the thread that currently owns exclusive access. 
			 *  A null argument indicates that no thread owns access. 
			 *  This method does not otherwise impose any synchronization or volatile field accesses.
			 *  
			 *  Parameters:thread the owner thread
			 */
			setExclusiveOwnerThread(null);
			setState(0);
			return true;
		}
		
		/**
		 * 返回一个Condition，每个Condition都包含了一个condition队列
		 */
		Condition newCondition() {
			// java.util.concurrent.locks.AbstractQueuedSynchronizer.ConditionObject.ConditionObject()
			// Creates a new ConditionObject instance.
			return new ConditionObject();
		}
	}

	// 仅需要将操作代理到Sync自定义同步器上即可
	private final Sync sync = new Sync();
	
	/**
	 * Lock 类抽象方法的实现
	 */
	public void lock() {
		sync.acquire(1);
	}

	/**
	 * Lock 类抽象方法的实现
	 */
	public void lockInterruptibly() throws InterruptedException {
		sync.acquireInterruptibly(1);
	}

	/**
	 * Lock 类抽象方法的实现
	 */
	public boolean tryLock() {
		return sync.tryAcquire(1);
	}

	/**
	 * Lock 类抽象方法的实现
	 */
	public boolean tryLock(long time, TimeUnit unit) throws InterruptedException {
		return sync.tryAcquireNanos(1, unit.toNanos(time));
	}

	/**
	 * Lock 类抽象方法的实现
	 */
	public void unlock() {
		sync.release(0);
	}

	/**
	 * Lock 类抽象方法的实现
	 */
	public Condition newCondition() {
		return sync.newCondition();
	}

	//////////
	/**
	 * 自定义
	 * @return true：独占
	 */
	public boolean isLocked() {
		return sync.isHeldExclusively();
	}
	
	/**
	 * 自定义
	 * Queries whether any threads are waiting to acquire. 
	 * Note that because cancellations due to interrupts and timeouts may occur at any time, 
	 * a true return does not guarantee that any other thread will ever acquire. 
	 * @return
	 */
	public boolean hasQueuedThreads() {
		return sync.hasQueuedThreads();
	}
}
