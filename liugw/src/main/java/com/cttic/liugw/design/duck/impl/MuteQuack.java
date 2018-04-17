package com.cttic.liugw.design.duck.impl;

import com.cttic.liugw.design.duck.intf.QuackBehavior;

public class MuteQuack implements QuackBehavior{

    @Override
    public void quack() {
        // TODO Auto-generated method stub
        System.out.println("<<Slience>>");
    }

}
