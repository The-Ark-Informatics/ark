/**
 * 
 * This is a new file
 *
 *
 */
package au.org.theark.registry.web.menu;

import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.extensions.markup.html.tabs.AbstractTab;
import org.apache.wicket.extensions.markup.html.tabs.ITab;
import org.apache.wicket.extensions.markup.html.tabs.TabbedPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;

import au.org.theark.core.Constants;
import au.org.theark.core.vo.MenuModule;
import au.org.theark.registry.web.component.invoice.InvoiceContainerPanel;

/**
 * @author nivedann
 *
 */
public class RegistrySubMenuTab extends Panel{

	List<ITab> tabList;
	/**
	 * @param id
	 */
	public RegistrySubMenuTab(String id) {
		super(id);
		buildTabs();
	}
	
	public void buildTabs(){
		
		List<ITab> moduleSubTabsList = new ArrayList<ITab>();
		List<MenuModule> moduleTabs = new ArrayList<MenuModule>();

		// THis way we can get the menus from the back-end. We should source this data from a table in the backend and wrap it up in a class like this
		MenuModule menuModule = new MenuModule();
		menuModule.setModuleName("Invoice");
		menuModule.setResourceKey("tab.module.registry.invoice");
		moduleTabs.add(menuModule);
		
		for (final MenuModule moduleName : moduleTabs)
		{
			moduleSubTabsList.add(new AbstractTab(new Model<String>(getLocalizer().getString(moduleName.getResourceKey(), RegistrySubMenuTab.this, moduleName.getModuleName())))
			{

				public boolean isVisible()
				{
					return true;
				}
				
				@Override
				public Panel getPanel(String panelId)
				{
					Panel panelToReturn = null;// Set up a common tab that will be accessible for all users
					
					if(moduleName.getModuleName().equalsIgnoreCase("Invoice")){
					 panelToReturn = new InvoiceContainerPanel(panelId);	
					}
					return panelToReturn;
				};
			});
		}

		TabbedPanel moduleTabbedPanel = new TabbedPanel(Constants.MENU_REGISTRY_SUBMENU, moduleSubTabsList);
		add(moduleTabbedPanel);
		
	}

}
