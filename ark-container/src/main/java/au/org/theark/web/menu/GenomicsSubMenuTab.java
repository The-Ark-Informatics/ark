package au.org.theark.web.menu;

import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.extensions.markup.html.tabs.AbstractTab;
import org.apache.wicket.extensions.markup.html.tabs.ITab;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import au.org.theark.core.model.study.entity.ArkFunction;
import au.org.theark.core.model.study.entity.ArkModule;
import au.org.theark.core.service.IArkCommonService;
import au.org.theark.core.web.component.menu.AbstractArkTabPanel;
import au.org.theark.core.web.component.tabbedPanel.ArkAjaxTabbedPanel;
import au.org.theark.genomics.util.Constants;
import au.org.theark.genomics.web.component.datacenter.DataCenterContainerPanel;
import au.org.theark.genomics.web.component.microservice.MicroServiceContainerPanel;
import au.org.theark.worktracking.web.component.researcher.ResearcherContainerPanel;

public class GenomicsSubMenuTab extends AbstractArkTabPanel {
	private static final long serialVersionUID = -3695404298701886701L;

	@SpringBean(name = au.org.theark.core.Constants.ARK_COMMON_SERVICE)
	private IArkCommonService<Void> iArkCommonService;

	private List<ITab> moduleSubTabsList;

	public GenomicsSubMenuTab(String id) {
		super(id);
		moduleSubTabsList = new ArrayList<ITab>();
		buildTabs();
	}
	
	public void buildTabs() {		
		ArkModule arkModule = iArkCommonService.getArkModuleByName(au.org.theark.core.Constants.ARK_MODULE_GENOMICS);
		List<ArkFunction> arkFunctionList = iArkCommonService.getModuleFunction(arkModule);// Gets a list of ArkFunctions for the given Module

		/*
		 * Iterate each ArkFunction render the Tabs.When something is clicked it uses the arkFunction and calls processAuthorizationCache to clear
		 * principals of the user and loads the new set of principals.(permissions)
		 */
		for (final ArkFunction menuArkFunction : arkFunctionList) {
			moduleSubTabsList.add(new AbstractTab(new StringResourceModel(menuArkFunction.getResourceKey(), this, null)) {
				/**
				 * 
				 */
				private static final long	serialVersionUID	= -8421399480756599074L;

				@Override
				public Panel getPanel(String panelId) {
					Panel panelToReturn = null;// Set up a common tab that will be accessible for all users

					// Clear authorisation cache
					processAuthorizationCache(au.org.theark.core.Constants.ARK_MODULE_GENOMICS, menuArkFunction);
					
					if (menuArkFunction.getName().equalsIgnoreCase(Constants.MICRO_SERVICE)) {
						panelToReturn = new MicroServiceContainerPanel(panelId);
					}
					else if (menuArkFunction.getName().equalsIgnoreCase(Constants.DATA_CENTER)) {
						panelToReturn = new DataCenterContainerPanel(panelId);
					}
					return panelToReturn;
				}
			});
		}

		ArkAjaxTabbedPanel moduleTabbedPanel = new ArkAjaxTabbedPanel(Constants.GENO_SUBMENU, moduleSubTabsList);
		add(moduleTabbedPanel);
	}
}
