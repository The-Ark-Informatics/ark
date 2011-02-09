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
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;

import au.org.theark.core.service.IMainTabProvider;

/**
 * @author nivedann
 *
 */
public class RegistryTabProviderImpl extends Panel implements  IMainTabProvider{

	
	List<ITab> moduleTabsList;
	/**
	 * @param id
	 */
	public RegistryTabProviderImpl(String id) {
		super(id);
		moduleTabsList = new ArrayList<ITab>();
	}
	
	public  List<ITab> buildTabs(){
		ITab tab1 = createTab("Registry");
		moduleTabsList.add(tab1);
		return moduleTabsList;
	}

	/* (non-Javadoc)
	 * @see au.org.theark.core.service.IMainTabProvider#createTab(java.lang.String)
	 */
	public ITab createTab(final String tabName) {
		
		return  new AbstractTab(new Model<String>(tabName)) {

			public boolean isVisible(){
				//If the logged in user is a member of this module then allow him to view this tab
				return true;
			}
			
			@Override
			public Panel getPanel(String panelId) {
				
				Panel panelToReturn = null;//S
				// TODO Auto-generated method stub
				if(tabName.equals("Registry")){
					return new RegistrySubMenuTab(panelId);
				}
				return panelToReturn;
			}
			
		};
	}

}
