package au.org.theark.phenotypic.exception;

import au.org.theark.core.exception.ArkSystemException;

@SuppressWarnings("serial")
public class PhenotypicSystemException extends ArkSystemException {
	
	public PhenotypicSystemException(){
		super();
	}
	public PhenotypicSystemException(String message){ 
		super(message);
	}
}
