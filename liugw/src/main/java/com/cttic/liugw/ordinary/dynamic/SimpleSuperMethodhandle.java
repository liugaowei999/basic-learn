package com.cttic.liugw.ordinary.dynamic;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;

public class SimpleSuperMethodhandle {
    @Override
    public String toString() {
        return "SimpleSuperMethodhandle Object toString.";
    }

    public String callToString() throws Throwable {
        System.out.println(this.toString());
        //        MethodHandle methodHandle = MethodHandles.lookup().findSpecial(object.getClass().getSuperclass(), "toString",
        //                MethodType.methodType(String.class), object.getClass()).bindTo(object);
        MethodHandle methodHandle = MethodHandles.lookup().findSpecial(Object.class, "toString",
                MethodType.methodType(String.class), this.getClass()).bindTo(this);
        return (String) methodHandle.invokeExact();
    }

    public String callToString(Object obj) throws Throwable {
        System.out.println(obj.toString());
        //        MethodHandle methodHandle = MethodHandles.lookup().findSpecial(object.getClass().getSuperclass(), "toString",
        //                MethodType.methodType(String.class), object.getClass()).bindTo(object);
        MethodHandle methodHandle = MethodHandles.lookup().findSpecial(obj.getClass().getSuperclass(), "toString",
                MethodType.methodType(String.class), obj.getClass()).bindTo(obj);
        return (String) methodHandle.invokeExact();
    }

    public static void main(String[] args) throws Throwable {
        SimpleSuperMethodhandle simpleSuperMethodhandle = new SimpleSuperMethodhandle();

        Man man = new Man();
        System.out.println(man.toString());

        System.out.println(simpleSuperMethodhandle.callToString());
        System.out.println(simpleSuperMethodhandle.callToString(simpleSuperMethodhandle));

        System.out.println(simpleSuperMethodhandle.callToString(man));
    }
}

class Human {

    @Override
    public String toString() {
        return "Human Object toString.";
    }

}

class Man extends Human {
    @Override
    public String toString() {
        return "man Object toString.";
    }
}