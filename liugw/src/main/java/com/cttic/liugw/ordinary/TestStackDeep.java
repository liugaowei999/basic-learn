package com.cttic.liugw.ordinary;

public class TestStackDeep {

    private static int count = 0;

    public static void recursion() {
        count++;
        if (count <= 102500) {
            recursion();
        }
    }

    /**
     *  -Xss1024k(默认1M） -Xmx32M
     * @param args
     */
    public static void main(String[] args) {
        System.out.println("-Xmx" + Runtime.getRuntime().maxMemory() / 1000 / 1000 + "M");
        try {
            recursion();
        } catch (Exception e) {
            // TODO: handle exception

            e.printStackTrace();
        } finally {
            System.out.println("count=" + count);
        }

    }
}
