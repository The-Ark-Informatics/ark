package au.org.theark.core.exception;

public class PersonNotFoundException extends ArkBaseException{

	private static final long serialVersionUID = 1L;
	
	public PersonNotFoundException(){
		super();
	}
	
	public PersonNotFoundException(String errorMessage){
		super(errorMessage);
	}
}
