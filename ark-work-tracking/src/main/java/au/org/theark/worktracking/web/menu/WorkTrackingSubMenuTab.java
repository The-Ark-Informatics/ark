package au.org.theark.worktracking.web.menu;

import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.extensions.markup.html.tabs.AbstractTab;
import org.apache.wicket.extensions.markup.html.tabs.ITab;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;

import au.org.theark.core.vo.MenuModule;
import au.org.theark.core.web.component.tabbedPanel.ArkAjaxTabbedPanel;
import au.org.theark.worktracking.util.Constants;
import au.org.theark.worktracking.web.component.researcher.ResearcherContainerPanel;


public class WorkTrackingSubMenuTab extends Panel{

	private static final long	serialVersionUID	= -3695404298701886701L;
	private List<ITab>			moduleSubTabsList;

	public WorkTrackingSubMenuTab(String id) {
		super(id);
		moduleSubTabsList = new ArrayList<ITab>();
		buildTabs();
	}

	public void buildTabs() {
		List<MenuModule> moduleTabs = new ArrayList<MenuModule>();

		MenuModule menuModule = new MenuModule();
		menuModule.setModuleName(Constants.RESEARCHER);
		menuModule.setResourceKey(Constants.TAB_MODULE_RESEARCHER);
		moduleTabs.add(menuModule);

		for (final MenuModule moduleName : moduleTabs) {
			moduleSubTabsList.add(new AbstractTab(new Model<String>(getLocalizer().getString(moduleName.getResourceKey(), WorkTrackingSubMenuTab.this, moduleName.getModuleName()))) {
				/**
				 * 
				 */
				private static final long	serialVersionUID	= -7414890128705025350L;

				public boolean isVisible() {
					// Work tab always visible
					return true;
				}

				@Override
				public Panel getPanel(String panelId) {
					Panel panelToReturn = null;// Set up a common tab that will be accessible for all users

					if (moduleName.getModuleName().equalsIgnoreCase(Constants.RESEARCHER)) {
						ResearcherContainerPanel researcherContainerPanel = new ResearcherContainerPanel(panelId);
						panelToReturn = researcherContainerPanel;
					}

					return panelToReturn;
				};
			});
		}

		ArkAjaxTabbedPanel moduleTabbedPanel = new ArkAjaxTabbedPanel(Constants.WORK_SUBMENU, moduleSubTabsList);
		add(moduleTabbedPanel);
	}
	
}
