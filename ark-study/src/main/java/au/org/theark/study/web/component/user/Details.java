package au.org.theark.study.web.component.user;

import java.util.List;

import javax.naming.InvalidNameException;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.util.StringUtils;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.PasswordTextField;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import au.org.theark.core.exception.ArkSystemException;
import au.org.theark.core.exception.UserNameExistsException;
import au.org.theark.core.vo.ArkUserVO;
import au.org.theark.core.vo.ModuleVO;
import au.org.theark.core.vo.RoleVO;
import au.org.theark.core.vo.StudyVO;
import au.org.theark.core.util.UIHelper;
import au.org.theark.study.model.entity.Study;
import au.org.theark.study.service.IStudyService;
import au.org.theark.study.service.IUserService;
import au.org.theark.study.web.Constants;
import au.org.theark.study.web.component.site.SiteModel;
import au.org.theark.study.web.component.user.form.ContainerForm;
import au.org.theark.study.web.form.UserForm;


/**
 * A Component that will have the user details. This component should be a standalone component and used in any page or panel that requires it.
 * @author nivedann
 */
@SuppressWarnings("serial")
public class Details extends Panel{

	private transient Logger log = LoggerFactory.getLogger(Details.class);

	@SpringBean( name = "userService")
	private IUserService userService;
	
	@SpringBean( name = Constants.STUDY_SERVICE)
	private IStudyService studyService;

	private Search searchPanel;
	private UserForm userForm;
	private AppRoleAccordion appRoleAccordion;

	
	/**
	 * Setters and Getters for private members
	 * @return
	 */
	public Search getSearchPanel() {
		return searchPanel;
	}

	public void setSearchPanel(Search searchPanel) {
		this.searchPanel = searchPanel;
	}

	public UserForm getUserForm() {
		return userForm;
	}

	public void setUserForm(UserForm userForm) {
		this.userForm = userForm;
	}

	
	private String customValidation(PasswordTextField password, PasswordTextField confirmPassword){
		String error ="";
		if(!StringUtils.hasText(password.getModelObject()) &&  !StringUtils.hasText(confirmPassword.getModelObject())){
			error ="Password fields are required.";//Resource bundle to add this error message
		}
		return error;
	}
	
	
	private FeedbackPanel feedBackPanel;
	private WebMarkupContainer listContainer;
	private WebMarkupContainer detailsContainer;
	private WebMarkupContainer searchPanelContainer;
	private ContainerForm containerForm;
	
	public Details(	String id,
					final WebMarkupContainer resultListContainer, 
					FeedbackPanel feedBackPanel,
					WebMarkupContainer detailPanelContainer,
					WebMarkupContainer searchPanelContainer,
					ContainerForm containerForm){
		
		super(id);
		this.feedBackPanel = feedBackPanel;
		this.listContainer = resultListContainer;
		this.detailsContainer = detailPanelContainer;
		this.searchPanelContainer = searchPanelContainer;
		this.containerForm = containerForm;
		
	}
	
	public void initialisePanel(){
		
		userForm = new UserForm(Constants.USER_DETAILS_FORM, listContainer, detailsContainer, containerForm){
			
			protected void onDelete(ArkUserVO arkUserVO, AjaxRequestTarget target)
			{
				log.info("Delete the user details from ldap");
				try{
					userService.deleteLdapUser(arkUserVO);
					this.info(arkUserVO.getUserName() + " was deleted successfully.");
					processFeedback(target);
				}catch(ArkSystemException arkSystemException){
					log.error("Exception occured while performing a delete on the user details in LDAP " + arkSystemException.getMessage());
				}catch(Exception ex){
					log.error("Exception occured when saving user details " + ex.getMessage());
				}
			}
			
			protected void onSave(ArkUserVO arkUserVO, AjaxRequestTarget target){
				
				try{

					if(arkUserVO.getMode() == Constants.MODE_NEW){
						
						processNew(arkUserVO,this);
						
						this.info(arkUserVO.getUserName() + " was added successfully.");
						this.groupPasswordContainer.setVisible(false);
						this.userNameTxtField.setEnabled(false);
						this.userPasswordField.setRequired(false);
						this.confirmPasswordField.setRequired(false);
						processFeedback(target);
						
					}else if(arkUserVO.getMode() == Constants.MODE_EDIT){
						
						processUpdate(arkUserVO, this);
						this.groupPasswordContainer.setVisible(false);
						this.userNameTxtField.setEnabled(false);
						processFeedback(target);
						
					}
					
					//Accordion will have its own form object
					//List<ModuleVO> modules = userService.getModules(true);
					
					//userForm.remove(appRoleAccordion);
					//arkUserVO.setMode(Constants.MODE_EDIT);
					
					//containerForm.getModelObject().setAvailableModules(modules);
					//appRoleAccordion = new AppRoleAccordion(Constants.APP_ROLE_ACCORDION, containerForm.getModelObject(), modules);
					//appRoleAccordion = new AppRoleAccordion(Constants.APP_ROLE_ACCORDION,containerForm);
					
					//appRoleAccordion = new AppRoleAccordion(Constants.APP_ROLE_ACCORDION, arkUserVO, modules);
					//userForm.add(appRoleAccordion);
					
					
				}catch (InvalidNameException e) {

					log.error("Exception occured while performing an update on the user details in LDAP " + e.getMessage());
					//userForm.info(userVO.getUserName() + " could not be added or updated. There seems to be a problem with the server. Please try again later");
				}catch(UserNameExistsException userNameExists){
					//userForm.error(userNameExists.getMessage());
				}catch(Exception ex){
					log.error("Exception occured when saving user details " + ex.getMessage());
					userForm.info("A System error has occured. We will have someone contact you.");
				}
				
				target.addComponent(feedBackPanel);
				
			}
			
			protected  void onCancel(AjaxRequestTarget target)
			{
				ArkUserVO arkUserVO = new ArkUserVO();
				containerForm.setModelObject(arkUserVO);
				searchPanelContainer.setVisible(true);
				detailsContainer.setVisible(false);
				target.addComponent(searchPanelContainer);
				target.addComponent(detailsContainer);
			}
			
		};
		
		
		try {
			/* Get A list of Module/Application names from the backend */
			List<ModuleVO> modules = userService.getModules(true);
			containerForm.getModelObject().setAvailableModules(modules);
			appRoleAccordion = new AppRoleAccordion(Constants.APP_ROLE_ACCORDION, containerForm.getModelObject(), modules);
			//appRoleAccordion = new AppRoleAccordion(Constants.APP_ROLE_ACCORDION,containerForm);
			//Accordion will have its own form object
			userForm.add(appRoleAccordion);
			
		} catch (ArkSystemException e) {
			e.printStackTrace();
		}
		
		userForm.initialiseForm();
		add(userForm);
		
	}
	
	
	private void processUpdate(ArkUserVO arkUserVO, UserForm userForm) throws ArkSystemException{
		boolean isValidRoles = AppRoleAccordion.validateRoles(userForm);
		if(isValidRoles)
		{
			AppRoleAccordion.getSelectedAppRoles(userForm, arkUserVO);
			mapToSystemValues(arkUserVO);
			userService.updateLdapUser(arkUserVO);
			userForm.info("Update was successful.");
			
		}else{
			this.error("The user has not been assigned a role.");
		}
	}
	
	private void processNew(ArkUserVO arkUserVO, UserForm userForm) throws ArkSystemException, UserNameExistsException, Exception{
		
		boolean isValidRoles = AppRoleAccordion.validateRoles(userForm);
		
		if(isValidRoles)
		{
			AppRoleAccordion.getSelectedAppRoles(userForm, arkUserVO);
			mapToSystemValues(arkUserVO);
			Long sessionStudyId = (Long)SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.core.Constants.STUDY_CONTEXT_ID);
			if(sessionStudyId != null){
				Study study = studyService.getStudy(sessionStudyId);
				StudyVO studyVO = new StudyVO();
				studyVO.setStudyName(study.getName());
				studyVO.setModules(arkUserVO.getModules());
				arkUserVO.setStudyVO(studyVO);
				userService.createLdapUser(arkUserVO);	
			}
		}
		else
		{
			this.error("The user has not been assigned a role.");
		}
		
	}

	
	/**
	 * A common method that maps all display values to system. Add the logic here as required
	 * 1. One of the usage is to map Module Names from Display to System 
	 * 2. Map Display Role names to system role names
	 * If any other lists or items are required to be mapped to system values place them here.
	 * This method should be called before invoking the service layer. 
	 * @param userVO
	 */
	private void mapToSystemValues(ArkUserVO userVO){
		List<ModuleVO> modules = userVO.getModules();
		
		for (ModuleVO moduleVO : modules) {
			moduleVO.setModule(UIHelper.getSystemModuleName(moduleVO.getModule()));
			
			List<RoleVO> roles = moduleVO.getRole();
			for( RoleVO roleVO: roles){
				roleVO.setRole( UIHelper.getSystemRoleName(roleVO.getRole()));
			}
		}
	}

}
