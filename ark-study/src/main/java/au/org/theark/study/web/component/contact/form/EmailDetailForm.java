package au.org.theark.study.web.component.contact.form;

import java.util.List;

import org.apache.shiro.SecurityUtils;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.validation.validator.EmailAddressValidator;

import au.org.theark.core.exception.ArkSystemException;
import au.org.theark.core.exception.EntityNotFoundException;
import au.org.theark.core.model.study.entity.EmailAccountType;
import au.org.theark.core.model.study.entity.EmailStatus;
import au.org.theark.core.model.study.entity.Person;
import au.org.theark.core.model.study.entity.PhoneStatus;
import au.org.theark.core.model.study.entity.State;
import au.org.theark.core.model.study.entity.Study;
import au.org.theark.core.service.IArkCommonService;
import au.org.theark.core.validator.EmailValidator;
import au.org.theark.core.vo.ArkCrudContainerVO;
import au.org.theark.core.vo.ContactVO;
import au.org.theark.core.web.component.audit.button.HistoryButtonPanel;
import au.org.theark.core.web.form.AbstractDetailForm;
import au.org.theark.study.service.IStudyService;
import au.org.theark.study.web.Constants;

public class EmailDetailForm extends AbstractDetailForm<ContactVO> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private TextField<String>				 emailIdTxtFld;
	private TextField<String>				 emailTxtFld;
	private DropDownChoice<EmailAccountType> emailTypeChoice;
	private DropDownChoice<EmailStatus>		 emailStatusChoice;
	private CheckBox						 preferredEmailChkBox;
	
	private FeedbackPanel 					feedBackPanel;
	private ArkCrudContainerVO 				arkCrudContainerVO;
	private HistoryButtonPanel              historyButtonPanel;
	
	@SuppressWarnings("unchecked")
	@SpringBean(name = au.org.theark.core.Constants.ARK_COMMON_SERVICE)
	private IArkCommonService					iArkCommonService;

	@SpringBean(name = Constants.STUDY_SERVICE)
	private IStudyService						iStudyService;
	
	public EmailDetailForm(String id, FeedbackPanel feedBackPanel, ArkCrudContainerVO arkCrudContainerVO, ContainerForm containerForm) {
		super(id, feedBackPanel, containerForm, arkCrudContainerVO);
		this.arkCrudContainerVO=arkCrudContainerVO;
		this.feedBackPanel = feedBackPanel;
	}
	
	@Override
	public void onBeforeRender() {
		// Disable preferred phone for new phone and if no others exist
		boolean enabled = !(isNew() && containerForm.getModelObject().getEmailAccountVo().getEmailAccountList().size() == 0);
		preferredEmailChkBox.setEnabled(enabled);
		historyButtonPanel.setVisible(!isNew());
		this.containerForm.getModelObject().setObjectId(containerForm.getModelObject().getEmailAccountVo().getArkVoName());
		super.onBeforeRender();
	}
	
	public void initialiseDetailForm() {
		this.emailIdTxtFld = new TextField<String>("emailAccountVo.emailAccount.id");
		this.emailTxtFld = new TextField<String>("emailAccountVo.emailAccount.name");
		this.preferredEmailChkBox = new CheckBox("emailAccountVo.emailAccount.primaryAccount");
		
		List<EmailAccountType> emailAccountTypeList = iArkCommonService.getEmailAccountTypes();
		ChoiceRenderer<EmailAccountType> emailAccountTypeRenderer = new ChoiceRenderer<EmailAccountType>(Constants.NAME, Constants.ID);
		this.emailTypeChoice = new DropDownChoice<EmailAccountType>("emailAccountVo.emailAccount.emailAccountType", emailAccountTypeList, emailAccountTypeRenderer);

		List<EmailStatus> emailStatusList = (List<EmailStatus>)iArkCommonService.getAllEmailStatuses();
		ChoiceRenderer<EmailStatus> emailStatusRenderer = new ChoiceRenderer<EmailStatus>(Constants.NAME, Constants.ID);
		this.emailStatusChoice = new DropDownChoice<EmailStatus>("emailAccountVo.emailAccount.emailStatus", emailStatusList, emailStatusRenderer);
		
		historyButtonPanel = new HistoryButtonPanel(containerForm, arkCrudContainerVO.getEditButtonContainer(), arkCrudContainerVO.getDetailPanelFormContainer(),feedBackPanel);

		addDetailFormComponents();
		
		attachValidators();
	}

	@Override
	protected void attachValidators() {
		// TODO Auto-generated method stub
		emailTxtFld.setRequired(true).setLabel(new StringResourceModel("emailAccount.name.RequiredValidator", this, new Model<String>("email")));
		emailTxtFld.add(EmailAddressValidator.getInstance());
		emailTypeChoice.setRequired(true).setLabel(new StringResourceModel("emailAccount.accountType.RequiredValidator", this, new Model<String>("emailType")));
		emailStatusChoice.setRequired(true).setLabel(new StringResourceModel("emailAccount.status.RequiredValidator", this, new Model<String>("emailStatusl")));
	}

	@Override
	protected void onCancel(AjaxRequestTarget target) {
		ContactVO contactVO = new ContactVO();
		containerForm.setModelObject(contactVO);

	}

	@Override
	protected void onSave(Form<ContactVO> containerForm, AjaxRequestTarget target) {
		Long personSessionId = (Long) SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.core.Constants.PERSON_CONTEXT_ID);
		// Get the person and set it on the AddressVO.
		try {
			Person person = iStudyService.getPerson(personSessionId);
			
			containerForm.getModelObject().getEmailAccountVo().getEmailAccount().setPerson(person);
			if (containerForm.getModelObject().getEmailAccountVo().getEmailAccount().getId() == null) {
				iStudyService.create(containerForm.getModelObject().getEmailAccountVo().getEmailAccount());
				this.saveInformation();
			}
			else {
				
				iStudyService.update(containerForm.getModelObject().getEmailAccountVo().getEmailAccount());
				this.updateInformation();
			}

			
			processErrors(target);
			onSavePostProcess(target);
			// Invoke backend to persist the AddressVO
		}
		catch (EntityNotFoundException e) {
			this.error("The specified subject is not available any more in the system. Please re-do the operation.");
		}
		catch (ArkSystemException e) {
			this.error("A system error has occurred. Please contact the system administrator.");
		}
		
	}

	@Override
	protected void onDeleteConfirmed(AjaxRequestTarget target, String selection) {
		try {
			iStudyService.delete(containerForm.getModelObject().getEmailAccountVo().getEmailAccount());
			this.deleteInformation();
			//this.info("The Address has been deleted successfully.");
			editCancelProcess(target);
		}
		catch (ArkSystemException e) {
			this.error("An error occured while processing your delete request. Please contact the system administrator.");
			// TODO Need to work out more on how user will contact support (Level 1..etc) a generic message with contact info plus logs to be emailed to
			// admin
			e.printStackTrace();
		}
		onCancel(target);
		
	}

	@Override
	protected void processErrors(AjaxRequestTarget target) {
		target.add(feedBackPanel);
		
	}

	@Override
	protected boolean isNew() {
		if (containerForm.getModelObject().getEmailAccountVo().getEmailAccount().getId() == null) {
			return true;
		}
		else {
			return false;
		}
	}

	@Override
	protected void addDetailFormComponents() {
		arkCrudContainerVO.getDetailPanelFormContainer().add(emailIdTxtFld.setEnabled(false));
		arkCrudContainerVO.getDetailPanelFormContainer().add(emailTxtFld);
		arkCrudContainerVO.getDetailPanelFormContainer().add(emailTypeChoice);
		arkCrudContainerVO.getDetailPanelFormContainer().add(emailStatusChoice);
		arkCrudContainerVO.getDetailPanelFormContainer().add(preferredEmailChkBox);
		
		arkCrudContainerVO.getEditButtonContainer().add(historyButtonPanel);
		
	}

}
