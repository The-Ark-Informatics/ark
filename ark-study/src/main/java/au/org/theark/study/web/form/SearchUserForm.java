package au.org.theark.study.web.form;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.ThreadContext;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.validation.validator.EmailAddressValidator;
import org.apache.wicket.validation.validator.StringValidator;
import org.odlabs.wiquery.ui.themes.ThemeUiHelper;

import au.org.theark.core.security.RoleConstants;
import au.org.theark.core.vo.ArkUserVO;
import au.org.theark.study.web.Constants;

@SuppressWarnings("serial")
public class SearchUserForm extends Form<ArkUserVO>{
	
	Long sessionStudyId;
	TextField<String> userNameTxtField =new TextField<String>(Constants.USER_NAME);
	TextField<String> firstNameTxtField = new TextField<String>(Constants.FIRST_NAME);
	TextField<String> lastNameTxtField = new TextField<String>(Constants.LAST_NAME);
	TextField<String> emailTxtField = new TextField<String>(Constants.EMAIL);
	AjaxButton searchBtn;
	AjaxButton newBtn;
	
	private void initFormFields(){
		emailTxtField.add(EmailAddressValidator.getInstance());
		firstNameTxtField.add(StringValidator.lengthBetween(3, 50));
		lastNameTxtField.add(StringValidator.lengthBetween(3, 50));
		userNameTxtField.add(StringValidator.lengthBetween(3, 50));
	}
	
	/**
	 * New Constructor that will be used.
	 * @param id
	 * @param model
	 */
	public SearchUserForm(String id, CompoundPropertyModel<ArkUserVO> model){
		
		super(id,model);
		
		initFormFields();
		
		searchBtn = new AjaxButton(Constants.SEARCH, new StringResourceModel(Constants.PAGE_SEARCH, this, null))
		{
			public void onSubmit(AjaxRequestTarget target, Form<?> form)
			{
				
				onSearch(target);
			}
		};
		
		
		
		newBtn = new AjaxButton(Constants.NEW, new StringResourceModel(Constants.PAGE_NEW, this, null))
		{
			public void onSubmit(AjaxRequestTarget target, Form<?> form)
			{
				onNew(target);
			}
			@Override
			public boolean isVisible(){
				
				SecurityManager securityManager =  ThreadContext.getSecurityManager();
				Subject currentUser = SecurityUtils.getSubject();		
				boolean flag = false;
				if(		securityManager.hasRole(currentUser.getPrincipals(), RoleConstants.ARK_SUPER_ADMIN) ||
						securityManager.hasRole(currentUser.getPrincipals(), RoleConstants.STUDY_ADMIN)){
					flag = true;
				}
				//if it is a Super or Study admin then make the new available
				return flag;
			}
		
		};
		
		/* Secure the Action buttons if there is no study in context */
		sessionStudyId = (Long)SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.core.Constants.STUDY_CONTEXT_ID);
		if(sessionStudyId == null){
			searchBtn.setEnabled(false);
			newBtn.setEnabled(false);
			this.error("There is no study in context. You can only search or manage users based on a study.");
			
		}else{
			newBtn.setEnabled(true);
			searchBtn.setEnabled(true);
		}
		
		addComponentsToForm();
	}
	
	private void decorateComponents(){
		ThemeUiHelper.componentRounded(userNameTxtField);
		ThemeUiHelper.componentRounded(firstNameTxtField);
		ThemeUiHelper.componentRounded(lastNameTxtField);
		ThemeUiHelper.componentRounded(emailTxtField);
		ThemeUiHelper.componentRounded(searchBtn);
		ThemeUiHelper.componentRounded(newBtn);
	}

	private void addComponentsToForm(){
		/* Add the look up fields */
		add(userNameTxtField);
		add(firstNameTxtField);
		add(lastNameTxtField);
		add(emailTxtField);
		add(searchBtn);
		add(newBtn);
	}
	
	// A non-ajax function
	protected void onReset(){
		clearInput();
		updateFormComponentModels();
	}
	
	
	protected void onSearch(AjaxRequestTarget target){}
	
	protected void onNew(AjaxRequestTarget target){}
	
}



