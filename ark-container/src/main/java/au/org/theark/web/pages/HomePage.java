package au.org.theark.web.pages;

import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.PageParameters;
import org.apache.wicket.extensions.markup.html.tabs.ITab;
import org.apache.wicket.extensions.markup.html.tabs.TabbedPanel;
import org.apache.wicket.markup.html.WebMarkupContainer;

import au.org.theark.core.web.component.ArkContextPanel;
import au.org.theark.geno.web.menu.GenoTabProviderImpl;
import au.org.theark.phenotypic.web.menu.PhenotypicTabProviderImpl;
import au.org.theark.study.web.menu.MainTabProviderImpl;

/**
 * Homepage aka Index page of The ARK
 */
public class HomePage extends BasePage
{

	private static final long	serialVersionUID	= 1L;
	private WebMarkupContainer wmc;

	/**
	 * Constructor that is invoked when page is invoked without a session.
	 * 
	 * @param parameters
	 *           Page parameters
	 */
	public HomePage(final PageParameters parameters)
	{
		buildModuleTabs();
		
		wmc = new WebMarkupContainer("arkSummaryContainer");
		ArkContextPanel arkContextPanel = new ArkContextPanel("arkContextPanel");
		wmc.add(arkContextPanel);
		add(wmc);
	}

	@Override
	protected void buildModuleTabs()
	{
		//this.studyLogoMarkup
		List<ITab> moduleTabsList = new ArrayList<ITab>();

		MainTabProviderImpl studyMainTabProvider = new MainTabProviderImpl("study");
		// Pass in the Study logo markup, to allow dynamic logo reference
		moduleTabsList = studyMainTabProvider.buildTabs(this.studyNameMarkup, this.studyLogoMarkup);

		PhenotypicTabProviderImpl phenotypicTabs = new PhenotypicTabProviderImpl("phenotypic");
		List<ITab> phenotypicTabsList = phenotypicTabs.buildTabs();
		for (ITab itab : phenotypicTabsList)
		{
			moduleTabsList.add(itab);
		}

		GenoTabProviderImpl genoTabs = new GenoTabProviderImpl("geno");
		List<ITab> genoTabsList = genoTabs.buildTabs();
		for (ITab itab : genoTabsList)
		{
			moduleTabsList.add(itab);
		}

		TabbedPanel moduleTabbedPanel = new TabbedPanel("moduleTabsList", moduleTabsList);
		add(moduleTabbedPanel);
	}

	@Override
	protected void buildTabMenu()
	{
		// TODO Auto-generated method stub
	}

	@Override
	String getTitle()
	{
		// TODO Auto-generated method stub
		return null;
	}
}
