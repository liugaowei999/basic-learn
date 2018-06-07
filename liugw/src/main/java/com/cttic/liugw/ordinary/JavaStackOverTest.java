package com.cttic.liugw.ordinary;

public class JavaStackOverTest 
{

	private int stackLength = 1;
	private int threadNum = 1;
	
	public void stackLeak()
	{
		stackLength++;
		stackLeak();
	}
	
	public void stackLeakByThread()
	{
		while(true)
		{
			Thread thread = new Thread(new Runnable() {
				
				@Override
				public void run() {
					// TODO Auto-generated method stub
					while(true)
					{
						// don't stop
					}
				}
			});
			
			thread.start();
		}
	}
	
	public static void main(String[] args) throws Throwable
	{
		JavaStackOverTest javaStackOverTest = new JavaStackOverTest();
		try{
//			javaStackOverTest.stackLeak();
			javaStackOverTest.stackLeakByThread();
		}
		catch(Throwable e)
		{
			System.out.println("Stack length:" + javaStackOverTest.stackLength);
			System.out.println("Thread num:" + javaStackOverTest.threadNum);
			throw e;
		}
	}
}
