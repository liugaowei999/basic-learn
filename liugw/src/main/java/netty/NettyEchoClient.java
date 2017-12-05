package netty;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;

public class NettyEchoClient {
    private String delimiterStr = "$.";
    
    public void connect(String host, int port) throws Exception {
        // 创建客户端NIO线程组
        EventLoopGroup group = new NioEventLoopGroup();

        try {
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(group)
                     .channel(NioSocketChannel.class)
                     .option(ChannelOption.TCP_NODELAY, true)
                     .handler(new ChannelInitializer<SocketChannel>() {

                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            // Auto-generated method stub
                            ByteBuf delimiter = Unpooled.copiedBuffer(delimiterStr.getBytes());
                            
                            ch.pipeline().addLast(new DelimiterBasedFrameDecoder(1024, delimiter));
                            ch.pipeline().addLast(new StringDecoder());
                            ch.pipeline().addLast(new ChannelInboundHandlerAdapter() {
                                private int counter = 0;
                                @Override
                                public void channelActive(ChannelHandlerContext ctx) throws Exception {
                                    for (int i=0; i<100 ; i++) {
//                                        ByteBuf request = Unpooled.copiedBuffer(("Hello Netty" + delimiterStr).getBytes());
                                        ctx.writeAndFlush(Unpooled.copiedBuffer(("Hello Netty" + delimiterStr).getBytes()));
                                        System.out.println("Send ..." + i);
                                    }
                                }
                                
                                @Override
                                public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                                    System.out.println("Receive reponse from server : [" + msg + "] , NO:" + ++counter);
                                }
                                
                                @Override
                                public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
                                    System.out.println("channelReadComplete .......");
                                    ctx.flush();
                                    ctx.close();
                                }
                                
                                @Override
                                public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
                                        throws Exception {
                                    cause.printStackTrace();
                                    ctx.close();
                                }
                            });
                        }
                    });
           ChannelFuture future = bootstrap.connect(host, port).sync();
           future.channel().closeFuture().sync();
        } finally {
            group.shutdownGracefully();
        }
    }
    
    public static void main(String[] args) throws Exception {
        new NettyEchoClient().connect("localhost", 8080);
    }
}
