/**
 * 
 */
package au.org.theark.phenotypic.exception;

import au.org.theark.phenotypic.exception.PhenotypicBaseException;

/**
 * @author elam
 *
 */
@SuppressWarnings("serial")
public class FileFormatException extends PhenotypicBaseException {
	public FileFormatException() {
		super();
	}
	public FileFormatException(String message) { 
		super(message);
	}
}
