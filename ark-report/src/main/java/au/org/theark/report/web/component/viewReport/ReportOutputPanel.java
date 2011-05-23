package au.org.theark.report.web.component.viewReport;

import org.apache.wicket.markup.html.link.AbstractLink;
import org.apache.wicket.markup.html.link.ExternalLink;
import org.apache.wicket.markup.html.link.ResourceLink;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.wicketstuff.jasperreports.JRResource;

import au.org.theark.core.service.IArkCommonService;
import au.org.theark.report.service.Constants;
import au.org.theark.report.service.IReportService;

public class ReportOutputPanel extends Panel {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public AbstractLink reportLink;

	public ReportOutputPanel(String id) {
		super(id);
		// TODO Auto-generated constructor stub
	}

	public void initialisePanel() {
		reportLink = new ExternalLink("linkToReport", "", "");
		reportLink.setOutputMarkupPlaceholderTag(true);	//allow link to be replaced even when invisible
		add(reportLink);
		this.setVisible(false);	//start off invisible
	}
	
	public void setReportResource(JRResource reportResource) {
		if (reportResource != null) {
			ResourceLink<Void> newLink = new ResourceLink<Void>("linkToReport", reportResource);
			newLink.setOutputMarkupPlaceholderTag(true);	//allow link to be replaced even when invisible
			addOrReplace(newLink);
			reportLink = newLink;
		}
		else {
			if (!reportLink.getClass().equals(ExternalLink.class)) {
				ExternalLink newLink = new ExternalLink("linkToReport", "", "");
				newLink.setOutputMarkupPlaceholderTag(true);	//allow link to be replaced even when invisible
				reportLink.replaceWith(newLink);
				reportLink = newLink;
			}
		}
	}
	
	@Override
	public boolean isVersioned()
	{
		return false;
	}
}
