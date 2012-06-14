package au.org.theark.core.vo;

import java.io.Serializable;

import au.org.theark.core.model.pheno.entity.PhenoData;
import au.org.theark.core.model.pheno.entity.PhenoCollection;
import au.org.theark.core.model.study.entity.CustomFieldGroup;
import au.org.theark.core.web.component.customfield.dataentry.CustomDataVO;

public class PhenoDataCollectionVO extends CustomDataVO<PhenoData> implements Serializable {

	private static final long	serialVersionUID	= 1L;

	private CustomFieldGroup customFieldGroup;
	private PhenoCollection phenoCollection;
	
	public PhenoDataCollectionVO() {
		customFieldGroup = new CustomFieldGroup();
		phenoCollection = new PhenoCollection();
	}
	
	public CustomFieldGroup getCustomFieldGroup() {
		return customFieldGroup;
	}
	public void setCustomFieldGroup(CustomFieldGroup customFieldGroup) {
		this.customFieldGroup = customFieldGroup;
	}
	public PhenoCollection getPhenoCollection() {
		return phenoCollection;
	}
	public void setPhenoCollection(PhenoCollection phenoCollection) {
		this.phenoCollection = phenoCollection;
	}

}
