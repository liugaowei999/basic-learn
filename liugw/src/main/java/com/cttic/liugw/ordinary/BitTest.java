package com.cttic.liugw.ordinary;

import java.util.BitSet;

public class BitTest {

    public void bitTest() {
        BitSet bitSet = new BitSet(1024);
        int[] ia = { 4, 7, 2, 5, 3 };
        for (int i : ia) {
            bitSet.set(i, true);
        }
        int size = bitSet.size();
        System.out.println("size=" + size);
        for (int j = 0; j < size; j++) {
            boolean b = bitSet.get(j);
            if (b)
                System.out.print(j + " ");
        }
    }

    public static void main(String[] args) {
        //        bitTest();

        Long aa = Long.valueOf("20171231000000123");
        Long bb = Long.valueOf("20171031000000125");
        long res = bb - aa;
        System.out.println(aa.toString());
        System.out.println(res);
    }
}
