package au.org.theark.lims.web.menu;

import java.util.ArrayList;
import java.util.List;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.apache.wicket.extensions.markup.html.tabs.AbstractTab;
import org.apache.wicket.extensions.markup.html.tabs.ITab;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;

import au.org.theark.core.security.ArkSecurityManager;
import au.org.theark.core.security.RoleConstants;
import au.org.theark.core.vo.MenuModule;
import au.org.theark.core.web.component.ArkAjaxTabbedPanel;
import au.org.theark.lims.web.Constants;
import au.org.theark.lims.web.component.bioCollection.BioCollectionContainerPanel;
import au.org.theark.lims.web.component.subject.SubjectContainerPanel;

@SuppressWarnings("serial")
public class LimsSubMenuTab extends Panel
{
	
	//@SpringBean( name = au.org.theark.core.Constants.ARK_COMMON_SERVICE)
	//private IArkCommonService iArkCommonService;
	
	//NN Commented not used as yet
	//@SpringBean( name="arkLdapRealm")
	//private ArkLdapRealm realm;
	
	List<ITab> tabList;
	private WebMarkupContainer	arkContextMarkup;
	
	public LimsSubMenuTab(String id)
	{
		super(id);
		tabList = new ArrayList<ITab>();
		buildTabs();
	}

	public LimsSubMenuTab(String id, WebMarkupContainer arkContextMarkup)
	{
		super(id);
		tabList = new ArrayList<ITab>();
		this.arkContextMarkup = arkContextMarkup;
		buildTabs();
	}

	public void buildTabs()
	{

		List<ITab> moduleSubTabsList = new ArrayList<ITab>();
		List<MenuModule> moduleTabs = new ArrayList<MenuModule>();

		MenuModule menuModule = new MenuModule();
		menuModule.setModuleName(au.org.theark.core.Constants.TAB_SUBJECT_DETAIL);
		menuModule.setResourceKey(au.org.theark.core.Constants.TAB_MODULE_LIMS_SUBJECT_DETAIL);
		moduleTabs.add(menuModule);
		
		menuModule = new MenuModule();
		menuModule.setModuleName(Constants.TAB_BIO_COLLECTION);
		menuModule.setResourceKey(Constants.TAB_MODULE_LIMS_COLLECTION);
		moduleTabs.add(menuModule);

		for (final MenuModule moduleName : moduleTabs)
		{
			moduleSubTabsList.add(new AbstractTab(new Model<String>(getLocalizer().getString(moduleName.getResourceKey(), LimsSubMenuTab.this, moduleName.getModuleName())))
			{
				
				
				public boolean isVisible()
				{

					boolean flag = false;
					if (moduleName.getModuleName().equalsIgnoreCase(au.org.theark.core.Constants.USER) || moduleName.getModuleName().equalsIgnoreCase(au.org.theark.core.Constants.SUBJECT))
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
					if(moduleName.getModuleName().equalsIgnoreCase(au.org.theark.core.Constants.TAB_SUBJECT_DETAIL)){
						//arkFunction = iArkCommonService.getArkFunctionByName(au.org.theark.core.Constants.USECASE_KEY_VALUE_SUBJECT); //Place a default use case into session
						panelToReturn = new SubjectContainerPanel(panelId, arkContextMarkup);//Note the constructor
					}
					else if(moduleName.getModuleName().equalsIgnoreCase(Constants.TAB_BIO_COLLECTION)){
						//arkFunction = iArkCommonService.getArkFunctionByName(au.org.theark.core.Constants.USECASE_KEY_VALUE_SUBJECT); //Place a default use case into session
						panelToReturn = new BioCollectionContainerPanel(panelId, arkContextMarkup);//Note the constructor
					}	 
					return panelToReturn;
				};
			});
		}

		ArkAjaxTabbedPanel moduleTabbedPanel = new ArkAjaxTabbedPanel(Constants.MENU_LIMS_SUBMENU, moduleSubTabsList);
		add(moduleTabbedPanel);
	}
}