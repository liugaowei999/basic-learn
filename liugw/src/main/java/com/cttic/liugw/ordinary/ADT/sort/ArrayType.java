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
     * 稳定的，就地得
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
     * 稳定的，就地得
     * 将移动元素的次数降到了最低
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

    /**
     * 稳定的，就地得
     * 适合少量数据和基本有序的数据
     * 比较次数时间复杂度还是 N*N, 但将移动元素的操作改为了复制(一次交换要做3次复制)， 复制的复杂度是 N*N
     * 最好的情况下只需要比较N次 ， O(N)
     */
    public void insertSort() {
        long begin = System.nanoTime();
        int out, in;
        for (out = 1; out < nCount; out++) {
            in = out;
            long temp = a[out];
            while (in > 0 && a[in - 1] > temp) {
                a[in] = a[in - 1]; // 只需要做一次复制， 不需要swap
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
