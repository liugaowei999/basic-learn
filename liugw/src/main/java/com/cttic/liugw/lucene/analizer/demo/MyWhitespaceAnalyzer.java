package com.cttic.liugw.lucene.analizer.demo;

import java.io.IOException;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenFilter;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.Tokenizer;
import org.apache.lucene.util.Attribute;
import org.apache.lucene.util.AttributeImpl;
import org.apache.lucene.util.AttributeReflector;

public class MyWhitespaceAnalyzer extends Analyzer {

    @Override
    protected TokenStreamComponents createComponents(String fieldName) {
        // 建立自己的 分词器
        Tokenizer tokenizer = new MyWhitespaceTokenizer();
        TokenStream filter = new MyLowerCaseTokenFilter(tokenizer);

        return new TokenStreamComponents(tokenizer, filter);
    }

    public static interface MyCharAttribute extends Attribute {
        void setChars(char[] buffer, int length);

        char[] getChars();

        int getLength();

        String getString();
    }

    /**
     * 实现类的名称必须是 是接口名称 加"Impl" : "MyCharAttribute" + "Impl" =
     * "MyCharAttributeImpl"
     * 
     * @author liugaowei
     *
     */
    public static class MyCharAttributeImpl extends AttributeImpl implements MyCharAttribute {

        private char[] charTerm = new char[255];
        private int length = 0;

        @Override
        public void setChars(char[] buffer, int length) {
            this.length = length;
            // 未检测长度大于255的情况
            if (length > 0) {
                System.arraycopy(buffer, 0, charTerm, 0, length);
            }
        }

        @Override
        public char[] getChars() {
            return charTerm;
        }

        @Override
        public int getLength() {
            return length;
        }

        @Override
        public String getString() {
            if (this.length > 0) {
                return new String(this.charTerm, 0, length);
            }
            return null;
            //return String.valueOf(charTerm);
        }

        @Override
        public void clear() {
            this.length = 0;
        }

        @Override
        public void reflectWith(AttributeReflector reflector) {
            // TODO Auto-generated method stub

        }

        @Override
        public void copyTo(AttributeImpl target) {
            // TODO Auto-generated method stub

        }

    }

    /**
     * 使用空格进行分词的 自定义分词器
     * 
     * @author liugaowei
     *
     */
    public static class MyWhitespaceTokenizer extends Tokenizer {

        ////////////////////// 需要记录的属性 /////////////////////////////
        // 属性1： 词
        // addAttribute 只需要传接口的名称，实现的类名，通过接口名+"Impl" 拼接而成，然后通过反射实例化
        MyCharAttribute mycharAttr = addAttribute(MyCharAttribute.class);

        char[] buffer = new char[255];
        int length = 0;
        int c;

        @Override
        public boolean incrementToken() throws IOException {
            // 1. 清楚所有词元属性
            clearAttributes();

            // 
            length = 0;

            while (true) {
                c = this.input.read();

                // 如果没有内容可读了, -1 表示Reader 流读取结束
                if (c == -1) {
                    if (length > 0) {
                        mycharAttr.setChars(buffer, length);
                        return true;
                    } else {
                        return false;
                    }
                }

                // 如果是空格， 则生成一个词元
                if (Character.isWhitespace(c)) {
                    if (length > 0) {
                        mycharAttr.setChars(buffer, length);
                        return true;
                    } else {
                        return false;
                    }
                }

                buffer[length++] = (char) c;
            }
        }

    }

    /**
     * 将大写转换为小写的 词元转换 装饰器
     * 
     * 将MyWhitespaceTokenizer的处理结果的词元对象 做二次处理进行大写转小写
     * 
     * @author liugaowei
     *
     */
    public static class MyLowerCaseTokenFilter extends TokenFilter {

        // 注册词元属性
        MyCharAttribute charAttr = this.addAttribute(MyCharAttribute.class);

        protected MyLowerCaseTokenFilter(TokenStream input) {
            super(input);
        }

        @Override
        public boolean incrementToken() throws IOException {
            boolean res = this.input.incrementToken();
            if (res) {
                char[] chars = charAttr.getChars();
                int length = charAttr.getLength();
                if (length > 0) {
                    for (int i = 0; i < length; i++) {
                        chars[i] = Character.toLowerCase(chars[i]);
                    }
                }
            }
            return res;
        }

    }

    public static void main(String[] args) {
        String text = "An AttributeSource contains a list of different AttributeImpls, and methods to add and get them. ";
        System.out.println(text);
        try (MyWhitespaceAnalyzer myana = new MyWhitespaceAnalyzer(); TokenStream ts = myana.tokenStream("aa", text)) {

            MyCharAttribute myCharAttribute = ts.getAttribute(MyCharAttribute.class);
            ts.reset();

            while (ts.incrementToken()) {
                System.out.print(myCharAttribute.getString() + "|");
            }

            ts.end();
            System.out.println("\n=====================================");
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

}
