package ordinary;

import java.lang.reflect.Field;

import net.jpountz.util.UnsafeUtils;

/**
 * 使用Unsafe分配本机内存
 * VM args: -Xmx20M -XX:MaxDirectMemorySize=10M
 * @author liugaowei
 *
 */
public class DirectMemoryOOM 
{

	private static final int _1M = 1 * 1024 *1024;
	
	public  static void main(String[] args) throws IllegalArgumentException, IllegalAccessException
	{
		Field unsafeField = UnsafeUtils.class.getDeclaredFields()[0];
		unsafeField.setAccessible(true);
		UnsafeUtils unsafeUtils = (UnsafeUtils)unsafeField.get(null);
		
//		while(true)
//		{
//			unsafeUtils.allocateMemory(_1MB);
//		}
	}
}
