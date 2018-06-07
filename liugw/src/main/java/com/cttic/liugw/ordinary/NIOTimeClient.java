package com.cttic.liugw.ordinary;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.CountDownLatch;


public class NIOTimeClient {
    static CountDownLatch countDownLatch = new CountDownLatch(1);
    
    static class TimeClientHandle implements Runnable {
        private String host;
        private int port;
        private Selector selector;
        private SocketChannel socketChannel;
        private volatile boolean stop;

        public TimeClientHandle(String host, int port) {
            this.host = (host==null ? "localhost" : host);
            this.port = port;
            try {
                selector = Selector.open();
                socketChannel = SocketChannel.open();
                socketChannel.configureBlocking(false);
            } catch (IOException e) {
                e.printStackTrace();
                System.exit(-1);
            }
        }

        @Override
        public void run() {
            try {
                countDownLatch.await();
            } catch (InterruptedException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
            try {
                doConnect();
            } catch (Exception e) {
                System.out.println("Connect failed!");
                e.printStackTrace();
                System.exit(-1);
            }
            
            while (!stop) {
                try {
//                    selector.select(1000);
                    selector.select();
                    Set<SelectionKey> selectionKeys = selector.selectedKeys();
//                    System.out.println(selectionKeys.size());
                    Iterator<SelectionKey> iterator = selectionKeys.iterator();
                    SelectionKey key = null;
                    while (iterator.hasNext()) {
                        key = iterator.next();
                        iterator.remove();
                        try {
                            handleInput(key);
                        } catch (Exception e) {
                            e.printStackTrace();
                            System.out.println("Exception happend!");
                            if (key != null) {
                                key.cancel();
                                if (key.channel() != null) {
                                    key.channel().close();
                                }
                            }
                        }
                    }
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
            
            if (selector != null) {
                try {
                    selector.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }

        private void handleInput(SelectionKey key) throws Exception {
            if (key.isValid()) {
                SocketChannel channel = (SocketChannel) key.channel();
                if (key.isConnectable()) {
                    /**
                     * If this channel is already connected then this method will not block and will 
                     * immediately return true. 
                     * If this channel is in non-blocking mode then this method will return false 
                     * if the connection process is not yet complete. 
                     * If this channel is in blocking mode then this method will block until the connection 
                     * either completes or fails, and will always either return true or throw a checked 
                     * exception describing the failure
                     */
                    try {
                        if (channel.finishConnect()) {
                            socketChannel.register(selector, SelectionKey.OP_READ);
                            doWrite(channel);
                        } else {
                            System.out.println("Connect to server failed!");
                            System.exit(-1);
                        }
                    } catch (Exception e) {
                        System.out.println("Connect to server failed!");
                        e.printStackTrace();
                        System.exit(-1);
                    }
                }
                
                if (key.isReadable()) {
                    ByteBuffer readBuffer = ByteBuffer.allocate(1024);
                    int readbytes = channel.read(readBuffer);
                    if (readbytes > 0) {
                        readBuffer.flip();
                        byte[] bytes = new byte[readBuffer.remaining()];
                        readBuffer.get(bytes);
                        String body = new String(bytes, "UTF-8");
                        System.out.println(Thread.currentThread().getName() + ", Get the time from server :" + body);
//                        this.stop = true;
                    } else if (readbytes < 0) {
                        key.cancel();
                        channel.close();
                    }
                }
            } else {
                if (key != null) {
                    key.cancel();
                    if (key.channel() != null) {
                        key.channel().close();
                    }
                }
            }
        }

        private void doConnect() throws IOException {
            // 如果直接连接成功， 则注册到多路复用器上，发送请求消息， 读应答
            if (socketChannel.connect(new InetSocketAddress(host, port))) {
                socketChannel.register(selector, SelectionKey.OP_READ);
                doWrite(socketChannel);
            } else {
//                System.out.println("socketChannel.register(selector, SelectionKey.OP_CONNECT);");
                socketChannel.register(selector, SelectionKey.OP_CONNECT);
            }
        }

        private void doWrite(SocketChannel channel) throws IOException {
            byte[] request = "QUERY_TIME_ORDER".getBytes();
            ByteBuffer writeBuffer = ByteBuffer.allocate(request.length);
            writeBuffer.put(request);
            writeBuffer.flip();
            channel.write(writeBuffer);
            if (!writeBuffer.hasRemaining()) {
//                System.out.println("Send order to server succeed!");
            }
        }
    }

    public static void main(String[] args) {
        int port = 8080;
        if (args != null && args.length > 0) {
            try {
                port = Integer.valueOf(args[0]);
            } catch (NumberFormatException e) {
                // 采用默认值
            }
        }

        for (int i=0; i<1; i++) {
            TimeClientHandle timeClientHandle = new TimeClientHandle("localhost", port);
            new Thread(timeClientHandle, "NIO-timeClient-thread-" + i).start();
        }
        countDownLatch.countDown();
    }
}
