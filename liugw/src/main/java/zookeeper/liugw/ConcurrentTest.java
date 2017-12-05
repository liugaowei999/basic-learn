package zookeeper.liugw;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 1. CopyOnWriteArrayList 介绍
 *    CopyOnWriteArrayList 是线程安全的。 CopyOnWrite并发容器用于读多写少的并发场景。
 *    CopyOnWriteArrayList使用了一种叫写时复制的方法，当有新元素添加到CopyOnWriteArrayList时，先从原有的数组中拷贝一份出来，
 *    然后在新的数组做写操作，写完之后，再将原来的数组引用指向到新数组。
 * 2. CopyOnWriteArrayList 的缺点
 *    （1）由于写操作的时候，需要拷贝数组，会消耗内存，如果原数组的内容比较多的情况下，可能导致young gc或者full gc。 可以使用ConcurrentHashMap代替。
 *    （2）不能用于实时读的场景，像拷贝数组、新增元素都需要时间，所以调用一个set操作后，读取到数据可能还是旧的,虽然CopyOnWriteArrayList 能做到最终一致性,
 *        但是还是没法满足实时性要求； CopyOnWrite容器只能保证数据的最终一致性，不能保证数据的实时一致性
 * 
 * 
 * 3. AtomicInteger
 *    AtomicInteger是一个提供原子操作的Integer类，通过线程安全的方式操作加减。AtomicInteger是在使用非阻塞算法实现并发控制，在一些高并发程序中非常适合
 *    在Java语言中，++i和i++操作并不是线程安全的，在使用的时候，不可避免的会用到synchronized关键字。而AtomicInteger则通过一种线程安全的加减操作接口。
 *    
 * 4. AtomicInteger 缺点
 *    AtomicInteger类的实现中使用了volatile， volatile在这里可以做到的作用是使得多个线程可以共享变量。 但是问题在于使用volatile将使得VM优化失去作用，
 *    导致效率较低，所以要在必要的时候使用，因此AtomicInteger类不要随意使用，要在使用场景下使用。
 * @author liugaowei
 *
 */
public class ConcurrentTest 
{
    private CountDownLatch startSignal = new CountDownLatch(1);//开始阀门
    private CountDownLatch doneSignal = null;//结束阀门
    private CopyOnWriteArrayList<Long> list = new CopyOnWriteArrayList<Long>();
    private AtomicInteger err = new AtomicInteger();//原子递增
    private ConcurrentTask[] task = null;
     
    public ConcurrentTest(ConcurrentTask... task)
    {
        this.task = task;
        if(task == null)
        {
            System.out.println("task can not null");
            System.exit(1);
        }
        doneSignal = new CountDownLatch(task.length);
        start();
    }
    
    /**
     * @param args
     * @throws ClassNotFoundException
     */
    private void start()
    {
        //创建线程，并将所有线程等待在阀门处
        createThread();
        
        //打开阀门
        startSignal.countDown();//递减锁存器的计数，如果计数到达零，则释放所有等待的线程
        try 
        {
            doneSignal.await();//等待所有线程都执行完毕
        } catch (InterruptedException e) 
        {
            e.printStackTrace();
        }
        
        //计算执行时间
        getExeTime();
    }
    
    /**
     * 初始化所有线程，并在阀门处等待
     */
    private void createThread() 
    {
        long len = doneSignal.getCount();
        for (int i = 0; i < len; i++) 
        {
            final int j = i;
            new Thread(new Runnable()
			           {
			                public void run() 
			                {
			                    try 
			                    {
			                        startSignal.await();//使当前线程在锁存器倒计数至零之前一直等待
			                        long start = System.currentTimeMillis();
			                        task[j].run();
			                        long end = (System.currentTimeMillis() - start);
			                        System.out.println("Thread " + Thread.currentThread().getId() + " run used time:" + end );
			                        list.add(end);
			                    } catch (Exception e) 
			                    {
			                        err.getAndIncrement();//相当于err++
			                    }
			                    doneSignal.countDown();
			                }
			           }
            ).start();
        }
    }
    /**
     * 计算平均响应时间
     */
    private void getExeTime() 
    {
        int size = list.size();
        List<Long> _list = new ArrayList<Long>(size);
        _list.addAll(list);
        Collections.sort(_list);
        long min = _list.get(0);
        long max = _list.get(size-1);
        long sum = 0L;
        for (Long t : _list) 
        {
            sum += t;
        }
        long avg = sum/size;
        System.out.println("min: " + min);
        System.out.println("max: " + max);
        System.out.println("avg: " + avg);
        System.out.println("err: " + err.get());
    }
     
    public interface ConcurrentTask 
    {
        void run();
    }
 
}