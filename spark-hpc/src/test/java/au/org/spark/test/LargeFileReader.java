package au.org.spark.test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;

public class LargeFileReader {
	 public static void main(String[] args) throws FileNotFoundException {
		 
	        File filePath = new File("/home/tranaweera/data/10000SNP/10000/10000SAMPLE.ped");
	 
	        BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(filePath)));
	 
	        reader.lines().forEach(line -> {
	            // process liness
	            System.out.println(line);
	        });
	    }
}
