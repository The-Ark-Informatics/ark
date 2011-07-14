/**
 * 
 */
package au.org.theark.phenotypic.exception;

/**
 * @author cellis
 *
 */
@SuppressWarnings("serial")
public class FileTypeNotAvailableException extends PhenotypicBaseException {
	public FileTypeNotAvailableException() {
		super();
	}
	public FileTypeNotAvailableException(String message) { 
		super(message);
	}
}