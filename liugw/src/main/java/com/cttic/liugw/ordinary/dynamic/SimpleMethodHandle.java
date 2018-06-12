package com.cttic.liugw.ordinary.dynamic;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodHandles.Lookup;
import java.lang.invoke.MethodType;

/**
 * 基础知识描述：
 * JDK1.7引入了一个新的invoke指令-----> invokedynamic； 该指令是为了更好的支持java虚拟机平台上的动态语言， 以及java8中的lambda表达式。
 *  invokedynamic的加入， 引入一下几个概念：
 *  （1）方法句柄（Method Handler）： 方法句柄就像一个方法指针，或者代理。 通过方法句柄，就可以调用一个方法。 
 *      Class文件的常量池中，有一项常量类型为CONSTANT_MethodHandle ，这就是方法句柄。
 *  （2）调用点（CallSite）：调用点是对方法句柄的封装，通过调用点可以获取一个方法句柄对方法进行调用。
 *       使用调用点可以增强对方法句柄的表达能力。例如：对于可变调用点来说，它绑定的方法句柄是可变的， 
 *       因此对同一个调用点而言，其调用方法是可变的。
 *  （3）启动方法（BootstrapMethods）：通过启动方法可以获取一个调用点， 获取调用点的目的是为了进行
 *       方法绑定和调用。启动方法在Class文件的属性中进行描述。
 *  （4）方法类型（Method Type）：用于描述方法的签名， 比如方法的参数类型、返回值等。 
 *      根据方法的类型，可以查找到可用的方法句柄。
 */

/**
 * 一个使用MethodHandle 进行函数调用的简单例子。
 * 
 * @author liugaowei
 *
 */
public class SimpleMethodHandle {
    static class MyPrintln {
        protected void println(String s) {
            System.out.println(s);
        }
    }

    public static void main(String[] args) throws Throwable {
        Object object = (System.currentTimeMillis() & 1L) == 0L ? System.out : new MyPrintln();
        System.out.println(object.getClass().getName());

        /**
         * invoke 方法允许更加松散的调用方式; 它会尝试在调用的时候进行返回值和参数类型的转换工作。
         * 当方法句柄在调用时的类型与其声明的类型完全一致的时候，调用invoke方法等于调用invokeExact方法；
         * 否则，invoke方法会先调用asType方法来尝试适配到调用时的类型。
         * 如果适配成功，则可以继续调用。否则会抛出相关的异常。
         * 这种灵活的适配机制，使invoke方法成为在绝大多数情况下都应该使用的方法句柄调用方式。
         *  进行类型匹配的基本规则是对比返回值类型和每个参数的类型是否都可以相互匹配。假设源类型为S，目标类型为T，则基本规则如下：
            1、可以通过java的类型转换来完成，一般从子类转成父类，比如从String到Object类型；
            2、可以通过基本类型的转换来完成，只能将类型范围的扩大，比如从int切换到long；
            3、可以通过基本类型的自动装箱和拆箱机制来完成，例如从int到Integer；
            4、如果S有返回值类型，而T的返回值类型为void，则S的返回值会被丢弃。
            5、如果S的返回值是void，而T的返回值是引用类型，T的返回值会是null；
            6、如果S的返回值是void，而T的返回值是基本类型，T的返回值会是0；
         * 
         * 下面的调用 invoke将void返回值 往String类型进行了自动转换。 如果使用invokeExact，就会报错。
         */
        String aa = (String) getPrintlnMethodHandler(object).invoke("hello world");
        /**
         * invokeExact方法在调用时要求严格的类型匹配，方法的返回值类型也在考虑范围之内。 不会自动做类型转换，例如int 到 Integer等。
         * 
         */
        //String bb = (String) getPrintlnMethodHandler(object).invokeExact("hello world");
        System.out.println(aa);

        /**
         * 如果MethodHandle 没有做bindTo操作绑定 方法的执行者， 则需要在调用invoke时传入。
         * 下面的实例中第一个参数：object 便是方法的执行者， 后面的参数 是 执行方法本身需要的参数：
         * 相当于： object.println("not bind hello world");
         */
        getPrintlnMethodHandlerNotBind(object).invoke(object, "not bind hello world");
    }

    /**
     * 返回一个bind到一个执行方法的对象的MethodHandle
     * 
     * @param object
     * @return
     * @throws NoSuchMethodException
     * @throws IllegalAccessException
     */
    private static MethodHandle getPrintlnMethodHandler(Object object)
            throws NoSuchMethodException, IllegalAccessException {
        // 构造一个MethodType （返回值为void， 参数为String类型）
        MethodType mt = MethodType.methodType(void.class, String.class);

        Lookup lookup = MethodHandles.lookup();
        // MethodHandle java.lang.invoke.MethodHandles.Lookup.findVirtual(Class<?> refc, String name, MethodType type) throws NoSuchMethodException, IllegalAccessException
        MethodHandle methodHandle = lookup.findVirtual(object.getClass(), "println", mt).bindTo(object);
        System.out.println("bind: methodHandle=" + methodHandle);

        return methodHandle;
    }

    /**
     * 返回一个没有bind到一个执行方法的对象的MethodHandle
     * 
     * @param object
     * @return
     * @throws NoSuchMethodException
     * @throws IllegalAccessException
     */
    private static MethodHandle getPrintlnMethodHandlerNotBind(Object object)
            throws NoSuchMethodException, IllegalAccessException {
        // 构造一个MethodType （返回值为void， 参数为String类型）
        MethodType mt = MethodType.methodType(void.class, String.class);

        Lookup lookup = MethodHandles.lookup();
        // MethodHandle java.lang.invoke.MethodHandles.Lookup.findVirtual(Class<?> refc, String name, MethodType type) throws NoSuchMethodException, IllegalAccessException
        MethodHandle methodHandle = lookup.findVirtual(object.getClass(), "println", mt);
        System.out.println("not bind: methodHandle=" + methodHandle);
        return methodHandle;
    }
}
