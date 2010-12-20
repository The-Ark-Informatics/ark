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

import org.apache.shiro.SecurityUtils;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.validation.validator.StringValidator;
import org.odlabs.wiquery.ui.datepicker.DatePicker;

import au.org.theark.core.model.study.entity.GenderType;
import au.org.theark.core.model.study.entity.MaritalStatus;
import au.org.theark.core.model.study.entity.Study;
import au.org.theark.core.model.study.entity.SubjectStatus;
import au.org.theark.core.model.study.entity.TitleType;
import au.org.theark.core.model.study.entity.VitalStatus;
import au.org.theark.core.service.IArkCommonService;
import au.org.theark.core.vo.SubjectVO;
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
	
	@SpringBean( name =  au.org.theark.core.Constants.ARK_COMMON_SERVICE)
	private IArkCommonService iArkCommonService;
	
	private WebMarkupContainer  resultListContainer;
	private WebMarkupContainer detailPanelContainer;
	private WebMarkupContainer searchSubjectPanelContainer;
	private WebMarkupContainer phoneListMarkupContainer;
	private WebMarkupContainer phoneDetailPanelContainer;
	
	private ContainerForm subjectContainerForm;
	
//	private PhoneListContainer phoneListContainer;
//	private PhoneList phoneListPanel;
//	private IModel<Object> iModel;
//	private PageableListView<Phone> phonePageableListView;
	
	private TextField<String> subjectIdTxtFld;
	private TextField<String> firstNameTxtFld;
	private TextField<String> middleNameTxtFld;
	private TextField<String> lastNameTxtFld;
	private TextField<String> preferredNameTxtFld;
	private TextField<String> subjectUIDTxtFld;
	
	private DatePicker<Date> dateOfBirth;
	
	//Reference Data 
	private DropDownChoice<TitleType> titleTypeDdc;
	private DropDownChoice<VitalStatus> vitalStatusDdc;
	private DropDownChoice<GenderType> genderTypeDdc;
	private DropDownChoice<SubjectStatus> subjectStatusDdc;
	private DropDownChoice<MaritalStatus> maritalStatusDdc;
	
	//TODO There will be mobile, email and address that will added via a component
	
	private AjaxButton deleteButton;
	private AjaxButton saveButton;
	private AjaxButton cancelButton;
	private FeedbackPanel detailFeedbackPanel;
	
	private AjaxButton addPhoneButton;
	private Study study;
	/**
	 * @param id
	 */
	public DetailsForm(	String id,
						Details detailsPanel, 
						WebMarkupContainer listContainer,
						WebMarkupContainer detailsContainer,
						WebMarkupContainer searchPanelContainer,
						//WebMarkupContainer phoneListWebMarkupContainer,
						ContainerForm containerForm, 
						FeedbackPanel feedbackPanel	) {
		super(id);
		this.subjectContainerForm = containerForm;
		this.resultListContainer = listContainer;
		this.detailPanelContainer = detailsContainer;
		this.detailFeedbackPanel = feedbackPanel;
		this.searchSubjectPanelContainer = searchPanelContainer;
		//this.phoneListMarkupContainer = phoneListWebMarkupContainer;
		
	
		
		cancelButton = new AjaxButton(Constants.CANCEL,  new StringResourceModel("cancelKey", this, null))
		{

			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
				
				SubjectVO subjectVO = new SubjectVO();
				
				subjectContainerForm.setModelObject(subjectVO);
				searchSubjectPanelContainer.setVisible(true);
				detailPanelContainer.setVisible(false);
				resultListContainer.setVisible(false);
				
				//phoneDetailPanelContainer.setVisible(false);
				//phoneListMarkupContainer.setVisible(true);
				
				target.addComponent(searchSubjectPanelContainer);
				target.addComponent(detailPanelContainer);
				target.addComponent(detailFeedbackPanel);
				//target.addComponent(phoneDetailPanelContainer);
				//target.addComponent(phoneListMarkupContainer);
				onCancel(target);
			}
		};
		
		saveButton = new AjaxButton(Constants.SAVE, new StringResourceModel("saveKey", this, null))
		{

			public void onSubmit(AjaxRequestTarget target, Form<?> form) {
				
				Long studyId = (Long)SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.core.Constants.STUDY_CONTEXT_ID);
				if(studyId == null){
					//No study in context
					this.error("There is no study in Context. Please select a study to manage a subject.");
				}
				else{
					study = iArkCommonService.getStudy(studyId);
					
					if(subjectContainerForm.getModelObject().getPerson().getId() == null || subjectContainerForm.getModelObject().getPerson().getId() == 0){
						subjectContainerForm.getModelObject().setStudy(study);
						studyService.createSubject(subjectContainerForm.getModelObject());
						this.info("Subject has been saved successfully and linked to the study in context " + study.getName());
					}else{
						studyService.updateSubject(subjectContainerForm.getModelObject());
						this.info("Subject has been updated successfully and linked to the study in context " + study.getName());
					}
					
					//Collection<SubjectVO> collectionOfSubject = iArkCommonService.getSubject(subjectContainerForm.getModelObject());
					//for (SubjectVO subjectVO2 : collectionOfSubject) {
					//subjectContainerForm.setModelObject(subjectVO2);
					//break;
					//}
						
					//gain access to the ListView in the markup container.
					// PhoneList phoneListPanel  = (PhoneList) phoneListMarkupContainer.get("phoneListPanel");
					// phonePageableListView = (PageableListView<Phone>)phoneListPanel.get("phoneNumberList");
					// phonePageableListView.removeAll();
					// phoneListMarkupContainer.setVisible(true);
					// target.addComponent(phoneListMarkupContainer);
					detailPanelContainer.setVisible(true);

					target.addComponent(detailPanelContainer);
				}
				target.addComponent(detailFeedbackPanel);
			}
			
			public void onError(AjaxRequestTarget target, Form<?> form){
				processErrors(target);
			}
		};
		
		addPhoneButton = new AjaxButton(Constants.ADD_PHONE, new StringResourceModel("addPhoneKey", this, null))
		{

			public void onSubmit(AjaxRequestTarget target, Form<?> form) {
				onAddPhone(subjectContainerForm.getModelObject(), target);
				//target.addComponent(detailPanelContainer);
			}
			
			public void onError(AjaxRequestTarget target, Form<?> form){
				processErrors(target);
			}
		};
		
	}
	
	public void initialiseForm(){
		
		/* Contains the List of Phone Numbers */
		//		markupContainerPhoneList = new WebMarkupContainer("phoneListMarkupContainer");
		//		markupContainerPhoneList.setOutputMarkupPlaceholderTag(true);
		//		markupContainerPhoneList.setVisible(true);
		
		phoneDetailPanelContainer = new WebMarkupContainer("phoneDetailMarkupContainer");
		phoneDetailPanelContainer.setOutputMarkupPlaceholderTag(true);
		phoneDetailPanelContainer.setVisible(false);
		
		
		//subjectIdTxtFld = new TextField<String>(Constants.PERSON_PERSON_ID);
		firstNameTxtFld = new TextField<String>(Constants.PERSON_FIRST_NAME);
		middleNameTxtFld = new TextField<String>(Constants.PERSON_MIDDLE_NAME);
		lastNameTxtFld = new TextField<String>(Constants.PERSON_LAST_NAME);
		preferredNameTxtFld = new TextField<String>(Constants.PERSON_PREFERRED_NAME);
		subjectUIDTxtFld = new TextField<String>(Constants.SUBJECT_UID);
		
		dateOfBirth = new DatePicker<Date>(Constants.PERSON_DOB);
		dateOfBirth.setChangeMonth(true);
		dateOfBirth.setChangeYear(true);
		
		//Initialise Drop Down Choices 
		//Title We can also have the reference data populated on Application start and refer to a static list instead of hitting the database
		Collection<TitleType> titleTypeList = iArkCommonService.getTitleType();
		ChoiceRenderer<TitleType> defaultChoiceRenderer = new ChoiceRenderer<TitleType>(Constants.NAME,Constants.ID);
		titleTypeDdc = new DropDownChoice<TitleType>(Constants.PERSON_TYTPE_TYPE,(List)titleTypeList,defaultChoiceRenderer);
		
		Collection<VitalStatus> vitalStatusList = iArkCommonService.getVitalStatus();
		ChoiceRenderer<VitalStatus> vitalStatusRenderer = new ChoiceRenderer<VitalStatus>(Constants.NAME, Constants.ID);
		vitalStatusDdc = new DropDownChoice<VitalStatus>(Constants.PERSON_VITAL_STATUS,(List)vitalStatusList,vitalStatusRenderer);
		
		Collection<GenderType> genderTypeList = iArkCommonService.getGenderType(); 
		ChoiceRenderer<GenderType> genderTypeRenderer = new ChoiceRenderer<GenderType>(Constants.NAME,Constants.ID);
		genderTypeDdc = new DropDownChoice<GenderType>(Constants.PERSON_GENDER_TYPE,(List)genderTypeList,genderTypeRenderer);
		
		Collection<SubjectStatus> subjectStatusList = iArkCommonService.getSubjectStatus();
		ChoiceRenderer<SubjectStatus> subjectStatusRenderer = new ChoiceRenderer<SubjectStatus>(Constants.NAME,Constants.SUBJECT_STATUS_ID);
		subjectStatusDdc = new DropDownChoice<SubjectStatus>(Constants.SUBJECT_STATUS,(List)subjectStatusList,subjectStatusRenderer);
		
		Collection<MaritalStatus> maritalStatusList = iArkCommonService.getMaritalStatus(); 
		ChoiceRenderer<MaritalStatus> maritalStatusRender = new ChoiceRenderer<MaritalStatus>(Constants.NAME,Constants.ID);
		maritalStatusDdc = new DropDownChoice<MaritalStatus>(Constants.PERSON_MARITAL_STATUS,(List) maritalStatusList, maritalStatusRender);
		
		//Add the PhoneListContainer
		//phoneListContainer = new PhoneListContainer("phoneListContainer", subjectContainerForm, detailFeedbackPanel,phoneListMarkupContainer,phoneDetailPanelContainer);
		
		
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
		subjectUIDTxtFld.setRequired(true);
		
	}
	
	private void addComponents(){

		//add(subjectIdTxtFld);
		add(titleTypeDdc);
		add(firstNameTxtFld);
		add(middleNameTxtFld);
		add(lastNameTxtFld);
		add(preferredNameTxtFld);
		add(dateOfBirth);
		add(vitalStatusDdc);
		add(genderTypeDdc);
		add(subjectStatusDdc);
		add(maritalStatusDdc);
		add(subjectUIDTxtFld);
		add(saveButton);
		add(cancelButton.setDefaultFormProcessing(false));
		//Add the Phone Management Container Panel into this form
		//add(phoneListContainer);
	}
	

	
	protected void onSave(SubjectVO subjectVo, AjaxRequestTarget target){
		
	}
	
	protected  void onCancel(AjaxRequestTarget target){
		
	}
	
	protected  void onAddPhone(SubjectVO subjectVo, AjaxRequestTarget target){
		phoneDetailPanelContainer.setVisible(true);
		target.addComponent(phoneDetailPanelContainer);
	}

	protected void processErrors(AjaxRequestTarget target){
		target.addComponent(detailFeedbackPanel);
	}
	
	

	public TextField<String> getSubjectIdTxtFld() {
		return subjectIdTxtFld;
	}

	public void setSubjectIdTxtFld(TextField<String> subjectIdTxtFld) {
		this.subjectIdTxtFld = subjectIdTxtFld;
	}

}
