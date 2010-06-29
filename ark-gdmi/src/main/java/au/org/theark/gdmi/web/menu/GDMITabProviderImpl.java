package au.org.theark.gdmi.web.menu;

import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.extensions.markup.html.tabs.AbstractTab;
import org.apache.wicket.extensions.markup.html.tabs.ITab;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;

import au.org.theark.core.service.IMainTabProvider;

public class GDMITabProviderImpl extends Panel implements IMainTabProvider {


	private static final long serialVersionUID = 1L;
	
	List<ITab> moduleTabsList;
	public GDMITabProviderImpl(String panelId){
		super(panelId);
		moduleTabsList = new ArrayList<ITab>();
	}
	
	
	public  List<ITab> buildTabs(){
		ITab tab1 = createTab("Genotypic");//Forms the Main Top level Tab
		moduleTabsList.add(tab1);
		return moduleTabsList;
	}
	
	
	
	public ITab createTab(String tabName) {
		// TODO Auto-generated method stub
		return  new AbstractTab(new Model(tabName)) {
		
			private static final long serialVersionUID = 1L;

			@Override
			public boolean isVisible(){
				return true;
				//ArkSecurityManager asm = ArkSecurityManager.getInstance();
				//return asm.subjectHasRole(RoleConstants.ARK_SUPER_ADMIN);
			}
			@Override
			public Panel getPanel(String pid) {
				System.out.println("Panel ID in getPanel " + pid);
				return new GDMISubMenuTab(pid);//The sub menus Study 
			}
			
		};
	}
}
