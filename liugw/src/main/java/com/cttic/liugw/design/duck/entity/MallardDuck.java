package com.cttic.liugw.design.duck.entity;

import com.cttic.liugw.design.duck.base.Duck;
import com.cttic.liugw.design.duck.impl.FlyWithWings;
import com.cttic.liugw.design.duck.impl.Quack;

public class MallardDuck extends Duck{
    
    public MallardDuck(){
        flyBehavior = new FlyWithWings();
        quackBehavior = new Quack();
    }

    @Override
    public void display() {
        // TODO Auto-generated method stub
        System.out.println("绿头鸭");
    }

}
