package au.org.theark.core.jni;

import java.io.Serializable;

public class MadelineArkProxy implements Serializable{
   
	/**
	 * 
	 */
	private static final long	serialVersionUID	= 1L;

    public native void sayHello();
    
    public native void generatePedigree(String dataFilePath, String fileOutputPath, String columnList);

}
