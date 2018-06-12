package com.cttic.liugw.ordinary.javaAgent;

import com.cttic.liugw.ordinary.asmtest.Account;

public class RunLoopAccountMain {

    public static void main(String[] args) throws InterruptedException {
        Account account = new Account();
        while (true) {
            account.operator();

            Thread.sleep(1000);
        }
    }
}
