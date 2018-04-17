package com.cttic.liugw.design.duck.impl;

import com.cttic.liugw.design.duck.intf.FlyBehavior;

public class FlyWithWings implements FlyBehavior{

    @Override
    public void fly() {
        // TODO Auto-generated method stub
        System.out.println("I'am flying!");
    }

}
