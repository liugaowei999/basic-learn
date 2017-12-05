package ordinary;

import java.util.ArrayList;
import java.util.List;

/**
 * Run time Out Of Memory --- Constant Pool
 * VM Args: -XX:PermSize=10M -XX:MaxPermSize=10M
 * 常量池溢出测试 --------- 方法区中的常量池
 * @author liugaowei
 *
 */
public class JavaRuntimeConstantOOM {

	public static void main(String[] args)
	{
		// 使用List保持着常量池的引用， 避免 FULL GC回收常量池
		List<String> list = new ArrayList<>();
		System.out.println(Runtime.getRuntime().availableProcessors());
		System.out.println(System.getProperty("java.vm.name"));
//		System.out.println(System.getProperties());
		int i=0;
		try
		{
			while(true)
			{
				/**
				 * When the intern method is invoked, if the pool already contains a string equal to 
				 * this String object as determined by the equals(Object) method, then the string from 
				 * the pool is returned. 
				 * Otherwise, this String object is added to the pool and a reference to this String 
				 * object is returned. 
				 * It follows that for any two strings s and t, 
				 * s.intern() == t.intern() is true if and only if s.equals(t) is true. 
				 */
				list.add(("dfdsafdsafdsaf" + String.valueOf(i++)).intern());
			}
		}finally {
			System.out.println("String count:"+i);
		}
	}
}
