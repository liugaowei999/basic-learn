package com.cttic.liugw.design.command.entity;

import com.cttic.liugw.design.command.impl.NoCommand;
import com.cttic.liugw.design.command.intf.Command;
import com.cttic.liugw.utils.Stack;

/**
 * 模拟遥控器类 （客户）
 * @author liugaowei
 *
 */
public class RemoteControl {
    private static final int SLOT_SIZE = 7;

    Command[] onCommands;
    Command[] offCommands;
    Stack<Command> stack;
    
    
    public RemoteControl(){
        onCommands = new Command[SLOT_SIZE];
        offCommands = new Command[SLOT_SIZE];

        NoCommand noCommand = new NoCommand();
        for(int i=0; i<SLOT_SIZE; i++){
            onCommands[i] = noCommand;
            offCommands[i] = noCommand;
        }
        
        stack = new Stack<>(5);
    }
    
    public void setCommand(int slot, Command onCommand, Command offCommand){
        onCommands[slot] = onCommand;
        offCommands[slot] = offCommand;
    }
    
    public void onButtonWasPushed(int slot) {
        onCommands[slot].execute();
        stack.push(onCommands[slot]);
    }
    
    public void offButtonWasPushed(int slot){
        offCommands[slot].execute();
        stack.push(offCommands[slot]);
    }
    
    public void undoButtonWasPushed(){
        if(!stack.isEmpty()){
            stack.pop().undo();
        }
    }
    
    @Override
    public String toString(){
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("\n ---------------- 远程遥控控制 ------------------------\n");
        for(int i=0; i<onCommands.length; i++){
            stringBuffer.append("【按钮"+ i + "】" + onCommands[i].getClass().getName() + 
                    "        " + offCommands[i].getClass().getName() + "\n");
        }
        return stringBuffer.toString();
    }
}
