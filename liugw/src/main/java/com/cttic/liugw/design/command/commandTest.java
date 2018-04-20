package com.cttic.liugw.design.command;

import com.cttic.liugw.design.command.entity.Light;
import com.cttic.liugw.design.command.entity.RemoteControl;
import com.cttic.liugw.design.command.impl.LightOffCommand;
import com.cttic.liugw.design.command.impl.LightOnCommand;
import com.cttic.liugw.design.command.intf.Command;

public class commandTest {

    public static void main(String[] args) {
        // 模拟卧室灯
        Light livingRoomLight = new Light("卧室灯");
        Light kichenRoomLight = new Light("厨房灯");

        // 生产一个遥控器
        RemoteControl remoteControl = new RemoteControl();
        // 设置遥控的功能
        LightOnCommand livingRoomLightOnCommand = new LightOnCommand(livingRoomLight);
        LightOffCommand livingRoomLightOffCommand = new LightOffCommand(livingRoomLight);

        LightOnCommand kichenRoomLightOnCommand = new LightOnCommand(kichenRoomLight);
        LightOffCommand kichenRoomLightOffCommand = new LightOffCommand(kichenRoomLight);

        remoteControl.setCommand(0, livingRoomLightOnCommand, livingRoomLightOffCommand);
        remoteControl.setCommand(1, kichenRoomLightOnCommand, kichenRoomLightOffCommand);

        System.out.println(remoteControl);

        // 模拟按键 -- 使用遥控器
        remoteControl.onButtonWasPushed(0);
        remoteControl.offButtonWasPushed(0);

        remoteControl.onButtonWasPushed(1);
        remoteControl.offButtonWasPushed(1);

        System.out.println("====================================== 无限记忆功能的Undo 操作 ====");
        remoteControl.undoButtonWasPushed();
        remoteControl.undoButtonWasPushed();
        remoteControl.undoButtonWasPushed();
        remoteControl.undoButtonWasPushed();
        remoteControl.undoButtonWasPushed();
        remoteControl.undoButtonWasPushed();

        // Command.aa = "rrrrrrrr";
        System.out.println(Command.aa);
    }
}
