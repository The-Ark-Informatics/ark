package au.org.theark.study.web.component.managestudy.form;

import java.io.IOException;
import java.sql.Blob;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.wicket.ResourceReference;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormChoiceComponentUpdatingBehavior;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.ajax.form.AjaxFormValidatingBehavior;
import org.apache.wicket.extensions.markup.html.form.DateTextField;
import org.apache.wicket.extensions.markup.html.form.palette.Palette;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.CheckBox;
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
import org.apache.wicket.markup.html.panel.FeedbackPanel;
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

import au.org.theark.core.exception.ArkSystemException;
import au.org.theark.core.exception.CannotRemoveArkModuleException;
import au.org.theark.core.exception.EntityCannotBeRemoved;
import au.org.theark.core.exception.EntityExistsException;
import au.org.theark.core.exception.UnAuthorizedOperation;
import au.org.theark.core.model.study.entity.ArkModule;
import au.org.theark.core.model.study.entity.Study;
import au.org.theark.core.model.study.entity.StudyStatus;
import au.org.theark.core.model.study.entity.SubjectUidPadChar;
import au.org.theark.core.model.study.entity.SubjectUidToken;
import au.org.theark.core.service.IArkCommonService;
import au.org.theark.core.util.ContextHelper;
import au.org.theark.core.vo.ModuleVO;
import au.org.theark.core.vo.StudyModelVO;
import au.org.theark.core.web.behavior.ArkDefaultFormFocusBehavior;
import au.org.theark.core.web.component.ArkDatePicker;
import au.org.theark.core.web.form.AbstractArchiveDetailForm;
import au.org.theark.study.service.IStudyService;
import au.org.theark.study.web.Constants;
import au.org.theark.study.web.component.managestudy.StudyCrudContainerVO;
import au.org.theark.study.web.component.managestudy.StudyHelper;
import au.org.theark.study.web.component.managestudy.StudyLogoValidator;

@SuppressWarnings( { "unchecked", "serial", "unused" })
public class DetailForm extends AbstractArchiveDetailForm<StudyModelVO>
{
	@SpringBean(name = Constants.STUDY_SERVICE)
	private IStudyService			studyService;

	@SpringBean(name = au.org.theark.core.Constants.ARK_COMMON_SERVICE)
	private IArkCommonService		iArkCommonService;

	private int									mode;
	private Label								requiredLabel;
	private TextField<String>				studyIdTxtFld;
	private TextField<String>				studyNameTxtFld;
	private TextArea<String>				studyDescriptionTxtArea;
	private TextField<String>				estYearOfCompletionTxtFld;
	private TextField<String>				principalContactTxtFld;
	private TextField<String>				principalContactPhoneTxtFld;
	private TextField<String>				chiefInvestigatorTxtFld;
	private TextField<String>				coInvestigatorTxtFld;
	private TextField<String>				subjectUidPrefixTxtFld;
	private TextField<String>				subjectUidTokenTxtFld;
	private DropDownChoice<SubjectUidToken>			subjectUidTokenDpChoices;
	//private TextField<String> 				subjectUidPadCharsTxtFld;
	private DropDownChoice<Long>			subjectUidPadCharsDpChoices;
	private TextField<Integer>				subjectUidStartTxtFld;
	private Label								subjectUidExampleLbl;
	private TextField<String>				bioSpecimenPrefixTxtFld;
	private DateTextField					dateOfApplicationDp;
	private DropDownChoice<StudyStatus>	studyStatusDpChoices;
	private RadioChoice<Boolean>			autoGenSubIdRdChoice;
	private CheckBox						autoGenSubIdChkBox;
	private CheckBox							autoConsentChkBox;
	private RadioChoice<Boolean>			autoConsentRdChoice;

	// Application Select Palette
	private Palette							appPalette;
	//NN Working on this. Commented until we turn the new security mechanism
	private Palette arkModulePalette;

	// Study logo uploader
	private FileUploadField					fileUploadField;

	// Study Logo image
	public NonCachingImage					studyLogoImage;
	private ContextImage						noStudyLogoImage;
	private Container							containerForm;

	// Summary Details
	private Label								studySummaryLabel;
	private List<ModuleVO>					modules;

	private WebMarkupContainer				autoSubjectUidContainer;
	private WebMarkupContainer				subjectUidContainer;
	private String								subjectUidExampleTxt	= "";

	private transient StudyHelper			studyHelper;
	protected StudyCrudContainerVO		studyCrudVO;
	
	/**
	 * Constructor
	 * 
	 * @param id
	 * @param crudVO
	 * @param feedbackPanel
	 * @param containerForm
	 */
	public DetailForm(String id, StudyCrudContainerVO crudVO, FeedbackPanel feedbackPanel, Container containerForm)
	{
		super(id, feedbackPanel, crudVO, containerForm);
		this.studyCrudVO = crudVO;
		this.containerForm = containerForm;
		setMultiPart(true);
		AjaxFormValidatingBehavior.addToAllFormComponents(this, "onKeyup", Duration.seconds(2));
	}
	
	public WebMarkupContainer getSubjectUidContainer()
	{
		return subjectUidContainer;
	}
	
	public WebMarkupContainer getAutoSubjectUidContainer()
	{
		return autoSubjectUidContainer;
	}
	
	public Label getSubjectUidExampleLbl()
	{
		return subjectUidExampleLbl;
	}
	
	public int getMode()
	{
		return mode;
	}

	public void setMode(int mode)
	{
		this.mode = mode;
	}

	public void initialiseDetailForm()
	{
		studyIdTxtFld = new TextField<String>(Constants.STUDY_ID);
		studyNameTxtFld = new TextField<String>(Constants.STUDY_NAME);
		studyNameTxtFld.setRequired(true);
		studyNameTxtFld.add(new ArkDefaultFormFocusBehavior());
		
		studyDescriptionTxtArea = new TextArea<String>(Constants.STUDY_DESCRIPTION);
		estYearOfCompletionTxtFld = new TextField<String>(Constants.STUDY_ESTIMATED_YEAR_OF_COMPLETION);
		principalContactTxtFld = new TextField<String>(Constants.STUDY_CONTACT_PERSON);
		principalContactPhoneTxtFld = new TextField<String>(Constants.STUDY_CONTACT_PERSON_PHONE);
		chiefInvestigatorTxtFld = new TextField<String>(Constants.STUDY_CHIEF_INVESTIGATOR);
		coInvestigatorTxtFld = new TextField<String>(Constants.STUDY_CO_INVESTIGATOR);

		// Container for SubjectUID auto-generation yes/no
		autoSubjectUidContainer = new WebMarkupContainer("autoSubjectUidContainer");
		autoSubjectUidContainer.setOutputMarkupPlaceholderTag(true);

		// Container for SubjectUID auto-generation details
		subjectUidContainer = new WebMarkupContainer("subjectUidContainer");
		subjectUidContainer.setOutputMarkupPlaceholderTag(true);

		// Label showing example auto-generated SubjectUIDs
		subjectUidExampleTxt = iArkCommonService.getSubjectUidExample(containerForm.getModelObject().getStudy());
		if(subjectUidExampleTxt == null || subjectUidExampleTxt.length() == 0)
			subjectUidExampleTxt = Constants.SUBJECTUID_EXAMPLE;
		
		subjectUidExampleLbl = new Label("study.subjectUid.example", new PropertyModel(this, "subjectUidExampleTxt"));
		subjectUidExampleLbl.setOutputMarkupId(true);
		subjectUidExampleLbl.setDefaultModelObject(containerForm.getModelObject().getSubjectUidExample());
		subjectUidExampleLbl.setVisible(true);

		// SubjectUID prefix (e.g. three char representation of the Study name
		subjectUidPrefixTxtFld = new TextField<String>(au.org.theark.study.web.Constants.SUBJECT_UID_PREFIX);
		subjectUidPrefixTxtFld.add(new AjaxFormComponentUpdatingBehavior("onChange")
		{
			@Override
			protected void onUpdate(AjaxRequestTarget target)
			{
				String subjectUidPrefix = subjectUidPrefixTxtFld.getDefaultModelObjectAsString();
				subjectUidExampleTxt = getSubjectUidExample();
				target.addComponent(subjectUidExampleLbl);
			}
		});

		// Token to separate the string (e.g. "-")
		subjectUidTokenTxtFld = new TextField<String>("subjectUidToken");
		subjectUidTokenTxtFld.add(new AjaxFormComponentUpdatingBehavior("onChange")
		{
			@Override
			protected void onUpdate(AjaxRequestTarget target)
			{
				String subjectUidtToken = subjectUidTokenTxtFld.getDefaultModelObjectAsString();
				subjectUidExampleTxt = getSubjectUidExample();
				target.addComponent(subjectUidExampleLbl);
			}
		});
		subjectUidTokenTxtFld.setVisible(false);
		
		// Token selection
		initSubjectUidTokenDropDown();

		// How many padded chars in SubjectUID incrementor
		initSubjectUidPadCharsDropDown();

		// If the Study wishes to start the incrementor at a particular value
		subjectUidStartTxtFld = new TextField<Integer>(au.org.theark.study.web.Constants.SUBJECT_UID_START, Integer.class);
		subjectUidStartTxtFld.add(new AjaxFormComponentUpdatingBehavior("onChange")
		{
			@Override
			protected void onUpdate(AjaxRequestTarget target)
			{
				String subjectUidStart = subjectUidStartTxtFld.getDefaultModelObjectAsString();
				subjectUidExampleTxt = getSubjectUidExample();
				target.addComponent(subjectUidExampleLbl);
			}
		});

		bioSpecimenPrefixTxtFld = new TextField<String>(Constants.SUB_STUDY_BIOSPECIMENT_PREFIX);

		// Create new DateTextField and assign date format
		dateOfApplicationDp = new DateTextField(Constants.STUDY_SEARCH_DOA, au.org.theark.core.Constants.DD_MM_YYYY);
		ArkDatePicker datePicker = new ArkDatePicker();
		datePicker.bind(dateOfApplicationDp);
		dateOfApplicationDp.add(datePicker);

		//initPalette();
		
		initialiseArkModulePalette();
		
		CompoundPropertyModel<StudyModelVO> studyCmpModel = (CompoundPropertyModel<StudyModelVO>) containerForm.getModel(); // details.getCpm();
		initStudyStatusDropDown(studyCmpModel);

		// Radio buttons having issue with setting correct model, hiding and using checkBox
		PropertyModel<Study> pm = new PropertyModel<Study>((CompoundPropertyModel<StudyModelVO>) containerForm.getModel(), "study");
		autoGenSubIdRdChoice = initRadioButtonChoice(null, Constants.STUDY_AUTO_GENERATE_SUBJECTUID, "autoGenerateSubjectUid");
		autoGenSubIdRdChoice.setVisible(false);

		autoGenSubIdChkBox = new CheckBox(Constants.STUDY_AUTO_GENERATE_SUBJECTUID);
		autoGenSubIdChkBox.setVisible(true);

		autoGenSubIdChkBox.add(new AjaxFormComponentUpdatingBehavior("onChange")
		{
			@Override
			protected void onUpdate(AjaxRequestTarget target)
			{
				// Check what was selected and then toggle
				boolean autoGenerateSubjectUId = containerForm.getModelObject().getStudy().getAutoGenerateSubjectUid();
				subjectUidContainer.setEnabled(false);

				if (autoGenerateSubjectUId)
				{
					subjectUidContainer.setEnabled(true);
				}
				target.addComponent(subjectUidContainer);
			}
		});
		autoGenSubIdChkBox.setOutputMarkupId(true);

		autoGenSubIdRdChoice.add(new AjaxFormChoiceComponentUpdatingBehavior()
		{
			@Override
			protected void onUpdate(AjaxRequestTarget target)
			{
				// Check what was selected and then toggle
				boolean autoGenerateSubjectUId = containerForm.getModelObject().getStudy().getAutoGenerateSubjectUid();
				subjectUidContainer.setEnabled(false);

				if (autoGenerateSubjectUId)
				{
					subjectUidContainer.setEnabled(true);
				}
				target.addComponent(subjectUidContainer);
			}
		});

		// Radio buttons having issue with setting correct model, hiding and using checkBox
		autoConsentRdChoice = initRadioButtonChoice(pm, Constants.STUDY_AUTO_CONSENT, "autoConsent");
		autoConsentRdChoice.setVisible(false);
		
		autoConsentChkBox = new CheckBox(Constants.STUDY_AUTO_CONSENT);
		autoConsentChkBox.setVisible(true);
		
		studyIdTxtFld.setEnabled(false);
		studySummaryLabel = new Label("studySummaryLabel");
		studyCrudVO.setStudyLogoUploadContainer(new WebMarkupContainer("studyLogoUploadContainer"));
		studyCrudVO.getStudyLogoUploadContainer().setOutputMarkupPlaceholderTag(true);

		// fileUpload for logo
		fileUploadField = new FileUploadField(Constants.STUDY_FILENAME, new Model<FileUpload>());
		studyCrudVO.getStudyLogoUploadContainer().add(fileUploadField);

		// Set maximum logo image size to 100K
		setMaxSize(Bytes.kilobytes(Constants.STUDY_LOGO_FILESIZE_KB));

		// Add default image regardless
		noStudyLogoImage = new ContextImage("study.studyLogoImage", new Model<String>("images/no_study_logo.gif"));
		studyCrudVO.getStudyLogoImageContainer().add(noStudyLogoImage);
		studyHelper = new StudyHelper();
		studyHelper.setStudyLogoImage(containerForm.getModelObject().getStudy(), "study.studyLogoImage", studyCrudVO.getStudyLogoImageContainer());

		attachValidators();
		addComponents();
	}

	public String getSubjectUidExample()
	{
		Study study = containerForm.getModelObject().getStudy();
		String subjectUidPrefix = new String("");
		String subjectUidToken = new String("");
		String subjectUidPaddedIncrementor = new String("");
		String subjectUidPadChar = new String("0");
		String subjectUidStart = new String("");
		String subjectUidExample = new String("");

		if (study.getSubjectUidPrefix() != null)
			subjectUidPrefix = study.getSubjectUidPrefix();

		if (study.getSubjectUidToken() != null)
			subjectUidToken = study.getSubjectUidToken().getName();

		if (study.getSubjectUidPadChar() != null)
		{
			subjectUidPadChar = study.getSubjectUidPadChar().getName().trim();
		}

		if (study.getSubjectUidStart() != null)
			subjectUidStart = study.getSubjectUidStart().toString();

		int size = Integer.parseInt(subjectUidPadChar);
		subjectUidPaddedIncrementor = StringUtils.leftPad(subjectUidStart, size, "0");
		subjectUidExample = subjectUidPrefix + subjectUidToken + subjectUidPaddedIncrementor;
		
		return subjectUidExample;
	}

	private void initialiseArkModulePalette(){
		
		CompoundPropertyModel<StudyModelVO> sm = (CompoundPropertyModel<StudyModelVO>) containerForm.getModel();
		IChoiceRenderer<String> renderer = new ChoiceRenderer<String>("name", "name");
		PropertyModel<Collection<ArkModule>> selectedModPm =  new PropertyModel<Collection<ArkModule>>(sm,"selectedArkModules");
		PropertyModel<Collection<ArkModule>> availableModulesPm =  new PropertyModel<Collection<ArkModule>>(sm,"availableArkModules");
		
		arkModulePalette = new Palette("selectedArkModules", selectedModPm, availableModulesPm, renderer, au.org.theark.study.web.Constants.PALETTE_ROWS, false)
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
		ChoiceRenderer<StudyStatus> defaultChoiceRenderer = new ChoiceRenderer<StudyStatus>(Constants.NAME, Constants.STUDY_STATUS_KEY);
		studyStatusDpChoices = new DropDownChoice(Constants.STUDY_STATUS, studyStatusList, defaultChoiceRenderer);
	}

	private void initSubjectUidTokenDropDown()
	{
		List<SubjectUidToken> subjectUidTokenList = iArkCommonService.getListOfSubjectUidToken();
		ChoiceRenderer<SubjectUidToken> defaultChoiceRenderer = new ChoiceRenderer<SubjectUidToken>(Constants.NAME, Constants.STUDY_STATUS_KEY);
		subjectUidTokenDpChoices = new DropDownChoice(au.org.theark.study.web.Constants.SUBJECT_UID_TOKEN, subjectUidTokenList, defaultChoiceRenderer);
		subjectUidTokenDpChoices.add(new AjaxFormComponentUpdatingBehavior("onChange")
		{
			@Override
			protected void onUpdate(AjaxRequestTarget target)
			{
				String subjectUidToken = containerForm.getModelObject().getStudy().getSubjectUidToken().getName();
				subjectUidExampleTxt = getSubjectUidExample();
				target.addComponent(subjectUidExampleLbl);
			}
		});
	}
	
	private void initSubjectUidPadCharsDropDown()
	{
		List<SubjectUidPadChar> subjectUidPadCharList = iArkCommonService.getListOfSubjectUidPadChar();
		ChoiceRenderer<SubjectUidPadChar> defaultChoiceRenderer = new ChoiceRenderer<SubjectUidPadChar>(Constants.NAME, Constants.STUDY_STATUS_KEY);
		subjectUidPadCharsDpChoices = new DropDownChoice(au.org.theark.study.web.Constants.SUBJECT_UID_PADCHAR, subjectUidPadCharList, defaultChoiceRenderer);
		subjectUidPadCharsDpChoices.add(new AjaxFormComponentUpdatingBehavior("onChange")
		{
			@Override
			protected void onUpdate(AjaxRequestTarget target)
			{
				String subjectUidPadChar = containerForm.getModelObject().getStudy().getSubjectUidPadChar().getName();
				subjectUidExampleTxt = getSubjectUidExample();
				target.addComponent(subjectUidExampleLbl);
			}
		});
	}

	private void addComponents()
	{
		studyCrudVO.getDetailPanelFormContainer().add(studyIdTxtFld);
		studyCrudVO.getDetailPanelFormContainer().add(studyNameTxtFld);
		studyCrudVO.getDetailPanelFormContainer().add(studyDescriptionTxtArea);
		studyCrudVO.getDetailPanelFormContainer().add(estYearOfCompletionTxtFld);
		studyCrudVO.getDetailPanelFormContainer().add(studyStatusDpChoices);
		studyCrudVO.getDetailPanelFormContainer().add(dateOfApplicationDp);
		studyCrudVO.getDetailPanelFormContainer().add(principalContactPhoneTxtFld);
		studyCrudVO.getDetailPanelFormContainer().add(principalContactTxtFld);
		studyCrudVO.getDetailPanelFormContainer().add(chiefInvestigatorTxtFld);
		studyCrudVO.getDetailPanelFormContainer().add(coInvestigatorTxtFld);

		// SubjectUID auto-generator fields need own container to be disabled on certain criteria
		subjectUidContainer.add(subjectUidPrefixTxtFld);
		subjectUidContainer.add(subjectUidTokenTxtFld);
		subjectUidContainer.add(subjectUidTokenDpChoices);
		// subjectUidContainer.add(subjectUidPadCharsTxtFld);
		subjectUidContainer.add(subjectUidPadCharsDpChoices);
		subjectUidContainer.add(subjectUidStartTxtFld);
		subjectUidContainer.add(subjectUidExampleLbl);
		studyCrudVO.getDetailPanelFormContainer().add(subjectUidContainer);

		studyCrudVO.getDetailPanelFormContainer().add(bioSpecimenPrefixTxtFld);

		// AutoGenerateSubjectUID needs own container to be disabled on certain criteria
		autoSubjectUidContainer.add(autoGenSubIdRdChoice);
		autoSubjectUidContainer.add(autoGenSubIdChkBox);
		studyCrudVO.getDetailPanelFormContainer().add(autoSubjectUidContainer);
		studyCrudVO.getDetailPanelFormContainer().add(autoConsentRdChoice);
		studyCrudVO.getDetailPanelFormContainer().add(autoConsentChkBox);
		studyCrudVO.getDetailPanelFormContainer().add(arkModulePalette);
		studyCrudVO.getDetailPanelFormContainer().add(studyCrudVO.getStudyLogoUploadContainer());
		studyCrudVO.getSummaryContainer().add(studySummaryLabel);
		studyCrudVO.getDetailPanelFormContainer().add(studyCrudVO.getStudyLogoImageContainer());
		add(studyCrudVO.getDetailPanelFormContainer());
		add(studyCrudVO.getSummaryContainer());
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

		
		RadioChoice<Boolean> radioBtn = null;
		if(pm == null)
			radioBtn = new RadioChoice<Boolean>(radioChoiceId);
		else
		{
			PropertyModel<Boolean> propertyModel = new PropertyModel<Boolean>(pm, propertyModelExpr);
			radioBtn = new RadioChoice<Boolean>(radioChoiceId, propertyModel, list, radioChoiceRender);
		}
		return radioBtn;
	}

	@Override
	protected void attachValidators()
	{
		studyNameTxtFld.setRequired(true).setLabel(new StringResourceModel("error.study.name.required", null, new Model<String>("Study Name")));
		// TODO Have to stop the validator posting the content with the error message
		studyDescriptionTxtArea.add(StringValidator.lengthBetween(1, 255)).setLabel(new StringResourceModel("study.description.length.exceeded", null, new Model<String>("Study Synopsis")));
		studyStatusDpChoices.setRequired(true).setLabel(new StringResourceModel("error.study.status.required", this, new Model<String>("Status")));

		// Max dateOfApplicationDp can be only today
		dateOfApplicationDp.add(DateValidator.maximum(new Date())).setLabel(new StringResourceModel("error.study.doa.max.range", this, null));

		// TODO: Write CustomValidator to handle numeric validation
		// Estimated Year of completion a numeric year field, greater than dateOfApplicationDp
		// estYearOfCompletionTxtFld.add(new PatternValidator("^\\d{4}$")).setLabel(new StringResourceModel("error.study.yearOfCompletion", this, new
		// Model<String>("Estimated Year of Completion")));

		chiefInvestigatorTxtFld.setRequired(true).setLabel(new StringResourceModel("error.study.chief", this, new Model<String>("Chief Investigator")));
		chiefInvestigatorTxtFld.add(StringValidator.lengthBetween(3, 50));

		coInvestigatorTxtFld.add(StringValidator.lengthBetween(3, 50)).setLabel(new StringResourceModel("error.study.co.investigator", this, new Model<String>("Co Investigator")));
		// selectedApplicationsLmc.setRequired(true).setLabel( new StringResourceModel("error.study.selected.app", this, null));
		subjectUidStartTxtFld.add(new RangeValidator<Integer>(1, Integer.MAX_VALUE)).setLabel(new StringResourceModel("error.study.subject.key.prefix", this, null));
		// file image validator, checking size, type etc
		fileUploadField.add(new StudyLogoValidator());
	}

	private boolean validateEstYearOfComp(Long yearOfCompletion, Date dateOfApplication, String message, AjaxRequestTarget target)
	{
		boolean validFlag = true;
		SimpleDateFormat simpleDateformat = new SimpleDateFormat("yyyy");
		int dateOfApplicationYear = Integer.parseInt(simpleDateformat.format(dateOfApplication));

		if (yearOfCompletion < dateOfApplicationYear)
		{
			this.error(message);
			processErrors(target);
			validFlag = false;
		}

		return validFlag;
	}

	private void processSaveUpdate(StudyModelVO studyModel, AjaxRequestTarget target) throws EntityExistsException, UnAuthorizedOperation, ArkSystemException, EntityCannotBeRemoved, CannotRemoveArkModuleException
	{
		Collection<ModuleVO> moduleVoCollection = studyModel.getModulesSelected();
		// Convert to Set<String> this can be removed later by changing the interface
		Set<String> moduleList = new HashSet<String>();
		for (ModuleVO moduleVO : moduleVoCollection)
		{
			moduleList.add(moduleVO.getModule());
		}
		studyModel.setLmcSelectedApps(moduleList);

		if (studyModel.getStudy() != null && studyModel.getStudy().getId() == null)
		{
			// Create
			studyService.createStudy(studyModel);
			subjectUidExampleTxt = getSubjectUidExample();
			target.addComponent(subjectUidExampleLbl);
			this.info("Study: " + studyModel.getStudy().getName().toUpperCase() + " has been saved.");
			onSavePostProcess(target, studyCrudVO);
			studyCrudVO.getSummaryContainer().setVisible(true);// added as part of refactoring
		}
		else
		{
			// Update
			studyService.updateStudy(studyModel);
			//studyService.updateStudy(studyModel.getStudy(), studyModel.getLmcSelectedApps());
			//subjectUidExampleTxt = iArkCommonService.getSubjectUidExample(containerForm.getModelObject().getStudy());
			subjectUidExampleTxt = getSubjectUidExample();
			target.addComponent(subjectUidExampleLbl);
			//this.info("Update of Study is under work in progress. The modules are maintained in database instead of LDAP.This feature will be in very soon.");
			this.info("Update of Study: " + studyModel.getStudy().getName().toUpperCase() + " was Successful.");
			onSavePostProcess(target, studyCrudVO);
			studyCrudVO.getSummaryContainer().setVisible(true);
		}

		SecurityUtils.getSubject().getSession().setAttribute("studyId", studyModel.getStudy().getId());
		SecurityUtils.getSubject().getSession().removeAttribute(au.org.theark.core.Constants.PERSON_CONTEXT_ID);
		SecurityUtils.getSubject().getSession().removeAttribute(au.org.theark.core.Constants.PERSON_TYPE);
		containerForm.getModelObject().setStudy(studyModel.getStudy());

		// Set Study into context items
		ContextHelper contextHelper = new ContextHelper();
		contextHelper.resetContextLabel(target, studyCrudVO.getArkContextMarkup());
		contextHelper.setStudyContextLabel(target, studyModel.getStudy().getName(), studyCrudVO.getArkContextMarkup());

		StudyHelper studyHelper = new StudyHelper();
		studyHelper.setStudyLogo(studyModel.getStudy(), target, studyCrudVO.getStudyNameMarkup(), studyCrudVO.getStudyLogoMarkup());
		studyHelper.setStudyLogoImage(studyModel.getStudy(), "study.studyLogoImage", studyCrudVO.getStudyLogoImageContainer());
		target.addComponent(studyCrudVO.getDetailPanelContainer());
		target.addComponent(studyCrudVO.getStudyLogoMarkup());
	}

	@Override
	protected void onSave(Form<StudyModelVO> containerForm, AjaxRequestTarget target)
	{
		try
		{
			String message = "Estimated Year of Completion must be greater than Date Of Application";

			boolean customValidationFlag = false;

			if (containerForm.getModelObject().getStudy().getEstimatedYearOfCompletion() != null)
			{
				customValidationFlag = validateEstYearOfComp(containerForm.getModelObject().getStudy().getEstimatedYearOfCompletion(), containerForm.getModelObject().getStudy().getDateOfApplication(),
						message, target);
			}

			// Store Study logo image
			if (fileUploadField != null && fileUploadField.getFileUpload() != null)
			{
				// Retrieve file and store as Blob in databasse
				FileUpload fileUpload = fileUploadField.getFileUpload();

				// Copy file to Blob object
				Blob payload = Hibernate.createBlob(fileUpload.getInputStream());
				containerForm.getModelObject().getStudy().setStudyLogoBlob(payload);
				containerForm.getModelObject().getStudy().setFilename(fileUpload.getClientFileName());
			}

			if (containerForm.getModelObject().getStudy().getEstimatedYearOfCompletion() != null && customValidationFlag)
			{
				// If there was a value provided then upon successful validation proceed with save or update
				processSaveUpdate(containerForm.getModelObject(), target);
			}
			else if (containerForm.getModelObject().getStudy().getEstimatedYearOfCompletion() == null)
			{
				// If the value for estimate year of completion was not provided then we can proceed with save or update.
				processSaveUpdate(containerForm.getModelObject(), target);
			}
		}
		catch (EntityExistsException eee)
		{
			this.error("The specified study already exists in the system.");
		}
		catch (EntityCannotBeRemoved ecbr)
		{

			this.error("The Study cannot be removed from the system.There are participants linked to the study");
		}
		catch (IOException ioe)
		{
			// TODO: NN This error is not related to a user. Have to mask this/log it and report it as business exception ie why the file was not
			// transferred
			// due to file size etc..or if it was a system exception as a ArkSystemException
			this.error("There was an error transferring the specified Study logo image.");
		}
		catch (ArkSystemException arkSystemExeption)
		{
			this.error("A System exception has occurred. Please contact Support");
		}
		catch (UnAuthorizedOperation e)
		{
			this.error("You (logged in user) is unauthorised to create/update or archive this study.");
		} catch (CannotRemoveArkModuleException e) {
			this.error("You cannot remove the modules as part of the update. There are System Users who are associated with this study and modules.");
		}

	}

	protected void onCancel(AjaxRequestTarget target)
	{
		containerForm.setModelObject(new StudyModelVO());
		studyCrudVO.getSummaryContainer().setVisible(true);
	}

	@Override
	protected void processErrors(AjaxRequestTarget target)
	{
		target.addComponent(feedBackPanel);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see au.org.theark.core.web.form.AbstractArchiveDetailForm#isNew()
	 */
	@Override
	protected boolean isNew()
	{
		if (containerForm.getModelObject().getStudy().getId() == null)
		{
			return true;
		}
		else
		{
			return false;
		}

	}
}
