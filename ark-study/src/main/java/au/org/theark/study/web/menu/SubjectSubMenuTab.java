package au.org.theark.study.web.menu;

import java.util.ArrayList;
import java.util.List;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.apache.wicket.extensions.markup.html.tabs.AbstractTab;
import org.apache.wicket.extensions.markup.html.tabs.ITab;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;
import org.apache.wicket.spring.injection.annot.SpringBean;

import au.org.theark.core.Constants;
import au.org.theark.core.model.study.entity.ArkFunction;
import au.org.theark.core.model.study.entity.ArkModule;
import au.org.theark.core.security.ArkLdapRealm;
import au.org.theark.core.service.IArkCommonService;
import au.org.theark.core.vo.MenuModule;
import au.org.theark.core.web.component.ArkAjaxTabbedPanel;
import au.org.theark.study.web.component.address.AddressContainerPanel;
import au.org.theark.study.web.component.consent.ConsentContainerPanel;
import au.org.theark.study.web.component.correspondence.CorrespondenceContainerPanel;
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
	private ArkFunction  arkFunction;
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
	
	public  void buildTabs(){
		
		List<ITab> moduleSubTabsList = new ArrayList<ITab>();
		List<MenuModule> moduleTabs = new ArrayList<MenuModule>();
		
		//THis way we can get the menus from the back-end. We should source this data from a table in the backend and wrap it up in a class like this
		MenuModule menuModule = new MenuModule();
		menuModule.setModuleName(Constants.TAB_SUBJECT_DETAIL);
		menuModule.setResourceKey(Constants.TAB_MODULE_SUBJECT_DETAIL);
		moduleTabs.add(menuModule);

		menuModule = new MenuModule();
		menuModule.setModuleName(Constants.TAB_PERSON_PHONE);
		menuModule.setResourceKey(Constants.TAB_MODULE_PERSON_PHONE);
		moduleTabs.add(menuModule);
		
		menuModule = new MenuModule();
		menuModule.setModuleName(Constants.TAB_PERSON_ADDRESS);
		menuModule.setResourceKey(Constants.TAB_MODULE_PERSON_ADDRESS);
		moduleTabs.add(menuModule);

		menuModule = new MenuModule();
		menuModule.setModuleName(Constants.TAB_SUBJECT_CONSENT);
		menuModule.setResourceKey(Constants.TAB_MODULE_SUBJECT_CONSENT);
		moduleTabs.add(menuModule);
		
		menuModule = new MenuModule();
		menuModule.setModuleName(Constants.TAB_SUBJECT_CORRESPONDENCE);
		menuModule.setResourceKey(Constants.TAB_MODULE_SUBJECT_CORRESPONDENCE);
		moduleTabs.add(menuModule);
		
		menuModule = new MenuModule();
		menuModule.setModuleName(Constants.TAB_SUBJECT_SUBJECT_FILE);
		menuModule.setResourceKey(Constants.TAB_MODULE_SUBJECT_SUBJECT_FILE);
		moduleTabs.add(menuModule);
		
		menuModule = new MenuModule();
		menuModule.setModuleName(Constants.TAB_SUBJECT_SUBJECT_UPLOAD);
		menuModule.setResourceKey(Constants.TAB_MODULE_SUBJECT_SUBJECT_UPLOAD);
		moduleTabs.add(menuModule);

		
		for(final MenuModule moduleName : moduleTabs)
		{
			moduleSubTabsList.add( new AbstractTab(new Model<String>(getLocalizer().getString(moduleName.getResourceKey(), SubjectSubMenuTab.this, moduleName.getModuleName())) )
			{
				public boolean isVisible(){
					
					return true;
				}
				
				@Override
				public Panel getPanel(String panelId) 
				{

					Panel panelToReturn = null;//Set up a common tab that will be accessible for all users
					
					if(moduleName.getModuleName().equalsIgnoreCase(Constants.TAB_SUBJECT_DETAIL)){
						arkFunction = iArkCommonService.getArkFunctionByName(au.org.theark.core.Constants.FUNCTION_KEY_VALUE_SUBJECT); //Place a default use case into session
						processAuthorizationCache(arkFunction);
						panelToReturn = new SubjectContainer(panelId, arkContextMarkup);//Note the constructor
					}
					else if(moduleName.getModuleName().equalsIgnoreCase(Constants.TAB_PERSON_PHONE)){
						arkFunction = iArkCommonService.getArkFunctionByName(au.org.theark.core.Constants.FUNCTION_KEY_VALUE_PHONE); //Place a default use case into session
						processAuthorizationCache(arkFunction);
						panelToReturn = new PhoneContainerPanel(panelId);
					}
					else if(moduleName.getModuleName().equalsIgnoreCase(Constants.TAB_PERSON_ADDRESS)){
						arkFunction = iArkCommonService.getArkFunctionByName(au.org.theark.core.Constants.FUNCTION_KEY_VALUE_ADDRESS); //Place a default use case into session
						processAuthorizationCache(arkFunction);
						panelToReturn = new AddressContainerPanel(panelId);
					}
					else if(moduleName.getModuleName().equalsIgnoreCase(Constants.TAB_SUBJECT_CONSENT)){
						
						arkFunction = iArkCommonService.getArkFunctionByName(au.org.theark.core.Constants.FUNCTION_KEY_VALUE_CONSENT); //Place a default use case into session
						processAuthorizationCache(arkFunction);
						panelToReturn = new ConsentContainerPanel(panelId);
					}
					else if(moduleName.getModuleName().equalsIgnoreCase(Constants.TAB_SUBJECT_SUBJECT_FILE)){
					
						arkFunction = iArkCommonService.getArkFunctionByName(au.org.theark.core.Constants.FUNCTION_KEY_VALUE_SUBJECT_FILE); //Place a default use case into session
						processAuthorizationCache(arkFunction);
						panelToReturn = new SubjectFileContainerPanel(panelId);
					}
					else if(moduleName.getModuleName().equalsIgnoreCase(Constants.TAB_SUBJECT_SUBJECT_UPLOAD)){
						
						arkFunction = iArkCommonService.getArkFunctionByName(au.org.theark.core.Constants.FUNCTION_KEY_VALUE_SUBJECT_UPLOAD); //Place a default use case into session
						processAuthorizationCache(arkFunction);
						panelToReturn = new SubjectUploadContainerPanel(panelId);
					}
					else if(moduleName.getModuleName().equalsIgnoreCase(Constants.TAB_SUBJECT_CORRESPONDENCE)) {
						arkFunction = iArkCommonService.getArkFunctionByName(au.org.theark.core.Constants.FUNCTION_KEY_VALUE_SUBJECT_CORRESPONDENCE); //Place a default use case into session						
						processAuthorizationCache(arkFunction);
						panelToReturn = new CorrespondenceContainerPanel(panelId);
					}
					return panelToReturn;
				};
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