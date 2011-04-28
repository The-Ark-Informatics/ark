package au.org.theark.report.web.menu;

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

import au.org.theark.report.web.Constants;
import au.org.theark.core.security.ArkSecurityManager;
import au.org.theark.core.security.RoleConstants;
import au.org.theark.core.vo.ArkUserVO;
import au.org.theark.core.vo.MenuModule;

@SuppressWarnings("serial")
public class ReportSubMenuTab extends Panel
{

	List<ITab> tabList;
	private WebMarkupContainer	studyNameMarkup;
	private WebMarkupContainer	studyLogoMarkup;
	private WebMarkupContainer	arkContextMarkup;

	public ReportSubMenuTab(String id)
	{
		super(id);
		tabList = new ArrayList<ITab>();
		buildTabs();
	}

	public ReportSubMenuTab(String id, WebMarkupContainer studyLogoMarkup)
	{
		super(id);
		tabList = new ArrayList<ITab>();
		this.studyLogoMarkup = studyLogoMarkup;
		buildTabs();
	}

	public ReportSubMenuTab(String id, WebMarkupContainer studyNameMarkup, WebMarkupContainer studyLogoMarkup, WebMarkupContainer arkContextMarkup)
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
		menuModule.setModuleName(Constants.REPORT_DETAIL);
		menuModule.setResourceKey(Constants.TAB_MODULE_REPORT_DETAIL);
		moduleTabs.add(menuModule);

		for (final MenuModule moduleName : moduleTabs)
		{
			moduleSubTabsList.add(new AbstractTab(new Model<String>(getLocalizer().getString(moduleName.getResourceKey(), ReportSubMenuTab.this, moduleName.getModuleName())))
			{
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

					Panel panelToReturn = null;// Set up a common tab that will be accessible for all users

					if (moduleName.getModuleName().equalsIgnoreCase(Constants.REPORT_DETAIL))
					{

//						panelToReturn = new ReportContainer(panelId, studyNameMarkup, studyLogoMarkup, arkContextMarkup);

					}
					
					return panelToReturn;
				};
			});
		}

		TabbedPanel moduleTabbedPanel = new TabbedPanel(Constants.REPORT_DETAIL, moduleSubTabsList);
		add(moduleTabbedPanel);
	}
}
