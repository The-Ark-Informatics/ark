package au.org.theark.report.web.component.viewReport.studyLevelConsent;

import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.CompoundPropertyModel;

import au.org.theark.report.model.vo.ConsentDetailsReportVO;
import au.org.theark.report.model.vo.GenericReportViewVO;
import au.org.theark.report.web.component.viewReport.consentDetails.filterForm.ConsentDetailsFilterForm;
import au.org.theark.report.web.component.viewReport.studyLevelConsent.filterForm.StudyLevelConsentDetailsFilterForm;

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

	public void initialisePanel(CompoundPropertyModel<ConsentDetailsReportVO> cpModel, 
								FeedbackPanel feedbackPanel, 
								au.org.theark.report.web.component.viewReport.ReportOutputPanel reportOutputPanel) {
		StudyLevelConsentDetailsFilterForm consentDetailsFilterForm = new StudyLevelConsentDetailsFilterForm("filterForm", cpModel);
		consentDetailsFilterForm.initialiseFilterForm(feedbackPanel, reportOutputPanel);
		add(consentDetailsFilterForm);
	}
}
