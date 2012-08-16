package au.org.theark.report.model.vo;


public class StudyCostReportVo extends GenericReportViewVO {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Long studyId;
	private String invoice;
	private String year;
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
	
	

}
