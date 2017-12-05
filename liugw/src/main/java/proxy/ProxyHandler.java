package proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class ProxyHandler implements InvocationHandler 
{
	private Object proxied;
	
	public ProxyHandler( Object proxied )
	{
		this.proxied = proxied;
	}

	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable 
	{
		//在转调具体目标对象之前，可以执行一些功能处理
		
		//转调具体目标对象的方法
		/*
		 * Invokes the underlying method represented by this Method object, 
		 * on the specified object with the specified parameters. 
		 * Individual parameters are automatically unwrapped to match primitive formal parameters, 
		 * and both primitive and reference parameters are subject to method invocation conversions 
		 * as necessary. If the underlying method is static, then the specified obj argument is ignored. 
		 * It may be null. 
		 * 
		 * If the number of formal parameters required by the underlying method is 0, 
		 * the supplied args array may be of length 0 or null. 
		 * 
		 * If the underlying method is an instance method, 
		 * it is invoked using dynamic method lookup as documented in The Java Language Specification, 
		 * Second Edition, section 15.12.4.4; in particular, 
		 * overriding based on the runtime type of the target object will occur. 
		 * 
		 * If the underlying method is static, the class that declared the method is 
		 * initialized if it has not already been initialized. 
		 * 
		 * If the method completes normally, the value it returns is returned to the caller of invoke; 
		 * if the value has a primitive type, it is first appropriately wrapped in an object. 
		 * However, if the value has the type of an array of a primitive type, 
		 * the elements of the array are not wrapped in objects; in other words, 
		 * an array of primitive type is returned. If the underlying method return type is void, 
		 * the invocation returns null.
		 */
		
		System.out.println("method.getName()=" + method.getName());
		return method.invoke(proxied, args);
		
		// TODO Auto-generated method stub
//		return null;
	}

}
