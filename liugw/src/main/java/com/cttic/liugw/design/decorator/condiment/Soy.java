package com.cttic.liugw.design.decorator.condiment;

import com.cttic.liugw.design.decorator.coffee.Beverage;

/**
 * 大豆 调料装饰者类 
 * @author liugaowei
 *
 */
public class Soy extends CondimentDecorator {
    Beverage beverage;
    
    public Soy(Beverage beverage){
        this.beverage = beverage;
    }

    @Override
    public String getDescription() {
        // TODO Auto-generated method stub
        return beverage.getDescription() + ", 大豆调料";
    }

    @Override
    public double cost() {
        // TODO Auto-generated method stub
        return beverage.cost() + .5;
    }

}
