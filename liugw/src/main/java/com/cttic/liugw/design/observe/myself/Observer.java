package com.cttic.liugw.design.observe.myself;

/**
 * 观察者 接口定义
 * @author liugaowei
 *
 */
public interface Observer {
    public void update(float temp, float humidity, float pressure);
}
