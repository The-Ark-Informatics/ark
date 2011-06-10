/**
 * 
 * This is a new file
 *
 *
 */
package au.org.theark.registry.web.menu;

import java.util.ArrayList;
import java.util.List;

import org.apache.shiro.SecurityUtils;
import org.apache.wicket.extensions.markup.html.tabs.ITab;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;

import au.org.theark.core.service.IMainTabProvider;
import au.org.theark.core.web.component.ArkMainTab;
import au.org.theark.registry.web.Constants;

/**
 * @author nivedann
 *
 */
public class RegistryTabProviderImpl extends Panel implements  IMainTabProvider{

	
	/**
	 * 
	 */
	private static final long	serialVersionUID	= -5905065151035360540L;
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

	public ITab createTab(final String tabName) {
		
		return  new ArkMainTab(new Model<String>(tabName)) {

			/**
			 * 
			 */
			private static final long	serialVersionUID	= -4616700064526881402L;

			public boolean isVisible(){
				//If the logged in user is a member of this module then allow him to view this tab
				return true;
			}
			
			@Override
			public Panel getPanel(String panelId) {
				
				Panel panelToReturn = null;//S
				// TODO Auto-generated method stub
				if(tabName.equals(Constants.REGISTRY_MAIN_TAB)){
					return new RegistrySubMenuTab(panelId);
				}
				return panelToReturn;
			}
			
			public boolean isAccessible()
			{
				Long sessionStudyId = (Long) SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.core.Constants.STUDY_CONTEXT_ID);
				if(sessionStudyId == null)
				{
					this.getPanel(Constants.REGISTRY_MAIN_TAB).error(au.org.theark.core.Constants.STUDY_IN_CONTEXT_MESSAGE);
					return false;
				}
				else
					return true;
			}
			
		};
	}

}
