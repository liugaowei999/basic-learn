package Thread.liugw;

import java.io.IOException;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.io.PipedReader;
import java.io.PipedWriter;

import org.omg.CORBA.PRIVATE_MEMBER;

/**
 * 管道输入/输出流主要包括了如下4种具体实现：PipedOutputStream、PipedInputStream、PipedReader和PipedWriter，
 * 前两种面向字节，而后两种面向字符。
 * @author liugaowei
 *
 */
public class Piped 
{
	
	static class Print implements Runnable
	{
		private PipedReader in;
		private PipedInputStream inputStream;
		
		public Print( PipedReader in )
		{
			this.in = in;
		}
		
		public Print( PipedInputStream in )
		{
			this.inputStream = in;
		}
		
		public void run1()
		{
			int receive = 0;
			
			try
			{
				while( ( receive = in.read() ) != -1 )
				{
					System.out.println((char)receive);
				}
			}
			catch(IOException ex)
			{
				ex.printStackTrace();
			}
		}
		
		public void run()
		{
			int receive = 0;
			
			try
			{
				while( ( receive = inputStream.read() ) != -1 )
				{
					System.out.println((char)receive);
				}
			}
			catch(IOException ex)
			{
				ex.printStackTrace();
			}
		}
	}
	
	public static void main( String[] args ) throws IOException
	{
		PipedWriter out = new PipedWriter();
		PipedReader in  = new PipedReader();
		
		PipedInputStream inputStream = new PipedInputStream();
		PipedOutputStream outputStream = new PipedOutputStream();
		
		// 将输入输出流进行连接， 否则在使用时会抛出 IOExcepion
		out.connect(in);
		outputStream.connect(inputStream);
		
		
//		Thread printThread = new Thread( new Print(in), "Print_Thread");
		Thread printThread = new Thread( new Print(inputStream), "Print_Thread");
		printThread.start();
		
		int receive = 0;
		try
		{
			while( (receive=System.in.read()) != -1 )
			{
//				out.write(receive);
				outputStream.write(receive);
			}
		}
		finally
		{
			out.close();
		}
	}
}
