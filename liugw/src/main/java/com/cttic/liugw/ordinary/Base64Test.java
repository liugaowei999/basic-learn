package com.cttic.liugw.ordinary;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.Base64;
import java.util.Base64.Decoder;
import java.util.Base64.Encoder;

/************************************************************************************
 * 基础知识：
 * Java 8实现了BASE64编解码API，它包含到java.util包
 * java.util.Base64工具类提供了一套静态方法获取下面三种BASE64编解码器：
 *  1）Basic编码 ：用于处理常规的需求：输出的内容不添加换行符，而且输出的内容由字母加数字组成
 *  2）URL编码 ： 由于URL对反斜线“/”有特殊的意义，因此URL编码需要替换掉它，使用下划线替换
 *  3）MIME编码 ： MIME编码器会使用基本的字母数字产生BASE64输出，而且对MIME格式友好：每一行输出不超过76个字符，而且每行以“\r\n”符结束。
 *  
 *  同时： java API 还提供的对流的包装。 
 */

/**
 * 本类时对以上三种编码方式的演示。
 * 
 * 同时实现一个通过BASE64编码 将 图片转 txt文件 和 从txt文件 还原成图片的实现示例。
 * 
 * @author liugaowei
 *
 */
public class Base64Test {
    /*********************************************************************************
     *        Basic编码 
     *********************************************************************************/
    /**
     * Basic编码 （基本的base64编码， 编码之后的字符可能带有反斜杠/, 不适合作为url地址的一部分）
     * @param str
     * @return
     * @throws UnsupportedEncodingException
     */
    public static String basicEncode64(String str) throws UnsupportedEncodingException {
        Encoder encoder = Base64.getEncoder();
        return encoder.encodeToString(str.getBytes("utf-8"));
    }

    public static String basicEncode64(byte[] bytes) throws UnsupportedEncodingException {
        Encoder encoder = Base64.getEncoder();
        return encoder.encodeToString(bytes);
    }

    public static byte[] basicDecode64(String str) {
        Decoder decoder = Base64.getDecoder();
        return decoder.decode(str);
    }

    /*********************************************************************************
     *        URL编码 ----- 针对URL的base64 编码， 编码后的数据去除了可能影响url地址的字符， 例如"/"
     *********************************************************************************/
    public static String URLEncode64(String str) throws UnsupportedEncodingException {
        Encoder encoder = Base64.getUrlEncoder();
        return encoder.encodeToString(str.getBytes("utf-8"));
    }

    public static byte[] URLDecode64(String str) {
        Decoder decoder = Base64.getUrlDecoder();
        return decoder.decode(str);
    }

    /*********************************************************************************
     *        MIME编码 ----- 
     *        MIME编码器会使用基本的字母数字产生BASE64输出，而且对MIME格式友好：
     *        每一行输出不超过76个字符，而且每行以“\r\n”符结束。比如下面的例子：
     *********************************************************************************/
    public static String MIMEEncode64(String str) throws UnsupportedEncodingException {
        Encoder encoder = Base64.getMimeEncoder();
        return encoder.encodeToString(str.getBytes("utf-8"));
    }

    public static String MIMEEncode64(byte[] bytes) throws UnsupportedEncodingException {
        Encoder encoder = Base64.getMimeEncoder();
        return encoder.encodeToString(bytes);
    }

    public static byte[] MIMEDecode64(String str) {
        Decoder decoder = Base64.getMimeDecoder();
        return decoder.decode(str);
    }

    /******************************************************************************
     * 二进制图片 转 txt文件                                                       *
     * txt文件 转 二进制图片文件                                                   *
     *****************************************************************************/

    public static void transImageToTxt(String inputimgfile, String outTxtPath) throws Exception {
        // 图片等二进制编码
        FileInputStream fileInputStream = new FileInputStream(new File(inputimgfile));
        BufferedInputStream bufferedInputStream = new BufferedInputStream(fileInputStream);

        StringBuilder stringBuilder = new StringBuilder();
        byte[] imgBytes = new byte[1024];

        File inputFile = new File(outTxtPath);
        if (!inputFile.exists())
            inputFile.createNewFile();
        FileWriter fileOutputStream = new FileWriter(inputFile);
        BufferedWriter bufferedwriter = new BufferedWriter(fileOutputStream);

        while (bufferedInputStream.read(imgBytes) != -1) {
            stringBuilder.append(basicEncode64(imgBytes) + "\r\n");
        }
        bufferedwriter.write(stringBuilder.toString());
        bufferedwriter.close();
        fileOutputStream.close();
        bufferedInputStream.close();
    }

    public static void transTxtToImage(String txtfilePath, String outimgfile) throws Exception {
        BufferedReader bufferedReader = new BufferedReader(new FileReader(new File(txtfilePath)));
        FileOutputStream fileOutputStream = new FileOutputStream(new File(outimgfile));
        BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(fileOutputStream);

        String line;
        while ((line = bufferedReader.readLine()) != null) {
            //            System.out.println(line);
            byte[] bytes = basicDecode64(line);
            bufferedOutputStream.write(bytes);
            //            System.out.println("Decode=" + new String(bytes));
        }
        bufferedReader.close();
        bufferedOutputStream.flush();
        bufferedOutputStream.close();
        fileOutputStream.close();
    }

    /******************************************************************************
     * 使用流的方式处理                                                            *
     * 二进制图片 转 txt文件                                                       *
     * txt文件 转 二进制图片文件                                                   *
     *****************************************************************************/
    public static void transTxtToImageWrap(String txtfilePath, String outimgfile) {

        try (InputStream inputStream = Base64.getDecoder().wrap(new FileInputStream(new File(txtfilePath)));
                FileOutputStream fileOutputStream = new FileOutputStream(new File(outimgfile));
                BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(fileOutputStream);) {
            byte[] bytes = new byte[128];
            int len = 0;
            while ((len = inputStream.read(bytes)) != -1) {
                bufferedOutputStream.write(bytes, 0, len);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static void transImageToTxtWrap(String inputimgfile, String outTxtPath) throws Exception {
        try (OutputStream outputStream = Base64.getEncoder().wrap(new FileOutputStream(new File(outTxtPath)));
                FileInputStream fileInputStream = new FileInputStream(new File(inputimgfile));) {

            byte[] bytes = new byte[128];
            int len = 0;
            while ((len = fileInputStream.read(bytes)) != -1) {
                outputStream.write(bytes, 0, len);
            }

        } catch (final Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            throw e;
        }
    }

    public static void main(String[] args) throws Exception {
        String encodeStr = basicEncode64("hello world!");
        System.out.println("Encode=" + encodeStr);

        byte[] bytes = basicDecode64(encodeStr);
        System.out.println("Decode=" + new String(bytes));

        // 比较下面两个输出的区别：basic编码带有 / ， URL编码不带 /
        encodeStr = basicEncode64("subjects?abcd");
        System.out.println("Basic Encode（有/）=" + encodeStr);

        System.out.println("\n===========================================");
        encodeStr = URLEncode64("subjects?abcd");
        System.out.println("URL   Encode（没/了）=" + encodeStr);

        bytes = URLDecode64(encodeStr);
        System.out.println("URL   Decode=" + new String(bytes));
        System.out.println("\n##########################################################################\n");

        // MIME 编解码器测试
        encodeStr = MIMEEncode64("subjects?abcd");
        System.out.println("MIME Encode=" + encodeStr);

        // MIME 编码后的内容如果很长超过67个字符，会自动换行。 basic编码器总是一行
        encodeStr = MIMEEncode64(
                "subjects?abcdfdsafdfdksalfjdksalfjkdsajfkl93094382094840983210948093214809324823109443201804320943209194");
        System.out.println("MIME Encode(换行了)=[" + encodeStr + "]");

        System.out.println("\n===========================================");
        encodeStr = basicEncode64(
                "subjects?abcdfdsafdfdksalfjdksalfjkdsajfkl93094382094840983210948093214809324823109443201804320943209194");
        System.out.println("basic Encode(没换行)=[" + encodeStr + "]");

        System.out.println("\n##########################################################################\n");

        // 将图片转为文本的txt文件
        transImageToTxt("d:/307944.jpg", "d:/307944_2.txt");

        // 将文本的txt文件 ， 还原为图片二进制文件
        transTxtToImage("d:/307944_2.txt", "d:/307944_2.jpg");

        // 使用流的方式 - 将图片转为文本的txt文件
        transImageToTxtWrap("d:/307944.jpg", "d:/307944_3.txt");

        // 使用流的方式 - 将文本的txt文件 ， 还原为图片二进制文件
        transTxtToImageWrap("d:/307944_3.txt", "d:/307944_3.jpg");

        //transTxtToImageWrap("d:/img.txt", "d:/img_8.jpg");

    }
}
