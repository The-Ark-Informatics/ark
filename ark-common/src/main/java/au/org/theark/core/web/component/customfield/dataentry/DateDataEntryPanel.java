package au.org.theark.core.web.component.customfield.dataentry;

import java.util.Date;

import org.apache.wicket.extensions.markup.html.form.DateTextField;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.validation.FormComponentFeedbackBorder;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.util.convert.IConverter;
import org.apache.wicket.validation.IValidator;

import au.org.theark.core.web.component.ArkDatePicker;

/**
 * DateTextField based Data entry panel for dates
 */
public class DateDataEntryPanel extends Panel {
	
	private static final long	serialVersionUID	= 1L;

	protected IModel<Date> dateDataValueModel;
	protected IModel<String> badDataValueModel;
	protected IModel<String> fieldLabelModel;

	protected DateTextField dataValueDateFld;
	protected Label badValueLbl;

	/**
	 * DateDataEntryPanel Constructor
	 * @param id - component id
	 * @param dateDataModel - must be a model for a Date dataValue
	 * @param labelModel - field-specific String label model to be used for feedback
	 */
	public DateDataEntryPanel(String id, IModel<Date> dateDataModel, IModel<String> labelModel) {
		super(id);

		dateDataValueModel = dateDataModel;
		badDataValueModel = new Model<String>("");
		if (labelModel != null) {
			fieldLabelModel = labelModel;
		}
		else {
			fieldLabelModel = new Model<String>("(unspecified label for date field)");
		}
		
		dataValueDateFld = new DateTextField("dateDataValue", dateDataValueModel, au.org.theark.core.Constants.DD_MM_YYYY);
		ArkDatePicker datePicker = new ArkDatePicker();
		datePicker.bind(dataValueDateFld);
		dataValueDateFld.setLabel(fieldLabelModel);	// set the ${label} for feedback messages
		dataValueDateFld.add(datePicker);

		badValueLbl = new Label("badValueLabel", badDataValueModel);
		badValueLbl.setOutputMarkupId(true);
		
		this.add(new FormComponentFeedbackBorder("border").add(dataValueDateFld));
		this.add(badValueLbl);
	}
	
	public void setRequired(boolean required) {
		dataValueDateFld.setRequired(required);
	}
	
	public void setBadDateStringModel(IModel<String> aBadDateStringModel) {
		if (aBadDateStringModel != null 
				&& aBadDateStringModel.getObject() != null && !aBadDateStringModel.getObject().isEmpty()) {
			badDataValueModel = new StringResourceModel("badValueLabel.invalidDate", this, 
																		null, new Object[] { aBadDateStringModel.getObject() });
		}
		else {
			badDataValueModel = new Model<String>("");
		}
	}
	
	public void addValidator(IValidator<Date> aValidator) {
		dataValueDateFld.add(aValidator);
	}
	
	public IConverter getDateConverter() {
		return dataValueDateFld.getConverter(dataValueDateFld.getType());
	}
	
}
