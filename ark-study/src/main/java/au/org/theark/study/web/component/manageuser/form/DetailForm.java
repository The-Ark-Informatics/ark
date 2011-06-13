package au.org.theark.study.web.component.manageuser.form;

import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.PasswordTextField;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.validation.validator.EmailAddressValidator;
import org.apache.wicket.validation.validator.StringValidator;

import au.org.theark.core.exception.ArkSystemException;
import au.org.theark.core.exception.UserNameExistsException;
import au.org.theark.core.model.study.entity.ArkUserRole;
import au.org.theark.core.vo.ArkCrudContainerVO;
import au.org.theark.core.vo.ArkUserVO;
import au.org.theark.core.web.form.AbstractDetailForm;
import au.org.theark.study.service.IUserService;
import au.org.theark.study.web.Constants;

public class DetailForm extends AbstractDetailForm<ArkUserVO>{
	
	@SpringBean( name = "userService")
	private IUserService userService;
	

	
	protected TextField<String> userNameTxtField  =new TextField<String>(Constants.USER_NAME);
	protected TextField<String> firstNameTxtField = new TextField<String>(Constants.FIRST_NAME);
	protected TextField<String> lastNameTxtField = new TextField<String>(Constants.LAST_NAME);
	protected TextField<String> emailTxtField = new TextField<String>(Constants.EMAIL);
	protected PasswordTextField userPasswordField = new PasswordTextField(Constants.PASSWORD);
	protected PasswordTextField confirmPasswordField = new PasswordTextField(Constants.CONFIRM_PASSWORD);
	protected PasswordTextField oldPasswordField = new PasswordTextField(Constants.OLD_PASSWORD);
	protected WebMarkupContainer groupPasswordContainer = new WebMarkupContainer("groupPasswordContainer");
	private ArkCrudContainerVO arkCrudContainerVO;
	
	public DetailForm(String id, FeedbackPanel feedBackPanel,	ArkCrudContainerVO arkCrudContainerVO, Form<ArkUserVO> containerForm) {
		super(id, feedBackPanel, arkCrudContainerVO, containerForm);
		this.arkCrudContainerVO = arkCrudContainerVO;
		
	}

	public void initialiseDetailForm(){
		userNameTxtField  =new TextField<String>(Constants.USER_NAME);
		firstNameTxtField = new TextField<String>(Constants.FIRST_NAME);
		lastNameTxtField = new TextField<String>(Constants.LAST_NAME);
		emailTxtField = new TextField<String>(Constants.EMAIL);
		userPasswordField = new PasswordTextField(Constants.PASSWORD);
		confirmPasswordField = new PasswordTextField(Constants.CONFIRM_PASSWORD);
		oldPasswordField = new PasswordTextField(Constants.OLD_PASSWORD);
		groupPasswordContainer = new WebMarkupContainer("groupPasswordContainer");
		attachValidators();
		addDetailFormComponents();
	}
	
	private void addDetailFormComponents(){
		
		arkCrudContainerVO.getDetailPanelFormContainer().add(userNameTxtField);
		arkCrudContainerVO.getDetailPanelFormContainer().add(firstNameTxtField);
		arkCrudContainerVO.getDetailPanelFormContainer().add(lastNameTxtField);
		arkCrudContainerVO.getDetailPanelFormContainer().add(emailTxtField);
		
		
		//We use this markup to hide unhide the password fields during edit. i.e. if the user selects edit password then make it visible/enabled.
		
		groupPasswordContainer.add(userPasswordField);
		groupPasswordContainer.add(confirmPasswordField);
		arkCrudContainerVO.getDetailPanelFormContainer().add(groupPasswordContainer);
		add(arkCrudContainerVO.getDetailPanelFormContainer());
	}
	
	@Override
	protected void attachValidators() {

		lastNameTxtField.add(StringValidator.lengthBetween(1, 50)).setLabel(new StringResourceModel("lastName",this,null));
		lastNameTxtField.setRequired(true);

		userNameTxtField.add(StringValidator.lengthBetween(1, 50)).setLabel(new StringResourceModel("userName",this,null));
		userNameTxtField.add(EmailAddressValidator.getInstance()).setLabel(new StringResourceModel("userName.incorrectPattern",this,null));
		userNameTxtField.setRequired(true).setLabel(new StringResourceModel("userName",this,null));
		
		firstNameTxtField.setRequired(true).setLabel(new StringResourceModel("firstName",this,null));
		firstNameTxtField.add(StringValidator.lengthBetween(1, 50)).setLabel(new StringResourceModel("firstName",this,null));
		
		emailTxtField.add(StringValidator.lengthBetween(1, 50)).setLabel(new StringResourceModel("email",this,null));
		emailTxtField.add(EmailAddressValidator.getInstance()).setLabel(new StringResourceModel("email.incorrectpattern",this,null));
		emailTxtField.setRequired(true).setLabel(new StringResourceModel("email",this,null));
		
		userPasswordField.setRequired(false);
		confirmPasswordField.setRequired(false);
		
	}

	
	protected void onCancel(AjaxRequestTarget target) {
		
		containerForm.getModelObject().setArkUserRoleList(new ArrayList<ArkUserRole>());
		containerForm.setModelObject(new ArkUserVO());
		
	}

	@Override
	protected void onSave(Form<ArkUserVO> containerForm,AjaxRequestTarget target) {
		//Persist the user to ArkUser Group
		if(containerForm.getModelObject().getMode() == Constants.MODE_NEW){
			
			try {
				//Create the user in LDAP - Step 1 does not have Study related information
				ArkUserVO savedObject = containerForm.getModelObject();
				userService.createArkUser(containerForm.getModelObject());
				StringBuffer sb = new StringBuffer();
				sb.append("The user with Login/User Name " );
				sb.append(containerForm.getModelObject().getUserName());
				sb.append(" has been added successfully into the System.");
				containerForm.getModelObject().setMode(Constants.MODE_EDIT);
				onSavePostProcess(target,arkCrudContainerVO);
				this.info(sb.toString());
			} catch (ArkSystemException e) {
				this.error("A System error has occured. Please contact Support.");
			} catch (UserNameExistsException e) {
				this.error("The given username is already present in the Ark System. Please provide a unique username.");
			} catch (Exception e) {
				this.error("A System error has occured. Please contact Support");
			}
			target.addComponent(feedBackPanel);
		}else if(containerForm.getModelObject().getMode() == Constants.MODE_EDIT){
			
			try {
				
				userService.updateArkUser(containerForm.getModelObject());
				
				StringBuffer sb = new StringBuffer();
				sb.append("The user with Login/User Name " );
				sb.append(containerForm.getModelObject().getUserName());
				sb.append(" has been updated successfully into the System.");
				this.info(sb.toString());
				onSavePostProcess(target,arkCrudContainerVO);
			} catch (ArkSystemException e) {
				this.error("A System error has occured. Please contact Support.");
				
			}
			
		}
	}

	@Override
	protected void processErrors(AjaxRequestTarget target) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected boolean isNew() {
			if (containerForm.getModelObject().getMode() == Constants.MODE_NEW){
				return true;
			}else{
				return false;
			}
	}

	@Override
	protected void onDeleteConfirmed(AjaxRequestTarget target,	String selection, ModalWindow selectModalWindow) {
		// TODO Auto-generated method stub
		
	}
	
	

}
