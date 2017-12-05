package Thread.liugw;

import java.util.concurrent.atomic.AtomicInteger;

import com.fasterxml.jackson.core.util.DefaultIndenter;

public class AtomicIntegerCompareTest 
{  
    private int value = 0;  
      
    public AtomicIntegerCompareTest(int value)
    {  
        this.value = value;  
    }  
      
    public synchronized int increase()
    {  
        return value++;  
    }  
      
    public static void main(String args[])
    { 
    	long count = 1000;
    	
        long start = System.currentTimeMillis();  
          
        AtomicIntegerCompareTest test = new AtomicIntegerCompareTest(0);  
        for( int i=0;i< count;i++)
        {  
            test.increase();  
        }  
        long end = System.currentTimeMillis();  
        System.out.println("synchronized int time elapse:"+(end -start));  
        System.out.println("value:" + test.value);  
          
        start = System.currentTimeMillis();  
          
        AtomicInteger atomic = new AtomicInteger(0);  
          
        for( int i=0;i< count;i++)
        {  
            atomic.incrementAndGet();  
        }  
        end = System.currentTimeMillis();  
        System.out.println("AtomicInteger time elapse:"+(end -start) );  
        
        //////////////////////////////////////////////////////////////
        start = System.currentTimeMillis();  
        for( int i=0;i< 1000000000;i++)
        {  
        	test.increase();  
        }  
        end = System.currentTimeMillis();  
        System.out.println("synchronized int2 time elapse:"+(end -start) );  
        
        atomic.set(0);
        start = System.currentTimeMillis();  
        for( int i=0;i< 1000000000;i++)
        {  
            atomic.incrementAndGet();  
        }  
        end = System.currentTimeMillis();  
        System.out.println("AtomicInteger2 time elapse:"+(end -start) );  
   
    }  
} 
