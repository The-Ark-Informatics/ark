package au.org.spark.util;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Scanner;

public final class SnpOpperations {
	
	public static int prepareInt(int pos, String[] mapValues, HashMap<String, Byte> alleleMap)throws IOException{
		int finalInt = Integer.parseInt("00",2);	
		
		int length = mapValues.length; 
		
		for(int i=0; i<32; i+=2) {
			if(pos + i > length-1) {
				break;
			}
			byte a = snp2byte(mapValues[pos+i], mapValues[pos+i+1],alleleMap);
			finalInt = (finalInt | (a << i));
		}
		
		return finalInt;
	}
	
	public static byte snp2byte(String a1, String a2, HashMap<String, Byte> alleleMap){	
		Byte result = alleleMap.get(a1 + a2);
		return result !=null ? result: 0b00000010;
	}
	
	public static HashMap<String, Byte> alleleMap(String[] snpArray, int snpId, String mapFileName)throws IOException{
		HashMap<String, Byte> alleleMap = new HashMap<String, Byte>();
		
		// Collections.frequency(c, o) Arrays.asList(snpArray).re
		
		char allele1='0';
		char allele2='0';
		
		int count =0;
		for(int i=0;i<snpArray.length;++i){
			if("0".equalsIgnoreCase(snpArray[i])){
				continue;
			}
			++count;
		} 
		
		String[] values = new String[count];
		for(int i =0,j=0;i<snpArray.length;++i){
			String s=snpArray[i];
			if(!"0".equalsIgnoreCase(s)){
				values[j]=s;
				++j;
			}
		}
		
		if(values.length >0){
			char tmp1=values[0].charAt(0);
			char tmp2='0';
			int match =0;
			for(String s:values){
				if(tmp1 == s.charAt(0)){
					++match;
				}else{
					tmp2=s.charAt(0);
				}	
			}
			
			int diff= values.length - match;
			
			if(diff == 0){
				allele2= tmp1;
			}else if(diff > values.length/2){
				allele1 = tmp1;
				allele2 = tmp2;
			}else if(diff > values.length/2){
				allele1 = tmp2;
				allele2 = tmp1;
			}else if(diff == values.length/2){				
				int comp =Character.compare(tmp1, tmp2);
				if(comp >0){
					allele1 =tmp1; 
					allele2 = tmp2;
				}else{
					allele1 =tmp2; 
					allele2 = tmp1;
				}
			}							
		}
		
		
		FileInputStream inputStream = null;
		Scanner sc = null;
//		String mapFileName= "data/5SNPx3/3SAMPLE.map";

		inputStream = new FileInputStream(mapFileName);
		sc = new Scanner(inputStream, "UTF-8");
		String dataLine = null;
		
		for(int i =1; sc.hasNextLine();++i){
			dataLine=sc.nextLine();
			if(i==snpId){
				String bimLine = dataLine+"\t"+allele1+"\t"+allele2+"\n"; 
//				writeFile(bimLine.getBytes(), "sparksnpmjc1.bim");
				break;
			}
		}
		
		sc.close();
		inputStream.close();
		
		
		alleleMap.put(allele1+""+allele1, new Byte((byte) 0b00000000)); // Homozygote "1"/"1"
		alleleMap.put(allele1+""+allele2,((byte) 0b00000010)); // Heterozygote
		alleleMap.put(allele2+""+allele1, new Byte((byte) 0b00000010)); // Heterozygote
		alleleMap.put(allele2+""+allele2, new Byte((byte) 0b00000011)); // Homozygote "2"/"2"
		
		return alleleMap;
	}

}
