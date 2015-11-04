package au.org.theark.core.util;



import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.wicket.extensions.markup.html.form.DateTextField;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.markup.html.form.validation.AbstractFormValidator;
import org.apache.wicket.validation.IValidationError;
import org.apache.wicket.validation.ValidationError;

public class MissingValueDateRangeValidator  extends AbstractFormValidator{

	private static final long serialVersionUID = 1L;
	/** form components to be checked. */
    private final DateTextField[] components;
    
    private String labComp1;
    
    private String labComp2;
    
    private String labComp3;
    
	/**
	 * 
	 * @param dateValidFrom
	 * @param dateValidTo
	 */
    public MissingValueDateRangeValidator(DateTextField dateValidFrom, DateTextField dateValidTo,DateTextField missingvValueDate,
    		String lableComponent1,String  lableComponent2,String  lableComponent3) {
        components = new DateTextField[] { dateValidFrom, dateValidTo,missingvValueDate};
        labComp1=lableComponent1;
        labComp2=lableComponent2;
        labComp3=lableComponent3;
        
    }
	@Override
	public FormComponent<?>[] getDependentFormComponents() {
		return components;
	}

	@Override
	public void validate(Form<?> form) {
		Date startDate = (Date) components[0].getConvertedInput();
        Date endDate = (Date) components[1].getConvertedInput();
        Date missingValueDate = (Date) components[2].getConvertedInput();
        if(endDate!=null && startDate!=null && missingValueDate !=null){
	        if (missingValueDate.after(startDate) && missingValueDate.before(endDate)){
	        	ValidationError ve = new ValidationError();
	        	ve.setVariable("startDate",labComp1);
	        	ve.setVariable("endDate",labComp2);
	        	ve.setVariable("missingvValueDate",labComp3);
	        	ve.addMessageKey("missingValueDateValidate.range");
	        	components[2].error((IValidationError) ve);
	        }
       }
	}

}
