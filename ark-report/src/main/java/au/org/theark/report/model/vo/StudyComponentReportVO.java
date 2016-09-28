package au.org.theark.report.model.vo;

import java.util.Date;

import au.org.theark.core.model.study.entity.Consent;

public class StudyComponentReportVO extends GenericReportViewVO  {
	private static final long serialVersionUID = 1L;
	protected Consent consent;
	private Date fromDate;
	private Date toDate;
	private String fromDateLabel;
	private String toDateLabel;
	

	public Consent getConsent() {
		return consent;
	}

	public void setConsent(Consent consent) {
		this.consent = consent;
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

	public String getFromDateLabel() {
		return fromDateLabel;
	}

	public void setFromDateLabel(String fromDateLabel) {
		this.fromDateLabel = fromDateLabel;
	}

	public String getToDateLabel() {
		return toDateLabel;
	}

	public void setToDateLabel(String toDateLabel) {
		this.toDateLabel = toDateLabel;
	}
	

}
