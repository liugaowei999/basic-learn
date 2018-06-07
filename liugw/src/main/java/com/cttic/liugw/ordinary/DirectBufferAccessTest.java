package com.cttic.liugw.ordinary;

import java.nio.ByteBuffer;

public class DirectBufferAccessTest {

    // 直接内存读写操作
    public void directAccess() {
        long starttime = System.currentTimeMillis();
        ByteBuffer buffer = ByteBuffer.allocateDirect(500);
        for (int i = 0; i < 100000; i++) {
            for (int j = 0; j < 99; j++) {
                buffer.putInt(j);
            }
            buffer.flip();

            for (int j = 0; j < 99; j++) {
                buffer.getInt();
            }
            buffer.clear();
        }
        long endtime = System.currentTimeMillis();
        System.out.println("Test Direct Access:" + (endtime - starttime));
    }

    // 堆内存读写操作
    public void bufferAccess() {
        long starttime = System.currentTimeMillis();
        ByteBuffer buffer = ByteBuffer.allocate(500);
        for (int i = 0; i < 100000; i++) {
            for (int j = 0; j < 99; j++) {
                buffer.putInt(j);
            }
            buffer.flip();

            for (int j = 0; j < 99; j++) {
                buffer.getInt();
            }
            buffer.clear();
        }
        long endtime = System.currentTimeMillis();
        System.out.println("Test Buffer Access:" + (endtime - starttime));
    }

    // 直接内存分配测试
    public void directAllocate() {
        long starttime = System.currentTimeMillis();

        for (int i = 0; i < 100000; i++) {
            ByteBuffer buffer = ByteBuffer.allocateDirect(1000);
        }
        long endtime = System.currentTimeMillis();
        System.out.println("Test Direct Allocate:" + (endtime - starttime));
    }

    // 堆内存分配测试
    public void bufferAllocate() {
        long starttime = System.currentTimeMillis();

        for (int i = 0; i < 100000; i++) {
            ByteBuffer buffer = ByteBuffer.allocate(1000);
        }
        long endtime = System.currentTimeMillis();
        System.out.println("Test Buffer Allocate:" + (endtime - starttime));
    }

    public static void main(String[] args) {
        DirectBufferAccessTest directBufferAccessTest = new DirectBufferAccessTest();

        // 热身
        for (int i = 0; i < 10002; i++) {
            System.out.println("***********************************");
            directBufferAccessTest.directAccess();
            directBufferAccessTest.bufferAccess();

            directBufferAccessTest.bufferAllocate();
            directBufferAccessTest.directAllocate();
        }

        /**
         * 可以看出 直接内存读写 比 堆内存读写快 大约4倍
         * =====================================
         * Test Direct Access:15
         * Test Buffer Access:63
         */
        System.out.println("=====================================");
        directBufferAccessTest.directAccess();
        directBufferAccessTest.bufferAccess();

        /**
         * 可以看出堆内存的分配 比 直接内存的分配要快的多。 因此， 直接内存只适合于 申请次数较少，访问较频繁的场景。
         * 如果内存空间需要频繁申请， 并不适合使用直接内存。
         * %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
         * Test Direct Allocate:42
         * Test Buffer Allocate:17
         */
        System.out.println("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%");
        directBufferAccessTest.directAllocate();
        directBufferAccessTest.bufferAllocate();
    }
}
