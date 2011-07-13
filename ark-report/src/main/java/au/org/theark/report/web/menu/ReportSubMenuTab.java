package au.org.theark.report.web.menu;

import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.extensions.markup.html.tabs.AbstractTab;
import org.apache.wicket.extensions.markup.html.tabs.ITab;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;

import au.org.theark.core.vo.MenuModule;
import au.org.theark.core.web.component.ArkAjaxTabbedPanel;
import au.org.theark.report.web.Constants;
import au.org.theark.report.web.component.viewReport.ReportContainerPanel;

public class ReportSubMenuTab extends Panel
{
	/**
	 * 
	 */
	private static final long	serialVersionUID	= -3695404298701886701L;
	private List<ITab> moduleSubTabsList;

	public ReportSubMenuTab(String id)
	{
		super(id);
		moduleSubTabsList = new ArrayList<ITab>();
		buildTabs();
	}

	public void buildTabs()
	{
		List<MenuModule> moduleTabs = new ArrayList<MenuModule>();

		// This way we can get the menus from the back-end. We should source this data from a table in the backend and wrap it up in a class like this
		MenuModule menuModule = new MenuModule();
		menuModule.setModuleName(Constants.REPORT_DETAIL);
		menuModule.setResourceKey(Constants.TAB_MODULE_REPORT_DETAIL);
		moduleTabs.add(menuModule);

		for (final MenuModule moduleName : moduleTabs)
		{
			moduleSubTabsList.add(new AbstractTab(new Model<String>(getLocalizer().getString(moduleName.getResourceKey(), ReportSubMenuTab.this, moduleName.getModuleName())))
			{
				/**
				 * 
				 */
				private static final long	serialVersionUID	= -7414890128705025350L;

				public boolean isVisible()
				{
					// Reporting tab always visible
					return true;
				}

				@Override
				public Panel getPanel(String panelId)
				{
					Panel panelToReturn = null;// Set up a common tab that will be accessible for all users

					if (moduleName.getModuleName().equalsIgnoreCase(Constants.REPORT_DETAIL))
					{
						ReportContainerPanel reportContainerPanel = new ReportContainerPanel(panelId);
						reportContainerPanel.initialisePanel();
						panelToReturn = reportContainerPanel;
					}
					
					return panelToReturn;
				};
			});
		}

		ArkAjaxTabbedPanel moduleTabbedPanel = new ArkAjaxTabbedPanel(Constants.REPORT_SUBMENU, moduleSubTabsList);
		add(moduleTabbedPanel);
	}
}