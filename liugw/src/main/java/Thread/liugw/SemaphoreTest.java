package Thread.liugw;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;

/**
 * 1. Mutex是一把钥匙，一个人拿了就可进入一个房间，出来的时候把钥匙交给队列的第一个。
 * 一般的用法是用于串行化对critical section代码的访问，保证这段代码不会被并行的运行。
 * 
 * 2. Semaphore是一件可以容纳N人的房间，如果人不满就可以进去，如果人满了，就要等待有人出来。
 * 对于N=1的情况，称为binary semaphore。一般的用法是，用于限制对于某一资源的同时访问。
 * 通过设定许可个数， 可以控制 同时运行的线程数量
 * 
 * 
 * 
 * Binary semaphore与Mutex的差异：
 *            在 有的系统中Binary semaphore与Mutex是没有差异的。在有的系统上，主要的差异是mutex一定要由获得锁的进程来释放。
 *            而semaphore可以由其它进程释 放（这时的semaphore实际就是个原子的变量，大家可以加或减），因此semaphore可以用于进程间同步。
 *            Semaphore的同步功能是所有 系统都支持的，而Mutex能否由其他进程释放则未定，因此建议mutex只用于保护critical section。
 *            而semaphore则用于保护某变量，或者同步。
 * @author liugaowei
 *
 */
public class SemaphoreTest 
{

    public static void main(String[] args) throws InterruptedException 
    {  
       // 线程池 
       ExecutorService exec = Executors.newCachedThreadPool();  
       final CountDownLatch countDownLatch = new CountDownLatch(20);
       // 只能5个线程同时访问 
       final Semaphore semp = new Semaphore(5);  
       long begin = System.currentTimeMillis();
       // 模拟20个客户端访问 
       for (int index = 0; index < 20; index++) 
       {
           final int NO = index;  
           Runnable run = new Runnable() 
           {  
               public void run() 
               {  
                   try 
                   {  
                       // 获取许可 
                       semp.acquire();  
                       System.out.println("Accessing: " + NO);  
//                       Thread.sleep((long) (Math.random() * 10000));  
                       System.out.println(semp.getQueueLength());
                       Thread.sleep(1000);
                       // 访问完后，释放 ，如果屏蔽下面的语句，则在控制台只能打印5条记录，之后线程一直阻塞
                       
                       semp.release();  
                       System.out.println(semp.getQueueLength());
                       countDownLatch.countDown();
                   } 
                   catch (InterruptedException e) 
                   {  
                   }  
               }  
           };  
           exec.execute(run);  
       }  
       // 退出线程池 
       exec.shutdown();  
       countDownLatch.await();
       long end = System.currentTimeMillis();
       
       System.out.println("consume:" + (end-begin));
   }  
}