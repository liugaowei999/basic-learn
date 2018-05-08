package com.cttic.liugw.design.state.intf.impl;

import com.cttic.liugw.design.state.entity.GumballMachine;
import com.cttic.liugw.design.state.intf.State;

/**
 * 没有投币的状态类 封装
 * 
 * @author liugaowei
 *
 */
public class SoldState implements State {
    GumballMachine machine;

    public SoldState(GumballMachine machine) {
        this.machine = machine;
    }

    @Override
    public void insertQuarter() {
        // TODO Auto-generated method stub
        System.out.println("已经投入了硬币，请等待为您分配货物.");
    }

    @Override
    public void ejectQuarter() {
        // TODO Auto-generated method stub
        System.out.println("你已经购买了货物！");
    }

    @Override
    public void turnCrank() {
        // TODO Auto-generated method stub
        System.out.println("不能重复摇柄！");
    }

    @Override
    public void dispense() {
        // 为客户分配获取
        machine.releaseBall();
        if (machine.getCount() > 0) {
            machine.setState(machine.getNoQuarterState());
        } else {
            System.out.println("已售完！");
            machine.setState(machine.getSoldOutState());
        }
    }

}
