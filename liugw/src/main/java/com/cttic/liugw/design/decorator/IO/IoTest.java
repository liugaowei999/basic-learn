package com.cttic.liugw.design.decorator.IO;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

public class IoTest {

    public static void main(String[] args) {
        int c = -1;
        try {
            InputStream in = new LowerCaseInputStream(
                                            new BufferedInputStream(new FileInputStream("d://zk_stack.txt"))
                                      );
            while ((c=in.read()) >=0 ){
                System.out.print((char)c);
            }
            in.close();
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
