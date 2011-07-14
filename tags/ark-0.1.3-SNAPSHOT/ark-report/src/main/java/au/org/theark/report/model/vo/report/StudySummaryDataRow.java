package au.org.theark.report.model.vo.report;

import java.io.Serializable;

public class StudySummaryDataRow implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	protected String section;
	protected String status;
	protected Number subjectCount;
	
	public StudySummaryDataRow(String section, String status, Number subjectCount) {
		this.section = section;
		this.status = status;
		this.subjectCount = subjectCount;
	}

	public String getSection() {
		return section;
	}

	public void setSection(String section) {
		this.section = section;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Number getSubjectCount() {
		return subjectCount;
	}

	public void setSubjectCount(Number subjectCount) {
		this.subjectCount = subjectCount;
	}
	
}
