package com.cttic.liugw.ordinary.ADT.sort;

import java.util.Arrays;

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
        int count = 0;
        for (long l : a) {
            System.out.print(l + " , ");
            if (count++ > 100)
                break;
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
        System.out.println("冒泡排序耗时:" + (System.nanoTime() - begin) / 1000000 + "ms");
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
        System.out.println("选择排序耗时:" + (System.nanoTime() - begin) / 1000000 + "ms");
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
        System.out.println("插入排序耗时:" + (System.nanoTime() - begin) / 1000000 + "ms");
    }

    public void insertSort(int left, int right) {
        int out, in;
        for (out = left + 1; out <= right; out++) {
            in = out;
            long temp = a[out];
            while (in > left && a[in - 1] > temp) {
                a[in] = a[in - 1]; // 只需要做一次复制， 不需要swap
                --in;
            }
            a[in] = temp;
        }
    }

    /**
     * 希尔排序 -- 基于插入排序的优化
     * 时间复杂度 很难评估
     * 希尔排序通过加大插入排序中的元素之间的间隔，并在这些有间隔的元素中进行插入排序，从而使数据项能大跨度
     * 的移动。
     * 
     * 选择跨度很重要， 跨度选择的： 间隔序列中数字互质通常被认为很重要；也就是除1之外，他们没有最大公约数。
     * 这个约束条件使每一趟排序更有可能保持前一趟排序已拍好的效果。
     */
    public void shellSort() {
        long begin = System.nanoTime();
        int inner, outer;
        long temp;

        int h = 1; // 跨度
        // 选择合适的最大跨度; 可能有更优的跨度值。一般这个取法也算可以了
        while (h <= nCount / 3) {
            h = h * 3 + 1; // 1,4,13,40,121
        }

        while (h > 0) {
            for (outer = h; outer < nCount; outer = outer + h) {
                temp = a[outer];
                inner = outer;
                while (inner > h - 1 && a[inner - h] > temp) {
                    // if (a[inner - h] > temp) {
                    a[inner] = a[inner - h];
                    inner = inner - h;
                    // }
                }
                a[inner] = temp;
            }
            h = (h - 1) / 3;
        }
        System.out.println("希尔排序耗时:" + (System.nanoTime() - begin) / 1000000 + "ms");

    }

    /**
     * 归并排序， 时间复杂度：O(N * logN)
     * 缺点：需要双倍的空间
     * // 10万条 0.35秒 是 选择，插入的 125倍
     */
    public void mergeSort() {
        long begin = System.nanoTime();
        long begin1 = System.currentTimeMillis();
        long[] workSpace = new long[nCount];
        recMergeSort(workSpace, 0, nCount - 1);
        System.out.println(
                "归并排序耗时:" + (System.currentTimeMillis() - begin1) + "ms");
    }

    public void timSort() {
        long begin = System.nanoTime();
        long begin1 = System.currentTimeMillis();
        Arrays.sort(a);
        System.out.println(
                "自适应归并排序TimSort耗时:" + (System.currentTimeMillis() - begin1) + "ms");
    }

    //    public int totalcount = 0;

    private void recMergeSort(long[] workSpace, int start, int end) {
        // 当切分的只剩下一个元素时， 此时不用排序了
        if (start == end) {
            return;
        } else {
            int mid = (start + end) / 2;

            recMergeSort(workSpace, start, mid);
            recMergeSort(workSpace, mid + 1, end);
            merge(workSpace, start, mid + 1, end);
        }
    }

    private void merge(long[] workSpace, int lstart, int hstart, int end) {
        int idx = 0;
        int segment_start = lstart;
        int mid = hstart - 1;
        int count = end - lstart + 1;
        //        totalcount++;

        while (lstart <= mid && hstart <= end) {
            if (a[lstart] < a[hstart]) {
                workSpace[idx++] = a[lstart++];
            } else {
                workSpace[idx++] = a[hstart++];
            }
        }

        while (lstart <= mid) {
            workSpace[idx++] = a[lstart++];
        }

        while (hstart <= end) {
            workSpace[idx++] = a[hstart++];
        }

        // 更新原始数组对应位置的元素为排好序的结果
        for (idx = 0; idx < count; idx++) {
            a[segment_start + idx] = workSpace[idx];
        }

    }

    /**
     * ===============================================================================================
     * 快速排序， 原地排序，空间复杂度O(1)
     * 时间复杂度：O(N * logN)
     * 最坏情况可能降为 N*N (每次排序的那部分都是有序的情景）
     * ===============================================================================================
     */

    /**
     * pivotSelectImprov: 是否优化选择枢轴元素的算法
     * 优化算法： 选择两头和中间位置的元素， 取中间值的元素 放在 最右端。
     * 优化后： 对随机序列排序没有明显的优势。 但是对基本有序的序列 很有优势。 总体来说：算法复杂度比较稳定。
     * 未优化算法如果排序 已经有序的序列，会事算法复杂度降为 N的平方。 因为每次partition只会分开一个元素，
     * 递归调用很容易 StackOverflowError。
     * 
     * @param pivotSelectImprov
     */
    public void quickSort(boolean pivotSelectImprov) {
        long begin = System.nanoTime();
        recQuickSort(0, nCount - 1, pivotSelectImprov);
        System.out.println("快速排序耗时:" + (System.nanoTime() - begin) / 1000000 + "ms");
    }

    private void recQuickSort(int left, int right, boolean pivotSelectImprov) {
        // 进一步优化快速排序： 当剩余的元素小于一定个数时，使用插入排序或者蛮力排序
        int size = right - left + 1;
        //if (right - left <= 0) {
        //    return;
        if (size < 10) {
            insertSort(left, right);
        } else if (size <= 3) {
            manualSort(left, right);
        } else {

            if (pivotSelectImprov) {
                selectPivot(left, right);
            }
            // 选择枢轴
            long pivot = a[right];
            int partionPos = doPartion(left, right, pivot);
            recQuickSort(left, partionPos - 1, pivotSelectImprov);
            recQuickSort(partionPos + 1, right, pivotSelectImprov);
        }

    }

    private int doPartion(int left, int right, long pivot) {
        int leftPtr = left - 1;
        int rightPtr = right;
        while (true) {
            // 从最左边开始找大于枢轴的元素; 遇到大的，while停止
            while (a[++leftPtr] < pivot)
                ;

            // 从最右边开始找小于枢轴的元素; 遇到小的，while停止
            while (rightPtr > 0 && a[--rightPtr] > pivot)
                ;
            if (leftPtr >= rightPtr) {
                break;
            } else {
                swap(leftPtr, rightPtr);
            }
        } // end while (true)
        swap(leftPtr, right); // 将枢轴的值放在中间分割的位置
        return leftPtr;
    }

    private void manualSort(int left, int right) {
        int size = right - left + 1;
        //        System.out.println(right + ", " + left);
        if (size <= 1) {
            return; // 1个元素时认为是有序的
        } else if (size == 2) {
            if (a[left] > a[right]) {
                swap(left, right);
            }
        } else {
            if (a[left] > a[right - 1]) {
                swap(left, right - 1);
            }

            if (a[left] > a[right]) {
                swap(left, right);
            }

            if (a[right - 1] > a[right]) {
                swap(right - 1, right);
            }
        }

    }

    /**
     * 取3个数， 将中间值放在 最右边的位置
     * @param left
     * @param right
     */
    private void selectPivot(int left, int right) {
        // 数据量少时，直接取右边的
        if (right < 5) {
            return;
        }
        int mid = (left + right) / 2;
        if ((a[left] <= a[mid] && a[mid] <= a[right]) || (a[right] <= a[mid] && a[mid] <= a[left])) {
            swap(mid, right);
        } else if ((a[mid] <= a[left] && a[left] <= a[right]) || (a[right] <= a[left] && a[left] <= a[mid])) {
            swap(left, right);
        }
    }

    /**
     * ===============================================================================================
     * ===============================================================================================
     */
    private void swap(int l, int m) {
        long temp = a[l];
        a[l] = a[m];
        a[m] = temp;
    }

    public long get(int index) {
        return a[index];
    }

    public void set(int index, long value) {
        a[index] = value;
    }

    /**
     * ===============================================================================================
     * 基数排序
     * 需要额外一倍的存储空间。 是快速排序空间的2倍， 算法复杂度差不多 N*logN
     * 对整个序列， 先排个位， 再排十位 ->百位 ->千位.................
     * 101 252 120 312 ===> 开始
     * -> 120 101 252 312  ---> 按个位排序
     * -> 101 312 120 252  ---> 对按个位排序后的结果，再按十位排序
     * -> 101 120 252 312  ---> 对按十位排序后的结果，再按百位排序
     *                 ===> 结束
     * ===============================================================================================
     */

}
