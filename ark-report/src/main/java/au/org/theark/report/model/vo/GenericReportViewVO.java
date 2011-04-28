/**
 * 
 */
package au.org.theark.report.model.vo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Properties;

import au.org.theark.report.model.entity.LinkStudyReportTemplate;
import au.org.theark.report.model.entity.ReportSecurity;

/**
 * @author elam
 *
 */
public class GenericReportViewVO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private LinkStudyReportTemplate selectedReport;
	private Properties parameters;
	
	public GenericReportViewVO() {
		this.selectedReport = new LinkStudyReportTemplate();
		this.parameters = new Properties();
	}

	public LinkStudyReportTemplate getSelectedReport() {
		return selectedReport;
	}

	public void setSelectedReport(LinkStudyReportTemplate selectedReport) {
		this.selectedReport = selectedReport;
	}

	public Properties getParameters() {
		return parameters;
	}
	
}
