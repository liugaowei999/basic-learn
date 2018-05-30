package com.cttic.liugw.reference;

import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.HashSet;

/**
 * 通过弱引用类型的内部类， 代替finalize方法 如果使用finalize方法， HashMap data 是不能被释放的， 必须和原生内存一块释放。
 * 
 * 使用内部类的弱引用类方式， 当没有CleanupTest对象的强引用时， CleanupTest 对象可以被立即释放 HashMap data
 * 也就被释放了。 原生内存的释放可以通过内部弱引用类来释放。
 * 
 * @author liugaowei
 *
 */
public class CleanupTest {
    private CleanupFinalizer cleanupFinalizer;
    private HashMap data = new HashMap<>();

    public CleanupTest() {
        // 建议弱引用 ， 引用到 这个 CleanupTest的对象实例
        cleanupFinalizer = new CleanupFinalizer(this);
    }

    public void put(Object key, Object value) {
        data.put(key, value);
    }

    /**
     * 正常情况下， 开发者调用close 释放原生的本地资源；
     * 
     * 如果没有调用close也没关系。 CleanupFinalizer类一直在监控弱引用队列， 如果弱引用队列中， 取到了这个对象的弱引用，
     * 会自动释放这个对象。
     * 
     * 
     * 弱引用对象的存在不会阻止它所指向的对象被垃圾回收器回收。弱引用最常见的用途是实现规范映射(canonicalizing
     * mappings，比如哈希表）。
     * 
     * 假设垃圾收集器在某个时间点决定一个对象是弱可达的(weakly reachable)（也就是说当前指向它的全都是弱引用），
     * 这时垃圾收集器会清除所有指向该对象的弱引用，然后把这个弱可达对象标记为可终结(finalizable)的，这样它随后就会被回收。
     * 与此同时或稍后，垃圾收集器会把那些刚清除的弱引用放入创建弱引用对象时所指定的引用队列(Reference Queue)中。
     */
    public void close() {
        data = null;
        cleanupFinalizer.setClosed();
    }

    /**
     * 使用弱引用 替代 finalize() 方法清理 本地对象资源
     * 
     * @author liugaowei
     *
     */
    private static class CleanupFinalizer extends WeakReference {

        private static ReferenceQueue<CleanupFinalizer> finRefQueue;
        /**
         * 通过pendingRefs 保存对弱引用的强引用， 避免弱引用CleanupFinalizer对象本身在进入引用队列前，被垃圾收集掉。
         */
        private static HashSet<CleanupFinalizer> pendingRefs = new HashSet<>();

        private boolean closed = false;

        static {
            finRefQueue = new ReferenceQueue<>();

            Runnable runnable = new Runnable() {

                @Override
                public void run() {
                    // TODO Auto-generated method stub
                    CleanupFinalizer fr;

                    while (true) {
                        try {
                            // 弱引用 进入了引用队列，说明其指向的引用对象已经可以被回收了（没有其他强引用了）。
                            fr = (CleanupFinalizer) finRefQueue.remove();
                            fr.cleanUp();
                            pendingRefs.remove(fr);
                        } catch (InterruptedException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    }
                }
            };

            Thread thread = new Thread(runnable, "finRefQueue-handle-thread");
            thread.setDaemon(true);
            thread.start();
        }

        @SuppressWarnings("unchecked")
        public CleanupFinalizer(Object obj) {

            // 创建弱引用，其引用对象为obj，并在引用队列finRefQueue中注册
            super(obj, finRefQueue);
            // TODO Auto-generated constructor stub
            allocateNative();
            pendingRefs.add(this);
        }

        public void setClosed() {
            closed = true;
            doNativeCleanup();
        }

        public void cleanUp() {
            if (!closed) {
                doNativeCleanup();
            }
        }

        private native void allocateNative();

        private native void doNativeCleanup();

    }

    public static void main(String[] args) {
        CleanupTest cleanupTest = new CleanupTest();
        cleanupTest.put("key1", "hello");

        // 显示释放原生内存; 如果没有掉用close， 原生内存也会被内部弱引用类释放，
        // 而不用使用 finalize方法的方式释放原生本地资源
        // cleanupTest.close();

        // 释放强引用
        cleanupTest = null;

    }

}
