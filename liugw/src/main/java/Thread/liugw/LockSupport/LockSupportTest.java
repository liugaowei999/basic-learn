package Thread.liugw.LockSupport;

import java.util.concurrent.locks.LockSupport;


/**
 * park和wait的区别:
 *    wait让线程阻塞前，必须通过synchronized获取同步锁。
 *    
 * @author liugaowei
 *
 */
public class LockSupportTest {

	private static Thread mainThread;
	
	public static void main(String[] args){
		// 获取主线程
		mainThread = Thread.currentThread();
		
		ThreadA tA = new ThreadA("ta");

		System.out.println(Thread.currentThread().getName()+" start ta");
		tA.start();
		
		System.out.println(Thread.currentThread().getName()+" block");
//		LockSupport.park(mainThread); // Thread 对象作为阻塞对象
		LockSupport.park(tA); // 默认阻塞当前线程
		
		System.out.println(Thread.currentThread().getName()+" continue");
	}
	
	static class ThreadA extends Thread{
		public ThreadA(String name){
			super.setName(name);
			// super(name);
		}
		
		public void run(){
			System.out.println(Thread.currentThread().getName()+" wakup others");
			LockSupport.unpark(mainThread);
		}
	}
}
