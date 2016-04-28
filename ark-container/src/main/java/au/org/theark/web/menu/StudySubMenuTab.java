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
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.ThreadContext;
import org.apache.wicket.extensions.markup.html.tabs.AbstractTab;
import org.apache.wicket.extensions.markup.html.tabs.ITab;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import au.org.theark.core.Constants;
import au.org.theark.core.model.study.entity.ArkFunction;
import au.org.theark.core.model.study.entity.ArkModule;
import au.org.theark.core.model.study.entity.Study;
import au.org.theark.core.security.ArkPermissionHelper;
import au.org.theark.core.service.IArkCommonService;
import au.org.theark.core.web.component.customfield.CustomFieldContainerPanel;
import au.org.theark.core.web.component.customfieldcategory.CustomFieldCategoryContainerPanel;
import au.org.theark.core.web.component.customfieldupload.CustomFieldUploadContainerPanel;
import au.org.theark.core.web.component.menu.AbstractArkTabPanel;
import au.org.theark.core.web.component.tabbedPanel.ArkAjaxTabbedPanel;
import au.org.theark.study.web.component.calendar.CalendarContainerPanel;
import au.org.theark.study.web.component.managestudy.StudyContainerPanel;
import au.org.theark.study.web.component.manageuser.UserContainerPanel;
import au.org.theark.study.web.component.studycomponent.StudyComponentContainerPanel;
import au.org.theark.study.web.component.subjectUpload.SubjectUploadContainerPanel;


/**
 * <p>
 * The <code>StudySubMenuTab</code> class that extends the {@link au.org.theark.core.web.component.menu.AbstractArkTabPanel AbstractArkTabPanel}
 * class. It provides the implementation of the Study tab panel (sub-menu).
 * </p>
 * 
 * @author nivedann
 * @author cellis
 */
public class StudySubMenuTab extends AbstractArkTabPanel {

	private static final long			serialVersionUID	= -2725142200726870636L;
	private transient static Logger	log					= LoggerFactory.getLogger(StudySubMenuTab.class);
	@SpringBean(name = au.org.theark.core.Constants.ARK_COMMON_SERVICE)
	private IArkCommonService<Void>	iArkCommonService;

	private WebMarkupContainer			studyNameMarkup;
	private WebMarkupContainer			studyLogoMarkup;
	private WebMarkupContainer			arkContextMarkup;
	private MainTabProviderImpl		mainTabProvider;
	private List<ITab>					moduleSubTabsList	= new ArrayList<ITab>();
	

	/**
	 * StudySubMenuTab Constructor
	 * 
	 * @param id
	 *           this component identifier
	 * @param studyNameMarkup
	 *           the WebMarkupContainer that references the study name in context
	 * @param studyLogoMarkup
	 *           the WebMarkupContainer that references the study logo in context
	 * @param arkContextMarkup
	 *           the WebMarkupContainer that references the context items
	 */
	public StudySubMenuTab(String id, WebMarkupContainer studyNameMarkup, WebMarkupContainer studyLogoMarkup, WebMarkupContainer arkContextMarkup) {
		super(id);
		this.studyNameMarkup = studyNameMarkup;
		this.studyLogoMarkup = studyLogoMarkup;
		this.arkContextMarkup = arkContextMarkup;
		buildTabs();
	}

	/**
	 * StudySubMenuTab Constructor
	 * 
	 * @param id
	 *           this component identifier
	 * @param studyNameMarkup
	 *           the WebMarkupContainer that references the study name in context
	 * @param studyLogoMarkup
	 *           the WebMarkupContainer that references the study logo in context
	 * @param arkContextMarkup
	 *           the WebMarkupContainer that references the context items
	 * @param mainTabProvider
	 *           the reference to the main tabs (to allow repaint on Study selection)
	 */
	public StudySubMenuTab(String id, WebMarkupContainer studyNameMarkup, WebMarkupContainer studyLogoMarkup, WebMarkupContainer arkContextMarkup, MainTabProviderImpl mainTabProvider) {
		super(id);
		this.studyNameMarkup = studyNameMarkup;
		this.studyLogoMarkup = studyLogoMarkup;
		this.arkContextMarkup = arkContextMarkup;
		this.mainTabProvider = mainTabProvider;
		buildTabs();
	}

	/**
	 * Build the list of tabs that represent the sub-menus
	 */
	public void buildTabs() {
		ArkModule arkModule = iArkCommonService.getArkModuleByName(Constants.ARK_MODULE_STUDY);
		List<ArkFunction> arkFunctionList = iArkCommonService.getModuleFunction(arkModule);// Gets a list of ArkFunctions for the given Module

		/*
		 * Iterate each ArkFunction render the Tabs.When something is clicked it uses the arkFunction and calls processAuthorizationCache to clear
		 * principals of the user and loads the new set of principals.(permissions)
		 */
		for (final ArkFunction arkFunction : arkFunctionList) {
			moduleSubTabsList.add(new AbstractTab(new StringResourceModel(arkFunction.getResourceKey(), this, null)) {
				/**
				 * 
				 */
				private static final long	serialVersionUID	= -8421399480756599074L;

				@Override
				public Panel getPanel(String panelId) {
					Panel panelToReturn = null;// Set up a common tab that will be accessible for all users

					// Clear authorisation cache
					processAuthorizationCache(au.org.theark.core.Constants.ARK_MODULE_STUDY, arkFunction);
					
					if (arkFunction.getName().equalsIgnoreCase(au.org.theark.core.Constants.FUNCTION_KEY_VALUE_STUDY)) {
						panelToReturn = new StudyContainerPanel(panelId, studyNameMarkup, studyLogoMarkup, arkContextMarkup, mainTabProvider.getModuleTabbedPanel());
					}
					else if (arkFunction.getName().equalsIgnoreCase(au.org.theark.core.Constants.FUNCTION_KEY_VALUE_STUDY_COMPONENT)) {
						panelToReturn = new StudyComponentContainerPanel(panelId);
					}
					else if (arkFunction.getName().equalsIgnoreCase(au.org.theark.core.Constants.FUNCTION_KEY_VALUE_USER)) {
						panelToReturn = new UserContainerPanel(panelId);
					}
					//Added on 2015-06-22 Categorize  the custom field.
					//Changed the Constant value from "Subject" to "Study" on 2015-08-17.        
					else if (arkFunction.getName().equalsIgnoreCase(au.org.theark.core.Constants.FUNCTION_KEY_VALUE_SUBJECT_CUSTOM_FIELD_CATEGORY)){
						panelToReturn = new CustomFieldCategoryContainerPanel(panelId, true, iArkCommonService.getArkFunctionByName(au.org.theark.core.Constants.FUNCTION_KEY_VALUE_SUBJECT_CUSTOM_FIELD_CATEGORY));
						
					}else if (arkFunction.getName().equalsIgnoreCase(au.org.theark.core.Constants.FUNCTION_KEY_VALUE_SUBJECT_CUSTOM_FIELD)) {
						panelToReturn = new CustomFieldContainerPanel(panelId, true, iArkCommonService.getArkFunctionByName(au.org.theark.core.Constants.FUNCTION_KEY_VALUE_SUBJECT_CUSTOM_FIELD));
					}
					else if (arkFunction.getName().equalsIgnoreCase(au.org.theark.core.Constants.FUNCTION_KEY_VALUE_SUBJECT_CUSTOM_FIELD_UPLOAD)) {
						panelToReturn = new CustomFieldUploadContainerPanel(panelId, iArkCommonService.getArkFunctionByName(au.org.theark.core.Constants.FUNCTION_KEY_VALUE_SUBJECT_CUSTOM_FIELD_UPLOAD));
					}
					else if (arkFunction.getName().equalsIgnoreCase(au.org.theark.core.Constants.FUNCTION_KEY_VALUE_STUDY_STUDY_DATA_UPLOAD)) {
						panelToReturn = new SubjectUploadContainerPanel(panelId, arkFunction);
					}
					else if (arkFunction.getName().equalsIgnoreCase(au.org.theark.core.Constants.FUNCTION_KEY_VALUE_CALENDAR)) {
						panelToReturn = new CalendarContainerPanel(panelId);
					}
					return panelToReturn;
				}
				
				@Override
				public boolean isVisible() {
					if(arkFunction.getName().equalsIgnoreCase(au.org.theark.core.Constants.FUNCTION_KEY_VALUE_STUDY)) {
						// Study function always visible
						return true;
					}
					else {
						// Other functions require study in context 
						Long sessionStudyId = (Long) SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.core.Constants.STUDY_CONTEXT_ID);
						// Subject Upload only visible to parent studies 
						if (arkFunction.getName().equalsIgnoreCase(au.org.theark.core.Constants.FUNCTION_KEY_VALUE_USER)) {
							processAuthorizationCache(au.org.theark.core.Constants.ARK_MODULE_STUDY, arkFunction);
							SecurityManager securityManager = ThreadContext.getSecurityManager();
							Subject currentUser = SecurityUtils.getSubject();
							return ArkPermissionHelper.hasEditPermission(securityManager, currentUser) && sessionStudyId != null;
						}
						return sessionStudyId != null;
					}
				}
			});
		}

		ArkAjaxTabbedPanel moduleTabbedPanel = new ArkAjaxTabbedPanel(Constants.MENU_STUDY_SUBMENU, moduleSubTabsList, arkContextMarkup);
		add(moduleTabbedPanel);
	}

	/**
	 * @param log
	 *           the log to set
	 */
	public static void setLog(Logger log) {
		StudySubMenuTab.log = log;
	}

	/**
	 * @return the log
	 */
	public static Logger getLog() {
		return log;
	}
}
