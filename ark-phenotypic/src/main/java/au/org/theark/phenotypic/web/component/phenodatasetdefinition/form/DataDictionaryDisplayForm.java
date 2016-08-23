package au.org.theark.phenotypic.web.component.phenodatasetdefinition.form;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.ThreadContext;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import au.org.theark.core.Constants;
import au.org.theark.core.exception.ArkSystemException;
import au.org.theark.core.model.study.entity.CustomField;
import au.org.theark.core.security.ArkPermissionHelper;
import au.org.theark.core.service.IArkCommonService;
import au.org.theark.core.vo.CustomFieldGroupVO;

/**
 * @author nivedann
 * 
 */
public class DataDictionaryDisplayForm extends Form<CustomFieldGroupVO> {


	private static final long										serialVersionUID	= 1L;
//	private TextField<String>										requiredMessageTxtFld;
	private TextField<String>										customFieldNameTxtFld;
	private TextField<String>										customFieldDisplayIdTxtFld;
	private CheckBox												requiredFieldCb;
	private CheckBox												allowMultiselectCb;
	protected CompoundPropertyModel<CustomFieldGroupVO>	cpModel;
	protected AjaxButton												saveButton;
	protected AjaxButton												cancelButton;
	protected FeedbackPanel											feedbackPanel;
	protected ModalWindow											modalWindow;
	protected Label 												multiSelectLabel;
	private Boolean													flag;
	protected WebMarkupContainer									cfdMarkupContainer;
	
	@SuppressWarnings("unchecked")
	@SpringBean(name = au.org.theark.core.Constants.ARK_COMMON_SERVICE)
	private IArkCommonService										iArkCommonService;

	/**
	 * @param id
	 */
	public DataDictionaryDisplayForm(String id, CompoundPropertyModel<CustomFieldGroupVO> cpModel, ModalWindow modalWindow, FeedbackPanel feedbackPanel, Boolean flag) {
		super(id, cpModel);

		// this.cpModel = cpModel;
		this.feedbackPanel = feedbackPanel;
		this.modalWindow = modalWindow;
		this.flag = flag;
		setMultiPart(true);
		initialiseForm();
		addSearchComponentsToForm();
	}

	public void onBeforeRender() {
		super.onBeforeRender();
		SecurityManager securityManager = ThreadContext.getSecurityManager();
		Subject currentUser = SecurityUtils.getSubject();
		if (ArkPermissionHelper.hasEditPermission(securityManager, currentUser) || // User can UPDATE
				ArkPermissionHelper.hasNewPermission(securityManager, currentUser) || // User can CREATE
				ArkPermissionHelper.hasDeletePermission(securityManager, currentUser)) { // User can DELETE

			// If the logged in user has Create,Update Or Delete then by-pass the View/Read Only Screen and show the Edit Screen
			saveButton.setEnabled(true);
			cfdMarkupContainer.setEnabled(true);

		}
		else {
			cfdMarkupContainer.setEnabled(false);
			saveButton.setEnabled(false);
		}
	}

	public void initialiseForm() {
		cfdMarkupContainer = new WebMarkupContainer("cfdFieldContainer");
		customFieldDisplayIdTxtFld = new TextField<String>("customFieldDisplay.id");
		customFieldDisplayIdTxtFld.setEnabled(false);
		customFieldNameTxtFld = new TextField<String>("customFieldDisplay.customField.name");
		requiredFieldCb = new CheckBox("customFieldDisplay.required");
		
		multiSelectLabel = new Label("multiSelectLabel"){
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public boolean isVisible() {
				return isMultiSelectVisible();
			}
		};
		multiSelectLabel.setOutputMarkupId(true);
		
		
		allowMultiselectCb = new CheckBox("customFieldDisplay.allowMultiselect") {
			/**
			 * 
			 */
			private static final long	serialVersionUID	= 1L;

			//ARK-861 Hide the visibility rather than disable
//			@Override
//			public boolean isEnabled() {
//				CustomField cf = CustomFieldDisplayForm.this.getModelObject().getCustomFieldDisplay().getCustomField();
//				return (cf.getEncodedValues() !=null && !cf.getEncodedValues().isEmpty());
//			}
			
			@Override
			public boolean isVisible() {
				return isMultiSelectVisible();
			}
		};
		
		saveButton = new AjaxButton(Constants.SAVE) {

			private static final long	serialVersionUID	= 1L;

			public void onSubmit(AjaxRequestTarget target, Form<?> form) {
				// Save
				try {
					onSave(target);
					this.info("Successfully updated Custom Field Display Details. ");
					target.add(feedbackPanel);
				}
				catch (ArkSystemException e) {
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

		cancelButton = new AjaxButton(Constants.CANCEL) {

			private static final long	serialVersionUID	= 1L;

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
	
	protected final boolean isMultiSelectVisible(){
		CustomField cf = DataDictionaryDisplayForm.this.getModelObject().getCustomFieldDisplay().getCustomField();
		return (cf.getEncodedValues() !=null && !cf.getEncodedValues().isEmpty());
	}

	protected void onSave(AjaxRequestTarget target) throws ArkSystemException {
		iArkCommonService.updateCustomFieldDisplay(getModelObject().getCustomFieldDisplay());
	}

	protected void addSearchComponentsToForm() {
		if (flag) {
			cfdMarkupContainer.setEnabled(false);
		}
		cfdMarkupContainer.add(customFieldDisplayIdTxtFld);
		cfdMarkupContainer.add(customFieldNameTxtFld);
		cfdMarkupContainer.add(requiredFieldCb);
		cfdMarkupContainer.add(multiSelectLabel);
		cfdMarkupContainer.add(allowMultiselectCb);

		add(saveButton);
		add(cancelButton.setDefaultFormProcessing(false));
		add(cfdMarkupContainer);
	}
}
