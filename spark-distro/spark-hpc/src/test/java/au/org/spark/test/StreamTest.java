package au.org.spark.test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.util.concurrent.atomic.AtomicInteger;

public class StreamTest {
	public static void main1(String[] args) throws FileNotFoundException {
        
        File filePath = new File("/home/thilina/plink-1.07-x86_64/data/expr_data/10000SNP/1000/");
 
        BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(filePath)));
//        int i=0;
        
        AtomicInteger i = new AtomicInteger(0);
        
        reader.lines().forEach(line -> {
            // process liness
            System.out.println(line);
            i.addAndGet(1);
        });
        
        System.out.println("The line count -- "+i.get());
    }
	
	
	public static void main(String[] args) {
		for(AtomicInteger i = new AtomicInteger(0);i.get()<10;){
			System.out.println(i.addAndGet(1));
		}
	}
}
