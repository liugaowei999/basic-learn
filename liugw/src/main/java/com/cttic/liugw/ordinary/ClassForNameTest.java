package com.cttic.liugw.ordinary;

class TestClass1{
    private static String aaa;
    private static String bbb = "hello world";
    
    static{
        aaa = "good morning";
        System.out.println("aaa="+aaa);
        System.out.println("bbb="+bbb);
    }
}


public class ClassForNameTest {

    public static void main(String[] args) {
        try {
            String aaa = "ddd";
            Class cls = Class.forName("ordinary.TestClass1",true,Thread.currentThread().getContextClassLoader());
//            Class cls = Class.forName("ordinary.ClassForNameTest",false,aaa.getClass().getClassLoader());
            System.out.println(cls.getClassLoader().toString());
        } catch (ClassNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
