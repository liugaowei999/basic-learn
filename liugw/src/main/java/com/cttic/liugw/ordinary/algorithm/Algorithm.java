package com.cttic.liugw.ordinary.algorithm;

public class Algorithm {

    /**
     * 求最大公因子/最大公约数
     * 
     * 与getYueShu2 的计算逻辑是一样的。 写法不一样而已。 该写法更简洁
     * 
     * a = nb+r
     * a - nb =r
     * nb + r = a
     * 
     * @param a
     * @param b
     * @return
     */
    public static int getYueShu1(int a, int b) {
        if (b > 0)
            while ((a = a % b) > 0 && (b = b % a) > 0)
                ;
        return a + b;
    }

    /**
     * 求最大公因子/最大公约数
     * 
     * @param a
     * @param b
     * @return
     */
    public static int getYueShu2(int a, int b) {

        int result = 0;
        int max = (a > b ? a : b);
        int min = (a > b ? b : a);
        while (max >= min) {
            result = max % min;
            // 余数等于0， 则找到最大公约数了
            if (result == 0) {
                return min;
            }
            // 否则， 将max替换为min， 将min替换为余数，继续下一次循环判断
            max = min;
            min = result;
            //            System.out.println("max=" + max + ",min =" + min);
        }
        return -1;
    }

    public static void main(String[] args) {
        System.out.println("最大公因子/公约数：" + getYueShu1(544, 119));
        System.out.println("最大公因子/公约数：" + getYueShu1(24, 54));
    }
}
