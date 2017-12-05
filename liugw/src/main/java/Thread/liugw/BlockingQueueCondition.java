package Thread.liugw;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class BlockingQueueCondition 
{

    public static void main(String[] args) 
    {
    	System.out.println(Thread.currentThread().getName() + " Started!");
        ExecutorService service = Executors.newSingleThreadExecutor();
        final Business3 business = new Business3();
        service.execute(new Runnable()
        {

            public void run() 
            {
            	System.out.println(Thread.currentThread().getName() + " Started!");
                for(int i=0;i<50;i++)
                {
                	System.out.println(Thread.currentThread().getName() + " ----- [" + i + "]");
                    business.sub();
                }
                System.out.println(Thread.currentThread().getName() + " Exit!");
            }
            
        });
        
        for(int i=0;i<50;i++)
        {
        	System.out.println(Thread.currentThread().getName() + " ----- [" + i + "]");
            business.main();
        }
        System.out.println(Thread.currentThread().getName() + " Exit!");
    }

}

class Business3
{
    BlockingQueue subQueue = new ArrayBlockingQueue(1);
    BlockingQueue mainQueue = new ArrayBlockingQueue(1);
    //这里是匿名构造方法，只要new一个对象都会调用这个匿名构造方法，它与静态块不同，静态块只会执行一次，
    //在类第一次加载到JVM的时候执行
    //这里主要是让main线程首先put一个，就有东西可以取，如果不加这个匿名构造方法put一个的话程序就死锁了
    {
        try 
        {
            mainQueue.put(1);
        } catch (InterruptedException e) 
        {
            e.printStackTrace();
        }
    }
    public void sub()
    {
        try
        {
            mainQueue.take();
            for(int i=0;i<10;i++)
            {
                System.out.println(Thread.currentThread().getName() + " : " + i);
            }
            subQueue.put(1);
        }catch(Exception e)
        {

        }
    }
    
    public void main()
    {
        try
        {
            subQueue.take();
            for(int i=0;i<5;i++)
            {
                System.out.println(Thread.currentThread().getName() + " : " + i);
            }
            mainQueue.put(1);
        }catch(Exception e)
        {
        }        
    }
}