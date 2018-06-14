package com.cttic.liugw.ordinary.dynamic;

import java.lang.invoke.CallSite;
import java.lang.invoke.ConstantCallSite;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles.Lookup;
import java.lang.invoke.MethodType;

/**
 * 为了使用 invokedynamic指令， 先创建一个引导方法
 * 
 * @author liugaowei
 *
 */
public class DynBootStrap {

    /**
     * 
     * @param lookup
     * @param name : 调用方法的名称
     * @param type
     * @param value ： 方法的第一个参数， 实例方法的第一个参数就是this
     * @return
     * @throws NoSuchMethodException
     * @throws IllegalAccessException
     */
    public static CallSite bootstrap(Lookup lookup, String name, MethodType type, Object value)
            throws NoSuchMethodException, IllegalAccessException {
        System.out.println("bootstrap called, name=" + name);

        // 完成方法句柄的查找工作
        MethodHandle methodHandle = lookup.findVirtual(value.getClass(), name, type).bindTo(value);

        // 返回一个常量调用点，系统会根据这个调用点进行方法调用
        return new ConstantCallSite(methodHandle);
    }

    public static void main(String[] args) {
        System.out.println(DynBootStrap.class.getName());
        System.out.println("replace=" + DynBootStrap.class.getName().replace(".", "/"));
        System.out.println("replaceAll=" + DynBootStrap.class.getName().replaceAll("\\.", "/"));
    }
}
