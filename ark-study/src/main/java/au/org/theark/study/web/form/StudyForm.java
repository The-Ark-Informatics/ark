package au.org.theark.study.web.form;

import java.util.ArrayList;
import java.util.Collection;
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
import org.odlabs.wiquery.ui.datepicker.DatePicker;
import org.odlabs.wiquery.ui.themes.ThemeUiHelper;

import au.org.theark.core.util.UIHelper;
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
	TextField<String> principalContactTxtFld = new TextField<String>(Constants.STUDY_CONTACT);
	TextField<String> principalContactPhoneTxtFld = new TextField<String>(Constants.STUDY_CONTACT_PHONE);
	TextField<String> chiefInvestigatorTxtFld = new TextField<String>(Constants.STUDY_CHIEF_INVESTIGATOR);
	TextField<String> coInvestigatorTxtFld = new TextField<String>(Constants.STUDY_CO_INVESTIGATOR);
	TextField<String> subjectIdPrefixTxtFld = new TextField<String>(Constants.SUBJECT_ID_PREFIX);
	TextField<String> subjectIdStartAtTxtFld = new TextField<String>(Constants.SUBJECT_KEY_START);
	TextField<String> bioSpecimenPrefixTxtFld = new TextField<String>(Constants.SUB_STUDY_BIOSPECIMENT_PREFIX);
	
	DatePicker<Date> dateOfApplicationDp = new DatePicker<Date>(Constants.STUDY_DATE_OF_APPLICATION);
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
		autoGenSubIdRdChoice = initRadioButtonChoice(study,Constants.STUDY_AUTO_GENERATE_SUBJECT_KEY,"autoGenSubId");
		autoConsentRdChoice = initRadioButtonChoice(study,Constants.STUDY_ATUO_CONSENT,"autoConsent");
		listMultipleChoiceContainer = initLMCContainer();
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
		ThemeUiHelper.componentRounded(subjectIdStartAtTxtFld);
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
		add(subjectIdPrefixTxtFld);
		add(subjectIdStartAtTxtFld);
		add(bioSpecimenPrefixTxtFld);
		add(autoGenSubIdRdChoice);
		add(autoConsentRdChoice);
		add(listMultipleChoiceContainer);
		add(saveButton);
		add(cancelButton.setDefaultFormProcessing(false));
		add(deleteButton);
	}
	
	@SuppressWarnings("unchecked")
	private void initStudyStatusDropDown(Study study){
		List<StudyStatus>  studyStatusList = studyService.getListOfStudyStatus();
		ChoiceRenderer defaultChoiceRenderer = new ChoiceRenderer(Constants.NAME, Constants.STUDY_STATUS_KEY);
		PropertyModel propertyModel = new PropertyModel(study,Constants.STUDY_STATUS);
		studyStatusDpChoices = new DropDownChoice(Constants.STUDY_DROP_DOWN_CHOICE,propertyModel,studyStatusList,defaultChoiceRenderer);
	}
	/**
	 * A common method that can be used to render Yes/No using RadioChoice controls
	 * @param study
	 * @param propertyModelExpr
	 * @param radioChoiceId
	 * @return
	 */
	private RadioChoice<Boolean> initRadioButtonChoice(Study study, String propertyModelExpr,String radioChoiceId){
	
		List<Boolean> list = new ArrayList<Boolean>();
		list.add(Boolean.TRUE);
		list.add(Boolean.FALSE);
		/* Implement the IChoiceRenderer*/
		
		IChoiceRenderer<Boolean> radioChoiceRender = new IChoiceRenderer<Boolean>() {
			public Object getDisplayValue(final Boolean choice){
				
				String displayValue=Constants.NO;
				if(choice !=null && choice.booleanValue()){
					displayValue = Constants.NO;
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
	
	
	public StudyForm(String id, Study study) {
		
		super(id, new CompoundPropertyModel<Study>(study));
		
		initFormFields(study);
		
		decorateComponents();
		
		addComponents();
		
	}
	
	private WebMarkupContainer initLMCContainer(){
		
		listMultipleChoiceContainer = new WebMarkupContainer(Constants.LMC_AJAX_CONTAINER);
		listMultipleChoiceContainer.setOutputMarkupId(true);	//Ensures that the html markup for components under this container are all refreshed along with their current state.
		
		/*Initialise the selected application List first*/
		List<String> selectedApps = new ArrayList<String>();
		selectedApplicationsLmc = new ListMultipleChoice<String>(Constants.LMC_SELECTED_APPS, new Model(), selectedApps);
		
		/*Initialise the available application list*/
		List<String> availableApps = new ArrayList<String>();
		availableApps.add("Ark");
		availableApps.add("Genotypic");;
		availableApps.add("Phenotypic");
		
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
	
	private AjaxButton initialiseAddButton(final WebMarkupContainer container, final ListMultipleChoice<String> availableAppsLMC, final ListMultipleChoice<String> targetMLC, String buttonId, Button button, final String action){
		
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
		};
		button.setModel(new StringResourceModel("addSelectedTxt",this,null));
		return (AjaxButton)button;
	}
	
	
	private AjaxButton initialiseRemoveButton(final WebMarkupContainer container, final ListMultipleChoice<String> targetMLC, String buttonId, Button button, final String action){
		
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
