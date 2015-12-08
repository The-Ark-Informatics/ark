package au.org.theark.core.vo;

import java.io.Serializable;

import au.org.theark.core.model.pheno.entity.PhenoDataSetCategory;

public class PhenoDataSetCategoryVO implements Serializable {
	
	private static final long serialVersionUID = 1L;
		
	private PhenoDataSetCategory  phenoDataSetCategory;
	
	public PhenoDataSetCategoryVO(){
		phenoDataSetCategory=new PhenoDataSetCategory();
	}

	public PhenoDataSetCategory getPhenoDataSetCategory() {
		return phenoDataSetCategory;
	}

	public void setPhenoDataSetCategory(PhenoDataSetCategory phenoDataSetCategory) {
		this.phenoDataSetCategory = phenoDataSetCategory;
	}

	

	
	
}
