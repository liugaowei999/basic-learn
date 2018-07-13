package com.cttic.liugw.ordinary;

import java.io.Serializable;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;
import java.util.TreeSet;

/**
 * 基础知识：
 * 大端模式：是指数据的高字节保存在内存的低地址中，而数据的低字节保存在内存的高地址中，
 *          这样的存储模式有点儿类似于把数据当作字符串顺序处理：地址由小向大增加，而数据从高位往低位放；
 *          
 * 小端模式：是指数据的高字节保存在内存的高地址中，而数据的低字节保存在内存的低地址中，
 *          这种存储模式将地址的高低和数据位权有效地结合起来，高地址部分权值高，低地址部分权值低，和我们的逻辑方法一致。
 *          
 * 对于一个由2个字节组成的16位整数，在内存中存储这两个字节有两种方法：
 *        一种是将低序字节存储在起始地址，这称为小端(little-endian)字节序；
 *        另一种方法是将高序字节存储在起始地址，这称为大端(big-endian)字节序。
 *          
 * 在网络上必须采用网络字节顺序，也就是大端模式。
 */

/**
 * 
 *ByteBuffer中字节存储次序
 *
 *java 中ByteBuffer存储字节次序默认为大端模式
 *
 *程序输出：
 *[0, 97, 0, 98, 0, 100, 0, 99, 0, 101, 0, 102] -----》 大端存储
 *[0, 97, 0, 98, 0, 99, 0, 100, 0, 101, 0, 102] -----》 大端存储
 *[97, 0, 98, 0, 99, 0, 100, 0, 101, 0, 102, 0] -----》 小端存储
 */
public class Endians implements Serializable {

    /**
     * 判断主机CPU是按大端还是小端模式处理
     * windows intel: LITTLE_ENDIAN
     */
    public static void testCPU() {
        if (ByteOrder.nativeOrder() == ByteOrder.BIG_ENDIAN) {
            System.out.println("BIG_ENDIAN");
        } else {
            System.out.println("LITTLE_ENDIAN");
        }
    }

    public static void main(String[] args) {
        List<Integer> list = new LinkedList<>();
        list.get(1);

        TreeSet<Integer> set = new TreeSet<>();
        Iterator<Integer> iterator = set.descendingIterator();

        HashSet<String> hashSet = new HashSet<>();
        LinkedHashSet<String> linkedHashSet = new LinkedHashSet<>();

        Hashtable<String, String> hashtable = new Hashtable<>();
        Properties properties = new Properties();

        HashMap<String, String> hashmap = new HashMap<>();

        //创建12个字节的字节缓冲区
        ByteBuffer bb = ByteBuffer.wrap(new byte[12]);
        //存入字符串
        bb.asCharBuffer().put("abdcef");
        System.out.println(Arrays.toString(bb.array()));

        //反转缓冲区
        bb.rewind();
        //设置字节存储次序
        bb.order(ByteOrder.BIG_ENDIAN);
        bb.asCharBuffer().put("abcdef");
        System.out.println(Arrays.toString(bb.array()));

        //反转缓冲区
        bb.rewind();
        //设置字节存储次序
        bb.order(ByteOrder.LITTLE_ENDIAN);
        bb.asCharBuffer().put("abcdef");
        System.out.println(Arrays.toString(bb.array()));

        testCPU();
    }
}