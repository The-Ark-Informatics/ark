package au.org.theark.core.vo;

import java.io.Serializable;

import au.org.theark.core.model.pheno.entity.PhenoData;
import au.org.theark.core.model.pheno.entity.PhenotypicCollection;
import au.org.theark.core.model.study.entity.CustomFieldGroup;
import au.org.theark.core.web.component.customfield.dataentry.CustomDataVO;

public class PhenoDataCollectionVO extends CustomDataVO<PhenoData> implements Serializable {

	private static final long	serialVersionUID	= 1L;
	
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
