package au.org.theark.core.exception;

/**
 * @author nivedann
 *
 */
public class CannotRemoveArkModuleException extends ArkBaseException{

	private static final long serialVersionUID = 1L;
	
	public CannotRemoveArkModuleException(){
		super();
	}
	
	public CannotRemoveArkModuleException(String errorMessage){
		super(errorMessage);
	}

}
