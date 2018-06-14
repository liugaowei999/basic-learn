package com.cttic.liugw.ordinary.dynamic;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.invoke.CallSite;
import java.lang.invoke.MethodHandles.Lookup;
import java.lang.invoke.MethodType;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Handle;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

/**
 * 使用ASM ， 构造一个使用invokedynamic指令的类
 * DynBootStrap.bootstrap 为其引导方法
 * 
 * static CallSite bootstrap(Lookup lookup, String name, MethodType type, Object value)
 * @author liugaowei
 *
 */
public class DynInvokeSample extends ClassLoader {
    public DynInvokeSample() {
        super(Thread.currentThread().getContextClassLoader());
    }

    //    @Override
    //    public Class<?> defineClass(String name, byte[] data) {
    //        return this.defineClass(name, data, 0, data.length);
    //    }

    //@Override 不需要重写 findClass ， 使用父类的加载机制即可
    protected Class<?> findClass1(String name) throws ClassNotFoundException {
        System.out.println("要加载的类名：" + name);
        //name = "DynInvokeSampleMain";
        // 先去加载器里面看看已经加载到的类中是否有这个类  
        Class<?> c = findLoadedClass(name); // loader.loadClass("com.zm.HelloWorld");  

        //应该要先查询有没有加载过这个类。如果已经加载，则直接返回加载好的类。如果没有，则加载新的类。  
        if (c != null) {
            System.out.println("已经加载了这个类");
            return c;
        } else {
            ClassLoader parent = this.getParent();
            try {
                c = parent.loadClass(name); //委派给父类加载  调用JVM委托机制来加载，如果一层层上抛都没有加载到com.zm.HelloWorld的话 则走到46行 调用自己的类加载  
            } catch (Exception e) {
                //              e.printStackTrace();  
            }

            if (c != null) {
                return c;
            } else {//   
                byte[] classData = getClassData(name);
                if (classData == null) {
                    throw new ClassNotFoundException();
                } else {
                    // 将字节码处理成 Class实例返回  
                    c = defineClass(name, classData, 0, classData.length);
                }
            }

        }

        return c;
    }

    // 读取流 将读到的流处理成字节码数组返回  
    private byte[] getClassData(String classname) {
        String path = "d:/" + classname.replace('.', '/') + ".class";
        if (classname.contains("..")) {
            path = "d:/DynInvokeSampleMain.class";
        }
        System.out.println("要加载的类：" + path);

        InputStream is = null;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            is = new FileInputStream(path);

            byte[] buffer = new byte[1024];
            int temp = 0;
            while ((temp = is.read(buffer)) != -1) {
                baos.write(buffer, 0, temp);
            }

            return baos.toByteArray();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            try {
                if (is != null) {
                    is.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                if (baos != null) {
                    baos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @SuppressWarnings("deprecation")
    // Handle(int tag, String owner, String name, String descriptor
    // Opcodes.H_INVOKESTATIC
    // replaceAll 第一个参数为正则表达式， 如果要替换"."为"/" , 应该写为：replaceAll("\\.", "/")) ， 而不是 replaceAll(".", "/"));
    public static final Handle BSM = new Handle(Opcodes.H_INVOKESTATIC,
            DynBootStrap.class.getName().replace(".", "/"),
            "bootstrap",
            MethodType.methodType(CallSite.class, Lookup.class, String.class, MethodType.class, Object.class)
                    .toMethodDescriptorString());

    @SuppressWarnings("deprecation")
    public Class createClass(String className, String testStr) throws IOException {
        ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_FRAMES | ClassWriter.COMPUTE_MAXS);
        /**
         * visit(int version, int access, String name, String signature, String superName, String[] interfaces)
         * visite the header of the class
         * 创建类， 类名为DynInvokeSampleMain
         */
        cw.visit(Opcodes.V1_7, Opcodes.ACC_PUBLIC | Opcodes.ACC_SUPER,
                className, null, "java/lang/Object",
                null);

        // 创建构造函数
        MethodVisitor mvConstuctor = cw.visitMethod(Opcodes.ACC_PUBLIC, "<init>", "()V", null, null);
        mvConstuctor.visitVarInsn(Opcodes.ALOAD, 0);
        mvConstuctor.visitMethodInsn(Opcodes.INVOKESPECIAL, "java/lang/Object", "<init>", "()V");

        // 增加调试信息， 构造函数中打印一段字符串
        mvConstuctor.visitFieldInsn(Opcodes.GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;");
        mvConstuctor.visitLdcInsn("开始执行构造函数， 建立对象实例.");
        mvConstuctor.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/io/PrintStream", "println", "(Ljava/lang/String;)V");

        mvConstuctor.visitInsn(Opcodes.RETURN);
        mvConstuctor.visitMaxs(1, 1); // 访问该方法的最大栈大小和局部变量的最大数目
        mvConstuctor.visitEnd();

        // 创建普通的静态方法 run()
        MethodVisitor mv = cw.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_STATIC, "run", "(Ljava/lang/String;)V", null,
                null);

        // 开始方法代码的访问，如果有的话（即非抽象方法）。
        mv.visitCode();
        /**
         * 访问字段指令。字段指令是加载或存储对象字段值的指令。--- 加载out对象
         * 注意： 字段的类型， 必须得以 分号结束， 例如："Ljava/io/PrintStream;" 不能是 "Ljava/io/PrintStream"
         * 分号表示全限定名结束。
         */
        mv.visitFieldInsn(Opcodes.GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;");
        /**
         * 访问 invokedynamic 指令
         * 
         * visitInvokeDynamicInsn(String name, String descriptor, Handle bootstrapMethodHandle, Object... bootstrapMethodArguments)
         * Visits an invokedynamic instruction.
         * Parameters:
         *      name : 方法的名称.
         *      descriptor ：方法的签名（方法类型描述符）.
         *      bootstrapMethodHandle ： 引导方法（the bootstrap method）.
         *      bootstrapMethodArguments ： 传给引导方法的参数列表（the bootstrap method constant arguments）. 
         * Each argument must be an Integer, Float, Long, Double, String, Type or Handle value. 
         * This method is allowed to modify the content of the array so a caller should 
         * expect that this array may change
         */
        mv.visitInvokeDynamicInsn("hashCode", "()I", BSM, testStr);// 访问 invokedynamic 指令
        /**
         * Visits a method instruction. A method instruction is an instruction that invokes a method.
         * 访问方法指令， 方法指令是一个执行方法的指令。
         */
        mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/io/PrintStream", "println", "(I)V");
        mv.visitInsn(Opcodes.RETURN);// (访问一个零操作数指令,不需要参数的指令)Visits a zero operand instruction.
        /**
         * 访问该方法的最大栈大小和局部变量的最大数目
         * Visits the maximum stack size and the maximum number of local variables of the method.
         * Parameters:
         *      maxStack maximum stack size of the method.
         *      maxLocals maximum number of local variables for the method.
         */
        mv.visitMaxs(0, 0);
        /**
         * Visits the end of the method. 
         * This method, which is the last one to be called, is used to inform the visitor that 
         * all the annotations and attributes of the method have been visited.
         * 访问方法的末尾。该方法是最后调用的方法，用于通知访问者，该方法的所有注释和属性都已被访问。
         */
        mv.visitEnd();
        /**
         * Visits the end of the class. 
         * This method, which is the last one to be called, is used to inform the visitor 
         * that all the fields and methods of the class have been visited.
         * 该方法是最后一个被调用的方法，用于通知访问者访问了该类的所有字段和方法。
         */
        cw.visitEnd();

        // 获取上面构造的 DynInvokeSampleMain 类的 字节码数据
        byte[] bytes = cw.toByteArray();

        // 写出到磁盘(也可以不写， 写出方便使用javap -verbose 检查，或者使用反编译工具）
        //        FileOutputStream fileOutputStream = new FileOutputStream(new File("d:/DynInvokeSampleMain.class"));
        //        fileOutputStream.write(bytes);
        //        fileOutputStream.flush();
        //        fileOutputStream.close();

        return this.defineClass(className, bytes, 0, bytes.length);
    }

    public static void main(String[] args) throws InstantiationException, IllegalAccessException, IOException,
            IllegalArgumentException, InvocationTargetException, SecurityException, NoSuchMethodException {

        String testStr = "Hello world! good morning!";
        DynInvokeSample dynInvokeSample = new DynInvokeSample();
        //        System.out.println("Start Create Class ");
        String className = "DynInvokeSampleMain"; // com/cttic/liugw/ordinary/dynamic/
        Class<?> clazz = dynInvokeSample.createClass(className, testStr);
        //        System.out.println("Create Class finish.");
        Object newInstance = clazz.newInstance();
        //        System.out.println("Create newInstance finish." + newInstance);
        //
        //        System.out.println("getClassLoader=" + newInstance.getClass().getClassLoader());
        //        System.out.println("DynInvokeSample getClassLoader=" + DynInvokeSample.class.getClassLoader());
        // 使用反射调用 run 方法
        Method method = newInstance.getClass().getMethod("run", String.class);
        //        System.out.println(method.getName());
        //        System.out.println("class name:" + method.getDeclaringClass().getName());

        method.invoke(newInstance, "DynInvokeSampleMain");
        // 验证结果的正确性
        System.out.println(testStr.hashCode());

        //DynInvokeSample.class.getMethod("test1").invoke(dynInvokeSample);

        /**
         * 执行输出结果：
         * 开始执行构造函数， 建立对象实例.
            bootstrap called, name=hashCode
            1295442941
            1295442941
         */

    }

    public void test1() {
        System.out.println("test1 ..........");
    }
}
