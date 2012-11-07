package au.org.theark.worktracking.web.component.researcher.form;

import java.util.Collection;
import java.util.List;

import org.apache.shiro.SecurityUtils;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
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
import org.apache.wicket.validation.IValidatable;
import org.apache.wicket.validation.validator.EmailAddressValidator;
import org.apache.wicket.validation.validator.PatternValidator;
import org.apache.wicket.validation.validator.StringValidator;

import au.org.theark.core.model.study.entity.TitleType;
import au.org.theark.core.model.worktracking.entity.BillingType;
import au.org.theark.core.model.worktracking.entity.Researcher;
import au.org.theark.core.model.worktracking.entity.ResearcherRole;
import au.org.theark.core.model.worktracking.entity.ResearcherStatus;
import au.org.theark.core.service.IArkCommonService;
import au.org.theark.core.vo.ArkCrudContainerVO;
import au.org.theark.core.web.form.AbstractDetailForm;
import au.org.theark.worktracking.model.vo.ResearcherVo;
import au.org.theark.worktracking.service.IWorkTrackingService;
import au.org.theark.worktracking.util.Constants;
import au.org.theark.worktracking.util.NumberValidatable;
import au.org.theark.worktracking.util.ValidatableItemType;

public class DetailForm extends AbstractDetailForm<ResearcherVo> {
	

	public static final long	serialVersionUID	= -8267651986631341353L;

	@SpringBean(name = Constants.WORK_TRACKING_SERVICE)
	private IWorkTrackingService iWorkTrackingService;
	
	@SpringBean(name = au.org.theark.core.Constants.ARK_COMMON_SERVICE)
	private IArkCommonService	iArkCommonService;

	private TextField<String>	resercherIdTxtFld;
	private TextField<String>	resercherOrganizationTxtFld;
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
	private DropDownChoice<TitleType>		 	 	 titleTypes;
	
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
		resercherOrganizationTxtFld=new TextField<String>(Constants.RESEARCHER_ORGANIZATION);
		
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
		this.initTitleTypeDropDown();
	
		this.setEnableBankFields(false);
		
		addDetailFormComponents();
		attachValidators();
	}
	
	
	private void initTitleTypeDropDown() {
		Collection<TitleType> titleTypeList = iArkCommonService.getTitleType();
		ChoiceRenderer defaultChoiceRenderer = new ChoiceRenderer(Constants.NAME, Constants.ID);
		this.titleTypes = new DropDownChoice(Constants.RESEARCHER_TITLE,  (List)titleTypeList, defaultChoiceRenderer);
		
	}

	private void initResearcherStatusDropDown() {
		this.researcherStatusList=this.iWorkTrackingService.getResearcherStatuses();
		ChoiceRenderer defaultChoiceRenderer = new ChoiceRenderer(Constants.NAME, Constants.ID);
		this.researcherStatuses = new DropDownChoice(Constants.RESEARCHER_STATUS,  this.researcherStatusList, defaultChoiceRenderer);
	}
	
	private void initResearcherRoleDropDown() {
		this.researcherRoleList=this.iWorkTrackingService.getResearcherRoles();
		ChoiceRenderer defaultChoiceRenderer = new ChoiceRenderer(Constants.NAME, Constants.ID);
		this.researcherRoles = new DropDownChoice(Constants.RESEARCHER_ROLE,  this.researcherRoleList, defaultChoiceRenderer);
	}
	
	private void initBillingTypeDropDown() {		
		this.billingTypeList=this.iWorkTrackingService.getResearcherBillingTypes();
		ChoiceRenderer defaultChoiceRenderer = new ChoiceRenderer(Constants.NAME, Constants.ID);
		this.billingTypes=new DropDownChoice(Constants.RESEARCHER_BILLING_TYPE,  this.billingTypeList, defaultChoiceRenderer);
		this.billingTypes.add(new AjaxFormComponentUpdatingBehavior("onChange") {

			private static final long	serialVersionUID	= 1L;

			@Override
			protected void onUpdate(AjaxRequestTarget target) {
				Researcher researcher=getFormModel().getResearcher();
				if(researcher.getBillingType() == null 
						|| 
							!"EFT".equalsIgnoreCase(researcher.getBillingType().getName())){					
					researcher.setAccountName(null);
					researcher.setAccountNumber(null);
					researcher.setBank(null);
					researcher.setBsb(null);
					setEnableBankFields(false,target);
				}
				else{
					setEnableBankFields(true,target);
				}
			}
			
			@Override
			protected void onError(AjaxRequestTarget target, RuntimeException e) {
				// TODO Auto-generated method stub
				setEnableBankFields(true,target);
			}
		});
	}

	public void addDetailFormComponents() {
		
		arkCrudContainerVO.getDetailPanelFormContainer().add(resercherIdTxtFld);
		arkCrudContainerVO.getDetailPanelFormContainer().add(resercherOrganizationTxtFld);
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
		arkCrudContainerVO.getDetailPanelFormContainer().add(resercherAccountNameTxtFld);
		arkCrudContainerVO.getDetailPanelFormContainer().add(titleTypes);
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
		
		resercherOrganizationTxtFld.setRequired(true).setLabel(new StringResourceModel(Constants.ERROR_WORK_RESEARCHER_ORGANIZATION_REQUIRED, resercherOrganizationTxtFld, new Model<String>(Constants.RESEARCHER_ORGANIZATION_TAG)));
		
		resercherOrganizationTxtFld.add(StringValidator.lengthBetween(1, 50)).setLabel(
				new StringResourceModel(Constants.ERROR_WORK_RESEARCHER_ORGANIZATION_LENGTH, resercherOrganizationTxtFld, new Model<String>(Constants.RESEARCHER_ORGANIZATION_TAG)));
		
		resercherAddressTxtArea.setRequired(true).setLabel(new StringResourceModel(Constants.ERROR_WORK_RESEARCHER_ADDRESS_REQUIRED, resercherAddressTxtArea, new Model<String>(Constants.RESEARCHER_ADDRESS_TAG)));
		
		resercherAddressTxtArea.add(StringValidator.lengthBetween(1, 255)).setLabel(
				new StringResourceModel(Constants.ERROR_WORK_RESEARCHER_ADDRESS_LENGTH, resercherAddressTxtArea, new Model<String>(Constants.RESEARCHER_ADDRESS_TAG)));	
		
		researcherRoles.setRequired(true).setLabel(new StringResourceModel(Constants.ERROR_WORK_RESEARCHER_ROLE_REQUIRED, researcherRoles, new Model<String>(Constants.RESEARCHER_ROLE_TAG)));
		
		researcherStatuses.setRequired(true).setLabel(new StringResourceModel(Constants.ERROR_WORK_RESEARCHER_STATUS_REQUIRED, researcherStatuses, new Model<String>(Constants.RESEARCHER_STATUS_TAG)));
		
		resercherOfficePhoneTxtFld.add(StringValidator.lengthBetween(1, 25)).setLabel(
				new StringResourceModel(Constants.ERROR_WORK_RESEARCHER_OFFICEPHONE_LENGTH, resercherOfficePhoneTxtFld, new Model<String>(Constants.RESEARCHER_OFFICE_PHONE_TAG)));
		resercherOfficePhoneTxtFld.add(new PatternValidator(Constants.PHONE_NUMBER_PATTERN){
			private static final long serialVersionUID = 1L;

			@Override
			protected void onValidate(IValidatable<String> validatable) {
				super.onValidate(new NumberValidatable(validatable,ValidatableItemType.PHONE_NUMBER));
			}
		});	
		
		resercherMobileTxtFld.add(StringValidator.lengthBetween(1, 25)).setLabel(
				new StringResourceModel(Constants.ERROR_WORK_RESEARCHER_MOBILEPHONE_LENGTH, resercherMobileTxtFld, new Model<String>(Constants.RESEARCHER_MOBILE_PHONE_TAG)));
		resercherMobileTxtFld.add(new PatternValidator(Constants.PHONE_NUMBER_PATTERN){
			private static final long serialVersionUID = 1L;

			@Override
			protected void onValidate(IValidatable<String> validatable) {
				super.onValidate(new NumberValidatable(validatable,ValidatableItemType.MOBILE_NUMBER));
			}
		});	
			
		resercherFaxTxtFld.add(StringValidator.lengthBetween(1, 25)).setLabel(
				new StringResourceModel(Constants.ERROR_WORK_RESEARCHER_FAX_LENGTH, resercherFaxTxtFld, new Model<String>(Constants.RESEARCHER_FAX_TAG)));
		resercherFaxTxtFld.add(new PatternValidator(Constants.PHONE_NUMBER_PATTERN){
			private static final long serialVersionUID = 1L;

			@Override
			protected void onValidate(IValidatable<String> validatable) {
				super.onValidate(new NumberValidatable(validatable,ValidatableItemType.FAX_NUMBER));
			}
		});	
		
		resercherEmailTxtFld.add(StringValidator.lengthBetween(1, 45)).setLabel(
				new StringResourceModel(Constants.ERROR_WORK_RESEARCHER_EMAIL_LENGTH, resercherEmailTxtFld, new Model<String>(Constants.RESEARCHER_EMAIL_TAG)));
		
		resercherCommentTxtArea.add(StringValidator.lengthBetween(1, 255)).setLabel(
				new StringResourceModel(Constants.ERROR_WORK_RESEARCHER_COMMENT_LENGTH, resercherCommentTxtArea, new Model<String>(Constants.RESEARCHER_COMMENT_TAG)));
		
		resercherAccountNumberTxtFld.add(StringValidator.lengthBetween(1, 30)).setLabel(
				new StringResourceModel(Constants.ERROR_WORK_RESEARCHER_ACCOUNTNUMBER_LENGTH, resercherAccountNumberTxtFld, new Model<String>(Constants.RESEARCHER_ACCOUNT_NUMBER_TAG)));
		
		resercherAccountNumberTxtFld.add(new PatternValidator(Constants.FULL_NUMBER_PATTERN){
			private static final long serialVersionUID = 1L;

			@Override
			protected void onValidate(IValidatable<String> validatable) {
				super.onValidate(new NumberValidatable(validatable,ValidatableItemType.ACCOUNT_NUMBER));
			}
		});	
		
		
		resercherBSBTxtFld.add(StringValidator.exactLength(6)).setLabel(
				new StringResourceModel(Constants.ERROR_WORK_RESEARCHER_BSB_LENGTH, resercherBSBTxtFld, new Model<String>(Constants.RESEARCHER_BSB_TAG)));
		
		resercherBSBTxtFld.add(new PatternValidator(Constants.FULL_NUMBER_PATTERN){
			private static final long serialVersionUID = 1L;

			@Override
			protected void onValidate(IValidatable<String> validatable) {
				super.onValidate(new NumberValidatable(validatable,ValidatableItemType.BSB));
			}
		});	
		
		resercherBankTxtFld.add(StringValidator.lengthBetween(1, 50)).setLabel(
				new StringResourceModel(Constants.ERROR_WORK_RESEARCHER_BANK_LENGTH, resercherBankTxtFld, new Model<String>(Constants.RESEARCHER_BANK_TAG)));
		
		resercherAccountNameTxtFld.add(StringValidator.lengthBetween(1, 50)).setLabel(
				new StringResourceModel(Constants.ERROR_WORK_RESEARCHER_ACCOUNTNAME_LENGTH, resercherAccountNameTxtFld, new Model<String>(Constants.RESEARCHER_ACCOUNT_NAME_TAG)));
		resercherEmailTxtFld.add(EmailAddressValidator.getInstance());
		titleTypes.setRequired(true).setLabel(new StringResourceModel(Constants.ERROR_WORK_RESEARCHER_TITLE_TYPE_REQUIRED, researcherStatuses, new Model<String>(Constants.RESEARCHER_TITLE_TYPE_TAG)));
		
	}
	
	/*
	@Override
	public void process(IFormSubmitter submittingComponent) {
		Researcher researcher=getFormModel().getResearcher();
		
		if(researcher.getBillingType() !=null &&
				"EFT".equalsIgnoreCase(researcher.getBillingType().getName())){
			if(!resercherAccountNameTxtFld.isRequired()){
				resercherAccountNameTxtFld.setRequired(true).setLabel(new StringResourceModel(Constants.ERROR_WORK_RESEARCHER_ACCOUNTNAME_REQUIRED, resercherAccountNameTxtFld, new Model<String>(Constants.RESEARCHER_ACCOUNT_NAME_TAG)));
			}
			if(!resercherBankTxtFld.isRequired()){
				resercherBankTxtFld.setRequired(true).setLabel(new StringResourceModel(Constants.ERROR_WORK_RESEARCHER_BANK_REQUIRED, resercherBankTxtFld, new Model<String>(Constants.RESEARCHER_BANK_TAG)));
			}
			if(!resercherBSBTxtFld.isRequired()){
				resercherBSBTxtFld.setRequired(true).setLabel(new StringResourceModel(Constants.ERROR_WORK_RESEARCHER_BSB_REQUIRED, resercherBSBTxtFld, new Model<String>(Constants.RESEARCHER_BSB_TAG)));
			}
			if(!resercherAccountNumberTxtFld.isRequired()){
				resercherAccountNumberTxtFld.setRequired(true).setLabel(new StringResourceModel(Constants.ERROR_WORK_RESEARCHER_ACCOUNTNUMBER_REQUIRED, resercherAccountNumberTxtFld, new Model<String>(Constants.RESEARCHER_ACCOUNT_NUMBER_TAG)));
			}	
		}
		super.process(submittingComponent);
	}
	*/
	
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
		if(checkBillingDetails()>0){
			processErrors(target);
			return;
		}
		
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
	
	private int checkBillingDetails(){
		int  count=0;
		Researcher researcher=getFormModel().getResearcher();
		
		if(researcher.getBillingType() !=null &&
				"EFT".equalsIgnoreCase(researcher.getBillingType().getName())){
			if(researcher.getAccountName() ==null 
					|| researcher.getAccountName().trim().length() < 1){
				this.error("Field 'Account Name' is required.");
				++count;
			}
			if(researcher.getBank() ==null 
					|| researcher.getBank().trim().length() < 1){
				this.error("Field 'Bank' is required.");
				++count;
			}
			if(researcher.getBsb() ==null 
					|| researcher.getBsb().trim().length() < 1){
				this.error("Field 'BSB' is required.");
				++count;
			}
			if(researcher.getAccountNumber() == null 
					|| researcher.getAccountNumber().trim().length() < 1){
				this.error("Field 'Account Number' is required.");
				++count;
			}	
		}
		return count;
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
	
	private void setEnableBankFields(final boolean enabled,final AjaxRequestTarget target) {

		resercherAccountNumberTxtFld.setEnabled(enabled);
		resercherBankTxtFld.setEnabled(enabled);
		resercherBSBTxtFld.setEnabled(enabled);
		resercherAccountNameTxtFld.setEnabled(enabled);
		
//		resercherAccountNameTxtFld.setRequired(false);
//		resercherBankTxtFld.setRequired(false);
//		resercherBSBTxtFld.setRequired(false);
//		resercherAccountNumberTxtFld.setRequired(false);
		
		target.add(resercherAccountNumberTxtFld,resercherBankTxtFld,resercherBSBTxtFld,resercherAccountNameTxtFld);

	}
	
	private void setEnableBankFields(final boolean enabled) {
		resercherAccountNumberTxtFld.setEnabled(enabled);
		resercherBankTxtFld.setEnabled(enabled);
		resercherBSBTxtFld.setEnabled(enabled);
		resercherAccountNameTxtFld.setEnabled(enabled);
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
	
	private ResearcherVo getFormModel(){
		return containerForm.getModelObject();
	}

}