package au.org.theark.core.util;


import org.springframework.stereotype.Component;

import au.org.theark.core.Constants;

@Component(Constants.VALIDATION_MESSAGES)
public class ValidationMessages {

	public String save(){
		return Constants.VALIDATION_CREATE;
	}
	
	public String update(){
		return Constants.VALIDATION_UPDATE;
	}
	
	public String delete(){
		return Constants.VALIDATION_DELETE;
	}
}
