package au.org.spark.test;

import java.lang.reflect.Field;
import java.security.AlgorithmParameterGenerator;
import java.security.InvalidParameterException;


public class Diffie {
	public static void main(String[] args) throws Exception {
		
//		System.setProperty("jdk.tls.ephemeralDHKeySize", "2048");
		
		try {
			Field field = Class.forName("javax.crypto.JceSecurity").
			getDeclaredField("isRestricted");
			field.setAccessible(true);
			field.set(null, java.lang.Boolean.FALSE);
			} catch (Exception ex) {
			ex.printStackTrace();
			}
		
		AlgorithmParameterGenerator apg = AlgorithmParameterGenerator.getInstance("DiffieHellman");
		int good = 0;
		for (int i = 512; i <= 16384; i += 64) {
			try {
				apg.init(i);
			} catch (InvalidParameterException e) {
				break;
			}
			good = i;
		}
		System.out.println("maximum DH size is " + good);
	}
}
