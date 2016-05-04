package au.org.theark.core.vo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import au.org.theark.core.model.pheno.entity.PhenoDataSetCategory;
import au.org.theark.core.model.pheno.entity.PhenoDataSetField;
import au.org.theark.core.model.pheno.entity.PhenoDataSetFieldDisplay;

public class PhenoDataSetFieldVO implements Serializable {
	
	private static final long serialVersionUID = 1L;
	private PhenoDataSetField phenoDataSetField;
	/*private PhenoDataSetFieldDisplay phenoDataSetFieldDisplay;
	protected List<PhenoDataSetCategory>			phenoDataSetFieldCategoryLst;
	protected boolean usePhenoDataSetFieldDisplay;	*/
	
	public PhenoDataSetFieldVO() {
		super();
		phenoDataSetField = new PhenoDataSetField();
		/*phenoDataSetFieldDisplay = new PhenoDataSetFieldDisplay();
		phenoDataSetFieldCategoryLst=new ArrayList<PhenoDataSetCategory>();
		usePhenoDataSetFieldDisplay = false;*/
	}
	
	
	public PhenoDataSetField getPhenoDataSetField() {
		return phenoDataSetField;
	}
	public void setPhenoDataSetField(PhenoDataSetField phenoDataSetField) {
		this.phenoDataSetField = phenoDataSetField;
	}
	/*public PhenoDataSetFieldDisplay getPhenoDataSetFieldDisplay() {
		return phenoDataSetFieldDisplay;
	}
	public void setPhenoDataSetFieldDisplay(
			PhenoDataSetFieldDisplay phenoDataSetFieldDisplay) {
		this.phenoDataSetFieldDisplay = phenoDataSetFieldDisplay;
	}
	public List<PhenoDataSetCategory> getPhenoDataSetFieldCategoryLst() {
		return phenoDataSetFieldCategoryLst;
	}
	public void setPhenoDataSetFieldCategoryLst(
			List<PhenoDataSetCategory> phenoDataSetFieldCategoryLst) {
		this.phenoDataSetFieldCategoryLst = phenoDataSetFieldCategoryLst;
	}
	public boolean isUsePhenoDataSetFieldDisplay() {
		return usePhenoDataSetFieldDisplay;
	}
	public void setUsePhenoDataSetFieldDisplay(boolean usePhenoDataSetFieldDisplay) {
		this.usePhenoDataSetFieldDisplay = usePhenoDataSetFieldDisplay;
	}*/
}
