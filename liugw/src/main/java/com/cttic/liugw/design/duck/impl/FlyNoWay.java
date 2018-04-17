package com.cttic.liugw.design.duck.impl;

import com.cttic.liugw.design.duck.intf.FlyBehavior;

public class FlyNoWay implements FlyBehavior{

    @Override
    public void fly() {
        // TODO Auto-generated method stub
        System.out.println("I cant't fly!");
    }

}
