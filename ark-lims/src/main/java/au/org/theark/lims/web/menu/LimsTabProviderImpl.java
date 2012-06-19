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

import org.apache.wicket.extensions.markup.html.tabs.ITab;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import au.org.theark.core.service.IArkCommonService;
import au.org.theark.core.service.IMainTabProvider;
import au.org.theark.core.web.component.ArkMainTab;

public class LimsTabProviderImpl extends Panel implements IMainTabProvider {

	private static final long			serialVersionUID	= -2064073261192985087L;
	private transient static Logger	log					= LoggerFactory.getLogger(LimsTabProviderImpl.class);
	private WebMarkupContainer			arkContextPanelMarkup;
	private WebMarkupContainer			studyNameMarkup;
	private WebMarkupContainer			studyLogoMarkup;
	private List<ITab>					moduleTabsList;
	boolean visible;
	
	@SpringBean(name = au.org.theark.core.Constants.ARK_COMMON_SERVICE)
	private IArkCommonService<Void>	iArkCommonService;

	public LimsTabProviderImpl(String panelId) {
		super(panelId);
		visible = iArkCommonService.getCountOfStudies() > 0;
		moduleTabsList = new ArrayList<ITab>();
	}
	
	@Override
	public boolean isVisible() {
		return visible;
	}

	public List<ITab> buildTabs(WebMarkupContainer arkContextPanelMarkup, WebMarkupContainer studyNameMarkup, WebMarkupContainer studyLogoMarkup) {
		this.arkContextPanelMarkup = arkContextPanelMarkup;
		this.studyNameMarkup = studyNameMarkup;
		this.studyLogoMarkup = studyLogoMarkup;

		// Forms the Main Top level Tab
		ITab iTab = createTab(au.org.theark.core.Constants.ARK_MODULE_LIMS);
		moduleTabsList.add(iTab);

		return moduleTabsList;
	}

	public ITab createTab(final String tabName) {
		return new ArkMainTab(new Model<String>(tabName)) {

			private static final long	serialVersionUID	= 4461043265879777714L;

			@Override
			public Panel getPanel(String pid) {
				// The sub menu(s)
				return new LimsSubMenuTab(pid, arkContextPanelMarkup, studyNameMarkup, studyLogoMarkup);
			}

			public boolean isAccessible() {
				return true;
			}

			public boolean isVisible() {
				return true;
			}
		};
	}

	/**
	 * @param log
	 *           the log to set
	 */
	public static void setLog(Logger log) {
		LimsTabProviderImpl.log = log;
	}

	/**
	 * @return the log
	 */
	public static Logger getLog() {
		return log;
	}
}
