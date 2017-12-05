package Thread.liugw.ThreadPool;

import java.util.concurrent.TimeUnit;

public class PoolTest 
{	
	
	public static void main( String[] args )
	{
		DefaultThreadPool<JobTest> jobThreadPool = new DefaultThreadPool<JobTest>();
		
		for( int i=0; i<1000 ; i++ )
		{
			JobTest jobThread = new JobTest( "JOB_THREAD_"+i );
			jobThreadPool.execute(jobThread);
		}
	}
}

class JobTest implements Runnable
{
	private String jobName = "";
	
	public JobTest( String jobName )
	{
		setJobName(jobName);
	}
	
	public void setJobName( String jobName )
	{
		this.jobName = jobName;
	}
	
	public void run()
	{
		System.out.println( jobName + " Running.");
		try 
		{
			TimeUnit.SECONDS.sleep(2);
		} catch (InterruptedException e) 
		{
			e.printStackTrace();
		}
		System.out.println( jobName + " Done.");
	}
}
