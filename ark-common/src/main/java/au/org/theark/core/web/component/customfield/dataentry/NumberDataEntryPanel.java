package au.org.theark.core.web.component.customfield.dataentry;

import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.util.convert.IConverter;
import org.apache.wicket.validation.IValidator;

/**
 * TextField based Data entry panel for numbers
 */
public class NumberDataEntryPanel extends AbstractDataEntryPanel<Double> {
	
	private static final long	serialVersionUID	= 1L;
	protected TextField<Double> dataValueTxtFld;

	/**
	 * NumberDataEntryPanel Constructor
	 * @param id - component id
	 * @param dataModel - must be a model Double dataValue
	 * @param labelModel - field-specific String label model to be used for feedback
	 */
	public NumberDataEntryPanel(String id, IModel<Double> dataModel, IModel<String> labelModel) {
		super(id, labelModel);
		dataValueModel = dataModel;
		
		dataValueTxtFld = new TextField<Double>("numberDataValue", dataValueModel, Double.class);
		dataValueTxtFld.setLabel(fieldLabelModel);	// set the ${label} for feedback messages
		this.add(dataValueTxtFld);
	}
	
	public IConverter getNumberConverter() {
		return dataValueTxtFld.getConverter(dataValueTxtFld.getType());
	}

	@Override
	public void setRequired(boolean required) {
		dataValueTxtFld.setRequired(required);
	}
	
	@Override
	public void addValidator(IValidator<Double> aValidator) {
		dataValueTxtFld.add(aValidator);
	}

	@Override
	protected DataEntryType getDataEntryType() {
		return DataEntryType.NUMBER;
	}

}
