package com.cttic.liugw.design.duck.base;

import com.cttic.liugw.design.duck.intf.FlyBehavior;
import com.cttic.liugw.design.duck.intf.QuackBehavior;

public abstract class Duck {
    protected FlyBehavior flyBehavior;
    protected QuackBehavior quackBehavior;
    
    public Duck(){
    }
    
    public void performFly(){
        flyBehavior.fly();
    }
    
    public void performQuack(){
        quackBehavior.quack();
    }
    
    public abstract void display();
    
    public void swim(){
        System.out.println("All ducks float, even decoys!");
    }
}
