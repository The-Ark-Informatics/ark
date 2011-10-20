package au.org.theark.core.vo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;

import au.org.theark.core.model.study.entity.CustomField;
import au.org.theark.core.model.study.entity.CustomFieldDisplay;
import au.org.theark.core.model.study.entity.CustomFieldGroup;

/**
 * @author nivedann
 *
 */
public class CustomFieldGroupVO implements Serializable{
	
	private CustomFieldGroup customFieldGroup;
	private Collection<CustomField> selectedCustomFields;
	private Collection<CustomField> availableCustomFields;
	private CustomFieldDisplay customFieldDisplay;
	
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

	public Collection<CustomField> getSelectedCustomFields() {
		return selectedCustomFields;
	}

	public void setSelectedCustomFields(Collection<CustomField> selectedCustomFields) {
		this.selectedCustomFields = selectedCustomFields;
	}

	public Collection<CustomField> getAvailableCustomFields() {
		return availableCustomFields;
	}

	public void setAvailableCustomFields(Collection<CustomField> availableCustomFields) {
		this.availableCustomFields = availableCustomFields;
	}

	public CustomFieldDisplay getCustomFieldDisplay() {
		return customFieldDisplay;
	}

	public void setCustomFieldDisplay(CustomFieldDisplay customFieldDisplay) {
		this.customFieldDisplay = customFieldDisplay;
	}


}
