package com.cttic.liugw.design.observe.myself.entity;

import java.util.ArrayList;

import com.cttic.liugw.design.observe.myself.Observer;
import com.cttic.liugw.design.observe.myself.Subject;

/**
 * 主题实现类（气象站类 实现 主题接口）， 作为一个被观察者角色（主题角色）
 * @author liugaowei
 *
 */
public class WeatherData implements Subject{
    private ArrayList<Observer> observers; // 保存注册的所有观察者实例
    
    private float temperature; // 温度
    private float humidity ; // 湿度
    private float pressure; // 气压
    
    public WeatherData(){
        observers = new ArrayList<>();
    }

    @Override
    public void registerObserver(Observer observer) {
        observers.add(observer);
    }

    @Override
    public void removeObserver(Observer observer) {
        observers.remove(observer);
    }

    @Override
    public void notifyObservers() {
        for (Observer observer : observers) {
            observer.update(temperature, humidity, pressure);
        }
    }
    
    public void measurementsChanged(){
        notifyObservers();
    }
    
    // 模拟气象信息更新的操作
    public void setMeasurements(float temperature, float humidity, float pressure){
        this.temperature=temperature;
        this.humidity = humidity;
        this.pressure = pressure;
        measurementsChanged();
    }

}
