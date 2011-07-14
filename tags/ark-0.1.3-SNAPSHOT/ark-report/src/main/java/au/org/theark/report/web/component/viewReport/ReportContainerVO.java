package au.org.theark.report.web.component.viewReport;

import java.io.Serializable;

import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.panel.FeedbackPanel;

import au.org.theark.report.web.component.viewReport.studySummary.filterForm.StudySummaryFilterForm;

public class ReportContainerVO implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private FeedbackPanel feedbackPanel;
	private ReportSelectPanel reportSelectPanel;
	private WebMarkupContainer selectedReportContainerWMC;
	private AbstractSelectedReportContainer selectedReportPanel;

	public ReportContainerVO() {

		selectedReportContainerWMC = new WebMarkupContainer("selectedReportContainerWMC");
		selectedReportContainerWMC.setOutputMarkupPlaceholderTag(true);
		selectedReportContainerWMC.setVisible(false);

	}

	public FeedbackPanel getFeedbackPanel() {
		return feedbackPanel;
	}

	public void setFeedbackPanel(FeedbackPanel feedBackPanel) {
		this.feedbackPanel = feedBackPanel;
	}

	public ReportSelectPanel getReportSelectPanel() {
		return reportSelectPanel;
	}

	public void setReportSelectPanel(ReportSelectPanel reportSelectPanel) {
		this.reportSelectPanel = reportSelectPanel;
	}

	public WebMarkupContainer getSelectedReportContainerWMC() {
		return selectedReportContainerWMC;
	}

	public void setSelectedReportContainerWMC(
			WebMarkupContainer selectedReportContainerWMC) {
		this.selectedReportContainerWMC = selectedReportContainerWMC;
	}

	public AbstractSelectedReportContainer getSelectedReportPanel() {
		return selectedReportPanel;
	}

	public void setSelectedReportPanel(
			AbstractSelectedReportContainer selectedReportPanel) {
		this.selectedReportPanel = selectedReportPanel;
	}

}