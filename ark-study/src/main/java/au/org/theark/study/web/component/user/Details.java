package au.org.theark.study.web.component.user;

import java.util.List;

import javax.naming.InvalidNameException;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.util.StringUtils;
import org.apache.wicket.markup.html.form.PasswordTextField;
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
	
	/**
	 * Constructor that has a reference to Search
	 * @param id
	 * @param userVO
	 * @param searchPanel
	 * @throws InvalidNameException 
	 */
	public Details(String id, ArkUserVO userVO, final Search searchPanel)  {
		
		super(id);
		setSearchPanel(searchPanel);
		
		userForm  = new UserForm(Constants.USER_DETAILS_FORM, new ArkUserVO()){
			
			private static final long serialVersionUID = 6077699021177330917L;
			
			/**
			 * When user clicks on save the VO is validated and then passed to the backend.
			 */
			protected  void onSave(ArkUserVO userVO){
				
				try {
					
					if(userVO.getMode() == Constants.MODE_NEW){
						
						boolean isValidRoles = AppRoleAccordion.validateRoles(this);
						//Run custom validations
						if(isValidRoles){
							AppRoleAccordion.getSelectedAppRoles(this, userVO);
							mapToSystemValues(userVO);
							//Get the study in context
							//TODO and set the study into the user vo
							
							Long sessionStudyId = (Long)SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.core.Constants.STUDY_CONTEXT_ID);
							if(sessionStudyId != null){
								Study study = studyService.getStudy(sessionStudyId);
								StudyVO studyVO = new StudyVO();
								studyVO.setStudyName(study.getName());
								studyVO.setModules(userVO.getModules());
								userVO.setStudyVO(studyVO);
								userService.createLdapUser(userVO);	
								userForm.info(userVO.getUserName() + " was added successfully.");
								this.groupPasswordContainer.setVisible(false);
								this.userNameTxtField.setEnabled(false);
								this.userPasswordField.setRequired(false);
								this.confirmPasswordField.setRequired(false);
							}
							
						}else{
							this.error("The user has not been assigned a role.");
						}
					   
					}else if(userVO.getMode() == Constants.MODE_EDIT){
						
						//Map any display values into system and set the VO before calling update
						//.e.g Module and Role names need to be mapped to System equivalent.
						boolean isValidRoles = AppRoleAccordion.validateRoles(this);
						if(isValidRoles){
							AppRoleAccordion.getSelectedAppRoles(this, userVO);
							mapToSystemValues(userVO);
							userService.updateLdapUser(userVO);
							userForm.info("Update was successful.");
							this.groupPasswordContainer.setVisible(false);
							this.userNameTxtField.setEnabled(false);
						}else{
							this.error("The user has not been assigned a role.");
						}
					}
					
					//Accordion will have its own form object
					List<ModuleVO> modules = userService.getModules(true);
					userForm.remove(appRoleAccordion);
					userVO.setMode(Constants.MODE_EDIT);
					appRoleAccordion = new AppRoleAccordion(Constants.APP_ROLE_ACCORDION, userVO, modules);
					userForm.add(appRoleAccordion);
				}catch (InvalidNameException e) {
					//e.printStackTrace();
					log.error("Exception occured while performing an update on the user details in LDAP " + e.getMessage());
					userForm.info(userVO.getUserName() + " could not be added or updated. There seems to be a problem with the server. Please try again later");
				}catch(UserNameExistsException userNameExists){
					userForm.error(userNameExists.getMessage());
				}
				catch(Exception ex){
					log.error("Exception occured when saving user details " + ex.getMessage());
					userForm.info("A System error has occured. We will have someone contact you.");
				}
			}
			protected void onCancel(){
				log.info("\n -----------------onCancel Clicked hide Details-----------------\n");
				log.info("SearchPanel.isVisible()" + searchPanel.isVisible() );
				searchPanel.setDetailsPanelVisible(false);
			}
			
			protected void onDelete(ArkUserVO userVO){
				log.info("Delete the user details from ldap");
				try{
					userService.deleteLdapUser(userVO);	
				}catch(ArkSystemException arkSystemException){
					log.error("Exception occured while performing a delete on the user details in LDAP " + arkSystemException.getMessage());
				}catch(Exception ex){
					log.error("Exception occured when saving user details " + ex.getMessage());
				}
			}
			
			
		};

		add(userForm);
		try {
			List<ModuleVO> modules = userService.getModules(true);
			appRoleAccordion = new AppRoleAccordion(Constants.APP_ROLE_ACCORDION, userVO, modules);
			//Accordion will have its own form object
			userForm.add(appRoleAccordion);
			
		} catch (ArkSystemException e) {
			e.printStackTrace();
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
