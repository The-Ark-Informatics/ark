package au.org.theark.core.vo;

import java.util.List;

import au.org.theark.core.model.pheno.entity.PhenoDataSetCategory;
import au.org.theark.core.model.pheno.entity.PhenoDataSetField;
import au.org.theark.core.model.pheno.entity.PhenoDataSetFieldDisplay;
import au.org.theark.core.model.study.entity.CustomField;
import au.org.theark.core.model.study.entity.CustomFieldCategory;
import au.org.theark.core.model.study.entity.CustomFieldDisplay;

public class PhenoDataSetFieldVO {
	
	private static final long serialVersionUID = 1L;
	private PhenoDataSetField phenoDataSetField;
	private PhenoDataSetFieldDisplay phenoDataSetFieldDisplay;
	//please remove not needed unless used for phenoDataSetCategories.
	protected List<PhenoDataSetCategory>			phenoDataSetFieldCategoryLst;
	protected boolean usePhenoDataSetFieldDisplay;	// Flags whether or not CustomFieldDisplay should be saved, etc
	//Below fields used for testing purpose only.
	private List<PhenoDataSetCategory>				selectedCategories;
	private List<PhenoDataSetCategory>  			firstLevelAvailableCategories;
	private List<PhenoDataSetCategory>  			firstLevelSelectedCategories;
	private List<PhenoDataSetCategory>  			lastLevelAvailableCategories;
	private List<PhenoDataSetCategory>  			lastLevelSelectedCategories;
	
	
	public PhenoDataSetField getPhenoDataSetField() {
		return phenoDataSetField;
	}
	public void setPhenoDataSetField(PhenoDataSetField phenoDataSetField) {
		this.phenoDataSetField = phenoDataSetField;
	}
	public PhenoDataSetFieldDisplay getPhenoDataSetFieldDisplay() {
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
	}
	public List<PhenoDataSetCategory> getSelectedCategories() {
		return selectedCategories;
	}
	public void setSelectedCategories(List<PhenoDataSetCategory> selectedCategories) {
		this.selectedCategories = selectedCategories;
	}
	public List<PhenoDataSetCategory> getFirstLevelAvailableCategories() {
		return firstLevelAvailableCategories;
	}
	public void setFirstLevelAvailableCategories(
			List<PhenoDataSetCategory> firstLevelAvailableCategories) {
		this.firstLevelAvailableCategories = firstLevelAvailableCategories;
	}
	public List<PhenoDataSetCategory> getFirstLevelSelectedCategories() {
		return firstLevelSelectedCategories;
	}
	public void setFirstLevelSelectedCategories(
			List<PhenoDataSetCategory> firstLevelSelectedCategories) {
		this.firstLevelSelectedCategories = firstLevelSelectedCategories;
	}
	public List<PhenoDataSetCategory> getLastLevelAvailableCategories() {
		return lastLevelAvailableCategories;
	}
	public void setLastLevelAvailableCategories(
			List<PhenoDataSetCategory> lastLevelAvailableCategories) {
		this.lastLevelAvailableCategories = lastLevelAvailableCategories;
	}
	public List<PhenoDataSetCategory> getLastLevelSelectedCategories() {
		return lastLevelSelectedCategories;
	}
	public void setLastLevelSelectedCategories(
			List<PhenoDataSetCategory> lastLevelSelectedCategories) {
		this.lastLevelSelectedCategories = lastLevelSelectedCategories;
	}
	

}
