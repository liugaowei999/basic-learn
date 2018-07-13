package com.cttic.liugw.ordinary;

import java.io.Serializable;
import java.util.Map;
import java.util.TreeMap;

public class AA extends ParA implements Serializable {
    int a;
    transient String bString;

    public AA() {

    }

    public static void cost() {
        //super.cost();
        System.out.println("AA");
    }

    public void cost1() {
        super.cost();
        System.out.println("AA");
    }

    public AA(int a, String bString) {
        this.a = a;
        this.bString = bString;
    }

    //public String toString()
    //{
    //  return "a = " + this.a + ", bString = " + this.bString;
    //}

    public static void setName(Persion persion) {
        System.out.println("1:" + persion);
        persion = new Persion();
        System.out.println("2:" + persion);
        persion.name = "BBBBBB";
    }

    public static void main(String[] args) {
        AA aa = new AA();
        aa.cost1();

        Persion persion = new Persion();
        System.out.println("0:" + persion);
        persion.name = "AAAAA";
        setName(persion);
        System.out.println(persion.name);
        Map<String, String> hashMap = new TreeMap<>();
        System.out.println("1: " + hashMap.size());
        hashMap.put("a", "1");
        hashMap.put("b", "1");
        System.out.println("2: " + hashMap.size());
        hashMap.put("a1", "2");
        System.out.println("3: " + hashMap.size());
        hashMap.put("a", "2");
        System.out.println("4: " + hashMap.size());
    }
}

class ParA {
    public static void cost() {
        System.out.println("ParA");
    }
}

class Persion {
    public String name;
}