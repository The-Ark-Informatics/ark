package au.org.theark.report.web.component.viewReport.biospecimensummary;

import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.CompoundPropertyModel;

import au.org.theark.report.model.vo.BiospecimenSummaryReportVO;
import au.org.theark.report.web.component.viewReport.ReportOutputPanel;
import au.org.theark.report.web.component.viewReport.biospecimensummary.filterform.BiospecimenSummaryFilterForm;

public class ReportFilterPanel extends Panel {

	private static final long	serialVersionUID	= 1L;

	AjaxButton						generateButton;

	public ReportFilterPanel(String id) {
		super(id);
	}

	public void initialisePanel(CompoundPropertyModel<BiospecimenSummaryReportVO> cpModel, FeedbackPanel feedbackPanel, ReportOutputPanel reportOutputPanel) {
		BiospecimenSummaryFilterForm biospecimenSummaryFilterForm = new BiospecimenSummaryFilterForm("filterForm", cpModel);
		biospecimenSummaryFilterForm.initialiseFilterForm(feedbackPanel, reportOutputPanel);
		add(biospecimenSummaryFilterForm);
	}

}
