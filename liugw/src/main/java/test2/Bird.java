package test2;

import test1.Animal;

public class Bird {
    Animal animal ;
    public Bird(){
        
    }
    public String getName(){
        animal = new Animal();
        animal.name="bird";
        return animal.name;
    }
}


