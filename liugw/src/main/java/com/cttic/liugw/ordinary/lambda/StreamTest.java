package com.cttic.liugw.ordinary.lambda;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Stream;

/**
 * 1. Stream 不会存储元素， 元素可能被存储在底层的集合中，或者根据需要产生出来。
 * 2. Stream操作符不会改变源对象。他们会返回一个持有结果的新Stream。
 * 3. Stream 操作符可能是延迟执行的。这意味着它们会等到需要结果的时候才执行。
 * 
 * 
 * @author liugaowei
 *
 */
public class StreamTest {
    public static void staticCount() throws Exception {
        long begin = System.nanoTime();
        String contents = new String(Files.readAllBytes(Paths.get("d:/img.txt")), StandardCharsets.UTF_8);
        List<String> words = Arrays.asList(contents.split("[\\P{L}]+"));
        //        words.forEach(System.out::println);
        //        words.stream().filter(w -> w.length() > 15).forEach(System.out::println);
        long count = words.stream().filter(w -> w.length() > 15).count();
        long end = System.nanoTime();
        System.out.println("count=" + count + ", cost time:" + (end - begin));
    }

    public static void parellCount() throws Exception {
        long begin = System.nanoTime();
        String contents = new String(Files.readAllBytes(Paths.get("d:/img.txt")), StandardCharsets.UTF_8);
        //List<String> words = Arrays.asList(contents.split("[\\P{L}]+"));
        Stream<String> words = Pattern.compile("[\\P{L}]+").splitAsStream(contents);
        //        words.forEach(System.out::println);
        //        words.stream().filter(w -> w.length() > 15).forEach(System.out::println);
        //long count = words.parallelStream().filter(w -> w.length() > 15).count();
        long count = words.filter(w -> w.length() > 15).count();
        long end = System.nanoTime();
        System.out.println("count=" + count + ", cost time:" + (end - begin));
    }

    public static void main(String[] args) throws Exception {

        Stream.of("a", "bdf", "d").forEach(System.out::println);
        Stream<String> empty = Stream.empty();

        // 创建一个含有常量值的无限的无序流Stream
        Stream<String> echos = Stream.generate(() -> "Echo");
        //System.out.println(echos.count());
        //        echos.forEach(System.out::println);

        // 创建一个无限的随机数流
        Stream<Double> generate = Stream.generate(Math::random);

        Stream<BigInteger> integers = Stream.iterate(BigInteger.ZERO, n -> n.add(BigInteger.ONE));
        //integers.forEach(System.out::println);
        Stream.iterate(BigInteger.ZERO, n -> n.add(BigInteger.ONE)).map(s -> s.add(BigInteger.TEN));
        //                .forEach(System.out::println);
        integers.peek(s -> s.add(BigInteger.TEN)).limit(10).forEach(System.out::println);

        try (Stream<String> lines = Files.lines(Paths.get("d:/img.txt"))) {
            lines.forEach(s -> {
                String aString = s.substring(1, 2);
                System.out.println(s);
                System.out.println(aString);
            });
        }

    }

    //    @Override
    //    public void start(Stage stage) throws Exception {
    //        Label label = new Label("xxxxxxxxxxxxdddd");
    //        label.setFont(new Font(100));
    //
    //        stage.setScene(new Scene(label));
    //        stage.setTitle("hello");
    //        stage.show();
    //
    //    }

}
