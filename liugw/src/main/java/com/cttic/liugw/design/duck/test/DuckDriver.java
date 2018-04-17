package com.cttic.liugw.design.duck.test;

import com.cttic.liugw.design.duck.base.Duck;
import com.cttic.liugw.design.duck.entity.MallardDuck;

public class DuckDriver {
    public static void main(String[] args) {
        Duck duck = new MallardDuck();
        duck.display();
        duck.performFly();
        duck.performQuack();
    }
}
