package au.org.theark.report.web.component.viewReport.studyUserRolePermissions;

import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.CompoundPropertyModel;

import au.org.theark.report.model.vo.GenericReportViewVO;
import au.org.theark.report.web.component.viewReport.ReportOutputPanel;
import au.org.theark.report.web.component.viewReport.studyUserRolePermissions.filterForm.StudyUserRolePermissionsFilterForm;

public class ReportFilterPanel extends Panel {

	/**
	 * 
	 */
	private static final long	serialVersionUID	= 1L;

	AjaxButton						generateButton;

	public ReportFilterPanel(String id) {
		super(id);
		// TODO Auto-generated constructor stub
	}

	public void initialisePanel(CompoundPropertyModel<GenericReportViewVO> cpModel, FeedbackPanel feedbackPanel, ReportOutputPanel reportOutputPanel) {
		StudyUserRolePermissionsFilterForm studyUserRolePermissionsFilterForm = new StudyUserRolePermissionsFilterForm("filterForm", cpModel);
		studyUserRolePermissionsFilterForm.initialiseFilterForm(feedbackPanel, reportOutputPanel);
		add(studyUserRolePermissionsFilterForm);
	}
}
