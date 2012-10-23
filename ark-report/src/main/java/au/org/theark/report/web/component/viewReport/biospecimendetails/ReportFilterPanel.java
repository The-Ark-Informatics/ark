package au.org.theark.report.web.component.viewReport.biospecimendetails;

import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.CompoundPropertyModel;

import au.org.theark.report.model.vo.BiospecimenDetailsReportVO;
import au.org.theark.report.web.component.viewReport.ReportOutputPanel;
import au.org.theark.report.web.component.viewReport.biospecimendetails.filterform.BiospecimenDetailsFilterForm;

public class ReportFilterPanel extends Panel {

	private static final long	serialVersionUID	= 1L;

	AjaxButton						generateButton;

	public ReportFilterPanel(String id) {
		super(id);
	}

	public void initialisePanel(CompoundPropertyModel<BiospecimenDetailsReportVO> cpModel, FeedbackPanel feedbackPanel, ReportOutputPanel reportOutputPanel) {
		BiospecimenDetailsFilterForm biospecimenDetailsFilterForm = new BiospecimenDetailsFilterForm("filterForm", cpModel);
		biospecimenDetailsFilterForm.initialiseFilterForm(feedbackPanel, reportOutputPanel);
		add(biospecimenDetailsFilterForm);
	}

}
