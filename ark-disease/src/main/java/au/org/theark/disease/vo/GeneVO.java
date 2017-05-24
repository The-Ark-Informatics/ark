package au.org.theark.disease.vo;

import java.util.ArrayList;
import java.util.List;

import au.org.theark.core.model.disease.entity.Disease;
import au.org.theark.core.model.disease.entity.Gene;
import au.org.theark.core.vo.BaseVO;
import au.org.theark.core.vo.ArkVo;

public class GeneVO extends BaseVO implements ArkVo{

	private Gene gene;
	private List<Disease> selectedDiseases = new ArrayList<Disease>();
	private List<Disease> availableDiseases = new ArrayList<Disease>();
	
	public GeneVO() {
		gene = new Gene();
	}
	
	public GeneVO(Gene gene) {
		this.gene = gene;
	}
	
	public Gene getGene() {
		return this.gene;
	}
	
	public void setGene(Gene gene) {
		this.gene = gene;
	}

	public List<Disease> getSelectedDiseases() {
		return selectedDiseases;
	}

	public void setSelectedDiseases(List<Disease> selectedDiseases) {
		this.selectedDiseases = selectedDiseases;
	}

	public List<Disease> getAvailableDiseases() {
		return availableDiseases;
	}

	public void setAvailableDiseases(List<Disease> availableDiseases) {
		this.availableDiseases = availableDiseases;
	}	
	
	@Override
	public String getArkVoName(){
		return "Gene";
	}
}
