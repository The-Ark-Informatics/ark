/**
 * 
 */
package au.org.theark.report.model.vo;

import java.io.Serializable;
import java.util.List;
import java.util.Properties;

import au.org.theark.report.model.entity.ReportOutputFormat;
import au.org.theark.report.model.entity.ReportTemplate;

/**
 * @author elam
 *
 */
public class GenericReportViewVO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private ReportTemplate selectedReportTemplate;
	private List<ReportOutputFormat> listReportOutputFormats;
	private ReportOutputFormat selectedOutputFormat;
	private Properties parameters;
	
	public GenericReportViewVO() {
		this.parameters = new Properties();
	}

	public Properties getParameters() {
		return parameters;
	}

	public void setSelectedReportTemplate(ReportTemplate selectedReportTemplate) {
		this.selectedReportTemplate = selectedReportTemplate;
	}

	public ReportTemplate getSelectedReportTemplate() {
		return selectedReportTemplate;
	}

	public List<ReportOutputFormat> getListReportOutputFormats() {
		return listReportOutputFormats;
	}

	public void setListReportOutputFormats(
			List<ReportOutputFormat> listReportOutputFormats) {
		this.listReportOutputFormats = listReportOutputFormats;
	}
	
	public void setSelectedOutputFormat(ReportOutputFormat selectedOutputFormat) {
		this.selectedOutputFormat = selectedOutputFormat;
	}

	public ReportOutputFormat getSelectedOutputFormat() {
		return selectedOutputFormat;
	}

	
}
