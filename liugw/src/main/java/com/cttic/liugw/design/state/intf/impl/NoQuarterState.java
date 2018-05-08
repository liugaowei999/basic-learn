package com.cttic.liugw.design.state.intf.impl;

import com.cttic.liugw.design.state.entity.GumballMachine;
import com.cttic.liugw.design.state.intf.State;

/**
 * 没有投币的状态类 封装
 * 
 * @author liugaowei
 *
 */
public class NoQuarterState implements State {
    GumballMachine machine;

    public NoQuarterState(GumballMachine machine) {
        this.machine = machine;
    }

    @Override
    public void insertQuarter() {
        // TODO Auto-generated method stub
        System.out.println("你投入了硬币.......");
        machine.setState(machine.getHasQuarterState());
    }

    @Override
    public void ejectQuarter() {
        // TODO Auto-generated method stub
        System.out.println("没有投入硬币！");
    }

    @Override
    public void turnCrank() {
        // TODO Auto-generated method stub
        System.out.println("请先投入硬币，再按摇柄！");
    }

    @Override
    public void dispense() {
        // TODO Auto-generated method stub
        System.out.println("无法出货， 请应该先投入硬币！");
    }

}
