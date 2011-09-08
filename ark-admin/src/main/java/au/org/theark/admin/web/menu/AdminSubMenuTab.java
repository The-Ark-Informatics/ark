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
package au.org.theark.admin.web.menu;

import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.extensions.markup.html.tabs.AbstractTab;
import org.apache.wicket.extensions.markup.html.tabs.ITab;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import au.org.theark.admin.web.component.function.FunctionContainerPanel;
import au.org.theark.admin.web.component.module.ModuleContainerPanel;
import au.org.theark.admin.web.component.rolePolicy.RolePolicyContainerPanel;
import au.org.theark.core.model.study.entity.ArkFunction;
import au.org.theark.core.model.study.entity.ArkModule;
import au.org.theark.core.service.IArkCommonService;
import au.org.theark.core.web.component.menu.AbstractArkTabPanel;
import au.org.theark.core.web.component.tabbedPanel.ArkAjaxTabbedPanel;

public class AdminSubMenuTab extends AbstractArkTabPanel {
	/**
	 * 
	 */
	private static final long			serialVersionUID	= 2808674930679468072L;
	@SpringBean(name = au.org.theark.core.Constants.ARK_COMMON_SERVICE)
	private IArkCommonService<Void>	iArkCommonService;
	private List<ITab>					moduleSubTabsList	= new ArrayList<ITab>();

	public AdminSubMenuTab(String id) {
		super(id);
		buildTabs();
	}

	public void buildTabs() {
		ArkModule arkModule = iArkCommonService.getArkModuleByName(au.org.theark.core.Constants.ARK_MODULE_ADMIN);
		List<ArkFunction> arkFunctionList = iArkCommonService.getModuleFunction(arkModule);

		// TODO: Shoud admin sub-menus really access the database to determine their visibility?
		for (final ArkFunction arkFunction : arkFunctionList) {
			AbstractTab tab = new AbstractTab(new StringResourceModel(arkFunction.getResourceKey(), this, null)) {
				/**
				 * 
				 */
				private static final long	serialVersionUID	= 5972079308171290188L;

				@Override
				public Panel getPanel(final String panelId) {
					return panelToReturn(arkFunction, panelId);
				}
			};
			moduleSubTabsList.add(tab);
		}

		ArkAjaxTabbedPanel moduleTabbedPanel = new ArkAjaxTabbedPanel("adminSubMenus", moduleSubTabsList);
		add(moduleTabbedPanel);
	}

	protected Panel panelToReturn(ArkFunction arkFunction, String panelId) {
		Panel panelToReturn = null;

		// Clear cache to determine permissions
		processAuthorizationCache(au.org.theark.core.Constants.ARK_MODULE_ADMIN, arkFunction);

		if (arkFunction.getName().equalsIgnoreCase(au.org.theark.core.Constants.FUNCTION_KEY_VALUE_MODULE)) {
			ModuleContainerPanel containerPanel = new ModuleContainerPanel(panelId);
			panelToReturn = containerPanel;
		}
		else if (arkFunction.getName().equalsIgnoreCase(au.org.theark.core.Constants.FUNCTION_KEY_VALUE_FUNCTION)) {
			FunctionContainerPanel containerPanel = new FunctionContainerPanel(panelId);
			panelToReturn = containerPanel;
		}
		else if (arkFunction.getName().equalsIgnoreCase(au.org.theark.core.Constants.FUNCTION_KEY_VALUE_ROLE_POLICY_TEMPLATE)) {
			RolePolicyContainerPanel containerPanel = new RolePolicyContainerPanel(panelId);
			panelToReturn = containerPanel;
		}

		return panelToReturn;
	}
}
