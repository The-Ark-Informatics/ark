package au.org.theark.core.util;



import org.apache.wicket.datetime.markup.html.form.DateTextField;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.markup.html.form.validation.AbstractFormValidator;
import org.apache.wicket.validation.IValidationError;
import org.apache.wicket.validation.ValidationError;

import java.util.Date;

public class DateFromToValidator  extends AbstractFormValidator{

	private static final long serialVersionUID = 1L;
	/** form components to be checked. */
    private final DateTextField[] components;
    
    private String labComp1;
    
    private String labComp2;
    
	/**
	 * 
	 * @param dateValidFrom
	 * @param dateValidTo
	 */
    public DateFromToValidator(DateTextField dateValidFrom, DateTextField dateValidTo,String lableComponent1,String  lableComponent2) {
        components = new DateTextField[] { dateValidFrom, dateValidTo};
        labComp1=lableComponent1;
        labComp2=lableComponent2;
    }
	@Override
	public FormComponent<?>[] getDependentFormComponents() {
		return components;
	}

	@Override
	public void validate(Form<?> form) {
		Date startDate = (Date) components[0].getConvertedInput();
        Date endDate = (Date) components[1].getConvertedInput();
        if(endDate!=null && startDate!=null){
	        if (endDate.before(startDate)){
	        	ValidationError ve = new ValidationError();
	        	ve.setVariable("startDate",labComp1);
	        	ve.setVariable("endDate",labComp2);
	        	ve.addKey("dateValidFromAndDatevalidTo.range");
	        	components[0].error((IValidationError) ve);
	        }
       }
	}

}
