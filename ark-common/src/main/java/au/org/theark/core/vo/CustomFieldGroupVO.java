package au.org.theark.core.vo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import au.org.theark.core.model.study.entity.CustomField;
import au.org.theark.core.model.study.entity.CustomFieldGroup;

/**
 * @author nivedann
 *
 */
public class CustomFieldGroupVO implements Serializable{
	
	private CustomFieldGroup customFieldGroup;
	private List<CustomField> selectedCustomFields;
	private List<CustomField> availableCustomFields;
	
	/**
	 * Constructor
	 */
	public CustomFieldGroupVO(){
		
		customFieldGroup = new CustomFieldGroup();
		selectedCustomFields = new ArrayList<CustomField>();
		availableCustomFields = new ArrayList<CustomField>();
	}

	public CustomFieldGroup getCustomFieldGroup() {
		return customFieldGroup;
	}

	public void setCustomFieldGroup(CustomFieldGroup customFieldGroup) {
		this.customFieldGroup = customFieldGroup;
	}

	public List<CustomField> getSelectedCustomFields() {
		return selectedCustomFields;
	}

	public void setSelectedCustomFields(List<CustomField> selectedCustomFields) {
		this.selectedCustomFields = selectedCustomFields;
	}

	public List<CustomField> getAvailableCustomFields() {
		return availableCustomFields;
	}

	public void setAvailableCustomFields(List<CustomField> availableCustomFields) {
		this.availableCustomFields = availableCustomFields;
	}

}
