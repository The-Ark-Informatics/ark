/**
 * 
 * This is a new file
 *
 *
 */
package au.org.theark.core.exception;

/**
 * @author nivedann
 *
 */
public class EntityNotFoundException extends ArkBaseException{

	public EntityNotFoundException(){
		super();
	}
	
	public EntityNotFoundException(String errorMessage){
		super(errorMessage);
	}

}
