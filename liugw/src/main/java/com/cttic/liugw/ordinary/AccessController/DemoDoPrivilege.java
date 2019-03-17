package com.cttic.liugw.ordinary.AccessController;

/**
 *  打开系统安全权限检查开关后， 运行参数配置：
 *  配置JVM运行参数 -Djava.security.policy=.\\MyPolicy.txt
 * @author liugaowei
 *
 */
public class DemoDoPrivilege {

    public static void main(String[] args) {
        System.out.println("***************************************");
        System.out.println("I will show AccessControl functionality...");

        System.out.println("Preparation step : 打开系统安全权限检查开关.[对于本地代码，默认关闭]");
        // 打开系统安全权限检查开关
        System.setSecurityManager(new SecurityManager());

        System.out.println();

        System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
        System.out.println("创建文件： temp1.txt 通过 privileged action ...");
        // 用特权访问方式在工程 A 执行文件路径中创建 temp1.txt 文件
        FileUtil.doPrivilegedAction("temp1.txt");
        System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
        System.out.println();
        //
        //        System.out.println("/////////////////////////////////////////");
        //        System.out.println("创建文件： temp2.txt 通过 File类 ...");
        //        try {
        //            // 用普通文件操作方式在工程 A 执行文件路径中创建 temp2.txt 文件
        //            File fs = new File(
        //                    "e:\\tmp\\temp2.txt");
        //            fs.createNewFile();
        //        } catch (IOException e) {
        //            e.printStackTrace();
        //        } catch (AccessControlException e1) {
        //            e1.printStackTrace();
        //        }
        //        System.out.println("/////////////////////////////////////////");
        //        System.out.println();
        //
        //        System.out.println("-----------------------------------------");
        //        System.out.println("创建文件： temp3.txt 通过 FileUtil工具类 ...");
        //        // 直接调用普通接口方式在工程 A 执行文件路径中创建 temp3.txt 文件
        //        FileUtil.makeFile("temp3.txt");
        //        System.out.println("-----------------------------------------");
        //        System.out.println();

        System.out.println("***************************************");
    }

}
