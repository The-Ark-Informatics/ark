package au.org.theark.core.vo;

import java.io.Serializable;

import au.org.theark.core.model.study.entity.CustomFieldCategory;

public class CustomFieldCategoryVO implements Serializable {
	
	private static final long serialVersionUID = 1L;
		
	private CustomFieldCategory  customFieldCategory;
	
	public CustomFieldCategoryVO(){
		customFieldCategory=new CustomFieldCategory();
	}

	public CustomFieldCategory getCustomFieldCategory() {
		return customFieldCategory;
	}

	public void setCustomFieldCategory(CustomFieldCategory customFieldCategory) {
		this.customFieldCategory = customFieldCategory;
	}
	
}
