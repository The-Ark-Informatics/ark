package au.org.theark.core.jni;

import java.io.Serializable;

public class ArkMadelineProxy implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public native void connect();
	
	public native String generatePedigree(String pedigreeMsg, String columnList);
	
}
