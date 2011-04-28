package au.org.theark.study.web.menu;

import java.util.ArrayList;
import java.util.List;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.apache.wicket.extensions.markup.html.tabs.AbstractTab;
import org.apache.wicket.extensions.markup.html.tabs.ITab;
import org.apache.wicket.extensions.markup.html.tabs.TabbedPanel;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;
import org.apache.wicket.spring.injection.annot.SpringBean;

import au.org.theark.core.Constants;
import au.org.theark.core.model.study.entity.ArkModule;
import au.org.theark.core.model.study.entity.ArkUsecase;
import au.org.theark.core.security.ArkSecurityManager;
import au.org.theark.core.security.RoleConstants;
import au.org.theark.core.service.IArkCommonService;
import au.org.theark.core.vo.ArkUserVO;
import au.org.theark.core.vo.MenuModule;
import au.org.theark.study.web.component.managestudy.StudyContainer;
import au.org.theark.study.web.component.mydetails.MyDetailsContainer;
import au.org.theark.study.web.component.studycomponent.StudyComponentContainerPanel;
import au.org.theark.study.web.component.user.UserContainer;

@SuppressWarnings("serial")
public class StudySubMenuTab extends Panel
{
	
	@SpringBean( name = au.org.theark.core.Constants.ARK_COMMON_SERVICE)
	private IArkCommonService iArkCommonService;
	
	//NN Commented not used as yet
	//	@SpringBean( name="arkLdapRealm")
	//	private ArkLdapRealm realm;
	
	List<ITab> tabList;
	private WebMarkupContainer	studyNameMarkup;
	private WebMarkupContainer	studyLogoMarkup;
	private WebMarkupContainer	arkContextMarkup;
	
	private ArkUsecase  arkUseCase;
	private ArkModule arkModule;

	public StudySubMenuTab(String id)
	{
		super(id);
		tabList = new ArrayList<ITab>();
		buildTabs();
	}

	public StudySubMenuTab(String id, WebMarkupContainer studyLogoMarkup)
	{
		super(id);
		tabList = new ArrayList<ITab>();
		this.studyLogoMarkup = studyLogoMarkup;
		buildTabs();
	}

	public StudySubMenuTab(String id, WebMarkupContainer studyNameMarkup, WebMarkupContainer studyLogoMarkup, WebMarkupContainer arkContextMarkup)
	{
		super(id);
		tabList = new ArrayList<ITab>();
		this.studyNameMarkup = studyNameMarkup;
		this.studyLogoMarkup = studyLogoMarkup;
		this.arkContextMarkup = arkContextMarkup;
		buildTabs();
	}

	public void buildTabs()
	{

		List<ITab> moduleSubTabsList = new ArrayList<ITab>();
		List<MenuModule> moduleTabs = new ArrayList<MenuModule>();

		// THis way we can get the menus from the back-end. We should source this data from a table in the backend and wrap it up in a class like this
		MenuModule menuModule = new MenuModule();
		menuModule.setModuleName(Constants.STUDY_DETAIL);
		// menuModule.setResourceKey(au.org.theark.core.Constants.TAB_MODULE_STUDY_DETAIL);
		menuModule.setResourceKey(Constants.TAB_MODULE_STUDY_DETAIL);
		moduleTabs.add(menuModule);

//		menuModule = new MenuModule();
//		menuModule.setModuleName(Constants.SITE);
//		menuModule.setResourceKey(Constants.TAB_MODULE_SITE);
//		moduleTabs.add(menuModule);

		menuModule = new MenuModule();
		menuModule.setModuleName(Constants.STUDY_COMPONENT);
		menuModule.setResourceKey(Constants.TAB_MODULE_STUDY_COMPONENT);
		moduleTabs.add(menuModule);

//		menuModule = new MenuModule();
//		menuModule.setModuleName(Constants.CUSTOM_FIELD);
//		menuModule.setResourceKey(Constants.TAB_CUSTOM_FIELD);
//		moduleTabs.add(menuModule);
		
		menuModule = new MenuModule();
		menuModule.setModuleName(Constants.USER);
		menuModule.setResourceKey(Constants.TAB_MODULE_USER);
		moduleTabs.add(menuModule);

		menuModule = new MenuModule();
		menuModule.setModuleName(Constants.MY_DETAIL);
		menuModule.setResourceKey(Constants.TAB_MODULE_MY_DETAIL);
		moduleTabs.add(menuModule);

		for (final MenuModule moduleName : moduleTabs)
		{
			moduleSubTabsList.add(new AbstractTab(new Model<String>(getLocalizer().getString(moduleName.getResourceKey(), StudySubMenuTab.this, moduleName.getModuleName())))
			{
				public boolean isVisible()
				{

					boolean flag = false;
					if (moduleName.getModuleName().equalsIgnoreCase(Constants.USER) || moduleName.getModuleName().equalsIgnoreCase(Constants.SUBJECT))
					{
						
						Subject currentUser = SecurityUtils.getSubject();
						//Clear Cache here to load the roles & permissions for the user in session.The very next auth check will delegate the call to doAuthorizationInfo()
						//realm.clearCachedAuthorizationInfo(currentUser.getPrincipals());
						ArkSecurityManager arkSecurityManager = ArkSecurityManager.getInstance();
					
						if (  (arkSecurityManager.subjectHasRole(RoleConstants.SUPER_ADMIN))|| (arkSecurityManager.subjectHasRole(RoleConstants.ARK_SUPER_ADMIN) || (arkSecurityManager.subjectHasRole(RoleConstants.STUDY_ADMIN))))
						{

							flag = currentUser.isAuthenticated();

						}
						else
						{
							flag = false;
						}
					}
					else
					{
						flag = true;
					}
					return flag;
				}

				@Override
				public Panel getPanel(String panelId)
				{

					Panel panelToReturn = null;// Set up a common tab that will be accessible for all users

					if (moduleName.getModuleName().equalsIgnoreCase(Constants.USER))
					{
						arkUseCase = iArkCommonService.getArkUsecaseByName(au.org.theark.core.Constants.USECASE_KEY_VALUE_USER); //Place a default use case into session
						panelToReturn = new UserContainer(panelId, new ArkUserVO());// Note the constructor

					}
					else if (moduleName.getModuleName().equalsIgnoreCase(Constants.STUDY_DETAIL))
					{
						arkUseCase = iArkCommonService.getArkUsecaseByName(au.org.theark.core.Constants.USECASE_KEY_VALUE_STUDY); //Place a default use case into session
						panelToReturn = new StudyContainer(panelId, studyNameMarkup, studyLogoMarkup, arkContextMarkup);

					}
//					else if (moduleName.getModuleName().equalsIgnoreCase(Constants.SITE))
//					{
//
//						panelToReturn = new SiteContainerPanel(panelId);
//
//					}
					else if (moduleName.getModuleName().equalsIgnoreCase(Constants.STUDY_COMPONENT))
					{
						arkUseCase = iArkCommonService.getArkUsecaseByName(au.org.theark.core.Constants.USECASE_KEY_VALUE_STUDY_COMPONENT); //Place a default use case into session
						panelToReturn = new StudyComponentContainerPanel(panelId);

					}
//					else if(moduleName.getModuleName().equalsIgnoreCase(Constants.CUSTOM_FIELD)){
//						
//						panelToReturn = new CustomFieldContainer(panelId); 
//						
//					}
					else if (moduleName.getModuleName().equalsIgnoreCase(Constants.MY_DETAIL))
					{
						arkUseCase = iArkCommonService.getArkUsecaseByName(au.org.theark.core.Constants.USECASE_KEY_VALUE_MY_DETAIL); //Place a default use case into session
						Subject currentUser = SecurityUtils.getSubject();
						panelToReturn = new MyDetailsContainer(panelId, new ArkUserVO(), currentUser);

					}
					
					arkModule = iArkCommonService.getArkModuleByName(au.org.theark.core.Constants.ARK_MODULE_STUDY); //Place a default module into session
					SecurityUtils.getSubject().getSession().setAttribute(au.org.theark.core.Constants.ARK_USECASE_KEY, arkUseCase.getId());
					SecurityUtils.getSubject().getSession().setAttribute(au.org.theark.core.Constants.ARK_MODULE_KEY, arkModule.getId());
				
					//refresh cache (NN Commented not used as yet)
					//SecurityManager securityManager =  ThreadContext.getSecurityManager();
					//Subject currentUser = SecurityUtils.getSubject();	
					//realm.clearCachedAuthorizationInfo(currentUser.getPrincipals());
					//securityManager.hasRole(currentUser.getPrincipals(), "Administrator");//Enforce authorization check here so it loads the roles
					return panelToReturn;
				};
			});
		}

		TabbedPanel moduleTabbedPanel = new TabbedPanel(Constants.MENU_STUDY_SUBMENU, moduleSubTabsList);
		add(moduleTabbedPanel);
	}
}
