package com.cttic.liugw.design.celve.test;

import com.cttic.liugw.design.celve.base.Duck;
import com.cttic.liugw.design.celve.entity.MallardDuck;
import com.cttic.liugw.design.celve.impl.FlyNoWay;
import com.cttic.liugw.design.celve.impl.FlyWithWings;

public class DuckDriver {
    public static void main(String[] args) {
        Duck duck = new MallardDuck();
        duck.display();
        duck.performFly();
        duck.performQuack();
        
        duck.setFlyBehavior(new FlyNoWay());
        duck.display();
        duck.performFly();
        duck.performQuack();
    }
}
