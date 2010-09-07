package au.org.theark.study.web.form;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormValidatingBehavior;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.extensions.markup.html.form.palette.Palette;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.IChoiceRenderer;
import org.apache.wicket.markup.html.form.RadioChoice;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.util.time.Duration;
import org.apache.wicket.validation.validator.DateValidator;
import org.apache.wicket.validation.validator.RangeValidator;
import org.apache.wicket.validation.validator.StringValidator;
import org.odlabs.wiquery.ui.datepicker.DatePicker;
import org.odlabs.wiquery.ui.themes.ThemeUiHelper;

import au.org.theark.core.vo.ModuleVO;
import au.org.theark.study.model.entity.Study;
import au.org.theark.study.model.entity.StudyStatus;
import au.org.theark.study.service.IStudyService;
import au.org.theark.study.web.Constants;
import au.org.theark.study.web.component.study.Details;
import au.org.theark.study.web.component.study.StudyModel;

@SuppressWarnings("serial")
public class StudyForm extends Form<StudyModel>{
	

	@SpringBean( name = Constants.STUDY_SERVICE)
	private IStudyService studyService;
	
	//Study Details Panel
	Details details;
	WebMarkupContainer  listContainer;
	private int mode;
	private TextField<String> studyIdTxtFld;
	public void setStudyNameTxtFld(TextField<String> studyNameTxtFld) {
		this.studyNameTxtFld = studyNameTxtFld;
	}

	private TextField<String> studyNameTxtFld;
	private TextArea<String> studyDescriptionTxtArea;
	private TextField<String> estYearOfCompletionTxtFld;
	private TextField<String> principalContactTxtFld;
	private TextField<String> principalContactPhoneTxtFld;
	private TextField<String> chiefInvestigatorTxtFld;
	private TextField<String> coInvestigatorTxtFld;
	private TextField<String> subjectKeyPrefixTxtFld;
	private TextField<Integer> subjectKeyStartAtTxtFld;
	private TextField<String> bioSpecimenPrefixTxtFld;
	private DatePicker<Date> dateOfApplicationDp;
	private DropDownChoice<StudyStatus> studyStatusDpChoices;
	private RadioChoice<Boolean> autoGenSubIdRdChoice;
	private RadioChoice<Boolean> autoConsentRdChoice;
	//Application Select Palette
	private Palette	appPalette;
	AjaxButton saveButton;
	

	AjaxButton cancelButton;

	AjaxButton archiveButton;
	

	List<ModuleVO> modules;
	
	protected void onSave(StudyModel studyModel, AjaxRequestTarget target){
		
	}
	
	protected  void onCancel(AjaxRequestTarget target){
		
	}
	
	protected void  onArchive(StudyModel studyModel,AjaxRequestTarget target){
		
	}

	public AjaxButton getArchiveButton() {
		return archiveButton;
	}

	public AjaxButton getSaveButton() {
		return saveButton;
	}
	public AjaxButton getCancelButton() {
		return cancelButton;
	}

	
	/**
	 * Constructor: 
	 * @param id
	 * @param detailsPanel The panel that is linked to this Form instance
	 * @param container The WebMarkupContainer that will wrap the SearchResults
	 */
	public StudyForm(String id, Details detailsPanel, WebMarkupContainer container, final FeedbackPanel feedBackPanel, final WebMarkupContainer detailsContainer){
		
		super(id);
		
		AjaxFormValidatingBehavior.addToAllFormComponents(this, "onKeyup", Duration.seconds(2));
		listContainer = container;
		details = detailsPanel;
		
		cancelButton = new AjaxButton(Constants.CANCEL,  new StringResourceModel("cancelKey", this, null))
		{
			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
				listContainer.setVisible(false);
				detailsContainer.setVisible(false);
				target.addComponent(detailsContainer);
				target.addComponent(listContainer);
				details.getCpm().getObject().setStudy(new Study());
				onCancel(target);
			}
		};
		
		saveButton = new AjaxButton(Constants.SAVE, new StringResourceModel("saveKey", this, null))
		{
			public void onSubmit(AjaxRequestTarget target, Form<?> form)
			{	//Access the model associated using a reference to details panel
				CompoundPropertyModel<StudyModel> detailsCpm = details.getCpm();
				
				StudyModel model = detailsCpm.getObject();
				Collection<ModuleVo> moduleVoCollection = model.getModulesSelected(); 
				//Convert to Set<String> this can be removed later by changing the interface
				Set<String> moduleList = new HashSet<String>();
				for (ModuleVo moduleVo : moduleVoCollection) {
					moduleList.add(moduleVo.getModuleName());
				}
				model.setLmcSelectedApps(moduleList);
				target.addComponent(detailsContainer);
				onSave(model,target);
			}
			
			protected void onError(AjaxRequestTarget target, Form<?> form){
				target.addComponent(feedBackPanel);
			}
		}; 
		
		archiveButton = new AjaxButton(Constants.ARCHIVE, new StringResourceModel("deleteKey", this, null))
		{
			public void onSubmit(AjaxRequestTarget target, Form<?> form)
			{
				//Go to Search users page
				CompoundPropertyModel<StudyModel> detailsCpm = details.getCpm();
				StudyModel model = detailsCpm.getObject();
				target.addComponent(detailsContainer);
				onArchive(model, target);
				
			}
		};
		
		studyIdTxtFld =new TextField<String>(Constants.STUDY_KEY);
		studyNameTxtFld = new TextField<String>(Constants.STUDY_NAME); 
		studyDescriptionTxtArea = new TextArea<String>(Constants.STUDY_DESCRIPTION);
		estYearOfCompletionTxtFld = new TextField<String>(Constants.STUDY_ESTIMATED_YEAR_OF_COMPLETION);
		principalContactTxtFld = new TextField<String>(Constants.STUDY_CONTACT_PERSON);
		principalContactPhoneTxtFld = new TextField<String>(Constants.STUDY_CONTACT_PERSON_PHONE);
		chiefInvestigatorTxtFld = new TextField<String>(Constants.STUDY_CHIEF_INVESTIGATOR);
		coInvestigatorTxtFld = new TextField<String>(Constants.STUDY_CO_INVESTIGATOR);
		subjectKeyPrefixTxtFld = new TextField<String>(Constants.SUBJECT_ID_PREFIX);
		subjectKeyStartAtTxtFld = new TextField<Integer>(Constants.SUBJECT_KEY_START, Integer.class);
		bioSpecimenPrefixTxtFld = new TextField<String>(Constants.SUB_STUDY_BIOSPECIMENT_PREFIX);
		dateOfApplicationDp = new DatePicker<Date>(Constants.STUDY_DATE_OF_APPLICATION);
		
		initPalette();
		
		CompoundPropertyModel<StudyModel> studyCmpModel = details.getCpm();
		initStudyStatusDropDown(studyCmpModel);

		PropertyModel<Study> pm = new PropertyModel<Study>(studyCmpModel,"study");
		autoGenSubIdRdChoice = initRadioButtonChoice(pm,"autoGenerateSubjectKey","autoGenSubId");
		autoConsentRdChoice = initRadioButtonChoice(pm,"autoConsent","autoConsent");
		attachValidation();
		studyIdTxtFld.setEnabled(false);
		decorateComponents();
		addComponents();
		
	}
	
	private void initPalette(){
		
		CompoundPropertyModel<StudyModel> sm  = details.getCpm();
		IChoiceRenderer<String> renderer = new ChoiceRenderer<String>("moduleName", "moduleName");
		PropertyModel<Collection<ModuleVo>> selectedModPm = new PropertyModel<Collection<ModuleVo>>(sm,"modulesSelected");
		PropertyModel<Collection<ModuleVo>> lhsPm = new PropertyModel<Collection<ModuleVo>>(sm,"modulesAvailable");
		appPalette = new Palette("modulesSelected", selectedModPm,lhsPm, renderer,5,true);	
	}
	
	@SuppressWarnings("unchecked")
	private void initStudyStatusDropDown(CompoundPropertyModel<StudyModel> studyCmpModel){
		List<StudyStatus>  studyStatusList = studyService.getListOfStudyStatus();
		ChoiceRenderer defaultChoiceRenderer = new ChoiceRenderer(Constants.NAME, Constants.STUDY_STATUS_KEY);
		studyStatusDpChoices = new DropDownChoice(Constants.STUDY_STATUS,studyStatusList,defaultChoiceRenderer);
	}

	public TextField<String> getStudyIdTxtFld() {
		return studyIdTxtFld;
	}
	
	public TextField<String> getStudyNameTxtFld() {
		return studyNameTxtFld;
	}
	
	public int getMode() {
		return mode;
	}

	public void setMode(int mode) {
		this.mode = mode;
	}

	
	
	private void attachValidation(){
	
		studyNameTxtFld.setRequired(true).setLabel(new StringResourceModel("error.study.name.required", this, new Model<String>("Name")));
		//TODO Have to stop the validator posting the content with the error message
		studyDescriptionTxtArea.add(StringValidator.lengthBetween(1, 255));
		studyStatusDpChoices.setRequired(true).setLabel(new StringResourceModel("error.study.status.required",this, new Model<String>("Status")));
		dateOfApplicationDp.add(DateValidator.maximum(new Date())).setLabel( new StringResourceModel("error.study.doa.max.range",this, null));
		//Can be only today
		//Estimate year of completion - should be a valid year. Must be less than dateOfApplication
		chiefInvestigatorTxtFld.setRequired(true).setLabel(new StringResourceModel("error.study.chief",this,new Model<String>("Chief Investigator")));
		chiefInvestigatorTxtFld.add(StringValidator.lengthBetween(3, 50));
		
		coInvestigatorTxtFld.add(StringValidator.lengthBetween(3,50)).setLabel(new StringResourceModel("error.study.co.investigator",this, new Model<String>("Co Investigator")));
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
		ThemeUiHelper.buttonRounded(archiveButton);
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
		add(appPalette);
		add(saveButton);
		add(cancelButton.setDefaultFormProcessing(false));
		add(archiveButton);
	}
	

	/**
	 * A common method that can be used to render Yes/No using RadioChoice controls
	 * @param study
	 * @param propertyModelExpr
	 * @param radioChoiceId
	 * @return
	 */
	private RadioChoice<Boolean> initRadioButtonChoice(PropertyModel<Study> pm, String propertyModelExpr,String radioChoiceId){
	
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
		
		PropertyModel<Boolean> propertyModel = new PropertyModel<Boolean>(pm,propertyModelExpr);
		return new RadioChoice<Boolean>(radioChoiceId,propertyModel,list,radioChoiceRender);
	}

	public StudyForm getDetailsForm() {
		return this;
	}


}
