package com.cttic.liugw.ordinary.ADT.sort;

public class ArrayType {

    private long[] a;
    private int nCount;

    public ArrayType(int max) {
        a = new long[max];
        nCount = 0;
    }

    public void insert(long value) {
        a[nCount] = value;
        nCount++;
    }

    public void display() {
        for (long l : a) {
            System.out.print(l + " , ");
        }
        System.out.println("");
    }

    /**
     * 冒泡排序， 比较次数 和 移动元素的 时间复杂度都是 N*N
     */
    public void bubbleSort() {
        long begin = System.nanoTime();
        int out, in;
        for (out = nCount - 1; out > 1; out--) {
            for (in = 0; in < out; in++) {
                if (a[in] > a[in + 1]) {
                    swap(in, in + 1);
                }
            }
        }
        System.out.println("冒泡排序耗时:" + (System.nanoTime() - begin));
    }

    /**
     * 选择排序, 比较次数时间复杂度还是 N*N, 但将移动元素的时间复杂度降为了N
     */
    public void selectSort() {
        long begin = System.nanoTime();
        int out, in, min;
        for (out = 0; out < nCount; out++) {
            min = out;
            for (in = out + 1; in < nCount; in++) {
                if (a[out] > a[in]) {
                    min = in;
                }
            }
            if (min != out) {
                swap(out, min);
            }
        }
        System.out.println("选择排序耗时:" + (System.nanoTime() - begin));
    }

    public void insertSort() {
        long begin = System.nanoTime();
        int out, in;
        for (out = 1; out < nCount; out++) {
            in = out;
            long temp = a[out];
            while (in > 0 && a[in - 1] > temp) {
                a[in] = a[in - 1];
                --in;
            }
            a[in] = temp;
        }
        System.out.println("插入排序耗时:" + (System.nanoTime() - begin));
    }

    private void swap(int l, int m) {
        long temp = a[l];
        a[l] = a[m];
        a[m] = temp;
    }

}
