package au.org.theark.study.web.menu;
import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.extensions.markup.html.tabs.AbstractTab;
import org.apache.wicket.extensions.markup.html.tabs.ITab;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;

import au.org.theark.core.service.IMainTabProvider;
import au.org.theark.study.web.Constants;

/**
 * The main class that implements the common service IMainTabProvider.This contributes the
 * Tab menu which forms the entry point into Study module. As part of the main Tab that it contributes 
 * it will also contain the sub-menu tabs.This more like a plugin class.
 * @author nivedann
 *
 */
@SuppressWarnings("serial")
	public class MainTabProviderImpl extends Panel implements  IMainTabProvider {

	List<ITab> moduleTabsList;
	//List<MenuModule> moduleTabs = new ArrayList<MenuModule>();
	private WebMarkupContainer studyNameMarkup;
	private WebMarkupContainer studyLogoMarkup;
	private WebMarkupContainer arkContextMarkup;
	
	public MainTabProviderImpl(String panelId){
		super(panelId);
		moduleTabsList = new ArrayList<ITab>();
	}
	
	
	public  List<ITab> buildTabs(WebMarkupContainer studyLogoMarkup)
	{	
		this.studyLogoMarkup = studyLogoMarkup;
		ITab tab1 = createTab(Constants.STUDY_MAIN_TAB);//Forms the Main Top level Tab
		ITab tab2 = createTab(Constants.SUBJECT_MAIN_TAB);
		moduleTabsList.add(tab1);
		moduleTabsList.add(tab2);
		return moduleTabsList;
	}
	
	public  List<ITab> buildTabs(WebMarkupContainer studyNameMarkup, WebMarkupContainer studyLogoMarkup, WebMarkupContainer arkContextMarkup)
	{	
		this.studyNameMarkup = studyNameMarkup;
		this.studyLogoMarkup = studyLogoMarkup;
		this.arkContextMarkup = arkContextMarkup;
		
		ITab tab1 = createTab(Constants.STUDY_MAIN_TAB);//Forms the Main Top level Tab
		ITab tab2 = createTab(Constants.SUBJECT_MAIN_TAB);
		moduleTabsList.add(tab1);
		moduleTabsList.add(tab2);
		return moduleTabsList;
	}
	
	public ITab createTab(final String tabName) {
		
		return  new AbstractTab(new Model<String>(tabName)) {
			/**
			 * Implement this to secure the module
			 */
			public boolean isVisible(){
				//If the logged in user is a member of this module then allow him to view this tab
				return true;
			}
			@Override
			public Panel getPanel(String pid) {
				Panel panelToReturn = null;//Set up a common tab that will be accessible for all users
				if(tabName.equals(Constants.STUDY_MAIN_TAB)){
					panelToReturn =  new StudySubMenuTab(pid, studyNameMarkup, studyLogoMarkup, arkContextMarkup);//The sub menus for Study 
				}else if(tabName.equalsIgnoreCase(Constants.SUBJECT_MAIN_TAB)){
					panelToReturn = new SubjectSubMenuTab(pid);
				}
				return panelToReturn;
			}
			
		};
		
	}

}
