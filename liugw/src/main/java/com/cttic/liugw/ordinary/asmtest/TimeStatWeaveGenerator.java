package com.cttic.liugw.ordinary.asmtest;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

public class TimeStatWeaveGenerator {
    /**
     * 1. 第一步先编译出 Account.class
     * 2. 使用本main方法将 Account.class 文件的字节码进行修改， 织入SecurityChecker的checkSecurity方法；
     *    将织入checkSecurity方法后的新类的字节码 覆盖 Account.class文件。便产生了带有 checkSecurity方法的Account类
     * 3. 使用 javap -verbose Account 可以看到Account.class字节码中已经织入了checkSecurity方法的调用
     *    cd E:\git\basic-learn\liugw\target\classes\com\cttic\liugw\ordinary\asmtest
     *    javap -verbose Account
     *    #13 = NameAndType        #12:#6         // start:()V
     *    #45 = NameAndType        #44:#6         // end:()V
     *    ........................
     *     public void operator();
            descriptor: ()V
            flags: ACC_PUBLIC
            Code:
              stack=2, locals=1, args_size=1
                 0: invokestatic  #14                 // Method com/cttic/liugw/ordinary/asmtest/TimeStat.start:()V
                 3: ifne          6
                 6: getstatic     #21                 // Field java/lang/System.out:Ljava/io/PrintStream;
                 9: ldc           #23                 // String 执行帐户操作.....1
                11: invokevirtual #29                 // Method java/io/PrintStream.println:(Ljava/lang/String;)V
                39: invokestatic  #46                // Method com/cttic/liugw/ordinary/asmtest/TimeStat.end:()V
                14: return
              StackMapTable: number_of_entries = 1
                frame_type = 6 // same 
     * @param args
     * @throws IOException
     */
    public static void reGenClazzClassFile(Class<?> clazz) throws IOException {
        String className = clazz.getName();

        String clazzClassFile = clazz.getResource("").getFile() + "Account";
        System.out.println("SecurityChecker class 文件位置：" + clazzClassFile);

        ClassReader classReader = new ClassReader(className);
        ClassWriter classWriter = new ClassWriter(ClassWriter.COMPUTE_MAXS | ClassWriter.COMPUTE_FRAMES);

        TimeStatClassAdapter classAdapter = new TimeStatClassAdapter(classWriter);
        classReader.accept(classAdapter, ClassReader.SKIP_DEBUG);

        byte[] data = classWriter.toByteArray();
        File file = new File(clazzClassFile + ".class");
        //File file = new File("D:/" + "Account.class");
        FileOutputStream fileOutputStream = new FileOutputStream(file);
        fileOutputStream.write(data);
        fileOutputStream.flush();
    }

    /**
     * 运行测试
     */
    public static void main(String[] args) throws IOException {
        // 执行一次即可， 反复执行 Account 字节码中会重复写入 checkSecurity 方法的调用
        reGenClazzClassFile(Account.class);

        // 执行测试
        Account account = new Account();
        account.operator();
    }

}

class TimeStatClassAdapter extends ClassVisitor {

    public TimeStatClassAdapter(ClassVisitor classVisitor) {
        super(Opcodes.ASM5, classVisitor);
        // TODO Auto-generated constructor stub
    }

    @Override
    public MethodVisitor visitMethod(final int access, final String name, final String desc, final String signature,
            final String[] exceptions) {
        System.out.println("Run visitMethod .......");
        MethodVisitor mv = cv.visitMethod(access, name, desc, signature, exceptions);
        MethodVisitor wrappedMv = mv;
        if (mv != null) {
            System.out.println("name=" + name);
            if (name.equals("operator")) {
                wrappedMv = new TimeStatMethodAdapter(mv);
            }
        }
        return wrappedMv;
    }

}

class TimeStatMethodAdapter extends MethodVisitor implements Opcodes {
    static String timeStatClassName = TimeStat.class.getName();

    public TimeStatMethodAdapter(MethodVisitor classVisitor) {
        super(Opcodes.ASM5, classVisitor);
        // TODO Auto-generated constructor stub
    }

    /**
     * Code 属性被访问时，调用 start 方法。 即方法的开始执行 TimeStat类的 start() 静态类方法。
     */
    @Override
    public void visitCode() {
        //        Label continueLabel = new Label();
        visitMethodInsn(Opcodes.INVOKESTATIC, timeStatClassName.replaceAll("\\.", "/"), "start", "()V");
        //        visitJumpInsn(Opcodes.IFNE, continueLabel);
        //        visitLabel(continueLabel);
        super.visitCode();
    }

    /**
     * 当访问到XRetrun指令时，进行TimeStat.end()方法的调用， 表示方法即将退出。
     * xreturn 指令是从 ireturn (172) 到 return(177) 这个范围内。
     * ireturn = 172
     * lreturn = 173
     * freturn = 174
     * dreturn = 175
     * areturn = 176
     * return  = 177
     */
    @Override
    public void visitInsn(int opCode) {
        // 下一条指令是Return，则在Return指令前 加入 end方法的调用指令。
        if (opCode >= IRETURN && opCode <= RETURN) {
            visitMethodInsn(Opcodes.INVOKESTATIC, timeStatClassName.replaceAll("\\.", "/"), "end", "()V");
        }
        mv.visitInsn(opCode);
    }

}
