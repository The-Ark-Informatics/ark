package au.org.theark.study.web.menu;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.ThreadContext;
import org.apache.wicket.extensions.markup.html.tabs.AbstractTab;
import org.apache.wicket.extensions.markup.html.tabs.ITab;
import org.apache.wicket.extensions.markup.html.tabs.TabbedPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;

import au.org.theark.core.security.RoleConstants;
import au.org.theark.core.vo.EtaUserVO;
import au.org.theark.study.web.Constants;
import au.org.theark.study.web.component.consent.ConsentContainerPanel;
import au.org.theark.study.web.component.site.SiteContainerPanel;
import au.org.theark.study.web.component.study.StudyContainerPanel;
import au.org.theark.study.web.component.studycomponent.StudyComponentContainerPanel;
import au.org.theark.study.web.component.user.MyDetailsContainer;
import au.org.theark.study.web.component.user.UserContainerPanel;



public class StudySubMenuTab extends Panel {
	
	List<ITab> tabList;
	
	transient SecurityManager securityManager =  ThreadContext.getSecurityManager();
	transient Subject currentUser = SecurityUtils.getSubject();
	boolean isSuperAdmin = securityManager.hasRole(currentUser.getPrincipals(), RoleConstants.ARK_SUPER_ADMIN);
	boolean isStudyAdmin = securityManager.hasRole(currentUser.getPrincipals(), RoleConstants.STUDY_ADMIN);

	
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
		menuModule.setModuleName("Consent Sections");
		menuModule.setResourceKey("tab.module.consent.sections");
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
			moduleSubTabsList.add( new AbstractTab(new Model(getLocalizer().getString(moduleName.getResourceKey(), StudySubMenuTab.this, moduleName.getModuleName())) )
			{
				private static final long serialVersionUID = 1L;
				@Override
				public Panel getPanel(String panelId) 
				{
					
					Panel panelToReturn = null;//Set up a common tab that will be accessible for all users
					
					if(moduleName.getModuleName().equalsIgnoreCase(Constants.USERS)){
						panelToReturn = new UserContainerPanel(panelId, new EtaUserVO());//Note the constructor
					
					}else if(moduleName.getModuleName().equalsIgnoreCase(Constants.STUDY_DETAILS)){
						panelToReturn = new StudyContainerPanel(panelId);
					
					}else if(moduleName.getModuleName().equalsIgnoreCase(Constants.SUB_STUDIES)){
						panelToReturn = new StudyContainerPanel(panelId);
					
					}else if(moduleName.getModuleName().equalsIgnoreCase(Constants.SITES)){
						panelToReturn = new SiteContainerPanel(panelId);
					
					}else if(moduleName.getModuleName().equalsIgnoreCase(Constants.CONSENT_SECTIONS)){
						panelToReturn = new ConsentContainerPanel(panelId);
					
					}else if(moduleName.getModuleName().equalsIgnoreCase(Constants.STUDY_COMPONENTS)){
						panelToReturn = new StudyComponentContainerPanel(panelId);
					}else if(moduleName.getModuleName().equalsIgnoreCase(Constants.MY_DETAILS)){
						panelToReturn = new MyDetailsContainer(panelId,new EtaUserVO(),currentUser );
					}
					
					
					return panelToReturn;
				};
			});
		}
		
		TabbedPanel moduleTabbedPanel = new TabbedPanel("studySubMenus", moduleSubTabsList);
		add(moduleTabbedPanel);
	}
}


