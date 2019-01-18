package au.org.spark.test;

import java.util.concurrent.atomic.AtomicInteger;

public class Main {

	public static void main1(String[] args) {
		System.out.println("Main Class");
		
		AtomicInteger i = new AtomicInteger(6);
		
		System.out.println(i.getAndIncrement());
		
		System.out.println(i.get());
		
		int j=2;
		
		System.out.println(j*4);
		
		System.out.println(j);
		
	}
	
	public static void main(String[] args) {
		System.out.println("PED file generator");
		System.out.println("MAP file generator");
		
	}
	
	
	

}
