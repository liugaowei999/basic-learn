package com.cttic.liugw.ordinary.asmtest;

public class TimeStat {
    static ThreadLocal<Long> threadLocal = new ThreadLocal<>();

    public static void start() {
        threadLocal.set(System.currentTimeMillis());
    }

    public static void end() {
        long time = System.currentTimeMillis() - threadLocal.get();
        System.out.println(Thread.currentThread().getStackTrace()[2] + " 耗时：[" + time + "ms]");
    }
}
