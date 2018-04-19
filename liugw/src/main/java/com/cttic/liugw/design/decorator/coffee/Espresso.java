package com.cttic.liugw.design.decorator.coffee;

/**
 * 意大利浓咖啡 , 被装饰类 之一
 * 与装饰者有相同的超类型Beverage
 * @author liugaowei
 *
 */
public class Espresso extends Beverage{

    public Espresso(){
        setDescription("意大利浓咖啡");
    }

    @Override
    public double cost() {
        // TODO Auto-generated method stub
        return 7.5;
    }

}
