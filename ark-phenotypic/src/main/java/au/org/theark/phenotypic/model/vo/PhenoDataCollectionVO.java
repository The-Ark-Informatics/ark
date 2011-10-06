package au.org.theark.phenotypic.model.vo;

import au.org.theark.core.model.pheno.entity.PhenotypicCollection;
import au.org.theark.core.model.study.entity.CustomFieldGroup;

public class PhenoDataCollectionVO {
	
	private CustomFieldGroup customFieldGroup;
	private PhenotypicCollection phenotypicCollection;
	
	public PhenoDataCollectionVO() {
		customFieldGroup = new CustomFieldGroup();
		phenotypicCollection = new PhenotypicCollection();
	}
	
	public CustomFieldGroup getCustomFieldGroup() {
		return customFieldGroup;
	}
	public void setCustomFieldGroup(CustomFieldGroup customFieldGroup) {
		this.customFieldGroup = customFieldGroup;
	}
	public PhenotypicCollection getPhenotypicCollection() {
		return phenotypicCollection;
	}
	public void setPhenotypicCollection(PhenotypicCollection phenotypicCollection) {
		this.phenotypicCollection = phenotypicCollection;
	}
	
}
