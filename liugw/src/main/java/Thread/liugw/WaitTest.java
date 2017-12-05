package Thread.liugw;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * 如果线程A希望立即结束线程B，则可以对线程B对应的Thread实例调用interrupt方法。如果此刻线程B正在wait/sleep/join，
 * 则线程B会立刻抛出InterruptedException，在catch() {} 中直接return即可安全地结束线程。
 * 
 * 需要注意的是:InterruptedException是线程自己从内部抛出的，并不是interrupt()方法抛出的。对某一线程调用interrupt()时，
 * 如果该线程正在执行普通的代码，那么该线程根本就不会抛出InterruptedException。但是，一旦该线程进入到wait()/sleep()/join()后，
 * 就会立刻抛出InterruptedException。
 * @author liugaowei
 *
 */
public class WaitTest extends Object 
{

	   private List synchedList;
	   private List synchedList1;

	   public WaitTest() 
	   {
	      // create a new synchronized list to be used
	      synchedList = Collections.synchronizedList(new LinkedList());
	      synchedList1 = Collections.synchronizedList(new LinkedList());
	   }

	   // method used to remove an element from the list
	   public String removeElement() throws InterruptedException 
	   {
	      synchronized (synchedList) 
	      {

	         // while the list is empty, wait
//	         while (synchedList.isEmpty()) 
	    	 if (synchedList.isEmpty()) 
	         {
	            System.out.println("Thread:[" + Thread.currentThread().getName() + "] ----- List is empty...");
	            synchedList.wait();
	            System.out.println("Thread:[" + Thread.currentThread().getName() + "] Waiting...");
	         }

	    	 if (!synchedList.isEmpty()) 
	    	 {
	    		 String element = (String) synchedList.remove(0);
	    		 return element;
	    	 }
	         return null;

	         
	      }
	   }

	   // method to add an element in the list
	   public void addElement(String element) 
	   {
	      System.out.println("Thread:[" + Thread.currentThread().getName() + "] addElement Opening...");
	      synchronized (synchedList) 
	      {

	         // add an element and notify all that an element exists
	         synchedList.add(element);
	         System.out.println("Thread:[" + Thread.currentThread().getName() + "] Add New Element:'" + element + "'");

//	         synchedList.notify();
	         synchedList.notifyAll();
	         System.out.println("Thread:[" + Thread.currentThread().getName() + "] notifyAll called!");
	      }
	      System.out.println("Thread:[" + Thread.currentThread().getName() + "] Closing...");
	   }

	   public static void main(String[] args) 
	   {
	      final WaitTest demo = new WaitTest();

	      Runnable runRemove = new Runnable() 
	      {

	         public void run() 
	         {
	            try 
	            {
//	              while(true)
//	              {
		               String item = demo.removeElement();
		               System.out.println("Thread:[" + Thread.currentThread().getName() + "] remove elment:" + item);
		               TimeUnit.SECONDS.sleep(1);
//	              }
	            } catch (InterruptedException ix) 
	            {
	               System.out.println("Thread:[" + Thread.currentThread().getName() + "] Interrupted Exception! exit!");
	            } catch (Exception x) 
	            {
	            	x.printStackTrace();
	               System.out.println("Thread:[" + Thread.currentThread().getName() + "] Exception thrown.");
	            }
	         }
	      };

	      Runnable runAdd = new Runnable() 
	      {

	         // run adds an element in the list and starts the loop
	         public void run() 
	         {
        	    try 
        		{
//		        	while (true)
//		        	{
		        		demo.addElement("Hello!");
						TimeUnit.SECONDS.sleep(1);
						
//		        	}
        		} catch (InterruptedException e) 
        		{
					// TODO Auto-generated catch block
//					e.printStackTrace();
					System.out.println("Thread:[" + Thread.currentThread().getName() + "] Interrupted Exception! exit!");
				}
	         }
	      };

	      try {
	         Thread threadA1 = new Thread(runRemove, "REMOVE-A");
	         threadA1.start();

	         Thread.sleep(1000);

	         Thread threadA2 = new Thread(runRemove, "REMOVE-B");
	         threadA2.start();

	         Thread.sleep(5000);

	         Thread threadB = new Thread(runAdd, "ADD-C");
	         threadB.start();

	         Thread.sleep(10000);
	         Thread threadB2 = new Thread(runAdd, "ADD-D");
	         threadB2.start();

//	         threadA1.interrupt();
//	         threadA2.interrupt();
//	         threadB.interrupt();
	         
	      } catch (InterruptedException x) 
	      {
	      }
	   }
	}