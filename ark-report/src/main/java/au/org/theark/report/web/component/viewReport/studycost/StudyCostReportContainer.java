package au.org.theark.report.web.component.viewReport.studycost;

import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.CompoundPropertyModel;

import au.org.theark.core.model.report.entity.ReportTemplate;
import au.org.theark.report.model.vo.ResearcherCostResportVO;
import au.org.theark.report.model.vo.StudyCostReportVo;
import au.org.theark.report.web.component.viewReport.AbstractSelectedReportContainer;
import au.org.theark.report.web.component.viewReport.ReportOutputPanel;
import au.org.theark.report.web.component.viewReport.studycost.ReportFilterPanel;

public class StudyCostReportContainer extends AbstractSelectedReportContainer<StudyCostReportVo> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public StudyCostReportContainer(String id) {
		super(id);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void initialiseCPModel() {
		StudyCostReportVo grvVO = new StudyCostReportVo();
		cpModel = new CompoundPropertyModel<StudyCostReportVo>(grvVO);		
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
