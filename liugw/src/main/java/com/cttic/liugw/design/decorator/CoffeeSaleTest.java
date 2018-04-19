package com.cttic.liugw.design.decorator;

import java.io.FileInputStream;

import com.cttic.liugw.design.decorator.coffee.Beverage;
import com.cttic.liugw.design.decorator.coffee.Espresso;
import com.cttic.liugw.design.decorator.coffee.HouseBlend;
import com.cttic.liugw.design.decorator.condiment.Milk;
import com.cttic.liugw.design.decorator.condiment.Mocha;
import com.cttic.liugw.design.decorator.condiment.Soy;

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
