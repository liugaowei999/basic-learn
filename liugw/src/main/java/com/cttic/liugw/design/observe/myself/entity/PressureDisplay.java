package com.cttic.liugw.design.observe.myself.entity;

import com.cttic.liugw.design.observe.myself.DisplayElement;
import com.cttic.liugw.design.observe.myself.Observer;
import com.cttic.liugw.design.observe.myself.Subject;

public class PressureDisplay implements Observer, DisplayElement{
    private float pressure;
    private Subject wetherData;
    
    public PressureDisplay(Subject subject){
        wetherData = subject;
        wetherData.registerObserver(this);
    }
    
    public void unRegester(){
        wetherData.removeObserver(this);
    }

    @Override
    public void display() {
        // TODO Auto-generated method stub
        System.out.println("气压显示板 --- 气压：" + pressure);
    }

    @Override
    public void update(float temp, float humidity, float pressure) {
        // TODO Auto-generated method stub
        this.pressure = pressure;
        display();
    }

}
