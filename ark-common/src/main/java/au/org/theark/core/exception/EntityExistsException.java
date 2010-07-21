package au.org.theark.core.exception;

public class EntityExistsException extends ArkBaseException{
	
	public EntityExistsException(){
		super();
	}
	
	public EntityExistsException(String msg){
		super(msg);
	}

}
