package au.org.theark.report.model.vo;

import au.org.theark.core.model.study.entity.Study;

public class BiospecimenDetailsReportVO extends GenericReportViewVO {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Study study;
	
	private String subjectUID;

	public BiospecimenDetailsReportVO() {
	}

	public Study getStudy() {
		return study;
	}

	public void setStudy(Study study) {
		this.study = study;
	}

	public String getSubjectUID() {
		return subjectUID;
	}

	public void setSubjectUID(String subjectUID) {
		this.subjectUID = subjectUID;
	}

}
