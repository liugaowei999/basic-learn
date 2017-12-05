package Thread.liugw.ThreadPool;

public class HttpServerTest 
{

	public static void main( String[] args )
	{
		SimpleHttpServer httpServer = new SimpleHttpServer();
		httpServer.setPort(8089);
		httpServer.setBasePath("D:/lgw/httpserver");
		try {
			httpServer.start();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
