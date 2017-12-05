
package proxy;

import com.sun.accessibility.internal.resources.accessibility_ja;

public class InnerClass 
{
	static
	{
		System.out.println("static InnerClass contructor!");
	}
	
	public InnerClass()
	{
		System.out.println("InnerClass contructor!");
	}
	
	public void modifyStr( String string) {
		string = "Had modified!";
	}

	public void test()
	{
		System.out.println(this.getClass().getName()+" .test() " + this.getClass().getSimpleName());
	}
	
	public static void testStatic()
	{
		System.out.println( " .testStatic()");
	}
	
	class BClass
	{
		
		public BClass()
		{
			System.out.println("BClass contructor!");
		}
		public void test()
		{
			System.out.println(this.getClass().getName()+" .test()");
		}
		
		public void funB()
		{
			InnerClass.this.test();
			InnerClass.testStatic();
			this.test();
			test();
		}
	}
	
	class CClass extends BClass
	{
		
	}
	
	public void funA()
	{
		BClass bClass = new BClass();
		bClass.funB();
	}
	
	public static void main(String[] args)
	{
		System.out.println("=============================================");
		InnerClass innerClass = new InnerClass();
		String string = "11Original string22";
		String string1 =new String( "Original string");
		innerClass.modifyStr(string);
		innerClass.modifyStr(string1);
		System.out.println(string);
		System.out.println(string1);
		System.out.println("=============================================");
		DClass dClass1 = new DClass();
		System.out.println("=============================================");
		DClass dClass2 = new DClass();
		System.out.println("=============================================");
//		innerClass.funA();
//		innerClass.test();
//		
//		Class<InnerClass> a = InnerClass.class;
//		System.out.println(a.getName());
//		
//		try 
//		{
//			InnerClass b = a.newInstance();
//			InnerClass c = (InnerClass)Class.forName("proxy.InnerClass").newInstance();
//			b.test();
//			c.test();
//			
//			Class<?> ic;
//			ic = Double.class;
//			ic = InnerClass.class;
//			
//			Class<?> classType = Class.forName("java.lang.String");
//			Class STR = Class.forName("java.lang.String");
////			STR str = STR.newInstance();
//
//
//			
//		} catch (InstantiationException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (IllegalAccessException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (ClassNotFoundException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		
		
	}
}

class DClass extends InnerClass
{
	static{
		System.out.println("static DClass constuct!");
	}
	
	public DClass(){
		System.out.println("DClass constuct!");
	}
}
