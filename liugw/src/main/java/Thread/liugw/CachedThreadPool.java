package Thread.liugw;

import Thread.liugw.ThreadRunnabl;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
 
public class CachedThreadPool {
    public static void main(String[] args) 
    {
       ExecutorService exec = Executors.newCachedThreadPool();
       for (int i = 0; i < 5; i++)
       {
           exec.execute(new ThreadRunnabl(i));
       }
       exec.shutdown();
    }
}