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
import org.apache.wicket.extensions.markup.html.tabs.TabbedPanel;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import au.org.theark.core.Constants;
import au.org.theark.core.model.study.entity.ArkFunction;
import au.org.theark.core.model.study.entity.ArkModule;
import au.org.theark.core.service.IArkCommonService;
import au.org.theark.core.web.component.menu.AbstractArkTabPanel;
import au.org.theark.core.web.component.tabbedPanel.ArkAjaxTabbedPanel;
import au.org.theark.lims.service.IInventoryService;
import au.org.theark.lims.web.component.global.biospecimen.BiospecimenContainerPanel;
import au.org.theark.lims.web.component.inventory.tree.TreeModel;
import au.org.theark.study.web.component.global.subject.SubjectContainerPanel;

/**
 * @author nivedann
 * @author cellis
 * 
 */
public class GlobalSearchSubMenuTab extends AbstractArkTabPanel {

	private static final long	serialVersionUID	= -467105983288558903L;

	@SuppressWarnings("unchecked")
	@SpringBean(name = au.org.theark.core.Constants.ARK_COMMON_SERVICE)
	private IArkCommonService	iArkCommonService;

	@SpringBean(name = au.org.theark.lims.web.Constants.LIMS_INVENTORY_SERVICE)
	private IInventoryService					iInventoryService;

	private WebMarkupContainer	studyNameMarkup;
	private WebMarkupContainer	studyLogoMarkup;
	private WebMarkupContainer	arkContextMarkup;
//	boolean childStudy;

	private TabbedPanel mainTabs;
	
	/**
	 * @param id
	 */
	public GlobalSearchSubMenuTab(String id, WebMarkupContainer arkContextMarkup, WebMarkupContainer studyNameMarkup, WebMarkupContainer studyLogoMarkup, TabbedPanel mainTabs) {
		super(id);
		this.arkContextMarkup = arkContextMarkup;
		this.studyNameMarkup = studyNameMarkup;
		this.studyLogoMarkup = studyLogoMarkup;
		this.mainTabs = mainTabs;
		buildTabs();
	}

	@SuppressWarnings( { "serial", "unchecked" })
	public void buildTabs() {

		List<ITab> moduleSubTabsList = new ArrayList<ITab>();

		ArkModule arkModule = iArkCommonService.getArkModuleByName(Constants.ARK_MODULE_SUBJECT);
		List<ArkFunction> arkFunctionList = new ArrayList<ArkFunction>();

		arkFunctionList.add(iArkCommonService.getArkFunctionByName(au.org.theark.core.Constants.FUNCTION_KEY_VALUE_SUBJECT));
		arkFunctionList.add(iArkCommonService.getArkFunctionByName(au.org.theark.core.Constants.FUNCTION_KEY_VALUE_GLOBAL_BIOSPECIMEN_SEARCH));
		
		for(final ArkFunction arkFunction : arkFunctionList) {
			System.out.println("ArkFunction: " + arkFunction + " " + arkFunction.getResourceKey());
			moduleSubTabsList.add(new AbstractTab(new StringResourceModel(arkFunction.getResourceKey(), this, null)) {
				@Override
				public Panel getPanel(String panelId) {
					Panel panelToReturn = null;// Set
					// Clear cache
					processAuthorizationCache(au.org.theark.core.Constants.ARK_MODULE_SUBJECT, arkFunction);
					if (arkFunction.getName().equalsIgnoreCase(au.org.theark.core.Constants.FUNCTION_KEY_VALUE_SUBJECT)) {
						panelToReturn = new SubjectContainerPanel(panelId, arkContextMarkup, studyNameMarkup, studyLogoMarkup, mainTabs);// Note the constructor
					}
					else if (arkFunction.getName().equalsIgnoreCase(au.org.theark.core.Constants.FUNCTION_KEY_VALUE_GLOBAL_BIOSPECIMEN_SEARCH)) {
						// Clear cache to determine permissions
						processAuthorizationCache(au.org.theark.core.Constants.ARK_MODULE_LIMS, arkFunction);
						panelToReturn = new BiospecimenContainerPanel(panelId, arkContextMarkup, studyNameMarkup, studyLogoMarkup, new TreeModel(iArkCommonService, iInventoryService).createTreeModel());
					}
					return panelToReturn;
				}
				
				@Override
				public boolean isVisible() {
					return true;
				}
			});
		}
		
		ArkAjaxTabbedPanel moduleTabbedPanel = new ArkAjaxTabbedPanel("globalSearchSubMenu", moduleSubTabsList);
		add(moduleTabbedPanel);
	}
	
	/**
	 * @return the studyNameMarkup
	 */
	public WebMarkupContainer getStudyNameMarkup() {
		return studyNameMarkup;
	}

	/**
	 * @param studyNameMarkup the studyNameMarkup to set
	 */
	public void setStudyNameMarkup(WebMarkupContainer studyNameMarkup) {
		this.studyNameMarkup = studyNameMarkup;
	}

	/**
	 * @return the studyLogoMarkup
	 */
	public WebMarkupContainer getStudyLogoMarkup() {
		return studyLogoMarkup;
	}

	/**
	 * @param studyLogoMarkup the studyLogoMarkup to set
	 */
	public void setStudyLogoMarkup(WebMarkupContainer studyLogoMarkup) {
		this.studyLogoMarkup = studyLogoMarkup;
	}

	/**
	 * @return the arkContextMarkup
	 */
	public WebMarkupContainer getArkContextMarkup() {
		return arkContextMarkup;
	}

	/**
	 * @param arkContextMarkup the arkContextMarkup to set
	 */
	public void setArkContextMarkup(WebMarkupContainer arkContextMarkup) {
		this.arkContextMarkup = arkContextMarkup;
	}
}