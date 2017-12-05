package Thread.liugw.ThreadPool;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

import java.io.Closeable;
import java.io.File;


public class SimpleHttpServer 
{

	// 处理HttpRequest的线程池, 默认1个线程
	static ThreadPool<HttpRequestHandler>  threadPool = new DefaultThreadPool<HttpRequestHandler>(10);
	
	// SimpleHttpServer的根路径
	static String basePath;
	static ServerSocket serverSocket;
	
	// 服务器监听端口
	static int port = 8089;
	
	public static void setPort( int port )
	{
		
		if( port > 0 )
		{
			SimpleHttpServer.port = port;
		}
	}
	
	public static void setBasePath( String basePath )
	{
		if( basePath != null && new File(basePath).exists() && new File(basePath).isDirectory() )
		{
			SimpleHttpServer.basePath = basePath;
		}
	}
	
	// 启动SimpleHttpServer
	public static void start() throws Exception
	{
		serverSocket = new ServerSocket(port);
		System.out.println("Http Server started. Port=" + port + ", basepath=" + basePath);
		
		Socket socket = null;
		while( (socket = serverSocket.accept() ) != null )
		{
			System.out.println("Received one client 。。。");
			// 接收一个客户端的socket， 生成一个HttpRequestHandler ,放入线程池执行
			threadPool.execute(new HttpRequestHandler(socket));
		}
		serverSocket.close();
	}
	
	static class HttpRequestHandler implements Runnable
	{
		private Socket socket;
		
		public HttpRequestHandler( Socket socket )
		{
			this.socket = socket;
		}
		
		public void run()
		{
			String line = null;
			BufferedReader bufferedReader = null;
			BufferedReader bufferedReader2 = null;
			
			PrintWriter out = null;
			InputStream in  = null;
			
			try
			{
				bufferedReader2 = new BufferedReader(new InputStreamReader(socket.getInputStream(),"UTF-8"));
				String header = bufferedReader2.readLine();
				System.out.println("由相对路径计算出绝对路径: header=" + header);
				
				// 由相对路径计算出绝对路径
				String filePath = basePath + header.split(" ")[1];
				System.out.println("由相对路径计算出绝对路径: filePath=" + filePath);
				
				out = new PrintWriter(socket.getOutputStream());
				
				if( !(new File(filePath).exists()) )
				{
					out.println("HTTP/1.1 500");
					out.println("ERROR!");
					out.flush();
					return;
				}
				
				// 如果请求资源的后缀为jpg或者ico， 则读取资源并输出
				if( filePath.endsWith("jpg") || filePath.endsWith("ico") )
				{
					in = new FileInputStream(filePath);
					ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
					int i = 0;
					byte[] databyte = new byte[1024];
					
					while( (i=in.read(databyte)) != -1 )
					{
						byteArrayOutputStream.write(databyte);
					}
					
					byte[] array = byteArrayOutputStream.toByteArray();
					out.println("HTTP/1.1 200 OK");
					out.println("Server : Molly");
					out.println("Content-Type: imge/jpeg");
					out.println("Content-Length: " + array.length);
					out.println("");
					socket.getOutputStream().write(array,0,array.length);
				}
				else
				{
					bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(filePath)));
					out = new PrintWriter(socket.getOutputStream());
					out.println("HTTP/1.1 200 OK");
					out.println("Server: Molly");
					out.println("Content-Type: text/html; charset=UTF-8");
					out.println("");
					while( (line=bufferedReader.readLine()) != null )
					{
						System.out.println("line=[" + line + "]");
						out.println(line);
					}
				}
				out.flush();
			}
			catch( Exception exception )
			{
				out.println("HTTP/1.1 500");
				out.println("");
				out.flush();
			}
			finally
			{
				HttpRequestHandler.close(bufferedReader, in , bufferedReader2, out, socket );
			}
		}
		
		// 关闭流或者Socket
		public static void close( Closeable ...closeables )
		{
			if( closeables != null )
			{
				for( Closeable closeable  : closeables )
				{
					try 
					{
						closeable.close();
					} catch (Exception e) 
					{
						// TODO: handle exception
					}
				}
			}
		}
	}
}
