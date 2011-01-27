package au.org.theark.study.web.component.managestudy.form;

import java.io.IOException;
import java.sql.Blob;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.wicket.ResourceReference;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormValidatingBehavior;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.extensions.markup.html.form.palette.Palette;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.IChoiceRenderer;
import org.apache.wicket.markup.html.form.RadioChoice;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.form.upload.FileUpload;
import org.apache.wicket.markup.html.form.upload.FileUploadField;
import org.apache.wicket.markup.html.image.ContextImage;
import org.apache.wicket.markup.html.image.NonCachingImage;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.util.lang.Bytes;
import org.apache.wicket.util.time.Duration;
import org.apache.wicket.validation.validator.DateValidator;
import org.apache.wicket.validation.validator.RangeValidator;
import org.apache.wicket.validation.validator.StringValidator;
import org.hibernate.Hibernate;
import org.odlabs.wiquery.ui.datepicker.DatePicker;
import org.odlabs.wiquery.ui.themes.ThemeUiHelper;

import au.org.theark.core.model.study.entity.Study;
import au.org.theark.core.model.study.entity.StudyStatus;
import au.org.theark.core.service.IArkCommonService;
import au.org.theark.core.vo.ModuleVO;
import au.org.theark.core.vo.StudyModelVO;
import au.org.theark.study.service.IStudyService;
import au.org.theark.study.web.Constants;
import au.org.theark.study.web.component.managestudy.StudyLogoValidator;
import au.org.theark.study.web.component.managestudy.StudyLogo;

@SuppressWarnings({ "unchecked", "serial", "unused" })
public class DetailForm extends Form<StudyModelVO>
{
	@SpringBean(name = Constants.STUDY_SERVICE)
	private IStudyService		studyService;

	@SpringBean(name = au.org.theark.core.Constants.ARK_COMMON_SERVICE)
	private IArkCommonService	iArkCommonService;

	private WebMarkupContainer	detailsFormContainer;
	private WebMarkupContainer	summaryPanelContainer;
	private WebMarkupContainer	saveArchivebuttonContainer;
	private WebMarkupContainer	editbuttonContainer;
	private WebMarkupContainer	searchContainer;
	private WebMarkupContainer	studyNameMarkupContainer;
	private WebMarkupContainer	studyLogoMarkupContainer;

	private int						mode;
	private TextField<String>	studyIdTxtFld;

	public void setStudyNameTxtFld(TextField<String> studyNameTxtFld)
	{
		this.studyNameTxtFld = studyNameTxtFld;
	}

	private TextField<String>				studyNameTxtFld;
	private TextArea<String>				studyDescriptionTxtArea;
	private TextField<String>				estYearOfCompletionTxtFld;
	private TextField<String>				principalContactTxtFld;
	private TextField<String>				principalContactPhoneTxtFld;
	private TextField<String>				chiefInvestigatorTxtFld;
	private TextField<String>				coInvestigatorTxtFld;
	private TextField<String>				subjectKeyPrefixTxtFld;
	private TextField<Integer>				subjectKeyStartAtTxtFld;
	private TextField<String>				bioSpecimenPrefixTxtFld;
	private DatePicker<Date>				dateOfApplicationDp;
	private DropDownChoice<StudyStatus>	studyStatusDpChoices;
	private RadioChoice<Boolean>			autoGenSubIdRdChoice;
	private RadioChoice<Boolean>			autoConsentRdChoice;
	// Application Select Palette
	
	private Palette							appPalette;

	// Study logo uploader
	private FileUploadField					fileUploadField;

	// Study Logo image
	public NonCachingImage					studyLogoImage;
	private ContextImage						noStudyLogoImage;

	private WebMarkupContainer				studyLogoUploadContainer;
	private WebMarkupContainer				studyLogoContainer;

	private Container							containerForm;
	/* Summary Details */

	Label											studySummaryLabel;
	AjaxButton									saveButton;
	AjaxButton									cancelButton;
	AjaxButton									editButton;
	AjaxButton									editCancelButton;
	List<ModuleVO>								modules;

	private transient StudyLogo			studyHelper;

	protected void onEdit(StudyModelVO studyModel, AjaxRequestTarget target)
	{

	}

	protected void onSave(StudyModelVO studyModel, AjaxRequestTarget target)
	{

	}

	protected void onCancel(AjaxRequestTarget target)
	{

	}

	protected void onArchive(StudyModelVO studyModel, AjaxRequestTarget target)
	{

	}

	protected void processErrors(AjaxRequestTarget target)
	{

	}

	public AjaxButton getSaveButton()
	{
		return saveButton;
	}

	public AjaxButton getCancelButton()
	{
		return cancelButton;
	}

	/**
	 * Constructor:
	 * 
	 * @param id
	 * @param detailsPanel
	 *           The panel that is linked to this Form instance
	 * @param container
	 *           The WebMarkupContainer that will wrap the SearchResults
	 */
	public DetailForm(String id, final WebMarkupContainer detailsContainer, WebMarkupContainer saveArchiveContainer, WebMarkupContainer editBtnContainer, WebMarkupContainer sumContainer,
			WebMarkupContainer detailFormContainer, WebMarkupContainer studyNameMarkup, WebMarkupContainer studyLogoMarkup, WebMarkupContainer studyLogoImageContainer, Container studyContainerForm)
	{

		super(id);
		this.containerForm = studyContainerForm;
		// Set the Markup containers
		saveArchivebuttonContainer = saveArchiveContainer;
		editbuttonContainer = editBtnContainer;
		summaryPanelContainer = sumContainer;
		detailsFormContainer = detailFormContainer;
		this.studyLogoContainer = studyLogoImageContainer;
		this.studyNameMarkupContainer = studyNameMarkup;
		this.studyLogoMarkupContainer = studyLogoMarkup;

		// Multipart required for file uploads
		setMultiPart(true);

		AjaxFormValidatingBehavior.addToAllFormComponents(this, "onKeyup", Duration.seconds(2));

		/* Initialise and define the events for the buttons */
		cancelButton = new AjaxButton(Constants.CANCEL, new StringResourceModel("cancelKey", this, null))
		{
			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form)
			{
				onCancel(target);
			}

			public void onError(AjaxRequestTarget target, Form<?> form)
			{
				processErrors(target);
			}
		};

		saveButton = new AjaxButton(Constants.SAVE, new StringResourceModel("saveKey", this, null))
		{
			public void onSubmit(AjaxRequestTarget target, Form<?> form)
			{

				StudyModelVO model = containerForm.getModelObject();
				Collection<ModuleVO> moduleVoCollection = containerForm.getModelObject().getModulesSelected();
				// Convert to Set<String> this can be removed later by changing the interface
				Set<String> moduleList = new HashSet<String>();
				for (ModuleVO moduleVO : moduleVoCollection)
				{
					moduleList.add(moduleVO.getModule());
				}
				model.setLmcSelectedApps(moduleList);

				try
				{
					// Retrieve file and store as Blob in databasse
					FileUpload fileUpload = fileUploadField.getFileUpload();
					
					System.out.println("Study logo size: " + fileUpload.getSize()); 
					
					// Copy file to Blob object
					Blob payload = Hibernate.createBlob(fileUpload.getInputStream());
					model.getStudy().setStudyLogoBlob(payload);
					model.getStudy().setFilename(fileUpload.getClientFileName());
				}
				catch (IOException ioe)
				{
					System.out.println("Failed to save the uploaded file: " + ioe);
				}

				onSave(model, target);

				// Set Study logo (both on application header, and within study detail form)
				StudyLogo studyHelper = new StudyLogo();
				studyHelper.setStudyLogo(model.getStudy(), target, studyNameMarkupContainer, studyLogoMarkupContainer);
				studyHelper.setStudyLogoImage(model.getStudy(), "study.studyLogoImage", studyLogoContainer);

				// Hide upload item once saved
				studyLogoUploadContainer.setVisible(false);
				
				target.addComponent(detailsContainer);
				target.addComponent(studyLogoContainer);
			}

			public void onError(AjaxRequestTarget target, Form<?> form)
			{
				processErrors(target);
			}

		};

		editButton = new AjaxButton("edit", new StringResourceModel("deleteKey", this, null))
		{
			public void onSubmit(AjaxRequestTarget target, Form<?> form)
			{
				editbuttonContainer.setVisible(false);
				saveArchivebuttonContainer.setVisible(true);
				detailsFormContainer.setEnabled(true);
				getStudyNameTxtFld().setEnabled(false);
				summaryPanelContainer.setVisible(false);
				
				// Enable logo upload button
				studyLogoUploadContainer.setVisible(true);
				target.addComponent(studyLogoContainer);
				
				target.addComponent(summaryPanelContainer);

				target.addComponent(editbuttonContainer);
				target.addComponent(saveArchivebuttonContainer);
				target.addComponent(saveArchivebuttonContainer);

				target.addComponent(detailsFormContainer);
				target.addComponent(editbuttonContainer);
				target.addComponent(saveArchivebuttonContainer);
			}

			public void onError(AjaxRequestTarget target, Form<?> form)
			{
				processErrors(target);
			}
		};

		editCancelButton = new AjaxButton("editCancel", new StringResourceModel("deleteKey", this, null))
		{
			public void onSubmit(AjaxRequestTarget target, Form<?> form)
			{
				onCancel(target);
			}

			public void onError(AjaxRequestTarget target, Form<?> form)
			{
				processErrors(target);
			}
		};

		studyIdTxtFld = new TextField<String>(Constants.STUDY_KEY);
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
		dateOfApplicationDp.setChangeMonth(true);
		dateOfApplicationDp.setChangeYear(true);
		// dateOfApplicationDp.setDateFormat("dd/mm/yy").getLocale();
		dateOfApplicationDp.setDateFormat("dd/mm/yy");
		initPalette();

		CompoundPropertyModel<StudyModelVO> studyCmpModel = (CompoundPropertyModel<StudyModelVO>) containerForm.getModel(); // details.getCpm();
		initStudyStatusDropDown(studyCmpModel);

		PropertyModel<Study> pm = new PropertyModel<Study>((CompoundPropertyModel<StudyModelVO>) containerForm.getModel(), "study");
		autoGenSubIdRdChoice = initRadioButtonChoice(pm, "autoGenerateSubjectUId", "autoGenSubId");
		autoConsentRdChoice = initRadioButtonChoice(pm, "autoConsent", "autoConsent");
		
		studyIdTxtFld.setEnabled(false);
		studySummaryLabel = new Label("studySummaryLabel");

		studyLogoUploadContainer = new WebMarkupContainer("studyLogoUploadContainer");
		studyLogoUploadContainer.setOutputMarkupPlaceholderTag(true);
		// Hide upload item until edit mode
		studyLogoUploadContainer.setVisible(false);

		// studyLogoImageContainer = new WebMarkupContainer("studyLogoImageContainer");
		// studyLogoImageContainer.setOutputMarkupPlaceholderTag(true);

		// fileUpload for logo
		fileUploadField = new FileUploadField("study.filename",new Model<FileUpload>());
		studyLogoUploadContainer.add(fileUploadField);
		// Set maximum logo image size to 100K
		setMaxSize(Bytes.kilobytes(100));

		// Add default image regardless
		noStudyLogoImage = new ContextImage("study.studyLogoImage", new Model<String>("images/no_study_logo.gif"));
		studyLogoContainer.add(noStudyLogoImage);
		studyHelper = new StudyLogo();
		studyHelper.setStudyLogoImage(containerForm.getModelObject().getStudy(), "study.studyLogoImage", studyLogoContainer);

		attachValidation();
		// decorateComponents();
		addComponents();
	}

	private void initPalette()
	{
		CompoundPropertyModel<StudyModelVO> sm = (CompoundPropertyModel<StudyModelVO>) containerForm.getModel(); // details.getCpm();
		IChoiceRenderer<String> renderer = new ChoiceRenderer<String>("module", "module");
		PropertyModel<Collection<ModuleVO>> selectedModPm = new PropertyModel<Collection<ModuleVO>>(sm, "modulesSelected");
		PropertyModel<Collection<ModuleVO>> lhsPm = new PropertyModel<Collection<ModuleVO>>(sm, "modulesAvailable");
		appPalette = new Palette("modulesSelected", selectedModPm, lhsPm, renderer, au.org.theark.study.web.Constants.PALETTE_ROWS, false)
		{
			@Override
			public ResourceReference getCSS()
			{
				return null;
			}
		};
	}

	private void initStudyStatusDropDown(CompoundPropertyModel<StudyModelVO> studyCmpModel)
	{
		List<StudyStatus> studyStatusList = iArkCommonService.getListOfStudyStatus();
		ChoiceRenderer defaultChoiceRenderer = new ChoiceRenderer(Constants.NAME, Constants.STUDY_STATUS_KEY);
		studyStatusDpChoices = new DropDownChoice(Constants.STUDY_STATUS, studyStatusList, defaultChoiceRenderer);
	}

	public TextField<String> getStudyIdTxtFld()
	{
		return studyIdTxtFld;
	}

	public TextField<String> getStudyNameTxtFld()
	{
		return studyNameTxtFld;
	}

	public int getMode()
	{
		return mode;
	}

	public void setMode(int mode)
	{
		this.mode = mode;
	}

	private void attachValidation()
	{
		studyNameTxtFld.setRequired(true).setLabel(new StringResourceModel("error.study.name.required", null, new Model<String>("Study Name")));
		// TODO Have to stop the validator posting the content with the error message
		studyDescriptionTxtArea.add(StringValidator.lengthBetween(1, 255)).setLabel(new StringResourceModel("study.description.length.exceeded",null, new Model<String>("Study Synopsis")));
		studyStatusDpChoices.setRequired(true).setLabel(new StringResourceModel("error.study.status.required", this, new Model<String>("Status")));
		dateOfApplicationDp.add(DateValidator.maximum(new Date())).setLabel(new StringResourceModel("error.study.doa.max.range", this, null));
		// Can be only today
		// Estimate year of completion - should be a valid year. Must be less than dateOfApplication
		chiefInvestigatorTxtFld.setRequired(true).setLabel(new StringResourceModel("error.study.chief", this, new Model<String>("Chief Investigator")));
		chiefInvestigatorTxtFld.add(StringValidator.lengthBetween(3, 50));

		coInvestigatorTxtFld.add(StringValidator.lengthBetween(3, 50)).setLabel(new StringResourceModel("error.study.co.investigator", this, new Model<String>("Co Investigator")));
		// selectedApplicationsLmc.setRequired(true).setLabel( new StringResourceModel("error.study.selected.app", this, null));
		subjectKeyStartAtTxtFld.add(new RangeValidator<Integer>(1, Integer.MAX_VALUE)).setLabel(new StringResourceModel("error.study.subject.key.prefix", this, null));
	
		// file image validator, checking size, type etc
		fileUploadField.add(new StudyLogoValidator());
	}

	private void decorateComponents()
	{
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
		ThemeUiHelper.buttonRounded(editButton);
		ThemeUiHelper.buttonRounded(editCancelButton);
	}

	private void addComponents()
	{
		detailsFormContainer.add(studyIdTxtFld);
		detailsFormContainer.add(studyNameTxtFld);
		detailsFormContainer.add(studyDescriptionTxtArea);
		detailsFormContainer.add(estYearOfCompletionTxtFld);
		detailsFormContainer.add(studyStatusDpChoices);
		detailsFormContainer.add(dateOfApplicationDp);
		detailsFormContainer.add(principalContactPhoneTxtFld);
		detailsFormContainer.add(principalContactTxtFld);
		detailsFormContainer.add(chiefInvestigatorTxtFld);
		detailsFormContainer.add(coInvestigatorTxtFld);
		detailsFormContainer.add(subjectKeyPrefixTxtFld);
		detailsFormContainer.add(subjectKeyStartAtTxtFld);
		detailsFormContainer.add(bioSpecimenPrefixTxtFld);
		detailsFormContainer.add(autoGenSubIdRdChoice);
		detailsFormContainer.add(autoConsentRdChoice);
		detailsFormContainer.add(appPalette);
		detailsFormContainer.add(studyLogoUploadContainer);
		detailsFormContainer.add(studyLogoContainer);

		// Summary related fields into another container
		summaryPanelContainer.add(studySummaryLabel);

		saveArchivebuttonContainer.add(saveButton);
		saveArchivebuttonContainer.add(cancelButton.setDefaultFormProcessing(false));

		editbuttonContainer.add(editButton);
		editbuttonContainer.add(editCancelButton.setDefaultFormProcessing(false));

		add(detailsFormContainer);
		add(saveArchivebuttonContainer);
		add(editbuttonContainer);
		add(summaryPanelContainer);

	}

	/**
	 * A common method that can be used to render Yes/No using RadioChoice controls
	 * 
	 * @param study
	 * @param propertyModelExpr
	 * @param radioChoiceId
	 * @return
	 */
	private RadioChoice<Boolean> initRadioButtonChoice(PropertyModel<Study> pm, String propertyModelExpr, String radioChoiceId)
	{

		List<Boolean> list = new ArrayList<Boolean>();
		list.add(Boolean.TRUE);
		list.add(Boolean.FALSE);
		/* Implement the IChoiceRenderer */

		IChoiceRenderer<Boolean> radioChoiceRender = new IChoiceRenderer<Boolean>()
		{
			public Object getDisplayValue(final Boolean choice)
			{

				String displayValue = Constants.NO;

				if (choice != null && choice.booleanValue())
				{
					displayValue = Constants.YES;
				}
				return displayValue;
			}

			public String getIdValue(final Boolean object, final int index)
			{
				return object.toString();
			}
		};

		PropertyModel<Boolean> propertyModel = new PropertyModel<Boolean>(pm, propertyModelExpr);
		return new RadioChoice<Boolean>(radioChoiceId, propertyModel, list, radioChoiceRender);
	}

	public DetailForm getDetailsForm()
	{
		return this;
	}

	public AjaxButton getEditButton()
	{
		return editButton;
	}

	public void setEditButton(AjaxButton editButton)
	{
		this.editButton = editButton;
	}
}
