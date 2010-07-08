package au.org.theark.study.web.form;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.RadioChoice;
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
	
	TextField<String> studyIdTxtFld =new TextField<String>(Constants.STUDY_KEY);
	TextField<String> studyNameTxtFld = new TextField<String>(Constants.STUDY_NAME);
	TextArea<String> studyDescriptionTxtArea = new TextArea<String>(Constants.STUDY_DESCRIPTION);
	TextField<String> estYearOfCompletionTxtFld = new TextField<String>(Constants.STUDY_ESTIMATED_YEAR_OF_COMPLETION);
	DropDownChoice<StudyStatus> studyStatusDpChoices;
	DatePicker<Date> dateOfApplicationDp = new DatePicker<Date>(Constants.STUDY_DATE_OF_APPLICATION);
	TextField<String> principalContactTxtFld = new TextField<String>(Constants.STUDY_CONTACT);
	TextField<String> principalContactPhoneTxtFld = new TextField<String>(Constants.STUDY_CONTACT_PHONE);
	TextField<String> chiefInvestigatorTxtFld = new TextField<String>(Constants.STUDY_CHIEF_INVESTIGATOR);
	TextField<String> coInvestigatorTxtFld = new TextField<String>(Constants.STUDY_CO_INVESTIGATOR);
	RadioChoice<String> autoGenSubIdRdChoice;
	TextField<String> subjectIdPrefixTxtFld = new TextField<String>(Constants.SUBJECT_ID_PREFIX);
	Button saveButton;
	Button cancelButton;
	Button deleteButton;

	public TextField<String> getStudyIdTxtFld() {
		return studyIdTxtFld;
	}
	
	protected  void onSave(Study study){}
	protected  void onCancel(){}
	protected void  onDelete(Study study){}
	
	private void initFormFields(Study study){
		
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
		initRadioButtonChoice(study);
		
	}
	
	private void decorateComponents(){
		ThemeUiHelper.componentRounded(studyIdTxtFld);
		ThemeUiHelper.componentRounded(studyNameTxtFld);
		ThemeUiHelper.componentRounded(studyDescriptionTxtArea);
		ThemeUiHelper.componentRounded(estYearOfCompletionTxtFld);
		ThemeUiHelper.componentRounded(studyStatusDpChoices);
		ThemeUiHelper.componentRounded(dateOfApplicationDp);
		ThemeUiHelper.componentRounded(principalContactPhoneTxtFld);
		ThemeUiHelper.componentRounded(principalContactTxtFld);
		ThemeUiHelper.componentRounded(chiefInvestigatorTxtFld);
		ThemeUiHelper.componentRounded(coInvestigatorTxtFld);
		ThemeUiHelper.componentRounded(subjectIdPrefixTxtFld);
		ThemeUiHelper.buttonRounded(saveButton);
		ThemeUiHelper.buttonRounded(cancelButton);
		ThemeUiHelper.buttonRounded(deleteButton);
	}
	
	private void addComponents(){
		add(studyIdTxtFld);
		add(studyNameTxtFld);
		add(studyDescriptionTxtArea);
		add(estYearOfCompletionTxtFld);
		add(studyStatusDpChoices);
		add(dateOfApplicationDp);
		add(principalContactPhoneTxtFld);
		add(principalContactTxtFld);
		add(chiefInvestigatorTxtFld);
		add(coInvestigatorTxtFld);
		add(autoGenSubIdRdChoice);
		add(subjectIdPrefixTxtFld);
		add(saveButton);
		add(cancelButton.setDefaultFormProcessing(false));
		add(deleteButton);
	}
	
	@SuppressWarnings("unchecked")
	private void initStudyStatusDropDown(Study study){
		List<StudyStatus>  studyStatusList = studyService.getListOfStudyStatus();
		ChoiceRenderer defaultChoiceRenderer = new ChoiceRenderer("name", "studyStatusKey");
		PropertyModel propertyModel = new PropertyModel(study, "studyStatus");
		studyStatusDpChoices = new DropDownChoice("studyChoice",propertyModel,studyStatusList,defaultChoiceRenderer);
	}
	
	private void initRadioButtonChoice(Study study){
		List<String> list = new ArrayList<String>();
		list.add("Yes");
		list.add("No");
		PropertyModel propertyModel = new PropertyModel(study,"autoGenerateSubjectKey");
		autoGenSubIdRdChoice = new RadioChoice<String>("autoGenSubId",propertyModel, list);
	}
	
	public StudyForm(String id, Study study) {
		
		super(id, new CompoundPropertyModel<Study>(study));
		
		initFormFields(study);
		
		decorateComponents();
		
		addComponents();
		
	}

}
