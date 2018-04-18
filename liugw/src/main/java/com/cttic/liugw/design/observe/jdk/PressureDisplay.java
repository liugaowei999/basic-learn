package com.cttic.liugw.design.observe.jdk;

import java.util.Observable;
import java.util.Observer;

import com.cttic.liugw.design.observe.myself.DisplayElement;


public class PressureDisplay implements Observer, DisplayElement{
    private float pressure;
    private Observable wetherData;
    
    public PressureDisplay(Observable subject){
        wetherData = subject;
        wetherData.addObserver(this);
    }
    
    public void unRegester(){
        wetherData.deleteObserver(this);
    }

    @Override
    public void display() {
        // TODO Auto-generated method stub
        System.out.println("JDK 气压显示板 --- 气压：" + pressure);
    }

    @Override
    public void update(Observable o, Object arg) {
        // TODO Auto-generated method stub
        if(o instanceof WeatherData){
            WeatherData weatherData = (WeatherData)o;
            this.pressure = weatherData.getPressure();
            display();
        }
    }

    

}
