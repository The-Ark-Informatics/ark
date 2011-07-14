package au.org.theark.lims.web.component.subjectLims.subject.form;

import java.util.Calendar;
import java.util.Collection;
import java.util.List;

import org.apache.shiro.SecurityUtils;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.extensions.markup.html.form.DateTextField;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.EmptyPanel;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import au.org.theark.core.model.study.entity.ConsentStatus;
import au.org.theark.core.model.study.entity.ConsentType;
import au.org.theark.core.model.study.entity.GenderType;
import au.org.theark.core.model.study.entity.MaritalStatus;
import au.org.theark.core.model.study.entity.PersonContactMethod;
import au.org.theark.core.model.study.entity.Study;
import au.org.theark.core.model.study.entity.SubjectStatus;
import au.org.theark.core.model.study.entity.TitleType;
import au.org.theark.core.model.study.entity.VitalStatus;
import au.org.theark.core.model.study.entity.YesNo;
import au.org.theark.core.service.IArkCommonService;
import au.org.theark.core.vo.SubjectVO;
import au.org.theark.core.web.behavior.ArkDefaultFormFocusBehavior;
import au.org.theark.core.web.component.ArkDatePicker;
import au.org.theark.core.web.form.AbstractDetailForm;
import au.org.theark.lims.web.Constants;

/**
 * @author cellis
 *
 */
public class DetailForm extends AbstractDetailForm<SubjectVO>{

	/**
	 * 
	 */
	private static final long	serialVersionUID	= 6510243238571556231L;
	protected static final Logger log = LoggerFactory.getLogger(DetailForm.class);

	@SpringBean( name =  au.org.theark.core.Constants.ARK_COMMON_SERVICE)
	private IArkCommonService<Void> iArkCommonService;

	private WebMarkupContainer arkContextMarkupContainer;

	protected TextField<String> subjectUIDTxtFld;
	protected TextField<String> firstNameTxtFld;
	protected TextField<String> middleNameTxtFld;
	protected TextField<String> lastNameTxtFld;
	protected TextField<String> previousLastNameTxtFld;
	protected TextField<String> preferredNameTxtFld;

	protected DateTextField dateOfBirthTxtFld;
	protected DateTextField dateOfDeathTxtFld;
	protected TextField<String> causeOfDeathTxtFld;
	
	// Custom Fields and Consents at Subject Study Level
	protected TextField<String> amdrifIdTxtFld;
	protected DateTextField studyApproachDate;
	protected TextField<Long> yearOfFirstMamogramTxtFld;
	protected TextField<String> yearOfRecentMamogramTxtFld;
	protected TextField<String> totalNumberOfMamogramsTxtFld;
	protected DropDownChoice<YesNo> consentToActiveContactDdc;
	protected DropDownChoice<YesNo> consentToUseDataDdc;
	protected DropDownChoice<YesNo> consentToPassDataGatheringDdc;
	
	// Address Stuff comes here 
	protected TextField<String> preferredEmailTxtFld;
	protected TextField<String> otherEmailTxtFld;
	
	// Reference Data 
	protected DropDownChoice<TitleType> titleTypeDdc;
	protected DropDownChoice<VitalStatus> vitalStatusDdc;
	protected DropDownChoice<GenderType> genderTypeDdc;
	protected DropDownChoice<SubjectStatus> subjectStatusDdc;
	protected DropDownChoice<MaritalStatus> maritalStatusDdc;
	protected DropDownChoice<PersonContactMethod> personContactMethodDdc;
	
	//Study Level Consent Controls
	protected DropDownChoice<ConsentStatus> consentStatusChoice;
	protected DropDownChoice<ConsentType> consentTypeChoice;
	protected DateTextField consentDateTxtFld;
	// Webmarkup for Ajax refreshing of items based on particular criteria
	protected WebMarkupContainer wmcPreferredEmailContainer;
	protected WebMarkupContainer wmcDeathDetailsContainer;
	protected ContainerForm containerForm;

	protected Study study;
	
	
	public DetailForm(	String id,
						FeedbackPanel feedBackPanel,
						WebMarkupContainer resultListContainer,
						WebMarkupContainer detailPanelContainer,
						WebMarkupContainer detailPanelFormContainer,
						WebMarkupContainer searchPanelContainer,
						WebMarkupContainer viewButtonContainer,
						WebMarkupContainer editButtonContainer,
						WebMarkupContainer arkContextContainer,
						ContainerForm containerForm) {
		
	
			super(id,feedBackPanel,resultListContainer,detailPanelContainer,detailPanelFormContainer,searchPanelContainer,viewButtonContainer,editButtonContainer,containerForm);
			this.arkContextMarkupContainer = arkContextContainer;
			this.containerForm = containerForm;
			
			// Disable editing of Subject details in LIMS
			editButton = new AjaxButton(au.org.theark.core.Constants.EDIT)
			{

				@Override
				public boolean isVisible()
				{
					return false;
				}

				@Override
				protected void onSubmit(AjaxRequestTarget target, Form<?> form)
				{
					// Should never get here since Edit button should never be enabled for Subject Details via LIMS
					log.error("Incorrect application workflow - tried to edit Subject Details via LIMS");
				}
			};
			this.viewButtonContainer.addOrReplace(editButton);
	}
		
	@SuppressWarnings("unchecked")
	public void initialiseDetailForm()
	{
		subjectUIDTxtFld = new TextField<String>(Constants.SUBJECT_UID);
		subjectUIDTxtFld.setOutputMarkupId(true);
		
		firstNameTxtFld = new TextField<String>(Constants.PERSON_FIRST_NAME);
		middleNameTxtFld = new TextField<String>(Constants.PERSON_MIDDLE_NAME);
		lastNameTxtFld = new TextField<String>(Constants.PERSON_LAST_NAME);
		
		preferredNameTxtFld = new TextField<String>(Constants.PERSON_PREFERRED_NAME);
		
		preferredEmailTxtFld = new TextField<String>(Constants.PERSON_PREFERRED_EMAIL);
		otherEmailTxtFld = new TextField<String>(Constants.PERSON_OTHER_EMAIL);
		
		dateOfBirthTxtFld = new DateTextField(Constants.PERSON_DOB,au.org.theark.core.Constants.DD_MM_YYYY);
		ArkDatePicker dobDatePicker = new ArkDatePicker();
		dobDatePicker.bind(dateOfBirthTxtFld);
		dateOfBirthTxtFld.add(dobDatePicker);
		
		consentDateTxtFld =  new DateTextField(Constants.PERSON_CONSENT_DATE,au.org.theark.core.Constants.DD_MM_YYYY);
		ArkDatePicker consentDatePicker = new ArkDatePicker();
		consentDatePicker.bind(consentDateTxtFld);
		consentDateTxtFld.add(consentDatePicker);
		
		dateOfDeathTxtFld = new DateTextField(Constants.PERSON_DOD,au.org.theark.core.Constants.DD_MM_YYYY);
		
		causeOfDeathTxtFld = new TextField<String>(Constants.PERSON_CAUSE_OF_DEATH);
		ArkDatePicker dodDatePicker = new ArkDatePicker();
		dodDatePicker.bind(dateOfDeathTxtFld);
		dateOfDeathTxtFld.add(dodDatePicker);
		
		wmcDeathDetailsContainer = new  WebMarkupContainer("deathDetailsContainer");
		wmcDeathDetailsContainer.setOutputMarkupId(true);
		
		// Default death details to disabled (enable onChange of vitalStatus)
		setDeathDetailsContainer();
		
		// Initialise Drop Down Choices
		// We can also have the reference data populated on Application start 
		// and refer to a static list instead of hitting the database
		
		// Title 
		Collection<TitleType> titleTypeList = iArkCommonService.getTitleType();
		ChoiceRenderer<TitleType> defaultChoiceRenderer = new ChoiceRenderer<TitleType>(Constants.NAME,Constants.ID);
		titleTypeDdc = new DropDownChoice<TitleType>(Constants.PERSON_TYTPE_TYPE,(List)titleTypeList,defaultChoiceRenderer);
		titleTypeDdc.add(new ArkDefaultFormFocusBehavior());

		// Vital Status
		Collection<VitalStatus> vitalStatusList = iArkCommonService.getVitalStatus();
		ChoiceRenderer<VitalStatus> vitalStatusRenderer = new ChoiceRenderer<VitalStatus>(Constants.NAME, Constants.ID);
		vitalStatusDdc = new DropDownChoice<VitalStatus>(Constants.PERSON_VITAL_STATUS,(List<VitalStatus>)vitalStatusList,vitalStatusRenderer);
		vitalStatusDdc.add(new AjaxFormComponentUpdatingBehavior("onchange"){
			/**
			 * 
			 */
			private static final long	serialVersionUID	= 3329734396453572649L;

			@Override
			protected void onUpdate(AjaxRequestTarget target) {
				setDeathDetailsContainer();
				target.addComponent(wmcDeathDetailsContainer);
			}
		});
		
		// Gender Type
		Collection<GenderType> genderTypeList = iArkCommonService.getGenderType(); 
		ChoiceRenderer<GenderType> genderTypeRenderer = new ChoiceRenderer<GenderType>(Constants.NAME,Constants.ID);
		genderTypeDdc = new DropDownChoice<GenderType>(Constants.PERSON_GENDER_TYPE,(List<GenderType>)genderTypeList,genderTypeRenderer);
		
		// Subject Status
		List<SubjectStatus> subjectStatusList = iArkCommonService.getSubjectStatus();
		ChoiceRenderer<SubjectStatus> subjectStatusRenderer = new ChoiceRenderer<SubjectStatus>(Constants.NAME,Constants.SUBJECT_STATUS_ID);
		subjectStatusDdc = new DropDownChoice<SubjectStatus>(Constants.SUBJECT_STATUS,subjectStatusList,subjectStatusRenderer);
		
		// Marital Status
		Collection<MaritalStatus> maritalStatusList = iArkCommonService.getMaritalStatus(); 
		ChoiceRenderer<MaritalStatus> maritalStatusRender = new ChoiceRenderer<MaritalStatus>(Constants.NAME,Constants.ID);
		maritalStatusDdc = new DropDownChoice<MaritalStatus>(Constants.PERSON_MARITAL_STATUS,(List) maritalStatusList, maritalStatusRender);
		
		// Container for preferredEmail (required when Email selected as preferred contact)
		wmcPreferredEmailContainer = new  WebMarkupContainer("preferredEmailContainer");
		wmcPreferredEmailContainer.setOutputMarkupPlaceholderTag(true);
		// Depends on preferredContactMethod
		setPreferredEmailContainer();
		
		// Person Contact Method
		Collection<PersonContactMethod> contactMethodList = iArkCommonService.getPersonContactMethodList(); 
		ChoiceRenderer<PersonContactMethod> contactMethodRender = new ChoiceRenderer<PersonContactMethod>(Constants.NAME,Constants.ID);
		personContactMethodDdc= new DropDownChoice<PersonContactMethod>(Constants.PERSON_CONTACT_METHOD,(List) contactMethodList, contactMethodRender);
		personContactMethodDdc.add(new AjaxFormComponentUpdatingBehavior("onchange"){
			/**
			 * 
			 */
			private static final long	serialVersionUID	= 7009218706170453708L;

			@Override
			protected void onUpdate(AjaxRequestTarget target) {
				//Check what was selected and then toggle
				setPreferredEmailContainer();
				target.addComponent(wmcPreferredEmailContainer);
			}
		});
		
		initialiseConsentStatusChoice();
		initialiseConsentTypeChoice();
		attachValidators();
		addDetailFormComponents();
	}
	
	/**
	 * Initialise the Consent Status Drop Down Choice Control
	 */
	protected void initialiseConsentStatusChoice(){
		List<ConsentStatus> consentStatusList = iArkCommonService.getRecordableConsentStatus();
		ChoiceRenderer<ConsentType> defaultChoiceRenderer = new ChoiceRenderer<ConsentType>(Constants.NAME, Constants.ID);
		consentStatusChoice  = new DropDownChoice(Constants.SUBJECT_CONSENT_STATUS, consentStatusList,defaultChoiceRenderer);
	}
	
	protected void initialiseConsentTypeChoice(){
		List<ConsentType> consentTypeList = iArkCommonService.getConsentType();
		ChoiceRenderer defaultChoiceRenderer = new ChoiceRenderer(Constants.NAME, Constants.ID);
		consentTypeChoice = new DropDownChoice(Constants.SUBJECT_CONSENT_TYPE,consentTypeList,defaultChoiceRenderer);
	}
	
	// Death details dependent on Vital Status selected to "Deceased"
	private void setDeathDetailsContainer()
	{
		VitalStatus vitalStatus = containerForm.getModelObject().getLinkSubjectStudy().getPerson().getVitalStatus();
		if(vitalStatus != null){
			String vitalStatusName  = vitalStatus.getName();
			
			if(vitalStatusName.equalsIgnoreCase("DECEASED")){
				wmcDeathDetailsContainer.setEnabled(true);
			}
			else{
				wmcDeathDetailsContainer.setEnabled(false);;
			}
		}
		else
		{
			wmcDeathDetailsContainer.setEnabled(false);
		}
	}
	
	// Email required when preferred contact set to "Email"
	private void setPreferredEmailContainer(){
		PersonContactMethod personContactMethod = containerForm.getModelObject().getLinkSubjectStudy().getPerson().getPersonContactMethod();
		
		if(personContactMethod != null){
			String personContactMethodName  = personContactMethod.getName();
			if(personContactMethodName.equalsIgnoreCase("EMAIL")){
				preferredEmailTxtFld.setRequired(true).setLabel(new StringResourceModel("subject.preferredEmail.required", null));
			}
			else{
				preferredEmailTxtFld.setRequired(false);
				
			}
		}
	}
	
	public void addDetailFormComponents(){
		detailPanelFormContainer.add(subjectUIDTxtFld);
		detailPanelFormContainer.add(titleTypeDdc);
		detailPanelFormContainer.add(firstNameTxtFld);
		detailPanelFormContainer.add(middleNameTxtFld);
		detailPanelFormContainer.add(lastNameTxtFld);
		detailPanelFormContainer.add(preferredNameTxtFld);
		detailPanelFormContainer.add(dateOfBirthTxtFld);
		detailPanelFormContainer.add(vitalStatusDdc);
		
		// Death details only be edited when vital status set to deceased
		wmcDeathDetailsContainer.add(dateOfDeathTxtFld);
		wmcDeathDetailsContainer.add(causeOfDeathTxtFld);
		detailPanelFormContainer.add(wmcDeathDetailsContainer);
		
		detailPanelFormContainer.add(genderTypeDdc);
		detailPanelFormContainer.add(subjectStatusDdc);
		detailPanelFormContainer.add(maritalStatusDdc);
		detailPanelFormContainer.add(personContactMethodDdc);
		
		// Preferred email becomes required when selected as preferred contact method
		wmcPreferredEmailContainer.add(preferredEmailTxtFld);
		detailPanelFormContainer.add(wmcPreferredEmailContainer);
		detailPanelFormContainer.add(otherEmailTxtFld);
	}

	/* (non-Javadoc)
	 * @see au.org.theark.core.web.form.AbstractDetailForm#processErrors(org.apache.wicket.ajax.AjaxRequestTarget)
	 */
	@Override
	protected void processErrors(AjaxRequestTarget target) {
		target.addComponent(feedBackPanel);
	}
	
	protected  void onCancel(AjaxRequestTarget target){
		subjectUIDTxtFld.setEnabled(true);
		SubjectVO subjectVO = new SubjectVO();
		
		// Reset the SubjectVO (with study in context)
		Long sessionStudyId = (Long) SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.core.Constants.STUDY_CONTEXT_ID);
		Study study = iArkCommonService.getStudy(sessionStudyId);
		subjectVO.getLinkSubjectStudy().setStudy(study);
		containerForm.setModelObject(subjectVO);
		
		// Refresh the contextUpdateTarget (remove)
		if (containerForm.getContextUpdateLimsWMC() != null) {
			Panel limsContainerPanel = new EmptyPanel("limsContainerPanel");
			containerForm.getContextUpdateLimsWMC().addOrReplace(limsContainerPanel);
			target.addComponent(containerForm.getContextUpdateLimsWMC());
		}
	}
	
	/* (non-Javadoc)
	 * @see au.org.theark.core.web.form.AbstractDetailForm#attachValidators()
	 */
	@Override
	protected void attachValidators() {
	}

	private boolean validateCustomFields(Long fieldToValidate,String message, AjaxRequestTarget target){
		boolean validFlag=true;
		Calendar calendar = Calendar.getInstance();
		int calYear = calendar.get(Calendar.YEAR);
		if(fieldToValidate > calYear){
			validFlag=false;
			this.error(message);
			processErrors(target);
		}
		
		return validFlag;
	}

	private void saveUpdateProcess(SubjectVO subjectVO,AjaxRequestTarget target){
		// Should never get here since edit should never be enabled for Subject Details via LIMS
		log.error("Incorrect application workflow - tried to save/edit Subject Details via LIMS");
	}
	
	@Override
	protected void onSave(Form<SubjectVO> containerForm, AjaxRequestTarget target) {
		// Should never get here since edit should never be enabled for Subject Details via LIMS
		log.error("Incorrect application workflow - tried to save/edit Subject Details via LIMS");
	}

	/* (non-Javadoc)
	 * @see au.org.theark.core.web.form.AbstractDetailForm#onDeleteConfirmed(org.apache.wicket.ajax.AjaxRequestTarget, java.lang.String, org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow)
	 */
	@Override
	protected void onDeleteConfirmed(AjaxRequestTarget target,	String selection, ModalWindow selectModalWindow) {
		// This should never happen for Subject Management because the Delete button
		// should never be visible/disabled
		log.error("Incorrect application workflow - tried to delete Subject via LIMS");
		selectModalWindow.close(target);
		onCancel(target);
	}

	/* (non-Javadoc)
	 * @see au.org.theark.core.web.form.AbstractDetailForm#isNew()
	 */
	@Override
	protected boolean isNew() {
		if(containerForm.getModelObject().getLinkSubjectStudy().getId() == null){
			return true;
		}else{
			return false;
		}
		
	}

	public TextField<String> getSubjectUIDTxtFld()
	{
		return subjectUIDTxtFld;
	}

	public void setSubjectUIDTxtFld(TextField<String> subjectUIDTxtFld)
	{
		this.subjectUIDTxtFld = subjectUIDTxtFld;
	}
}