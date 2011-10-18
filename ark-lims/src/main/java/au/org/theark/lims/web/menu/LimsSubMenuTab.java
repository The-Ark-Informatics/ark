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
package au.org.theark.lims.web.menu;

import java.util.ArrayList;
import java.util.List;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.ThreadContext;
import org.apache.wicket.extensions.markup.html.tabs.AbstractTab;
import org.apache.wicket.extensions.markup.html.tabs.ITab;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.panel.EmptyPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import au.org.theark.core.model.study.entity.ArkFunction;
import au.org.theark.core.model.study.entity.ArkModule;
import au.org.theark.core.service.IArkCommonService;
import au.org.theark.core.web.component.customfield.CustomFieldContainerPanel;
import au.org.theark.core.web.component.menu.AbstractArkTabPanel;
import au.org.theark.core.web.component.tabbedPanel.ArkAjaxTabbedPanel;
import au.org.theark.lims.web.Constants;
import au.org.theark.lims.web.component.barcodelabel.BarcodeLabelContainerPanel;
import au.org.theark.lims.web.component.barcodeprinter.BarcodePrinterContainerPanel;
import au.org.theark.lims.web.component.biocollectioncustomdata.BioCollectionCustomDataContainerPanel;
import au.org.theark.lims.web.component.biospecimen.BiospecimenContainerPanel;
import au.org.theark.lims.web.component.biospecimencustomdata.BiospecimenCustomDataContainerPanel;
import au.org.theark.lims.web.component.inventory.panel.InventoryContainerPanel;
import au.org.theark.lims.web.component.subjectlims.subject.SubjectContainerPanel;

@SuppressWarnings("serial")
public class LimsSubMenuTab extends AbstractArkTabPanel {
	@SpringBean(name = au.org.theark.core.Constants.ARK_COMMON_SERVICE)
	private IArkCommonService<Void>	iArkCommonService;

	private WebMarkupContainer			arkContextMarkup;

	public LimsSubMenuTab(String id, WebMarkupContainer arkContextMarkup) {
		super(id);
		this.arkContextMarkup = arkContextMarkup;
		buildTabs();
	}

	public void buildTabs() {
		List<ITab> moduleSubTabsList = new ArrayList<ITab>();

		ArkModule arkModule = iArkCommonService.getArkModuleByName(au.org.theark.core.Constants.ARK_MODULE_LIMS);
		List<ArkFunction> arkFunctionList = iArkCommonService.getModuleFunction(arkModule);// Gets a list of ArkFunctions for the given Module

		for (final ArkFunction menuArkFunction : arkFunctionList) {
			moduleSubTabsList.add(new AbstractTab(new StringResourceModel(menuArkFunction.getResourceKey(), this, null)) {
				@Override
				public Panel getPanel(String panelId) {
					return buildPanels(menuArkFunction, panelId);
				}
				
				@Override
				public boolean isVisible() {
					boolean flag = true;
					if(menuArkFunction.getResourceKey().equalsIgnoreCase("tab.module.lims.barcodeprinter") || menuArkFunction.getResourceKey().equalsIgnoreCase("tab.module.lims.barcodelabel")) {
						SecurityManager securityManager = ThreadContext.getSecurityManager();
						Subject currentUser = SecurityUtils.getSubject();

						// Only a Super Administrator can see the barcodeprinter/barcodelabel tabs
						if (securityManager.hasRole(currentUser.getPrincipals(), au.org.theark.core.security.RoleConstants.ARK_ROLE_SUPER_ADMINISTATOR)) {
							flag = currentUser.isAuthenticated();
						}
						else {
							flag = false;
						}
					}
					return super.isVisible() && flag;
				}
			});
		}

		ArkAjaxTabbedPanel moduleTabbedPanel = new ArkAjaxTabbedPanel(Constants.MENU_LIMS_SUBMENU, moduleSubTabsList);
		add(moduleTabbedPanel);
	}

	protected Panel buildPanels(final ArkFunction arkFunction, String panelId) {
		Panel panelToReturn = null;// Set
		processAuthorizationCache(au.org.theark.core.Constants.ARK_MODULE_LIMS, arkFunction);

		if (arkFunction.getName().equalsIgnoreCase(au.org.theark.core.Constants.FUNCTION_KEY_VALUE_LIMS_SUBJECT)) {
			panelToReturn = new SubjectContainerPanel(panelId, arkContextMarkup);// Note the constructor
		}
//		else if (arkFunction.getName().equalsIgnoreCase(au.org.theark.core.Constants.FUNCTION_KEY_VALUE_LIMS_COLLECTION)) {
//			panelToReturn = new BioCollectionContainerPanel(panelId, arkContextMarkup);// Note the constructor
//		}
		else if (arkFunction.getName().equalsIgnoreCase(au.org.theark.core.Constants.FUNCTION_KEY_VALUE_BIOSPECIMEN)) {
			panelToReturn = new BiospecimenContainerPanel(panelId, arkContextMarkup);// Note the constructor
		}
		else if (arkFunction.getName().equalsIgnoreCase(au.org.theark.core.Constants.FUNCTION_KEY_VALUE_INVENTORY)) {
			panelToReturn = new InventoryContainerPanel(panelId);
		}
		else if (arkFunction.getName().equalsIgnoreCase(au.org.theark.core.Constants.FUNCTION_KEY_VALUE_LIMS_COLLECTION_CUSTOM_FIELD)) {
			// useCustomFieldDisplay = true
			panelToReturn = new CustomFieldContainerPanel(panelId, true, 
											iArkCommonService.getArkFunctionByName(au.org.theark.core.Constants.FUNCTION_KEY_VALUE_LIMS_COLLECTION));
		}
		else if (arkFunction.getName().equalsIgnoreCase(au.org.theark.core.Constants.FUNCTION_KEY_VALUE_BIOSPECIMEN_CUSTOM_FIELD)) {
			// useCustomFieldDisplay = true
			panelToReturn = new CustomFieldContainerPanel(panelId, true, 
											iArkCommonService.getArkFunctionByName(au.org.theark.core.Constants.FUNCTION_KEY_VALUE_BIOSPECIMEN));
		}
		else if (arkFunction.getName().equalsIgnoreCase(au.org.theark.core.Constants.FUNCTION_KEY_VALUE_LIMS_COLLECTION_CUSTOM_DATA)) {
			panelToReturn = new BioCollectionCustomDataContainerPanel(panelId).initialisePanel();
		}
		else if (arkFunction.getName().equalsIgnoreCase(au.org.theark.core.Constants.FUNCTION_KEY_VALUE_BIOSPECIMEN_CUSTOM_DATA)) {
			panelToReturn = new BiospecimenCustomDataContainerPanel(panelId).initialisePanel();
		}
		else if (arkFunction.getName().equalsIgnoreCase(au.org.theark.core.Constants.FUNCTION_KEY_VALUE_BARCODE_PRINTER)) {
			panelToReturn = new BarcodePrinterContainerPanel(panelId, arkContextMarkup);
		}
		else if (arkFunction.getName().equalsIgnoreCase(au.org.theark.core.Constants.FUNCTION_KEY_VALUE_BARCODE_LABEL)) {
			panelToReturn = new BarcodeLabelContainerPanel(panelId, arkContextMarkup);
		}
		else {
			//TODO: This shouldn't happen when all functions have been implemented
			panelToReturn = new EmptyPanel(panelId);
		}
		return panelToReturn;
	}

}
