package au.org.theark.study.web.menu;

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

import au.org.theark.core.security.ArkSecurityManager;
import au.org.theark.core.security.RoleConstants;
import au.org.theark.core.vo.ArkUserVO;
import au.org.theark.study.web.Constants;
import au.org.theark.study.web.component.consent.ConsentContainerPanel;
import au.org.theark.study.web.component.managestudy.StudyContainer;
import au.org.theark.study.web.component.mydetails.MyDetailsContainer;
import au.org.theark.study.web.component.site.SiteContainerPanel;
import au.org.theark.study.web.component.study.StudyContainerPanel;
import au.org.theark.study.web.component.studycomponent.StudyComponentContainerPanel;
import au.org.theark.study.web.component.user.UserContainer;



@SuppressWarnings("serial")
public class StudySubMenuTab extends Panel {
	
	List<ITab> tabList;
	
	public StudySubMenuTab(String id) {
		super(id);
		tabList = new ArrayList<ITab>();
		buildTabs();
	}
	

	private class MenuModule implements Serializable{
		
		public MenuModule(){
			super();
		}
		private String moduleName;
		private String resourceKey;
		public String getModuleName() {
			return moduleName;
		}
		public void setModuleName(String moduleName) {
			this.moduleName = moduleName;
		}
		public String getResourceKey() {
			return resourceKey;
		}
		public void setResourceKey(String resourceKey) {
			this.resourceKey = resourceKey;
		}
		
	}
	
	public  void buildTabs(){
		
		List<ITab> moduleSubTabsList = new ArrayList<ITab>();
		List<MenuModule> moduleTabs = new ArrayList<MenuModule>();
		
		//THis way we can get the menus from the back-end. We should source this data from a table in the backend and wrap it up in a class like this
		MenuModule menuModule = new MenuModule();
		menuModule.setModuleName("Study Details");
		menuModule.setResourceKey("tab.module.study.details");
		moduleTabs.add(menuModule);
		
		menuModule = new MenuModule();
		menuModule.setModuleName("Sites");
		menuModule.setResourceKey("tab.module.sites");
		moduleTabs.add(menuModule);

		menuModule = new MenuModule();
		menuModule.setModuleName("Study Components");
		menuModule.setResourceKey("tab.module.study.components");
		moduleTabs.add(menuModule);
		
		menuModule = new MenuModule();
		menuModule.setModuleName("Users");
		menuModule.setResourceKey("tab.module.users");
		moduleTabs.add(menuModule);

		menuModule = new MenuModule();
		menuModule.setModuleName("My Details");
		menuModule.setResourceKey("tab.module.mydetails");
		moduleTabs.add(menuModule);
		
		for(final MenuModule moduleName : moduleTabs)
		{
			moduleSubTabsList.add( new AbstractTab(new Model<String>(getLocalizer().getString(moduleName.getResourceKey(), StudySubMenuTab.this, moduleName.getModuleName())) )
			{
				
				
				public boolean isVisible(){
					
					boolean flag = false;
					if(moduleName.getModuleName().equalsIgnoreCase(Constants.USERS)){
						ArkSecurityManager arkSecurityManager = ArkSecurityManager.getInstance();
						Subject currentUser = SecurityUtils.getSubject();
						if((arkSecurityManager.subjectHasRole(RoleConstants.ARK_SUPER_ADMIN))){
							 flag =  currentUser.isAuthenticated();	 
						}else{
							 flag = false;
						}
					}else{
						flag=true;
					}
					return flag;
				}
				
				@Override
				public Panel getPanel(String panelId) 
				{
					
					Panel panelToReturn = null;//Set up a common tab that will be accessible for all users
					
					if(moduleName.getModuleName().equalsIgnoreCase(Constants.USERS)){
						
						panelToReturn = new UserContainer(panelId, new ArkUserVO());//Note the constructor
					
					}else if(moduleName.getModuleName().equalsIgnoreCase(Constants.STUDY_DETAILS)){
					
						panelToReturn = new StudyContainer(panelId);
					
					}else if(moduleName.getModuleName().equalsIgnoreCase(Constants.SITES)){
						
						panelToReturn = new SiteContainerPanel(panelId);
					
					}else if(moduleName.getModuleName().equalsIgnoreCase(Constants.STUDY_COMPONENTS)){
					
						panelToReturn = new StudyComponentContainerPanel(panelId);
					
					}else if(moduleName.getModuleName().equalsIgnoreCase(Constants.MY_DETAILS)){
						
						Subject currentUser = SecurityUtils.getSubject();
						panelToReturn = new MyDetailsContainer(panelId,new ArkUserVO(),currentUser );
					}
					
					return panelToReturn;
				};
			});
		}
		
		TabbedPanel moduleTabbedPanel = new TabbedPanel(Constants.MENU_STUDY_SUBMENU, moduleSubTabsList);
		add(moduleTabbedPanel);
	}
}


