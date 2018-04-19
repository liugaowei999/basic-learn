package com.cttic.liugw.design.decorator.condiment;

import com.cttic.liugw.design.decorator.coffee.Beverage;

/**
 * 摩卡 调料装饰者类 
 * @author liugaowei
 *
 */
public class Mocha extends CondimentDecorator {
    Beverage beverage;
    
    public Mocha(Beverage beverage){
        this.beverage = beverage;
    }

    @Override
    public String getDescription() {
        // TODO Auto-generated method stub
        return beverage.getDescription() + ", 摩卡调料";
    }

    @Override
    public double cost() {
        // TODO Auto-generated method stub
        return beverage.cost() + .5;
    }

}
