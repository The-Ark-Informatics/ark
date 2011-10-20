package au.org.theark.phenotypic.web.component.customfieldgroup.form;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import au.org.theark.core.Constants;
import au.org.theark.core.exception.ArkSystemException;
import au.org.theark.core.service.IArkCommonService;
import au.org.theark.core.vo.CustomFieldGroupVO;

/**
 * @author nivedann
 *
 */
public class CustomFieldDisplayForm extends Form<CustomFieldGroupVO>{

	private TextField<String> requiredMessageTxtFld;
	private TextField<String> customFieldNameTxtFld;
	private TextField<String> customFieldDisplayIdTxtFld;
	private CheckBox requiredFieldCb;	
	protected CompoundPropertyModel<CustomFieldGroupVO> cpModel;
	protected AjaxButton saveButton;
	protected AjaxButton cancelButton;
	protected FeedbackPanel	feedbackPanel;
	protected ModalWindow	modalWindow;
	
	@SpringBean(name = au.org.theark.core.Constants.ARK_COMMON_SERVICE)
	private IArkCommonService iArkCommonService;
	
	/**
	 * @param id
	 */
	public CustomFieldDisplayForm(String id, CompoundPropertyModel<CustomFieldGroupVO> cpModel, ModalWindow	modalWindow, FeedbackPanel feedbackPanel) {
		super(id,cpModel);
		
		//this.cpModel = cpModel;
		this.feedbackPanel = feedbackPanel;
		this.modalWindow = modalWindow;
		initialiseForm();
		addSearchComponentsToForm();
	}
	
	public void initialiseForm(){
		customFieldDisplayIdTxtFld = new TextField<String>("customFieldDisplay.id");
		requiredMessageTxtFld = new TextField<String>("customFieldDisplay.requiredMessage");
		customFieldNameTxtFld = new TextField<String>("customFieldDisplay.customField.name");
		requiredFieldCb = new CheckBox("customFieldDisplay.required");
		
		saveButton = new AjaxButton(Constants.SAVE, new StringResourceModel("saveKey", this, null)) {

			public void onSubmit(AjaxRequestTarget target, Form<?> form) {
				//Save the CFD TODO
				try {
					
					onSave(target);		
					
				} catch (ArkSystemException e) {
					this.error("Could not update Custom Field Display");
					e.printStackTrace();
				}
			}

			@Override
			protected void onError(AjaxRequestTarget target, Form<?> arg1) {
				this.error("An error occurred while saving the Custom Field Display information. ");
				target.add(feedbackPanel);
			}
		};
		
		cancelButton = new AjaxButton(Constants.CANCEL, new StringResourceModel("cancelKey", this, null)) {
			/**
			 * 
			 */
			private static final long	serialVersionUID	= 1684005199059571017L;

			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
				target.add(feedbackPanel);
				modalWindow.close(target);
			}

			@Override
			protected void onError(AjaxRequestTarget target, Form<?> arg1) {
				this.error("An error occurred. Please contact administrator");
				target.add(feedbackPanel);
			}
		};
		
		customFieldNameTxtFld.setEnabled(false);
	}
	
	
	protected void onSave(AjaxRequestTarget target) throws ArkSystemException{
		iArkCommonService.updateCustomFieldDisplay(getModelObject().getCustomFieldDisplay());
	}
	protected void addSearchComponentsToForm() {
		add(customFieldDisplayIdTxtFld);
		add(customFieldNameTxtFld);
		add(requiredFieldCb);
		add(requiredMessageTxtFld);
		add(saveButton);
		add(cancelButton.setDefaultFormProcessing(false));
	}
}
