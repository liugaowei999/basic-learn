package com.cttic.liugw.ordinary.dynamic.callsite;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.invoke.MutableCallSite;

public class MutableCallSiteMain {

    public static void main(String[] args) throws Throwable {
        MethodHandles.Lookup lookup = MethodHandles.lookup();
        MethodType methodType = MethodType.methodType(double.class, double.class);
        MethodHandle mhcos = lookup.findStatic(Math.class, "cos", methodType);
        MethodHandle mhsin = lookup.findStatic(Math.class, "sin", methodType);

        MutableCallSiteSample callSiteSample = new MutableCallSiteSample();

        MutableCallSite callSite = callSiteSample.getCallSite();
        /**
         * dynamicInvoker 会感知setTarget的修改， 后续自动会变更方法的调用，不需要重新调用dynamicInvoker 来获取方法句柄。
         * getTarget() ， 必须先调用 setTarget才会变更方法的调用。 getTarget 后不管调用多少次setTarget， 
         *                方法句柄都不会改变，必须重新getTarget获取新的方法句柄。
         */
        MethodHandle dynamicInvoker1 = callSite.dynamicInvoker(); // 此时可变调用点没有完成初始化，尚未绑定任何方法句柄
        callSite.setTarget(mhsin); // 可变调用点第一次绑定一个sin的方法句柄， dynamicInvoker1 自动指向 sin方法（实际内存地址）
        MethodHandle dynamicInvoker = callSite.getTarget(); // 方法句柄 为 sin方法

        callSite.setTarget(mhsin); // dynamicInvoker1 自动指向 sin方法
        System.out.println("dynamicInvoker:" + dynamicInvoker1.invoke(Math.PI / 2)); // 值为 1.0
        System.out.println("getTarget:" + dynamicInvoker.invoke(Math.PI / 2)); // 值为 1.0

        System.out.println("修改为 String.valueOf(i) 方法句柄");
        // 可以修改
        callSiteSample.setCallSite(mhcos);
        callSite.setTarget(mhcos); // dynamicInvoker1 自动指向 cos 方法 ； dynamicInvoker 仍然为sin方法
        System.out.println("dynamicInvoker:" + dynamicInvoker1.invoke(Math.PI / 2)); // 值为 6.123233995736766E-17
        System.out.println("getTarget:" + dynamicInvoker.invoke(Math.PI / 2)); // 值仍然为 1.0
    }
}

class MutableCallSiteSample {
    static MutableCallSite mutableCallSite;

    static {
        MethodHandles.Lookup lookup = MethodHandles.lookup();
        MethodType methodType = MethodType.methodType(double.class, double.class);
        // 根据方法签名， 构造了一个可变话的调用点， 此时没有绑定任何的方法句柄
        mutableCallSite = new MutableCallSite(methodType);
    }

    /**
     * 可以再修改
     * 
     * @param mh
     */
    public void setCallSite(MethodHandle mh) {
        //        mutableCallSite = new MutableCallSite(mh);
        mutableCallSite.setTarget(mh);
    }

    public MutableCallSite getCallSite() {
        return mutableCallSite;
    }
}
