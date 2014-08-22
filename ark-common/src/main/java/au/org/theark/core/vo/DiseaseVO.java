package au.org.theark.core.vo;

import java.util.ArrayList;
import java.util.List;

import au.org.theark.core.model.disease.entity.Disease;
import au.org.theark.core.model.disease.entity.Gene;

public class DiseaseVO extends BaseVO {

	private static final long serialVersionUID = 1L;
	
	private Disease disease;
	private List<Gene> availableGenes = new ArrayList<Gene>();
	private List<Gene> selectedGenes = new ArrayList<Gene>();
	
	public DiseaseVO() {
		disease = new Disease();
	}
	
	public DiseaseVO(Disease disease) {
		this.disease = disease;
	}

	public Disease getDisease() {
		return disease;
	}

	public void setDisease(Disease disease) {
		this.disease = disease;
	}

	public List<Gene> getAvailableGenes() {
		return availableGenes;
	}

	public void setAvailableGenes(List<Gene> availableGenes) {
		this.availableGenes = availableGenes;
	}

	public List<Gene> getSelectedGenes() {
		return selectedGenes;
	}

	public void setSelectedGenes(List<Gene> selectedGenes) {
		this.selectedGenes = selectedGenes;
	}

	@Override
	public String toString() {
		return "DiseaseVO [availableGenes="
				+ availableGenes + ", selectedGenes=" + selectedGenes + "]";
	}
	
}