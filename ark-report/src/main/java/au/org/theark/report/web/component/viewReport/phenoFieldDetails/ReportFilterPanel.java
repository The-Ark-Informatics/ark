package au.org.theark.report.web.component.viewReport.phenoFieldDetails;

import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.CompoundPropertyModel;

import au.org.theark.report.model.vo.FieldDetailsReportVO;
import au.org.theark.report.web.component.viewReport.phenoFieldDetails.filterForm.FieldDetailsFilterForm;

public class ReportFilterPanel extends Panel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	AjaxButton generateButton;
	
	public ReportFilterPanel(String id) {
		super(id);
		// TODO Auto-generated constructor stub
	}

	public void initialisePanel(CompoundPropertyModel<FieldDetailsReportVO> cpModel, 
								FeedbackPanel feedbackPanel, 
								au.org.theark.report.web.component.viewReport.ReportOutputPanel reportOutputPanel) {
		FieldDetailsFilterForm fieldDetailsFilterForm = new FieldDetailsFilterForm("filterForm", cpModel);
		fieldDetailsFilterForm.initialiseFilterForm(feedbackPanel, reportOutputPanel);
		add(fieldDetailsFilterForm);
	}
}
