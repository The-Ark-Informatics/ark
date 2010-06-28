package au.org.theark.study.web.menu;
import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.extensions.markup.html.tabs.AbstractTab;
import org.apache.wicket.extensions.markup.html.tabs.ITab;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;

import au.org.theark.core.service.IMainTabProvider;
import au.org.theark.study.web.Constants;

/**
 * The main class that implements the common service IMainTabProvider.This contributes the
 * Tab menu which forms the entry point into Study module. As part of the main Tab that it contributes 
 * it will also contain the sub-menu tabs.
 * @author nivedann
 *
 */
public class MainTabProviderImpl extends Panel implements  IMainTabProvider {

	private static final long serialVersionUID = 1L;
	
	List<ITab> moduleTabsList;
	public MainTabProviderImpl(String panelId){
		super(panelId);
		moduleTabsList = new ArrayList<ITab>();
	}
	
	
	public  List<ITab> buildTabs(){
		ITab tab1 = createTab(Constants.STUDY_MAIN_TAB);//Forms the Main Top level Tab
		moduleTabsList.add(tab1);
		return moduleTabsList;
	}
	
	
	
	public ITab createTab(String tabName) {
		return  new AbstractTab(new Model(tabName)) {
		
			private static final long serialVersionUID = 1L;

			@Override
			public Panel getPanel(String pid) {
				return new StudySubMenuTab(pid);//The sub menus Study 
			}
			
		};
	}




}
