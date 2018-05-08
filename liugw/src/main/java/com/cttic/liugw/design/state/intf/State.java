package com.cttic.liugw.design.state.intf;

public interface State {

    public void insertQuarter(); // 投币

    public void ejectQuarter(); // 退回硬币

    public void turnCrank(); // 转弯曲柄

    public void dispense(); // 出货
}
