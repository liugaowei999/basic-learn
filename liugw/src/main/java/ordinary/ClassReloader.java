package ordinary;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.junit.experimental.theories.Theories;

public class ClassReloader extends ClassLoader{

    private String classPath;
    String className = "ordinary.AA";
    
    public ClassReloader(String classPath) {
        this.classPath = classPath;
    }
    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        byte[] classData = getData(name);
        if (null == classData) {
            throw new ClassNotFoundException();
        } else {
            return defineClass(className, classData, 0, classData.length);
        }
    }
    
    private byte[] getData(String className) {
        String path = classPath + className;
        try {
            @SuppressWarnings("resource")
            InputStream inputStream = new FileInputStream(path);
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            
            byte[] buffer = new byte[2048];
            int num = 0;
            while ((num = inputStream.read(buffer)) !=-1 ) {
                stream.write(buffer,0,num);
            }
            return stream.toByteArray();
        }catch(IOException ioe) {
            ioe.printStackTrace();
        }
        return null;
    }
    
    public static void main(String[] args) {
        try {
            String path = "D:\\work\\workspace\\liugw\\target\\classes\\ordinary\\";
//            String path = "D:\\work\\workspace\\liugw\\target\\classes\\";
            ClassReloader reloader = new ClassReloader(path);
            Class rClass = reloader.findClass("AA.class");
            System.out.println(rClass.getName() + "--| " + rClass.newInstance());
            System.out.println(rClass.getName() + "--| " + rClass.newInstance());
            
            ClassReloader reloader2 = new ClassReloader(path);
            Class<?> rClass2 = reloader2.findClass("AA.class");
            Object aa = rClass2.newInstance();
            Object aa2 = rClass2.newInstance();
            System.out.println(rClass2.getName() + "--| " + aa);
            System.out.println( aa instanceof ordinary.AA) ;
            
            AA bb = new AA();
            System.out.println( bb instanceof ordinary.AA) ;
            
            System.out.println("rClass.getClassLoader()              === " + rClass.getClassLoader());
            System.out.println("rClass2.getClassLoader()             === " + rClass2.getClassLoader());
            System.out.println("aa.getClass().getClassLoader()       === " + aa.getClass().getClassLoader());
            System.out.println("aa2.getClass().getClassLoader()      === " + aa2.getClass().getClassLoader());
            System.out.println("bb.getClass().getClassLoader()       === " + bb.getClass().getClassLoader());
            System.out.println("reloader.getClass().getClassLoader() === " + reloader.getClass().getClassLoader());
            System.out.println(aa.getClass());
            
        } catch (ClassNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } 
    }
}
