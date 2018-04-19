package com.cttic.liugw.design.decorator.condiment;

import com.cttic.liugw.design.decorator.coffee.Beverage;

/**
 * 牛奶调料 装饰者类
 * 
 * @author liugaowei
 *
 */
public class Milk extends CondimentDecorator{
    Beverage beverage;
    
    public Milk(Beverage beverage){
        this.beverage = beverage;
    }

    @Override
    public String getDescription() {
        // TODO Auto-generated method stub
        return beverage.getDescription() + ", 牛奶调料";
    }

    @Override
    public double cost() {
        // TODO Auto-generated method stub
        return beverage.cost() + 1.0;
    }

}
