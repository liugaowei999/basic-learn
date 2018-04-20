package com.cttic.liugw.design.command.intf;

public interface Command {
    public static String aa = "STR1";

    public abstract void execute();

    public abstract void undo();
}
