package au.org.theark.core.web.component.customfield.dataentry;

import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.form.validation.FormComponentFeedbackBorder;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

/**
 * TextField based Data entry panel for text
 */
public class TextDataEntryPanel extends Panel {
	
	private static final long	serialVersionUID	= 1L;
	protected TextField<String> dataValueTxtFld;
	protected IModel<String> dataValueModel;
	protected IModel<String> fieldLabelModel;

	/**
	 * TextDataEntryPanel Constructor
	 * @param id - component id
	 * @param dataModel - must be a model a String dataValue
	 * @param labelModel - field-specific String label model to be used for feedback
	 */
	public TextDataEntryPanel(String id, IModel<String> dataModel, IModel<String> labelModel) {
		super(id);
		dataValueModel = dataModel;
		if (labelModel != null) {
			fieldLabelModel = labelModel;
		}
		else {
			fieldLabelModel = new Model<String>("(unknown label for text field)");
		}
		
		dataValueTxtFld = new TextField<String>("textDataValue", dataValueModel);
		dataValueTxtFld.setLabel(fieldLabelModel);	// set the ${label} for feedback messages
		this.add(new FormComponentFeedbackBorder("border").add(dataValueTxtFld));
	}

	public void setRequired(boolean required) {
		dataValueTxtFld.setRequired(required);
	}
	
}
