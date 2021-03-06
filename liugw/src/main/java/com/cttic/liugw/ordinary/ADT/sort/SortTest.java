package com.cttic.liugw.ordinary.ADT.sort;

import java.util.Random;

public class SortTest {
    public static ArrayType array;

    static {
        //        array = new ArrayType(SIZE);
        //        Random random = new Random(System.currentTimeMillis());
        //        for (int i = 0; i < SIZE; i++) {
        //            array.insert(random.nextInt(SIZE));
        //        }
        //        array.display();
    }

    public static ArrayType getArray() {
        array = new ArrayType(SIZE);
        Random random = new Random(System.currentTimeMillis());
        for (int i = 0; i < SIZE; i++) {
            array.insert(random.nextInt(SIZE * 10));
        }
        //        array.display();
        return array;
    }

    private static final int SIZE = 99999999;

    public static void main(String[] args) {
        long aa = 11, bb = 12;
        System.out.println("aa=" + aa + ",bb=" + bb);
        aa ^= bb;
        bb ^= aa;
        aa ^= bb;
        System.out.println("aa=" + aa + ",bb=" + bb);

        ArrayType a = getArray();
        ArrayType b = getArray();
        //        b.display();
        //        ArrayType c = getArray();
        ArrayType m = getArray();
        ArrayType q = getArray();
        q.display();
        System.out.println("开始排序");

        //        b.bubbleSort();
        //a.selectSort();
        //c.insertSort();
        m.mergeSort();
        //b.shellSort();
        q.quickSort(true);
        b.quickSort(false);
        a.timSort();
        a.display();
        //        a.display();
        q.display();
        //        m.display();
        //        m.selectSort();
        //        m.display();
    }

}
