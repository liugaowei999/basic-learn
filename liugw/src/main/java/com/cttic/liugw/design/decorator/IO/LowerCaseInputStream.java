package com.cttic.liugw.design.decorator.IO;

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * FilterInputStream 是 装饰者类， 和 流组件（例如FileInputStream, ObjectInputStream, ByteArrayInputStream)
 * 继承了共同的超类型 InputStream
 * @author liugaowei
 *
 */
public class LowerCaseInputStream extends FilterInputStream{

    protected LowerCaseInputStream(InputStream in) {
        super(in);
    }

    public int read() throws IOException{
        int c = super.read();
        return (c==-1 ? c : Character.toLowerCase((char)c));
    }
    
    public int read(byte[] b, int offset, int len) throws IOException{
        int result = super.read(b, offset, len);
        for(int i=offset; i<offset+result; i++){
            b[i] = (byte) Character.toLowerCase((char)b[i]);
        }
        return result;
    }

}
