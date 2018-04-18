package com.cttic.liugw.design.observe.myself.entity;

import com.cttic.liugw.design.observe.myself.DisplayElement;
import com.cttic.liugw.design.observe.myself.Observer;
import com.cttic.liugw.design.observe.myself.Subject;

public class CurrentConditionsDisplay implements Observer, DisplayElement{
    private float temperature; // 温度
    private float humidity ; // 湿度
    private Subject weatherData; // 记录 注册的主题对象引用
    
    public CurrentConditionsDisplay(Subject subject){
        this.weatherData = subject;
        weatherData.registerObserver(this);
    }
    
    public void unRegester(){
        weatherData.removeObserver(this);
    }

    @Override
    public void display() {
        // TODO Auto-generated method stub
        System.out.println("实时气象情报部---温度："+ temperature + "0C, 湿度：" + humidity + "%");
    }

    @Override
    public void update(float temp, float humidity, float pressure) {
        // TODO Auto-generated method stub
        this.temperature = temp;
        this.humidity =humidity;
        display();
    }

}
