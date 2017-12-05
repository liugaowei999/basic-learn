package proxy;

import java.io.FileOutputStream;
import java.lang.reflect.Proxy;

import sun.misc.ProxyGenerator;

public class DynamicProxyTest 
{

	public static void createProxyClassFile()
	{
		String name = "D:/ProxySubject";
		
		byte[] data = ProxyGenerator.generateProxyClass(name, new Class[] {Subject.class} );
		
		try{
			FileOutputStream out = new FileOutputStream(name + ".class");
			out.write(data);
			out.close();
		}
		catch( Exception e)
		{}
	}
	
	public static void main( String[] args )
	{

		RealSubject realSubject = new RealSubject();
		
		/**
		 * Returns an instance of a proxy class for the specified interfaces that dispatches method 
		 * invocations to the specified invocation handler. 
		 * 
		 * Proxy.newProxyInstance throws IllegalArgumentException for the same reasons that 
		 * Proxy.getProxyClass does.
		 * 
		 * Parameters:
		 *      loader: the class loader to define the proxy class
		 *      interfaces: the list of interfaces for the proxy class to implement
		 *      h: the invocation handler to dispatch method invocations to
		 * Returns:a proxy instance with the specified invocation handler of a proxy class that 
		 *         is defined by the specified class loader and that implements the specified interfaces
		 */
		Subject proxySubject = (Subject)Proxy.newProxyInstance(Subject.class.getClassLoader(), new Class[]{Subject.class}, new ProxyHandler(realSubject));
		
		proxySubject.doSomething();
		
		//write proxySubject class binary data to file
		createProxyClassFile();
	}
}
