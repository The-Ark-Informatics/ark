package au.org.theark.core.vo;

import java.io.Serializable;

import au.org.theark.core.model.study.entity.CustomFieldCategory;
import au.org.theark.core.model.study.entity.FamilyCustomFieldData;


public class FamilyCustomDataVO extends StudyCustomDataVo<FamilyCustomFieldData> implements Serializable {

	
	private static final long serialVersionUID = 1L;
	
	private FamilyCustomFieldData familyCustomFieldData;
	
	private CustomFieldCategory customFieldCategory;

	private String familyUId;

	public FamilyCustomDataVO() {
		super();
		familyCustomFieldData=new FamilyCustomFieldData();
		
	}
	
	public FamilyCustomFieldData getFamilyCustomFieldData() {
		return familyCustomFieldData;
	}


	public void setFamilyCustomFieldData(FamilyCustomFieldData familyCustomFieldData) {
		this.familyCustomFieldData = familyCustomFieldData;
	}

	public String getFamilyUId() {
		return familyUId;
	}

	public void setFamilyUId(String familyUId) {
		this.familyUId = familyUId;
	}
	
	public CustomFieldCategory getCustomFieldCategory() {
		return customFieldCategory;
	}

	public void setCustomFieldCategory(CustomFieldCategory customFieldCategory) {
		this.customFieldCategory = customFieldCategory;
	}
	
}
