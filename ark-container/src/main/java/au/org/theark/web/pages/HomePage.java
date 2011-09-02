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
package au.org.theark.web.pages;

import java.util.ArrayList;
import java.util.List;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.apache.wicket.PageParameters;
import org.apache.wicket.extensions.markup.html.tabs.ITab;
import org.apache.wicket.extensions.markup.html.tabs.TabbedPanel;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import au.org.theark.admin.web.menu.AdminTabProviderImpl;
import au.org.theark.core.exception.EntityNotFoundException;
import au.org.theark.core.model.study.entity.ArkModule;
import au.org.theark.core.model.study.entity.ArkUser;
import au.org.theark.core.service.IArkCommonService;
import au.org.theark.core.web.component.tabbedPanel.ArkAjaxTabbedPanel;
import au.org.theark.geno.web.menu.GenoTabProviderImpl;
import au.org.theark.lims.web.menu.LimsTabProviderImpl;
import au.org.theark.phenotypic.web.menu.PhenotypicTabProviderImpl;
import au.org.theark.registry.web.menu.RegistryTabProviderImpl;
import au.org.theark.report.web.menu.ReportTabProviderImpl;
import au.org.theark.study.web.menu.MainTabProviderImpl;

/**
 * <p>
 * The <code>HomePage</code> class that extends the {@link au.org.theark.web.pages.BasePage BasePage} class. It provides the implementation of the
 * index page of The Ark application.
 * </p>
 * 
 * @author nivedann
 * @author cellis
 */
public class HomePage extends BasePage {
	private transient static Logger	log	= LoggerFactory.getLogger(HomePage.class);
	private WebMarkupContainer			arkContextPanelMarkup;
	private TabbedPanel					moduleTabbedPanel;

	@SpringBean(name = au.org.theark.core.Constants.ARK_COMMON_SERVICE)
	private IArkCommonService<Void>	iArkCommonService;

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
				if (arkModule.getName().equalsIgnoreCase(au.org.theark.core.Constants.ARK_MODULE_STUDY)) {
					// Study
					studyMainTabProvider = new MainTabProviderImpl(arkModule.getName());
					// Pass in the Study logo mark up, to allow dynamic logo reference
					moduleTabsList = studyMainTabProvider.buildTabs(this.studyNameMarkup, this.studyLogoMarkup, this.arkContextPanelMarkup);
				}

				if (arkModule.getName().equalsIgnoreCase(au.org.theark.core.Constants.ARK_MODULE_PHENOTYPIC)) {
					// Pheno
					PhenotypicTabProviderImpl phenotypicTabProvidor = new PhenotypicTabProviderImpl(arkModule.getName());
					List<ITab> phenotypicTabsList = phenotypicTabProvidor.buildTabs(this.arkContextPanelMarkup);
					for (ITab itab : phenotypicTabsList) {
						moduleTabsList.add(itab);
					}
				}

				if (arkModule.getName().equalsIgnoreCase(au.org.theark.core.Constants.ARK_MODULE_GENOTYPIC)) {
					// Genotypic
					GenoTabProviderImpl genoTabs = new GenoTabProviderImpl(arkModule.getName());
					List<ITab> genoTabsList = genoTabs.buildTabs();
					for (ITab itab : genoTabsList) {
						moduleTabsList.add(itab);
					}
				}

				if (arkModule.getName().equalsIgnoreCase(au.org.theark.core.Constants.ARK_MODULE_REGISTRY)) {
					// Registry
					RegistryTabProviderImpl registryTabProvider = new RegistryTabProviderImpl(arkModule.getName());
					List<ITab> registryTabList = registryTabProvider.buildTabs();
					for (ITab tab : registryTabList) {
						moduleTabsList.add(tab);
					}
				}

				if (arkModule.getName().equalsIgnoreCase(au.org.theark.core.Constants.ARK_MODULE_LIMS)) {
					// LIMS
					LimsTabProviderImpl limsTabProvider = new LimsTabProviderImpl(arkModule.getName());
					List<ITab> limsTabList = limsTabProvider.buildTabs(this.arkContextPanelMarkup);
					for (ITab tab : limsTabList) {
						moduleTabsList.add(tab);
					}
				}
				
				if (arkModule.getName().equalsIgnoreCase(au.org.theark.core.Constants.ARK_MODULE_REPORTING)) {
					// Reporting
					ReportTabProviderImpl reportTabProvider = new ReportTabProviderImpl(arkModule.getName());
					List<ITab> reportTabList = reportTabProvider.buildTabs();
					for (ITab tab : reportTabList) {
						moduleTabsList.add(tab);
					}
				}
				
				if (arkModule.getName().equalsIgnoreCase(au.org.theark.core.Constants.ARK_MODULE_ADMIN)) {
					// Admin
					AdminTabProviderImpl adminTabProvider = new AdminTabProviderImpl(arkModule.getName());
					List<ITab> adminTabList = adminTabProvider.buildTabs();
					for (ITab tab : adminTabList) {
						moduleTabsList.add(tab);
					}
				}
			}
		}
		catch (EntityNotFoundException e) {
			log.error("ArkUser [" + ldapUserName + "] was not found!");
			log.error(e.getMessage());
			
			// Study
			studyMainTabProvider = new MainTabProviderImpl(au.org.theark.core.Constants.ARK_MODULE_STUDY);
			// Pass in the Study logo mark up, to allow dynamic logo reference
			moduleTabsList = studyMainTabProvider.buildTabs(this.studyNameMarkup, this.studyLogoMarkup, this.arkContextPanelMarkup);
			
			// Reporting
			ReportTabProviderImpl reportTabProvider = new ReportTabProviderImpl(au.org.theark.core.Constants.ARK_MODULE_REPORTING);
			List<ITab> reportTabList = reportTabProvider.buildTabs();
			for (ITab tab : reportTabList) {
				moduleTabsList.add(tab);
			}
		}

		moduleTabbedPanel = new ArkAjaxTabbedPanel("moduleTabsList", moduleTabsList);
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
