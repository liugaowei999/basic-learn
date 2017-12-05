package gc;

public class NotInitialization 
{

	///  非主动使用类字段
	// 对于静态字段， 只有直接定义它的类才会被初始化
	public static void main( String[] args )
	{
		// 只触发父类的初始化
//		System.out.println(SubClass.value);
		
		// 对于常量的访问，不会触发父类 和 子类的初始化 。 java虚拟机在编译阶段将此 常量值存储到了 NotInitialization类本身的常量池中，其实和 类SubClass定义已经没有什么关系了。 常量存在的常量池中。
		System.out.println(SubClass.helloworld);
		
//		SuperClass[] superClass = new SuperClass[10];
	}
}
