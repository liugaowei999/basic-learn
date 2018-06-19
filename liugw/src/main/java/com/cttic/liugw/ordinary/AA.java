package com.cttic.liugw.ordinary;

import java.io.Serializable;

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
    public static void main(String[] args) {
        AA aa = new AA();
        aa.cost1();
    }
}

class ParA {
    public static void cost() {
        System.out.println("ParA");
    }
}