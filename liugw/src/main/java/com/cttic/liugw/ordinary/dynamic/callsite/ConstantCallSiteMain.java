package com.cttic.liugw.ordinary.dynamic.callsite;

import java.lang.invoke.ConstantCallSite;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;

public class ConstantCallSiteMain {

    public static MethodHandle getMethodHandle() throws NoSuchMethodException, IllegalAccessException {
        MethodHandles.Lookup lookup = MethodHandles.lookup();
        MethodType methodType = MethodType.methodType(String.class, int.class, int.class);

        MethodHandle mh = lookup.findVirtual(String.class, "substring", methodType);
        return mh;
    }

    public static void main(String[] args) throws Throwable {
        ConstantCallSiteSample callSiteSample = new ConstantCallSiteSample();
        //callSiteSample.setCallSite(getMethodHandle());

        ConstantCallSite callSite = callSiteSample.getCallSite();
        MethodHandle dynamicInvoker1 = callSite.dynamicInvoker();
        MethodHandle dynamicInvoker = callSite.getTarget();
        System.out.println("dynamicInvoker:" + dynamicInvoker1.invoke("hello world", 0, 5));
        System.out.println("getTarget:" + dynamicInvoker.invoke("hello world", 0, 5));
    }
}

class ConstantCallSiteSample {
    static ConstantCallSite constantCallSite;

    static {
        MethodHandles.Lookup lookup = MethodHandles.lookup();
        MethodType methodType = MethodType.methodType(String.class, int.class, int.class);
        try {
            MethodHandle mh = lookup.findVirtual(String.class, "substring", methodType);
            constantCallSite = new ConstantCallSite(mh);
        } catch (NoSuchMethodException | IllegalAccessException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /**
     * 不可以再修改， 调用setTarget方法会报错
     * 
     * @param mh
     */
    public void setCallSite(MethodHandle mh) {
        //        constantCallSite = new ConstantCallSite(mh);
        constantCallSite.setTarget(mh);
    }

    public ConstantCallSite getCallSite() {
        return constantCallSite;
    }
}
