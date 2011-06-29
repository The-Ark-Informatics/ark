package au.org.theark.lims.web.menu;

import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.extensions.markup.html.tabs.AbstractTab;
import org.apache.wicket.extensions.markup.html.tabs.ITab;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import au.org.theark.core.model.study.entity.ArkFunction;
import au.org.theark.core.model.study.entity.ArkModule;
import au.org.theark.core.service.IArkCommonService;
import au.org.theark.core.web.component.ArkAjaxTabbedPanel;
import au.org.theark.core.web.component.menu.AbstractArkTabPanel;
import au.org.theark.lims.web.Constants;
import au.org.theark.lims.web.component.bioCollection.BioCollectionContainerPanel;
import au.org.theark.lims.web.component.biospecimen.BiospecimenContainerPanel;
import au.org.theark.lims.web.component.subject.SubjectContainerPanel;

@SuppressWarnings("serial")
public class LimsSubMenuTab extends AbstractArkTabPanel
{
	@SpringBean( name = au.org.theark.core.Constants.ARK_COMMON_SERVICE)
	private IArkCommonService<Void> iArkCommonService;
	
	private List<ITab> tabList;
	private WebMarkupContainer	arkContextMarkup;
	private ArkFunction  arkFunction;
	
	public LimsSubMenuTab(String id)
	{
		super(id);
		tabList = new ArrayList<ITab>();
		buildTabs();
	}

	public LimsSubMenuTab(String id, WebMarkupContainer arkContextMarkup)
	{
		super(id);
		tabList = new ArrayList<ITab>();
		this.arkContextMarkup = arkContextMarkup;
		buildTabs();
	}

	public void buildTabs()
	{
		List<ITab> moduleSubTabsList = new ArrayList<ITab>();
		
		ArkModule arkModule = iArkCommonService.getArkModuleByName(au.org.theark.core.Constants.ARK_MODULE_LIMS);
		List<ArkFunction>   arkFunctionList = iArkCommonService.getModuleFunction(arkModule);//Gets a list of ArkFunctions for the given Module
		
		for (final ArkFunction menuArkFunction : arkFunctionList) {
			moduleSubTabsList.add(new AbstractTab(new StringResourceModel(menuArkFunction.getResourceKey(),this, null))
			{
				@Override
				public Panel getPanel(String panelId)
				{
					return buildPanels(menuArkFunction.getName(), panelId);
				}
			});
		}
		
		ArkAjaxTabbedPanel moduleTabbedPanel = new ArkAjaxTabbedPanel(Constants.MENU_LIMS_SUBMENU, moduleSubTabsList);
		add(moduleTabbedPanel);
	}
	
	protected Panel buildPanels(final String functionName, String panelId){
		Panel panelToReturn = null;// Set
		
		if(functionName.equalsIgnoreCase(au.org.theark.core.Constants.FUNCTION_KEY_VALUE_LIMS_SUBJECT)){
			arkFunction = iArkCommonService.getArkFunctionByName(au.org.theark.core.Constants.FUNCTION_KEY_VALUE_LIMS_SUBJECT); //Place a default use case into session
			processAuthorizationCache(au.org.theark.core.Constants.ARK_MODULE_LIMS,arkFunction);
			panelToReturn = new SubjectContainerPanel(panelId, arkContextMarkup);//Note the constructor
		}
		else if(functionName.equalsIgnoreCase(au.org.theark.core.Constants.FUNCTION_KEY_VALUE_LIMS_COLLECTION)){
			arkFunction = iArkCommonService.getArkFunctionByName(au.org.theark.core.Constants.FUNCTION_KEY_VALUE_LIMS_COLLECTION); //Place a default use case into session
			processAuthorizationCache(au.org.theark.core.Constants.ARK_MODULE_LIMS,arkFunction);
			panelToReturn = new BioCollectionContainerPanel(panelId, arkContextMarkup);//Note the constructor
		}
		else if(functionName.equalsIgnoreCase(au.org.theark.core.Constants.FUNCTION_KEY_VALUE_BIOSPECIMEN)){
			arkFunction = iArkCommonService.getArkFunctionByName(au.org.theark.core.Constants.FUNCTION_KEY_VALUE_BIOSPECIMEN); //Place a default use case into session
			processAuthorizationCache(au.org.theark.core.Constants.ARK_MODULE_LIMS,arkFunction);
			panelToReturn = new BiospecimenContainerPanel(panelId, arkContextMarkup);//Note the constructor
		}	
		return panelToReturn;
	}
	

}