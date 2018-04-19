package com.cttic.liugw.design.decorator.condiment;

import com.cttic.liugw.design.decorator.coffee.Beverage;

/**
 * 调料抽象类 ---- 用来装饰饮料 coffee的装饰者
 * 
 * @author liugaowei
 *
 */
public abstract class CondimentDecorator extends Beverage{
    public abstract String getDescription();
}
