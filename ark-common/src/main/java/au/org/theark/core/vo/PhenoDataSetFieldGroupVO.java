package au.org.theark.core.vo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import au.org.theark.core.model.pheno.entity.PhenoDataSetCategory;
import au.org.theark.core.model.pheno.entity.PhenoDataSetField;
import au.org.theark.core.model.pheno.entity.PhenoDataSetFieldDisplay;
import au.org.theark.core.model.pheno.entity.PhenoDataSetGroup;
import au.org.theark.core.model.pheno.entity.PickedPhenoDataSetCategory;
import au.org.theark.core.model.study.entity.ArkUser;

public class PhenoDataSetFieldGroupVO implements Serializable {

	private static final long serialVersionUID = 1L;
	private PhenoDataSetGroup 				phenoDataSetGroup;
	private PhenoDataSetFieldDisplay 		phenoDataSetFieldDisplay;
	
	protected List<PhenoDataSetCategory>	phenoDataSetFieldCategoryLst;// Persistence object(Already)
	private List<PhenoDataSetCategory>		selectedCategories;//Relevant selected object.

	private List<PickedPhenoDataSetCategory>  	pickedAvailableCategories;// Persistence object
	private List<PickedPhenoDataSetCategory>  	pickedSelectedCategories;//Relevant selected object.
	
	private List<PhenoDataSetField> 		availablePhenoDataSetFields;//Persistence object
	private List<PhenoDataSetField> 		selectedPhenoDataSetFields;//Relevant selected
	
	
	private List<PhenoDataSetField> 		linkedAvailablePhenoDataSetFields;//Persistence object
	private List<PhenoDataSetField> 		linkedSelectedPhenoDataSetFields;//Relevant selected.
	
	
	protected boolean 						usePhenoDataSetFieldDisplay;	// Flags whether or not CustomFieldDisplay should be saved, etc
	private String 							phenoDataSetFieldFileUploadField;
	private ArkUser 						arkUser;
	
	
	public PhenoDataSetFieldGroupVO() {
		super();
		phenoDataSetFieldDisplay = new PhenoDataSetFieldDisplay();
		phenoDataSetGroup=new PhenoDataSetGroup();
		//Pheno Categories
		phenoDataSetFieldCategoryLst=new ArrayList<PhenoDataSetCategory>();
		pickedAvailableCategories=new ArrayList<PickedPhenoDataSetCategory>();
		pickedSelectedCategories=new ArrayList<PickedPhenoDataSetCategory>();
		availablePhenoDataSetFields= new ArrayList<PhenoDataSetField>();
		linkedAvailablePhenoDataSetFields=new ArrayList<PhenoDataSetField>();
		usePhenoDataSetFieldDisplay = false;
	}
	
	public PhenoDataSetGroup getPhenoDataSetGroup() {
		return phenoDataSetGroup;
	}
	public void setPhenoDataSetGroup(PhenoDataSetGroup phenoDataSetGroup) {
		this.phenoDataSetGroup = phenoDataSetGroup;
	}
	public PhenoDataSetFieldDisplay getPhenoDataSetFieldDisplay() {
		return phenoDataSetFieldDisplay;
	}
	public void setPhenoDataSetFieldDisplay(PhenoDataSetFieldDisplay phenoDataSetFieldDisplay) {
		this.phenoDataSetFieldDisplay = phenoDataSetFieldDisplay;
	}
	
	public List<PhenoDataSetCategory> getPhenoDataSetFieldCategoryLst() {
		return phenoDataSetFieldCategoryLst;
	}

	public void setPhenoDataSetFieldCategoryLst(
			List<PhenoDataSetCategory> phenoDataSetFieldCategoryLst) {
		this.phenoDataSetFieldCategoryLst = phenoDataSetFieldCategoryLst;
	}

	public List<PhenoDataSetCategory> getSelectedCategories() {
		return selectedCategories;
	}

	public void setSelectedCategories(List<PhenoDataSetCategory> selectedCategories) {
		this.selectedCategories = selectedCategories;
	}
		
	public List<PickedPhenoDataSetCategory> getPickedAvailableCategories() {
		return pickedAvailableCategories;
	}

	public void setPickedAvailableCategories(
			List<PickedPhenoDataSetCategory> pickedAvailableCategories) {
		this.pickedAvailableCategories = pickedAvailableCategories;
	}

	public List<PickedPhenoDataSetCategory> getPickedSelectedCategories() {
		return pickedSelectedCategories;
	}

	public void setPickedSelectedCategories(
			List<PickedPhenoDataSetCategory> pickedSelectedCategories) {
		this.pickedSelectedCategories = pickedSelectedCategories;
	}

	public List<PhenoDataSetField> getAvailablePhenoDataSetFields() {
		return availablePhenoDataSetFields;
	}

	public void setAvailablePhenoDataSetFields(
			List<PhenoDataSetField> availablePhenoDataSetFields) {
		this.availablePhenoDataSetFields = availablePhenoDataSetFields;
	}

	public List<PhenoDataSetField> getSelectedPhenoDataSetFields() {
		return selectedPhenoDataSetFields;
	}

	public void setSelectedPhenoDataSetFields(
			List<PhenoDataSetField> selectedPhenoDataSetFields) {
		this.selectedPhenoDataSetFields = selectedPhenoDataSetFields;
	}

	public List<PhenoDataSetField> getLinkedAvailablePhenoDataSetFields() {
		return linkedAvailablePhenoDataSetFields;
	}

	public void setLinkedAvailablePhenoDataSetFields(
			List<PhenoDataSetField> linkedAvailablePhenoDataSetFields) {
		this.linkedAvailablePhenoDataSetFields = linkedAvailablePhenoDataSetFields;
	}

	public List<PhenoDataSetField> getLinkedSelectedPhenoDataSetFields() {
		return linkedSelectedPhenoDataSetFields;
	}

	public void setLinkedSelectedPhenoDataSetFields(
			List<PhenoDataSetField> linkedSelectedPhenoDataSetFields) {
		this.linkedSelectedPhenoDataSetFields = linkedSelectedPhenoDataSetFields;
	}

	public boolean isUsePhenoDataSetFieldDisplay() {
		return usePhenoDataSetFieldDisplay;
	}

	public void setUsePhenoDataSetFieldDisplay(boolean usePhenoDataSetFieldDisplay) {
		this.usePhenoDataSetFieldDisplay = usePhenoDataSetFieldDisplay;
	}

	public String getPhenoDataSetFieldFileUploadField() {
		return phenoDataSetFieldFileUploadField;
	}

	public void setPhenoDataSetFieldFileUploadField(
			String phenoDataSetFieldFileUploadField) {
		this.phenoDataSetFieldFileUploadField = phenoDataSetFieldFileUploadField;
	}

	public ArkUser getArkUser() {
		return arkUser;
	}

	public void setArkUser(ArkUser arkUser) {
		this.arkUser = arkUser;
	}

	
	
}
