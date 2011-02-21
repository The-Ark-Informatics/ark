package au.org.theark.study.web.form;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.WebMarkupContainerWithAssociatedMarkup;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.PasswordTextField;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.validation.validator.EmailAddressValidator;
import org.apache.wicket.validation.validator.StringValidator;
import org.odlabs.wiquery.ui.themes.ThemeUiHelper;

import au.org.theark.core.vo.ArkUserVO;
import au.org.theark.study.web.Constants;
import au.org.theark.study.web.component.user.form.ContainerForm;


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

	protected  void onSave(ArkUserVO userVO){}
	protected  void onCancel(){}
	protected void  onDelete(ArkUserVO etaUserVO){}
	private WebMarkupContainer  resultListContainer;
	private WebMarkupContainer  detailsContainer;
	private ContainerForm containerForm;
	private AjaxButton cancelBtn;
	private AjaxButton saveBtn;
	private AjaxButton deleteBtn;

	
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

	
	public UserForm(String id, ArkUserVO userVO) {
		super(id);
	}
	/**
	 * New Constructor
	 * @param id
	 * @param listContainer
	 * @param detailsContainer
	 * @param userContainerForm
	 */
	public UserForm(String id, WebMarkupContainer listContainer, final WebMarkupContainer detailsContainer, ContainerForm userContainerForm){
		
		super(id);
		
		this.resultListContainer = listContainer;
		this.detailsContainer = detailsContainer;
		this.containerForm = userContainerForm;
		
		cancelBtn = new AjaxButton(Constants.CANCEL,  new StringResourceModel("cancelKey", this, null))
		{

			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
				resultListContainer.setVisible(false);
				detailsContainer.setVisible(false);
				target.addComponent(detailsContainer);
				target.addComponent(resultListContainer);
				containerForm.setModelObject(new ArkUserVO());
				onCancel(target);
			}
		};
		
		saveBtn = new AjaxButton(Constants.SAVE, new StringResourceModel("saveKey", this, null))
		{

			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
				target.addComponent(detailsContainer);
				onSave(containerForm.getModelObject(), target);
			}
			
			public void onError(AjaxRequestTarget target, Form<?> form){
				processFeedback(target);
			}
		};
		
		deleteBtn = new AjaxButton(Constants.DELETE, new StringResourceModel("deleteKey", this, null))
		{
			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
				target.addComponent(detailsContainer);
				onSave(containerForm.getModelObject(), target);
			}
		};

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
		addComponents();
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
		
		add(cancelBtn.setDefaultFormProcessing(false));
		add(saveBtn);
		add(deleteBtn);
	}
	
	protected void onDelete(ArkUserVO arkUserVO, AjaxRequestTarget target){
		
	}
	
	protected void onSave(ArkUserVO arkUserVO, AjaxRequestTarget target){
		
	}
	
	protected  void onCancel(AjaxRequestTarget target){
		
	}
	protected void processFeedback(AjaxRequestTarget target){
		
	}
	


	public AjaxButton getDeleteBtn() {
		return deleteBtn;
	}

	public void setDeleteBtn(AjaxButton deleteBtn) {
		this.deleteBtn = deleteBtn;
	}
	
//	public UserForm(String id, ArkUserVO userVO) {
//			
//		super(id, new CompoundPropertyModel<ArkUserVO>(userVO));
//		
//		initFormFields();
//		
//		if(StringUtils.hasText(userPasswordField.getDefaultModelObjectAsString()) && 
//		   StringUtils.hasText(confirmPasswordField.getDefaultModelObjectAsString())){
//			add( new EqualPasswordInputValidator(userPasswordField, confirmPasswordField));
//		}
//		
//		add(userNameTxtField);
//		add(firstNameTxtField);
//		add(lastNameTxtField);
//		add(emailTxtField);
//		groupPasswordContainer.add(userPasswordField);
//		groupPasswordContainer.add(confirmPasswordField);
//		//groupPasswordContainer.add(oldPasswordField); 
//		
//		add(groupPasswordContainer);
//		
//		
//		Button saveButton = new Button(Constants.SAVE, new StringResourceModel("saveKey", this, null))
//		{
//			public void onSubmit()
//			{
//				onSave((ArkUserVO) getForm().getModelObject());
//			}
//		}; 
//		
//	
//		Button cancelButton = new Button(Constants.CANCEL,  new StringResourceModel("cancelKey", this, null))
//		{
//			public void onSubmit()
//			{
//				//Go to Search users page
//				onCancel();
//			}
//			
//		};
//		
//		deleteButton = new Button(Constants.DELETE, new StringResourceModel("deleteKey", this, null))
//		{
//			public void onSubmit()
//			{
//				//Go to Search users page
//				onDelete((ArkUserVO) getForm().getModelObject());
//			}
//			
//		};
//	
//		add(cancelButton.setDefaultFormProcessing(false));
//		add(saveButton);
//		add(deleteButton);
//		
//	}

}
