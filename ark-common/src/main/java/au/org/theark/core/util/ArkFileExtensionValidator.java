/*******************************************************************************
 * Copyright © The University of Melbourne and Peter MacCallum Cancer Centre - All Rights Reserved
 * Unauthorized copying of this file, via any medium, is strictly prohibited.
 * Proprietary and confidential.
 * Written by Sanjay Maddumarachchi <sanjaya.maddumarachchi@unimelb.edu.au>, May 2015
 *  
 *
 *******************************************************************************/
package au.org.theark.core.util;

import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.markup.html.form.upload.FileUpload;
import org.apache.wicket.validation.IValidatable;
import org.apache.wicket.validation.ValidationError;
import org.apache.wicket.validation.validator.AbstractValidator;

public class ArkFileExtensionValidator extends AbstractValidator {
	private static final long serialVersionUID = 1L;
	private String resourceKey;
	private String allowedFileExt;
	
	public ArkFileExtensionValidator(final String fileExt,final String resourceKey){
		this.resourceKey=resourceKey;
		this.allowedFileExt=fileExt;
	}
	public boolean validateOnNullValue(){
        return true;
	}
	@Override
	protected void onValidate(IValidatable validatable) {
		ValidationError error = new ValidationError();
		List files = (ArrayList) validatable.getValue();
		if(files !=null){
			for (Object obj : files){
				FileUpload file;
				file = FileUpload.class.cast(obj);
				String fileName = file.getClientFileName();
				String fileExt = fileName.substring(fileName.lastIndexOf("."));
				if (!allowedFileExt.equals(fileExt)){
					error.addMessageKey(resourceKey());
					error.setVariable("validExtension",allowedFileExt );
					validatable.error(error);
				}
	    	}
		}/*else{
			error.addMessageKey(resourceKey());
	        error.setVariable("validExtension",allowedFileExt );
	        validatable.error(error);
		}*/
	}
	@Override
	protected String resourceKey() {
		return this.resourceKey;
	}

}
