/**
 * 
 */
package au.org.theark.report.model.vo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import au.org.theark.core.model.study.entity.Study;
import au.org.theark.report.model.entity.LinkStudyReportTemplate;
import au.org.theark.report.model.entity.ReportTemplate;

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
	private ReportTemplate selectedReport;
	private List<ReportTemplate> reportsAvailableList;
//	TODO :: remove these when ReportSelectForm is no longer required
	private LinkStudyReportTemplate selectedLinkStudyReport;
	private List<LinkStudyReportTemplate> linkedStudyReportList;
	
	public ReportSelectVO() {
		this.selectedReport = new ReportTemplate();
		this.reportsAvailableList = new ArrayList<ReportTemplate>();
		this.selectedLinkStudyReport = new LinkStudyReportTemplate();
		this.linkedStudyReportList = new ArrayList<LinkStudyReportTemplate>();
	}

	public void setStudy(Study study) {
		this.study = study;
	}

	public Study getStudy() {
		return study;
	}

	public ReportTemplate getSelectedReport() {
		return selectedReport;
	}

	public void setSelectedReport(ReportTemplate selectedReport) {
		this.selectedReport = selectedReport;
	}

	public List<ReportTemplate> getReportsAvailableList() {
		return reportsAvailableList;
	}
	
	public void setReportsAvailableList(
			List<ReportTemplate> reportsAvailableList) {
		this.reportsAvailableList = reportsAvailableList;
	}

	public void setSelectedLinkStudyReport(LinkStudyReportTemplate selectedLinkStudyReport) {
		this.selectedLinkStudyReport = selectedLinkStudyReport;
	}

	public LinkStudyReportTemplate getSelectedLinkStudyReport() {
		return selectedLinkStudyReport;
	}

	public List<LinkStudyReportTemplate> getLinkedStudyReportList() {
		return linkedStudyReportList;
	}

	public void setLinkedStudyReportList(
			List<LinkStudyReportTemplate> linkedStuydReportList) {
		this.linkedStudyReportList = linkedStuydReportList;
	}

}
