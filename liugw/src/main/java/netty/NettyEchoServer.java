package netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.FixedLengthFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

public class NettyEchoServer {
    private String delimiterStr = "$.";
    public String getDelimiterStr() {
        return delimiterStr;
    }
    public void setDelimiterStr(String delimiterStr) {
        this.delimiterStr = delimiterStr;
    }
    public void bind(int port) throws Exception {
        // 配置服务端NIO线程组
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workGroup = new NioEventLoopGroup();
        
        try {
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(bossGroup, workGroup)
            .channel(NioServerSocketChannel.class)
            .option(ChannelOption.SO_BACKLOG, 100)
            // the ChannelHandler to use for serving the requests.
            .handler(new LoggingHandler(LogLevel.INFO))
            // Set the ChannelHandler which is used to serve the request for the Channel's.
            .childHandler(new ChannelInitializer<SocketChannel>() {
                
                @Override
                protected void initChannel(SocketChannel ch) throws Exception {
                    // Auto-generated method stub
                    ByteBuf delimiter = Unpooled.copiedBuffer(delimiterStr.getBytes());
//                    ch.pipeline().addLast(new DelimiterBasedFrameDecoder(1024, delimiter));
                    ch.pipeline().addLast(new FixedLengthFrameDecoder(5));
                    ch.pipeline().addLast(new StringDecoder());
                    ch.pipeline().addLast(new EchoServerHandler());
                }
            });
            
            // 绑定端口， 同步等待成功. ChannelFuture : 保存异步操作执行的结果和状态
            /**
             *                                        +---------------------------+
                                                  | Completed successfully    |
                                                  +---------------------------+
                                             +---->      isDone() = true      |
             +--------------------------+    |    |   isSuccess() = true      |
             |        Uncompleted       |    |    +===========================+
             +--------------------------+    |    | Completed with failure    |
             |      isDone() = false    |    |    +---------------------------+
             |   isSuccess() = false    |----+---->      isDone() = true      |
             | isCancelled() = false    |    |    |       cause() = non-null  |
             |       cause() = null     |    |    +===========================+
             +--------------------------+    |    | Completed by cancellation |
                                             |    +---------------------------+
                                             +---->      isDone() = true      |
                                                  | isCancelled() = true      |
                                                  +---------------------------+
             */
            ChannelFuture f = bootstrap.bind(port).sync();
            
            // 等待服务端监听端口关闭
            f.channel().closeFuture().sync();
        } finally {
            // 优雅的关闭
            bossGroup.shutdownGracefully();
            workGroup.shutdownGracefully();
        }
    }
    
    public static void main(String[] args) throws Exception {
        int port = 8080;
        new NettyEchoServer().bind(port);
    }
}
