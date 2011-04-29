/**
 * 
 */
package au.org.theark.report.model.vo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import au.org.theark.core.model.study.entity.Study;
import au.org.theark.report.model.entity.LinkStudyReportTemplate;

/**
 * @author elam
 *
 */
public class ReportSelectVO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Study study;
	private LinkStudyReportTemplate selectedReport;
	private List<LinkStudyReportTemplate> reportsAvailableList;
	
	public ReportSelectVO() {
		this.selectedReport = new LinkStudyReportTemplate();
		this.reportsAvailableList = new ArrayList<LinkStudyReportTemplate>();
	}

	public void setStudy(Study study) {
		this.study = study;
	}

	public Study getStudy() {
		return study;
	}

	public LinkStudyReportTemplate getSelectedReport() {
		return selectedReport;
	}

	public void setSelectedReport(LinkStudyReportTemplate selectedReport) {
		this.selectedReport = selectedReport;
	}

	public List<LinkStudyReportTemplate> getReportsAvailableList() {
		return reportsAvailableList;
	}
	
	public void setReportsAvailableList(
			List<LinkStudyReportTemplate> reportsAvailableList) {
		this.reportsAvailableList = reportsAvailableList;
	}

}
