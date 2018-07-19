package com.cttic.liugw.ordinary.ADT;

import java.sql.Time;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Random;
import java.util.TreeSet;
import java.util.concurrent.TimeUnit;

import com.cttic.liugw.ordinary.ADT.sort.ArrayType;

public class Diff {
	public static ArrayType array;
	
	public static ArrayType getArray() {
        array = new ArrayType(SIZE);
        Random random = new Random(System.currentTimeMillis());
        for (int i = 0; i < SIZE; i++) {
            array.insert(random.nextInt(SIZE*100 ));
        }
        //        array.display();
        return array;
    }
	
	public static void getDiff() throws InterruptedException{
		ArrayType a = getArray();
    	TimeUnit.SECONDS.sleep(1);
        ArrayType b = getArray();
		long begin = System.nanoTime();
		a.quickSort(false);
		b.quickSort(false);
		int index_a =0; int  index_b =0;
		while(index_a<SIZE && index_b<SIZE){
			if(a.get(index_a) == b.get(index_b)){
				a.set(index_a++, -1);
				b.set(index_b++, -1);
			}else if(a.get(index_a)< b.get(index_b)){
				index_a++;
			}else{
				index_b++;
			}
		}
		System.out.println("取差集耗时:" + (System.nanoTime() - begin) / 1000000 + "ms");
	}
	
	public static void getDiff2() throws InterruptedException{
		ArrayType a = getArray();
    	TimeUnit.SECONDS.sleep(1);
        ArrayType b = getArray();
        
		long begin = System.nanoTime();
		TreeSet<Long> treeSet = new TreeSet<>();
		for(int i=0 ; i<SIZE;i++){
			treeSet.add(a.get(i));
		}
		
		for(int i=0 ; i<SIZE;i++){
			if(treeSet.remove(b.get(i))){
				b.set(i, -1);
			}
		}
		System.out.println("TreeSet取差集耗时:" + (System.nanoTime() - begin) / 1000000 + "ms");
	}
	
	public static void getDiff3() throws InterruptedException{
		ArrayType a = getArray();
    	TimeUnit.SECONDS.sleep(1);
        ArrayType b = getArray();
        
		long begin = System.nanoTime();
		HashSet<Long> treeSet = new HashSet<>();
		for(int i=0 ; i<SIZE;i++){
			treeSet.add(a.get(i));
		}
		
		for(int i=0 ; i<SIZE;i++){
			if(treeSet.remove(b.get(i))){
				b.set(i, -1);
			}
		}
		System.out.println("HashSet取差集耗时:" + (System.nanoTime() - begin) / 1000000 + "ms");
	}
	
	public static void getDiff4() throws InterruptedException{
		ArrayType a = getArray();
    	TimeUnit.SECONDS.sleep(1);
        ArrayType b = getArray();
        
		long begin = System.nanoTime();
		LinkedHashSet<Long> treeSet = new LinkedHashSet<>();
		for(int i=0 ; i<SIZE;i++){
			treeSet.add(a.get(i));
		}
		
		for(int i=0 ; i<SIZE;i++){
			if(treeSet.remove(b.get(i))){
				b.set(i, -1);
			}
		}
		System.out.println("LinkedHashSet取差集耗时:" + (System.nanoTime() - begin) / 1000000 + "ms");
	}
	
	public static void getDiff5() throws InterruptedException{
		ArrayType a = getArray();
    	TimeUnit.SECONDS.sleep(1);
        ArrayType b = getArray();
        
		long begin = System.nanoTime();
		HashMap<Long,Object> treeSet = new HashMap<>();
		for(int i=0 ; i<SIZE;i++){
			treeSet.put(a.get(i),null);
		}
		
		for(int i=0 ; i<SIZE;i++){
			if(treeSet.remove(b.get(i),null)){
				b.set(i, -1);
			}
		}
		System.out.println("HashMap取差集耗时:" + (System.nanoTime() - begin) / 1000000 + "ms");
	}

    private static final int SIZE = 1100000;

    public static void main(String[] args) throws InterruptedException {
    	
        
//        a.display();
//        b.display();
    	getDiff();
    	getDiff2();
        getDiff3();
        getDiff4();
        getDiff5();
        System.out.println("======================");
//        a.display();
//        b.display();
    	
    }

}
