package Thread.liugw;

import java.util.concurrent.Exchanger;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ExchangeTest 
{

	private static final Exchanger<String> exchanger = new Exchanger<>();
	private static ExecutorService threadPool = Executors.newFixedThreadPool(2);
	
	public static void main(String[] args) throws InterruptedException {
		
		threadPool.execute(new Runnable() {
			
			@Override
			public void run() 
			{
				String A = "银行流水A"; // A录入银行流水数据
				String A1 = "银行流水B"; // A录入银行流水数据
				
				try {
					
					exchanger.exchange(A);
					Thread.sleep(2000);
					exchanger.exchange(A1);
				} catch (InterruptedException e) 
				{
//					e.printStackTrace();
					System.out.println("A receive Interrupt");
				}
			}
		});
		
		
		threadPool.execute(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				String B = "银行流水B"; // B录入银行流水数据
				try 
				{
					String A = exchanger.exchange(B);
					
					System.out.println("A和B数据是否一致：" + A.equals(B) 
										+ "，A录入的是：" + A 
										+ "，B录入是：" + B);
					//======================================================
					A = exchanger.exchange(B);
					System.out.println("A和B数据是否一致：" + A.equals(B) 
										+ "，A录入的是：" + A 
										+ "，B录入是：" + B);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					System.out.println("B receive Interrupt");
//					e.printStackTrace();
				}
			}
		});
		Thread.sleep(1000);
		threadPool.shutdownNow();
//		threadPool.shutdown();
	}
}
