package com.cttic.liugw.design.decorator;

import java.io.FileInputStream;

import com.cttic.liugw.design.decorator.coffee.Beverage;
import com.cttic.liugw.design.decorator.coffee.Espresso;
import com.cttic.liugw.design.decorator.coffee.HouseBlend;
import com.cttic.liugw.design.decorator.condiment.Milk;
import com.cttic.liugw.design.decorator.condiment.Mocha;
import com.cttic.liugw.design.decorator.condiment.Soy;

/**
 * 装饰者模式 ： 为现有的类提供新的功能， 不改变接口
 * 
 * 与适配器模式的区别： 
 ******* 适配器模式： 针对新系统接口，为客户提供期望的接口，使类之间能够无碍的交流； 会改变接口以适配客户的要求
 *       外观模式： 目的是简化现有系统接口的调用，使客户更方便的使用接口； 同时也可以做到客户与具体系统的解耦。
 * 
 * @author liugaowei
 *
 */
public class CoffeeSaleTest {
    public static void main(String[] args) {
        Beverage beverage = new Espresso();
        System.out.println(beverage.getDescription() + ", 价格：￥"+ beverage.cost());
        
        beverage = new Mocha(beverage);
        beverage = new Milk(beverage);
        beverage = new Soy(beverage);
        System.out.println(beverage.getDescription() + ", 价格：￥"+ beverage.cost());
        
        System.out.println("==================================================================");
        
        Beverage beverage1 = new HouseBlend();
        System.out.println(beverage1.getDescription() + ", 价格：￥"+ beverage1.cost());
        beverage1 = new Mocha(new Milk(new Soy(beverage)));
        System.out.println(beverage1.getDescription() + ", 价格：￥"+ beverage1.cost());

    }
}
