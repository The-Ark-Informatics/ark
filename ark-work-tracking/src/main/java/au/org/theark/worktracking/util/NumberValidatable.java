package au.org.theark.worktracking.util;

import org.apache.wicket.model.IModel;
import org.apache.wicket.validation.IValidatable;
import org.apache.wicket.validation.IValidationError;
import org.apache.wicket.validation.ValidationError;
import org.apache.wicket.validation.validator.PatternValidator;

/**
 * Convert input number to String and set the validate error messages.  
 * @author thilina
 * 
 * @see IValidatable
 * @see PatternValidator
 *
 */
public class NumberValidatable implements IValidatable<String> {
	private IValidatable<String> validatable;
	private ValidatableItemType itemType;
	
	public NumberValidatable(IValidatable<String> validatable,ValidatableItemType itemType){
		this.validatable=validatable;
		this.itemType=itemType;
	}
	
	public String getValue() {
		Object obj=this.validatable.getValue();
		return obj.toString();
	}

	public void error(IValidationError error) {
		ValidationError validationError = (ValidationError) error;
		switch(itemType){
			case UNIT_PRICE:
				this.validatable.error(validationError.addMessageKey(Constants.ERROR_BILLABLE_ITEM_TYPE_UNIT_PRICE));
				break;
			case GST:
				this.validatable.error(validationError.addMessageKey(Constants.ERROR_WORK_REQUEST_GST));
				break;
			case UNIT_QUANTITY:
				this.validatable.error(validationError.addMessageKey(Constants.ERROR_BILLABLE_ITEM_QUANTITY));
				break;
			case BSB:
				this.validatable.error(validationError.addMessageKey(Constants.ERROR_WORK_RESEARCHER_BSB_FORMAT));
				break;
			case ACCOUNT_NUMBER:
				this.validatable.error(validationError.addMessageKey(Constants.ERROR_WORK_RESEARCHER_ACCOUNTNUMBER_FORMAT));
				break;
			case PHONE_NUMBER:
				this.validatable.error(validationError.addMessageKey(Constants.ERROR_WORK_RESEARCHER_OFFICEPHONE_FORMAT));
				break;
			case MOBILE_NUMBER:
				this.validatable.error(validationError.addMessageKey(Constants.ERROR_WORK_RESEARCHER_MOBILEPHONE_FORMAT));
				break;
			case FAX_NUMBER:
				this.validatable.error(validationError.addMessageKey(Constants.ERROR_WORK_RESEARCHER_FAX_FORMAT));
				break;	
		}
	}

	public boolean isValid() {
		// TODO Auto-generated method stub
		return this.validatable.isValid();
	}

	public IModel<String> getModel() {
		// TODO Auto-generated method stub
		return this.validatable.getModel();
	}

}
