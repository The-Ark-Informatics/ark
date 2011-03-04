/**
 * 
 * This is a new file
 *
 *
 */
package au.org.theark.study.web.component.subject.form;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.apache.shiro.SecurityUtils;
import org.apache.wicket.ajax.AjaxEventBehavior;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.ajax.form.AjaxFormSubmitBehavior;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.extensions.markup.html.form.DateTextField;
import org.apache.wicket.extensions.yui.calendar.DatePicker;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.validation.validator.DateValidator;
import org.apache.wicket.validation.validator.StringValidator;

import au.org.theark.core.model.study.entity.Country;
import au.org.theark.core.model.study.entity.CountryState;
import au.org.theark.core.model.study.entity.GenderType;
import au.org.theark.core.model.study.entity.MaritalStatus;
import au.org.theark.core.model.study.entity.PersonContactMethod;
import au.org.theark.core.model.study.entity.Study;
import au.org.theark.core.model.study.entity.SubjectStatus;
import au.org.theark.core.model.study.entity.TitleType;
import au.org.theark.core.model.study.entity.VitalStatus;
import au.org.theark.core.model.study.entity.YesNo;
import au.org.theark.core.service.IArkCommonService;
import au.org.theark.core.util.ContextHelper;
import au.org.theark.core.vo.SubjectVO;
import au.org.theark.core.web.form.AbstractDetailForm;
import au.org.theark.study.service.IStudyService;
import au.org.theark.study.web.Constants;

/**
 * @author nivedann
 *
 */
public class DetailsForm extends AbstractSubjectDetailForm<SubjectVO>{

	@SpringBean( name = Constants.STUDY_SERVICE)
	private IStudyService studyService;
	
	@SpringBean( name =  au.org.theark.core.Constants.ARK_COMMON_SERVICE)
	private IArkCommonService iArkCommonService;

	private WebMarkupContainer arkContextMarkupContainer;

	private TextField<String> firstNameTxtFld;
	private TextField<String> middleNameTxtFld;
	private TextField<String> lastNameTxtFld;
	private TextField<String> preferredNameTxtFld;
	private TextField<String> subjectUIDTxtFld;
	
	private DateTextField dateOfBirthTxtFld;
	
	//Custom Fields and Consents at Subject Study Level
	private TextField<String> amdrifIdTxtFld;
	private DateTextField studyApproachDate;
	private TextField<Long> yearOfFirstMamogramTxtFld;
	private TextField<String> yearOfRecentMamogramTxtFld;
	private TextField<String> totalNumberOfMamogramsTxtFld;
	private DropDownChoice<YesNo> consentToActiveContactDdc;
	private DropDownChoice<YesNo> consentToUseDataDdc;
	private DropDownChoice<YesNo> consentToPassDataGatheringDdc;
	
	//Address Stuff comes here 
	private TextField<String> streetAddressTxtFld;
	private TextField<String> cityTxtFld;
	private TextField<String> postCodeTxtFld;
	private DropDownChoice<Country> countryChoice;
	private DropDownChoice<CountryState> stateChoice;
	private WebMarkupContainer countryStateSelector;
	
	//Reference Data 
	private DropDownChoice<TitleType> titleTypeDdc;
	private DropDownChoice<VitalStatus> vitalStatusDdc;
	private DropDownChoice<GenderType> genderTypeDdc;
	private DropDownChoice<SubjectStatus> subjectStatusDdc;
	private DropDownChoice<MaritalStatus> maritalStatusDdc;
	private DropDownChoice<PersonContactMethod> personContactMethodDdc;
	
	private Study study;
	
	
	public DetailsForm(	String id,
						FeedbackPanel feedBackPanel,
						WebMarkupContainer resultListContainer,
						WebMarkupContainer detailPanelContainer,
						WebMarkupContainer detailPanelFormContainer,
						WebMarkupContainer searchPanelContainer,
						WebMarkupContainer viewButtonContainer,
						WebMarkupContainer editButtonContainer,
						WebMarkupContainer arkContextContainer,
						ContainerForm containerForm				) {
		
	
			super(id,feedBackPanel,resultListContainer,detailPanelContainer,detailPanelFormContainer,searchPanelContainer,viewButtonContainer,editButtonContainer,containerForm);
			this.arkContextMarkupContainer = arkContextContainer;
	}

		
	 public void initialiseDetailForm(){

		firstNameTxtFld = new TextField<String>(Constants.PERSON_FIRST_NAME);
		middleNameTxtFld = new TextField<String>(Constants.PERSON_MIDDLE_NAME);
		lastNameTxtFld = new TextField<String>(Constants.PERSON_LAST_NAME);
		preferredNameTxtFld = new TextField<String>(Constants.PERSON_PREFERRED_NAME);
		subjectUIDTxtFld = new TextField<String>("subjectStudy.subjectUID"); //Constants.SUBJECT_UID);
		
		dateOfBirthTxtFld = new DateTextField(Constants.PERSON_DOB,au.org.theark.core.Constants.DD_MM_YYYY);
		DatePicker dobDatePicker = new DatePicker(){
			@Override
			protected boolean enableMonthYearSelection()
			{
				return true;
			}
		};
		dobDatePicker.bind(dateOfBirthTxtFld);
		dateOfBirthTxtFld.add(dobDatePicker);
		
		//Initialise Drop Down Choices 
		//Title We can also have the reference data populated on Application start and refer to a static list instead of hitting the database
		Collection<TitleType> titleTypeList = iArkCommonService.getTitleType();
		ChoiceRenderer<TitleType> defaultChoiceRenderer = new ChoiceRenderer<TitleType>(Constants.NAME,Constants.ID);
		titleTypeDdc = new DropDownChoice<TitleType>(Constants.PERSON_TYTPE_TYPE,(List)titleTypeList,defaultChoiceRenderer);

		Collection<VitalStatus> vitalStatusList = iArkCommonService.getVitalStatus();
		ChoiceRenderer<VitalStatus> vitalStatusRenderer = new ChoiceRenderer<VitalStatus>(Constants.NAME, Constants.ID);
		vitalStatusDdc = new DropDownChoice<VitalStatus>(Constants.PERSON_VITAL_STATUS,(List<VitalStatus>)vitalStatusList,vitalStatusRenderer);
		
		Collection<GenderType> genderTypeList = iArkCommonService.getGenderType(); 
		ChoiceRenderer<GenderType> genderTypeRenderer = new ChoiceRenderer<GenderType>(Constants.NAME,Constants.ID);
		genderTypeDdc = new DropDownChoice<GenderType>(Constants.PERSON_GENDER_TYPE,(List<GenderType>)genderTypeList,genderTypeRenderer);
		
		
		Collection<SubjectStatus> subjectStatusList = iArkCommonService.getSubjectStatus();
		ChoiceRenderer<SubjectStatus> subjectStatusRenderer = new ChoiceRenderer<SubjectStatus>(Constants.NAME,Constants.SUBJECT_STATUS_ID);
		subjectStatusDdc = new DropDownChoice<SubjectStatus>(Constants.SUBJECT_STATUS,(List)subjectStatusList,subjectStatusRenderer);
		
		Collection<MaritalStatus> maritalStatusList = iArkCommonService.getMaritalStatus(); 
		ChoiceRenderer<MaritalStatus> maritalStatusRender = new ChoiceRenderer<MaritalStatus>(Constants.NAME,Constants.ID);
		maritalStatusDdc = new DropDownChoice<MaritalStatus>(Constants.PERSON_MARITAL_STATUS,(List) maritalStatusList, maritalStatusRender);
		
		Collection<PersonContactMethod> contactMethodList = iArkCommonService.getPersonContactMethodList(); 
		ChoiceRenderer<PersonContactMethod> contactMethodRender = new ChoiceRenderer<PersonContactMethod>(Constants.NAME,Constants.ID);
		personContactMethodDdc= new DropDownChoice<PersonContactMethod>(Constants.PERSON_CONTACT_METHOD,(List) contactMethodList, contactMethodRender);
		
		initCustomFields();
		
		attachValidators();
		addDetailFormComponents();
	}
	 
	private void initCustomFields(){
		amdrifIdTxtFld = new TextField<String>("subjectStudy.amdrifId");
		
		studyApproachDate = new DateTextField("subjectStudy.studyApproachDate", au.org.theark.core.Constants.DD_MM_YYYY);
		
		DatePicker dobStudyApproachDatePicker = new DatePicker(){
			@Override
			protected boolean enableMonthYearSelection()
			{
				return true;
			}
		};
		
		dobStudyApproachDatePicker.bind(studyApproachDate);
		studyApproachDate.add(dobStudyApproachDatePicker);
		
		yearOfFirstMamogramTxtFld =  new TextField<Long>("subjectStudy.yearOfFirstMamogram",Long.class);
		yearOfRecentMamogramTxtFld =  new TextField<String>("subjectStudy.yearOfRecentMamogram");
		totalNumberOfMamogramsTxtFld = new TextField<String>("subjectStudy.totalNumberOfMamograms");
		
		streetAddressTxtFld = new TextField<String>("subjectStudy.siteAddress");
		cityTxtFld = new TextField<String>("subjectStudy.city");
		postCodeTxtFld = new TextField<String>("subjectStudy.postCode");
		
		initialiaseCountryDropDown();
		initialiseCountrySelector();
		
		Collection<YesNo> yesNoList = iArkCommonService.getYesNoList(); 
		ChoiceRenderer<YesNo> yesnoRenderer = new ChoiceRenderer<YesNo>(Constants.NAME,Constants.ID);
		consentToActiveContactDdc = new DropDownChoice<YesNo>("subjectStudy.consentToActiveContact",(List)yesNoList,yesnoRenderer);
		
		consentToUseDataDdc = new DropDownChoice<YesNo>("subjectStudy.consentToUseData",(List)yesNoList,yesnoRenderer);
		
		consentToPassDataGatheringDdc  = new DropDownChoice<YesNo>("subjectStudy.consentToPassiveDataGathering",(List)yesNoList,yesnoRenderer);
		
	}
	
	private void initialiseCountrySelector(){
		
		countryStateSelector = new WebMarkupContainer("countryStateSelector");
		countryStateSelector.setOutputMarkupPlaceholderTag(true);
		//Get the value selected in Country
		Country selectedCountry  = countryChoice.getModelObject();
		
		//If there is no country selected, back should default to current country and pull the states
		List<CountryState> countryStateList  = iArkCommonService.getStates(selectedCountry);
		ChoiceRenderer<CountryState> defaultStateChoiceRenderer = new ChoiceRenderer<CountryState>("state", Constants.ID);
		stateChoice = new DropDownChoice<CountryState>("subjectStudy.state",countryStateList,defaultStateChoiceRenderer);
		//Add the Country State Dropdown into the WebMarkupContainer - countrySelector
		countryStateSelector.add(stateChoice);
	}
	
	private void initialiaseCountryDropDown(){
		
		final List<Country> countryList = iArkCommonService.getCountries();
		ChoiceRenderer<Country> defaultChoiceRenderer = new ChoiceRenderer<Country>(Constants.NAME, Constants.ID);
		class TestVO implements Serializable{
			public Country selected= countryList.get(0);
		}
		countryChoice = new DropDownChoice<Country>("subjectStudy.country", new PropertyModel(new TestVO(),"selected"),countryList, defaultChoiceRenderer);
		//Attach a behavior, so when it changes it does something
		countryChoice.add(new AjaxFormComponentUpdatingBehavior("onchange") {
			@Override
			protected void onUpdate(AjaxRequestTarget target) {
				updateCountryStateChoices(countryChoice.getModelObject());
				target.addComponent(countryStateSelector);
			}
		});
	}
	
	/**
	 * A method that will refresh the choices in the State drop down choice based on
	 * what was selected in the Country Dropdown. It uses the country as the argument and invokes
	 * the back-end to fetch relative states.
	 */
	private void updateCountryStateChoices(Country country){
		
		List<CountryState> countryStateList = iArkCommonService.getStates(country);
		stateChoice.getChoices().clear();
		stateChoice.setChoices(countryStateList);
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
		detailPanelFormContainer.add(genderTypeDdc);
		detailPanelFormContainer.add(subjectStatusDdc);
		detailPanelFormContainer.add(maritalStatusDdc);
		detailPanelFormContainer.add(personContactMethodDdc);
		
		//Add the supposed-to-be custom controls into the form container.
		detailPanelFormContainer.add(amdrifIdTxtFld);
		detailPanelFormContainer.add(studyApproachDate);
		detailPanelFormContainer.add(yearOfFirstMamogramTxtFld);
		detailPanelFormContainer.add(yearOfRecentMamogramTxtFld);
		detailPanelFormContainer.add(totalNumberOfMamogramsTxtFld);
		detailPanelFormContainer.add(streetAddressTxtFld);
		detailPanelFormContainer.add(cityTxtFld);
		detailPanelFormContainer.add(postCodeTxtFld);
		detailPanelFormContainer.add(countryChoice);
		detailPanelFormContainer.add(countryStateSelector);//This contains the drop-downn for State
		detailPanelFormContainer.add(consentToActiveContactDdc);
		detailPanelFormContainer.add(consentToUseDataDdc);
		detailPanelFormContainer.add(consentToPassDataGatheringDdc);
	}


	/* (non-Javadoc)
	 * @see au.org.theark.core.web.form.AbstractDetailForm#processErrors(org.apache.wicket.ajax.AjaxRequestTarget)
	 */
	@Override
	protected void processErrors(AjaxRequestTarget target) {
		target.addComponent(feedBackPanel);
	}
	
	protected  void onCancel(AjaxRequestTarget target){
		SubjectVO subjectVO = new SubjectVO();
		containerForm.setModelObject(subjectVO);
		onCancelPostProcess(target);
		
	}
	

	/* (non-Javadoc)
	 * @see au.org.theark.core.web.form.AbstractDetailForm#attachValidators()
	 */
	@Override
	protected void attachValidators() {
		firstNameTxtFld.setRequired(true);
		dateOfBirthTxtFld.setRequired(true);
		studyApproachDate.add(DateValidator.maximum(new Date())).setLabel(new StringResourceModel("error.dateofbirth.max.range", this, null));
		
		vitalStatusDdc.setRequired(true);
		genderTypeDdc.setRequired(true);
		subjectUIDTxtFld.setRequired(true);
		subjectUIDTxtFld.add(StringValidator.lengthBetween(1, 8));
		titleTypeDdc.setRequired(true);
	}

	/* (non-Javadoc)
	 * @see au.org.theark.core.web.form.AbstractDetailForm#onDeleteConfirmed(org.apache.wicket.ajax.AjaxRequestTarget, java.lang.String, org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow)
	 */
//	@Override
//	protected void onDeleteConfirmed(AjaxRequestTarget target,	String selection, ModalWindow selectModalWindow) {
//		
//		selectModalWindow.close(target);
//		containerForm.setModelObject(new SubjectVO());
//		onCancel(target);
//		
//	}
	
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
		
		if(subjectVO.getSubjectStudy().getPerson().getId() == null || 		containerForm.getModelObject().getSubjectStudy().getPerson().getId() == 0){
	
			subjectVO.getSubjectStudy().setStudy(study);
			studyService.createSubject(subjectVO);
			this.info("Subject has been saved successfully and linked to the study in context " + study.getName());
			processErrors(target);

		}else{

			studyService.updateSubject(subjectVO);
			this.info("Subject has been updated successfully and linked to the study in context " + study.getName());
			processErrors(target);

		}
		
	}
	/* (non-Javadoc)
	 * @see au.org.theark.core.web.form.AbstractDetailForm#onSave(org.apache.wicket.markup.html.form.Form, org.apache.wicket.ajax.AjaxRequestTarget)
	 */
	@Override
	protected void onSave(Form<SubjectVO> containerForm,	AjaxRequestTarget target) {
		boolean firstMammogramFlag =false;
		boolean recentMamogramFlag = false;
		target.addComponent(detailPanelContainer);
		
		Long studyId = (Long)SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.core.Constants.STUDY_CONTEXT_ID);
		if(studyId == null){
			//No study in context
			this.error("There is no study in Context. Please select a study to manage a subject.");
			processErrors(target);
		}
		else{
			
			study = iArkCommonService.getStudy(studyId);
			Long yearOfFirstMammogram = containerForm.getModelObject().getSubjectStudy().getYearOfFirstMamogram();
			Long yearOfRecentMammogram =containerForm.getModelObject().getSubjectStudy().getYearOfRecentMamogram();	
			//validate if the fields were supplied
			if(yearOfFirstMammogram != null){
				firstMammogramFlag = validateCustomFields(containerForm.getModelObject().getSubjectStudy().getYearOfFirstMamogram(),
						"Year of Fist Mammogram cannot be in the future.",
						target);
			}
			 
			if(yearOfRecentMammogram != null){
				 recentMamogramFlag =validateCustomFields(containerForm.getModelObject().getSubjectStudy().getYearOfRecentMamogram(),
							"Year of recent Mammogram cannot be in the future.",
							target);
			}
			
			//When both the year fields were supplied, save only if they are valid
			if( (yearOfFirstMammogram != null && firstMammogramFlag)  && (yearOfRecentMammogram != null && recentMamogramFlag)){
				saveUpdateProcess(containerForm.getModelObject(), target);
				onSavePostProcess(target);	
			}
			else if((yearOfFirstMammogram != null && firstMammogramFlag)  && (yearOfRecentMammogram == null)){//when only yearOfFirstMammogram was supplied
				saveUpdateProcess(containerForm.getModelObject(), target);
				onSavePostProcess(target);	
			}
			else if((yearOfFirstMammogram == null )  && (yearOfRecentMammogram != null && recentMamogramFlag)){
				saveUpdateProcess(containerForm.getModelObject(), target);
				onSavePostProcess(target);	
			}else if(yearOfFirstMammogram == null && yearOfRecentMammogram == null){
				//When other
				saveUpdateProcess(containerForm.getModelObject(), target);
				onSavePostProcess(target);	
			}
			
			ContextHelper contextHelper = new ContextHelper();
			contextHelper.resetContextLabel(target, arkContextMarkupContainer);
			contextHelper.setStudyContextLabel(target, study.getName(), arkContextMarkupContainer);
			contextHelper.setSubjectContextLabel(target,containerForm.getModelObject().getSubjectUID(), arkContextMarkupContainer);
			
			SecurityUtils.getSubject().getSession().setAttribute(au.org.theark.core.Constants.PERSON_CONTEXT_ID, containerForm.getModelObject().getSubjectStudy().getPerson().getId());
			//We specify the type of person here as Subject
			SecurityUtils.getSubject().getSession().setAttribute(au.org.theark.core.Constants.PERSON_TYPE, au.org.theark.core.Constants.PERSON_CONTEXT_TYPE_SUBJECT);
			detailPanelContainer.setVisible(true);
		}
		
	}

}
