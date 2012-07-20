package au.org.theark.worktracking.web.component.researcher.form;

import java.util.List;

import org.apache.shiro.SecurityUtils;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.datetime.markup.html.basic.DateLabel;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.validation.validator.StringValidator;

import au.org.theark.core.model.worktracking.entity.BillingType;
import au.org.theark.core.model.worktracking.entity.ResearcherRole;
import au.org.theark.core.model.worktracking.entity.ResearcherStatus;
import au.org.theark.core.vo.ArkCrudContainerVO;
import au.org.theark.core.web.form.AbstractDetailForm;
import au.org.theark.worktracking.model.vo.ResearcherVo;
import au.org.theark.worktracking.service.IWorkTrackingService;
import au.org.theark.worktracking.util.Constants;

public class DetailForm extends AbstractDetailForm<ResearcherVo> {
	

	public static final long	serialVersionUID	= -8267651986631341353L;

	@SpringBean(name = Constants.WORK_TRACKING_SERVICE)
	private IWorkTrackingService iWorkTrackingService;

	private TextField<String>	resercherIdTxtFld;
	private TextField<String>	resercherInstituteTxtFld;
	private TextField<String>	resercherFirstNameTxtFld;
	private TextField<String>	resercherLastNameTxtFld;
	private TextField<String>	resercherOfficePhoneTxtFld;
	private TextField<String>	resercherMobileTxtFld;
	private TextField<String>	resercherEmailTxtFld;
	private TextField<String>	resercherFaxTxtFld;
	
	private TextField<String>	resercherAccountNumberTxtFld;
	private TextField<String>	resercherBankTxtFld;
	private TextField<String>	resercherBSBTxtFld;
	private TextField<String>	resercherAccountNameTxtFld;
	
	private TextArea<String>	resercherAddressTxtArea;
	private TextArea<String>	resercherCommentTxtArea;
	
	private DateLabel						createdDateLabel;
	
	private DropDownChoice<ResearcherStatus>		 researcherStatuses;
	private DropDownChoice<ResearcherRole>		 	 researcherRoles;
	private DropDownChoice<BillingType>		 	 	 billingTypes;
	
	private List<ResearcherStatus> 	researcherStatusList;
	private List<ResearcherRole> 	researcherRoleList;
	private List<BillingType> 		billingTypeList;
	
	private FeedbackPanel		feedBackPanel;

	/**
	 * 
	 * @param id
	 * @param feedBackPanel
	 * @param arkCrudContainerVO
	 * @param containerForm
	 */
	public DetailForm(String id, FeedbackPanel feedBackPanel, ArkCrudContainerVO arkCrudContainerVO, ContainerForm containerForm) {

		super(id, feedBackPanel, containerForm, arkCrudContainerVO);
		this.feedBackPanel = feedBackPanel;
	}

	public void onBeforeRender() {
		super.onBeforeRender();
	}

	public void initialiseDetailForm() {

		resercherIdTxtFld = new TextField<String>(Constants.RESEARCHER_ID);
		resercherIdTxtFld.setEnabled(false);
		resercherFirstNameTxtFld=new TextField<String>(Constants.RESEARCHER_FIRST_NAME);
		resercherLastNameTxtFld=new TextField<String>(Constants.RESEARCHER_LAST_NAME);
		resercherInstituteTxtFld=new TextField<String>(Constants.RESEARCHER_INSTITUTE);
		
		resercherOfficePhoneTxtFld = new TextField<String>(Constants.RESEARCHER_OFFICE_PHONE);
		resercherMobileTxtFld = new TextField<String>(Constants.RESEARCHER_MOBILE);
		resercherEmailTxtFld = new TextField<String>(Constants.RESEARCHER_EMAIL);
		resercherFaxTxtFld = new TextField<String>(Constants.RESEARCHER_FAX);
		
		resercherAccountNumberTxtFld= new TextField<String>(Constants.RESEARCHER_ACCOUNT_NUMBER);
		resercherBankTxtFld= new TextField<String>(Constants.RESEARCHER_BANK);
		resercherBSBTxtFld= new TextField<String>(Constants.RESEARCHER_BSB);
		resercherAccountNameTxtFld= new TextField<String>(Constants.RESEARCHER_ACCOUNT_NAME);
		
		resercherAddressTxtArea = new  TextArea<String>(Constants.RESEARCHER_ADDRESS);
		resercherCommentTxtArea = new  TextArea<String>(Constants.RESEARCHER_COMMENT);
		
		createdDateLabel = DateLabel.forDatePattern(Constants.RESEARCHER_CREATED_DATE, au.org.theark.core.Constants.DD_MM_YYYY);	
		
		this.initResearcherStatusDropDown();
		this.initResearcherRoleDropDown();
		this.initBillingTypeDropDown();
		
		addDetailFormComponents();
		attachValidators();
	}
	
	
	private void initResearcherStatusDropDown() {
		this.researcherStatusList=this.iWorkTrackingService.getResearcherStatuses();
		ChoiceRenderer defaultChoiceRenderer = new ChoiceRenderer(Constants.NAME, Constants.ID);
		researcherStatuses = new DropDownChoice(Constants.RESEARCHER_STATUS,  this.researcherStatusList, defaultChoiceRenderer);
	}
	
	private void initResearcherRoleDropDown() {
		this.researcherRoleList=this.iWorkTrackingService.getResearcherRoles();
		ChoiceRenderer defaultChoiceRenderer = new ChoiceRenderer(Constants.NAME, Constants.ID);
		researcherRoles = new DropDownChoice(Constants.RESEARCHER_ROLE,  this.researcherRoleList, defaultChoiceRenderer);
	}
	
	private void initBillingTypeDropDown() {		
		this.billingTypeList=this.iWorkTrackingService.getResearcherBillingTypes();
		ChoiceRenderer defaultChoiceRenderer = new ChoiceRenderer(Constants.NAME, Constants.ID);
		this.billingTypes=new DropDownChoice(Constants.RESEARCHER_BILLING_TYPE,  this.billingTypeList, defaultChoiceRenderer);
	}

	public void addDetailFormComponents() {
		
		arkCrudContainerVO.getDetailPanelFormContainer().add(resercherIdTxtFld);
		arkCrudContainerVO.getDetailPanelFormContainer().add(resercherInstituteTxtFld);
		arkCrudContainerVO.getDetailPanelFormContainer().add(resercherFirstNameTxtFld);
		arkCrudContainerVO.getDetailPanelFormContainer().add(resercherLastNameTxtFld);
		arkCrudContainerVO.getDetailPanelFormContainer().add(resercherOfficePhoneTxtFld);
		arkCrudContainerVO.getDetailPanelFormContainer().add(resercherMobileTxtFld);
		arkCrudContainerVO.getDetailPanelFormContainer().add(resercherEmailTxtFld);
		arkCrudContainerVO.getDetailPanelFormContainer().add(resercherFaxTxtFld);
		arkCrudContainerVO.getDetailPanelFormContainer().add(resercherAddressTxtArea);
		arkCrudContainerVO.getDetailPanelFormContainer().add(resercherCommentTxtArea);
		arkCrudContainerVO.getDetailPanelFormContainer().add(createdDateLabel);
		arkCrudContainerVO.getDetailPanelFormContainer().add(researcherRoles);
		arkCrudContainerVO.getDetailPanelFormContainer().add(researcherStatuses);
		arkCrudContainerVO.getDetailPanelFormContainer().add(resercherAccountNumberTxtFld);
		arkCrudContainerVO.getDetailPanelFormContainer().add(billingTypes);
		arkCrudContainerVO.getDetailPanelFormContainer().add(resercherBankTxtFld);
		arkCrudContainerVO.getDetailPanelFormContainer().add(resercherBSBTxtFld);
		arkCrudContainerVO.getDetailPanelFormContainer().add(resercherAccountNameTxtFld);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see au.org.theark.core.web.form.AbstractDetailForm#attachValidators()
	 */
	@Override
	protected void attachValidators() {
		
		resercherFirstNameTxtFld.setRequired(true).setLabel(new StringResourceModel(Constants.ERROR_WORK_RESEARCHER_FIRSTNAME_REQUIRED, resercherFirstNameTxtFld, new Model<String>(Constants.RESEARCHER_FIRST_NAME_TAG)));
		
		resercherFirstNameTxtFld.add(StringValidator.lengthBetween(1, 30)).setLabel(
				new StringResourceModel(Constants.ERROR_WORK_RESEARCHER_FIRSTNAME_LENGTH, resercherFirstNameTxtFld, new Model<String>(Constants.RESEARCHER_FIRST_NAME_TAG)));
		resercherLastNameTxtFld.setRequired(true).setLabel(new StringResourceModel(Constants.ERROR_WORK_RESEARCHER_LASTNAME_REQUIRED, resercherLastNameTxtFld, new Model<String>(Constants.RESEARCHER_LAST_NAME_TAG)));
		
		resercherLastNameTxtFld.add(StringValidator.lengthBetween(1, 45)).setLabel(
				new StringResourceModel(Constants.ERROR_WORK_RESEARCHER_LASTNAME_LENGTH, resercherLastNameTxtFld, new Model<String>(Constants.RESEARCHER_LAST_NAME_TAG)));
		
		resercherInstituteTxtFld.setRequired(true).setLabel(new StringResourceModel(Constants.ERROR_WORK_RESEARCHER_INSTITUTE_REQUIRED, resercherInstituteTxtFld, new Model<String>(Constants.RESEARCHER_INSTITUTE_TAG)));
		
		resercherInstituteTxtFld.add(StringValidator.lengthBetween(1, 50)).setLabel(
				new StringResourceModel(Constants.ERROR_WORK_RESEARCHER_INSTITUTE_LENGTH, resercherInstituteTxtFld, new Model<String>(Constants.RESEARCHER_INSTITUTE_TAG)));
		
		resercherAddressTxtArea.setRequired(true).setLabel(new StringResourceModel(Constants.ERROR_WORK_RESEARCHER_ADDRESS_REQUIRED, resercherAddressTxtArea, new Model<String>(Constants.RESEARCHER_ADDRESS_TAG)));
		
		resercherAddressTxtArea.add(StringValidator.lengthBetween(1, 255)).setLabel(
				new StringResourceModel(Constants.ERROR_WORK_RESEARCHER_ADDRESS_LENGTH, resercherAddressTxtArea, new Model<String>(Constants.RESEARCHER_ADDRESS_TAG)));	
		
		researcherRoles.setRequired(true).setLabel(new StringResourceModel(Constants.ERROR_WORK_RESEARCHER_ROLE_REQUIRED, researcherRoles, new Model<String>(Constants.RESEARCHER_ROLE_TAG)));
		
		researcherStatuses.setRequired(true).setLabel(new StringResourceModel(Constants.ERROR_WORK_RESEARCHER_STATUS_REQUIRED, researcherStatuses, new Model<String>(Constants.RESEARCHER_STATUS_TAG)));
		
		resercherOfficePhoneTxtFld.add(StringValidator.lengthBetween(1, 12)).setLabel(
				new StringResourceModel(Constants.ERROR_WORK_RESEARCHER_OFFICEPHONE_LENGTH, resercherOfficePhoneTxtFld, new Model<String>(Constants.RESEARCHER_OFFICE_PHONE_TAG)));
		
		resercherMobileTxtFld.add(StringValidator.lengthBetween(1, 12)).setLabel(
				new StringResourceModel(Constants.ERROR_WORK_RESEARCHER_MOBILEPHONE_LENGTH, resercherMobileTxtFld, new Model<String>(Constants.RESEARCHER_MOBILE_PHONE_TAG)));
		
		resercherFaxTxtFld.add(StringValidator.lengthBetween(1, 12)).setLabel(
				new StringResourceModel(Constants.ERROR_WORK_RESEARCHER_FAX_LENGTH, resercherFaxTxtFld, new Model<String>(Constants.RESEARCHER_FAX_TAG)));
		
		resercherEmailTxtFld.add(StringValidator.lengthBetween(1, 45)).setLabel(
				new StringResourceModel(Constants.ERROR_WORK_RESEARCHER_EMAIL_LENGTH, resercherEmailTxtFld, new Model<String>(Constants.RESEARCHER_EMAIL_TAG)));
		
		resercherCommentTxtArea.add(StringValidator.lengthBetween(1, 255)).setLabel(
				new StringResourceModel(Constants.ERROR_WORK_RESEARCHER_COMMENT_LENGTH, resercherCommentTxtArea, new Model<String>(Constants.RESEARCHER_COMMENT_TAG)));
		
		resercherAccountNumberTxtFld.add(StringValidator.lengthBetween(1, 30)).setLabel(
				new StringResourceModel(Constants.ERROR_WORK_RESEARCHER_ACCOUNTNUMBER_LENGTH, resercherAccountNumberTxtFld, new Model<String>(Constants.RESEARCHER_ACCOUNT_NUMBER_TAG)));
		
		resercherBSBTxtFld.add(StringValidator.lengthBetween(1, 8)).setLabel(
				new StringResourceModel(Constants.ERROR_WORK_RESEARCHER_BSB_LENGTH, resercherBSBTxtFld, new Model<String>(Constants.RESEARCHER_BSB_TAG)));
		
		resercherBankTxtFld.add(StringValidator.lengthBetween(1, 50)).setLabel(
				new StringResourceModel(Constants.ERROR_WORK_RESEARCHER_BANK_LENGTH, resercherBankTxtFld, new Model<String>(Constants.RESEARCHER_BANK_TAG)));
		
		resercherAccountNameTxtFld.add(StringValidator.lengthBetween(1, 50)).setLabel(
				new StringResourceModel(Constants.ERROR_WORK_RESEARCHER_ACCOUNTNAME_LENGTH, resercherAccountNameTxtFld, new Model<String>(Constants.RESEARCHER_ACCOUNT_NAME_TAG)));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see au.org.theark.core.web.form.AbstractDetailForm#onCancel(org.apache.wicket.ajax.AjaxRequestTarget)
	 */
	@Override
	protected void onCancel(AjaxRequestTarget target) {
		ResearcherVo researcherVo = new ResearcherVo();
		containerForm.setModelObject(researcherVo);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see au.org.theark.core.web.form.AbstractDetailForm#onSave(org.apache.wicket.markup.html.form.Form, org.apache.wicket.ajax.AjaxRequestTarget)
	 */
	@Override
	protected void onSave(Form<ResearcherVo> containerForm, AjaxRequestTarget target) {

		target.add(arkCrudContainerVO.getDetailPanelContainer());
		try {
			
			if (containerForm.getModelObject().getResearcher().getId() == null) {
				
				Long studyId = (Long) SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.core.Constants.STUDY_CONTEXT_ID);
				containerForm.getModelObject().getResearcher().setStudyId(studyId);
				iWorkTrackingService.createResearcher(containerForm.getModelObject().getResearcher());
				this.info("Researcher " + containerForm.getModelObject().getResearcher().getFirstName() + " " + containerForm.getModelObject().getResearcher().getLastName() + " was created successfully");
				processErrors(target);
			}
			else {

				iWorkTrackingService.updateResearcher(containerForm.getModelObject().getResearcher());
				this.info("Researcher " + containerForm.getModelObject().getResearcher().getFirstName() + " " + containerForm.getModelObject().getResearcher().getLastName() + " was updated successfully");
				processErrors(target);
			}

			onSavePostProcess(target);

		}
		catch (Exception e) {
			this.error("A System error occured, we will have someone contact you.");
			processErrors(target);
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see au.org.theark.core.web.form.AbstractDetailForm#processErrors(org.apache.wicket.ajax.AjaxRequestTarget)
	 */
	@Override
	protected void processErrors(AjaxRequestTarget target) {
		target.add(feedBackPanel);

	}

	protected void onDeleteConfirmed(AjaxRequestTarget target, String selection) {
		try {
			Long count = iWorkTrackingService.getWorkRequestCount(containerForm.getModelObject().getResearcher());
			if(count == 0){
				
				iWorkTrackingService.deleteResearcher(containerForm.getModelObject().getResearcher());
				ResearcherVo researcherVo = new ResearcherVo();
				containerForm.setModelObject(researcherVo);
				containerForm.info("The Researcher was deleted successfully.");
				editCancelProcess(target);
			}else{
				containerForm.error("Cannot Delete this Researcher Component. This researcher is associated with existing Work Requests ");
				processErrors(target);
			}
		}
		catch (Exception e) {
			containerForm.error("A System Error has occured please contact support.");
			processErrors(target);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see au.org.theark.core.web.form.AbstractDetailForm#isNew()
	 */
	@Override
	protected boolean isNew() {
		if (containerForm.getModelObject().getResearcher().getId() == null) {
			return true;
		}
		else {
			return false;
		}

	}

}