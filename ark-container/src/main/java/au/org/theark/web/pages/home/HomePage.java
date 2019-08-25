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
package au.org.theark.web.pages.home;

import java.util.ArrayList;
import java.util.List;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.ajax.IAjaxIndicatorAware;
import org.apache.wicket.extensions.markup.html.tabs.ITab;
import org.apache.wicket.extensions.markup.html.tabs.TabbedPanel;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import au.org.theark.core.exception.ArkSystemException;
import au.org.theark.core.exception.EntityNotFoundException;
import au.org.theark.core.model.study.entity.ArkModule;
import au.org.theark.core.model.study.entity.ArkUser;
import au.org.theark.core.model.study.entity.LinkSubjectStudy;
import au.org.theark.core.model.study.entity.Study;
import au.org.theark.core.service.IArkCommonService;
import au.org.theark.core.util.ContextHelper;
import au.org.theark.core.web.component.tabbedPanel.ArkAjaxTabbedPanel;
import au.org.theark.lims.web.component.panel.applet.PrintAppletPanel;
import au.org.theark.registry.web.menu.RegistryTabProviderImpl;
import au.org.theark.study.service.IStudyService;
import au.org.theark.study.web.Constants;
import au.org.theark.web.menu.AdminTabProviderImpl;
import au.org.theark.web.menu.CalendarTabProviderImpl;
import au.org.theark.web.menu.DiseaseTabProviderImpl;
import au.org.theark.web.menu.GenomicsTabProviderImpl;
import au.org.theark.web.menu.LimsTabProviderImpl;
import au.org.theark.web.menu.MainTabProviderImpl;
import au.org.theark.web.menu.PhenotypicTabProviderImpl;
import au.org.theark.web.menu.ReportTabProviderImpl;
import au.org.theark.web.menu.WorkTrackingTabProviderImpl;
import au.org.theark.web.pages.login.LoginPage;

/**
 * <p>
 * The <code>HomePage</code> class that extends the {@link au.org.theark.web.pages.home.BasePage BasePage} class. It provides the implementation of the
 * index page of The Ark application.
 * </p>
 * 
 * @author nivedann
 * @author cellis
 */
public class HomePage extends BasePage {

	private static final long	serialVersionUID	= 6042144198163845254L;
	private transient static Logger	log	= LoggerFactory.getLogger(HomePage.class);
	private WebMarkupContainer			arkContextPanelMarkup;
	private TabbedPanel					moduleTabbedPanel;

	@SpringBean(name = au.org.theark.core.Constants.ARK_COMMON_SERVICE)
	private IArkCommonService<Void>	iArkCommonService;

	@SpringBean(name = Constants.STUDY_SERVICE)
	private IStudyService											studyService;
	
	/**
	 * Constructor that is invoked when page is invoked without a session.
	 * 
	 * @param parameters
	 *           Page parameters
	 */
	public HomePage(final PageParameters parameters) {
		Subject currentUser = SecurityUtils.getSubject();

		if (currentUser.getPrincipal() != null) {
			buildContextPanel();
			buildModuleTabs();
		}
		else {
			setResponsePage(LoginPage.class);
		}
		
		// Applet used for barcode printing
		PrintAppletPanel printAppletPanel = new PrintAppletPanel("printAppletPanel", "zebra");
		printAppletPanel.add(new AttributeModifier("class", "floatLeft"));
		this.add(printAppletPanel);
	}

	public void onBeforeRender(){
		
		super.onBeforeRender();
		
		Long studyIdInSession = (Long) SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.core.Constants.STUDY_CONTEXT_ID);
		
		Study study = null;
		if(studyIdInSession != null){
			study  = iArkCommonService.getStudy(studyIdInSession);
		}
		  
		if(study != null){
			ContextHelper contextHelper = new ContextHelper();
			contextHelper.setStudyContextLabel(study.getName(), this.arkContextPanelMarkup);
			Long sessionPersonId = (Long) SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.core.Constants.PERSON_CONTEXT_ID);
			String sessionPersonType = (String) SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.core.Constants.PERSON_TYPE);
			if (sessionPersonId != null && sessionPersonType != null && sessionPersonType.equals(au.org.theark.core.Constants.PERSON_CONTEXT_TYPE_SUBJECT)) {
			
				try {
					//todo:  are we are getting just to catch an exception and log an error????
					studyService.getPerson(sessionPersonId);
					LinkSubjectStudy lss  = iArkCommonService.getSubject(sessionPersonId, study);
					contextHelper.setSubjectContextLabel(lss.getSubjectUID(), this.arkContextPanelMarkup);
					contextHelper.setSubjectNameContextLabel(lss.getPerson().getFullName(), this.arkContextPanelMarkup);
				}
				catch (EntityNotFoundException e) {
					log.error(e.getMessage());
				}
				catch (ArkSystemException e) {
					log.error(e.getMessage());
				}
			}
		}
	
	}
	/**
	 * Builds the ContextPanel that is used to store/show the context items in session, such as Study, SubjectUID etc)
	 */
	protected void buildContextPanel() {
		this.arkContextPanelMarkup = new WebMarkupContainer("contextMarkupContainer");

		// Require a Label for each context item (currently hard coded in mark up)
		// TODO: Use dynamic component to remove hard-coded Labels
		Label studyLabel = new Label("studyLabel", "");
		arkContextPanelMarkup.add(studyLabel);
		Label subjectLabel = new Label("subjectLabel", "");
		arkContextPanelMarkup.add(subjectLabel);
		Label subjectNameLabel = new Label("subjectNameLabel", "");
		arkContextPanelMarkup.add(subjectNameLabel);
		Label phenoLabel = new Label("phenoLabel", "");
		arkContextPanelMarkup.add(phenoLabel);
		Label genoLabel = new Label("genoLabel", "");
		arkContextPanelMarkup.add(genoLabel);
		add(arkContextPanelMarkup);
		arkContextPanelMarkup.setOutputMarkupPlaceholderTag(true);
	}

	/**
	 * Build the list of main tabs/modules based on the current logged in user
	 */
	@Override
	protected void buildModuleTabs() {
		List<ITab> moduleTabsList = new ArrayList<ITab>(0);
		List<ArkModule> arkModuleList = new ArrayList<ArkModule>(0);
		Subject currentUser = SecurityUtils.getSubject();
		String ldapUserName = currentUser.getPrincipal().toString();
		
		MainTabProviderImpl studyMainTabProvider = null;

		try {
			ArkUser arkUser = iArkCommonService.getArkUser(ldapUserName);
			arkModuleList = iArkCommonService.getArkModuleListByArkUser(arkUser);
			
			for (ArkModule arkModule: arkModuleList) {				
				//log.info("arkModule: " + arkModule.getName());
				if (arkModule.getName().equalsIgnoreCase(au.org.theark.core.Constants.ARK_MODULE_STUDY)) {
					// Study
					studyMainTabProvider = new MainTabProviderImpl(arkModule.getName());
					// Pass in the Study logo mark up, to allow dynamic logo reference
					moduleTabsList = studyMainTabProvider.buildTabs(this.studyNameMarkup, this.studyLogoMarkup, this.arkContextPanelMarkup);
				}
				
				if (arkModule.getName().equalsIgnoreCase(au.org.theark.core.Constants.ARK_MODULE_DISEASE)) {
					//Disease
					DiseaseTabProviderImpl diseaseTabProvider = new DiseaseTabProviderImpl(arkModule.getName());
					List<ITab> diseaseTabList = diseaseTabProvider.buildTabs(this.arkContextPanelMarkup);
					moduleTabsList.addAll(diseaseTabList);
				}
				

				if (arkModule.getName().equalsIgnoreCase(au.org.theark.core.Constants.ARK_MODULE_PHENOTYPIC)) {
					// Pheno
					PhenotypicTabProviderImpl phenotypicTabProvidor = new PhenotypicTabProviderImpl(arkModule.getName());
					List<ITab> phenotypicTabsList = phenotypicTabProvidor.buildTabs(this.arkContextPanelMarkup);
					for (ITab itab : phenotypicTabsList) {
						moduleTabsList.add(itab);
					}
				}

				if (arkModule.getName().equalsIgnoreCase(au.org.theark.core.Constants.ARK_MODULE_LIMS)) {
					// LIMS
					LimsTabProviderImpl limsTabProvider = new LimsTabProviderImpl(arkModule.getName());
					List<ITab> limsTabList = limsTabProvider.buildTabs(this.arkContextPanelMarkup, this.studyNameMarkup, this.studyLogoMarkup);
					for (ITab tab : limsTabList) {
						moduleTabsList.add(tab);
					}
				}
				
				if(arkModule.getName().equalsIgnoreCase(au.org.theark.core.Constants.ARK_MODULE_WORKTRACKING)){
					//  Work
					WorkTrackingTabProviderImpl workTrackingTabProvider=new WorkTrackingTabProviderImpl(arkModule.getName());
					List<ITab> workTabList = workTrackingTabProvider.buildTabs();
					for (ITab tab : workTabList) {
						moduleTabsList.add(tab);
					}
				}
				
				if(arkModule.getName().equalsIgnoreCase(au.org.theark.core.Constants.ARK_MODULE_REGISTRY)){
					//  Registry
					RegistryTabProviderImpl regoTab = new RegistryTabProviderImpl(arkModule.getName());
					List<ITab> regoTabList = regoTab.buildTabs();
					for (ITab tab : regoTabList) {
						moduleTabsList.add(tab);
					}
				}
								
				if(arkModule.getName().equalsIgnoreCase(au.org.theark.core.Constants.ARK_MODULE_REPORTING)){
					// Reporting always displayed, but data extraction function requires role/permisssion 
					ReportTabProviderImpl reportTabProvider = new ReportTabProviderImpl((au.org.theark.core.Constants.ARK_MODULE_REPORTING));
					List<ITab> reportTabList = reportTabProvider.buildTabs();
					for (ITab tab : reportTabList) {
						moduleTabsList.add(tab);
					}
				}
				
				if(arkModule.getName().equalsIgnoreCase(au.org.theark.core.Constants.ARK_MODULE_GENOMICS)){
					// Reporting always displayed, but data extraction function requires role/permisssion 
					GenomicsTabProviderImpl genomicsTabProvider = new GenomicsTabProviderImpl((au.org.theark.core.Constants.ARK_MODULE_GENOMICS));
					List<ITab> genomicsTabList = genomicsTabProvider.buildTabs();
					for (ITab tab : genomicsTabList) {
						moduleTabsList.add(tab);
					}
				}
				//Add the Calendar as a Module
				if(arkModule.getName().equalsIgnoreCase(au.org.theark.core.Constants.ARK_MODULE_CALENDAR)){
					CalendarTabProviderImpl calendarTabProvider = new CalendarTabProviderImpl((au.org.theark.core.Constants.ARK_MODULE_CALENDAR));
					List<ITab> calendarTabList = calendarTabProvider.buildTabs();
					for (ITab tab : calendarTabList) {
						moduleTabsList.add(tab);
					}
				}
				
			}
			
			//Only add the global search tab once, at the end of the tabs
			ITab globalSearchTab = studyMainTabProvider.createTab(au.org.theark.core.Constants.ARK_MODULE_GLOBAL_SEARCH);
			moduleTabsList.add(globalSearchTab);
			
			// Only display admin tab for the super user
			ArkModule arkModule = iArkCommonService.getArkModuleByName(au.org.theark.core.Constants.ARK_MODULE_ADMIN);
			if (arkModuleList.contains(arkModule)) {
				// Admin
				AdminTabProviderImpl adminTabProvider = new AdminTabProviderImpl(arkModule.getName());
				List<ITab> adminTabList = adminTabProvider.buildTabs();
				for (ITab tab : adminTabList) {
					moduleTabsList.add(tab);
				}
			}
		}
		catch (EntityNotFoundException e) {
			log.error("ArkUser [" + ldapUserName + "] was not found!");
			log.error(e.getMessage());
		}
		
		moduleTabbedPanel = new ArkAjaxTabbedPanel("moduleTabsList", moduleTabsList, arkContextPanelMarkup);
		moduleTabbedPanel.setOutputMarkupPlaceholderTag(true);
		studyMainTabProvider.setModuleTabbedPanel(moduleTabbedPanel);
		add(moduleTabbedPanel);
	}

	@Override
	String getTitle() {
		return "ArkHomePage";
	}

	/**
	 * @param log
	 *           the log to set
	 */
	public static void setLog(Logger log) {
		HomePage.log = log;
	}

	/**
	 * @return the log
	 */
	public static Logger getLog() {
		return log;
	}

	
}
