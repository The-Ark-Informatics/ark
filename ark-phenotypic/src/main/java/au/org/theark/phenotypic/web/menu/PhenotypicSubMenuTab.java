package au.org.theark.phenotypic.web.menu;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.extensions.markup.html.tabs.AbstractTab;
import org.apache.wicket.extensions.markup.html.tabs.ITab;
import org.apache.wicket.extensions.markup.html.tabs.TabbedPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;

import au.org.theark.phenotypic.web.Constants;
import au.org.theark.phenotypic.web.component.TestContainer.TestContainerPanel;


@SuppressWarnings("serial")
public class PhenotypicSubMenuTab extends Panel {

	List<ITab> tabList;
	
	public PhenotypicSubMenuTab(String id) {
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
		
		//This way we can get the menus from the back-end. We should source this data from a table in the backend and wrap it up in a class like this
		MenuModule menuModule = new MenuModule();
		menuModule.setModuleName("SubPhenotypic");
		menuModule.setResourceKey("tab.module.phenotypic.test");
		moduleTabs.add(menuModule);

		
		for(final MenuModule moduleName : moduleTabs)
		{
			moduleSubTabsList.add( new AbstractTab(new Model<String>(getLocalizer().getString(moduleName.getResourceKey(), this, moduleName.getModuleName())) )
			{
				
				public boolean isVisible(){                                        
					// Implement this if you want to secure this Tab
					return true;
				}  
				
				@Override
				public Panel getPanel(String panelId) 
				{
					
					Panel panelToReturn = null;//Set up a common tab that will be accessible for all users
					
					if(moduleName.getModuleName().equalsIgnoreCase("SubPhenotypic")){
						panelToReturn = new TestContainerPanel(panelId);
					}
					
					return panelToReturn;
				};
			});
		}
		
		TabbedPanel moduleTabbedPanel = new TabbedPanel(Constants.PHENOTYPIC_SUBMENU, moduleSubTabsList);
		add(moduleTabbedPanel);
	}
}
