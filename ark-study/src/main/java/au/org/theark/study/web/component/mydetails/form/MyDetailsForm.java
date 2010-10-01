package au.org.theark.study.web.component.mydetails.form;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.PasswordTextField;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.validation.validator.EmailAddressValidator;
import org.apache.wicket.validation.validator.StringValidator;
import org.odlabs.wiquery.ui.themes.ThemeUiHelper;

import au.org.theark.core.exception.ArkSystemException;
import au.org.theark.core.vo.ArkUserVO;
import au.org.theark.study.service.IUserService;
import au.org.theark.study.web.Constants;
import au.org.theark.study.web.component.user.form.ContainerForm;

public class MyDetailsForm extends Form<ArkUserVO>{

	@SpringBean( name = "userService")
	private IUserService userService;
	
	protected TextField<String> userNameTxtField  =new TextField<String>(Constants.USER_NAME);
	protected TextField<String> firstNameTxtField = new TextField<String>(Constants.FIRST_NAME);
	protected TextField<String> lastNameTxtField = new TextField<String>(Constants.LAST_NAME);
	protected TextField<String> emailTxtField = new TextField<String>(Constants.EMAIL);
	protected TextField<String> phoneNumberTxtField = new TextField<String>(Constants.PHONE_NUMBER);
	protected PasswordTextField userPasswordField = new PasswordTextField(Constants.PASSWORD);
	protected PasswordTextField confirmPasswordField = new PasswordTextField(Constants.CONFIRM_PASSWORD);
	protected PasswordTextField oldPasswordField = new PasswordTextField(Constants.OLD_PASSWORD);
	protected WebMarkupContainer groupPasswordContainer = new WebMarkupContainer("groupPasswordContainer");
	//private AjaxButton cancelBtn;
	private AjaxButton saveBtn;

	protected void onSave( AjaxRequestTarget target){
		
	}

	protected void onCancel( AjaxRequestTarget target){
		
	}

	public TextField<String> getUserNameTxtField() {
		return userNameTxtField;
	}
	
	public void initialiseForm(){
		
		userNameTxtField.setRequired(true);
		firstNameTxtField.setRequired(true);
		emailTxtField.setRequired(true);
		emailTxtField.add(EmailAddressValidator.getInstance());
		lastNameTxtField.setRequired(true);
		userPasswordField.setRequired(false);
		confirmPasswordField.setRequired(false);
		attachValidators();
		decorateComponents();
		addComponents();
	}
	
	
	private void decorateComponents(){
		
		ThemeUiHelper.componentRounded(userNameTxtField);
		ThemeUiHelper.componentRounded(firstNameTxtField);
		ThemeUiHelper.componentRounded(emailTxtField);
		ThemeUiHelper.componentRounded(lastNameTxtField);
		ThemeUiHelper.componentRounded(userPasswordField);
		ThemeUiHelper.componentRounded(confirmPasswordField);
		ThemeUiHelper.componentRounded(saveBtn);
		//ThemeUiHelper.componentRounded(cancelBtn);
	}
	
	private void attachValidators(){
		userNameTxtField.add(EmailAddressValidator.getInstance());
		firstNameTxtField.add(StringValidator.lengthBetween(3, 50));
		lastNameTxtField.add(StringValidator.lengthBetween(3, 50));
		userNameTxtField.add(StringValidator.lengthBetween(3, 50));
	}
	
	private void addComponents(){
		
		add(userNameTxtField);
		add(firstNameTxtField);
		add(lastNameTxtField);
		add(emailTxtField);
		groupPasswordContainer.add(userPasswordField);
		groupPasswordContainer.add(confirmPasswordField);
		add(groupPasswordContainer);
		//add(cancelBtn.setDefaultFormProcessing(false));
		add(saveBtn);
	}
	
	
	public MyDetailsForm(String id, CompoundPropertyModel<ArkUserVO> model) {
		super(id, model);
		// TODO Auto-generated constructor stub
		saveBtn = new AjaxButton(Constants.SAVE, new StringResourceModel("saveKey", this, null))
		{

			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
				onSave(target);
			}
			
			public void onError(AjaxRequestTarget target, Form<?> form){
				
			}
		};
	}

}
