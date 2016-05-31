package au.org.theark.core.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.lang.StringUtils;
import org.apache.wicket.extensions.markup.html.form.DateTextField;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.form.validation.AbstractFormValidator;
import org.apache.wicket.validation.IValidationError;
import org.apache.wicket.validation.ValidationError;

public class DefaultMissingValueDateRangeValidator extends AbstractFormValidator {

	private static final long serialVersionUID = 1L;
	/** form components to be checked. */
	private final DateTextField[] componentDates;

	private final TextField[] componentTexts;

	private String labComp1;

	private String labComp2;

	private String labComp3;
	
	private String labComp4;

	SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

	public DefaultMissingValueDateRangeValidator(DateTextField dateValidFrom,DateTextField dateValidTo, TextField missingvValueDate,TextField defaultValueDate,
			String lableComponent1, String lableComponent2,String lableComponent3,String lableComponent4) {
		componentDates = new DateTextField[] { dateValidFrom, dateValidTo };
		componentTexts = new TextField[] { missingvValueDate,defaultValueDate };
		labComp1 = lableComponent1;
		labComp2 = lableComponent2;
		labComp3 = lableComponent3;
		labComp4= lableComponent4;
	}

	@Override
	public FormComponent<?>[] getDependentFormComponents() {
		return componentDates;
	}

	@Override
	public void validate(Form<?> form) {
		Date startDate = (Date) componentDates[0].getConvertedInput();
		Date endDate = (Date) componentDates[1].getConvertedInput();
		String missingValueString = (String) componentTexts[0].getConvertedInput();
		String defaultValueString = (String) componentTexts[1].getConvertedInput();
		//Missing date validation
		if (missingValueString!=null && !missingValueString.isEmpty() && isValidDate(missingValueString)) {
			if (endDate != null && startDate != null) {
				Date convertDateMissing;
				try {
					convertDateMissing = dateFormat.parse(missingValueString);
					if (convertDateMissing.after(startDate)&& convertDateMissing.before(endDate)) {
						ValidationError ve1 = new ValidationError();
						ve1.setVariable("startDate", labComp1);
						ve1.setVariable("endDate", labComp2);
						ve1.setVariable("missingvValueDate", labComp3);
						ve1.addMessageKey("missingValueDateValidate.range");
						componentTexts[0].error((IValidationError) ve1);
					}
				} catch (ParseException e) {
					e.printStackTrace();
				}
			}
		}
		//Default Date(valid date) but range validation
		if (defaultValueString!=null && !defaultValueString.isEmpty() && isValidDate(defaultValueString)) {
			if (endDate != null && startDate != null) {
				Date convertDateDefault;
				try {
					convertDateDefault = dateFormat.parse(defaultValueString);
					if (convertDateDefault.before(startDate) || convertDateDefault.after(endDate)) {
						ValidationError ve2 = new ValidationError();
						ve2.setVariable("startDate", labComp1);
						ve2.setVariable("endDate", labComp2);
						ve2.setVariable("defaultValueDate", labComp4);
						ve2.addMessageKey("defaultValueDateValidate.range");
						componentTexts[0].error((IValidationError) ve2);
					}
				} catch (ParseException e) {
					e.printStackTrace();
				}
			}
		}
		// Default valid data validation 
		if(defaultValueString!=null && !defaultValueString.isEmpty() && !isValidDate(defaultValueString)){
			ValidationError ve3 = new ValidationError();
			ve3.setVariable("defaultValueDate", labComp4);
			ve3.addMessageKey("defaultValueDateValidate.NotValidDate");
			componentTexts[0].error((IValidationError) ve3);
		}
		
	}

	private boolean isValidDate(String inDate) {
		if (inDate == null)
			return false;
		// set the format to use as a constructor argument
		if (inDate.trim().length() != dateFormat.toPattern().length())
			return false;
		dateFormat.setLenient(false);
		try {
			// parse the inDate parameter
			dateFormat.parse(inDate.trim());
		} catch (ParseException pe) {
			return false;
		}
		return true;
	}

}
