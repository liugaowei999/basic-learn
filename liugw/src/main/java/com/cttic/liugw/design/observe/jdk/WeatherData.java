package com.cttic.liugw.design.observe.jdk;

import java.util.Observable;

public class WeatherData extends Observable{
    private float temperature; // 温度
    private float humidity ; // 湿度
    private float pressure; // 气压
    
    public WeatherData(){
        
    }
    
    public void measurementsChanged(){
        setChanged();
        notifyObservers();
    }
    
    // 模拟气象信息更新的操作
    public void setMeasurements(float temperature, float humidity, float pressure){
        this.temperature=temperature;
        this.humidity = humidity;
        this.pressure = pressure;
        measurementsChanged();
    }

    public float getTemperature() {
        return temperature;
    }

    public void setTemperature(float temperature) {
        this.temperature = temperature;
    }

    public float getHumidity() {
        return humidity;
    }

    public void setHumidity(float humidity) {
        this.humidity = humidity;
    }

    public float getPressure() {
        return pressure;
    }

    public void setPressure(float pressure) {
        this.pressure = pressure;
    }

}
