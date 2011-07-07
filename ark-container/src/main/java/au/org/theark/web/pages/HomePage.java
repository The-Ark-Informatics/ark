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

import au.org.theark.core.web.component.ArkAjaxTabbedPanel;
import au.org.theark.lims.web.menu.LimsTabProviderImpl;
import au.org.theark.phenotypic.web.menu.PhenotypicTabProviderImpl;
import au.org.theark.report.web.menu.ReportTabProviderImpl;
import au.org.theark.study.web.menu.MainTabProviderImpl;

/**
 * Index page of The ARK
 */
public class HomePage extends BasePage
{

	private static final long	serialVersionUID	= 1L;
	private WebMarkupContainer arkContextPanelMarkup;
	private TabbedPanel moduleTabbedPanel;

	/**
	 * Constructor that is invoked when page is invoked without a session.
	 * 
	 * @param parameters
	 *           Page parameters
	 */
	public HomePage(final PageParameters parameters)
	{
		Subject currentUser = SecurityUtils.getSubject();

		if(currentUser.getPrincipal() != null){
			buildContextPanel();
			buildModuleTabs();
		}else{
			setResponsePage(LoginPage.class);
		}
	
	}
	
	protected void buildContextPanel()
	{
		this.arkContextPanelMarkup = new WebMarkupContainer("contextMarkupContainer");
		
		// Require a Label for each context item (currently hard coded in mark up)
		//TODO: Use dynamic component to remove hard-coded Labels
		Label studyLabel = new Label("studyLabel","");
		arkContextPanelMarkup.add(studyLabel);
		Label subjectLabel = new Label("subjectLabel","");
		arkContextPanelMarkup.add(subjectLabel);
		Label phenoLabel = new Label("phenoLabel","");
		arkContextPanelMarkup.add(phenoLabel);
		Label genoLabel = new Label("genoLabel","");
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
		PhenotypicTabProviderImpl phenotypicTabs = new PhenotypicTabProviderImpl("phenotypic");
		List<ITab> phenotypicTabsList = phenotypicTabs.buildTabs(this.arkContextPanelMarkup);
		for (ITab itab : phenotypicTabsList)
		{
			moduleTabsList.add(itab);
		}

		// Geno & Registry not to be deployed as yet into Test
//		GenoTabProviderImpl genoTabs = new GenoTabProviderImpl("geno");
//		List<ITab> genoTabsList = genoTabs.buildTabs();
//		for (ITab itab : genoTabsList)
//		{
//			moduleTabsList.add(itab);
//		}
		
		// Registry
//		RegistryTabProviderImpl registryTabProvider = new RegistryTabProviderImpl("registry");
//		List<ITab> registryTabList =	registryTabProvider.buildTabs();
//		for(ITab tab : registryTabList){
//			moduleTabsList.add(tab);
//		}
//		
		// LIMS
		LimsTabProviderImpl limsTabProvider = new LimsTabProviderImpl("lims");
		List<ITab> limsTabList =	limsTabProvider.buildTabs(this.arkContextPanelMarkup);
		for(ITab tab : limsTabList){
			moduleTabsList.add(tab);
		}
		
		// Report
		ReportTabProviderImpl reportTabProvider = new ReportTabProviderImpl("report");
		List<ITab> reportTabList =	reportTabProvider.buildTabs();
		for(ITab tab : reportTabList){
			moduleTabsList.add(tab);
		}
		
		moduleTabbedPanel = new ArkAjaxTabbedPanel("moduleTabsList", moduleTabsList);
		moduleTabbedPanel.setOutputMarkupPlaceholderTag(true);
		studyMainTabProvider.setModuleTabbedPanel(moduleTabbedPanel);
		add(moduleTabbedPanel);
	}

	@Override
	protected void buildTabMenu()
	{
	}

	@Override
	String getTitle()
	{
		return null;
	}
}
