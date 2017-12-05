package netty;

import java.util.Date;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerAdapter; // netty5.0使用。5.0已经废弃
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class TimeServerHandler extends ChannelInboundHandlerAdapter {
	private int counter = 0;
	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
//	    System.out.println("channelRead");
//		ByteBuf buf = (ByteBuf) msg;
//		byte[] req = new byte[buf.readableBytes()];
//		buf.readBytes(req);
//		String body = new String(req, "UTF-8");
	    String body = (String) msg;
		System.out.println("Time Server receive order : " + body + ", total request count:" + ++counter);
		
		String currentTime = "QUERY_TIME".equalsIgnoreCase(body)?new Date().toString():"BAD COMMAND";
		currentTime = currentTime + System.getProperty("line.separator");
		ByteBuf respBuf = Unpooled.copiedBuffer(currentTime.getBytes());
		ctx.write(respBuf);
	}
	
	@Override
	public void channelReadComplete(ChannelHandlerContext ctx) throws Exception{
		ctx.flush();
	}
	
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
		ctx.close();
	}

}
