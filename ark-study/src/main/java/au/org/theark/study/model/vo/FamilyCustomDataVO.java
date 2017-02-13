package au.org.theark.study.model.vo;

import java.io.Serializable;

import au.org.theark.core.model.study.entity.FamilyCustomFieldData;
import au.org.theark.core.vo.StudyCustomDataVo;


public class FamilyCustomDataVO extends StudyCustomDataVo<FamilyCustomFieldData> implements Serializable {

	
	private static final long serialVersionUID = 1L;
	
	private FamilyCustomFieldData familyCustomFieldData;

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
	
}
