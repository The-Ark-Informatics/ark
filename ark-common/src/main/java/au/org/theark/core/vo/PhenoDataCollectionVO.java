package au.org.theark.core.vo;

import java.io.Serializable;

import au.org.theark.core.model.pheno.entity.PhenoDataSetCollection;
import au.org.theark.core.model.pheno.entity.PhenoDataSetData;
import au.org.theark.core.model.pheno.entity.PhenoDataSetFieldDisplay;
import au.org.theark.core.model.pheno.entity.PhenoDataSetGroup;
import au.org.theark.core.model.pheno.entity.PickedPhenoDataSetCategory;


public class PhenoDataCollectionVO extends PhenoDataVO<PhenoDataSetData> implements Serializable {

	private static final long	serialVersionUID	= 1L;
	
	private PhenoDataSetGroup phenoDataSetGroup;
	private PhenoDataSetGroup pheDataSetGroupSelected;
	private PhenoDataSetCollection phenoDataSetCollection;
	private PickedPhenoDataSetCategory pickedPhenoDataSetCategory;
	private PhenoDataSetFieldDisplay phenoDataSetFieldDisplay;
	
	

	public PhenoDataCollectionVO() {
		phenoDataSetGroup = new PhenoDataSetGroup();
		phenoDataSetCollection = new PhenoDataSetCollection();
		pickedPhenoDataSetCategory=new PickedPhenoDataSetCategory();
		phenoDataSetFieldDisplay=new PhenoDataSetFieldDisplay();
	}
	
	public PhenoDataSetGroup getPhenoDataSetGroup() {
		return phenoDataSetGroup;
	}
	public void setPhenoDataSetGroup(PhenoDataSetGroup phenoDataSetGroup) {
		this.phenoDataSetGroup = phenoDataSetGroup;
	}
	public PhenoDataSetGroup getPheDataSetGroupSelected() {
		return pheDataSetGroupSelected;
	}
	public void setPheDataSetGroupSelected(PhenoDataSetGroup pheDataSetGroupSelected) {
		this.pheDataSetGroupSelected = pheDataSetGroupSelected;
	}
	public PhenoDataSetCollection getPhenoDataSetCollection() {
		return phenoDataSetCollection;
	}
	public void setPhenoDataSetCollection(
			PhenoDataSetCollection phenoDataSetCollection) {
		this.phenoDataSetCollection = phenoDataSetCollection;
	}
	public PickedPhenoDataSetCategory getPickedPhenoDataSetCategory() {
		return pickedPhenoDataSetCategory;
	}
	public void setPickedPhenoDataSetCategory(
			PickedPhenoDataSetCategory pickedPhenoDataSetCategory) {
		this.pickedPhenoDataSetCategory = pickedPhenoDataSetCategory;
	}

	public PhenoDataSetFieldDisplay getPhenoDataSetFieldDisplay() {
		return phenoDataSetFieldDisplay;
	}

	public void setPhenoDataSetFieldDisplay(
			PhenoDataSetFieldDisplay phenoDataSetFieldDisplay) {
		this.phenoDataSetFieldDisplay = phenoDataSetFieldDisplay;
	}
	
}
