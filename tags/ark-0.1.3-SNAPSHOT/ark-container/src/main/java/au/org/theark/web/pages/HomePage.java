package au.org.theark.web.pages;

import java.util.ArrayList;
import java.util.List;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.apache.wicket.PageParameters;
import org.apache.wicket.extensions.markup.html.tabs.ITab;
import org.apache.wicket.extensions.markup.html.tabs.TabbedPanel;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import au.org.theark.core.web.component.ArkAjaxTabbedPanel;
import au.org.theark.lims.web.menu.LimsTabProviderImpl;
import au.org.theark.phenotypic.web.menu.PhenotypicTabProviderImpl;
import au.org.theark.report.web.menu.ReportTabProviderImpl;
import au.org.theark.study.web.menu.MainTabProviderImpl;

/**
 * <p>
 * The <code>HomePage</code> class that extends the {@link au.org.theark.web.pages.BasePage BasePage} class.
 * It provides the implementation of the index page of The Ark application.
 * </p>
 * 
 * @author nivedann
 * @author cellis
 */
public class HomePage extends BasePage
{
	private transient static Logger	log	= LoggerFactory.getLogger(HomePage.class);
	private WebMarkupContainer			arkContextPanelMarkup;
	private TabbedPanel					moduleTabbedPanel;

	/**
	 * Constructor that is invoked when page is invoked without a session.
	 * 
	 * @param parameters
	 *           Page parameters
	 */
	public HomePage(final PageParameters parameters)
	{
		Subject currentUser = SecurityUtils.getSubject();

		if (currentUser.getPrincipal() != null)
		{
			buildContextPanel();
			buildModuleTabs();
		}
		else
		{
			setResponsePage(LoginPage.class);
		}
	}

	/**
	 * Builds the ContextPanel that is used to store/show the context items in session, such as Study, SubjectUID etc)
	 */
	protected void buildContextPanel()
	{
		this.arkContextPanelMarkup = new WebMarkupContainer("contextMarkupContainer");

		// Require a Label for each context item (currently hard coded in mark up)
		// TODO: Use dynamic component to remove hard-coded Labels
		Label studyLabel = new Label("studyLabel", "");
		arkContextPanelMarkup.add(studyLabel);
		Label subjectLabel = new Label("subjectLabel", "");
		arkContextPanelMarkup.add(subjectLabel);
		Label phenoLabel = new Label("phenoLabel", "");
		arkContextPanelMarkup.add(phenoLabel);
		Label genoLabel = new Label("genoLabel", "");
		arkContextPanelMarkup.add(genoLabel);
		add(arkContextPanelMarkup);
		arkContextPanelMarkup.setOutputMarkupPlaceholderTag(true);
	}

	@Override
	protected void buildModuleTabs()
	{
		List<ITab> moduleTabsList = new ArrayList<ITab>();

		// Study
		MainTabProviderImpl studyMainTabProvider = new MainTabProviderImpl("study");
		// Pass in the Study logo mark up, to allow dynamic logo reference
		moduleTabsList = studyMainTabProvider.buildTabs(this.studyNameMarkup, this.studyLogoMarkup, this.arkContextPanelMarkup);

		// Pheno
		PhenotypicTabProviderImpl phenotypicTabProvidor = new PhenotypicTabProviderImpl("phenotypic");
		List<ITab> phenotypicTabsList = phenotypicTabProvidor.buildTabs(this.arkContextPanelMarkup);
		for (ITab itab : phenotypicTabsList)
		{
			moduleTabsList.add(itab);
		}

		//TODO: Geno & Registry not to be deployed as yet into Test
		/*  Geno  Not avaialble in 0.1.3 Snapshot
		GenoTabProviderImpl genoTabs = new GenoTabProviderImpl("geno");
		 List<ITab> genoTabsList = genoTabs.buildTabs();
		 for (ITab itab : genoTabsList)
		 {
		 	moduleTabsList.add(itab);
		 }
		 */

		/*  Registry not avaialable in 0.1.3
		 RegistryTabProviderImpl registryTabProvider = new RegistryTabProviderImpl("registry");
		 List<ITab> registryTabList = registryTabProvider.buildTabs();
		 for(ITab tab : registryTabList)
		 {
		 	moduleTabsList.add(tab);
		 }
		 */

		// 
		LimsTabProviderImpl limsTabProvider = new LimsTabProviderImpl("lims");
		List<ITab> limsTabList = limsTabProvider.buildTabs(this.arkContextPanelMarkup);
		for (ITab tab : limsTabList)
		{
			moduleTabsList.add(tab);
		}

		// Report 
		ReportTabProviderImpl reportTabProvider = new ReportTabProviderImpl("report");
		List<ITab> reportTabList = reportTabProvider.buildTabs();
		for (ITab tab : reportTabList)
		{
			moduleTabsList.add(tab);
		}
		
		// Admin
		/*  Not avaialble in 0.1.3 Snapshot
		AdminTabProviderImpl adminTabProvider = new AdminTabProviderImpl("admin");
		List<ITab> adminTabList = adminTabProvider.buildTabs();
		for (ITab tab : adminTabList)
		{
			moduleTabsList.add(tab);
		}
		*/
		
		moduleTabbedPanel = new ArkAjaxTabbedPanel("moduleTabsList", moduleTabsList);
		moduleTabbedPanel.setOutputMarkupPlaceholderTag(true);
		studyMainTabProvider.setModuleTabbedPanel(moduleTabbedPanel);
		add(moduleTabbedPanel);
	}

	@Override
	String getTitle()
	{
		return null;
	}

	/**
	 * @param log
	 *           the log to set
	 */
	public static void setLog(Logger log)
	{
		HomePage.log = log;
	}

	/**
	 * @return the log
	 */
	public static Logger getLog()
	{
		return log;
	}
}
