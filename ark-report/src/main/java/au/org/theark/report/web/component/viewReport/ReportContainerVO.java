package au.org.theark.report.web.component.viewReport;

import java.io.Serializable;

import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.panel.FeedbackPanel;

import au.org.theark.report.web.component.viewReport.studySummary.reportviewform.StudySummaryReportViewForm;

public class ReportContainerVO implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private FeedbackPanel feedBackPanel;
	private StudySummaryReportViewForm reportViewForm;
	private WebMarkupContainer arkContextMarkup;
	private WebMarkupContainer reportSelectWMC;
	private WebMarkupContainer reportParamsWMC;
	private WebMarkupContainer reportViewWMC;

	public ReportContainerVO() {
		
		reportSelectWMC = new WebMarkupContainer("reportSelectContainer");
		reportSelectWMC.setOutputMarkupPlaceholderTag(true);
		reportSelectWMC.setVisible(true);
		
		reportParamsWMC = new WebMarkupContainer("reportParamsContainer");
		reportParamsWMC.setOutputMarkupPlaceholderTag(true);
		reportParamsWMC.setVisible(false);

		reportViewWMC = new WebMarkupContainer("reportViewContainer");
		reportViewWMC.setOutputMarkupPlaceholderTag(true);
		reportViewWMC.setVisible(false);

	}

	public FeedbackPanel getFeedBackPanel() {
		return feedBackPanel;
	}

	public void setFeedBackPanel(FeedbackPanel feedBackPanel) {
		this.feedBackPanel = feedBackPanel;
	}

	public StudySummaryReportViewForm getReportViewForm() {
		return reportViewForm;
	}

	public void setReportViewForm(StudySummaryReportViewForm reportViewForm) {
		this.reportViewForm = reportViewForm;
	}

	public WebMarkupContainer getArkContextMarkup() {
		return arkContextMarkup;
	}

	public void setArkContextMarkup(WebMarkupContainer arkContextMarkup) {
		this.arkContextMarkup = arkContextMarkup;
	}

	public WebMarkupContainer getReportSelectWMC() {
		return reportSelectWMC;
	}

	public void setReportSelectWMC(WebMarkupContainer reportSelectWMC) {
		this.reportSelectWMC = reportSelectWMC;
	}

	public WebMarkupContainer getReportParamsWMC() {
		return reportParamsWMC;
	}

	public void setReportParamsWMC(WebMarkupContainer reportParamsWMC) {
		this.reportParamsWMC = reportParamsWMC;
	}

	public WebMarkupContainer getReportViewWMC() {
		return reportViewWMC;
	}

	public void setReportViewWMC(WebMarkupContainer reportViewWMC) {
		this.reportViewWMC = reportViewWMC;
	}
}