package Thread.liugw;

import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 从JDK1.2起，就有了HashMap，正如前一篇文章所说，HashMap不是线程安全的，因此多线程操作时需要格外小心。
 * 在JDK1.5中，伟大的Doug Lea给我们带来了concurrent包，从此Map也有安全的了。
 * @author liugaowei
 *
 */
public class ConcurrentHashMapTest 
{  
    
    private static ConcurrentHashMap<Integer, Integer> map = new ConcurrentHashMap<Integer, Integer>();  
//    private static HashMap<Integer, Integer> map = new HashMap<Integer, Integer>();  
    
    public static void main(String[] args) 
    {  
    	Thread thread1 = new Thread("Thread1")
        {  
            @Override  
            public void run() 
            {  
            	System.out.println(getName() + " Started!");
            	for( int i=0; i<10000; i++ )
            		map.put(i, i);  
            }  
        };  
          
        Thread thread2 = new Thread("Thread2")
        {  
            @Override  
            public void run() 
            {  
            	System.out.println(getName() + " Started!");
            	for( int i=10000; i<20000; i++ )
            		map.put(i, i);  
            }  
        };  
          
        Thread thread3 = new Thread("Thread3")
//        new Thread("Thread3")
        {  
            @Override  
            public void run() 
            {  
            	System.out.println(getName() + " Started!");
            	for( int i=20000; i<30000; i++ )
            		map.put(i, i);  
            }  
        };  
        thread1.start();
        thread2.start();
        thread3.start();
        try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        System.out.println(map.size());  
    }  
}  
