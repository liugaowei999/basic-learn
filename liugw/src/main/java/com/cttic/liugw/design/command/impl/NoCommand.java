package com.cttic.liugw.design.command.impl;

import com.cttic.liugw.design.command.intf.Command;

public class NoCommand implements Command{

    @Override
    public void execute() {
        // TODO Auto-generated method stub
        System.out.println("NoCommand, execute Do nothing.");
    }

    @Override
    public void undo() {
        // TODO Auto-generated method stub
        System.out.println("NoCommand, undo Do nothing.");
    }

}
