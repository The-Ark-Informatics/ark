package au.org.theark.phenotypic.web.menu;

import java.util.ArrayList;
import java.util.List;

import org.apache.shiro.subject.Subject;
import org.apache.wicket.extensions.markup.html.tabs.AbstractTab;
import org.apache.wicket.extensions.markup.html.tabs.ITab;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import au.org.theark.core.vo.MenuModule;
import au.org.theark.core.web.component.ArkAjaxTabbedPanel;
import au.org.theark.phenotypic.web.Constants;
import au.org.theark.phenotypic.web.component.field.FieldContainerPanel;
import au.org.theark.phenotypic.web.component.fieldData.FieldDataContainerPanel;
import au.org.theark.phenotypic.web.component.fieldUpload.FieldUploadContainerPanel;
import au.org.theark.phenotypic.web.component.phenoCollection.PhenoCollectionContainerPanel;
import au.org.theark.phenotypic.web.component.phenoUpload.PhenoUploadContainerPanel;
import au.org.theark.phenotypic.web.component.summaryModule.SummaryContainerPanel;

@SuppressWarnings( { "serial", "unused" })
public class PhenotypicSubMenuTab extends Panel
{
	private transient Logger	log					= LoggerFactory.getLogger(PhenotypicSubMenuTab.class);
	private transient Subject	currentUser;
	private transient Long		studyId;
	private WebMarkupContainer	arkContextMarkup;
	List<ITab>						tabList;
	List<ITab>						moduleSubTabsList	= new ArrayList<ITab>();
	List<MenuModule>				moduleTabs			= new ArrayList<MenuModule>();
	MenuModule						menuModule			= new MenuModule();

	public PhenotypicSubMenuTab(String id)
	{
		super(id);
		tabList = new ArrayList<ITab>();
		buildTabs();
	}

	public PhenotypicSubMenuTab(String id, WebMarkupContainer arkContextMarkup)
	{
		super(id);
		tabList = new ArrayList<ITab>();
		this.arkContextMarkup = arkContextMarkup;
		buildTabs(arkContextMarkup);
	}
	
	private void createMenuModule()
	{
		// This way we can get the menus from the back-end.
		// We should source this data from a table in the backend and wrap it up in a class like this
		menuModule.setModuleName(Constants.PHENOTYPIC_SUMMARY_SUBMENU);
		menuModule.setResourceKey(Constants.PHENOTYPIC_SUMMARY_RESOURCEKEY);
		moduleTabs.add(menuModule);

		menuModule = new MenuModule();
		menuModule.setModuleName(Constants.FIELD_SUBMENU);
		menuModule.setResourceKey(Constants.FIELD_RESOURCEKEY);
		moduleTabs.add(menuModule);
		
		menuModule = new MenuModule();
		menuModule.setModuleName(Constants.FIELD_UPLOAD_SUBMENU);
		menuModule.setResourceKey(Constants.FIELD_UPLOAD_RESOURCEKEY);
		moduleTabs.add(menuModule);

		menuModule = new MenuModule();
		menuModule.setModuleName(Constants.PHENO_COLLECTION_SUBMENU);
		menuModule.setResourceKey(Constants.COLLECTION_RESOURCEKEY);
		moduleTabs.add(menuModule);

		menuModule = new MenuModule();
		menuModule.setModuleName(Constants.FIELD_DATA_SUBMENU);
		menuModule.setResourceKey(Constants.FIELD_DATA_RESOURCEKEY);
		moduleTabs.add(menuModule);

		menuModule = new MenuModule();
		menuModule.setModuleName(Constants.PHENOTYPIC_DATA_UPLOAD_SUBMENU);
		menuModule.setResourceKey(Constants.PHENOTYPIC_DATA_UPLOAD_RESOURCEKEY);
		moduleTabs.add(menuModule);
	}

	public void buildTabs()
	{
		createMenuModule();

		for (final MenuModule moduleName : moduleTabs)
		{
			moduleSubTabsList.add(new AbstractTab(new Model<String>(getLocalizer().getString(moduleName.getResourceKey(), this, moduleName.getModuleName())))
			{
				@Override
				public Panel getPanel(String panelId)
				{
					return buildPanels(moduleName, panelId);
				};
			});
		}

		ArkAjaxTabbedPanel moduleTabbedPanel = new ArkAjaxTabbedPanel(Constants.PHENOTYPIC_SUBMENU, moduleSubTabsList);
		add(moduleTabbedPanel);
	}

	public void buildTabs(final WebMarkupContainer arkContextMarkup)
	{
		createMenuModule();

		for (final MenuModule moduleName : moduleTabs)
		{
			moduleSubTabsList.add(new AbstractTab(new Model<String>(getLocalizer().getString(moduleName.getResourceKey(), this, moduleName.getModuleName())))
			{
				@Override
				public Panel getPanel(String panelId)
				{
					return buildPanels(moduleName, panelId);
				};
			});
		}

		//TabbedPanel moduleTabbedPanel = new TabbedPanel(Constants.PHENOTYPIC_SUBMENU, moduleSubTabsList);
		ArkAjaxTabbedPanel moduleTabbedPanel = new ArkAjaxTabbedPanel(Constants.PHENOTYPIC_SUBMENU, moduleSubTabsList);
		
		add(moduleTabbedPanel);
	}

	protected Panel buildPanels(final MenuModule moduleName, String panelId)
	{
		Panel panelToReturn = null;// Set up a common tab that will be accessible for all users

		if (moduleName.getModuleName().equalsIgnoreCase(Constants.PHENOTYPIC_SUMMARY_SUBMENU))
		{
			panelToReturn = new SummaryContainerPanel(panelId); // Note the constructor
		}
		else if (moduleName.getModuleName().equalsIgnoreCase(Constants.PHENO_COLLECTION_SUBMENU))
		{
			panelToReturn = new PhenoCollectionContainerPanel(panelId, arkContextMarkup); // Note the constructor
		}
		else if (moduleName.getModuleName().equalsIgnoreCase(Constants.FIELD_SUBMENU))
		{
			panelToReturn = new FieldContainerPanel(panelId); // Note the constructor
		}
		else if (moduleName.getModuleName().equalsIgnoreCase(Constants.FIELD_UPLOAD_SUBMENU))
		{
			panelToReturn = new FieldUploadContainerPanel(panelId); // Note the constructor
		}
		else if (moduleName.getModuleName().equalsIgnoreCase(Constants.FIELD_DATA_SUBMENU))
		{
			panelToReturn = new FieldDataContainerPanel(panelId); // Note the constructor
		}
		else if (moduleName.getModuleName().equalsIgnoreCase(Constants.PHENOTYPIC_DATA_UPLOAD_SUBMENU))
		{
			panelToReturn = new PhenoUploadContainerPanel(panelId); // Note the constructor
		}
		return panelToReturn;
	}
}