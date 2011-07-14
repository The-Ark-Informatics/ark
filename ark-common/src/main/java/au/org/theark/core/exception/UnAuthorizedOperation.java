package au.org.theark.core.exception;

public class UnAuthorizedOperation extends ArkBaseException{

	private static final long serialVersionUID = 1L;
	
	public UnAuthorizedOperation(String errorMessage){
		super(errorMessage);
	}

}
