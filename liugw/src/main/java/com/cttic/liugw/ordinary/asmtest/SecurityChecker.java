package com.cttic.liugw.ordinary.asmtest;

/**
 * 模拟帐户安全检查类
 * 
 * @author liugaowei
 *
 */
public class SecurityChecker {

    public static boolean checkSecurity() {
        System.out.println("开始检查释放满足安全要求 .....");
        if ((System.currentTimeMillis() & 0x1) == 0) {
            System.out.println("不满足安全要求！");
            return false;
        } else {
            System.out.println("满足安全要求！");
            return true;
        }
    }

    public static void main(String[] args) {
        System.out.println(SecurityChecker.class.getResource("").getFile());
        System.out.println(Thread.currentThread().getContextClassLoader().getResource("").getFile());
        checkSecurity();
    }
}
