package ordinary;

import java.util.*;

import org.apache.zookeeper.data.StatPersisted;
import org.jboss.netty.channel.AdaptiveReceiveBufferSizePredictorFactory;

enum AAA
{
	RED(1), GREEN(3), DDD(5);
	
	public  int ss ;
	AAA()
	{
		
	}
	AAA(int value)
	{
		ss = value;
		System.out.println(value);
	}
	
	
	
	public int speed(){
		return ss;
	}
}
class  ListIteratorTest<T>
{
	public void fun(T t){
		
	}
	public static void main(String[] args)
	{
//	  method_1();
	  
	  int  a = 0b1100;
	  int b = 123_456;
	  System.out.println(a + "," + b);
	  
	  AAA aaa = AAA.RED;
	  System.out.println(aaa.speed());
	}
	
	public static void method_1()
	{
	  ArrayList<String> al = new ArrayList();
	  
	  al.add("java1");
	  al.add("java2");
	  al.add("java3");
	  al.add("java4");
	  
	  ListIterator<String> li = al.listIterator();
//	  Object obj = li.next();
	  for (;li.hasNext(); )
	  {
		   Object obj = li.next();
		   if (obj.equals("java2"))
		   {
//			li.previous();
//			li.previous();
		    li.set("java23");  //.next()返回的是java2， 则将“java2”替换为“java23”
//		    break;
		   }
		   sop(obj);
//		   sop(li.previous());
	  }
	  li.add("xxx");
	  
	  sop(al);
	}
	 
	public static void sop(Object obj) 
	{
	  System.out.println(obj);
	}
}