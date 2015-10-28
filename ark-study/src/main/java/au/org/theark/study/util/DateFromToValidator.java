package au.org.theark.study.util;

import java.util.Date;

import org.apache.wicket.extensions.markup.html.form.DateTextField;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.markup.html.form.validation.AbstractFormValidator;
import org.apache.wicket.validation.IValidationError;
import org.apache.wicket.validation.ValidationError;

public class DateFromToValidator  extends AbstractFormValidator{

	private static final long serialVersionUID = 1L;
	/** form components to be checked. */
    private final DateTextField[] components;
	/**
	 * 
	 * @param dateValidFrom
	 * @param dateValidTo
	 */
    public DateFromToValidator(DateTextField dateValidFrom, DateTextField dateValidTo) {
        components = new DateTextField[] { dateValidFrom, dateValidTo };
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
	        	ve.setVariables(DateFromToValidator.this.variablesMap());
	        	ve.addMessageKey("dateValidFromAndDatevalidTo.range");
	        	components[0].error((IValidationError) ve);
	        }
       }
	}

}
