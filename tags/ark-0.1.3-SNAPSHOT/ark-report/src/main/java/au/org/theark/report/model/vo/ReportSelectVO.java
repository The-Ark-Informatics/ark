/**
 * 
 */
package au.org.theark.report.model.vo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import au.org.theark.core.model.report.entity.ReportTemplate;
import au.org.theark.core.model.study.entity.Study;

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
	
	public ReportSelectVO() {
		this.selectedReport = new ReportTemplate();
		this.reportsAvailableList = new ArrayList<ReportTemplate>();
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

}
