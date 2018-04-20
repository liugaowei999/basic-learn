package com.cttic.liugw.design.command.impl;

import com.cttic.liugw.design.command.intf.Command;

/**
 * 宏命令
 * @author liugaowei
 *
 */
public class MacroCommand implements Command{
    private Command[] commands;
    
    public MacroCommand(Command[] commands){
        this.commands = commands;
    }

    @Override
    public void execute() {
        // TODO Auto-generated method stub
        for (Command command : commands) {
            command.execute();
        }
    }

    @Override
    public void undo() {
        // TODO Auto-generated method stub
        for (Command command : commands) {
            command.undo();
        }
    }
    
    
}
