package Thread.liugw.RecursiveTask;

import java.security.PublicKey;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.Future;
import java.util.concurrent.RecursiveTask;


public class CountTask extends RecursiveTask<Integer>
{

	private static final int THRESHOLD = 2; // 阀值
	private int start;
	private int end;
	
	public CountTask(int start, int end)
	{
		this.end = end;
		this.start = start;
	}

	@Override
	protected Integer compute() 
	{
		int sum = 0;
		
		// 如果任务足够小就计算任务
		boolean canCompute = (end - start) <= THRESHOLD;
		
		if (canCompute)
		{
			for(int i=start; i<=end; i++)
			{
				System.out.println("Thread_id:" + Thread.currentThread());
				sum += i;
			}
		}else
		{
			// 如果任务大于阀值，就分裂成两个子任务计算
			int middle = (start + end)/2;
			
			CountTask leftTask = new CountTask(start, middle);
			CountTask rightTask = new CountTask(middle + 1, end);
			
			// 执行子任务
			leftTask.fork();
			rightTask.fork();
			
			// 等待子任务执行完
			int leftResult = leftTask.join();
			int rightResult = rightTask.join();
			
			// 合并子任务
			sum = leftResult + rightResult;
		}
		return sum;
	}
	
	public static void main(String[] args)
	{
		ForkJoinPool forkJoinPool = new ForkJoinPool();
		
		// 生成计算任务
		CountTask countTask = new CountTask(1, 100);
		
		// 执行一个任务
		Future<Integer> result = forkJoinPool.submit(countTask);
		
		try {
			System.out.println("Result:" + result.get());
		} catch (InterruptedException | ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (countTask.isCompletedAbnormally())
		{
			System.out.println(countTask.getException());
		}
	}
}
