package au.org.theark.report.model.vo;

import java.util.Calendar;
import java.util.Date;

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
	private Date fromDate;
	private Date toDate;
	private Researcher researcher;
	
	
	
	public ResearcherCostResportVO() {
		
		Calendar firstDayCalendar=Calendar.getInstance();
		firstDayCalendar.set(Calendar.DAY_OF_YEAR,1);
		fromDate= firstDayCalendar.getTime();
		
		Calendar lastDayCalendar=Calendar.getInstance();
		lastDayCalendar.set(Calendar.MONTH,11);
		lastDayCalendar.set(Calendar.DAY_OF_MONTH,31);
		toDate= lastDayCalendar.getTime();	
	}
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
	public Date getFromDate() {
		return fromDate;
	}
	public void setFromDate(Date fromDate) {
		this.fromDate = fromDate;
	}
	public Date getToDate() {
		return toDate;
	}
	public void setToDate(Date toDate) {
		this.toDate = toDate;
	}
}
