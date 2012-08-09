package au.org.theark.report.model.vo;

import au.org.theark.core.model.worktracking.entity.Researcher;

public class ResearcherCostResportVO extends GenericReportViewVO {
		
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Long researcherId;
	private Long studyId;
	private String invoice;
	private String year;
	private Researcher researcher;
	
	public Long getResearcherId() {
		return researcherId;
	}
	public void setResearcherId(Long researcherId) {
		this.researcherId = researcherId;
	}
	public Long getStudyId() {
		return studyId;
	}
	public void setStudyId(Long studyId) {
		this.studyId = studyId;
	}
	public String getInvoice() {
		return invoice;
	}
	public void setInvoice(String invoice) {
		this.invoice = invoice;
	}
	public String getYear() {
		return year;
	}
	public void setYear(String year) {
		this.year = year;
	}
	public Researcher getResearcher() {
		return researcher;
	}
	public void setResearcher(Researcher researcher) {
		this.researcher = researcher;
	}
}
