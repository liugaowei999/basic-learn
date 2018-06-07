package com.cttic.liugw.ordinary;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class SerializableTest 
{

	public static void main(String[] args) throws FileNotFoundException, IOException, ClassNotFoundException
	{
		AA a = new AA( 10, "张三");
		
		System.out.println(a);
		
		// 序列化
		ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream("D:/AClass.txt"));
		objectOutputStream.writeObject(a);
		objectOutputStream.close();
		
		// 反序列化
		ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream("D:/AClass.txt"));
		a = (AA)objectInputStream.readObject();
		System.out.println(a);
		
	}
}

class A implements Serializable
{
	int a;
	transient String bString;
	
	public A (int a, String bString)
	{
		this.a = a;
		this.bString = bString;
	}
	
	public String toString()
	{
		return "a = " + a +", bString = " + bString;
	}
}