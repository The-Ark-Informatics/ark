package au.org.theark.admin.web.menu;

import java.util.ArrayList;
import java.util.List;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.apache.wicket.extensions.markup.html.tabs.AbstractTab;
import org.apache.wicket.extensions.markup.html.tabs.ITab;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;

import au.org.theark.admin.web.component.rolePolicy.RolePolicyContainerPanel;
import au.org.theark.core.security.ArkSecurityManager;
import au.org.theark.core.security.RoleConstants;
import au.org.theark.core.vo.MenuModule;
import au.org.theark.core.web.component.ArkAjaxTabbedPanel;

public class AdminSubMenuTab extends Panel
{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 2808674930679468072L;
	private List<ITab> tabList;

	public AdminSubMenuTab(String id)
	{
		super(id);
		setTabList(new ArrayList<ITab>());
		buildTabs();
	}

	public void buildTabs()
	{
		List<ITab> moduleSubTabsList = new ArrayList<ITab>();
		List<MenuModule> moduleTabs = new ArrayList<MenuModule>();

		// THis way we can get the menus from the back-end. We should source this data from a table in the backend and wrap it up in a class like this
		MenuModule menuModule = new MenuModule();
		menuModule.setModuleName("Administration");
		menuModule.setResourceKey("tab.module.administration.detail");
		moduleTabs.add(menuModule);

		for (final MenuModule moduleName : moduleTabs)
		{
			moduleSubTabsList.add(new AbstractTab(new Model<String>(getLocalizer().getString(moduleName.getResourceKey(), AdminSubMenuTab.this, moduleName.getModuleName())))
			{
				/**
				 * 
				 */
				private static final long serialVersionUID = -4074207330834453724L;

				public boolean isVisible()
				{

					boolean flag = false;
					if (moduleName.getModuleName().equalsIgnoreCase(au.org.theark.core.Constants.USER) || moduleName.getModuleName().equalsIgnoreCase(au.org.theark.core.Constants.SUBJECT))
					{

						ArkSecurityManager arkSecurityManager = ArkSecurityManager.getInstance();
						Subject currentUser = SecurityUtils.getSubject();
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
					Panel panelToReturn = null;

					if (moduleName.getModuleName().equalsIgnoreCase("Role Policy"))
					{
						RolePolicyContainerPanel adminContainerPanel = new RolePolicyContainerPanel(panelId);
						adminContainerPanel.initialisePanel();
						panelToReturn = adminContainerPanel;
					}
					
					return panelToReturn;
				};
			});
		}

		ArkAjaxTabbedPanel moduleTabbedPanel = new ArkAjaxTabbedPanel("adminSubMenus", moduleSubTabsList);
		add(moduleTabbedPanel);
	}

	/**
	 * @param tabList the tabList to set
	 */
	public void setTabList(List<ITab> tabList)
	{
		this.tabList = tabList;
	}

	/**
	 * @return the tabList
	 */
	public List<ITab> getTabList()
	{
		return tabList;
	}
}
