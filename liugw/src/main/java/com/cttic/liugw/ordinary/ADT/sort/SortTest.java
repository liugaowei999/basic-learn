package com.cttic.liugw.ordinary.ADT.sort;

import java.util.Random;

public class SortTest {
    public static ArrayType array;
    private static final int SIZE = 10;

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
            array.insert(random.nextInt(SIZE * 100));
        }
        //        array.display();
        return array;
    }

    public static void main(String[] args) {
        ArrayType a = getArray();
        ArrayType b = getArray();
        ArrayType c = getArray();
        System.out.println("开始排序");

        b.bubbleSort();
        a.selectSort();
        c.insertSort();
        //        a.display();
        //        b.display();
        c.display();
    }

}
