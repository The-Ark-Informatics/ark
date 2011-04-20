/**
 * 
 * This is a new file
 *
 *
 */
package au.org.theark.study.web.menu;

import java.util.ArrayList;
import java.util.List;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.ThreadContext;
import org.apache.wicket.extensions.markup.html.tabs.AbstractTab;
import org.apache.wicket.extensions.markup.html.tabs.ITab;
import org.apache.wicket.extensions.markup.html.tabs.TabbedPanel;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;
import org.apache.wicket.spring.injection.annot.SpringBean;

import au.org.theark.core.Constants;
import au.org.theark.core.model.study.entity.ArkModule;
import au.org.theark.core.model.study.entity.ArkUsecase;
import au.org.theark.core.security.ArkLdapRealm;
import au.org.theark.core.service.IArkCommonService;
import au.org.theark.core.vo.MenuModule;
import au.org.theark.study.web.component.address.AddressContainerPanel;
import au.org.theark.study.web.component.consent.ConsentContainerPanel;
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
	
//	@SpringBean( name="arkLdapRealm")
//	private ArkLdapRealm realm;
	
	private WebMarkupContainer	arkContextMarkup;
	List<ITab> tabList;
	ArkUsecase  arkUseCase;
	ArkModule arkModule;
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
						
						arkUseCase = iArkCommonService.getArkUsecaseByName(au.org.theark.core.Constants.USECASE_KEY_VALUE_SUBJECT); //Place a default use case into session
						panelToReturn = new SubjectContainer(panelId, arkContextMarkup);//Note the constructor
					
					}
					else if(moduleName.getModuleName().equalsIgnoreCase(Constants.TAB_PERSON_PHONE)){

						arkUseCase = iArkCommonService.getArkUsecaseByName(au.org.theark.core.Constants.USECASE_KEY_VALUE_PHONE); //Place a default use case into session
						panelToReturn = new PhoneContainerPanel(panelId);
					
					}
					else if(moduleName.getModuleName().equalsIgnoreCase(Constants.TAB_PERSON_ADDRESS)){
						arkUseCase = iArkCommonService.getArkUsecaseByName(au.org.theark.core.Constants.USECASE_KEY_VALUE_ADDRESS); //Place a default use case into session
						panelToReturn = new AddressContainerPanel(panelId);
					}
					else if(moduleName.getModuleName().equalsIgnoreCase(Constants.TAB_SUBJECT_CONSENT)){

						arkUseCase = iArkCommonService.getArkUsecaseByName(au.org.theark.core.Constants.USECASE_KEY_VALUE_CONSENT); //Place a default use case into session
						panelToReturn = new ConsentContainerPanel(panelId);
					
					}
					else if(moduleName.getModuleName().equalsIgnoreCase(Constants.TAB_SUBJECT_SUBJECT_FILE)){

						arkUseCase = iArkCommonService.getArkUsecaseByName(au.org.theark.core.Constants.USECASE_KEY_VALUE_SUBJECT_FILE); //Place a default use case into session
						panelToReturn = new SubjectFileContainerPanel(panelId);
					}
					else if(moduleName.getModuleName().equalsIgnoreCase(Constants.TAB_SUBJECT_SUBJECT_UPLOAD)){
						
						arkUseCase = iArkCommonService.getArkUsecaseByName(au.org.theark.core.Constants.USECASE_KEY_VALUE_SUBJECT_UPLOAD); //Place a default use case into session
						panelToReturn = new SubjectUploadContainerPanel(panelId);
					}
					
					arkModule = iArkCommonService.getArkModuleByName(au.org.theark.core.Constants.ARK_MODULE_SUBJECT); //Place a default module into session
					SecurityUtils.getSubject().getSession().setAttribute(au.org.theark.core.Constants.ARK_USECASE_KEY, arkUseCase.getId());
					SecurityUtils.getSubject().getSession().setAttribute(au.org.theark.core.Constants.ARK_MODULE_KEY, arkModule.getId());
					SecurityManager securityManager =  ThreadContext.getSecurityManager();
					Subject currentUser = SecurityUtils.getSubject();	
					//realm.clearCachedAuthorizationInfo(currentUser.getPrincipals());
					//securityManager.hasRole(currentUser.getPrincipals(), "Administrator");//Enforce authorization check here so it loads the roles
					return panelToReturn;
				};
			});
		}
		
		TabbedPanel moduleTabbedPanel = new TabbedPanel(Constants.MENU_SUBJECT_SUBMENU, moduleSubTabsList);
		add(moduleTabbedPanel);
	}

}
