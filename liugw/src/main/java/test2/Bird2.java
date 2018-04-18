package test2;

import test1.Animal;

public class Bird2 extends Animal{
    public Bird2(){
        
    }
    public String getName(){
        this.name="bird";
        this.age=12;
//        this.gender="a";
//        this.type="b";
        return name;
    }
}



