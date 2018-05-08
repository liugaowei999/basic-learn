package com.cttic.liugw.design.state.intf.impl;

import java.util.Random;

import com.cttic.liugw.design.state.entity.GumballMachine;
import com.cttic.liugw.design.state.intf.State;

/**
 * 没有投币的状态类 封装
 * 
 * @author liugaowei
 *
 */
public class HasQuarterState implements State {
    Random randomWinner = new Random(System.currentTimeMillis()); // 增加一个随机数
    GumballMachine machine;

    public HasQuarterState(GumballMachine machine) {
        this.machine = machine;
    }

    @Override
    public void insertQuarter() {
        System.out.println("已经投入了硬币，不能重复投币， 请选择货物进行购买.");
    }

    @Override
    public void ejectQuarter() {
        System.out.println("硬币退回！");
        machine.setState(machine.getNoQuarterState());
    }

    @Override
    public void turnCrank() {
        System.out.println("开始摇柄......");
        int win = randomWinner.nextInt(10); // 设置10%的几率
        if (win == 0 && machine.getCount() > 1) {
            machine.setState(machine.getWinnerState());
        } else {
            machine.setState(machine.getSoldState());
        }
    }

    @Override
    public void dispense() {
        // TODO Auto-generated method stub
        System.out.println("已经出货！");
    }

}
