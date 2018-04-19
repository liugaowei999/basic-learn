package com.cttic.liugw.design.celve.impl;

import com.cttic.liugw.design.celve.intf.QuackBehavior;

public class MuteQuack implements QuackBehavior{

    @Override
    public void quack() {
        // TODO Auto-generated method stub
        System.out.println("<<Slience>>");
    }

}
