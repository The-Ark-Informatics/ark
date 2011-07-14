package au.org.theark.report.model.vo;

import au.org.theark.core.model.pheno.entity.PhenoCollection;
import au.org.theark.core.model.study.entity.Study;

public class FieldDetailsReportVO extends GenericReportViewVO {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	protected Study study;
	protected PhenoCollection phenoCollection;
	protected boolean fieldDataAvailable;
	
	public FieldDetailsReportVO() {
	}

	public Study getStudy() {
		return study;
	}


	public void setStudy(Study study) {
		this.study = study;
	}

	public PhenoCollection getPhenoCollection() {
		return phenoCollection;
	}


	public void setPhenoCollection(PhenoCollection phenoCollection) {
		this.phenoCollection = phenoCollection;
	}


	public boolean getFieldDataAvailable() {
		return fieldDataAvailable;
	}


	public void setFieldDataAvailable(boolean fieldDataAvailable) {
		this.fieldDataAvailable = fieldDataAvailable;
	}

}
