package com.cttic.liugw.ordinary.dynamic.callsite;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.invoke.MutableCallSite;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * 本示例目的：对方法调用的两种方式进行比较。
 *      1. 使用java语言层面的反射 进行方法调用耗时统计
 *      2. 使用字节码层面的方法句柄进行方法调用耗时统计
 *   比较两种方式那种效率更高。
 *   
 * 基础知识：
 *    反射是在java语言层面进行方法调用的模拟； 方法句柄是在Java字节码层面进行的方法调用，执行效率要高于反射。
 *    （1）方法句柄（Method Handler）： 方法句柄就像一个方法指针，或者代理。 通过方法句柄，就可以调用一个方法。 
 *                                     Class文件的常量池中，有一项常量类型为CONSTANT_MethodHandle ，这就是方法句柄。
 * 
 * 实际测试结果： 使用JDK8.0 64位
 *      分别使用一下三种模式： 解释执行， 全编译机器码（不使用JIT）执行， 使用JIT执行：
 *      所有三种方式， 都是 反射调用效率更高， 是方法句柄高一倍。 与以前的结论并不相符。 说明JDK8 对反射进行了很好的优化。
 *      使用JDK7 ，反射效率 比 方法调用高更多。
 *
 * @author liugaowei
 *
 */
public class RelectionMain {
    public static final int COUNT = 1000000;
    int i = 0;

    public void method() {
        i++;
    }

    public static void callByMethodHandler() throws Throwable {
        RelectionMain relectionMain = new RelectionMain();
        // 复习方法调用点
        MethodType methodType = MethodType.methodType(void.class);
        MutableCallSite callSite = new MutableCallSite(methodType);
        MethodHandle mh = MethodHandles.lookup().findVirtual(RelectionMain.class, "method", methodType)
                .bindTo(relectionMain);
        callSite.setTarget(mh);

        // 通过调用点获取方法句柄
        MethodHandle methodHandle = callSite.dynamicInvoker(); // 此时尚未绑定方法调用者
        //methodHandle.bindTo(relectionMain); // 绑定对象， 表示执行的是relectionMain这个对象的这个方法。

        long b = System.currentTimeMillis();
        for (int i = 0; i <= COUNT; i++) {
            methodHandle.invoke();
        }
        System.out.println("方法句柄方式耗时：" + (System.currentTimeMillis() - b) + "ms.");
    }

    public static void callByReflection() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        RelectionMain relectionMain = new RelectionMain();
        Method method = RelectionMain.class.getMethod("method");

        long b = System.currentTimeMillis();
        for (int i = 0; i <= COUNT; i++) {
            method.invoke(relectionMain);
        }
        System.out.println("反射Method方式耗时：" + (System.currentTimeMillis() - b) + "ms.");
    }

    public static void main(String[] args) throws Throwable {
        for (int i = 0; i < 5; i++) {
            callByReflection();
            callByReflection();
            callByMethodHandler();
            callByMethodHandler();
        }
        /**
         * 执行结果：
         *  1. 使用解释执行 ： 参数： -Xint
         *  反射Method方式耗时：471ms.
            反射Method方式耗时：390ms.
            方法句柄方式耗时：698ms.
            方法句柄方式耗时：636ms.
            
            2. 使用 -Xcomp -XX:-BackgroundCompilation （全部被编译为机器码执行， 无法使用到JIT）
            反射Method方式耗时：465ms.
            反射Method方式耗时：60ms.
            方法句柄方式耗时：260ms.
            方法句柄方式耗时：46ms.
            反射Method方式耗时：20ms.
            反射Method方式耗时：20ms.
            方法句柄方式耗时：38ms.
            方法句柄方式耗时：31ms.
            反射Method方式耗时：11ms.
            反射Method方式耗时：12ms.
            方法句柄方式耗时：38ms.
            方法句柄方式耗时：33ms.
            
            3. 默认混合模式， 使用JIT编译器
            反射Method方式耗时：20ms.
            反射Method方式耗时：11ms.
            方法句柄方式耗时：24ms.
            方法句柄方式耗时：11ms.
            反射Method方式耗时：4ms.
            反射Method方式耗时：4ms.
            方法句柄方式耗时：9ms.
            方法句柄方式耗时：11ms.
            反射Method方式耗时：3ms.
            反射Method方式耗时：4ms.
            方法句柄方式耗时：8ms.
            方法句柄方式耗时：8ms.
            
         */
    }
}
