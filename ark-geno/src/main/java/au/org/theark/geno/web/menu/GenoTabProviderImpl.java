package au.org.theark.geno.web.menu;

import java.util.ArrayList;
import java.util.List;

import org.apache.shiro.SecurityUtils;
import org.apache.wicket.extensions.markup.html.tabs.ITab;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;

import au.org.theark.core.service.IMainTabProvider;
import au.org.theark.core.web.component.ArkMainTab;
import au.org.theark.geno.web.Constants;

public class GenoTabProviderImpl extends Panel implements IMainTabProvider {


	private static final long serialVersionUID = 1L;
	
	List<ITab> moduleTabsList;
	public GenoTabProviderImpl(String panelId){
		super(panelId);
		moduleTabsList = new ArrayList<ITab>();
	}
	
	
	public  List<ITab> buildTabs(){
		ITab tab1 = createTab(Constants.GENOTYPIC_MAIN_TAB);//Forms the Main Top level Tab
		moduleTabsList.add(tab1);
		return moduleTabsList;
	}
	
	
	
	public ITab createTab(String tabName) {
		return  new ArkMainTab(new Model(tabName)) {
		
			private static final long serialVersionUID = 1L;

			@Override
			public boolean isVisible(){
				return true;
				//ArkSecurityManager asm = ArkSecurityManager.getInstance();
				//return asm.subjectHasRole(RoleConstants.ARK_SUPER_ADMIN);
			}
			@Override
			public Panel getPanel(String pid) {
				GenoSubMenuTab genoSubMenu = new GenoSubMenuTab(pid);//The sub menus Genotypic
				genoSubMenu.buildTabs();
				return genoSubMenu;
			}
			
			public boolean isAccessible()
			{
				Long sessionStudyId = (Long) SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.core.Constants.STUDY_CONTEXT_ID);
				if(sessionStudyId == null)
				{
					this.getPanel(Constants.GENOTYPIC_MAIN_TAB).error(au.org.theark.core.Constants.STUDY_IN_CONTEXT_MESSAGE);
					return false;
				}
				else
					return true;
			}
		};
	}
}
