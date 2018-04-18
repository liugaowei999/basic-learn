package com.cttic.liugw.design.observe.myself;

/**
 * 可订阅的主题(也叫 可被观察者）
 * 
 * @author liugaowei
 *
 */
public interface Subject {
    // 观察者调用这个方法完成主题注册
    public void registerObserver(Observer observer);
    
    // 观察者调用这个方法，取消主题注册
    public void removeObserver(Observer observer );
    
    // 当主题改变时， 主题对象调用这个方法通知所有的观察者
    public void notifyObservers();
}
