/**
 * 
 */
package au.org.theark.core.exception;

/**
 * @author cellis
 *
 */
@SuppressWarnings("serial")
public class FileTypeNotAvailableException extends ArkBaseException {
	public FileTypeNotAvailableException() {
		super();
	}
	public FileTypeNotAvailableException(String message) { 
		super(message);
	}
}