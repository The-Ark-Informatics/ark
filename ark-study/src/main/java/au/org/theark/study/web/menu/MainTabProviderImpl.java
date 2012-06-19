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

import org.apache.shiro.SecurityUtils;
import org.apache.wicket.extensions.markup.html.tabs.ITab;
import org.apache.wicket.extensions.markup.html.tabs.TabbedPanel;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;

import au.org.theark.core.service.IMainTabProvider;
import au.org.theark.core.web.component.ArkMainTab;

/**
 * The main class that implements the common service IMainTabProvider.This contributes the Tab menu which forms the entry point into Study module. As
 * part of the main Tab that it contributes it will also contain the sub-menu tabs.This more like a plugin class.
 * 
 * @author nivedann
 * 
 */
public class MainTabProviderImpl extends Panel implements IMainTabProvider {

	private static final long	serialVersionUID	= 1507516631450757896L;
	private List<ITab>			moduleTabsList;
	private WebMarkupContainer	studyNameMarkup;
	private WebMarkupContainer	studyLogoMarkup;
	private WebMarkupContainer	arkContextMarkup;
	private TabbedPanel			moduleTabbedPanel;

	/**
	 * Default constructor. Constructs a new instance of MainTabProviderImpl. Also instantiates the list of sub-men tabs contained within it.
	 * 
	 * @param panelId
	 *           The panel identifer (passed to the Panel super class)
	 */
	public MainTabProviderImpl(String panelId) {
		super(panelId);
		moduleTabsList = new ArrayList<ITab>();
	}

	/**
	 * Builds the list of sub-menu tabs.
	 * 
	 * @param studyLogoMarkup
	 *           The reference to the WebMarkupContainer that contains the study log reference (refreshed on Study selection)
	 * @return the list of sub-menu tabs
	 */
	public List<ITab> buildTabs(WebMarkupContainer studyLogoMarkup) {
		this.studyLogoMarkup = studyLogoMarkup;

		// Main Top level Tabs
		ITab studyTab = createTab(au.org.theark.core.Constants.ARK_MODULE_STUDY);
		ITab subjectTab = createTab(au.org.theark.core.Constants.ARK_MODULE_SUBJECT);
		moduleTabsList.add(studyTab);
		moduleTabsList.add(subjectTab);
		return moduleTabsList;
	}

	/**
	 * Builds the list of sub-menu tabs.
	 * 
	 * @param studyNameMarkup
	 *           The reference to the WebMarkupContainer that contains the study name reference (refreshed on Study selection)
	 * @param studyLogoMarkup
	 *           The reference to the WebMarkupContainer that contains the study log reference (refreshed on Study selection)
	 * @param arkContextMarkup
	 *           The reference to the WebMarkupContainer that contains the context items reference (refreshed on Study selection)
	 * @return the list of sub-menu tabs
	 */
	public List<ITab> buildTabs(WebMarkupContainer studyNameMarkup, WebMarkupContainer studyLogoMarkup, WebMarkupContainer arkContextMarkup) {
		this.studyNameMarkup = studyNameMarkup;
		this.studyLogoMarkup = studyLogoMarkup;
		this.arkContextMarkup = arkContextMarkup;

		// Main Top level Tabs
		ITab studyTab = createTab(au.org.theark.core.Constants.ARK_MODULE_STUDY);
		ITab subjectTab = createTab(au.org.theark.core.Constants.ARK_MODULE_SUBJECT);
		moduleTabsList.add(studyTab);
		moduleTabsList.add(subjectTab);
		return moduleTabsList;
	}

	/**
	 * Builds the list of sub-menu tabs.
	 * 
	 * @param studyNameMarkup
	 *           The reference to the WebMarkupContainer that contains the study name reference (refreshed on Study selection)
	 * @param studyLogoMarkup
	 *           The reference to the WebMarkupContainer that contains the study log reference (refreshed on Study selection)
	 * @param arkContextMarkup
	 *           The reference to the WebMarkupContainer that contains the context items reference (refreshed on Study selection)
	 * @param moduleTabbedPanel
	 *           The reference to the main tab panel (Allows repainting on Study selection to show particular tabs)
	 * @return the list of sub-menu tabs
	 */
	public List<ITab> buildTabs(WebMarkupContainer studyNameMarkup, WebMarkupContainer studyLogoMarkup, WebMarkupContainer arkContextMarkup, TabbedPanel moduleTabbedPanel) {
		this.studyNameMarkup = studyNameMarkup;
		this.studyLogoMarkup = studyLogoMarkup;
		this.arkContextMarkup = arkContextMarkup;
		this.setModuleTabbedPanel(moduleTabbedPanel);

		// Main Top level Tabs
		ITab studyTab = createTab(au.org.theark.core.Constants.ARK_MODULE_STUDY);
		ITab subjectTab = createTab(au.org.theark.core.Constants.ARK_MODULE_SUBJECT);
		moduleTabsList.add(studyTab);
		moduleTabsList.add(subjectTab);
		return moduleTabsList;
	}

	public ITab createTab(final String tabName) {
		if (tabName.equalsIgnoreCase(au.org.theark.core.Constants.ARK_MODULE_STUDY)) {
			return new ArkMainTab(new Model<String>(tabName)) {
				/**
				 * 
				 */
				private static final long	serialVersionUID	= -8671910074409249398L;

				@Override
				public Panel getPanel(String pid) {
					return panelToReturn(pid, tabName);
				}

				public boolean isAccessible() {
					// Study tab is always accessible
					return true;
				}

				public boolean isVisible() {
					// Study tab is always visible
					return true;
				}
			};
		}
		else {
			return new ArkMainTab(new Model<String>(tabName)) {
				/**
				 * 
				 */
				private static final long	serialVersionUID	= -6838973454398478802L;

				@Override
				public Panel getPanel(String pid) {
					return panelToReturn(pid, tabName);
				}

				public boolean isAccessible() {
					// Only accessible when study in session (repainted on Study selection)
					Long sessionStudyId = (Long) SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.core.Constants.STUDY_CONTEXT_ID);
					if (sessionStudyId == null) {
						this.getPanel(au.org.theark.core.Constants.ARK_MODULE_SUBJECT).error(au.org.theark.core.Constants.NO_STUDY_IN_CONTEXT_MESSAGE);
						return false;
					}
					else
						return true;
				}

				public boolean isVisible() {
					// Only visible when study in session (repainted on Study selection)
					Long sessionStudyId = (Long) SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.core.Constants.STUDY_CONTEXT_ID);
					if (sessionStudyId == null) {
						return false;
					}
					else
						return true;
				}
			};
		}
	}

	/**
	 * Creates/returns the panel of sub-menus for the parent main tab
	 * 
	 * @param pid
	 *           The panel identifier
	 * @param tabName
	 *           The name of the main tab
	 * @return The panel of sub-menu tabs for the passed in main tabName
	 */
	public Panel panelToReturn(String pid, String tabName) {
		Panel panelToReturn = null;// Set up a common tab that will be accessible for all users
		if (tabName.equals(au.org.theark.core.Constants.ARK_MODULE_STUDY)) {
			panelToReturn = new StudySubMenuTab(pid, studyNameMarkup, studyLogoMarkup, arkContextMarkup, this);// The sub menus for Study
		}
		else if (tabName.equalsIgnoreCase(au.org.theark.core.Constants.ARK_MODULE_SUBJECT)) {
			panelToReturn = new SubjectSubMenuTab(pid, arkContextMarkup);
		}
		return panelToReturn;
	}

	/**
	 * @param moduleTabbedPanel
	 *           the moduleTabbedPanel to set
	 */
	public void setModuleTabbedPanel(TabbedPanel moduleTabbedPanel) {
		this.moduleTabbedPanel = moduleTabbedPanel;
	}

	/**
	 * @return the moduleTabbedPanel
	 */
	public TabbedPanel getModuleTabbedPanel() {
		return moduleTabbedPanel;
	}
}
