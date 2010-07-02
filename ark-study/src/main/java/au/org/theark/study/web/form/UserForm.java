package au.org.theark.study.web.form;

import org.apache.shiro.util.StringUtils;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.WebMarkupContainerWithAssociatedMarkup;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.PasswordTextField;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.form.validation.EqualPasswordInputValidator;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.validation.validator.EmailAddressValidator;
import org.apache.wicket.validation.validator.StringValidator;

import au.org.theark.core.vo.ArkUserVO;
import au.org.theark.study.web.Constants;


@SuppressWarnings("serial")
public class UserForm extends Form<ArkUserVO>{
	
	protected TextField<String> userNameTxtField  =new TextField<String>(Constants.USER_NAME);
	protected TextField<String> firstNameTxtField = new TextField<String>(Constants.FIRST_NAME);
	protected TextField<String> lastNameTxtField = new TextField<String>(Constants.LAST_NAME);
	protected TextField<String> emailTxtField = new TextField<String>(Constants.EMAIL);
	protected TextField<String> phoneNumberTxtField = new TextField<String>(Constants.PHONE_NUMBER);
	protected PasswordTextField userPasswordField = new PasswordTextField(Constants.PASSWORD);
	protected PasswordTextField confirmPasswordField = new PasswordTextField(Constants.CONFIRM_PASSWORD);
	protected PasswordTextField oldPasswordField = new PasswordTextField(Constants.OLD_PASSWORD);
	protected WebMarkupContainer groupPasswordContainer = new WebMarkupContainer("groupPasswordContainer");

	protected Button deleteButton;

	public Button getDeleteButton() {
		return deleteButton;
	}

	public void setDeleteButton(Button deleteButton) {
		this.deleteButton = deleteButton;
	}

	protected WebMarkupContainerWithAssociatedMarkup appRoleAcoAccordion  = new WebMarkupContainerWithAssociatedMarkup("appRoleAccordion");

	public WebMarkupContainerWithAssociatedMarkup getAppRoleAcoAccordion() {
		return appRoleAcoAccordion;
	}

	public void setAppRoleAcoAccordion(
			WebMarkupContainerWithAssociatedMarkup appRoleAcoAccordion) {
		this.appRoleAcoAccordion = appRoleAcoAccordion;
	}


	public WebMarkupContainer getGroupPasswordContainer() {
		return groupPasswordContainer;
	}


	public TextField<String> getUserNameTxtField() {
		return userNameTxtField;
	}
	
	public PasswordTextField getOldPasswordField() {
		return oldPasswordField;
	}

	public TextField<String> getFirstNameTxtField() {
		return firstNameTxtField;
	}
	public TextField<String> getLastNameTxtField() {
		return lastNameTxtField;
	}
	public TextField<String> getEmailTxtField() {
		return emailTxtField;
	}
	public TextField<String> getPhoneNumberTxtField() {
		return phoneNumberTxtField;
	}
	public PasswordTextField getUserPasswordField() {
		return userPasswordField;
	}
	public PasswordTextField getConfirmPasswordField() {
		return confirmPasswordField;
	}
	
	private int mode = 3;
	
	public int getMode() {
		return mode;
	}

	
	protected  void onSave(ArkUserVO userVO){}
	protected  void onCancel(){}
	protected void  onDelete(ArkUserVO etaUserVO){}
		
	private void initFormFields(){
		userNameTxtField.setRequired(true);
		userNameTxtField.add(EmailAddressValidator.getInstance());
		firstNameTxtField.setRequired(true);
		emailTxtField.setRequired(true);
		emailTxtField.add(EmailAddressValidator.getInstance());
		firstNameTxtField.add(StringValidator.lengthBetween(3, 50));
		lastNameTxtField.setRequired(true);
		lastNameTxtField.add(StringValidator.lengthBetween(3, 50));
		userNameTxtField.add(StringValidator.lengthBetween(3, 50));
		userPasswordField.setRequired(false);
		confirmPasswordField.setRequired(false);
	}
	public UserForm(String id, ArkUserVO userVO) {
			
		super(id, new CompoundPropertyModel<ArkUserVO>(userVO));
		
		initFormFields();
		
		if(StringUtils.hasText(userPasswordField.getDefaultModelObjectAsString()) && 
		   StringUtils.hasText(confirmPasswordField.getDefaultModelObjectAsString())){
			add( new EqualPasswordInputValidator(userPasswordField, confirmPasswordField));
		}
		
		add(userNameTxtField);
		add(firstNameTxtField);
		add(lastNameTxtField);
		add(emailTxtField);
		groupPasswordContainer.add(userPasswordField);
		groupPasswordContainer.add(confirmPasswordField);
		//groupPasswordContainer.add(oldPasswordField); 
		
		add(groupPasswordContainer);
		
		
		Button saveButton = new Button(Constants.SAVE, new StringResourceModel("saveKey", this, null))
		{
			public void onSubmit()
			{
				onSave((ArkUserVO) getForm().getModelObject());
			}
		}; 
		
	
		Button cancelButton = new Button(Constants.CANCEL,  new StringResourceModel("cancelKey", this, null))
		{
			public void onSubmit()
			{
				//Go to Search users page
				onCancel();
			}
			
		};
		
		deleteButton = new Button(Constants.DELETE, new StringResourceModel("deleteKey", this, null))
		{
			public void onSubmit()
			{
				//Go to Search users page
				onDelete((ArkUserVO) getForm().getModelObject());
			}
			
		};
	
		add(cancelButton.setDefaultFormProcessing(false));
		add(saveButton);
		add(deleteButton);
		
	}

}
