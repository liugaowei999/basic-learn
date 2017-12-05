package netty;

import java.io.UnsupportedEncodingException;
import java.util.logging.Logger;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class TimeClientHandler extends ChannelInboundHandlerAdapter {

	private static final Logger logger = Logger.getLogger(TimeClientHandler.class.getName());
	private final ByteBuf firstMessageBuf;
	private byte[] request;
	private int counter = 0;
	
	public TimeClientHandler() {
		request = ("QUERY_TIME"+System.getProperty("line.separator")).getBytes();
//		byte[] req = ("QUERY_TIME"+System.getProperty("line.separator")).getBytes();
		firstMessageBuf = Unpooled.buffer(request.length);
		firstMessageBuf.writeBytes(request);
	}
	
	@Override
	public void channelRead(ChannelHandlerContext ctx , Object message) throws Exception {
//		ByteBuf buf = (ByteBuf) message;
//		byte[] rep = new byte[buf.readableBytes()];
//		buf.readBytes(rep);
//		String body = new String(rep, "UTF-8");
	    String body = (String) message;
		System.out.println("Nos is " + body + ", receive response count : " + ++counter);
	}
	
	@Override
	public void channelActive (ChannelHandlerContext ctx) throws Exception {
	    System.out.println("Send order...... " + new String(request,"UTF-8"));
		ByteBuf message = null;
		for (int i=0; i<1000; i++) {
			message = Unpooled.buffer(request.length);
			message.writeBytes(request);
			ctx.writeAndFlush(message);
		}
	}
	
//	@Override
//    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
//        ctx.flush();
//    }
	
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
		logger.warning(cause.getMessage());
		ctx.close();
	}
	
}
