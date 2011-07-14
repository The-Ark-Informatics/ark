package au.org.theark.core.exception;

public class UserNameExistsException extends ArkBaseException{

	private static final long serialVersionUID = 1L;
	
	public UserNameExistsException(){
		super();
	}
	
	public UserNameExistsException(String msg){
		super(msg);
	}
	

}
