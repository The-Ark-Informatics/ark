package au.org.theark.report.web.component.viewReport.studyLevelConsent;

import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.CompoundPropertyModel;

import au.org.theark.core.model.report.entity.ReportTemplate;
import au.org.theark.report.model.vo.ConsentDetailsReportVO;
import au.org.theark.report.web.component.viewReport.AbstractSelectedReportContainer;
import au.org.theark.report.web.component.viewReport.ReportOutputPanel;

public class StudyLevelConsentReportContainer extends AbstractSelectedReportContainer<ConsentDetailsReportVO> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
		
	public StudyLevelConsentReportContainer(String id) {
		super(id);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	protected void initialiseCPModel() {
		ConsentDetailsReportVO reportViewVO = new ConsentDetailsReportVO();
		cpModel = new CompoundPropertyModel<ConsentDetailsReportVO>(reportViewVO);
	}

	public void initialisePanel(FeedbackPanel feedbackPanel, ReportTemplate reportTemplate) {
		cpModel.getObject().setSelectedReportTemplate(reportTemplate);
		ReportFilterPanel rfp = new ReportFilterPanel("reportFilterPanel");
		// Initialise output panel without link
		ReportOutputPanel reportOutputPanel = new ReportOutputPanel("reportOutputPanel");
		reportOutputPanel.initialisePanel();
		reportOutputPanel.setOutputMarkupPlaceholderTag(true);
		
		rfp.initialisePanel(cpModel, feedbackPanel, reportOutputPanel);
		add(rfp);
		add(reportOutputPanel);
	}

}
