package com.cttic.liugw.design.command.impl;

import com.cttic.liugw.design.command.entity.Light;
import com.cttic.liugw.design.command.intf.Command;

/**
 * 开灯的命令对象 
 * @author liugaowei
 *
 */
public class LightOnCommand implements Command{
    Light light;
    
    public LightOnCommand(Light light){
        this.light = light;
    }

    @Override
    public void execute() {
        // TODO Auto-generated method stub
        light.on();
    }

    @Override
    public void undo() {
        // TODO Auto-generated method stub
        light.off();
    }

}
