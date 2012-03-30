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
package au.org.theark.study.web.menu;

import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.extensions.markup.html.tabs.AbstractTab;
import org.apache.wicket.extensions.markup.html.tabs.ITab;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import au.org.theark.core.Constants;
import au.org.theark.core.model.study.entity.ArkFunction;
import au.org.theark.core.model.study.entity.ArkModule;
import au.org.theark.core.service.IArkCommonService;
import au.org.theark.core.web.component.customfield.CustomFieldContainerPanel;
import au.org.theark.core.web.component.menu.AbstractArkTabPanel;
import au.org.theark.core.web.component.tabbedPanel.ArkAjaxTabbedPanel;
import au.org.theark.study.web.component.address.AddressContainerPanel;
import au.org.theark.study.web.component.attachments.AttachmentsContainerPanel;
import au.org.theark.study.web.component.consent.ConsentContainerPanel;
import au.org.theark.study.web.component.correspondence.CorrespondenceContainerPanel;
import au.org.theark.study.web.component.phone.PhoneContainerPanel;
import au.org.theark.study.web.component.subject.SubjectContainerPanel;
import au.org.theark.study.web.component.subjectUpload.SubjectUploadContainerPanel;
import au.org.theark.study.web.component.subjectcustomdata.SubjectCustomDataContainerPanel;

/**
 * @author nivedann
 * @author cellis
 * 
 */
public class SubjectSubMenuTab extends AbstractArkTabPanel {
	/**
	 * 
	 */
	private static final long	serialVersionUID	= -467105983288558903L;

	@SuppressWarnings("unchecked")
	@SpringBean(name = au.org.theark.core.Constants.ARK_COMMON_SERVICE)
	private IArkCommonService	iArkCommonService;

	private WebMarkupContainer	arkContextMarkup;
	/**
	 * @param id
	 */
	public SubjectSubMenuTab(String id, WebMarkupContainer arkContextMarkup) {
		super(id);
		this.arkContextMarkup = arkContextMarkup;
		new ArrayList<ITab>();
		buildTabs();
	}

	@SuppressWarnings({ "serial", "unchecked" })
	public void buildTabs() {

		List<ITab> moduleSubTabsList = new ArrayList<ITab>();

		ArkModule arkModule = iArkCommonService.getArkModuleByName(Constants.ARK_MODULE_SUBJECT);
		List<ArkFunction> arkFunctionList = iArkCommonService.getModuleFunction(arkModule);// Gets a list of ArkFunctions for the given Module

		for (final ArkFunction menuArkFunction : arkFunctionList) {

			moduleSubTabsList.add(new AbstractTab(new StringResourceModel(menuArkFunction.getResourceKey(), this, null)) {
				@Override
				public Panel getPanel(String panelId) {
					Panel panelToReturn = null;// Set
					if (menuArkFunction.getName().equalsIgnoreCase(au.org.theark.core.Constants.FUNCTION_KEY_VALUE_SUBJECT)) {
						processAuthorizationCache(au.org.theark.core.Constants.ARK_MODULE_SUBJECT, menuArkFunction);
						panelToReturn = new SubjectContainerPanel(panelId, arkContextMarkup);// Note the constructor
					}
					else if (menuArkFunction.getName().equalsIgnoreCase(au.org.theark.core.Constants.FUNCTION_KEY_VALUE_PHONE)) {
						processAuthorizationCache(au.org.theark.core.Constants.ARK_MODULE_SUBJECT, menuArkFunction);
						panelToReturn = new PhoneContainerPanel(panelId);
					}
					else if (menuArkFunction.getName().equalsIgnoreCase(au.org.theark.core.Constants.FUNCTION_KEY_VALUE_ADDRESS)) {
						processAuthorizationCache(au.org.theark.core.Constants.ARK_MODULE_SUBJECT, menuArkFunction);
						panelToReturn = new AddressContainerPanel(panelId);
					}
					else if (menuArkFunction.getName().equalsIgnoreCase(au.org.theark.core.Constants.FUNCTION_KEY_VALUE_CONSENT)) {
						processAuthorizationCache(au.org.theark.core.Constants.ARK_MODULE_SUBJECT, menuArkFunction);
						panelToReturn = new ConsentContainerPanel(panelId);
					}
					else if (menuArkFunction.getName().equalsIgnoreCase(au.org.theark.core.Constants.FUNCTION_KEY_VALUE_ATTACHMENT)) {
						processAuthorizationCache(au.org.theark.core.Constants.ARK_MODULE_SUBJECT, menuArkFunction);
						panelToReturn = new AttachmentsContainerPanel(panelId);
					}
					else if (menuArkFunction.getName().equalsIgnoreCase(au.org.theark.core.Constants.FUNCTION_KEY_VALUE_SUBJECT_UPLOAD)) {
						processAuthorizationCache(au.org.theark.core.Constants.ARK_MODULE_SUBJECT, menuArkFunction);
						panelToReturn = new SubjectUploadContainerPanel(panelId,menuArkFunction);
					}
					else if (menuArkFunction.getName().equalsIgnoreCase(au.org.theark.core.Constants.FUNCTION_KEY_VALUE_SUBJECT_CORRESPONDENCE)) {
						processAuthorizationCache(au.org.theark.core.Constants.ARK_MODULE_SUBJECT, menuArkFunction);
						panelToReturn = new CorrespondenceContainerPanel(panelId);
					}
					else if (menuArkFunction.getName().equalsIgnoreCase(au.org.theark.core.Constants.FUNCTION_KEY_VALUE_SUBJECT_CUSTOM_FIELD)) {
						processAuthorizationCache(au.org.theark.core.Constants.ARK_MODULE_SUBJECT, menuArkFunction);
						// useCustomFieldDisplay = true
						panelToReturn = new CustomFieldContainerPanel(panelId, true, 
														iArkCommonService.getArkFunctionByName(au.org.theark.core.Constants.FUNCTION_KEY_VALUE_SUBJECT));
					}
					else if (menuArkFunction.getName().equalsIgnoreCase(au.org.theark.core.Constants.FUNCTION_KEY_VALUE_SUBJECT_CUSTOM_DATA)) {
						processAuthorizationCache(au.org.theark.core.Constants.ARK_MODULE_SUBJECT, menuArkFunction);
						panelToReturn = new SubjectCustomDataContainerPanel(panelId).initialisePanel();
					}
					return panelToReturn;
				}
			});
		}

		ArkAjaxTabbedPanel moduleTabbedPanel = new ArkAjaxTabbedPanel(Constants.MENU_SUBJECT_SUBMENU, moduleSubTabsList);
		add(moduleTabbedPanel);
	}
}