package au.org.theark.core.web.component.customfield.dataentry;

import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.form.validation.FormComponentFeedbackBorder;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.validation.IValidator;

/**
 * TextField based Data entry panel for text
 */
public class TextDataEntryPanel extends AbstractDataEntryPanel<String> {
	
	private static final long	serialVersionUID	= 1L;
	protected TextField<String> dataValueTxtFld;

	/**
	 * TextDataEntryPanel Constructor
	 * @param id - component id
	 * @param dataModel - must be a model a String dataValue
	 * @param labelModel - field-specific String label model to be used for feedback
	 */
	public TextDataEntryPanel(String id, IModel<String> dataModel, IModel<String> labelModel) {
		super(id, labelModel);
		dataValueModel = dataModel;
		
		dataValueTxtFld = new TextField<String>("textDataValue", dataValueModel);
		dataValueTxtFld.setLabel(fieldLabelModel);	// set the ${label} for feedback messages
		this.add(dataValueTxtFld);
	}

	@Override
	public void setRequired(boolean required) {
		dataValueTxtFld.setRequired(required);
	}

	@Override
	public void addValidator(IValidator<String> aValidator) {
		dataValueTxtFld.add(aValidator);
	}

	@Override
	protected DataEntryType getDataEntryType() {
		return DataEntryType.TEXT;
	}
	
}
