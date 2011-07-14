/**
 * 
 * This is a new file
 *
 *
 */
package au.org.theark.core.vo;

import java.io.Serializable;

import au.org.theark.core.model.study.entity.SubjectCustmFld;

/**
 * @author nivedann
 *
 */
public class CustomFieldVO implements Serializable{
	
	private SubjectCustmFld  customField;
	public CustomFieldVO(){
		super();
		customField = new SubjectCustmFld();
	}
	public SubjectCustmFld getCustomField() {
		return customField;
	}
	public void setCustomField(SubjectCustmFld customField) {
		this.customField = customField;
	}

}
