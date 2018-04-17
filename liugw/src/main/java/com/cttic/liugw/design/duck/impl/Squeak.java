package com.cttic.liugw.design.duck.impl;

import com.cttic.liugw.design.duck.intf.QuackBehavior;

public class Squeak implements QuackBehavior{

    @Override
    public void quack() {
        // TODO Auto-generated method stub
        System.out.println("Squeak!");
    }

}
