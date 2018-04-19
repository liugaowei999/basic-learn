package com.cttic.liugw.design.decorator.coffee;

/**
 * 饮料抽象类， 也可以是一个接口
 * @author liugaowei
 *
 */
public abstract class Beverage {

    String description = "未知饮料";
    
    
    public abstract double cost();

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
    
    
}
