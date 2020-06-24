package com.cttic.liugw.algorithm;

import java.util.LinkedList;
import java.util.Random;

/**
 * LinkedList : 基于链表结构的双向队列
 * 应避免使用普通的for循环， 因为每次随机读，都会造成一次链表的遍历。 应使用迭代器进行遍历，或者使用foreach
 *
 * ArrayList：基于数组结构的List
 * 普通的for循环读取，性能更高。 foreach 和 迭代器反而性能不是很好
 */
public class MaxWindow {
    static int[] arr = {4,3,5,4,3,3,6,7};
//    static final int size = 1000000;

    static {
        int size = 10000000;
        arr = new int[size];
        Random r = new Random();
        for (int i=0; i < size; i++) {
            arr[i] = r.nextInt(100);
        }
        System.out.println("================= 初始化结果:");
        for (int i=0; i < 30; i++) {
            System.out.print(arr[i] + ",");
        }
        System.out.println("\n================ 初始化结束 =============================");
    }


    /**
     * 使用双向队列
     *
     * @param array
     * @param widowSize
     * @return
     */
    public static int[] getMaxWindow(int[] array, int widowSize) {
        int result[] = new int[array.length - widowSize + 1];
        if( null == array || widowSize < 1 || array.length < widowSize ) {
            return result;
        }
        LinkedList<Integer> qmax = new LinkedList<>();
        int index = 0;
        for (int i = 0; i < array.length; i++) {
            // 队列为空 或 当前值大于等于队列中的最后一个元素，则从队列最后一个元素弹出
            while (!qmax.isEmpty() && array[qmax.peekLast()] <= array[i]) {
                qmax.pollLast();
            }
            // 将元素索引值放入队列尾部
            qmax.addLast(i);
            // 判断队列中第一个元素是否过期，过期则弹出
            if (qmax.peekFirst() == i - widowSize) {
                //System.out.println("执行弹出， i:" + (i) + ", qmax.peekFirst()=" + qmax.peekFirst());
                qmax.pollFirst();
            }
            // 队列中第一个元素就是当前窗口的最大值
            if (i >= widowSize - 1) {
                result[index++] = array[qmax.peekFirst()];
            }
        }

        return result;
    }

    /**
     * 不适用队列
     *
     * @param array
     * @param widowSize
     * @return
     */
    public static int[] getMaxWindowVersion2(int[] array, int widowSize) {
        int result[] = new int[array.length - widowSize + 1];
        if( null == array || widowSize < 1 || array.length < widowSize ) {
            return result;
        }

        int maxIndex = 0;
        int index = 0;
        for (int i = 0; i < array.length; i++) {
            if (array[i] >= array[maxIndex]) {
                maxIndex = i;
            }

            if (maxIndex <= i-widowSize) {
                maxIndex = maxIndex + 1;
                for (int j=maxIndex; j<i;j++) {
                    if (array[maxIndex] <= array[j] ) {
                        maxIndex = j;
                    }
                }
            }

            // 队列中第一个元素就是当前窗口的最大值
            if (i >= widowSize - 1) {
                result[index++] = array[maxIndex];
            }
        }
        return result;
    }


    /**
     * 1000万数组， 实际测试， version2 效率更高， 是队列方式的5倍
     *
     * @param args
     */
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

        for (int i = 0; i < arr.length; i++) {
            System.out.print(arr[i] + ",");
//        LinkedList<Integer> qmax = new LinkedList<>();
//        qmax.addLast(1);
//        qmax.addLast(2);
//        System.out.println(qmax.peekFirst());
//        System.out.println(qmax);
//        System.out.println(qmax.pollFirst());
//        System.out.println(qmax);
            int widowSize = 3;
            int printSize = 50;
            for (int hot = 0; hot < 0; hot++) {
                getMaxWindow(arr, widowSize);
                getMaxWindowVersion2(arr, widowSize);
            }
            long begin = System.currentTimeMillis();
            int[] maxWindow = getMaxWindow(arr, widowSize);
            long end = System.currentTimeMillis();
            for (i = 0; i < printSize; i++) {
                System.out.print(maxWindow[i] + ",");
            }
            System.out.println();
            System.out.println("-------------------------------------------- consume:" + (end - begin));

            begin = System.currentTimeMillis();
            int[] maxWindow2 = getMaxWindowVersion2(arr, widowSize);
            end = System.currentTimeMillis();
            for ( i = 0; i < printSize; i++) {
                System.out.print(maxWindow2[i] + ",");
            }
            System.out.println();
            System.out.println("-------------------------------------------- consume:" + (end - begin));

            int a = 5;
            if (a <= 5) {
                System.out.println("aaaa");
            } else if (a < 6) {
                System.out.println("bbb");
            }

        }
    }
}
