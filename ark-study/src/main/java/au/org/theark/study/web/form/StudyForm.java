package au.org.theark.study.web.form;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.IChoiceRenderer;
import org.apache.wicket.markup.html.form.ListMultipleChoice;
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
import au.org.theark.core.util.UIHelper;
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

	@SpringBean( name = "userService")
	private IUserService userService;

	
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

	WebMarkupContainer listMultipleChoiceContainer;//A container that will house the ListMultipleChoice controls
	ListMultipleChoice<String> availableApplicationsLmc;
	ListMultipleChoice selectedApplicationsLmc;
	AjaxButton addButton;
	AjaxButton addAllButton;
	AjaxButton removeButton;
	AjaxButton removeAllButton;
	
	
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
				List<String> selectedItems = (List<String>)selectedApplicationsLmc.getChoices();
				System.out.println("\n -----------------------------------");
				System.out.println("\n Selected Application Items" + selectedItems.size());
				StudyModel studyModel = (StudyModel) getForm().getModelObject();
				studyModel.setLmcSelectedApps(selectedItems);
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
		listMultipleChoiceContainer = initLMCContainer();
		
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
		selectedApplicationsLmc.setRequired(true).setLabel( new StringResourceModel("error.study.selected.app", this, null));
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
		ThemeUiHelper.componentRounded(availableApplicationsLmc);
		ThemeUiHelper.componentRounded(selectedApplicationsLmc);
		ThemeUiHelper.buttonRounded(addButton);
		ThemeUiHelper.buttonRounded(addAllButton);
		ThemeUiHelper.buttonRounded(removeButton);
		ThemeUiHelper.buttonRounded(removeAllButton);
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
		add(listMultipleChoiceContainer);
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
	/**
	 * Method that initialises a WebMarkupContainer that in turn holds the ListMultipleChoice controls and related Add/Remove buttons
	 * @return
	 * @throws ArkSystemException
	 */
	private WebMarkupContainer initLMCContainer() throws ArkSystemException{
		
		listMultipleChoiceContainer = new WebMarkupContainer(Constants.LMC_AJAX_CONTAINER);
		listMultipleChoiceContainer.setOutputMarkupId(true);//Ensures that the html markup for components under this container are all refreshed along with their current state.
		
		/*Initialise the selected application List first*/
		List<String> selectedApps = new ArrayList<String>();
		
		modules = userService.getModules(true);
		
		selectedApplicationsLmc = new ListMultipleChoice<String>(Constants.LMC_SELECTED_APPS, new Model(), selectedApps);
		
		/*Initialise the available application list*/
		List<String> availableApps = new ArrayList<String>();
		UIHelper.getDisplayModuleNameList(modules,availableApps);
		
		availableApplicationsLmc = new ListMultipleChoice<String>(Constants.LMC_AVAILABLE_APPS, new Model(), availableApps);
		
		//Attach a Ajax Behavior to update the Selected Applications control
		availableApplicationsLmc.add( new AjaxFormComponentUpdatingBehavior("ondblclick") {
			
			@Override
			protected void onUpdate(AjaxRequestTarget target) {
				List<String> selectedItems = (List<String>) availableApplicationsLmc.getModelObject();
				UIHelper.addSelectedItems(selectedItems, selectedApplicationsLmc);
				target.addComponent(listMultipleChoiceContainer);
			}
		});
		
		addButton = initialiseAddButton(	listMultipleChoiceContainer, availableApplicationsLmc,
											selectedApplicationsLmc, Constants.ADD_SELECTED, 
											addButton,	Constants.ACTION_ADD_SELECTED);
		
		addAllButton = initialiseAddButton(	listMultipleChoiceContainer, availableApplicationsLmc,
											selectedApplicationsLmc,	Constants.ADD_ALL_BUTTON,
											addAllButton,	Constants.ACTION_ADD_ALL);
		
		removeButton = initialiseRemoveButton(	listMultipleChoiceContainer,selectedApplicationsLmc,
												Constants.REMOVE_SELECTED_BUTTON,removeButton,
												Constants.ACTION_REMOVE_SELECTED);
		
		removeAllButton = initialiseRemoveButton(	listMultipleChoiceContainer,	selectedApplicationsLmc,
													Constants.REMOVE_ALL_BUTTON,removeAllButton,
													Constants.ACTION_REMOVE_ALL);
		
		listMultipleChoiceContainer.add(selectedApplicationsLmc);
		listMultipleChoiceContainer.add(availableApplicationsLmc);
		listMultipleChoiceContainer.add(addButton);
		listMultipleChoiceContainer.add(addAllButton);
		listMultipleChoiceContainer.add(removeButton);
		listMultipleChoiceContainer.add(removeAllButton);
		return listMultipleChoiceContainer;
	}
	
	private AjaxButton initialiseAddButton(	final WebMarkupContainer container, final ListMultipleChoice<String> availableAppsLMC, 
											final ListMultipleChoice<String> targetMLC, String buttonId, Button button, final String action){
		
		button =(AjaxButton) new AjaxButton(buttonId){
			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
				List<String> selectedChoice = new ArrayList<String>();
				//Get the items selected from the control's MODEL
				if(action.equalsIgnoreCase(Constants.ACTION_ADD_SELECTED)){
					selectedChoice = (List<String>)availableAppsLMC.getModelObject();
				}else{
					selectedChoice = (List<String>)availableAppsLMC.getChoices();
				}
				UIHelper.addSelectedItems(selectedChoice, targetMLC);
				target.addComponent(container);
			}
			@Override	
			protected void onError(AjaxRequestTarget target, Form<?> form){
				System.out.println("onError called on Add Button");
			}
		};
		button.setModel(new StringResourceModel("addSelectedTxt",this,null));
		return (AjaxButton)button;
	}
	
	
	private AjaxButton initialiseRemoveButton(	final WebMarkupContainer container,	final ListMultipleChoice<String> targetMLC, 
												String buttonId, Button button, final String action){
		
		button = (AjaxButton)new AjaxButton(buttonId){
			@Override
			protected void onSubmit(AjaxRequestTarget requestTarget, Form<?> arg1) {
				List<String> selectedItems = new ArrayList<String>(); 
				if(action.equalsIgnoreCase(Constants.ACTION_REMOVE_SELECTED)){
					selectedItems = (List<String>) targetMLC.getModelObject();
					targetMLC.getChoices().removeAll(selectedItems);	
										
				}else{
					selectedItems =(List<String>)targetMLC.getChoices();
					targetMLC.getChoices().removeAll(selectedItems);
				}
				requestTarget.addComponent(container);
			}
		};
		button.setModel(new StringResourceModel("removeSelectedTxt",this,null));
		return(AjaxButton) button;
	}	

}
