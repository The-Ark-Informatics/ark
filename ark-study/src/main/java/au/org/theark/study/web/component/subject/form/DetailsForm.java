/**
 * 
 * This is a new file
 *
 *
 */
package au.org.theark.study.web.component.subject.form;

import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.validation.validator.StringValidator;
import org.odlabs.wiquery.ui.datepicker.DatePicker;

import au.org.theark.study.model.entity.GenderType;
import au.org.theark.study.model.entity.StudyComp;
import au.org.theark.study.model.entity.SubjectStatus;
import au.org.theark.study.model.entity.TitleType;
import au.org.theark.study.model.entity.VitalStatus;
import au.org.theark.study.model.vo.SubjectVO;
import au.org.theark.study.service.IStudyService;
import au.org.theark.study.web.Constants;
import au.org.theark.study.web.component.subject.Details;

/**
 * @author nivedann
 *
 */
public class DetailsForm extends Form<SubjectVO>{

	@SpringBean( name = Constants.STUDY_SERVICE)
	private IStudyService studyService;
	
	private WebMarkupContainer  resultListContainer;
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
	
	/**
	 * @param id
	 */
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

			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
				onSave(subjectContainerForm.getModelObject(), target);
				target.addComponent(detailPanelContainer);
			}
			
			public void onError(AjaxRequestTarget target, Form<?> form){
				System.out.println("An Error occured");
			}
		};
		
		
		
	}
	
	public void initialiseForm(){
		
		subjectIdTxtFld = new TextField<String>("person.personKey");
		firstNameTxtFld = new TextField<String>("person.firstName");
		middleNameTxtFld = new TextField<String>("person.middleName");
		lastNameTxtFld = new TextField<String>("person.lastName");
		preferredNameTxtFld = new TextField<String>("person.preferredName");
		dateOfBirth = new DatePicker<Date>("person.dateOfBirth");
		
		//Initialise Drop Down Choices 
		//Title We can also have the reference data populated on Application start and refer to a static list instead of hitting the database
		Collection<TitleType> titleTypeList = studyService.getTitleType();
		ChoiceRenderer defaultChoiceRenderer = new ChoiceRenderer(Constants.NAME,"id");
		titleTypeDdc = new DropDownChoice<TitleType>("person.titleType",(List)titleTypeList,defaultChoiceRenderer);
		
		Collection<VitalStatus> vitalStatusList = studyService.getVitalStatus();
		ChoiceRenderer vitalStatusRenderer = new ChoiceRenderer("statusName", "id");
		vitalStatusDdc = new DropDownChoice<VitalStatus>("person.vitalStatus",(List)vitalStatusList,vitalStatusRenderer);
		
		Collection<GenderType> genderTypeList = studyService.getGenderType(); 
		ChoiceRenderer genderTypeRenderer = new ChoiceRenderer(Constants.NAME,"id");
		genderTypeDdc = new DropDownChoice<GenderType>("person.genderType",(List)genderTypeList,genderTypeRenderer);
		
		Collection<SubjectStatus> subjectStatusList = studyService.getSubjectStatus();
		ChoiceRenderer subjectStatusRenderer = new ChoiceRenderer(Constants.NAME,"subjectStatusKey");
		subjectStatusDdc = new DropDownChoice<SubjectStatus>("subjectStatus",(List)subjectStatusList,subjectStatusRenderer);
		
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

	public TextField<String> getSubjectIdTxtFld() {
		return subjectIdTxtFld;
	}

	public void setSubjectIdTxtFld(TextField<String> subjectIdTxtFld) {
		this.subjectIdTxtFld = subjectIdTxtFld;
	}

}
