package com.cttic.liugw.ordinary.lambda;

import java.util.concurrent.Callable;
import java.util.function.BiFunction;

public class LambdaTest {
    public static void main(String[] args) {

        // 只有一个抽象方法的接口， 都默认为函数式接口。 任何一个lambda表达式都可以转换为这个接口的实例
        // Runnable 和 Callable 都只有一个抽象方法， 因此可以将lambda表达式赋值给它的实例。
        Runnable runnable = () -> {
            for (int i = 0; i < 10; i++) {
                System.out.println("a" + i);
            }
        };

        Callable<Void> callable = () -> {
            for (int i = 0; i < 5; i++) {
                System.out.println("a" + i);
            }
            //return 1;
            return null;
        };

        Thread thread = new Thread(() -> {
            for (int i = 0; i < 5; i++) {
                System.out.println("a" + i);
            }
        });

        thread.start();

        BiFunction<String, String, Integer> comp = (first, second) -> {
            return Integer.compare(first.length(), second.length());
        };

        BiFunction<String, String, Integer> comp1 = (first, second) -> Integer.compare(first.length(), second.length());

        ConcurrentGreeter concurrentGreeter = new ConcurrentGreeter();
        concurrentGreeter.greet();
    }
}

class Greeter {
    public void greet() {
        System.out.println("Hello world!");
    }
}

class ConcurrentGreeter extends Greeter {
    public void greet() {
        Thread thread = new Thread(super::greet);
        thread.start();
    }
}
