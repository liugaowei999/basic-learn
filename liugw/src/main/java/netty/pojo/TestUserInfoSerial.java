package netty.pojo;

import java.io.ByteArrayOutputStream;
import java.io.ObjectOutputStream;
import java.nio.ByteBuffer;

public class TestUserInfoSerial {

    public static void TestSerial() throws Exception {
        UserInfo userInfo = new UserInfo();
        userInfo.buildUserName("Welcome to Netty").buildUserID(100);
        
        // 开始原生序列号
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
        objectOutputStream.writeObject(userInfo);
        objectOutputStream.flush();
        objectOutputStream.close();
        
        // 获取原生序列号的结果长度
        byte[] bs = byteArrayOutputStream.toByteArray();
        byteArrayOutputStream.close();
        System.out.println("The JDK serializable lenght is :" + bs.length);
        
        System.out.println("======================================================");
        
        // 自己重写的序列号结果长度
        int length = userInfo.codeC().length;
        System.out.println("The byte array serializable length is :" + length);
        
        int loop = 1000000;
        ByteArrayOutputStream byteArrayOutputStream2 = null;
        ObjectOutputStream objectOutputStream2 = null;
        long startTime = System.currentTimeMillis();
        for (int i=0; i<loop; i++) {
            byteArrayOutputStream2 = new ByteArrayOutputStream();
            objectOutputStream2 = new ObjectOutputStream(byteArrayOutputStream2);
            objectOutputStream2.writeObject(userInfo);
            objectOutputStream2.flush();
            objectOutputStream2.close();
            byte[] bs2 = byteArrayOutputStream2.toByteArray();
            byteArrayOutputStream2.close();
        }
        long endTime = System.currentTimeMillis();
        System.out.println("JDK cost time                          :" + (endTime - startTime) + "ms");
        
        startTime = System.currentTimeMillis();
        for (int i=0; i<loop; i++) {
            byte[] bs2 = userInfo.codeC();
        }
        endTime = System.currentTimeMillis();
        System.out.println("byte array[create every time] cost time:" + (endTime - startTime) + "ms");
        
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        startTime = System.currentTimeMillis();
        for (int i=0; i<loop; i++) {
            byte[] bs2 = userInfo.codeC2(buffer);
        }
        endTime = System.currentTimeMillis();
        System.out.println("byte array[one create time] cost time  :" + (endTime - startTime) + "ms");
    }
    
    public static void main(String[] args) throws Exception {
        TestUserInfoSerial.TestSerial();
    }
}
