package com.cttic.liugw.ordinary;

import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;

/**
 * 弱引用对象： 被弱引用引用的对象， 发现即被GC释放。 
 *             因此 弱引用一般用于，确定有其他线程持有强引用时， 才可以复用。 当强引用释放后， 弱引用等到下次gc很快就会不可用。
 *             当被弱引用引用的对象释放后， 弱引用变量也会自动放入 其注册的 引用队列中，可以跟踪被引用对象的回收情况。
 * @author liugaowei
 *
 */
public class WeakRefrence {

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

    public static void main(String[] args) throws IllegalArgumentException, InterruptedException {
        User user = new User(1, "jack");
        ReferenceQueue<User> referenceQueue = new ReferenceQueue<>();
        WeakReference<User> weakReference = new WeakReference<WeakRefrence.User>(user, referenceQueue);

        // 释放强引用
        user = null;
        System.out.println("获取弱引用对象：" + weakReference.get());
        System.out.println("queue element:" + referenceQueue.remove(1000));

        // 执行GC
        System.gc();

        System.out.println("GC后， 再次获取弱引用对象：" + weakReference.get());
        WeakReference<? extends User> user2 = (WeakReference<? extends User>) referenceQueue.remove(1000);
        System.out.println("GC后， queue element:" + user2);

        /**
         * 测试输出：
            获取弱引用对象：id=[1], name=[jack]
            queue element:null
            GC后， 再次获取弱引用对象：null
            GC后， queue element:java.lang.ref.WeakReference@15db9742
         */

    }
}
