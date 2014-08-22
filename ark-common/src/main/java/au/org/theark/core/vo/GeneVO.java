package au.org.theark.core.vo;

import au.org.theark.core.model.disease.entity.Gene;

public class GeneVO extends BaseVO {

	private Gene gene;
	
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
	
}
