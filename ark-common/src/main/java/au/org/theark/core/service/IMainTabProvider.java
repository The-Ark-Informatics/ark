package au.org.theark.core.service;

import org.apache.wicket.extensions.markup.html.tabs.ITab;

/**
 * Main interface for all Main Tabs in the application. All class implemtations must implement createTab()
 * @author nivedann
 *
 */
public interface IMainTabProvider {
	
	/**
	 * Create the main menu tab.
	 * 
	 * @param tabName
	 *           The id/name of the tab
	 * @return the main tab
	 */
	public ITab createTab(String tabName);
}
