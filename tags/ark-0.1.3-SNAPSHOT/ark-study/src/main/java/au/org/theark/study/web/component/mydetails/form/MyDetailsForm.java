package au.org.theark.study.web.component.mydetails.form;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.PasswordTextField;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.form.validation.EqualPasswordInputValidator;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.validation.validator.EmailAddressValidator;
import org.apache.wicket.validation.validator.PatternValidator;
import org.apache.wicket.validation.validator.StringValidator;

import au.org.theark.core.vo.ArkUserVO;
import au.org.theark.core.web.form.ArkFormVisitor;
import au.org.theark.study.service.IUserService;
import au.org.theark.study.web.Constants;

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
	private FeedbackPanel feedbackPanel;

	protected void processFeedback(AjaxRequestTarget target,FeedbackPanel fb){
		
	}
	
	protected void onSave( AjaxRequestTarget target){
		
	}

	protected void onCancel( AjaxRequestTarget target){
		
	}

	public TextField<String> getUserNameTxtField() {
		return userNameTxtField;
	}
	
	// Add a visitor class for required field marking/validation/highlighting
	ArkFormVisitor formVisitor = new ArkFormVisitor();
	public void onBeforeRender()
	{
		super.onBeforeRender();
		visitChildren(formVisitor);
	}
	
	public void initialiseForm(){
		
		
		emailTxtField.add(EmailAddressValidator.getInstance());
		attachValidators();
		addComponents();
	}
	
	
	private void attachValidators(){
		
		userNameTxtField.add(EmailAddressValidator.getInstance()).setLabel(new StringResourceModel("userName.incorrect.format", this, null));
		userNameTxtField.setRequired(true).setLabel(new StringResourceModel("userName", this, null));
		userNameTxtField.add(StringValidator.lengthBetween(3, 50)).setLabel(new StringResourceModel("userName", this, null));
		
		firstNameTxtField.setRequired(true).setLabel(new StringResourceModel("firstName", this, null));
		firstNameTxtField.add(StringValidator.lengthBetween(3, 50)).setLabel(new StringResourceModel("firstName", this, null));
		
		emailTxtField.add(EmailAddressValidator.getInstance()).setLabel(new StringResourceModel("email.incorrect.format", this, null));
		emailTxtField.setRequired(true).setLabel(new StringResourceModel("email", this, null));
		
		lastNameTxtField.add(StringValidator.lengthBetween(3, 50)).setLabel(new StringResourceModel("lastNameLength", this, null));
		lastNameTxtField.setRequired(true).setLabel(new StringResourceModel("lastName", this, null));
		
		userPasswordField.setRequired(false);
		confirmPasswordField.setRequired(false);
		
		userPasswordField.setLabel(Model.of("Password")); 
		userPasswordField.add(new PatternValidator(Constants.PASSWORD_PATTERN));
		confirmPasswordField.setLabel(Model.of("Confirm Password")); 
		
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
		add(new EqualPasswordInputValidator(userPasswordField, confirmPasswordField));
		add(saveBtn);
	}
	
	
	public MyDetailsForm(String id, CompoundPropertyModel<ArkUserVO> model,final  FeedbackPanel feedbackPanel) {
		super(id, model);
		this.feedbackPanel = feedbackPanel;
		// TODO Auto-generated constructor stub
		saveBtn = new AjaxButton(Constants.SAVE, new StringResourceModel("saveKey", this, null))
		{

			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
				onSave(target);
			}
			
			public void onError(AjaxRequestTarget target, Form<?> form){
				processFeedback(target,feedbackPanel);
			}
		};
	}
	
//	public MyDetailsForm(String id, CompoundPropertyModel<ArkUserVO> model, FeedbackPanel feedbackPanel) {
//		super(id, model);
//		// TODO Auto-generated constructor stub
//		saveBtn = new AjaxButton(Constants.SAVE, new StringResourceModel("saveKey", this, null))
//		{
//
//			@Override
//			protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
//				onSave(target);
//			}
//			
//			public void onError(AjaxRequestTarget target, Form<?> form){
//				
//			}
//		};
//	}

}
