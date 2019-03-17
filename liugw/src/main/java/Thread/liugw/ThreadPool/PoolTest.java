package Thread.liugw.ThreadPool;

import java.util.concurrent.TimeUnit;

public class PoolTest {
    private static final int COUNT_BITS = 32 - 3;
    private static final int RUNNING = -1 << COUNT_BITS;
    private static final int SHUTDOWN = 0 << COUNT_BITS;
    private static final int STOP = 1 << COUNT_BITS;
    private static final int TIDYING = 2 << COUNT_BITS;
    private static final int TERMINATED = 3 << COUNT_BITS;

    private static final int CAPACITY = (1 << COUNT_BITS) - 1;

    private static int runStateOf(int c) {
        return c & ~CAPACITY;
    }

    public static void main(String[] args) {
        System.out.println(RUNNING);
        System.out.println(SHUTDOWN);
        System.out.println(STOP);
        System.out.println(TIDYING);
        System.out.println(TERMINATED);
        System.out.println(runStateOf(RUNNING | 0));
        //		DefaultThreadPool<JobTest> jobThreadPool = new DefaultThreadPool<JobTest>();
        //		
        //		for( int i=0; i<1000 ; i++ )
        //		{
        //			JobTest jobThread = new JobTest( "JOB_THREAD_"+i );
        //			jobThreadPool.execute(jobThread);
        //		}
    }
}

class JobTest implements Runnable {
    private String jobName = "";

    public JobTest(String jobName) {
        setJobName(jobName);
    }

    public void setJobName(String jobName) {
        this.jobName = jobName;
    }

    public void run() {
        System.out.println(jobName + " Running.");
        try {
            TimeUnit.SECONDS.sleep(2);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(jobName + " Done.");
    }
}
