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
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;
import io.netty.handler.codec.string.StringDecoder;

public class SubReqServer {

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
			ch.pipeline().addLast(new ObjectDecoder(1024*1024, 
			                                         ClassResolvers.weakCachingConcurrentResolver(this.getClass().getClassLoader())));
			ch.pipeline().addLast(new ObjectEncoder());
			ch.pipeline().addLast(new TimeServerHandler());
			
		}
		
	}
	
	public static void main(String[] args) throws Exception {
		int port = 8080;
		new SubReqServer().bind(port);
	}
}
