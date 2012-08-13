package au.org.theark.report.web.component.viewReport.researcherdetailcost;

import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.CompoundPropertyModel;

import au.org.theark.core.model.report.entity.ReportTemplate;
import au.org.theark.report.model.vo.ResearcherCostResportVO;
import au.org.theark.report.web.component.viewReport.AbstractSelectedReportContainer;
import au.org.theark.report.web.component.viewReport.ReportOutputPanel;


public class WorkResearcherDetailCostReportContainer extends AbstractSelectedReportContainer<ResearcherCostResportVO>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public WorkResearcherDetailCostReportContainer(String id) {
		super(id);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void initialiseCPModel() {
		ResearcherCostResportVO grvVO = new ResearcherCostResportVO();
		cpModel = new CompoundPropertyModel<ResearcherCostResportVO>(grvVO);		
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
