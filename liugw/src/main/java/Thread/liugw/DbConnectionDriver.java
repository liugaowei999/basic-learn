package Thread.liugw;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.sql.Connection;
import java.util.concurrent.TimeUnit;


/**
 * 由于java.sql.Connection是一个接口，最终的实现是由数据库驱动提供方来实现的，
 * 考虑到只是个示例，我们通过动态代理构造了一个Connection，该Connection的代理实现仅仅是在commit()方法调用时休眠100毫秒
 * 
 * @author liugaowei
 *
 */
public class DbConnectionDriver 
{

	static class DbConnectionHandler implements InvocationHandler
	{
		public Object invoke( Object proxy, Method method, Object[] args ) throws InterruptedException
		{
			if( method.getName().equals("commit") )
			{
				System.out.println(Thread.currentThread().getName() + ", " + method.getName() +  "  invoke!");
				TimeUnit.MILLISECONDS.sleep(100);
			}
			else
			{
				System.out.println(Thread.currentThread().getName() + ", " + method.getName() + "  invoke!");
			}
			return null;
		}
	}
	
	// 创建一个 Connection 代理， 在Commit时休眠100ms(毫秒)
	public static final Connection CreateConnection()
	{
		return (Connection) Proxy.newProxyInstance(DbConnectionDriver.class.getClassLoader(), 
				new Class<?>[] {Connection.class}, new DbConnectionHandler());
	}
}
