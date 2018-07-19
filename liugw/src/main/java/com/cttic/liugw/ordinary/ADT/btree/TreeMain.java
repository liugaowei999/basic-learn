package com.cttic.liugw.ordinary.ADT.btree;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class TreeMain {
    public static void main(String[] args) throws IOException {
        int value;
        Tree theTree = new Tree();
        theTree.insert(new Data(50, 1.5));
        theTree.insert(new Data(25, 1.2));
        theTree.insert(new Data(75, 1.7));
        theTree.insert(new Data(12, 1.2));
        theTree.insert(new Data(37, 1.5));
        theTree.insert(new Data(43, 1.5));

        theTree.displayTree();

        while (true) {
            System.out.println("请输入要执行的操作：[i:增加 , f:查找 d:删除 t: 遍历 s: 展示整棵树]");
            int choice = getChar();
            switch (choice) {
            case 's':
                theTree.displayTree();
                break;
            case 'i':
                System.out.println("请输入要插入的值：");
                value = getInt();
                theTree.insert(new Data(value, value + 0.9));
                theTree.displayTree();
                break;
            case 'f':
                System.out.println("请输入要查找的值：");
                value = getInt();
                Node found = theTree.find(new Data(value, value + 0.9));
                if (found != null) {
                    System.out.print("Found:");
                    found.display();
                    System.out.println();
                } else {
                    System.out.println("没有找到:[" + value + "]");
                }
                break;
            case 'd':
                System.out.println("请输入要删除的值：");
                value = getInt();
                boolean isDelete = theTree.delete(new Data(value, value + 0.9));
                if (isDelete) {
                    theTree.displayTree();
                    System.out.println("删除：[" + value + "] 成功!");
                } else {
                    System.out.println("删除：[" + value + "] 失败!");
                }
                break;
            case 't':
                System.out.println("请输入要遍历的方式：1:前序 2：中序 3：后序");
                value = getInt();
                theTree.traverse(value);
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
