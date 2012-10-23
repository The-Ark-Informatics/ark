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
import org.apache.wicket.extensions.markup.html.tabs.ITab;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import au.org.theark.core.security.ArkPermissionHelper;
import au.org.theark.core.service.IMainTabProvider;
import au.org.theark.core.web.component.ArkMainTab;

/**
 * <p>
 * The <code>PhenotypicTabProviderImpl</code> class that extends the {@link au.org.theark.core.web.component.menu.AbstractArkTabPanel
 * AbstractArkTabPanel} class. It provides the implementation of the Phenotypic tab panel (sub-menu).
 * </p>
 * 
 * @author cellis
 */
public class PhenotypicTabProviderImpl extends Panel implements IMainTabProvider {

	private static final long			serialVersionUID	= -4820350102328281626L;
	private transient static Logger	log					= LoggerFactory.getLogger(PhenotypicTabProviderImpl.class);
	private WebMarkupContainer			arkContextPanelMarkup;
	private List<ITab>					moduleTabsList;

	/**
	 * PhenotypicTabProviderImpl contstructor
	 * 
	 * @param panelId
	 *           The panel indentifier
	 */
	public PhenotypicTabProviderImpl(String panelId) {
		super(panelId);
		moduleTabsList = new ArrayList<ITab>();
	}

	/**
	 * Build the list of tabs (sub-menus)
	 * 
	 * @return A {@link java.util.List List} of {@link org.apache.wicket.extensions.markup.html.tabs.ITab ITab}'s that represent the sub-menu
	 */
	public List<ITab> buildTabs() {
		// Forms the Main Top level Tab
		ITab iTab = createTab(au.org.theark.core.Constants.ARK_MODULE_PHENOTYPIC);
		moduleTabsList.add(iTab);

		return moduleTabsList;
	}

	/**
	 * Build the list of tabs (sub-menus)
	 * 
	 * @param arkContextPanelMarkup
	 * @return A {@link java.util.List List} of {@link org.apache.wicket.extensions.markup.html.tabs.ITab ITab}'s that represent the sub-menu
	 */
	public List<ITab> buildTabs(WebMarkupContainer arkContextPanelMarkup) {
		this.arkContextPanelMarkup = arkContextPanelMarkup;

		// Forms the Main Top level Tab
		ITab iTab = createTab(au.org.theark.core.Constants.ARK_MODULE_PHENOTYPIC);
		moduleTabsList.add(iTab);

		return moduleTabsList;
	}

	public ITab createTab(final String tabName) {
		return new ArkMainTab(new Model<String>(tabName)) {

			private static final long	serialVersionUID	= -9077903025658028710L;

			@Override
			public Panel getPanel(String pid) {
				// The sub menu(s)
				return new PhenotypicSubMenuTab(pid, arkContextPanelMarkup);
			}

			public boolean isAccessible() {
				Long sessionStudyId = (Long) SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.core.Constants.STUDY_CONTEXT_ID);
				if (sessionStudyId == null) {
					this.getPanel(au.org.theark.core.Constants.ARK_MODULE_PHENOTYPIC).error(au.org.theark.core.Constants.NO_STUDY_IN_CONTEXT_MESSAGE);
					return false;
				}
				else
					return true;
			}

			public boolean isVisible() {
				return ArkPermissionHelper.isModuleAccessPermitted(au.org.theark.core.Constants.ARK_MODULE_PHENOTYPIC);
			}
		};
	}

	/**
	 * @param log
	 *           the log to set
	 */
	public static void setLog(Logger log) {
		PhenotypicTabProviderImpl.log = log;
	}

	/**
	 * @return the log
	 */
	public static Logger getLog() {
		return log;
	}
}
