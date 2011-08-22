package au.org.theark.core.web.component.customfield.dataentry;

import java.util.Date;

import org.apache.wicket.extensions.markup.html.form.DateTextField;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.util.convert.IConverter;
import org.apache.wicket.validation.IValidator;

import au.org.theark.core.web.component.ArkDatePicker;

/**
 * DateTextField based Data entry panel for dates
 */
public class DateDataEntryPanel extends AbstractDataEntryPanel<Date> {
	
	private static final long	serialVersionUID	= 1L;

	protected DateTextField dataValueDateFld;
	/**
	 * DateDataEntryPanel Constructor
	 * @param id - component id
	 * @param dateDataModel - must be a model for a Date dataValue
	 * @param labelModel - field-specific String label model to be used for feedback
	 */
	public DateDataEntryPanel(String id, IModel<Date> dateDataModel, IModel<String> labelModel) {
		super(id, labelModel);
		dataValueModel = dateDataModel;

		dataValueDateFld = new DateTextField("dateDataValue", dataValueModel, au.org.theark.core.Constants.DD_MM_YYYY);
		ArkDatePicker datePicker = new ArkDatePicker();
		datePicker.bind(dataValueDateFld);
		dataValueDateFld.setLabel(fieldLabelModel);	// set the ${label} for feedback messages
		dataValueDateFld.add(datePicker);

		this.add(dataValueDateFld);
	}
	
	public IConverter getDateConverter() {
		return dataValueDateFld.getConverter(dataValueDateFld.getType());
	}
	
	@Override
	public void setRequired(boolean required) {
		dataValueDateFld.setRequired(required);
	}
	
	@Override
	public void addValidator(IValidator<Date> aValidator) {
		dataValueDateFld.add(aValidator);
	}

	@Override
	protected DataEntryType getDataEntryType() {
		return DataEntryType.DATE;
	}

}
