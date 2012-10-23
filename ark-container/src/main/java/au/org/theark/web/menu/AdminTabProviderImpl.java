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
import org.apache.wicket.extensions.markup.html.tabs.ITab;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;

import au.org.theark.core.service.IMainTabProvider;
import au.org.theark.core.web.component.ArkMainTab;

public class AdminTabProviderImpl extends Panel implements IMainTabProvider {

	private static final long	serialVersionUID	= 1L;
	private List<ITab>			moduleTabsList;

	public AdminTabProviderImpl(String panelId) {
		super(panelId);
		moduleTabsList = new ArrayList<ITab>();
	}

	public List<ITab> buildTabs() {
		// Main tab
		ITab iTab = createTab(au.org.theark.core.Constants.ARK_MODULE_ADMIN);
		moduleTabsList.add(iTab);

		return moduleTabsList;
	}

	public ITab createTab(String tabName) {
		return new ArkMainTab(new Model<String>(tabName)) {

			private static final long	serialVersionUID	= -5063032622932238615L;

			@Override
			public Panel getPanel(String pid) {
				// The sub menu(s) for Admin
				return new AdminSubMenuTab(pid);
			}

			public boolean isAccessible() {
				return true;
			}

			public boolean isVisible() {
				boolean flag = false;
				SecurityManager securityManager = ThreadContext.getSecurityManager();
				Subject currentUser = SecurityUtils.getSubject();

				// Only a Super Administrator can see the Admin tab/menu
				if (securityManager.hasRole(currentUser.getPrincipals(), au.org.theark.core.security.RoleConstants.ARK_ROLE_SUPER_ADMINISTATOR)) {
					flag = currentUser.isAuthenticated();
				}
				else {
					flag = false;
				}
				return flag;
			}
		};
	}
}
