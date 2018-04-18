package com.cttic.liugw.design.observe.myself;

import com.cttic.liugw.design.observe.myself.entity.CurrentConditionsDisplay;
import com.cttic.liugw.design.observe.myself.entity.PressureDisplay;
import com.cttic.liugw.design.observe.myself.entity.WeatherData;

public class weatherStationTest {

    public static void main(String[] args) {
        // 创建气象主题
        WeatherData weatherData = new WeatherData();
        
        // 创建观察者对象， 并注册气象主题
        Observer currentConditonsDisplay = new CurrentConditionsDisplay(weatherData);
        PressureDisplay pressureDisplay = new PressureDisplay(weatherData);
        
        // 当有气象数据更新时：
        weatherData.setMeasurements(30, 25, 80);
        
        System.out.println("气压显示板 取消注册.......");
        pressureDisplay.unRegester();
        
     // 当有气象数据更新时：
        weatherData.setMeasurements(31, 26, 86);
    }
}
