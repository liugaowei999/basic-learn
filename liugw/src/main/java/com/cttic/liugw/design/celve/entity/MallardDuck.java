package com.cttic.liugw.design.celve.entity;

import com.cttic.liugw.design.celve.base.Duck;
import com.cttic.liugw.design.celve.impl.FlyWithWings;
import com.cttic.liugw.design.celve.impl.Quack;

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
