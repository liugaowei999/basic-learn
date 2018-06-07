package com.cttic.liugw.ordinary;

import java.lang.ref.PhantomReference;
import java.lang.ref.ReferenceQueue;

/**
 * 虚引用： 必须与引用队列一起使用才有意义。 虚引用只能用于跟踪对象的GC释放情况。
 * 
 * @author liugaowei
 *
 */
public class PhantomReferenceTest {

    // 一个可以通过finalize() 复活的类
    public static class CanReliveObj {
        public static CanReliveObj obj;

        static ReferenceQueue<CanReliveObj> phantomQueue = null;

        public static class CheckRefQueue extends Thread {
            @Override
            public void run() {
                while (true) {
                    if (phantomQueue != null) {
                        PhantomReference<CanReliveObj> phantomReference = null;

                        try {
                            phantomReference = (PhantomReference<CanReliveObj>) phantomQueue.remove();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        if (phantomReference != null) {
                            System.out.println("CanReliveObj is released by GC!");
                        }
                    }
                }
            }
        }

        @Override
        public String toString() {
            return "这是一个CanReliveObj 对象实例";
        }

        @Override
        protected void finalize() throws Throwable {
            super.finalize();
            System.out.println("CanReliveObj 对象的finalize() 方法被调用！");

            // 复活当前对象实例， 重新赋予一个强引用
            obj = this;
        }
    }

    public static void main(String[] args) throws InterruptedException {
        CanReliveObj.CheckRefQueue checkRefQueue = new CanReliveObj.CheckRefQueue();
        checkRefQueue.setDaemon(true);
        checkRefQueue.start();

        CanReliveObj.phantomQueue = new ReferenceQueue<>();
        CanReliveObj.obj = new CanReliveObj();

        // 创建虚引用,并注册引用队列
        PhantomReference<CanReliveObj> phantomReference = new PhantomReference<PhantomReferenceTest.CanReliveObj>(
                CanReliveObj.obj, CanReliveObj.phantomQueue);

        // 释放强引用
        CanReliveObj.obj = null;

        // 执行第一次GC
        System.out.println("第1次GC");
        System.gc();

        // 等待GC执行finalize（）方法
        Thread.sleep(1000);

        if (CanReliveObj.obj == null) {
            System.out.println("CanReliveObj.obj is null");
        } else {
            System.out.println("CanReliveObj.obj is valid!" + CanReliveObj.obj);
        }

        System.out.println("第2次GC");
        // 释放强引用
        CanReliveObj.obj = null;
        System.gc();

        Thread.sleep(1000);
        if (CanReliveObj.obj == null) {
            System.out.println("CanReliveObj.obj is null");
        } else {
            System.out.println("CanReliveObj.obj is valid!" + CanReliveObj.obj);
        }

        /**
         * 测试输出结果：
            第1次GC
            CanReliveObj 对象的finalize() 方法被调用！
            CanReliveObj.obj is valid!这是一个CanReliveObj 对象实例
            第2次GC
            CanReliveObj is released by GC! -------->### 引用队列捕获到了对象被释放！
            CanReliveObj.obj is null
        
         */
    }
}
