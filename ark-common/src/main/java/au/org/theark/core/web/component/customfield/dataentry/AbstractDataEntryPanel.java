package au.org.theark.core.web.component.customfield.dataentry;

import java.util.HashMap;
import java.util.Map;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.validation.IValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import au.org.theark.core.web.component.customfield.CustomFieldContainerPanel;

public abstract class AbstractDataEntryPanel<T> extends Panel {

	/**
	 * 
	 */
	private static final long	serialVersionUID	= 1L;
	
	private static final Logger log = LoggerFactory.getLogger(AbstractDataEntryPanel.class);

	protected IModel<T> dataValueModel;
	protected IModel<String> errorDataValueModel;
	protected IModel<String> fieldLabelModel;
	protected Label errorValueLbl;
	
	private static final String DATE_VALIDATION_ERROR_RSRC_KEY = "validationError.dateType";
	private static final String NUMBER_VALIDATION_ERROR_RSRC_KEY = "validationError.numberType";
	private static final String TEXT_VALIDATION_ERROR_RSRC_KEY = "validationError.textType";
	private static final String DROPDOWN_VALIDATION_ERROR_RSRC_KEY = "validationError.dropDownType";
	
	public enum DataEntryType {
		TEXT, DROPDOWN, NUMBER, DATE
	}

	public AbstractDataEntryPanel(String id, IModel<String> labelModel) {
		super(id);

		if (labelModel != null) {
			fieldLabelModel = labelModel;
		}
		else {
			fieldLabelModel = new Model<String>("(unspecified label for date field)");
		}
		
		errorDataValueModel = new Model<String>("");
	}

	public void setErrorDataValueModel(IModel<String> errorDataStringModel) {
		if (errorDataStringModel != null 
				&& errorDataStringModel.getObject() != null && !errorDataStringModel.getObject().isEmpty()) {
			errorDataValueModel = new StringResourceModel(getValidationErrorResourceKey(), this, 
																		null, new Object[] { errorDataValueModel.getObject() });
		}
		else {
			errorDataValueModel = new Model<String>("");
		}
	}

	protected String getValidationErrorResourceKey() {
		String typeRsrcKey = null;
		switch(getDataEntryType()) {
			case TEXT : typeRsrcKey = TEXT_VALIDATION_ERROR_RSRC_KEY;
							break;
			case NUMBER : typeRsrcKey = NUMBER_VALIDATION_ERROR_RSRC_KEY;
							break;
			case DATE : typeRsrcKey = DATE_VALIDATION_ERROR_RSRC_KEY;
							break;
			case DROPDOWN : typeRsrcKey = DROPDOWN_VALIDATION_ERROR_RSRC_KEY;
							break;
			default : log.error("Internal error: Code did not map all DataEntryType enums to resource keys");
							typeRsrcKey = TEXT_VALIDATION_ERROR_RSRC_KEY;	//use least restrictive type by default
							break;
		}
		return typeRsrcKey;
	}
	
	abstract public void setRequired(boolean required);

	abstract public void addValidator(IValidator<T> aValidator);
	
	/**
	 * Override this to provide the error label with a type name to display
	 */
	abstract protected DataEntryType getDataEntryType();
	
}

