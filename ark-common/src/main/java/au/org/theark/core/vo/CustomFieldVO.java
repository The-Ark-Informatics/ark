/**
 * 
 * This is a new file
 *
 *
 */
package au.org.theark.core.vo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import au.org.theark.core.model.study.entity.CustomField;
import au.org.theark.core.model.study.entity.CustomFieldDisplay;
import au.org.theark.core.model.study.entity.SubjectCustmFld;

/**
 * @author nivedann
 * 
 */
public class CustomFieldVO implements Serializable {

	private SubjectCustmFld	subjectCustomField;//Old one will remove it (NN)
	
	private CustomField customField;
	private CustomFieldDisplay customFieldDisplay;
	
	protected boolean useCustomFieldDisplay;	// Flags whether or not CustomFieldDisplay should be saved, etc
	
	public CustomFieldVO() {
		super();
		subjectCustomField = new SubjectCustmFld();
		customField = new CustomField();
		customFieldDisplay = new CustomFieldDisplay();
		useCustomFieldDisplay = false;
	}

	public SubjectCustmFld getSubjectCustomField() {
		return subjectCustomField;
	}

	public void setSubjectCustomField(SubjectCustmFld subjectCustomField) {
		this.subjectCustomField = subjectCustomField;
	}

	public CustomField getCustomField() {
		return customField;
	}

	public void setCustomField(CustomField customField) {
		this.customField = customField;
	}

	public CustomFieldDisplay getCustomFieldDisplay() {
		return customFieldDisplay;
	}

	public void setCustomFieldDisplay(CustomFieldDisplay customFieldDisplay) {
		this.customFieldDisplay = customFieldDisplay;
	}

	public boolean isUseCustomFieldDisplay() {
		return useCustomFieldDisplay;
	}

	public void setUseCustomFieldDisplay(boolean useCustomFieldDisplay) {
		this.useCustomFieldDisplay = useCustomFieldDisplay;
	}

}
