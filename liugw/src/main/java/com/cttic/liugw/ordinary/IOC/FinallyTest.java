package com.cttic.liugw.ordinary.IOC;

public class FinallyTest {
    public static void main(String[] args) {
        System.out.print(getNumber(0));
        System.out.print(getNumber(1));
        System.out.print(getNumber(2));
        System.out.print(getNumber(4));

        byte a = 'a';
        byte b = 'b';
        System.out.println((char) a);
        replaceValue(a, b);
    }

    public static void replaceValue(byte a, byte b) {
        //        a[0] = (byte) (a[0] ^ b[0]);
        //        b[0] = (byte) (a[0] ^ b[0]);
        //        a[0] = (byte) (a[0] ^ b[0]);

        a = (byte) (a ^ b);
        b = (byte) (a ^ b);
        a = (byte) (a ^ b);
    }

    public static int getNumber(int num) {
        try {
            int result = 2 / num;

            return result;
        } catch (Exception exception) {
            return 0;
        } finally {
            if (num == 0) {
                return -1;
            }
            if (num == 1) {
                return 1;
            }
        }
    }
}
