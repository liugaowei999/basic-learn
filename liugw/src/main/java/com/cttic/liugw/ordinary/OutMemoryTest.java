package com.cttic.liugw.ordinary;

import java.util.ArrayList;
import java.util.List;

public class OutMemoryTest 
{

	static  class OOMObject{
		
	}
	
	public static void  main(String[] args) {
		
		List<OOMObject> list = new ArrayList<>();
		int cnt = 0;
		System.out.println("totalMemory:" + Runtime.getRuntime().totalMemory()/1024/1024);
		try{
			while(true)
			{
				cnt++;
				list.add(new OOMObject());
				
			}
		}
		catch (OutOfMemoryError e) {
			System.out.println("OutOfMemoryError!" + cnt); // 540218 1215488
			System.out.println("FreeMemory:" + Runtime.getRuntime().freeMemory()/1024/1024);
//			Runtime.getRuntime().gc();
			System.out.println("FreeMemory:" + Runtime.getRuntime().freeMemory()/1024/1024);
			// TODO: handle exception
		}
	}
}
