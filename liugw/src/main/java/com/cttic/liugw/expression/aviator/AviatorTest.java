package com.cttic.liugw.expression.aviator;

import java.util.HashMap;
import java.util.Map;

import com.googlecode.aviator.AviatorEvaluator;
import com.googlecode.aviator.AviatorEvaluatorInstance;

public class AviatorTest {
    // 多实例， 4.0开始支持
    static AviatorEvaluatorInstance newAviatorExeInstance = AviatorEvaluator.newInstance();

    /**
     * 数值类型测试
     * Aviator的数值类型仅支持Long和Double, 
     * 任何整数都将转换成Long, 任何浮点数都将转换为Double,
     * 
     * @param args
     */
    public static void testNumberExpr() {
        Object result = AviatorEvaluator.execute("1+2+3");
        System.out.println(result);

        Long result1 = (Long) AviatorEvaluator.execute("1+2+3");
        System.out.println(result1);

        String expr = "((1000+500/25*8)/20)*23+89.1+value";
        Double result2 = (Double) AviatorEvaluator.exec(expr, 2);
        System.out.println(result2);
    }

    /**
     * 带变量的表达式
     */
    public static void testVariable() {
        String yourName = "Michael";
        Map<String, Object> env = new HashMap<String, Object>();
        env.put("yourName", yourName);
        String result = (String) AviatorEvaluator.execute(" 'hello ' + yourName ", env);
        System.out.println(result); // hello Michael

        /**
         * exec方法, 可以更方便地传入变量并执行, 而不需要构造env这个map
         * 只要在exec中按照变量在表达式中的出现顺序传入变量值就可以执行, 不需要构建Map了
         */
        String name = "dennis";
        result = (String) AviatorEvaluator.exec(" 'hello ' + $yourName ", name); // hello dennis
        System.out.println(result);

        name = "Helen";
        AviatorEvaluator.exec(" println('hello ' + yourName) ", name);

        // 调用函数
        // Aviator 支持函数调用, 函数调用的风格类似 lua, 下面的例子获取字符串的长度:
        Long length = (Long) AviatorEvaluator.execute("string.length('hello')");
        System.out.println(length);

        /**
         * 通过string.substring('hello', 1, 2)获取字符串'e', 然后通过函数string.contains判断e是否在'test'中。
         * 可以看到, 函数可以嵌套调用
         */
        Boolean result1 = (Boolean) AviatorEvaluator
                .execute("string.contains(\"test\", string.substring('hello', 1, 2))"); // true
        System.out.println(result1);

        /**
         * 4.0.0 开始， aviator 支持通过 lambda 关键字定义一个匿名函数，并且支持闭包捕获：
         * 匿名函数的基本定义形式是: lambda (参数1,参数2...) -> 参数体表达式 end
         */
        length = (Long) AviatorEvaluator.exec("(lambda (x,y) -> x + y end)(x,y)", 1, 2);
        System.out.println(length);
    }

    public static void main(String[] args) {
        testNumberExpr();

        /**
         * 多行表达式， 以分号分割； 4.0开始支持
         * 多实例， 4.0开始支持
         */
        AviatorEvaluator.execute("println('hello world'); 1+2+3 ; 100-1");
        newAviatorExeInstance.execute("print('hello world'); 1+2+3 ; 100-1");
        newAviatorExeInstance.execute("println()");

        testVariable(); // 带变量的表达式

    }
}
