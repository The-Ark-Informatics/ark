/**
 * 
 * This is a new file
 *
 *
 */
package au.org.theark.core.vo;

import java.io.Serializable;

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

	public CustomFieldVO() {
		super();
		subjectCustomField = new SubjectCustmFld();
		customField = new CustomField();
		customFieldDisplay = new CustomFieldDisplay();
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

	

}
