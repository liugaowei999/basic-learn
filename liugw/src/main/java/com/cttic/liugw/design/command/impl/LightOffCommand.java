package com.cttic.liugw.design.command.impl;

import com.cttic.liugw.design.command.entity.Light;
import com.cttic.liugw.design.command.intf.Command;

public class LightOffCommand implements Command{
    Light light;
    
    public LightOffCommand(Light light){
        this.light = light;
    }

    @Override
    public void execute() {
        // TODO Auto-generated method stub
        light.off();
    }

    @Override
    public void undo() {
        // TODO Auto-generated method stub
        light.on();
    }

}
