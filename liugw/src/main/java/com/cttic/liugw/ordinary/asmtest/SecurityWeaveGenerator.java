package com.cttic.liugw.ordinary.asmtest;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

/**
 * 使用ASM将安全检查操作 织入 帐户操作中
 * 只有满足安全要求时， 才执行帐户操作。
 * 
 * @author liugaowei
 *
 */
public class SecurityWeaveGenerator {

    /**
     * 1. 第一步先编译出 Account.class
     * 2. 使用本main方法将 Account.class 文件的字节码进行修改， 织入SecurityChecker的checkSecurity方法；
     *    将织入checkSecurity方法后的新类的字节码 覆盖 Account.class文件。便产生了带有 checkSecurity方法的Account类
     * 3. 使用 javap -verbose Account 可以看到Account.class字节码中已经织入了checkSecurity方法的调用
     *    cd E:\git\basic-learn\liugw\target\classes\com\cttic\liugw\ordinary\asmtest
     *    javap -verbose Account
     *    ........................
     *   #15 = Methodref          #11.#14        // com/cttic/liugw/ordinary/asmtest/SecurityChecker.checkSecurity:()Z
     *     public void operator();
            descriptor: ()V
            flags: ACC_PUBLIC
            Code:
              stack=2, locals=1, args_size=1
                 0: invokestatic  #15                 // Method com/cttic/liugw/ordinary/asmtest/SecurityChecker.checkSecurity:()Z
                 3: ifne          6
                 6: getstatic     #21                 // Field java/lang/System.out:Ljava/io/PrintStream;
                 9: ldc           #23                 // String 执行帐户操作.....1
                11: invokevirtual #29                 // Method java/io/PrintStream.println:(Ljava/lang/String;)V
                14: return
              StackMapTable: number_of_entries = 1
                frame_type = 6 // same 
     * @param args
     * @throws IOException
     */
    public static void reGenAccountClass() throws IOException {
        String className = Account.class.getName();

        String AccountClassFile = Account.class.getResource("").getFile() + "Account";
        System.out.println("SecurityChecker class 文件位置：" + AccountClassFile);

        ClassReader classReader = new ClassReader(className);
        ClassWriter classWriter = new ClassWriter(ClassWriter.COMPUTE_MAXS | ClassWriter.COMPUTE_FRAMES);

        AddSecurityCheckClassAdapter classAdapter = new AddSecurityCheckClassAdapter(classWriter);
        classReader.accept(classAdapter, ClassReader.SKIP_DEBUG);

        byte[] data = classWriter.toByteArray();
        File file = new File(AccountClassFile + ".class");
        //File file = new File("D:/" + "Account.class");
        FileOutputStream fileOutputStream = new FileOutputStream(file);
        fileOutputStream.write(data);
        fileOutputStream.flush();
    }

    public static void main(String[] args) throws IOException {
        // 执行一次即可， 反复执行 Account 字节码中会重复写入 checkSecurity 方法的调用
        //        reGenAccountClass();

        // 执行测试
        Account account = new Account();
        account.operator();
    }
}

class AddSecurityCheckClassAdapter extends ClassVisitor {

    public AddSecurityCheckClassAdapter(ClassVisitor classVisitor) {
        super(Opcodes.ASM4, classVisitor);
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
                wrappedMv = new AddSecurityCheckMethodAdapter(mv);
            }
        }
        return wrappedMv;
    }

}

class AddSecurityCheckMethodAdapter extends MethodVisitor {
    static String securityCheckerClassName = SecurityChecker.class.getName();

    public AddSecurityCheckMethodAdapter(MethodVisitor classVisitor) {
        super(Opcodes.ASM4, classVisitor);
        // TODO Auto-generated constructor stub
    }

    @Override
    public void visitCode() {
        //        System.out.println(SecurityChecker.class.getResource("").getFile());
        //        System.out.println(Thread.currentThread().getContextClassLoader().getResource("").getFile());
        //        // 获取SecurityChecker类的class 位置， 从SecurityChecker.class 字节码中获取checkSecurity方法的定义
        //        String SecurityCheckerClassFile = SecurityChecker.class.getResource("").getFile() + "/SecurityChecker";
        //        System.out.println("SecurityChecker class 文件位置：" + SecurityCheckerClassFile);

        Label continueLabel = new Label();
        visitMethodInsn(Opcodes.INVOKESTATIC, securityCheckerClassName.replaceAll("\\.", "/"), "checkSecurity",
                "()Z");
        visitJumpInsn(Opcodes.IFNE, continueLabel);
        visitInsn(Opcodes.RETURN);
        visitLabel(continueLabel);
        super.visitCode();

    }

}
