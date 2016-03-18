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

public class MissingValueDateRangeValidator extends AbstractFormValidator {

	private static final long serialVersionUID = 1L;
	/** form components to be checked. */
	private final DateTextField[] componentDates;

	private final TextField[] componentTexts;

	private String labComp1;

	private String labComp2;

	private String labComp3;

	SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

	public MissingValueDateRangeValidator(DateTextField dateValidFrom,DateTextField dateValidTo, TextField missingvValueDate,String lableComponent1, String lableComponent2,String lableComponent3) {
		componentDates = new DateTextField[] { dateValidFrom, dateValidTo };
		componentTexts = new TextField[] { missingvValueDate };
		labComp1 = lableComponent1;
		labComp2 = lableComponent2;
		labComp3 = lableComponent3;
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
		if (missingValueString!=null && !missingValueString.isEmpty() && isValidDate(missingValueString)) {
			if (endDate != null && startDate != null) {
				Date convertDateMissing;
				try {
					convertDateMissing = dateFormat.parse(missingValueString);
					if (convertDateMissing.after(startDate)&& convertDateMissing.before(endDate)) {
						ValidationError ve = new ValidationError();
						ve.setVariable("startDate", labComp1);
						ve.setVariable("endDate", labComp2);
						ve.setVariable("missingvValueDate", labComp3);
						ve.addMessageKey("missingValueDateValidate.range");
						componentTexts[0].error((IValidationError) ve);
					}
				} catch (ParseException e) {
					e.printStackTrace();
				}
			}
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
