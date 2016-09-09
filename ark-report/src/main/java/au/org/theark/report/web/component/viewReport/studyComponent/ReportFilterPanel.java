package au.org.theark.report.web.component.viewReport.studyComponent;

import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.CompoundPropertyModel;

import au.org.theark.report.model.vo.ConsentDetailsReportVO;
import au.org.theark.report.model.vo.StudyComponentReportVO;
import au.org.theark.report.web.component.viewReport.ReportOutputPanel;
import au.org.theark.report.web.component.viewReport.studyComponent.filterform.StudyComponentFilterForm;

public class ReportFilterPanel extends Panel {

	private static final long	serialVersionUID	= 1L;

	AjaxButton	generateButton;

	public ReportFilterPanel(String id) {
		super(id);
	}

	public void initialisePanel(CompoundPropertyModel<StudyComponentReportVO> cpModel, FeedbackPanel feedbackPanel, ReportOutputPanel reportOutputPanel) {
		StudyComponentFilterForm studyComponentFilterForm = new StudyComponentFilterForm("filterForm", cpModel);
		studyComponentFilterForm.initialiseFilterForm(feedbackPanel, reportOutputPanel);
		add(studyComponentFilterForm);
	}

}
