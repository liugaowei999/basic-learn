package com.cttic.liugw.design.command.entity;

/**
 * 命令接收者类（电灯）
 * 
 * @author liugaowei
 *
 */
public class Light {
    private String name ;
    
    public Light(String name){
        this.name = name;
    }
    
    public void on(){
        System.out.println(name + ":开灯");
    }
    
    public void off(){
        System.out.println(name + ":关灯");
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    
    
}
