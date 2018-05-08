package com.cttic.liugw.design.state.intf.impl;

import com.cttic.liugw.design.state.entity.GumballMachine;
import com.cttic.liugw.design.state.intf.State;

public class WinnerState implements State {
    GumballMachine machine;

    public WinnerState(GumballMachine machine) {
        this.machine = machine;
    }

    @Override
    public void insertQuarter() {
        // TODO Auto-generated method stub
        System.out.println("No supported!");
    }

    @Override
    public void ejectQuarter() {
        // TODO Auto-generated method stub
        System.out.println("No supported!");
    }

    @Override
    public void turnCrank() {
        // TODO Auto-generated method stub
        System.out.println("No supported!");
    }

    @Override
    public void dispense() {
        // TODO Auto-generated method stub
        System.out.println("恭喜你中奖了， 额外送一份!");
        machine.releaseBall();
        if (machine.getCount() == 0) {
            machine.setState(machine.getSoldOutState());
        } else {
            machine.releaseBall();
            if (machine.getCount() == 0) {
                System.out.println("已售完!");
                machine.setState(machine.getSoldOutState());
            } else {
                machine.setState(machine.getNoQuarterState());
            }
        }
    }

}
