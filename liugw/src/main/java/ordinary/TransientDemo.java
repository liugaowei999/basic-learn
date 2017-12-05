package ordinary;

import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InvalidClassException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class TransientDemo {
	public static void main(String[] args) throws IOException 
	{
//        if (args.length != 1) 
//        {
//            System.err.println("usage: java TransDemo classfile");
//            return;
//        }
        String classfile = "D:/web_dev/hello-world/target/classes/com/liugw/learn/helloworld/HelloWorld.class";
        ClassLib cl = new ClassLib(new FileInputStream(classfile));
        System.out.printf("Minor version number: %d%n", cl.getMinorVer());
        System.out.printf("Major version number: %d%n", cl.getMajorVer());
        cl.showIS();
 
        try (FileOutputStream fos = new FileOutputStream("d:/HelloWorld.ser2");
             ObjectOutputStream oos = new ObjectOutputStream(fos)
            ) 
        {
            oos.writeObject(cl);
        }
 
        cl = null;
 
        try (FileInputStream fis = new FileInputStream("d:/HelloWorld.ser1"); // serialVersionUID = 2L
             ObjectInputStream ois = new ObjectInputStream(fis)) 
        {
            System.out.println();
            cl = (ClassLib) ois.readObject();
            System.out.printf("Minor version number: %d%n", cl.getMinorVer());
            System.out.printf("Major version number: %d%n", cl.getMajorVer());
            cl.showIS();
        } catch (ClassNotFoundException cnfe) 
        {
            System.err.println(cnfe.getMessage());
        } catch (InvalidClassException invalidClassException)
        {
        	System.out.println("要反序列化的文件 与 class定义的 serialVersionUID 不一致");
        	System.err.println(invalidClassException.getMessage());
        }
    }
}

class ClassLib implements Serializable 
{
    /**
	 * 
	 */
//	private static final long serialVersionUID = 1839194775749110060L;

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L; // 2L

	private transient InputStream is;
 
    private int majorVer;
    private int minorVer;
 
    ClassLib(InputStream is) throws IOException 
    {
        System.out.println("ClassLib(InputStream) called");
        this.is = is;
        DataInputStream dis;
        if (is instanceof DataInputStream){
            dis = (DataInputStream) is;
        }else{
            dis = new DataInputStream(is);
        }
        // 读取魔数(magic) 4个字节
        if (dis.readInt() != 0xcafebabe){
            throw new IOException("not a .class file");
        }
        
        // 读取class文件的版本号，支持的JDK最小版本号（2字节）【JDK版本号是从45开始的】
        minorVer = dis.readShort();
        // 读取class文件的版本号，支持的JDK最大版本号（2字节）
        majorVer = dis.readShort();
    }
 
    int getMajorVer() {
        return majorVer;
    }
 
    int getMinorVer() {
        return minorVer;
    }
 
    void showIS() {
        System.out.println(is);
    }
}