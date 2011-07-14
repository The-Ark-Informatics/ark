package au.org.theark.lims.web.component.subjectLims.subject.form;

import java.util.Calendar;

import org.apache.wicket.validation.IValidatable;
import org.apache.wicket.validation.IValidator;
import org.apache.wicket.validation.ValidationError;

public class SubjectValidator implements IValidator<Long>  {

	/* (non-Javadoc)
	 * @see org.apache.wicket.validation.IValidator#validate(org.apache.wicket.validation.IValidatable)
	 */
	public void validate(IValidatable<Long> arg0) {
		// TODO Auto-generated method stub
		if(arg0.getValue() != null ){
			Calendar calendar = Calendar.getInstance();
			int calYear = calendar.get(Calendar.YEAR);
			if(arg0.getValue() > calYear){
				
				ValidationError ve = new ValidationError().addMessageKey("error.found");
				//ve.setVariables(vars);
			}
		}
		
	}




}
