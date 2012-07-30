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
	

	private static final long serialVersionUID = 1L;
	private CustomFieldGroup customFieldGroup;
	private ArrayList<CustomField> selectedCustomFields;
	private Collection<CustomField> availableCustomFields;
	private CustomFieldDisplay customFieldDisplay;
	private String		customFieldFileUploadField;
	


	
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

	public ArrayList<CustomField> getSelectedCustomFields() {
		return selectedCustomFields;
	}

	public void setSelectedCustomFields(ArrayList<CustomField> selectedCustomFields) {
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
	
	public String getcustomFieldFileUploadField() {
		return customFieldFileUploadField;
	}
	
	public void setcustomFieldFileUploadField(String customFieldFileUploadField) {
		this.customFieldFileUploadField = customFieldFileUploadField;
	}
}
