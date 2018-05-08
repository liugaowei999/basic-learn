package com.cttic.liugw.design.state.entity;

import com.cttic.liugw.design.state.intf.State;
import com.cttic.liugw.design.state.intf.impl.HasQuarterState;
import com.cttic.liugw.design.state.intf.impl.NoQuarterState;
import com.cttic.liugw.design.state.intf.impl.SoldOutState;
import com.cttic.liugw.design.state.intf.impl.SoldState;
import com.cttic.liugw.design.state.intf.impl.WinnerState;

/**
 * 状态模式 ： 允许对象在内部状态改变时改变它的行为， 对象看起来好像修改了它的类
 * 
 * @author liugaowei
 *
 */
public class GumballMachine {
    // 状态属性
    State soldOutState; // 售完状态对象
    State noQuarterState; // 未投币状态对象
    State hasQuarterState; // 已投币状态对象
    State soldState; // 已出货状态
    WinnerState winnerState; // 中奖状态

    State state = soldOutState;
    int count = 0; // 可出售的货物的数量

    public GumballMachine(int numGumballs) {
        soldOutState = new SoldOutState(this);
        noQuarterState = new NoQuarterState(this);
        hasQuarterState = new HasQuarterState(this);
        soldState = new SoldState(this);
        winnerState = new WinnerState(this);

        this.count = numGumballs;
        if (numGumballs > 0) {
            this.state = noQuarterState;
        } else {
            this.state = soldOutState;
        }
    }

    public void insertQuarter() {
        state.insertQuarter();
    }

    public void ejectQuarter() {
        state.ejectQuarter();
    }

    public void turnCrank() {
        state.turnCrank();
        state.dispense();
    }

    public void releaseBall() {
        System.out.println("货物已经分配， 请在出货口取货！");
        if (count > 0) {
            count--;
        }
    }

    public State getSoldOutState() {
        return soldOutState;
    }

    public void setSoldOutState(State soldOutState) {
        this.soldOutState = soldOutState;
    }

    public State getNoQuarterState() {
        return noQuarterState;
    }

    public void setNoQuarterState(State noQuarterState) {
        this.noQuarterState = noQuarterState;
    }

    public State getHasQuarterState() {
        return hasQuarterState;
    }

    public void setHasQuarterState(State hasQuarterState) {
        this.hasQuarterState = hasQuarterState;
    }

    public State getSoldState() {
        return soldState;
    }

    public void setSoldState(State soldState) {
        this.soldState = soldState;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public WinnerState getWinnerState() {
        return winnerState;
    }

    public void setWinnerState(WinnerState winnerState) {
        this.winnerState = winnerState;
    }

    @Override
    public String toString() {
        return "当前货物数量：[" + this.count + "], 当前状态：[" + state.getClass().getName() + "]";
    }

}
