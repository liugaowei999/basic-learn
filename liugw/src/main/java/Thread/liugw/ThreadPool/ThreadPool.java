package Thread.liugw.ThreadPool;


public interface ThreadPool<Job extends Runnable>
{

	// 执行一个JOB， 这个JOB需要事先Runnable
	void execute( Job job );
	
	// 关闭线程池
	void shutDown();
	
	// 增加工作者线程
	void addWorkders( int num );
	
	// 减少工作者线程
	void removeWorkers( int num );
	
	// 得到正在等待执行的任务数量
	int getJobSize();
}
