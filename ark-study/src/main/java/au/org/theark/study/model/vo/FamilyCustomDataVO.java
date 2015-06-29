package au.org.theark.study.model.vo;

import java.io.Serializable;
import au.org.theark.core.model.study.entity.FamilyCustomFieldData;


public class FamilyCustomDataVO extends StudyCustomDataVo<FamilyCustomFieldData> implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String familyId;

	public FamilyCustomDataVO() {
		super();		
	}

	public String getFamilyId() {
		return familyId;
	}

	public void setFamilyId(String familyId) {
		this.familyId = familyId;
	}
	
}
