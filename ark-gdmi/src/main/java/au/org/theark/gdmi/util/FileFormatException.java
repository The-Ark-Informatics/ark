/**
 * 
 */
package au.org.theark.gdmi.util;

import au.org.theark.gdmi.exception.GDMIBaseException;

/**
 * @author elam
 *
 */
public class FileFormatException extends GDMIBaseException {
	public FileFormatException() {
		super();
	}
	public FileFormatException(String message) { 
		super(message);
	}
}
