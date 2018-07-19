package com.cttic.liugw.ordinary.ADT.tree234;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Random;

public class Tree234Main {

    public static void main(String[] args) throws IOException {
        Tree234 tree234 = new Tree234();

        tree234.insert(new DataItem(50));
        tree234.insert(new DataItem(40));
        tree234.insert(new DataItem(60));
        tree234.insert(new DataItem(30));
        tree234.insert(new DataItem(70));
        tree234.insert(new DataItem(20));
        tree234.insert(new DataItem(71));
        tree234.insert(new DataItem(72));
        tree234.insert(new DataItem(73));
        tree234.insert(new DataItem(74));

        Random random = new Random(System.currentTimeMillis());
        //        for (int i = 0; i < 30; i++) {
        //            tree234.insert(new DataItem(random.nextInt(1000)));
        //        }

        tree234.displayTree();

        int value;
        while (true) {
            System.out.println("\n请输入要执行的操作：[i:增加 , f:查找  s: 展示整棵树]");
            int choice = getChar();
            switch (choice) {
            case 's':
                tree234.displayTree();
                break;
            case 'i':
                System.out.println("请输入要插入的值：");
                value = getInt();
                tree234.insert(new DataItem(value));
                tree234.displayTree();
                break;
            case 'f':
                System.out.println("请输入要查找的值：");
                value = getInt();
                int found = tree234.find(new DataItem(value));
                if (found != -1) {
                    System.out.print("Found:");
                    System.out.println();
                } else {
                    System.out.println("没有找到:[" + value + "]");
                }
                break;

            default:
                System.out.println("不支持该命令:" + choice);
                break;
            }
        }
    }

    private static int getChar() throws IOException {
        String string = getString();
        return string.charAt(0);
    }

    private static int getInt() throws IOException {
        String string = getString();
        return Integer.valueOf(string);
    }

    private static String getString() throws IOException {
        InputStreamReader inputStreamReader = new InputStreamReader(System.in);
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
        String string = bufferedReader.readLine();
        return string;
    }
}
