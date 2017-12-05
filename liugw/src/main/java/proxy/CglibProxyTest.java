package proxy;

import net.sf.cglib.proxy.Enhancer;

public class CglibProxyTest 
{

	public static void main(String[] args) 
	{
		CglibProxy cglibProxy = new CglibProxy();
		Enhancer enhancer = new Enhancer(); 
		enhancer.setSuperclass(RealSubject.class);
		enhancer.setCallback(cglibProxy);
		
		Subject subject = (Subject)enhancer.create();
		subject.doSomething();
	}
}
