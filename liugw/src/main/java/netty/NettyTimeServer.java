package netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.LineBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;

public class NettyTimeServer {

	public void bind(int port) throws Exception {
		// 配置服务端NIO线程组
		EventLoopGroup bossGroup = new NioEventLoopGroup();
		EventLoopGroup workGroup = new NioEventLoopGroup();
		
		try {
			ServerBootstrap bootstrap = new ServerBootstrap();
			bootstrap.group(bossGroup, workGroup)
					.channel(NioServerSocketChannel.class)
					.option(ChannelOption.SO_BACKLOG, 1024)
					.childHandler(new ChildChannelHandler());
			// 绑定端口，同步等待成功
			ChannelFuture future = bootstrap.bind(port).sync();
			System.out.println("The TIME SERVER start in port : " + port);
			
			// 等待服务端监听端口关闭
			future.channel().closeFuture().sync();
			System.out.println("COLSED NOW!");
		} finally {
			bossGroup.shutdownGracefully();
			workGroup.shutdownGracefully();
		}
				
	}
	
	private class ChildChannelHandler extends ChannelInitializer<SocketChannel> {

		@Override
		protected void initChannel(SocketChannel ch) throws Exception {
		    System.out.println("initChannel ...");
			// LineBasedFrameDecoder : 遍历ByteBuf中的可读字节，判断是否有'\r' '\r\n'. 如果有，以此为结束位置
		    // 从可读索引到结束位置区间的字节就组成一行。
		    ch.pipeline().addLast(new LineBasedFrameDecoder(1024));
		    ch.pipeline().addLast(new StringDecoder()); // 将接收到对象转换为字符串
			ch.pipeline().addLast(new TimeServerHandler());
		}
		
	}
	
	public static void main(String[] args) throws Exception {
		int port = 8080;
		new NettyTimeServer().bind(port);
	}
}
