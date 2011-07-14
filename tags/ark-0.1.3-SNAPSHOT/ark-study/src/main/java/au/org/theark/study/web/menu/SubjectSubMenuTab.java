package au.org.theark.study.web.menu;

import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.extensions.markup.html.tabs.AbstractTab;
import org.apache.wicket.extensions.markup.html.tabs.ITab;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import au.org.theark.core.Constants;
import au.org.theark.core.model.study.entity.ArkFunction;
import au.org.theark.core.model.study.entity.ArkModule;
import au.org.theark.core.service.IArkCommonService;
import au.org.theark.core.web.component.ArkAjaxTabbedPanel;
import au.org.theark.core.web.component.menu.AbstractArkTabPanel;
import au.org.theark.study.web.component.address.AddressContainerPanel;
import au.org.theark.study.web.component.consent.ConsentContainerPanel;
import au.org.theark.study.web.component.correspondence.CorrespondenceContainerPanel;
import au.org.theark.study.web.component.customfield.CustomFieldContainer;
import au.org.theark.study.web.component.phone.PhoneContainerPanel;
import au.org.theark.study.web.component.subject.SubjectContainer;
import au.org.theark.study.web.component.subjectFile.SubjectFileContainerPanel;
import au.org.theark.study.web.component.subjectUpload.SubjectUploadContainerPanel;

/**
 * @author nivedann
 *
 */
public class SubjectSubMenuTab extends AbstractArkTabPanel{
	
	@SpringBean( name = au.org.theark.core.Constants.ARK_COMMON_SERVICE)
	private IArkCommonService iArkCommonService;
	
	private WebMarkupContainer	arkContextMarkup;
	private List<ITab> tabList;

	
	/**
	 * @param id
	 */
	public SubjectSubMenuTab(String id, WebMarkupContainer arkContextMarkup) {
		super(id);
		this.arkContextMarkup = arkContextMarkup;
		tabList = new ArrayList<ITab>();
		buildTabs();
	}
	
	@SuppressWarnings({ "serial", "unchecked" })
	public  void buildTabs(){
		
		List<ITab> moduleSubTabsList = new ArrayList<ITab>();
		
		ArkModule arkModule = iArkCommonService.getArkModuleByName(Constants.ARK_MODULE_SUBJECT);
		List<ArkFunction>   arkFunctionList = iArkCommonService.getModuleFunction(arkModule);//Gets a list of ArkFunctions for the given Module
		
		for (final ArkFunction menuArkFunction : arkFunctionList) {
			
			moduleSubTabsList.add(new AbstractTab(new StringResourceModel(menuArkFunction.getResourceKey(),this, null))
			{
				@Override
				public Panel getPanel(String panelId)
				{
					Panel panelToReturn = null;// Set
					if(menuArkFunction.getName().equalsIgnoreCase(au.org.theark.core.Constants.FUNCTION_KEY_VALUE_SUBJECT)){
					
						processAuthorizationCache(au.org.theark.core.Constants.ARK_MODULE_SUBJECT,menuArkFunction);
						panelToReturn = new SubjectContainer(panelId, arkContextMarkup);//Note the constructor
					}
					else if(menuArkFunction.getName().equalsIgnoreCase(au.org.theark.core.Constants.FUNCTION_KEY_VALUE_PHONE)){
					
						processAuthorizationCache(au.org.theark.core.Constants.ARK_MODULE_SUBJECT,menuArkFunction);
						panelToReturn = new PhoneContainerPanel(panelId);
					}
					else if(menuArkFunction.getName().equalsIgnoreCase(au.org.theark.core.Constants.FUNCTION_KEY_VALUE_ADDRESS)){

						processAuthorizationCache(au.org.theark.core.Constants.ARK_MODULE_SUBJECT,menuArkFunction);
						panelToReturn = new AddressContainerPanel(panelId);
					}
					else if(menuArkFunction.getName().equalsIgnoreCase(au.org.theark.core.Constants.FUNCTION_KEY_VALUE_CONSENT)){
						
						processAuthorizationCache(au.org.theark.core.Constants.ARK_MODULE_SUBJECT,menuArkFunction);
						panelToReturn = new ConsentContainerPanel(panelId);
					}
					else if(menuArkFunction.getName().equalsIgnoreCase(au.org.theark.core.Constants.FUNCTION_KEY_VALUE_SUBJECT_FILE)){
						
						processAuthorizationCache(au.org.theark.core.Constants.ARK_MODULE_SUBJECT,menuArkFunction);
						panelToReturn = new SubjectFileContainerPanel(panelId);
					}
					else if(menuArkFunction.getName().equalsIgnoreCase(au.org.theark.core.Constants.FUNCTION_KEY_VALUE_SUBJECT_UPLOAD)){
						
						processAuthorizationCache(au.org.theark.core.Constants.ARK_MODULE_SUBJECT,menuArkFunction);
						panelToReturn = new SubjectUploadContainerPanel(panelId);
					}
					else if(menuArkFunction.getName().equalsIgnoreCase(au.org.theark.core.Constants.FUNCTION_KEY_VALUE_SUBJECT_CORRESPONDENCE)) {
						
						processAuthorizationCache(au.org.theark.core.Constants.ARK_MODULE_SUBJECT,menuArkFunction);
						panelToReturn = new CorrespondenceContainerPanel(panelId);
					}
					 
					else if(menuArkFunction.getName().equalsIgnoreCase(au.org.theark.core.Constants.FUNCTION_KEY_VALUE_SUBJECT_CUSTOM)){
						processAuthorizationCache(au.org.theark.core.Constants.ARK_MODULE_SUBJECT,menuArkFunction);
						panelToReturn = new CustomFieldContainer(panelId);
					}
		
					return panelToReturn;
				}
			
			});
		}
		
		ArkAjaxTabbedPanel moduleTabbedPanel = new ArkAjaxTabbedPanel(Constants.MENU_SUBJECT_SUBMENU, moduleSubTabsList);
		add(moduleTabbedPanel);
	}

}