package com.cttic.liugw.ordinary;

public class MissException {
    @SuppressWarnings("finally")
    public static int runtaskMissException() throws Exception {
        try {
            throw new Exception("runtaskMissException 有异常发生了!");
        } finally {
            return 1;
        }
    }

    public static int runtask() throws Exception {
        try {
            throw new Exception("runtask 有异常发生了!") {

                @Override
                public synchronized Throwable fillInStackTrace() {
                    // TODO Auto-generated method stub
                    return this;
                }
            };
        } finally {

        }
    }

    public static void main(String[] args) throws Exception {
        int a = runtaskMissException();
        System.out.println(a);
        runtask();
    }
}
