package au.org.theark.study.web.form;

import java.util.Date;
import java.util.List;

import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.odlabs.wiquery.ui.datepicker.DatePicker;
import org.odlabs.wiquery.ui.themes.ThemeUiHelper;

import au.org.theark.study.model.entity.Study;
import au.org.theark.study.model.entity.StudyStatus;
import au.org.theark.study.service.IStudyService;
import au.org.theark.study.web.Constants;

@SuppressWarnings("serial")
public class StudyForm extends Form<Study>{
	
	@SpringBean( name = Constants.STUDY_SERVICE)
	private IStudyService studyService;
	
	TextField<String> studyIdTxtField =new TextField<String>(Constants.STUDY_KEY);
	TextField<String> studyNameTxtField = new TextField<String>(Constants.STUDY_NAME);
	TextArea<String> studyDescriptionTxtField = new TextArea<String>(Constants.STUDY_DESCRIPTION);
	TextField<String> yearOfCompletionTxtField = new TextField<String>(Constants.STUDY_ESTIMATED_YEAR_OF_COMPLETION);
	DropDownChoice<StudyStatus> studyStatusDpChoices;
	DatePicker<Date> dateOfApplicationDp = new DatePicker<Date>(Constants.STUDY_DATE_OF_APPLICATION);
	TextField<String> principalContactTxtField = new TextField<String>(Constants.STUDY_CONTACT);
	TextField<String> principalContactPhone = new TextField<String>(Constants.STUDY_CONTACT_PHONE);
	TextField<String> chiefInvestigator = new TextField<String>(Constants.STUDY_CHIEF_INVESTIGATOR);
	Button saveButton;
	Button cancelButton;
	Button deleteButton;
	
	protected  void onSave(Study study){}
	protected  void onCancel(){}
	protected void  onDelete(Study study){}
	
	private void initFormFields(){
		
	}
	
	private void decorateComponents(){
		ThemeUiHelper.componentRounded(studyIdTxtField);
		ThemeUiHelper.componentRounded(studyNameTxtField);
		ThemeUiHelper.componentRounded(studyDescriptionTxtField);
		ThemeUiHelper.componentRounded(yearOfCompletionTxtField);
		//ThemeUiHelper.componentRounded(studyStatusDpChoices);
		ThemeUiHelper.componentRounded(dateOfApplicationDp);
		ThemeUiHelper.componentRounded(principalContactPhone);
		ThemeUiHelper.componentRounded(principalContactTxtField);
		ThemeUiHelper.componentRounded(chiefInvestigator);
		ThemeUiHelper.buttonRounded(saveButton);
		ThemeUiHelper.buttonRounded(cancelButton);
		ThemeUiHelper.buttonRounded(deleteButton);
	}
	
	private void addComponents(){
		add(studyIdTxtField);
		add(studyNameTxtField);
		add(studyDescriptionTxtField);
		add(yearOfCompletionTxtField);
		//add(studyStatusDpChoices);
		add(dateOfApplicationDp);
		//add(principalContactPhone);
		//add(principalContactTxtField);
		add(chiefInvestigator);
		add(saveButton);
		add(cancelButton);
		add(deleteButton);
	}
	
	@SuppressWarnings("unchecked")
	private void initStudyStatusDropDown(Study study){
		List<StudyStatus>  studyStatusList = studyService.getListOfStudyStatus();
		ChoiceRenderer defaultChoiceRenderer = new ChoiceRenderer("name", "studyStatusKey");
		PropertyModel propertyModel = new PropertyModel(study, "studyStatus");
		studyStatusDpChoices = new DropDownChoice("studyChoice",propertyModel,studyStatusList,defaultChoiceRenderer);
	}
	
	public StudyForm(String id, Study study) {
		
		super(id, new CompoundPropertyModel<Study>(study));
		
		initFormFields();
		
		saveButton = new Button(Constants.SAVE, new StringResourceModel("saveKey", this, null))
		{
			public void onSubmit()
			{
				onSave((Study) getForm().getModelObject());
			}
		}; 
		
		cancelButton = new Button(Constants.CANCEL,  new StringResourceModel("cancelKey", this, null))
		{
			public void onSubmit()
			{
				//Go to Search users page
				onCancel();
			}
			
		};
		
		deleteButton = new Button(Constants.DELETE, new StringResourceModel("deleteKey", this, null))
		{
			public void onSubmit()
			{
				//Go to Search users page
				onDelete((Study) getForm().getModelObject());
			}
			
		};
		
		initStudyStatusDropDown(study);
		
		decorateComponents();
		
		addComponents();
		
	}

}
