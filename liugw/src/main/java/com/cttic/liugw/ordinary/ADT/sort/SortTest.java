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
            array.insert(random.nextInt(SIZE * 100));
        }
        //        array.display();
        return array;
    }

    private static final int SIZE = 11;

    public static void main(String[] args) {
        ArrayType a = getArray();
        ArrayType b = getArray();
        //        b.display();
        ArrayType c = getArray();
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
        //        a.display();
        q.display();
        //        m.display();
        //        m.selectSort();
        //        m.display();
    }

}
