package com.cttic.liugw.design.decorator.coffee;

/**
 * 混合咖啡 被装饰类 之一
 * 与装饰者有相同的超类型Beverage
 * @author liugaowei
 *
 */
public class HouseBlend extends Beverage {
    public HouseBlend(){
        setDescription("混合咖啡");
    }

    @Override
    public double cost() {
        // TODO Auto-generated method stub
        return 9.5;
    }
}
