package au.org.theark.report.web.component.viewReport.studySummary;

import org.apache.wicket.markup.html.panel.Panel;

import au.org.theark.report.web.component.viewReport.ReportContainerVO;
import au.org.theark.report.web.component.viewReport.form.ReportSelectForm;

@SuppressWarnings("serial")
public class ReportSelectPanel extends Panel
{

	private ReportContainerVO		reportContainerVO;
	private ReportSelectForm		reportSelectForm;
	
	public ReportSelectPanel(String id, 
					ReportContainerVO reportContainerVO,
					ReportSelectForm reportSelectForm)
	{
		super(id);
		this.reportContainerVO = reportContainerVO;
		this.reportSelectForm = reportSelectForm;
	}

}