package au.org.theark.report.web.component.viewReport.phenoFieldDetails;

import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.CompoundPropertyModel;

import au.org.theark.core.model.report.entity.ReportTemplate;
import au.org.theark.report.model.vo.ConsentDetailsReportVO;
import au.org.theark.report.model.vo.FieldDetailsReportVO;
import au.org.theark.report.web.component.viewReport.AbstractSelectedReportContainer;
import au.org.theark.report.web.component.viewReport.ReportOutputPanel;

public class PhenoFieldDetailsReportContainer extends AbstractSelectedReportContainer<FieldDetailsReportVO> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
		
	public PhenoFieldDetailsReportContainer(String id) {
		super(id);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	protected void initialiseCPModel() {
		FieldDetailsReportVO reportViewVO = new FieldDetailsReportVO();
		cpModel = new CompoundPropertyModel<FieldDetailsReportVO>(reportViewVO);
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
