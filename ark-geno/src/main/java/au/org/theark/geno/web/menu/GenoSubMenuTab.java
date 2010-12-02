package au.org.theark.geno.web.menu;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

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
import au.org.theark.geno.web.Constants;
import au.org.theark.geno.web.component.test.TestContainerPanel;
import au.org.theark.geno.web.component.upload.GenoCollectionContainerPanel;
import au.org.theark.geno.web.component.upload.SearchPanel;


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
		menuModule.setModuleName("Upload");
		menuModule.setResourceKey("tab.module.geno.upload");
		moduleTabs.add(menuModule);

		menuModule = new MenuModule();
		menuModule.setModuleName("Test");
		menuModule.setResourceKey("tab.module.geno.test");
		moduleTabs.add(menuModule);

		
		for(final MenuModule moduleName : moduleTabs)
		{
			// use ResourceModel instead of Model<String>(getLocalizer().getString...)
			moduleSubTabsList.add( new AbstractTab(new ResourceModel(moduleName.getResourceKey(), moduleName.getModuleName()) )
			{
				
//				public boolean isVisible(){
//					
//					boolean flag = false;
//					if(moduleName.getModuleName().equalsIgnoreCase(Constants.TEST)){
//						ArkSecurityManager arkSecurityManager = ArkSecurityManager.getInstance();
//						Subject currentUser = SecurityUtils.getSubject();
//						if((arkSecurityManager.subjectHasRole(RoleConstants.ARK_SUPER_ADMIN))){
//							 flag =  currentUser.isAuthenticated();	 
//						}else{
//							 flag = false;
//						}
//					}else{
//						flag=true;
//					}
//					return flag;
//				}
				
				@Override
				public Panel getPanel(String panelId) 
				{
					
					Panel panelToReturn = null;//Set up a common tab that will be accessible for all users
					
					if(moduleName.getModuleName().equalsIgnoreCase("Test")) {
						panelToReturn = new TestContainerPanel(panelId);
					} else if(moduleName.getModuleName().equalsIgnoreCase("Upload")) {
						GenoCollectionContainerPanel genoColPanel = new GenoCollectionContainerPanel(panelId);
						genoColPanel.initialisePanel();
						panelToReturn = genoColPanel;
					}
					
					return panelToReturn;
				};
			});
		}
		
		TabbedPanel moduleTabbedPanel = new TabbedPanel(Constants.GENO_SUBMENU, moduleSubTabsList);
		add(moduleTabbedPanel);
	}
}
