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

    public void bubbleSort() {
        long begin = System.currentTimeMillis();
        int out, in;
        for (out = nCount - 1; out > 1; out--) {
            for (in = 0; in < out; in++) {
                if (a[in] > a[in + 1]) {
                    swap(in, in + 1);
                }
            }
        }
        System.out.println("冒泡排序耗时:" + (System.currentTimeMillis() - begin));
    }

    public void selectSort() {
        long begin = System.currentTimeMillis();
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
        System.out.println("选择排序耗时:" + (System.currentTimeMillis() - begin));
    }

    private void swap(int l, int m) {
        long temp = a[l];
        a[l] = a[m];
        a[m] = temp;
    }

}
