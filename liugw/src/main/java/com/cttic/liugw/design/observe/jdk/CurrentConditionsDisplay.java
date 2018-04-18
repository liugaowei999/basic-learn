package com.cttic.liugw.design.observe.jdk;

import java.util.Observable;
import java.util.Observer;

import com.cttic.liugw.design.observe.myself.DisplayElement;

public class CurrentConditionsDisplay implements Observer, DisplayElement{
    private float temperature; // 温度
    private float humidity ; // 湿度
    private Observable weatherData; // 记录 注册的主题对象引用
    
    public CurrentConditionsDisplay(Observable subject){
        this.weatherData = subject;
        weatherData.addObserver(this);
    }
    
    public void unRegester(){
        weatherData.deleteObserver(this);
    }

    @Override
    public void display() {
        // TODO Auto-generated method stub
        System.out.println("JDK 实时气象情报部---温度："+ temperature + "0C, 湿度：" + humidity + "%");
    }

    @Override
    public void update(Observable o, Object arg) {
        // 使用了 拉模式 （需要什么数据 拉取什么数据， 不会有多余的数据）
        if(o instanceof WeatherData){
            WeatherData weatherData  = (WeatherData)o;
            this.temperature = weatherData.getTemperature();
            this.humidity = weatherData.getHumidity();
            display();
        }
    }

    

}
