package com.cttic.liugw.ordinary.javaAgent;

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.lang.instrument.Instrumentation;
import java.lang.instrument.UnmodifiableClassException;
import java.security.ProtectionDomain;

import com.cttic.liugw.ordinary.asmtest.Account;
import com.cttic.liugw.ordinary.asmtest.TimeStatWeaveGenerator;

public class PreMainTraceAgent {

    /**
     * 该方法会在main方法执行前调用
     * 在JVM加载类文件之前， 修改类的字节码
     * 
     * @param agentArgs
     * @param inst
     */
    public static void premain(String agentArgs, Instrumentation inst) {
        System.out.println("premain agentArgs=[" + agentArgs + "]");

        // 加入一个类转换器： 只是简单的打印出获取的类名， 不做实质性转换
        inst.addTransformer(new ClassFileTransformer() {

            /**
             * transform 方法用于完成类的转换。
             * 此处只是简单的打印出获取的类名， 不做实质性转换
             * @Params
             *  loader: 定义类的类加载器
             *  className ： 类的全限定名， 例如："java/lang/String"
             *  classBeingRedefined : 表示如果是被重定义或者重转换触发， 则为重定义或重转换的类。如果是类加载器，则为null.
             *  protectionDomain : 表示要定义或重定义的类的保护域。
             *  classfileBuffer：表示类文件格式的二进制数据（只读，不能修改）
             * 
             * @Return
             *  返回重新定义的新的类的二进制字节码数据
             */
            @Override
            public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined,
                    ProtectionDomain protectionDomain, byte[] classfileBuffer) throws IllegalClassFormatException {
                // TODO Auto-generated method stub
                System.out.println("Load Class:" + className);
                if (className.equals("com/cttic/liugw/ordinary/asmtest/Account")) {
                    System.out.println("***************** Modify Class:" + className);
                    System.out.println("织入TimeStat类的耗时统计字节码");

                    byte[] newClassFileBuffer = TimeStatWeaveGenerator.reGenClazzClassData(classfileBuffer);
                    return newClassFileBuffer;
                } else {
                    return classfileBuffer;
                }
            }
        }, true);
    }

    /**
     * JVM加载类文件之后， 运行时动态修改类的字节码定义
     * 
     * agentmain 方法会在 Java agent被attach到JVM上时执行。
     * 此处：agentmain 委托premain方法进行类转换器的注册， 并在最后使用了retransformClasses()方法
     *      要求使用注册的类转换器重转换类Account
     * @throws UnmodifiableClassException 
     */
    public static void agentmain(String agentArgs, Instrumentation inst) throws UnmodifiableClassException {
        System.out.println("agentmain called!");
        System.out.println("agentmain agentArgs=" + agentArgs);
        premain(agentArgs, inst);
        inst.retransformClasses(Account.class); // 重转换类Account

    }
}
