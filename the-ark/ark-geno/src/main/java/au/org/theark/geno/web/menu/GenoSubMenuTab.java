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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import mx4j.log.Log;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.apache.wicket.extensions.markup.html.tabs.AbstractTab;
import org.apache.wicket.extensions.markup.html.tabs.ITab;
import org.apache.wicket.extensions.markup.html.tabs.TabbedPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.ResourceModel;

import au.org.theark.core.security.ArkSecurityManager;
import au.org.theark.core.security.RoleConstants;
import au.org.theark.core.vo.ArkUserVO;
import au.org.theark.core.vo.MenuModule;
import au.org.theark.core.web.component.tabbedPanel.ArkAjaxTabbedPanel;
import au.org.theark.geno.web.Constants;
import au.org.theark.geno.web.component.genoCollection.GenoCollectionContainerPanel;
import au.org.theark.geno.web.component.genoCollection.SearchPanel;
import au.org.theark.geno.web.component.test.TestContainerPanel;
import au.org.theark.geno.web.component.upload.UploadContainerPanel;


public class GenoSubMenuTab extends Panel {

	List<ITab> tabList;
	
	public GenoSubMenuTab(String id) {
		super(id);
		tabList = new ArrayList<ITab>();
	}

	/**
	 * NB: Call this after new GenoSubMenuTab, but not within its constructor
	 */
	public  void buildTabs(){
		
		List<ITab> moduleSubTabsList = new ArrayList<ITab>();
		List<MenuModule> moduleTabs = new ArrayList<MenuModule>();
		
		//THis way we can get the menus from the back-end. We should source this data from a table in the backend and wrap it up in a class like this
		MenuModule menuModule = new MenuModule();
		menuModule.setModuleName(Constants.GENO_SUBMENU_COLLECTION);
		menuModule.setResourceKey(Constants.GENO_RESOURCEKEY_COLLECTION);
		moduleTabs.add(menuModule);

		menuModule = new MenuModule();
		menuModule.setModuleName(Constants.GENO_SUBMENU_UPLOAD);
		menuModule.setResourceKey(Constants.GENO_RESOURCEKEY_UPLOAD);
		moduleTabs.add(menuModule);
		
		menuModule = new MenuModule();
		menuModule.setModuleName(Constants.GENO_SUBMENU_TEST);
		menuModule.setResourceKey(Constants.GENO_RESOURCEKEY_TEST);
		moduleTabs.add(menuModule);

		for(final MenuModule moduleName : moduleTabs)
		{
			// use ResourceModel instead of Model<String>(getLocalizer().getString...) to avoid a warning like this:
			//- Tried to retrieve a localized string for a component that has not yet been added to the page. This can sometimes lead to an invalid or no localized resource returned. Make sure you are not calling Component#getString() inside your Component's constructor. Offending component: [MarkupContainer [Component id = panel]]
			moduleSubTabsList.add( new AbstractTab(new ResourceModel(moduleName.getResourceKey(), moduleName.getModuleName()) )
			{
				
				public boolean isVisible() {
					
					boolean flag = false;
					// only super-admins have access to the Test sub-menu
					if (moduleName.getModuleName().equalsIgnoreCase(Constants.GENO_SUBMENU_TEST)) {
						ArkSecurityManager arkSecurityManager = ArkSecurityManager.getInstance();
						Subject currentUser = SecurityUtils.getSubject();
						if (arkSecurityManager.subjectHasRole(RoleConstants.ARK_ROLE_SUPER_ADMINISTATOR)) {
							System.out.println("Used Ark super admin role");
							flag =  currentUser.isAuthenticated();
						} else if (arkSecurityManager.subjectHasRole("'Geno Read-Only User'")) {
							System.out.println("Used GWAS read only role");
							flag =  currentUser.isAuthenticated();	 
						} else {
							flag = false;
						}
						//TODO: Temporarily Super admin doesn't seem to work.  Turn on Test tab if forced by constant.
						if (Constants.GENO_SUBMENU_TEST_FORCE_ON) {
							flag = true;
						}
					} else {
						flag=true;
					}
					return flag;
				}
				
				@Override
				public Panel getPanel(String panelId) 
				{
					
					Panel panelToReturn = null;//Set up a common tab that will be accessible for all users
					
					if (moduleName.getModuleName().equalsIgnoreCase(Constants.GENO_SUBMENU_TEST)) {
						panelToReturn = new TestContainerPanel(panelId);
					} else if(moduleName.getModuleName().equalsIgnoreCase(Constants.GENO_SUBMENU_COLLECTION)) {
						GenoCollectionContainerPanel genoColPanel = new GenoCollectionContainerPanel(panelId);
						genoColPanel.initialisePanel();
						panelToReturn = genoColPanel;
					} else if(moduleName.getModuleName().equalsIgnoreCase(Constants.GENO_SUBMENU_UPLOAD)) {
						UploadContainerPanel uploadPanel = new UploadContainerPanel(panelId);
						uploadPanel.initialisePanel();
						panelToReturn = uploadPanel;
					}
					
					return panelToReturn;
				};
			});
		}
		
		ArkAjaxTabbedPanel moduleTabbedPanel = new ArkAjaxTabbedPanel(Constants.GENO_SUBMENU, moduleSubTabsList);
		add(moduleTabbedPanel);
	}
}
