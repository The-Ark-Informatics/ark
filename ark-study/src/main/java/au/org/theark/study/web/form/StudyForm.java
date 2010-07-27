package au.org.theark.study.web.form;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.IChoiceRenderer;
import org.apache.wicket.markup.html.form.RadioChoice;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.validation.validator.DateValidator;
import org.apache.wicket.validation.validator.RangeValidator;
import org.apache.wicket.validation.validator.StringValidator;
import org.odlabs.wiquery.ui.datepicker.DatePicker;
import org.odlabs.wiquery.ui.themes.ThemeUiHelper;

import au.org.theark.core.exception.ArkSystemException;
import au.org.theark.core.vo.ModuleVO;
import au.org.theark.study.model.entity.StudyStatus;
import au.org.theark.study.service.IStudyService;
import au.org.theark.study.service.IUserService;
import au.org.theark.study.web.Constants;
import au.org.theark.study.web.component.study.StudyModel;

@SuppressWarnings("serial")
public class StudyForm extends Form<StudyModel>{
	
	@SpringBean( name = Constants.STUDY_SERVICE)
	private IStudyService studyService;

	TextField<String> studyIdTxtFld =new TextField<String>(Constants.STUDY_KEY);
	TextField<String> studyNameTxtFld = new TextField<String>("study.name");
	TextArea<String> studyDescriptionTxtArea = new TextArea<String>("study.description");
	TextField<String> estYearOfCompletionTxtFld = new TextField<String>("study.estimatedYearOfCompletion");
	TextField<String> principalContactTxtFld = new TextField<String>("study.contactPerson");
	TextField<String> principalContactPhoneTxtFld = new TextField<String>("study.contactPersonPhone");
	TextField<String> chiefInvestigatorTxtFld = new TextField<String>("study.chiefInvestigator");
	TextField<String> coInvestigatorTxtFld = new TextField<String>("study.coInvestigator");
	TextField<String> subjectKeyPrefixTxtFld = new TextField<String>("study.subjectIdPrefix");
	TextField<Integer> subjectKeyStartAtTxtFld = new TextField<Integer>("study.subjectKeyStart", Integer.class);
	TextField<String> bioSpecimenPrefixTxtFld = new TextField<String>("study.subStudyBiospecimenPrefix");
	
	DatePicker<Date> dateOfApplicationDp = new DatePicker<Date>("study.dateOfApplication");
	DropDownChoice<StudyStatus> studyStatusDpChoices;
	RadioChoice<Boolean> autoGenSubIdRdChoice;
	RadioChoice<Boolean> autoConsentRdChoice;
	
	Button saveButton;
	Button cancelButton;
	Button deleteButton;
	
	List<ModuleVO> modules;

	public TextField<String> getStudyIdTxtFld() {
		return studyIdTxtFld;
	}
	
	protected  void onSave(StudyModel studyModel){}
	protected  void onCancel(){}
	protected void  onDelete(StudyModel studyModel){}
	
	private void initFormFields(StudyModel studyModel) throws ArkSystemException{
		
		saveButton = new Button(Constants.SAVE, new StringResourceModel("saveKey", this, null))
		{
			public void onSubmit()
			{
				StudyModel studyModel = (StudyModel) getForm().getModelObject();
				onSave(studyModel);
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
				onDelete((StudyModel) getForm().getModelObject());
			}
			
		};
		
		initStudyStatusDropDown(studyModel);
		autoGenSubIdRdChoice = initRadioButtonChoice(studyModel,"study.autoGenerateSubjectKey","autoGenSubId");
		autoConsentRdChoice = initRadioButtonChoice(studyModel,"study.autoConsent","autoConsent");
		attachValidation();
	}
	
	private void attachValidation(){
	
		studyNameTxtFld.setRequired(true).setLabel(new StringResourceModel("error.study.name.required", this, new Model("Name")));
		studyDescriptionTxtArea.add(StringValidator.lengthBetween(1, 255));//TODO Have to stop the validator posting the content with the error message
		studyStatusDpChoices.setRequired(true).setLabel(new StringResourceModel("error.study.status.required",this, new Model("Status")));
		dateOfApplicationDp.add(DateValidator.maximum(new Date())).setLabel( new StringResourceModel("error.study.doa.max.range",this, null));
		//Can be only today
		//Estimate year of completion - should be a valid year. Must be less than dateOfApplication
		chiefInvestigatorTxtFld.setRequired(true).setLabel(new StringResourceModel("error.study.chief",this,new Model("Chief Investigator")));
		chiefInvestigatorTxtFld.add(StringValidator.lengthBetween(3, 50));
		
		coInvestigatorTxtFld.add(StringValidator.lengthBetween(3,50)).setLabel(new StringResourceModel("error.study.co.investigator",this, new Model("Co Investigator")));
		//selectedApplicationsLmc.setRequired(true).setLabel( new StringResourceModel("error.study.selected.app", this, null));
		subjectKeyStartAtTxtFld.add( new RangeValidator<Integer>(1,Integer.MAX_VALUE)).setLabel( new StringResourceModel("error.study.subject.key.prefix", this, null));
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
		ThemeUiHelper.componentRounded(subjectKeyPrefixTxtFld);
		ThemeUiHelper.componentRounded(subjectKeyStartAtTxtFld);
		ThemeUiHelper.componentRounded(bioSpecimenPrefixTxtFld);
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
		add(subjectKeyPrefixTxtFld);
		add(subjectKeyStartAtTxtFld);
		add(bioSpecimenPrefixTxtFld);
		add(autoGenSubIdRdChoice);
		add(autoConsentRdChoice);
		add(saveButton);
		add(cancelButton.setDefaultFormProcessing(false));
		add(deleteButton);
	}
	
	@SuppressWarnings("unchecked")
	private void initStudyStatusDropDown(StudyModel study){
		List<StudyStatus>  studyStatusList = studyService.getListOfStudyStatus();
		ChoiceRenderer defaultChoiceRenderer = new ChoiceRenderer(Constants.NAME, Constants.STUDY_STATUS_KEY);
		PropertyModel propertyModel = new PropertyModel(study.getStudy(), Constants. STUDY_STATUS);
		studyStatusDpChoices = new DropDownChoice(Constants.STUDY_DROP_DOWN_CHOICE,propertyModel,studyStatusList,defaultChoiceRenderer);
	}
	/**
	 * A common method that can be used to render Yes/No using RadioChoice controls
	 * @param study
	 * @param propertyModelExpr
	 * @param radioChoiceId
	 * @return
	 */
	private RadioChoice<Boolean> initRadioButtonChoice(StudyModel study, String propertyModelExpr,String radioChoiceId){
	
		List<Boolean> list = new ArrayList<Boolean>();
		list.add(Boolean.TRUE);
		list.add(Boolean.FALSE);
		/* Implement the IChoiceRenderer*/
		
		IChoiceRenderer<Boolean> radioChoiceRender = new IChoiceRenderer<Boolean>() {
			public Object getDisplayValue(final Boolean choice){
				
				String displayValue=Constants.NO;
				
				if(choice !=null && choice.booleanValue()){
					displayValue = Constants.YES;
				}
				return displayValue;
			}
			
			public String getIdValue(final Boolean object,final int index){
				return object.toString();
			}
		};
		
		PropertyModel<Boolean> propertyModel = new PropertyModel<Boolean>(study,propertyModelExpr);
		return new RadioChoice<Boolean>(radioChoiceId,propertyModel,list,radioChoiceRender);
	}
	
	
	public StudyForm(String id, StudyModel studyModel) {
		
		super(id, new CompoundPropertyModel<StudyModel>(studyModel));
		
		try{

			initFormFields(studyModel);
			decorateComponents();
			addComponents();
		
		}catch(ArkSystemException ase){
			
		}
	}

}
