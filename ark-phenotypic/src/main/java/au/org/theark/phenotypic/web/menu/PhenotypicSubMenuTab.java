package au.org.theark.phenotypic.web.menu;

import java.util.ArrayList;
import java.util.List;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.apache.wicket.extensions.markup.html.tabs.AbstractTab;
import org.apache.wicket.extensions.markup.html.tabs.ITab;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import au.org.theark.core.model.study.entity.ArkFunction;
import au.org.theark.core.model.study.entity.ArkModule;
import au.org.theark.core.security.ArkLdapRealm;
import au.org.theark.core.service.IArkCommonService;
import au.org.theark.core.web.component.ArkAjaxTabbedPanel;
import au.org.theark.phenotypic.web.Constants;
import au.org.theark.phenotypic.web.component.field.FieldContainerPanel;
import au.org.theark.phenotypic.web.component.fieldData.FieldDataContainerPanel;
import au.org.theark.phenotypic.web.component.fieldUpload.FieldUploadContainerPanel;
import au.org.theark.phenotypic.web.component.phenoCollection.PhenoCollectionContainerPanel;
import au.org.theark.phenotypic.web.component.phenoUpload.PhenoUploadContainerPanel;
import au.org.theark.phenotypic.web.component.summaryModule.SummaryContainerPanel;

@SuppressWarnings( { "serial", "unused" })
public class PhenotypicSubMenuTab extends Panel
{
	@SpringBean( name = au.org.theark.core.Constants.ARK_COMMON_SERVICE)
	private IArkCommonService<Void> iArkCommonService;
	
	@SpringBean( name="arkLdapRealm")
	private ArkLdapRealm realm;
	
	private transient Logger	log					= LoggerFactory.getLogger(PhenotypicSubMenuTab.class);
	private transient Subject	currentUser;
	private transient Long		studyId;
	private WebMarkupContainer	arkContextMarkup;
	private List<ITab>						moduleSubTabsList	= new ArrayList<ITab>();
	private ArkFunction  arkFunction;
	private ArkModule arkModule;

	public PhenotypicSubMenuTab(String id, WebMarkupContainer arkContextMarkup)
	{
		super(id);
		this.arkContextMarkup = arkContextMarkup;
		buildTabs(arkContextMarkup);
	}
	

	public void buildTabs(final WebMarkupContainer arkContextMarkup)
	{
		//createMenuModule();
		
		ArkModule arkModule = iArkCommonService.getArkModuleByName(au.org.theark.core.Constants.ARK_MODULE_PHENOTYPIC);
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

		ArkAjaxTabbedPanel moduleTabbedPanel = new ArkAjaxTabbedPanel(Constants.PHENOTYPIC_SUBMENU, moduleSubTabsList);
		add(moduleTabbedPanel);
	}
	
	protected Panel buildPanels(final String functionName, String panelId){
		Panel panelToReturn = null;// Set
		
		if(functionName.equalsIgnoreCase(Constants.PHENOTYPIC_SUMMARY_SUBMENU)){
			
			panelToReturn = new SummaryContainerPanel(panelId); // Note the constructor
		}
		else if(functionName.equalsIgnoreCase(Constants.FIELD_SUBMENU)){
			arkFunction = iArkCommonService.getArkFunctionByName(au.org.theark.core.Constants.FUNCTION_KEY_VALUE_DATA_DICTIONARY); //Place a default use case into session
			processAuthorizationCache(arkFunction);
			panelToReturn = new FieldContainerPanel(panelId); // Note the constructor
		}
		else if(functionName.equalsIgnoreCase(Constants.FIELD_UPLOAD_SUBMENU)){
			arkFunction = iArkCommonService.getArkFunctionByName(au.org.theark.core.Constants.FUNCTION_KEY_VALUE_DATA_DICTIONARY_UPLOAD); //Place a default use case into session
			processAuthorizationCache(arkFunction);
			panelToReturn = new FieldUploadContainerPanel(panelId); // Note the constructor
		}
		else if(functionName.equalsIgnoreCase(Constants.PHENO_COLLECTION_SUBMENU)){
			arkFunction = iArkCommonService.getArkFunctionByName(au.org.theark.core.Constants.FUNCTION_KEY_VALUE_PHENO_COLLECTION); //Place a default use case into session
			processAuthorizationCache(arkFunction);
			panelToReturn = new PhenoCollectionContainerPanel(panelId, arkContextMarkup); // Note the constructor
		}
		else if(functionName.equalsIgnoreCase(Constants.FIELD_DATA_SUBMENU)){
			arkFunction = iArkCommonService.getArkFunctionByName(au.org.theark.core.Constants.FUNCTION_KEY_VALUE_FIELD_DATA); //Place a default use case into session
			processAuthorizationCache(arkFunction);
			panelToReturn = new FieldDataContainerPanel(panelId); // Note the constructor
		}
		else if(functionName.equalsIgnoreCase(Constants.FIELD_DATA_UPLOAD_SUBMENU)){
			arkFunction = iArkCommonService.getArkFunctionByName(au.org.theark.core.Constants.FUNCTION_KEY_VALUE_FIELD_DATA_UPLOAD); //Place a default use case into session
			processAuthorizationCache(arkFunction);
			panelToReturn = new PhenoUploadContainerPanel(panelId); // Note the constructor
		}

		return panelToReturn; 
	}

	
	private void processAuthorizationCache(ArkFunction arkFunction){
		arkModule = iArkCommonService.getArkModuleByName(au.org.theark.core.Constants.ARK_MODULE_PHENOTYPIC); //Place a default module into session
		SecurityUtils.getSubject().getSession().setAttribute(au.org.theark.core.Constants.ARK_FUNCTION_KEY, arkFunction.getId());
		SecurityUtils.getSubject().getSession().setAttribute(au.org.theark.core.Constants.ARK_MODULE_KEY, arkModule.getId());
		Subject currentUser = SecurityUtils.getSubject();	
		realm.clearCachedAuthorizationInfo(currentUser.getPrincipals());
	}
}