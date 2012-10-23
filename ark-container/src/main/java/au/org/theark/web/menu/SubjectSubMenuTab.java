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

import org.apache.shiro.SecurityUtils;
import org.apache.wicket.extensions.markup.html.tabs.AbstractTab;
import org.apache.wicket.extensions.markup.html.tabs.ITab;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import au.org.theark.core.Constants;
import au.org.theark.core.model.study.entity.ArkFunction;
import au.org.theark.core.model.study.entity.ArkModule;
import au.org.theark.core.model.study.entity.Study;
import au.org.theark.core.service.IArkCommonService;
import au.org.theark.core.web.component.menu.AbstractArkTabPanel;
import au.org.theark.core.web.component.tabbedPanel.ArkAjaxTabbedPanel;
import au.org.theark.phenotypic.web.component.phenodataentry.PhenoCollectionDataEntryContainerPanel;
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

	private static final long	serialVersionUID	= -467105983288558903L;

	@SuppressWarnings("unchecked")
	@SpringBean(name = au.org.theark.core.Constants.ARK_COMMON_SERVICE)
	private IArkCommonService	iArkCommonService;



	private WebMarkupContainer	studyNameMarkup;
	private WebMarkupContainer	studyLogoMarkup;
	private WebMarkupContainer	arkContextMarkup;
	boolean childStudy;

	/**
	 * @param id
	 */
	public SubjectSubMenuTab(String id, WebMarkupContainer arkContextMarkup) {
		super(id);
		this.arkContextMarkup = arkContextMarkup;
		buildTabs();
		
		Long sessionStudyId = (Long) SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.core.Constants.STUDY_CONTEXT_ID);
		Study study = iArkCommonService.getStudy(sessionStudyId);
		childStudy = study.getParentStudy() != null  && (study != study.getParentStudy());
	}

	@SuppressWarnings( { "serial", "unchecked" })
	public void buildTabs() {

		List<ITab> moduleSubTabsList = new ArrayList<ITab>();

		ArkModule arkModule = iArkCommonService.getArkModuleByName(Constants.ARK_MODULE_SUBJECT);
		List<ArkFunction> arkFunctionList = iArkCommonService.getModuleFunction(arkModule);// Gets a list of ArkFunctions for the given Module

		for (final ArkFunction arkFunction : arkFunctionList) {

			moduleSubTabsList.add(new AbstractTab(new StringResourceModel(arkFunction.getResourceKey(), this, null)) {
				@Override
				public Panel getPanel(String panelId) {
					Panel panelToReturn = null;// Set
					// Clear cache
					processAuthorizationCache(au.org.theark.core.Constants.ARK_MODULE_SUBJECT, arkFunction);
					if (arkFunction.getName().equalsIgnoreCase(au.org.theark.core.Constants.FUNCTION_KEY_VALUE_SUBJECT)) {
						panelToReturn = new SubjectContainerPanel(panelId, arkContextMarkup);// Note the constructor
					}
					else if (arkFunction.getName().equalsIgnoreCase(au.org.theark.core.Constants.FUNCTION_KEY_VALUE_SUBJECT_CUSTOM_DATA)) {
						panelToReturn = new SubjectCustomDataContainerPanel(panelId).initialisePanel();
					}
					else if (arkFunction.getName().equalsIgnoreCase(au.org.theark.core.Constants.FUNCTION_KEY_VALUE_PHONE)) {
						panelToReturn = new PhoneContainerPanel(panelId);
					}
					else if (arkFunction.getName().equalsIgnoreCase(au.org.theark.core.Constants.FUNCTION_KEY_VALUE_ADDRESS)) {
						panelToReturn = new AddressContainerPanel(panelId);
					}
					else if (arkFunction.getName().equalsIgnoreCase(au.org.theark.core.Constants.FUNCTION_KEY_VALUE_ATTACHMENT)) {
						panelToReturn = new AttachmentsContainerPanel(panelId);
					}
					else if (arkFunction.getName().equalsIgnoreCase(au.org.theark.core.Constants.FUNCTION_KEY_VALUE_CONSENT)) {
						panelToReturn = new ConsentContainerPanel(panelId);
					}
					else if (arkFunction.getName().equalsIgnoreCase(au.org.theark.core.Constants.FUNCTION_KEY_VALUE_SUBJECT_CORRESPONDENCE)) {
						panelToReturn = new CorrespondenceContainerPanel(panelId);
					}
					else if (arkFunction.getName().equalsIgnoreCase(au.org.theark.core.Constants.FUNCTION_KEY_VALUE_SUBJECT_UPLOAD)) {
						panelToReturn = new SubjectUploadContainerPanel(panelId, arkFunction);
					}
					/* Moved these two tabs into study sub-menu
					else if (menuArkFunction.getName().equalsIgnoreCase(au.org.theark.core.Constants.FUNCTION_KEY_VALUE_SUBJECT_CUSTOM_FIELD)) {
						// useCustomFieldDisplay = true
						panelToReturn = new CustomFieldContainerPanel(panelId, true, iArkCommonService.getArkFunctionByName(au.org.theark.core.Constants.FUNCTION_KEY_VALUE_SUBJECT_CUSTOM_FIELD));
					}
					else if (menuArkFunction.getName().equalsIgnoreCase(au.org.theark.core.Constants.FUNCTION_KEY_VALUE_SUBJECT_CUSTOM_FIELD_UPLOAD)) {
						panelToReturn = new CustomFieldUploadContainerPanel(panelId, iArkCommonService.getArkFunctionByName(au.org.theark.core.Constants.FUNCTION_KEY_VALUE_SUBJECT));
					}*/
					else if (arkFunction.getName().equalsIgnoreCase(au.org.theark.core.Constants.FUNCTION_KEY_VALUE_FIELD_DATA)) {
						// Clear cache to determine permissions
						processAuthorizationCache(au.org.theark.core.Constants.ARK_MODULE_PHENOTYPIC, arkFunction);
						panelToReturn = new PhenoCollectionDataEntryContainerPanel(panelId).initialisePanel();
					}
					return panelToReturn;
				}
				
				@Override
				public boolean isVisible() {
					// Subject Upload only visible to parent studies 
					if (arkFunction.getName().equalsIgnoreCase(au.org.theark.core.Constants.FUNCTION_KEY_VALUE_SUBJECT_UPLOAD)) {
						return (!childStudy);
					}
					else if (arkFunction.getName().equalsIgnoreCase(au.org.theark.core.Constants.FUNCTION_KEY_VALUE_LIMS_SUBJECT )) {
						// hide this tab for now, added below with differing title to that in LIMS
						return false;
					}
					return true;
				}
			});
			
			if (arkFunction.getName().equalsIgnoreCase(au.org.theark.core.Constants.FUNCTION_KEY_VALUE_LIMS_SUBJECT )) {
				moduleSubTabsList.add(new AbstractTab(new StringResourceModel("tab.module.subject.biospecimen", this, null)) {

					@Override
					public WebMarkupContainer getPanel(String panelId) {
						// Clear cache to determine permissions
						processAuthorizationCache(au.org.theark.core.Constants.ARK_MODULE_LIMS, arkFunction);
						return new au.org.theark.lims.web.component.subjectlims.subject.SubjectContainerPanel(panelId, getTitle().getObject());// Note the constructor;
					}
					
					@Override
					public boolean isVisible() {
						// Only visible for studies using LIMS?
						return true;
					}
					
				});
			}
		}

		ArkAjaxTabbedPanel moduleTabbedPanel = new ArkAjaxTabbedPanel(Constants.MENU_SUBJECT_SUBMENU, moduleSubTabsList);
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