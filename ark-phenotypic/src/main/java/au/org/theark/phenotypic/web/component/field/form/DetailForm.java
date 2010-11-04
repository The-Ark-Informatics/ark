/**
 * 
 * This is a new file
 *
 *
 */
package au.org.theark.phenotypic.web.component.field.form;

import java.util.Date;

import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.odlabs.wiquery.ui.datepicker.DatePicker;

import au.org.theark.core.model.study.entity.GenderType;
import au.org.theark.core.model.study.entity.SubjectStatus;
import au.org.theark.core.model.study.entity.TitleType;
import au.org.theark.core.model.study.entity.VitalStatus;

/**
 * @author nivedann
 *
 */
@SuppressWarnings({ "serial", "unchecked", "unused" })
public class DetailForm extends Form{

	//@SpringBean( name = Constants.STUDY_SERVICE)
	//private IStudyService studyService;
	
	public DetailForm(String id)
	{
		super(id);
		// TODO Auto-generated constructor stub
	}
	private WebMarkupContainer resultListContainer;
	private WebMarkupContainer detailPanelContainer;
	
	private ContainerForm subjectContainerForm;
	
	
	private TextField<String> subjectIdTxtFld;
	private TextField<String> firstNameTxtFld;
	private TextField<String> middleNameTxtFld;
	private TextField<String> lastNameTxtFld;
	private TextField<String> preferredNameTxtFld;
	
	private DatePicker<Date> dateOfBirth;
	
	//Reference Data 
	private DropDownChoice<TitleType> titleTypeDdc;
	private DropDownChoice<VitalStatus> vitalStatusDdc;
	private DropDownChoice<GenderType> genderTypeDdc;
	private DropDownChoice<SubjectStatus> subjectStatusDdc;
	
	//TODO There will be mobile, email and address that will added via a component
	
	private AjaxButton deleteButton;
	private AjaxButton saveButton;
	private AjaxButton cancelButton;
	
	
	/*
	*//**
	 * @param id
	 *//*
	public DetailsForm(	String id,
						Details detailsPanel, 
						WebMarkupContainer listContainer,
						WebMarkupContainer detailsContainer,
						ContainerForm containerForm) {
		super(id);
		this.subjectContainerForm = containerForm;
		this.resultListContainer = listContainer;
		this.detailPanelContainer = detailsContainer;
		this.subjectContainerForm = containerForm;
		
		
		cancelButton = new AjaxButton(Constants.CANCEL,  new StringResourceModel("cancelKey", this, null))
		{

			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
				
				onCancel(target);
			}
		};
		
		saveButton = new AjaxButton(Constants.SAVE, new StringResourceModel("saveKey", this, null))
		{

			public void onSubmit(AjaxRequestTarget target, Form<?> form) {
				onSave(subjectContainerForm.getModelObject(), target);
				target.addComponent(detailPanelContainer);
			}
			
			public void onError(AjaxRequestTarget target, Form<?> form){
				processErrors(target);
			}
		};
		
	}
	
	public void initialiseForm(){
		
		subjectIdTxtFld = new TextField<String>(Constants.PERSON_PERSON_KEY);
		firstNameTxtFld = new TextField<String>(Constants.PERSON_FIRST_NAME);
		middleNameTxtFld = new TextField<String>(Constants.PERSON_MIDDLE_NAME);
		lastNameTxtFld = new TextField<String>(Constants.PERSON_LAST_NAME);
		preferredNameTxtFld = new TextField<String>(Constants.PERSON_PREFERRED_NAME);
		dateOfBirth = new DatePicker<Date>(Constants.PERSON_DOB);
		
		//Initialise Drop Down Choices 
		//Title We can also have the reference data populated on Application start and refer to a static list instead of hitting the database
		Collection<TitleType> titleTypeList = studyService.getTitleType();
		ChoiceRenderer<TitleType> defaultChoiceRenderer = new ChoiceRenderer<TitleType>(Constants.NAME,Constants.ID);
		titleTypeDdc = new DropDownChoice<TitleType>(Constants.PERSON_TYTPE_TYPE,(List)titleTypeList,defaultChoiceRenderer);
		
		Collection<VitalStatus> vitalStatusList = studyService.getVitalStatus();
		ChoiceRenderer<VitalStatus> vitalStatusRenderer = new ChoiceRenderer<VitalStatus>(Constants.STATUS_NAME, Constants.ID);
		vitalStatusDdc = new DropDownChoice<VitalStatus>(Constants.PERSON_VITAL_STATUS,(List)vitalStatusList,vitalStatusRenderer);
		
		Collection<GenderType> genderTypeList = studyService.getGenderType(); 
		ChoiceRenderer<GenderType> genderTypeRenderer = new ChoiceRenderer<GenderType>(Constants.NAME,Constants.ID);
		genderTypeDdc = new DropDownChoice<GenderType>(Constants.PERSON_GENDER_TYPE,(List)genderTypeList,genderTypeRenderer);
		
		Collection<SubjectStatus> subjectStatusList = studyService.getSubjectStatus();
		ChoiceRenderer<SubjectStatus> subjectStatusRenderer = new ChoiceRenderer<SubjectStatus>(Constants.NAME,Constants.SUBJECT_STATUS_KEY);
		subjectStatusDdc = new DropDownChoice<SubjectStatus>(Constants.SUBJECT_STATUS,(List)subjectStatusList,subjectStatusRenderer);
		
		attachValidators();
		addComponents();
	}
	
	private void attachValidators(){
		
		firstNameTxtFld.setRequired(true);
		firstNameTxtFld.add(StringValidator.lengthBetween(3, 50));
		middleNameTxtFld.add(StringValidator.lengthBetween(3, 50));
		lastNameTxtFld.add(StringValidator.lengthBetween(3, 50));
		preferredNameTxtFld.setRequired(true);
		preferredNameTxtFld.add(StringValidator.lengthBetween(3, 50));
		dateOfBirth.setRequired(true);
		vitalStatusDdc.setRequired(true);
		genderTypeDdc.setRequired(true);
		
	}
	
	private void addComponents(){

		add(subjectIdTxtFld);
		add(titleTypeDdc);
		add(firstNameTxtFld);
		add(middleNameTxtFld);
		add(lastNameTxtFld);
		add(preferredNameTxtFld);
		add(dateOfBirth);
		add(vitalStatusDdc);
		add(genderTypeDdc);
		add(subjectStatusDdc);
		add(saveButton);
		add(cancelButton.setDefaultFormProcessing(false));
	}
	
	protected void onSave(SubjectVO subjectVo, AjaxRequestTarget target){
		
	}
	
	protected  void onCancel(AjaxRequestTarget target){
		
	}
	
	protected void processErrors(AjaxRequestTarget target){
		
	}

	public TextField<String> getSubjectIdTxtFld() {
		return subjectIdTxtFld;
	}

	public void setSubjectIdTxtFld(TextField<String> subjectIdTxtFld) {
		this.subjectIdTxtFld = subjectIdTxtFld;
	}*/

}
