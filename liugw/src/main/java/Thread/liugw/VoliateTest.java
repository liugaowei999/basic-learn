package Thread.liugw;

import java.util.concurrent.CountDownLatch;

import com.sun.org.apache.bcel.internal.generic.NEW;

/**
 * 用在多线程，同步变量。 线程为了提高效率，将成员变量(如A)某拷贝了一份（如B），线程中对A的访问其实访问的是B。只在某些动作时才进行A和B的同步。
 * 因此存在A和B不一致的情况。volatile就是用来避免这种情况的。volatile告诉jvm， 它所修饰的变量不保留拷贝，直接访问主内存中的（也就是上面说的A) ，
 * 但是不能用其来进行多线程同步控制.
 * 
 * 示例中 count++ 代码块必须使用synchronized进行并发控制
 * 
 * synchronized(this)
 * synchronized(this)
                             －＞如果不同线程监视同一个实例对象，就会等待，如果不同的实例，不会等待．
 * synchronized(class)
 * synchronized(class)
                             －＞如果不同线程监视同一个实例或者不同的实例对象，都会等待．
 * @author liugaowei
 *
 */
public class VoliateTest 
{
	public volatile  static int count = 0;  
    
    public synchronized void inc() 
    {  
        //这里延迟5毫秒，使得结果明显  
        try 
        {  
            Thread.sleep(5);  
        } catch (InterruptedException e) 
        {  
        }  
//        synchronized( this ) 
        synchronized (VoliateTest.class) // 显示使用获取class做为监视器．它与static synchronized method隐式获取class监视器一样．
        {  
            count ++;  
        }  
    }  
   
    public static void main(String[] args) throws InterruptedException 
    {  
        final CountDownLatch latch = new CountDownLatch(1000);  
//        final VoliateTest v1 = new VoliateTest(); // synchronized( this ) 测试
        VoliateTest v1 = new VoliateTest();  
        //同时启动1000个线程，去进行i++计算，看看实际结果  
        for (int i = 0; i < 1000; i++) 
        {  
            new Thread(new Runnable() 
            {  
            	
//                @Override  
                public void run() 
                {  
                	v1.inc();  
                    latch.countDown();  
                }  
            }).start();  
        }  
        latch.await();  
        //这里每次运行的值都有可能不同,可能为1000  
        System.out.println("运行结果:Counter.count=" + VoliateTest.count);  
    }  
}
