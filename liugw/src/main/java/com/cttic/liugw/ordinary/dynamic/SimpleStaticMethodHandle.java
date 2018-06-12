package com.cttic.liugw.ordinary.dynamic;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;

/**
 * 使用方法句柄 调用 Math类的静态方法 double sin(double)
 * 因为static 方法没有this指针， 所以静态方法的句柄， 执行时不需要绑定对象。
 * 
 * @author liugaowei
 *
 */
public class SimpleStaticMethodHandle {

    public double callSin() throws Throwable {
        MethodHandle methodHandle = MethodHandles.lookup().findStatic(Math.class, "sin",
                MethodType.methodType(double.class, double.class));
        return (double) methodHandle.invoke(Math.PI / 2);
    }

    public static void main(String[] args) throws Throwable {
        SimpleStaticMethodHandle simpleStaticMethodHandle = new SimpleStaticMethodHandle();
        System.out.println(simpleStaticMethodHandle.callSin());

    }
}
