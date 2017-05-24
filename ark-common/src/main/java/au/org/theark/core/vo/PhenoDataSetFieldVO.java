package au.org.theark.core.vo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import au.org.theark.core.model.pheno.entity.PhenoDataSetCategory;
import au.org.theark.core.model.pheno.entity.PhenoDataSetField;
import au.org.theark.core.model.pheno.entity.PhenoDataSetFieldDisplay;
import au.org.theark.core.vo.ArkVo;

public class PhenoDataSetFieldVO implements ArkVo, Serializable {
	
	private static final long serialVersionUID = 1L;
	private PhenoDataSetField phenoDataSetField;
	/*private PhenoDataSetFieldDisplay phenoDataSetFieldDisplay;
	protected boolean usePhenoDataSetFieldDisplay;	
	*/
	public PhenoDataSetFieldVO() {
		super();
		phenoDataSetField = new PhenoDataSetField();
		/*phenoDataSetFieldDisplay = new PhenoDataSetFieldDisplay();
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
	public boolean isUsePhenoDataSetFieldDisplay() {
		return usePhenoDataSetFieldDisplay;
	}
	public void setUsePhenoDataSetFieldDisplay(boolean usePhenoDataSetFieldDisplay) {
		this.usePhenoDataSetFieldDisplay = usePhenoDataSetFieldDisplay;
	}*/
	@Override
	public String getArkVoName(){
		return "Pheno Dataset Field";
	}
}
