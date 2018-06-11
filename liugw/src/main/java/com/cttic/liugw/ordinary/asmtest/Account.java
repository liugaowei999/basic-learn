package com.cttic.liugw.ordinary.asmtest;

/**
 * 模拟相关帐户操作的类
 * 
 * @author liugaowei
 *
 */
public class Account {

    public void operator() {
        System.out.println("执行帐户操作.....");
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        System.out.println("执行帐户操作完成!");
    }

    public static void main(String[] args) {
        Account account = new Account();
        account.operator();
    }
}
