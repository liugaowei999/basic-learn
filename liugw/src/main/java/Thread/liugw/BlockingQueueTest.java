package Thread.liugw;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class BlockingQueueTest 
{
    public static void main(String[] args) 
    {
        final BlockingQueue queue = new ArrayBlockingQueue(30);
//        long aelement = 0;
        for(int i=0;i<2;i++)
        {
            new Thread()
            {
            	double aelement = 0 ;
                public void run()
                {
                    while(true)
                    {
                        try 
                        {
                            Thread.sleep((long)(Math.random()*10000));
                            aelement = Math.random() *10;
                            System.out.println("PUT:" + Thread.currentThread().getName() + "准备放数据! [" + aelement + "]");                            
                            queue.put(aelement);
                            System.out.println("PUT:" + Thread.currentThread().getName() + "已经放了数据，队列目前有" + queue.size() + "个数据");
                        } catch (InterruptedException e) 
                        {
                            e.printStackTrace();
                        }

                    }
                }
                
            }.start();
        }
        
        new Thread()
        {
        	Double xx ;
            public void run()
            {
                while(true)
                {
                    try 
                    {
                        //将此处的睡眠时间分别改为100和1000，观察运行结果
                        Thread.sleep(100);
                        System.out.println("GET:" + Thread.currentThread().getName() + "准备取数据!");
                        xx = (Double)queue.take();
                        System.out.println("GET:" + Thread.currentThread().getName() + "取到的数据： [" + xx + "], 已经取走数据，队列目前有" + queue.size() + "个数据");                    
                    } catch (InterruptedException e) 
                    {
                        e.printStackTrace();
                    }
                }
            }
            
        }.start();            
    }
}