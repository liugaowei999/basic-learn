package test1;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import test1.Animal;

public class Bird {
    Animal animal ;
    public Bird(){
        
    }
    public String getName(){
        animal = new Animal();
        animal.name="bird";
        animal.type= "a";
        return animal.name;
    }
    public static void main(String[] args) {
        String aa = new String("ab");
        String bb = new String("ab");
        List<Animal> list = new LinkedList<Animal>();
        list.add(new Animal());
        list.add(new Bird2());
        //list.add(new Bird());
        
        System.out.println(aa==bb);
        System.out.println(aa.equals(bb));
        System.out.println(aa.hashCode());
        System.out.println(bb.hashCode());
    }
}

