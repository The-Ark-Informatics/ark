package au.org.theark.phenotypic.web.menu;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.apache.wicket.extensions.ajax.markup.html.tabs.AjaxTabbedPanel;
import org.apache.wicket.extensions.markup.html.tabs.AbstractTab;
import org.apache.wicket.extensions.markup.html.tabs.ITab;
import org.apache.wicket.extensions.markup.html.tabs.TabbedPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import au.org.theark.core.vo.MenuModule;
import au.org.theark.phenotypic.web.Constants;
import au.org.theark.phenotypic.web.component.field.FieldContainerPanel;
import au.org.theark.phenotypic.web.component.phenoCollection.PhenoCollectionContainerPanel;
import au.org.theark.phenotypic.web.component.phenotypicImport.PhenotypicImportContainer;
import au.org.theark.phenotypic.web.component.reportContainer.ReportContainerPanel;
import au.org.theark.phenotypic.web.component.summaryModule.SummaryContainerPanel;

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

	public void buildTabs()
	{
		List<ITab> moduleSubTabsList = new ArrayList<ITab>();
		List<MenuModule> moduleTabs = new ArrayList<MenuModule>();

		// This way we can get the menus from the back-end.
		// We should source this data from a table in the backend and wrap it up in a class like this
		MenuModule menuModule = new MenuModule();
		menuModule.setModuleName(Constants.PHENOTYPIC_SUMMARY_SUBMENU);
		menuModule.setResourceKey(Constants.PHENOTYPIC_SUMMARY_RESOURCEKEY);
		moduleTabs.add(menuModule);
		
		menuModule = new MenuModule();
		menuModule.setModuleName(Constants.PHENO_COLLECTION_SUBMENU);
		menuModule.setResourceKey(Constants.COLLECTION_RESOURCEKEY);
		moduleTabs.add(menuModule);

		menuModule = new MenuModule();
		menuModule.setModuleName(Constants.FIELD_SUBMENU);
		menuModule.setResourceKey(Constants.FIELD_RESOURCEKEY);
		moduleTabs.add(menuModule);
		
		menuModule = new MenuModule();
		menuModule.setModuleName(Constants.PHENOTYPIC_IMPORT_SUBMENU);
		menuModule.setResourceKey(Constants.PHENOTYPIC_IMPORT_RESOURCEKEY);
		moduleTabs.add(menuModule);
		
		menuModule = new MenuModule();
		menuModule.setModuleName(Constants.REPORT_SUBMENU);
		menuModule.setResourceKey(Constants.REPORT_RESOURCEKEY);
		moduleTabs.add(menuModule);

		for (final MenuModule moduleName : moduleTabs)
		{
			moduleSubTabsList.add(new AbstractTab(new Model<String>(getLocalizer().getString(moduleName.getResourceKey(), this, moduleName.getModuleName())))
			{
				@Override
				public Panel getPanel(String panelId)
				{

					Panel panelToReturn = null;// Set up a common tab that will be accessible for all users

					if (moduleName.getModuleName().equalsIgnoreCase(Constants.PHENOTYPIC_SUMMARY_SUBMENU))
					{
						panelToReturn = new SummaryContainerPanel(panelId); // Note the constructor
					}
					else if (moduleName.getModuleName().equalsIgnoreCase(Constants.PHENO_COLLECTION_SUBMENU))
					{
						panelToReturn = new PhenoCollectionContainerPanel(panelId); // Note the constructor
					}
					else if (moduleName.getModuleName().equalsIgnoreCase(Constants.FIELD_SUBMENU))
					{
						panelToReturn = new FieldContainerPanel(panelId); // Note the constructor
					}
					else if (moduleName.getModuleName().equalsIgnoreCase(Constants.PHENOTYPIC_IMPORT_SUBMENU))
					{
						panelToReturn = new PhenotypicImportContainer(panelId); // Note the constructor
					}
					else if (moduleName.getModuleName().equalsIgnoreCase(Constants.REPORT_SUBMENU))
					{
						panelToReturn = new ReportContainerPanel(panelId); // Note the constructor
					}

					return panelToReturn;
				};
			});
		}

		TabbedPanel moduleTabbedPanel = new TabbedPanel(Constants.PHENOTYPIC_SUBMENU, moduleSubTabsList);
		add(moduleTabbedPanel);
	}
}