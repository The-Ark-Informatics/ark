package au.org.theark.core.exception;
/**
 * All business exceptions within the Ark system  will extend this class.
 * @author nivedann
 *
 */
public class ArkBaseException extends Exception{

	private static final long serialVersionUID = 1L;
	public ArkBaseException(){
		super();	
	}
	
	public ArkBaseException(String message){
		super(message);
	}
}
