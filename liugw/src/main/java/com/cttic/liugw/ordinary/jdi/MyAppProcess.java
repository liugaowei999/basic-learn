package com.cttic.liugw.ordinary.jdi;

import java.util.Date;

/**
 * 启动时添加允许远程调式的启动参数：
 *               -agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=5005
 *               -agentlib:jdwp=transport=dt_socket,server=y,address=localhost:8000,timeout=5000
 * dt_socket：使用的通信方式
 * server：是主动连接调试器还是作为服务器等待调试器连接
 * suspend：是否在启动JVM时就暂停，并等待调试器连接
 * address：地址和端口，地址可以省略，两者用冒号分隔
 * 
 * jdwp: This option loads the JPDA reference implementation of JDWP. 
 *       This library resides in the target VM and uses JVMDI and JNI to 
 *       interact with it. It uses a transport and the JDWP protocol to 
 *       communicate with a separate debugger application.
 * 
 * @author liugaowei
 *
 */
public class MyAppProcess {
    public static void main(String[] args) throws Exception {
        test("hello", 100);
    }

    private static void test(String a, Integer b) throws Exception {
        Thread.sleep(20 * 1000);
        Boolean isOk = false;
        Date date = new Date();
        System.out.println("over");

        int addval = 0;
        for (int i = 0; i < 100; i++) {
            addval++;
            Thread.sleep(1000);
        }
    }
}
