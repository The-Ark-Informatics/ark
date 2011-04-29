package au.org.theark.report.web.component.viewReport.studySummary;

import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.CompoundPropertyModel;

import au.org.theark.report.model.vo.GenericReportViewVO;

@SuppressWarnings("serial")
public class ReportViewPanel extends Panel {
	private CompoundPropertyModel<GenericReportViewVO> cpmModel;

	public ReportViewPanel(	String id)
	{
		super(id);
	}

}