package au.org.theark.phenotypic.web.menu;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.apache.wicket.extensions.markup.html.tabs.AbstractTab;
import org.apache.wicket.extensions.markup.html.tabs.ITab;
import org.apache.wicket.extensions.markup.html.tabs.TabbedPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import au.org.theark.phenotypic.web.Constants;
import au.org.theark.phenotypic.web.component.TestContainer.TestContainerPanel;
import au.org.theark.phenotypic.web.component.collection.CollectionContainer;

@SuppressWarnings( { "serial", "unused" })
public class PhenotypicSubMenuTab extends Panel
{
	private transient Logger	log	= LoggerFactory.getLogger(PhenotypicSubMenuTab.class);
	private transient Subject	currentUser;
	private transient Long		studyId;
	List<ITab>						tabList;

	public PhenotypicSubMenuTab(String id)
	{
		super(id);
		tabList = new ArrayList<ITab>();
		buildTabs();
	}

	private class MenuModule implements Serializable
	{

		public MenuModule()
		{
			super();
		}

		private String	moduleName;
		private String	resourceKey;

		public String getModuleName()
		{
			return moduleName;
		}

		public void setModuleName(String moduleName)
		{
			this.moduleName = moduleName;
		}

		public String getResourceKey()
		{
			return resourceKey;
		}

		public void setResourceKey(String resourceKey)
		{
			this.resourceKey = resourceKey;
		}

	}

	public void buildTabs()
	{
		List<ITab> moduleSubTabsList = new ArrayList<ITab>();
		List<MenuModule> moduleTabs = new ArrayList<MenuModule>();

		// This way we can get the menus from the back-end.
		// We should source this data from a table in the backend and wrap it up in a class like this
		MenuModule menuModule = new MenuModule();
		menuModule.setModuleName(Constants.COLLECTION_SUBMENU);
		menuModule.setResourceKey(Constants.COLLECTION_RESOURCEKEY);
		moduleTabs.add(menuModule);

		menuModule = new MenuModule();
		menuModule.setModuleName(Constants.TEST_SUBMENU);
		menuModule.setResourceKey(Constants.TEST_RESOURCEKEY);
		moduleTabs.add(menuModule);

		for (final MenuModule moduleName : moduleTabs)
		{
			moduleSubTabsList.add(new AbstractTab(new Model<String>(getLocalizer().getString(moduleName.getResourceKey(), this, moduleName.getModuleName())))
			{
				public boolean isVisible()
				{
					boolean hasStudy = true;

					/*
					// Implement this if you want to secure this Tab
					try
					{
						currentUser = SecurityUtils.getSubject();
						studyId = (Long) currentUser.getSession().getAttribute(Constants.STUDY_ID);

						// TODO: Disable main tab if studyId not selected (in context)
						if (studyId != null)
						{
							hasStudy = true;
						}
						else
						{
							hasStudy = false;
						}
					}
					catch (Exception ex)
					{
						log.error("PhenotypicSubMenuTab: General exception when determing tab visibility. " + ex.getMessage());
					}
					*/

					return hasStudy;
				}

				@Override
				public Panel getPanel(String panelId)
				{

					Panel panelToReturn = null;// Set up a common tab that will be accessible for all users

					if (moduleName.getModuleName().equalsIgnoreCase(Constants.COLLECTION_SUBMENU))
					{
						panelToReturn = new CollectionContainer(panelId);
					}
					else if (moduleName.getModuleName().equalsIgnoreCase(Constants.TEST_SUBMENU))
					{
						panelToReturn = new TestContainerPanel(panelId); // Note the constructor
					}

					return panelToReturn;
				};
			});
		}

		TabbedPanel moduleTabbedPanel = new TabbedPanel(Constants.PHENOTYPIC_SUBMENU, moduleSubTabsList);
		add(moduleTabbedPanel);
	}
}
