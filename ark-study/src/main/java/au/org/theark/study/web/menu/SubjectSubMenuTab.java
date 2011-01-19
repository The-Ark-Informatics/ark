/**
 * 
 * This is a new file
 *
 *
 */
package au.org.theark.study.web.menu;

import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.extensions.markup.html.tabs.AbstractTab;
import org.apache.wicket.extensions.markup.html.tabs.ITab;
import org.apache.wicket.extensions.markup.html.tabs.TabbedPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;

import au.org.theark.core.Constants;
import au.org.theark.core.vo.MenuModule;
import au.org.theark.study.web.component.address.AddressContainerPanel;
import au.org.theark.study.web.component.consent.ConsentContainerPanel;
import au.org.theark.study.web.component.phone.PhoneContainerPanel;
import au.org.theark.study.web.component.subject.SubjectContainer;
import au.org.theark.study.web.component.subject.form.PhoneContainerForm;

/**
 * @author nivedann
 *
 */
public class SubjectSubMenuTab extends Panel{
	
	List<ITab> tabList;
	/**
	 * @param id
	 */
	public SubjectSubMenuTab(String id) {
		super(id);
		tabList = new ArrayList<ITab>();
		buildTabs();
	}
	
	public  void buildTabs(){
		
		List<ITab> moduleSubTabsList = new ArrayList<ITab>();
		List<MenuModule> moduleTabs = new ArrayList<MenuModule>();
		
		//THis way we can get the menus from the back-end. We should source this data from a table in the backend and wrap it up in a class like this
		MenuModule menuModule = new MenuModule();
		menuModule.setModuleName(Constants.TAB_SUBJECT_DETAIL);
		menuModule.setResourceKey(Constants.TAB_MODULE_SUBJECT_DETAIL);
		moduleTabs.add(menuModule);

		menuModule = new MenuModule();
		menuModule.setModuleName(Constants.TAB_PERSON_PHONE);
		menuModule.setResourceKey(Constants.TAB_MODULE_PERSON_PHONE);
		moduleTabs.add(menuModule);
		
		menuModule = new MenuModule();
		menuModule.setModuleName(Constants.TAB_PERSON_ADDRESS);
		menuModule.setResourceKey(Constants.TAB_MODULE_PERSON_ADDRESS);
		moduleTabs.add(menuModule);

		menuModule = new MenuModule();
		menuModule.setModuleName(Constants.TAB_SUBJECT_CONSENT);
		menuModule.setResourceKey(Constants.TAB_MODULE_SUBJECT_CONSENT);
		moduleTabs.add(menuModule);

		
		for(final MenuModule moduleName : moduleTabs)
		{
			moduleSubTabsList.add( new AbstractTab(new Model<String>(getLocalizer().getString(moduleName.getResourceKey(), SubjectSubMenuTab.this, moduleName.getModuleName())) )
			{
				public boolean isVisible(){
					
					return true;
				}
				
				@Override
				public Panel getPanel(String panelId) 
				{
					
					Panel panelToReturn = null;//Set up a common tab that will be accessible for all users
					
					if(moduleName.getModuleName().equalsIgnoreCase(Constants.TAB_SUBJECT_DETAIL)){
						
						panelToReturn = new SubjectContainer(panelId);//Note the constructor
					
					}
					else if(moduleName.getModuleName().equalsIgnoreCase(Constants.TAB_PERSON_PHONE)){
					
						panelToReturn = new PhoneContainerPanel(panelId);
					
					}
					else if(moduleName.getModuleName().equalsIgnoreCase(Constants.TAB_PERSON_ADDRESS)){
						
						panelToReturn = new AddressContainerPanel(panelId);
					
					}
					else if(moduleName.getModuleName().equalsIgnoreCase(Constants.TAB_SUBJECT_CONSENT)){
						
						panelToReturn = new ConsentContainerPanel(panelId);
					
					}
					return panelToReturn;
				};
			});
		}
		
		TabbedPanel moduleTabbedPanel = new TabbedPanel(Constants.MENU_SUBJECT_SUBMENU, moduleSubTabsList);
		add(moduleTabbedPanel);
	}

}
