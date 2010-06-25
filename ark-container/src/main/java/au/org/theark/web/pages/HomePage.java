package  au.org.theark.web.pages;

import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.PageParameters;
import org.apache.wicket.extensions.markup.html.tabs.ITab;
import org.apache.wicket.extensions.markup.html.tabs.TabbedPanel;
import org.apache.wicket.markup.html.basic.Label;

import au.org.theark.study.web.menu.MainTabProviderImpl;

/**
 * Homepage aka Index page of ETA
 */
public class HomePage extends BasePage {

	private static final long serialVersionUID = 1L;

	// TODO Add any page properties or variables here

    /**
	 * Constructor that is invoked when page is invoked without a session.
	 * 
	 * @param parameters
	 *            Page parameters
	 */
    public HomePage(final PageParameters parameters) {

        buildModuleTabs();
        
    }

	@Override
	protected void buildModuleTabs() {
		
		List<ITab> moduleTabsList  = new ArrayList<ITab>();
		MainTabProviderImpl studyMainTabProvider = new MainTabProviderImpl("study");
		moduleTabsList = studyMainTabProvider.buildTabs();
	        
		//	        for(ITab itab: subTabsList){
		//	        	moduleTabsList.add(itab);	
		//	        }
        TabbedPanel moduleTabbedPanel = new TabbedPanel("moduleTabsList", moduleTabsList); 
        add(moduleTabbedPanel);
	}

	@Override
	protected void buildTabMenu() {
		// TODO Auto-generated method stub
		
	}

	@Override
	String getTitle() {
		// TODO Auto-generated method stub
		return null;
	}
}
