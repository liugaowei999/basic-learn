package com.cttic.liugw.lucene.analizer.demo;

import java.io.IOException;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.cn.smart.SmartChineseAnalyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;

import com.cttic.liugw.lucene.analizer.ik.IKAnalyzer4Lucene7;

public class AnalizerTestDemo {

    // 分词处理
    public static void doToken(TokenStream ts) throws IOException {
        // 1. 清除所有属性 (incrementToken 实现里会做属性的清理）
        //ts.clearAttributes();

        // 1. 重置分词输入流， 使TokenStream恢复到可重用的安全的状态上去。
        ts.reset();

        CharTermAttribute charTermAttribute = ts.getAttribute(CharTermAttribute.class);
        //Consumers (i.e., IndexWriter) use this method to advance the stream to the next token.
        // 使用这个方法将流设置到下个词语的位置
        while (ts.incrementToken()) {
            System.out.print(charTermAttribute.toString() + "|");
        }
        System.out.println();
        ts.end();
        ts.close();

    }

    // 使用Lucene标准分词器
    public static void luceneStandAnaylzer(String text) {

        try (Analyzer analyzer = new StandardAnalyzer()) {
            TokenStream tokenStream = analyzer.tokenStream("content_field", text);
            doToken(tokenStream);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Smart 中文分词器
    public static void smartAnaylzer(String text) {

        try (Analyzer analyzer = new SmartChineseAnalyzer()) {
            TokenStream tokenStream = analyzer.tokenStream("content_field", text);
            doToken(tokenStream);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // IKAnaylzer 细粒度分词器
    public static void IKAnaylzer(String text) {

        try (Analyzer analyzer = new IKAnalyzer4Lucene7(false)) {
            TokenStream tokenStream = analyzer.tokenStream("content_field", text);
            doToken(tokenStream);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // IKAnalyzer 智能切分
    public static void IKAnaylzerSmart(String text) {

        try (Analyzer analyzer = new IKAnalyzer4Lucene7(true)) {
            TokenStream tokenStream = analyzer.tokenStream("content_field", text);
            doToken(tokenStream);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        String etext = "Analysis is one of the main causes of slow indexing. Simply put, the more you analyze the slower analyze the indexing (in most cases).";
        String chineseText = "张三说的确实在理。";
        String chineseText1 = "中华人民共和国简称中国。";

        // 使用Lucene标准分词器
        System.out.println("标准分词器，英文分词效果：");
        luceneStandAnaylzer(etext);

        System.out.println("标准分词器，中文分词效果：");
        luceneStandAnaylzer(chineseText);
        luceneStandAnaylzer(chineseText1);

        System.out.println("===================================================================");

        // 使用Smart 中文分词器
        System.out.println("Smart 中文分词器，英文分词效果：");
        smartAnaylzer(etext);

        System.out.println("Smart 中文分词器，中文分词效果：");
        smartAnaylzer(chineseText);
        smartAnaylzer(chineseText1);

        System.out.println("===================================================================");

        // IKAnaylzer 细粒度分词器
        System.out.println("IKAnaylzer 细粒度分词器，英文分词效果：");
        IKAnaylzer(etext);

        System.out.println("IKAnaylzer 细粒度分词器，中文分词效果：");
        IKAnaylzer(chineseText);
        IKAnaylzer(chineseText1);

        System.out.println("===================================================================");

        // IKAnalyzer 智能切分
        System.out.println("IKAnaylzer 智能切分，英文分词效果：");
        IKAnaylzerSmart(etext);

        System.out.println("IKAnaylzer 智能切分，中文分词效果：");
        IKAnaylzerSmart(chineseText);
        IKAnaylzerSmart(chineseText1);

    }

}
