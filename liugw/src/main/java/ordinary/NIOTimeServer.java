package ordinary;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Date;
import java.util.Iterator;
import java.util.Set;



public class NIOTimeServer {
    
    static class MultiplexerTimeServer implements Runnable {
        private Selector selector;
        private ServerSocketChannel serverChannel;
        private volatile boolean stop;
        private int maxConnections = 1024;

        /**
         * 初始化多路复用器，绑定端口
         * @param port
         */
        public MultiplexerTimeServer(int port) {
            try {
                selector = Selector.open();
                serverChannel = ServerSocketChannel.open();
                serverChannel.configureBlocking(false);
                serverChannel.socket().bind(new InetSocketAddress(port), maxConnections);
//                serverChannel.bind(new InetSocketAddress(port), 1024); // 这个bind什么用？
                serverChannel.register(selector, SelectionKey.OP_ACCEPT);
                System.out.println("The time server is start in port : " + port);
            } catch (IOException e) {
                // TODO: handle exception
            }
        }
        
        public void stop() {
            this.stop = true;
        }

        /**
         * 执行
         */
        @Override
        public void run() {
            while (!stop) {
                try {
                    // 每隔1秒钟重新主动触发一次
                    selector.select(1000);
                    // 一直阻塞，当有活跃的key时，被动触发
                    selector.select();
                    // Returns this selector's selected-key set. 
                    Set<SelectionKey> selectionKeys = selector.selectedKeys();
                    // Returns this selector's key set. 
                    //Set<SelectionKey> selectionKeys = selector.keys();
//                    System.out.println(selectionKeys.size());
                    Iterator<SelectionKey> iterator = selectionKeys.iterator();
                    SelectionKey key = null;
                    while (iterator.hasNext()) {
                        key = iterator.next();
                        iterator.remove();
                        // 处理当前key
                        try {
                            handleInput(key);
                        } catch (Exception e) {
                            if (key != null) {
                                key.cancel();
                                if (key.channel() != null) {
                                    key.channel().close();
                                }
                            }
                        }
                    }
                } catch (Throwable t) {
                    t.printStackTrace();
                }
            } // end while (!stop) 
            
            // 多路复用器关闭后 ， 所有注册在上面的channel， pipe 等资源都会被自动去注册和关闭，所以不需要重复释放资源
            if (selector != null) {
                try {
                    selector.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }

        private void handleInput(SelectionKey key) throws IOException {
            if (key.isValid()) {
                /**
                 * 处理新接入的请求消息
                 * Tests whether this key's channel is ready to accept a new socket connection. 
                 * Returns:true if, and only if, readyOps() & OP_ACCEPT is nonzero
                 */
//                if (key.readyOps() & SelectionKey.OP_ACCEPT == byte0) {
                if (key.isAcceptable()) {
                    // Accept the new connection
                    ServerSocketChannel serverSocketChannel = (ServerSocketChannel) key.channel();
                    SocketChannel socketChannel = serverSocketChannel.accept();
                    socketChannel.configureBlocking(false);
                    socketChannel.register(selector, SelectionKey.OP_READ);
                }
                
                /**
                 * 处理读请求
                 */
                if (key.isReadable()) {
                    SocketChannel socketChannel = (SocketChannel) key.channel();
                    ByteBuffer readBuffer = ByteBuffer.allocate(1024); // 100k
                    int readBytes = socketChannel.read(readBuffer);
                    if (readBytes > 0) {
                        // Flips this buffer. The limit is set to the current position and then 
                        // the position is set to zero. If the mark is defined then it is discarded. 
                        // 反转此缓冲区。首先将限制设置为当前位置，然后将位置设置为 0。如果已定义了标记，则丢弃该标记
                        readBuffer.flip();
                        // Returns the number of elements between the current position and the limit.
                        // 返回当前位置与限制之间的元素数。 
                        byte[] bytes = new byte[readBuffer.remaining()];
                        // This method transfers bytes from this buffer into the given destination array. 
                        // An invocation of this method of the form src.get(a) behaves in exactly the same way as the invocation
                        readBuffer.get(bytes);
                        String body = new String(bytes, "UTF-8");
                        System.out.println(Thread.currentThread().getName() + ",The time server receive order:" + body);
                        String currentTime = "QUERY_TIME_ORDER".equalsIgnoreCase(body)?new Date().toString():"BAD ORDER";
                        doWrite(socketChannel, currentTime);
                    } else if (readBytes < 0) {
                        // 对端链路关闭
                        key.cancel();
                        socketChannel.close();
                    } else {
                        // 读到0字节，忽略
                    }
                }
            }
        }

        private void doWrite(SocketChannel socketChannel, String reponse) throws IOException {
            if (reponse != null && reponse.trim().length() > 0) {
                byte[] bytes = reponse.getBytes();
                ByteBuffer writeBuffer = ByteBuffer.allocate(bytes.length);
                writeBuffer.put(bytes);
                writeBuffer.flip();
                socketChannel.write(writeBuffer);
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

        // 创建多路复用类对象
        MultiplexerTimeServer multiplexerTimeServer = new NIOTimeServer.MultiplexerTimeServer(port);

        // 启动线程
        new Thread(multiplexerTimeServer, "multiple-time-server-thread").start();
    }

}
