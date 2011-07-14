/**
 * 
 */
package au.org.theark.core.exception;
/**
 * @author elam
 *
 */
@SuppressWarnings("serial")
public class FileFormatException extends ArkBaseException {
	public FileFormatException() {
		super();
	}
	public FileFormatException(String message) { 
		super(message);
	}
}
