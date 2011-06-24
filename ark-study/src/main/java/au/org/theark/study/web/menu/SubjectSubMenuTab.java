package au.org.theark.study.web.menu;

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

import au.org.theark.core.Constants;
import au.org.theark.core.model.study.entity.ArkFunction;
import au.org.theark.core.model.study.entity.ArkModule;
import au.org.theark.core.security.ArkLdapRealm;
import au.org.theark.core.service.IArkCommonService;
import au.org.theark.core.web.component.ArkAjaxTabbedPanel;
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
public class SubjectSubMenuTab extends Panel{
	
	@SpringBean( name = au.org.theark.core.Constants.ARK_COMMON_SERVICE)
	private IArkCommonService iArkCommonService;
	
	@SpringBean( name="arkLdapRealm")
	private ArkLdapRealm realm;
	
	private WebMarkupContainer	arkContextMarkup;
	List<ITab> tabList;
	private ArkModule arkModule;
	
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
		//List<MenuModule> moduleTabs = new ArrayList<MenuModule>();
		
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
					
						processAuthorizationCache(menuArkFunction);
						panelToReturn = new SubjectContainer(panelId, arkContextMarkup);//Note the constructor
					}
					else if(menuArkFunction.getName().equalsIgnoreCase(au.org.theark.core.Constants.FUNCTION_KEY_VALUE_PHONE)){
					
						processAuthorizationCache(menuArkFunction);
						panelToReturn = new PhoneContainerPanel(panelId);
					}
					else if(menuArkFunction.getName().equalsIgnoreCase(au.org.theark.core.Constants.FUNCTION_KEY_VALUE_ADDRESS)){

						processAuthorizationCache(menuArkFunction);
						panelToReturn = new AddressContainerPanel(panelId);
					}
					else if(menuArkFunction.getName().equalsIgnoreCase(au.org.theark.core.Constants.FUNCTION_KEY_VALUE_CONSENT)){
						
						processAuthorizationCache(menuArkFunction);
						panelToReturn = new ConsentContainerPanel(panelId);
					}
					else if(menuArkFunction.getName().equalsIgnoreCase(au.org.theark.core.Constants.FUNCTION_KEY_VALUE_SUBJECT_FILE)){
						
						processAuthorizationCache(menuArkFunction);
						panelToReturn = new SubjectFileContainerPanel(panelId);
					}
					else if(menuArkFunction.getName().equalsIgnoreCase(au.org.theark.core.Constants.FUNCTION_KEY_VALUE_SUBJECT_UPLOAD)){
						
						processAuthorizationCache(menuArkFunction);
						panelToReturn = new SubjectUploadContainerPanel(panelId);
					}
					else if(menuArkFunction.getName().equalsIgnoreCase(au.org.theark.core.Constants.FUNCTION_KEY_VALUE_SUBJECT_CORRESPONDENCE)) {
						
						processAuthorizationCache(menuArkFunction);
						panelToReturn = new CorrespondenceContainerPanel(panelId);
					}else if(menuArkFunction.getName().equalsIgnoreCase(au.org.theark.core.Constants.FUNTION_KEY_VALUE_SUBJECT_CUSTOM)){
						processAuthorizationCache(menuArkFunction);
						panelToReturn = new CustomFieldContainer(panelId);
					}
		
					return panelToReturn;
				}
			
			});
		}
		
		ArkAjaxTabbedPanel moduleTabbedPanel = new ArkAjaxTabbedPanel(Constants.MENU_SUBJECT_SUBMENU, moduleSubTabsList);
		add(moduleTabbedPanel);
	}
	
	private void processAuthorizationCache(ArkFunction arkFunction){
		arkModule = iArkCommonService.getArkModuleByName(au.org.theark.core.Constants.ARK_MODULE_SUBJECT); //Place a default module into session
		SecurityUtils.getSubject().getSession().setAttribute(au.org.theark.core.Constants.ARK_FUNCTION_KEY, arkFunction.getId());
		SecurityUtils.getSubject().getSession().setAttribute(au.org.theark.core.Constants.ARK_MODULE_KEY, arkModule.getId());
		Subject currentUser = SecurityUtils.getSubject();	
		realm.clearCachedAuthorizationInfo(currentUser.getPrincipals());
	}

}