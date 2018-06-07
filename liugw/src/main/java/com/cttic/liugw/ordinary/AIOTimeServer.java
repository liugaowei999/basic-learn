package com.cttic.liugw.ordinary;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.util.Date;
import java.util.concurrent.CountDownLatch;

/**
 * CompletionHandler接口的实现类作为操作完成的回调
 * 本例中创建了：
 * AcceptCompletionHandler
 * ReadCompletionHandler
 * 两个实现类，分别循环回调用来处理 接收新的客户端连接  和  读取客户端的数据
 * 写数据的回调Handler没有单独定义类。
 * @author liugaowei
 *
 */
public class AIOTimeServer {
    
    static class AsyncTimeServerHandler implements Runnable {
        private int port;
        CountDownLatch latch;
        // 异步的 ServerSocketChannel
        AsynchronousServerSocketChannel asynchronousServerSocketChannel;
//        AsynchronousServerSocketChannel asynchronousServerSocketChannel1;
        
        
        public AsyncTimeServerHandler(int port) {
            this.port = port;
            try {
                // Opens an asynchronous server-socket channel. 
                // 创建一个异步的服务端通道asynchronousServerSocketChannel，并bind端口
                asynchronousServerSocketChannel = AsynchronousServerSocketChannel.open();
                asynchronousServerSocketChannel.bind(new InetSocketAddress(port));
//                asynchronousServerSocketChannel1 = AsynchronousServerSocketChannel.open();
//                asynchronousServerSocketChannel1.bind(new InetSocketAddress(80));
//                asynchronousServerSocketChannel.bind(new InetSocketAddress(port), 10);
                System.out.println("The TIME-SEVER start in port:" + port);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void run() {
            // 为测试而用。 因为是异步操作， 不会阻塞， 为了防止服务端执行完成后退出， 使用CountDownLatch进行阻塞。
            latch = new CountDownLatch(1);
            doAccept();
            try {
                latch.await();
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

        private void doAccept() {
            // accept(A attachment, CompletionHandler<AsynchronousSocketChannel, ? super A> handler)
            System.out.println("1asynchronousServerSocketChannel=" + asynchronousServerSocketChannel.hashCode());
//            System.out.println("asynchronousServerSocketChannel1=" + asynchronousServerSocketChannel1.hashCode());
            asynchronousServerSocketChannel.accept(this, new AcceptCompletionHandler());
        }
        
    }
    
    static class AcceptCompletionHandler implements 
                    CompletionHandler<AsynchronousSocketChannel, AsyncTimeServerHandler>{

        @Override
        public void completed(AsynchronousSocketChannel result, AsyncTimeServerHandler attachment) {
            System.out.println("2asynchronousServerSocketChannel=" + attachment.asynchronousServerSocketChannel.hashCode());
            attachment.asynchronousServerSocketChannel.accept(attachment, this);
            ByteBuffer buffer = ByteBuffer.allocate(1024);
            // read(ByteBuffer, A, CompletionHandler<Integer,? super A>)
            result.read(buffer,buffer,new ReadCompletionHandler(result));
        }

        @Override
        public void failed(Throwable exc, AsyncTimeServerHandler attachment) {
            // TODO Auto-generated method stub
            exc.printStackTrace();
            attachment.latch.countDown();
        }
    }
    
    static class ReadCompletionHandler implements CompletionHandler<Integer, ByteBuffer> {
        private AsynchronousSocketChannel channel;
        private final String COMMAND = "QUERY_TIME_ORDER";
        
        public ReadCompletionHandler(AsynchronousSocketChannel channel) {
            if (this.channel == null) {
                this.channel = channel;
            }
        }

        @Override
        public void completed(Integer result, ByteBuffer attachment) {
            attachment.flip();
            byte[] body = new byte[attachment.remaining()];
            attachment.get(body);
            try {
                String request = new String(body, "UTF-8");
                System.out.println("receive order:" + request);
                String currentTime = COMMAND.equalsIgnoreCase(request)?new Date().toString():"BAD COMMAND";
                doWrite(currentTime);
            } catch (UnsupportedEncodingException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

        private void doWrite(String currentTime) {
            if (currentTime != null && currentTime.length() > 0) {
                byte[] bytes = currentTime.getBytes();
                ByteBuffer writeBuffer = ByteBuffer.allocate(bytes.length);
                writeBuffer.put(bytes);
                writeBuffer.flip();
                channel.write(writeBuffer, writeBuffer, new CompletionHandler<Integer, ByteBuffer>() {
                    @Override
                    public void completed(Integer result, ByteBuffer buffer) {
                        // 如果没有发送完成继续发送
                        if (buffer.hasRemaining()) {
                            channel.write(buffer, buffer, this);
                        }
                    }

                    @Override
                    public void failed(Throwable exc, ByteBuffer attachment) {
                        try {
                            channel.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });
                
            }
        }

        @Override
        public void failed(Throwable exc, ByteBuffer attachment) {
            try {
                this.channel.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        
    }

    public static void main(String[] args) {
        int port = 8080;
        if (args != null && args.length > 0) {
            try {
                port = Integer.valueOf(args[0]);
            } catch (NumberFormatException e) {
                // 使用默认值
                System.out.println(e.toString() + ", [port] will be use the default value:" + port);
            }
        }
        
        // 实际项目中不需要启动 独立的线程来启动 AsynchronousServerSocketChannel
        AsyncTimeServerHandler asyncTimeServerHandler = new AsyncTimeServerHandler(port);
        new Thread(asyncTimeServerHandler,"ASYN-TIME-SEVER-THREAD").start();
    }
}
