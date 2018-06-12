package com.cttic.liugw.ordinary.dynamic;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;

public class SimplePrivateMethodHandle {
    public static void main(String[] args) throws Throwable {
        PrivateMethodHandle obj = new PrivateMethodHandle();
        // 无法调用私有方法， 编译会报错
        //obj.printLine();

        // 使用MethodHandle 的 findSpecial 可以找到私有方法句柄，并成功调用。
        obj.callToString(obj);
    }
}

class PrivateMethodHandle {

    private void printLine() {
        System.out.println("call private method!!!");
    }

    public void callToString(Object object) throws Throwable {
        MethodHandle methodHandle = MethodHandles.lookup().findSpecial(object.getClass(), "printLine",
                MethodType.methodType(void.class), object.getClass()).bindTo(object);
        methodHandle.invokeExact();
    }
}
