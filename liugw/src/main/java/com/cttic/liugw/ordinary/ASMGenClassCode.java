package com.cttic.liugw.ordinary;

import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;

/**
 * 使用ASM包， 动态生成一个 如下的一个class：
    package com.agent.my3;  
    public class Tester  
    {  
        public void run()  
        {  
            System.out.println("This is my first ASM test");
        }  
    }  
 * 
 * @author liugaowei
 *
 */
public class ASMGenClassCode {

    /** 
     * 动态创建一个类，有一个无参数的构造函数 
     */
    public static ClassWriter createClassWriter(String classNameStr) {
        //注意，这里需要把classname里面的.改成/，如com.asm.Test改成com/asm/Test  
        String className = classNameStr.replace('.', '/');

        // COMPUTE_MAXS : 表示希望ASM自动计算最大局部变量表 和 最深操作数栈， 如果不指定， 需要自己计算。
        // COMPUTE_FRAMES : 表示需要ASM自动计算栈映射桢（此标记隐含COMPUTE_MAXS）
        ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_MAXS | ClassWriter.COMPUTE_FRAMES);
        //声明一个类，使用JDK1.8版本，public的类，父类是java.lang.Object，没有实现任何接口  
        cw.visit(Opcodes.V1_8, Opcodes.ACC_PUBLIC, className, null, "java/lang/Object", null);

        //生成一个无参的构造函数  【开始】
        MethodVisitor constructor = cw.visitMethod(Opcodes.ACC_PUBLIC, "<init>", "()V", null, null);
        // ALOAD 指令： 将指定的引用类型本地变量推送至栈顶
        // 
        constructor.visitVarInsn(Opcodes.ALOAD, 0);
        //执行父类的init初始化  
        constructor.visitMethodInsn(Opcodes.INVOKESPECIAL, "java/lang/Object", "<init>", "()V", false);
        //从当前方法返回void    
        constructor.visitInsn(Opcodes.RETURN);
        constructor.visitMaxs(1, 1);
        constructor.visitEnd();
        //生成一个无参的构造函数  【结束】
        return cw;
    }

    /** 
     * 创建一个run方法，里面只有一个输出 
     * public void run() 
     * { 
     *      System.out.println(message); 
     * } 
     * @return 
     * @throws Exception 
     */
    public static byte[] createVoidMethod(ClassWriter cw, String message) throws Exception {
        //注意，这里需要把classname里面的.改成/，如com.asm.Test改成com/asm/Test  
        //ClassWriter cw = createClassWriter(className.replace('.', '/'));

        //创建run方法  
        //()V表示函数，无参数，无返回值  
        MethodVisitor runMethod = cw.visitMethod(Opcodes.ACC_PUBLIC, "run", "()V", null, null);
        //先获取一个java.io.PrintStream对象  
        runMethod.visitFieldInsn(Opcodes.GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;");
        //将int, float或String型常量值从常量池中推送至栈顶  (此处将message字符串从常量池中推送至栈顶[输出的内容])  
        runMethod.visitLdcInsn(message);
        //执行println方法（执行的是参数为字符串，无返回值的println函数）  
        runMethod.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/io/PrintStream", "println", "(Ljava/lang/String;)V",
                false);
        runMethod.visitInsn(Opcodes.RETURN);
        runMethod.visitMaxs(1, 1);
        runMethod.visitEnd();

        return cw.toByteArray();
    }

    /** 
     * 创建一个返回Integer=10的函数 
     * public Integer getIntVal() 
     * { 
     *      return 10; 
     * } 
     * @return 
     * @throws Exception 
     */
    static byte[] createShortRetrurnMethod(ClassWriter cw, int returnValue) throws Exception {
        //创建get方法  
        //()Ljava/lang/Integer;表示函数，无参数，返回值为：java.lang.Integer，注意最后面的分号，没有就会报错  
        MethodVisitor getMethod = cw.visitMethod(Opcodes.ACC_PUBLIC, "getIntVal", "()Ljava/lang/Integer;", null, null);
        //BIPUSH:将单字节的常量值(-128~127)推送至栈顶(如果不是-128~127之间的数字，则不能用bipush指令)  
        //SIPUSH : 将一个短整型常量值（-32768-32767）推送至栈顶
        getMethod.visitIntInsn(Opcodes.SIPUSH, returnValue);
        //调用Integer的静态方法valueOf把10转换成Integer对象  
        String methodDesc = Type.getMethodDescriptor(Integer.class.getMethod("valueOf", int.class));
        getMethod.visitMethodInsn(Opcodes.INVOKESTATIC, Type.getInternalName(Integer.class), "valueOf", methodDesc,
                false);
        //从当前方法返回对象引用   
        getMethod.visitInsn(Opcodes.ARETURN);
        getMethod.visitMaxs(1, 1);
        getMethod.visitEnd();

        return cw.toByteArray();
    }

    /** 
     * 创建一个返回Integer=10的函数 
     * public Float getFloatVal() 
     * { 
     *      return 10; 
     * } 
     * @return 
     * @throws Exception 
     */
    static byte[] createFloatRetrurnMethod(ClassWriter cw, String returnValue) throws Exception {
        //创建get方法  
        //()Ljava/lang/Integer;表示函数，无参数，返回值为：java.lang.Integer，注意最后面的分号，没有就会报错  
        MethodVisitor getMethod = cw.visitMethod(Opcodes.ACC_PUBLIC, "getFloatVal", "()Ljava/lang/Float;", null, null);
        //BIPUSH:将单字节的常量值(-128~127)推送至栈顶(如果不是-128~127之间的数字，则不能用bipush指令)  
        //SIPUSH : 将一个短整型常量值（-32768-32767）推送至栈顶
        System.out.println("-============================ mark0");
        getMethod.visitLdcInsn(returnValue);
        System.out.println("-============================ mark1");
        //调用Integer的静态方法valueOf把10转换成Integer对象  
        String methodDesc = Type.getMethodDescriptor(Float.class.getMethod("valueOf", String.class));
        getMethod.visitMethodInsn(Opcodes.INVOKESTATIC, Type.getInternalName(Float.class), "valueOf", methodDesc,
                false);
        //从当前方法返回对象引用   
        getMethod.visitLdcInsn(Opcodes.LRETURN);
        getMethod.visitMaxs(1, 1);
        getMethod.visitEnd();
        System.out.println("-============================ mark3");

        return cw.toByteArray();
    }

    public static void main(String[] args) throws Exception {
        String className = "com.agent.my3.Tester";
        ClassWriter w = createClassWriter(className);
        createVoidMethod(w, "This is my first ASM test1 ^_^");
        byte[] classData = createShortRetrurnMethod(w, 1279);
        //        byte[] classData = createFloatRetrurnMethod(w, "1279.1");
        Class<?> clazz = new MyClassLoader().defineClassForName(className, classData);
        System.out.println("-============================ mark5");
        clazz.getMethods()[0].invoke(clazz.newInstance());
        System.out.println("-============================ mark6");

        Object value = clazz.getMethods()[1].invoke(clazz.newInstance());
        System.out.println(value);
        System.out.println("-============================ mark7");
    }
}

class MyClassLoader extends ClassLoader {
    public MyClassLoader() {
        super(Thread.currentThread().getContextClassLoader());
    }

    public Class<?> defineClassForName(String name, byte[] data) {
        return this.defineClass(name, data, 0, data.length);
    }
}
