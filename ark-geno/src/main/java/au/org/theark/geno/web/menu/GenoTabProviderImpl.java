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
package au.org.theark.geno.web.menu;

import java.util.ArrayList;
import java.util.List;

import org.apache.shiro.SecurityUtils;
import org.apache.wicket.extensions.markup.html.tabs.ITab;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import au.org.theark.core.model.study.entity.ArkModule;
import au.org.theark.core.security.ArkPermissionHelper;
import au.org.theark.core.service.IArkCommonService;
import au.org.theark.core.service.IMainTabProvider;
import au.org.theark.core.web.component.ArkMainTab;
import au.org.theark.geno.web.Constants;

public class GenoTabProviderImpl extends Panel implements IMainTabProvider
{
	/**
	 * 
	 */
	private static final long	serialVersionUID	= 5330252275905661708L;
	private transient static Logger log = LoggerFactory.getLogger(GenoTabProviderImpl.class);
	@SpringBean(name = au.org.theark.core.Constants.ARK_COMMON_SERVICE)
	private IArkCommonService<Void>	iArkCommonService;
	List<ITab>								moduleTabsList;

	public GenoTabProviderImpl(String panelId)
	{
		super(panelId);
		moduleTabsList = new ArrayList<ITab>();
	}

	public List<ITab> buildTabs()
	{
		log.info("Creating main tab: " + au.org.theark.core.Constants.ARK_MODULE_GENOTYPIC);
		ITab tab1 = createTab(au.org.theark.core.Constants.ARK_MODULE_GENOTYPIC);// Forms the Main Top level Tab
		moduleTabsList.add(tab1);
		return moduleTabsList;
	}

	public ITab createTab(final String tabName)
	{
		return new ArkMainTab(new Model<String>(tabName))
		{
			/**
			 * 
			 */
			private static final long	serialVersionUID	= 197065531396767547L;

			@Override
			public Panel getPanel(String pid)
			{
				GenoSubMenuTab genoSubMenu = new GenoSubMenuTab(pid);// The sub menus Genotypic
				genoSubMenu.buildTabs();
				return genoSubMenu;
			}

			public boolean isAccessible()
			{
				Long sessionStudyId = (Long) SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.core.Constants.STUDY_CONTEXT_ID);
				if (sessionStudyId == null)
				{
					this.getPanel(Constants.GENOTYPIC_MAIN_TAB).error(au.org.theark.core.Constants.NO_STUDY_IN_CONTEXT_MESSAGE);
					return false;
				}
				else
					return true;
			}
			
			public boolean isVisible()
			{
				//ArkModule arkModule = iArkCommonService.getArkModuleByName(tabName);
				//return ArkPermissionHelper.isModuleAccessPermitted(arkModule);
				return true;
			}
		};
	}
}
