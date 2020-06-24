package com.cttic.liugw.algorithm;

import java.util.LinkedList;


public class MaxWindow {
    static int[] arr = {4,3,5,4,3,3,6,7};


    public static int[] getMaxWindow(int[] array, int widowSize) {
        int result[] = new int[array.length - widowSize + 1];
        if( null == array || widowSize < 1 || array.length < widowSize ) {
            return result;
        }
        LinkedList<Integer> qmax = new LinkedList<>();
        int index = 0;
        for (int i = 0; i < array.length; i++) {
            while (!qmax.isEmpty() && array[qmax.peekLast()] <= array[i]) {
                qmax.pollLast();
            }
            qmax.addLast(i);
            if (qmax.peekFirst() == i - widowSize) {
                System.out.println("执行弹出， i:" + (i) + ", qmax.peekFirst()=" + qmax.peekFirst());
                qmax.pollFirst();
            }
            if (i >= widowSize - 1) {
                result[index++] = array[qmax.peekFirst()];
            }
        }

        return result;
    }

    static String s = "hello";
    static StringBuilder ss = new StringBuilder("hello");

    static void func(String ss) {
        ss = "world";
//        System.out.println("--+ " + ss.hashCode());
//        System.out.println(ss);
//        System.out.println("--+ " + s.hashCode());
    }

    static void func(StringBuilder ss) {
        ss.append("world");
    }



    public static void main(String[] args) {
//        System.out.println(s.hashCode());
        func(s);
//        System.out.println(s);

        String s1 = new String("aaa");
        func(s1);
        System.out.println(s1);

//        func(ss);
//        System.out.println(ss);

        for (int i=0; i<arr.length; i++) {
            System.out.print(arr[i] + ",");
        }
        System.out.println();

        LinkedList<Integer> qmax = new LinkedList<>();
        qmax.addLast(1);
        qmax.addLast(2);
        System.out.println(qmax.peekFirst());
        System.out.println(qmax);
        System.out.println(qmax.pollFirst());
        System.out.println(qmax);

        int[] maxWindow = getMaxWindow(arr, 2);
        for (int i=0; i<maxWindow.length; i++) {
            System.out.print(maxWindow[i] + ",");
        }
        System.out.println();

        int a = 5;
        if (a<=5 ) {
            System.out.println("aaaa");
        } else if (a<6) {
            System.out.println("bbb");
        }

    }
}
