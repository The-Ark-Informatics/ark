/*******************************************************************************
 * Copyright (c) 2011  University of Western Australia. All rights reserved.
 * 
 * This file is part of The Ark.
 * 
 * The Ark is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 3
 * of the License, or (at your option) any later version.
 * 
 * The Ark is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
package au.org.theark.web.menu;

import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.extensions.markup.html.tabs.AbstractTab;
import org.apache.wicket.extensions.markup.html.tabs.ITab;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;

import au.org.theark.core.vo.MenuModule;
import au.org.theark.core.web.component.tabbedPanel.ArkAjaxTabbedPanel;
import au.org.theark.report.web.Constants;
import au.org.theark.report.web.component.dataextraction.DataExtractionContainerPanel;
import au.org.theark.report.web.component.viewReport.ReportContainerPanel;

public class ReportSubMenuTab extends Panel {

	private static final long	serialVersionUID	= -3695404298701886701L;
	private List<ITab>			moduleSubTabsList;

	public ReportSubMenuTab(String id) {
		super(id);
		moduleSubTabsList = new ArrayList<ITab>();
		buildTabs();
	}

	public void buildTabs() {
		List<MenuModule> moduleTabs = new ArrayList<MenuModule>();

		// This way we can get the menus from the back-end. We should source this data from a table in the backend and wrap it up in a class like this
		MenuModule menuModule = new MenuModule();
		menuModule.setModuleName(Constants.REPORT_DETAIL);  //these are functions and not so much "modules" as they are referened in the ark system
		menuModule.setResourceKey(Constants.TAB_MODULE_REPORT_DETAIL);
		moduleTabs.add(menuModule);
		
		MenuModule advancedMenuModule = new MenuModule();
		advancedMenuModule.setModuleName(Constants.DATA_EXTRACTION);  //these are functions and not so much "modules" as they are referened in the ark system
		advancedMenuModule.setResourceKey(Constants.TAB_MODULE_DATA_EXTRACTION);
		moduleTabs.add(advancedMenuModule);

		for (final MenuModule moduleName : moduleTabs) {
			moduleSubTabsList.add(new AbstractTab(new Model<String>(getLocalizer().getString(moduleName.getResourceKey(), ReportSubMenuTab.this, moduleName.getModuleName()))) {

				private static final long	serialVersionUID	= -7414890128705025350L;

				public boolean isVisible() {
					// Reporting tab always visible
					return true;
				}

				@Override
				public Panel getPanel(String panelId) {
					Panel panelToReturn = null;// Set up a common tab that will be accessible for all users

					if (moduleName.getModuleName().equalsIgnoreCase(Constants.REPORT_DETAIL)) {
						ReportContainerPanel reportContainerPanel = new ReportContainerPanel(panelId);
						reportContainerPanel.initialisePanel();
						panelToReturn = reportContainerPanel;
					}
					else if (moduleName.getModuleName().equalsIgnoreCase(Constants.DATA_EXTRACTION)) {
						DataExtractionContainerPanel dataExtractionContainerPanel = new DataExtractionContainerPanel(panelId);
						//dataExtractionContainerPanel.initialiseSearchPanel();   THESE ARE PROTECTED...BUT DO I REALLY NEED TO DO THEM NOW ANYWAY?
						panelToReturn = dataExtractionContainerPanel;
					}

					return panelToReturn;
				};
			});
		}

		ArkAjaxTabbedPanel moduleTabbedPanel = new ArkAjaxTabbedPanel(Constants.REPORT_SUBMENU, moduleSubTabsList);
		add(moduleTabbedPanel);
	}
}
