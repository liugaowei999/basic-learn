package com.cttic.liugw.expression.aviator;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import com.googlecode.aviator.AviatorEvaluator;
import com.googlecode.aviator.AviatorEvaluatorInstance;

/**
 * Aviator是一个高性能、轻量级的java语言实现的表达式求值引擎，主要用于各种表达式的动态求值。
 * 现在已经有很多开源可用的java表达式求值引擎，为什么还需要Avaitor呢？
 * Aviator的设计目标是轻量级和高性能，相比于Groovy、JRuby的笨重，Aviator非常小，
 * 加上依赖包也才450K,不算依赖包的话只有70K；当然，Aviator的语法是受限的，
 * 它不是一门完整的语言，而只是语言的一小部分集合。
 * 其次，Aviator的实现思路与其他轻量级的求值器很不相同，其他求值器一般都是通过解释的
 * 方式运行，而Aviator则是直接将表达式编译成Java字节码，交给JVM去执行。简单来说，
 * Aviator的定位是介于Groovy这样的重量级脚本语言和IKExpression这样的轻量级表达式引擎之间。
 * 
 * 使用ASM将表达式 ， 编译为一个class字节码
 * this.className = "Script_" + System.currentTimeMillis() + "_" + CLASS_COUNTER.getAndIncrement();
 * 
 * Aviator的特性
    支持大部分运算操作符，包括算术操作符、关系运算符、逻辑操作符、正则匹配操作符(=~)、三元表达式?:，并且支持操作符的优先级和括号强制优先级，具体请看后面的操作符列表。
    支持函数调用和自定义函数
    支持正则表达式匹配，类似Ruby、Perl的匹配语法，并且支持类Ruby的$digit指向匹配分组。
    自动类型转换，当执行操作的时候，会自动判断操作数类型并做相应转换，无法转换即抛异常。
    支持传入变量，支持类似a.b.c的嵌套变量访问。
    性能优秀
    Aviator的限制
    没有if else、do while等语句，没有赋值语句，没有位运算符
    仅支持逻辑表达式、算术表达式、三元表达式和正则匹配

 * 官网： https://github.com/killme2008/aviator/wiki
 * @author liugaowei
 *
 */
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
        String yourName = "Helen 1";
        Map<String, Object> env = new HashMap<String, Object>();
        env.put("yourName", yourName);
        String result = (String) AviatorEvaluator.execute(" 'hello ' + yourName ", env);
        System.out.println(result); // hello Michael

        /**
         * exec方法, 可以更方便地传入变量并执行, 而不需要构造env这个map
         * 只要在exec中按照变量在表达式中的出现顺序传入变量值就可以执行, 不需要构建Map了
         */
        String name = "Helen 2";
        result = (String) AviatorEvaluator.exec(" 'hello ' + $yourName ", name); // hello dennis
        System.out.println(result);

        name = "Helen 3";
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

    public static void main(String[] args) throws IOException {
        testNumberExpr();

        /**
         * 多行表达式， 以分号分割； 4.0开始支持
         * 多实例， 4.0开始支持
         */
        AviatorEvaluator.execute("println('hello Helen'); 1+2+3 ; 100-1");
        newAviatorExeInstance.execute("print('hello Helen'); 1+2+3 ; 100-1");
        newAviatorExeInstance.execute("println()");



        testVariable(); // 带变量的表达式

    }
}
