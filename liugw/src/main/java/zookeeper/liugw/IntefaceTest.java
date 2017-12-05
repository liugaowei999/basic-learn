package zookeeper.liugw;

public class IntefaceTest 
{

	public interface Inter1 
    {
        void work();
        void print(int ... intarr);
    }
	
	public static void main( String[] args )
	{
		Inter1 intf = new Inter1()
		{
			public void work()
			{
				System.out.println("Inter1 .... working ...");
			}
			
			public void print(int ... intarr)
			{
				for(int i: intarr)
				{
					System.out.print(i +" ");
				}
				 System.out.println();
			}
		};
		
		intf.work();
		intf.print(1,2,3,4,5,6,9,8 );
		
		
		Inter1 intf2 = new Inter1()
		{
			public void work()
			{
				System.out.println("Inter2 .... working ...");
			}
			
			public void print(int ... intarr)
			{
				
			}
		};
		
		intf2.work();
	}
}
