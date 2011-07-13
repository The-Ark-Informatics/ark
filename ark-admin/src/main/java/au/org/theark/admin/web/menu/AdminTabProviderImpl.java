package au.org.theark.admin.web.menu;

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

public class AdminTabProviderImpl extends Panel implements IMainTabProvider
{

	private static final long	serialVersionUID	= 1L;
	private List<ITab>			moduleTabsList;

	public AdminTabProviderImpl(String panelId)
	{
		super(panelId);
		moduleTabsList = new ArrayList<ITab>();
	}

	public List<ITab> buildTabs()
	{
		// Main tab
		ITab iTab = createTab(au.org.theark.core.Constants.ARK_MODULE_ADMIN);
		moduleTabsList.add(iTab);

		return moduleTabsList;
	}

	public ITab createTab(String tabName)
	{
		return new ArkMainTab(new Model<String>(tabName))
		{
			/**
			 * 
			 */
			private static final long	serialVersionUID	= -5063032622932238615L;

			@Override
			public Panel getPanel(String pid)
			{
				// The sub menu(s) for Admin
				return new AdminSubMenuTab(pid);
			}

			public boolean isAccessible()
			{
				return true;
			}

			public boolean isVisible()
			{
				boolean flag = false;
				SecurityManager securityManager = ThreadContext.getSecurityManager();
				Subject currentUser = SecurityUtils.getSubject();

				// Only a Super Administrator can see the Admin tab/menu
				if (securityManager.hasRole(currentUser.getPrincipals(), au.org.theark.core.security.RoleConstants.ARK_ROLE_SUPER_ADMINISTATOR))
				{
					flag = currentUser.isAuthenticated();
				}
				else
				{
					flag = false;
				}
				return flag;
			}
		};
	}
}