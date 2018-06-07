package com.cttic.liugw.ordinary;

import java.lang.ref.ReferenceQueue;

/**
 * 软引用 ： 当堆内存不足时， JVM自动回收被软引用引用的对象
 *          如果注册了引用队列， 当对象被释放后， 会自动将软引用放入引用队列中。
 * 
 * @author liugaowei
 *
 */
public class SoftReference {

    public static class User {
        public int id;
        public String name;

        public User(int id, String name) {
            this.id = id;
            this.name = name;
        }

        @Override
        public String toString() {
            return "id=[" + this.id + "], name=[" + this.name + "]";
        }
    }

    public static class UserSoftReference extends java.lang.ref.SoftReference<User> {
        int uid;

        public UserSoftReference(User referent, ReferenceQueue<? super User> queue) {
            super(referent);

            uid = referent.id;
        }

    }

    static ReferenceQueue<User> softQueue = null;

    public static class CheckRefQueue extends Thread {
        @Override
        public void run() {
            while (true) {
                if (softQueue != null) {
                    UserSoftReference obj = null;
                    try {
                        obj = (UserSoftReference) softQueue.remove();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    if (obj != null) {
                        System.out.println("use id =" + obj.uid + " is delete");
                    }
                }
            }
        }
    }

    /**
     * -Xmx10M 设置最大堆空间参数来运行这个测试
     * @param args
     */
    public static void main(String[] args) {
        Thread thread = new CheckRefQueue();
        thread.setDaemon(true);
        thread.start();

        softQueue = new ReferenceQueue<User>();

        User user = new User(1, "jack");

        UserSoftReference userSoftReference = new UserSoftReference(user, softQueue);
        user = null; // 释放强引用

        System.out.println(userSoftReference.get());
        // 垃圾回收(内存足够， 被软引用引用的User实例对象不会被回收)
        System.gc();

        System.out.println("after GC:");
        System.out.println(userSoftReference.get());

        // 分配一个大数组， 占满堆空间
        byte[] bs = new byte[1024 * 940 * 7];
        System.gc();
        System.out.println(userSoftReference.get()); // 输出null， 说明空间被释放

        /**
         * 测试输出结果：
            id=[1], name=[jack]
            after GC:
            id=[1], name=[jack]
            null        
         */

    }
}
