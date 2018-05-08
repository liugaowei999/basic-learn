package com.cttic.liugw.design.state.intf.impl;

import com.cttic.liugw.design.state.entity.GumballMachine;
import com.cttic.liugw.design.state.intf.State;

public class SoldOutState implements State {
    GumballMachine machine;

    public SoldOutState(GumballMachine machine) {
        this.machine = machine;
    }

    @Override
    public void insertQuarter() {
        // TODO Auto-generated method stub
        System.out.println("已售完，无法投币!");
    }

    @Override
    public void ejectQuarter() {
        // TODO Auto-generated method stub
        System.out.println("已售完，没有投币，无法退币!");
    }

    @Override
    public void turnCrank() {
        // TODO Auto-generated method stub
        System.out.println("已售完!");
    }

    @Override
    public void dispense() {
        // TODO Auto-generated method stub
        System.out.println("已售完!");
    }

}
