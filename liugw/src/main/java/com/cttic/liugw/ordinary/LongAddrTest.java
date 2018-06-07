package com.cttic.liugw.ordinary;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.LongAdder;

public class LongAddrTest {

    private static final int MaxThreads = 3; // 最大线程数
    private static final int TaskCount = 3; // 任务数
    private static final int TargetCount = 100000000; // 目标总数

    static CountDownLatch cdlSync = new CountDownLatch(TaskCount);
    static CountDownLatch cdlAtomic = new CountDownLatch(TaskCount);
    static CountDownLatch cdlLongAdder = new CountDownLatch(TaskCount);

    private LongAdder longAdder_count = new LongAdder(); // jdk8中提供的新的原子操作类
    private AtomicLong atomic_count = new AtomicLong(0); // 无锁的原子操作
    private long count = 0; // 普通的变量

    // 自己使用synchronized 同步实现的普通的变量的累加操作
    protected synchronized long inc() {
        return ++count;
    }

    protected synchronized long getCount() {
        return count;
    }

    public void clearCount() {
        count = 0;
    }

    public class SyncThread implements Runnable {
        protected String name;
        protected long starttime;
        LongAddrTest ouTest;

        public SyncThread(LongAddrTest out, long starttime) {
            this.starttime = starttime;
            this.ouTest = out;
        }

        @Override
        public void run() {
            long v = ouTest.getCount();
            while (v < TargetCount) {
                v = ouTest.inc();
            }
            long endTime = System.currentTimeMillis();
            System.out.println("SynThread 用时：" + (endTime - starttime) + "ms , v=" + v);
            cdlSync.countDown();
        }
    }

    public void testSync() throws InterruptedException {
        ExecutorService executorService = Executors.newFixedThreadPool(MaxThreads);
        long starttime = System.currentTimeMillis();
        SyncThread syncThread = new SyncThread(this, starttime);
        System.out.println("SyncThread start");
        for (int i = 0; i < TaskCount; i++) {
            executorService.submit(syncThread);
        }
        cdlSync.await();
        executorService.shutdown();
    }

    public class AtomicThread implements Runnable {
        protected String name;
        protected long starttime;

        public AtomicThread(long starttime) {
            this.starttime = starttime;
        }

        @Override
        public void run() {
            long v = atomic_count.get();
            while (v < TargetCount) {
                v = atomic_count.incrementAndGet();
            }
            long endTime = System.currentTimeMillis();
            System.out.println("AtomicThread 用时：" + (endTime - starttime) + "ms , v=" + v);
            cdlAtomic.countDown();
        }
    }

    public void testAtomic() throws InterruptedException {
        ExecutorService executorService = Executors.newFixedThreadPool(MaxThreads);
        AtomicThread atomicThread = new AtomicThread(System.currentTimeMillis());
        System.out.println("AtomicThread start");
        for (int i = 0; i < TaskCount; i++) {
            executorService.submit(atomicThread);
        }
        cdlAtomic.await();
        executorService.shutdown();
    }

    public class LongAdderThread implements Runnable {
        protected String name;
        protected long starttime;

        public LongAdderThread(long starttime) {
            this.starttime = starttime;
        }

        @Override
        public void run() {
            long v = longAdder_count.sum();
            while (v < TargetCount) {
                longAdder_count.increment();
                v = longAdder_count.sum();
            }
            long endTime = System.currentTimeMillis();
            System.out.println("LongAdderThread 用时：" + (endTime - starttime) + "ms , v=" + v);
            cdlLongAdder.countDown();
        }
    }

    public void testLongAdderThread() throws InterruptedException {
        ExecutorService executorService = Executors.newFixedThreadPool(MaxThreads);
        LongAdderThread longAdderThread = new LongAdderThread(System.currentTimeMillis());
        System.out.println("LongAdderThread start");
        for (int i = 0; i < TaskCount; i++) {
            executorService.submit(longAdderThread);
        }
        cdlLongAdder.await();
        executorService.shutdown();
    }

    public static void main(String[] args) throws InterruptedException {

        //        System.out.println("======================== 热身操作 ================");
        //        for (int i = 0; i < 100; i++) {
        //            LongAddrTest longAddrTest = new LongAddrTest();
        //            longAddrTest.testSync();
        //            longAddrTest.testAtomic();
        //        }
        System.out.println("====================== 结果 ======================");
        LongAddrTest longAddrTest = new LongAddrTest();

        longAddrTest.testSync();
        longAddrTest.testAtomic();
        longAddrTest.testLongAdderThread();
    }
}
