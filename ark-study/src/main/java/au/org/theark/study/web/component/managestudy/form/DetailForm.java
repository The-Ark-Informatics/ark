/*******************************************************************************
 * Copyright (c) 2011  University of Western Australia. All rights reserved.
 * 
 * This file is part of The Ark.
 * 
 * The Ark is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 3
 * of the License, or (at your option) any later version.
 * 
 * The Ark is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
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
import org.apache.shiro.subject.Subject;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.ajax.form.AjaxFormValidatingBehavior;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.extensions.markup.html.form.DateTextField;
import org.apache.wicket.extensions.markup.html.form.palette.Palette;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.IChoiceRenderer;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.form.upload.FileUpload;
import org.apache.wicket.markup.html.form.upload.FileUploadField;
import org.apache.wicket.markup.html.image.NonCachingImage;
import org.apache.wicket.markup.html.image.resource.BlobImageResource;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import au.org.theark.core.dao.LobUtil;
import au.org.theark.core.exception.ArkSystemException;
import au.org.theark.core.exception.CannotRemoveArkModuleException;
import au.org.theark.core.exception.EntityCannotBeRemoved;
import au.org.theark.core.exception.EntityExistsException;
import au.org.theark.core.exception.EntityNotFoundException;
import au.org.theark.core.exception.UnAuthorizedOperation;
import au.org.theark.core.model.lims.entity.BioCollectionUidPadChar;
import au.org.theark.core.model.lims.entity.BioCollectionUidTemplate;
import au.org.theark.core.model.lims.entity.BioCollectionUidToken;
import au.org.theark.core.model.lims.entity.BiospecimenUidPadChar;
import au.org.theark.core.model.lims.entity.BiospecimenUidTemplate;
import au.org.theark.core.model.lims.entity.BiospecimenUidToken;
import au.org.theark.core.model.study.entity.ArkModule;
import au.org.theark.core.model.study.entity.Study;
import au.org.theark.core.model.study.entity.StudyStatus;
import au.org.theark.core.model.study.entity.SubjectUidPadChar;
import au.org.theark.core.model.study.entity.SubjectUidToken;
import au.org.theark.core.security.ArkPermissionHelper;
import au.org.theark.core.security.ModuleConstants;
import au.org.theark.core.service.IArkCommonService;
import au.org.theark.core.util.ContextHelper;
import au.org.theark.core.vo.ArkUserVO;
import au.org.theark.core.vo.ModuleVO;
import au.org.theark.core.vo.StudyCrudContainerVO;
import au.org.theark.core.vo.StudyModelVO;
import au.org.theark.core.vo.SubjectVO;
import au.org.theark.core.web.StudyHelper;
import au.org.theark.core.web.behavior.ArkDefaultFormFocusBehavior;
import au.org.theark.core.web.component.ArkDatePicker;
import au.org.theark.core.web.component.button.ArkBusyAjaxButton;
import au.org.theark.core.web.component.palette.ArkPalette;
import au.org.theark.core.web.form.AbstractArchiveDetailForm;
import au.org.theark.study.service.IStudyService;
import au.org.theark.study.web.Constants;
import au.org.theark.study.web.component.managestudy.StudyLogoValidator;

public class DetailForm extends AbstractArchiveDetailForm<StudyModelVO> {


	private static final long								serialVersionUID				= -9102470673205363789L;
	private static final Logger							log								= LoggerFactory.getLogger(DetailForm.class);

	@SpringBean(name = Constants.STUDY_SERVICE)
	private IStudyService									iStudyService;

	@SpringBean(name = au.org.theark.core.Constants.ARK_COMMON_SERVICE)
	private IArkCommonService<Void>						iArkCommonService;

	private int													mode;
	private Container											containerForm;
	private TextField<String>								studyIdTxtFld;
	private TextField<String>								studyNameTxtFld;
	private TextArea<String>								studyDescriptionTxtArea;
	private TextField<String>								estYearOfCompletionTxtFld;
	private TextField<String>								principalContactTxtFld;
	private TextField<String>								principalContactPhoneTxtFld;
	private TextField<String>								chiefInvestigatorTxtFld;
	private TextField<String>								coInvestigatorTxtFld;
	private TextField<String>								subjectUidPrefixTxtFld;
	private TextField<String>								subjectUidTokenTxtFld;
	private DropDownChoice<SubjectUidToken>			subjectUidTokenDpChoices;
	private DropDownChoice<SubjectUidPadChar>			subjectUidPadCharsDpChoices;
	private TextField<Integer>								subjectUidStartTxtFld;
	private Label												subjectUidExampleLbl;
	private DateTextField									dateOfApplicationDp;
	private DropDownChoice<StudyStatus>					studyStatusDpChoices;
	private CheckBox											autoGenSubIdChkBox;
	private CheckBox											autoConsentChkBox;

	private Palette<ArkModule>								arkModulePalette;
	private NonCachingImage									studyLogoImage;
	// Study logo uploader
	private FileUploadField									logoFileUploadField;

	@SpringBean(name = "lobUtil")
	private LobUtil			util;
	// Summary Details
	private Label												studySummaryLabel;
	private WebMarkupContainer								autoSubjectUidContainer;
	private WebMarkupContainer								subjectUidContainer;

	private String												subjectUidExampleTxt			= "";

	private transient StudyHelper							studyHelper;
	protected StudyCrudContainerVO						studyCrudVO;

	private WebMarkupContainer								biospecimenUidContainer;
	private TextField<String>								biospecimenUidPrefixTxtFld;
	private TextField<String>								biospecimentUidTokenTxtFld;
	private DropDownChoice<BiospecimenUidToken>		biospecimenUidTokenDdc;
	private DropDownChoice<BiospecimenUidPadChar>	biospecimenUidPadCharDdc;
	private Label												biospecimenUidExampleLbl;
	private String												biospecimenUidExampleTxt;
	
	private WebMarkupContainer								parentStudyContainer;
	private DropDownChoice<Study>							parentStudyDdc;

	private WebMarkupContainer								bioCollectionUidContainer;
	private TextField<String>								bioCollectionUidPrefixTxtFld;
	private TextField<String>								bioCollectionUidTokenTxtFld;
	private DropDownChoice<BioCollectionUidToken>	bioCollectionUidTokenDdc;
	private DropDownChoice<BioCollectionUidPadChar>	bioCollectionUidPadCharDdc;
	private Label												bioCollectionUidExampleLbl;
	private String												bioCollectionUidExampleTxt	= "";

	private WebMarkupContainer								subjectFileUploadContainer;
	private FileUploadField									subjectFileUploadField;
	private Label												totalSubjectsLabel;
	
	private ArkBusyAjaxButton								newChildStudyButton;

	/**
	 * Constructor
	 * 
	 * @param id
	 * @param crudVO
	 * @param feedbackPanel
	 * @param containerForm
	 */
	public DetailForm(String id, StudyCrudContainerVO crudVO, FeedbackPanel feedbackPanel, Container containerForm) {
		super(id, feedbackPanel, crudVO, containerForm);
		this.studyCrudVO = crudVO;
		this.containerForm = containerForm;
		AjaxFormValidatingBehavior.addToAllFormComponents(this, "onKeyup", Duration.seconds(2));
	}

	@Override
	public void onBeforeRender() {
		initStudyLogo();
		parentStudyContainer.setEnabled(isNew());
		super.onBeforeRender();
	}

	public void initialiseDetailForm() {
		studySummaryLabel = new Label("studySummaryLabel");
		studySummaryLabel.setOutputMarkupId(true);
		totalSubjectsLabel = new Label("totalSubjectsLabel");
		totalSubjectsLabel.setOutputMarkupId(true);

		studyIdTxtFld = new TextField<String>(Constants.STUDY_ID);
		studyIdTxtFld.setEnabled(false);

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

		// Create new DateTextField and assign date format
		dateOfApplicationDp = new DateTextField(Constants.STUDY_SEARCH_DOA, au.org.theark.core.Constants.DD_MM_YYYY);
		ArkDatePicker datePicker = new ArkDatePicker();
		datePicker.bind(dateOfApplicationDp);
		dateOfApplicationDp.add(datePicker);

		CompoundPropertyModel<StudyModelVO> studyCmpModel = (CompoundPropertyModel<StudyModelVO>) containerForm.getModel(); // details.getCpm();
		initStudyStatusDropDown(studyCmpModel);

		autoGenSubIdChkBox = new CheckBox(Constants.STUDY_AUTO_GENERATE_SUBJECTUID);
		autoGenSubIdChkBox.setVisible(true);

		autoGenSubIdChkBox.add(new AjaxFormComponentUpdatingBehavior("onChange") {

			private static final long	serialVersionUID	= 1L;

			@Override
			protected void onUpdate(AjaxRequestTarget target) {
				// Check what was selected and then toggle
				boolean autoGenerateSubjectUId = containerForm.getModelObject().getStudy().getAutoGenerateSubjectUid();
				subjectUidContainer.setEnabled(false);

				if (autoGenerateSubjectUId) {
					subjectUidContainer.setEnabled(true);
				}
				target.add(subjectUidContainer);
			}
		});
		autoGenSubIdChkBox.setOutputMarkupId(true);

		autoConsentChkBox = new CheckBox(Constants.STUDY_AUTO_CONSENT);
		autoConsentChkBox.setVisible(true);

		studyCrudVO.setStudyLogoUploadContainer(new WebMarkupContainer("studyLogoUploadContainer"));
		studyCrudVO.getStudyLogoUploadContainer().setOutputMarkupPlaceholderTag(true);

		// fileUpload for logo
		logoFileUploadField = new FileUploadField(Constants.STUDY_FILENAME);
		studyCrudVO.getStudyLogoUploadContainer().add(logoFileUploadField);

		initStudyLogo();

		initSubjectUid();
		initBioCollectionUid();
		initBiospecimenUid();
		initParentStudyDdc();

		initArkModulePalette();

		initSubjectFileUpload();
		
		newChildStudyButton = new ArkBusyAjaxButton("newChildStudy") {

			private static final long	serialVersionUID	= 1666656098281624401L;

			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
				// Make the details panel visible, disabling delete button (if found)
				// AjaxButton ajaxButton = (AjaxButton) editButtonContainer.get("delete");
				AjaxButton ajaxButton = (AjaxButton) studyCrudVO.getEditButtonContainer().get("delete");
				if (ajaxButton != null) {
					ajaxButton.setEnabled(false);
					target.add(ajaxButton);
				}
				
				StudyModelVO newVo = new StudyModelVO();
				Study study = newVo.getStudy();
				Study parentStudy = containerForm.getModelObject().getStudy();
				
				study.setId(null);
				study.setDescription("Child study of " + parentStudy.getName());
				study.setParentStudy(parentStudy);
				study.setName(null);
				
				// Modules of parent
				Collection<ArkModule> availableArkModules = containerForm.getModelObject().getAvailableArkModules();
				newVo.setAvailableArkModules(availableArkModules);
				
				Collection<ArkModule> selectedArkModules = containerForm.getModelObject().getSelectedArkModules();
				newVo.setSelectedArkModules(selectedArkModules);
				
				// Set auto-generated patterns to that of the parent study
				study.setAutoGenerateSubjectUid(parentStudy.getAutoGenerateSubjectUid());
				study.setSubjectUidPrefix(parentStudy.getSubjectUidPrefix());
				study.setSubjectUidToken(parentStudy.getSubjectUidToken());
				study.setSubjectUidPadChar(parentStudy.getSubjectUidPadChar());
				study.setSubjectUidStart(parentStudy.getSubjectUidStart());
				subjectUidExampleTxt = iArkCommonService.getSubjectUidExample(parentStudy);

				// Set BioCollection template
				BioCollectionUidTemplate parentBioCollectionUidTemplate = iArkCommonService.getBioCollectionUidTemplate(parentStudy);
				if (parentBioCollectionUidTemplate != null) {
					BioCollectionUidTemplate bioCollectionUidTemplate = new BioCollectionUidTemplate();
					bioCollectionUidTemplate.setBioCollectionUidPrefix(parentBioCollectionUidTemplate.getBioCollectionUidPrefix());
					bioCollectionUidTemplate.setBioCollectionUidToken(parentBioCollectionUidTemplate.getBioCollectionUidToken());
					bioCollectionUidTemplate.setBioCollectionUidPadChar(parentBioCollectionUidTemplate.getBioCollectionUidPadChar());
					bioCollectionUidTemplate.setStudy(study);
					containerForm.getModelObject().setBioCollectionUidTemplate(bioCollectionUidTemplate);
					bioCollectionUidExampleTxt = getBiocollectionUidExample();
				}
				else {
					containerForm.getModelObject().setBioCollectionUidTemplate(new BioCollectionUidTemplate());
					initBioCollectionUid();
				}

				// Set Biospecimen template
				BiospecimenUidTemplate parentBiospecimenUidTemplate = iArkCommonService.getBiospecimenUidTemplate(parentStudy);
				if (parentBiospecimenUidTemplate != null) {
					BiospecimenUidTemplate biospecimenUidTemplate = new BiospecimenUidTemplate();
					biospecimenUidTemplate.setBiospecimenUidPrefix(parentBiospecimenUidTemplate.getBiospecimenUidPrefix());
					biospecimenUidTemplate.setBiospecimenUidToken(parentBiospecimenUidTemplate.getBiospecimenUidToken());
					biospecimenUidTemplate.setBiospecimenUidPadChar(parentBiospecimenUidTemplate.getBiospecimenUidPadChar());
					biospecimenUidTemplate.setStudy(study);
					containerForm.getModelObject().setBiospecimenUidTemplate(biospecimenUidTemplate);
					biospecimenUidExampleTxt = getBiospecimenUidExample();
				}
				else {
					containerForm.getModelObject().setBiospecimenUidTemplate(new BiospecimenUidTemplate());
					initBiospecimenUid();
				}

				autoGenSubIdChkBox.setEnabled(false);
				subjectUidContainer.setEnabled(false);
				biospecimenUidContainer.setEnabled(false);
				bioCollectionUidContainer.setEnabled(false);
				
				containerForm.setDefaultModelObject(newVo);
				target.add(studyCrudVO.getDetailPanelFormContainer());
				
				// Hide Summary details on new
				studyCrudVO.getSummaryContainer().setVisible(false);
				target.add(studyCrudVO.getSummaryContainer());
				
				getAutoSubjectUidContainer().setEnabled(false);
				getSubjectUidContainer().setEnabled(false);
				bioCollectionUidContainer.setEnabled(false);
				biospecimenUidContainer.setEnabled(false);
				
				target.add(getAutoSubjectUidContainer());
				target.add(getSubjectUidContainer());
				target.add(bioCollectionUidContainer);
				target.add(biospecimenUidContainer);
				
				// hide new child study button
				this.setVisible(false);
				target.add(this);
			}

			@Override
			public boolean isVisible() {
				Study study = null;
				Study parentStudy = null;
				
				if(containerForm.getModelObject().getStudy() != null) {
					study = containerForm.getModelObject().getStudy();
					parentStudy= containerForm.getModelObject().getStudy().getParentStudy();
				}
				
				return (ArkPermissionHelper.isActionPermitted(Constants.NEW) && 
						!isNew() && 
						(parentStudy == null || study == parentStudy)
						);
			}

			@Override
			protected void onError(final AjaxRequestTarget target, Form<?> form) {
				target.add(feedBackPanel);
			}
		};

		attachValidators();
		addComponents();
	}

	private void initSubjectFileUpload() {
		subjectFileUploadContainer = new WebMarkupContainer("subjectFileUploadContainer");
		subjectFileUploadContainer.setOutputMarkupId(true);
		// fileUpload for subject linking
		subjectFileUploadField = new FileUploadField("subjectFileUpload");
	}

	private void initSubjectUid() {
		// Container for SubjectUID auto-generation details
		subjectUidContainer = new WebMarkupContainer("subjectUidContainer");
		subjectUidContainer.setOutputMarkupPlaceholderTag(true);

		// Label showing example auto-generated Subject UIDs
		subjectUidExampleTxt = iArkCommonService.getSubjectUidExample(containerForm.getModelObject().getStudy());
		if (subjectUidExampleTxt == null || subjectUidExampleTxt.length() == 0) {
			subjectUidExampleTxt = Constants.SUBJECTUID_EXAMPLE;
		}

		subjectUidExampleLbl = new Label("study.subjectUid.example", new PropertyModel<String>(this, "subjectUidExampleTxt"));
		subjectUidExampleLbl.setOutputMarkupId(true);
		subjectUidExampleLbl.setDefaultModelObject(containerForm.getModelObject().getSubjectUidExample());
		subjectUidExampleLbl.setVisible(true);

		// Subject UID prefix (e.g. three char representation of the Study name
		subjectUidPrefixTxtFld = new TextField<String>(au.org.theark.study.web.Constants.SUBJECT_UID_PREFIX);
		subjectUidPrefixTxtFld.add(new AjaxFormComponentUpdatingBehavior("onChange") {

			private static final long	serialVersionUID	= 1L;

			@Override
			protected void onUpdate(AjaxRequestTarget target) {
				subjectUidExampleTxt = getSubjectUidExample();
				target.add(subjectUidExampleLbl);
			}
		});

		// Token to separate the string (e.g. "-")
		subjectUidTokenTxtFld = new TextField<String>("subjectUidToken");
		subjectUidTokenTxtFld.add(new AjaxFormComponentUpdatingBehavior("onChange") {

			private static final long	serialVersionUID	= 1L;

			@Override
			protected void onUpdate(AjaxRequestTarget target) {
				subjectUidExampleTxt = getSubjectUidExample();
				target.add(subjectUidExampleLbl);
			}
		});
		subjectUidTokenTxtFld.setVisible(false);

		// Token selection
		initSubjectUidTokenDropDown();

		// How many padded chars in SubjectUID incrementor
		initSubjectUidPadCharsDropDown();

		// If the Study wishes to start the incrementor at a particular value
		subjectUidStartTxtFld = new TextField<Integer>(au.org.theark.study.web.Constants.SUBJECT_UID_START, Integer.class);
		subjectUidStartTxtFld.add(new AjaxFormComponentUpdatingBehavior("onChange") {

			private static final long	serialVersionUID	= 1L;

			@Override
			protected void onUpdate(AjaxRequestTarget target) {
				subjectUidExampleTxt = getSubjectUidExample();
				target.add(subjectUidExampleLbl);
			}
		});
	}

	private void initBioCollectionUid() {
		/* BioCollection UID Container and related controls */
		bioCollectionUidContainer = new WebMarkupContainer("bioCollectionUidContainer");
		bioCollectionUidContainer.setOutputMarkupPlaceholderTag(true);

		// Label showing example auto-generated BioCollection UIDs
		bioCollectionUidExampleTxt = getBiocollectionUidExample();
		if (bioCollectionUidExampleTxt == null || bioCollectionUidExampleTxt.length() == 0) {
			bioCollectionUidExampleTxt = Constants.BIOCOLLECTIONUID_EXAMPLE;
		}

		bioCollectionUidPrefixTxtFld = new TextField<String>("bioCollectionUidTemplate.bioCollectionUidPrefix");
		bioCollectionUidPrefixTxtFld.add(new AjaxFormComponentUpdatingBehavior("onChange") {

			private static final long	serialVersionUID	= 1L;

			@Override
			protected void onUpdate(AjaxRequestTarget target) {
				bioCollectionUidExampleTxt = getBiocollectionUidExample();
				target.add(bioCollectionUidExampleLbl);
			}
		});

		initBioCollectionUidTokenDropDown();
		initBioCollectionUidPadCharDdc();

		bioCollectionUidExampleLbl = new Label("study.bioCollectionUid.example", new PropertyModel<String>(this, "bioCollectionUidExampleTxt"));
		bioCollectionUidExampleLbl.setOutputMarkupId(true);
		bioCollectionUidExampleLbl.setDefaultModelObject(containerForm.getModelObject().getBioCollectionUidExample());
		bioCollectionUidExampleLbl.setVisible(true);
	}

	private void initBioCollectionUidTokenDropDown() {
		List<BioCollectionUidToken> bioCollectionUidToken = iArkCommonService.getBioCollectionUidToken();
		ChoiceRenderer<BioCollectionUidToken> choiceRenderer = new ChoiceRenderer<BioCollectionUidToken>(Constants.NAME, Constants.ID);
		bioCollectionUidTokenDdc = new DropDownChoice<BioCollectionUidToken>("bioCollectionUidTemplate.bioCollectionUidToken", bioCollectionUidToken, choiceRenderer);

		bioCollectionUidTokenDdc.add(new AjaxFormComponentUpdatingBehavior("onChange") {
			private static final long	serialVersionUID	= 1L;

			@Override
			protected void onUpdate(AjaxRequestTarget target) {

				bioCollectionUidExampleTxt = getBiocollectionUidExample();
				target.add(bioCollectionUidExampleLbl);
			}
		});
	}

	private void initBioCollectionUidPadCharDdc() {
		List<BioCollectionUidPadChar> bioCollectionUidPadChar = new ArrayList<BioCollectionUidPadChar>(0);
		bioCollectionUidPadChar = iArkCommonService.getBioCollectionUidPadChar();
		ChoiceRenderer<BioCollectionUidPadChar> choiceRenderer = new ChoiceRenderer<BioCollectionUidPadChar>(Constants.NAME, Constants.ID);
		bioCollectionUidPadCharDdc = new DropDownChoice<BioCollectionUidPadChar>("bioCollectionUidTemplate.bioCollectionUidPadChar", bioCollectionUidPadChar, choiceRenderer);

		bioCollectionUidPadCharDdc.add(new AjaxFormComponentUpdatingBehavior("onChange") {

			private static final long	serialVersionUID	= 1L;

			@Override
			protected void onUpdate(AjaxRequestTarget target) {
				bioCollectionUidExampleTxt = getBiocollectionUidExample();
				target.add(bioCollectionUidExampleLbl);
			}
		});
	}

	private void initBiospecimenUid() {
		// Label showing example auto-generated Biospecimen UIDs
		biospecimenUidExampleTxt = getBiospecimenUidExample();
		if (bioCollectionUidExampleTxt == null || bioCollectionUidExampleTxt.length() == 0) {
			bioCollectionUidExampleTxt = Constants.BIOSPECIMENUID_EXAMPLE;
		}

		biospecimenUidContainer = new WebMarkupContainer("biospecimenUidContainer");
		biospecimenUidContainer.setOutputMarkupPlaceholderTag(true);
		biospecimenUidPrefixTxtFld = new TextField<String>("biospecimenUidTemplate.biospecimenUidPrefix");

		biospecimenUidPrefixTxtFld.add(new AjaxFormComponentUpdatingBehavior("onChange") {


			private static final long	serialVersionUID	= 1L;

			protected void onUpdate(AjaxRequestTarget target) {
				biospecimenUidExampleTxt = getBiospecimenUidExample();
				target.add(biospecimenUidExampleLbl);
			}
		});

		// Token to separate the string (e.g. "-")
		biospecimentUidTokenTxtFld = new TextField<String>("biospecimenUidTemplate.biospecimenUidToken");
		biospecimentUidTokenTxtFld.add(new AjaxFormComponentUpdatingBehavior("onChange") {

			private static final long	serialVersionUID	= 1L;

			@Override
			protected void onUpdate(AjaxRequestTarget target) {
				biospecimenUidExampleTxt = getBiocollectionUidExample();
				target.add(biospecimenUidExampleLbl);
			}
		});

		biospecimenUidExampleLbl = new Label("biospecimenUid.example", new PropertyModel<String>(this, "biospecimenUidExampleTxt"));
		biospecimenUidExampleLbl.setOutputMarkupId(true);
		biospecimenUidExampleLbl.setVisible(true);

		initBiospecimenUidTokenDropDown();
		initBiospecimenUidPadCharDdc();
	}

	private void initBiospecimenUidTokenDropDown() {
		List<BiospecimenUidToken> biospecimenUidTokens = new ArrayList<BiospecimenUidToken>(0);
		biospecimenUidTokens = iArkCommonService.getBiospecimenUidTokens();
		ChoiceRenderer<BiospecimenUidToken> choiceRenderer = new ChoiceRenderer<BiospecimenUidToken>(Constants.NAME, Constants.ID);
		biospecimenUidTokenDdc = new DropDownChoice<BiospecimenUidToken>("biospecimenUidTemplate.biospecimenUidToken", biospecimenUidTokens, choiceRenderer);
		biospecimenUidTokenDdc.add(new AjaxFormComponentUpdatingBehavior("onChange") {

			private static final long	serialVersionUID	= 1L;

			@Override
			protected void onUpdate(AjaxRequestTarget target) {
				biospecimenUidExampleTxt = getBiospecimenUidExample();
				target.add(biospecimenUidExampleLbl);
			}
		});
	}

	private void initBiospecimenUidPadCharDdc() {
		List<BiospecimenUidPadChar> biospecimenUidPadChars = new ArrayList<BiospecimenUidPadChar>(0);
		biospecimenUidPadChars = iArkCommonService.getBiospecimenUidPadChars();
		ChoiceRenderer<BiospecimenUidPadChar> choiceRenderer = new ChoiceRenderer<BiospecimenUidPadChar>(Constants.NAME, Constants.ID);
		biospecimenUidPadCharDdc = new DropDownChoice<BiospecimenUidPadChar>("biospecimenUidTemplate.biospecimenUidPadChar", biospecimenUidPadChars, choiceRenderer);
		biospecimenUidPadCharDdc.add(new AjaxFormComponentUpdatingBehavior("onChange") {

			private static final long	serialVersionUID	= 1L;

			@Override
			protected void onUpdate(AjaxRequestTarget target) {
				biospecimenUidExampleTxt = getBiospecimenUidExample();
				target.add(biospecimenUidExampleLbl);
			}
		});
	}

	private void initParentStudyDdc() {
		List<Study> parentStudyListForUser = new ArrayList<Study>(0);
		ChoiceRenderer<Study> choiceRenderer = new ChoiceRenderer<Study>(Constants.NAME, Constants.ID);
		parentStudyDdc = new DropDownChoice<Study>("study.parentStudy", parentStudyListForUser, choiceRenderer) {

			private static final long	serialVersionUID	= 1L;

			@Override
			protected void onBeforeRender() {
				this.setChoices(getParentStudyList());
				super.onBeforeRender();
			}
		};

		/*
		 * If a parent study was selected, then reset the AutoGeneration of Subject UID and Biospecimen for this study and disable it in the front end
		 */
		parentStudyDdc.add(new AjaxFormComponentUpdatingBehavior("onChange") {
			private static final long	serialVersionUID	= 1L;

			@Override
			protected void onUpdate(AjaxRequestTarget target) {
				Study study = containerForm.getModelObject().getStudy();
				Study parentStudy = parentStudyDdc.getModelObject();

				if (parentStudy != null && (parentStudy.getId() != study.getId())) {
					parentStudy = iArkCommonService.getStudy(parentStudy.getId());

					// Set auto-generated patterns to that of the parent study
					study.setAutoGenerateSubjectUid(parentStudy.getAutoGenerateSubjectUid());
					study.setSubjectUidPrefix(parentStudy.getSubjectUidPrefix());
					study.setSubjectUidToken(parentStudy.getSubjectUidToken());
					study.setSubjectUidPadChar(parentStudy.getSubjectUidPadChar());
					study.setSubjectUidStart(parentStudy.getSubjectUidStart());
					subjectUidExampleTxt = iArkCommonService.getSubjectUidExample(parentStudy);

					// Set BioCollection template
					BioCollectionUidTemplate parentBioCollectionUidTemplate = iArkCommonService.getBioCollectionUidTemplate(parentStudy);
					if (parentBioCollectionUidTemplate != null) {
						BioCollectionUidTemplate bioCollectionUidTemplate = new BioCollectionUidTemplate();
						bioCollectionUidTemplate.setBioCollectionUidPrefix(parentBioCollectionUidTemplate.getBioCollectionUidPrefix());
						bioCollectionUidTemplate.setBioCollectionUidToken(parentBioCollectionUidTemplate.getBioCollectionUidToken());
						bioCollectionUidTemplate.setBioCollectionUidPadChar(parentBioCollectionUidTemplate.getBioCollectionUidPadChar());
						bioCollectionUidTemplate.setStudy(study);
						containerForm.getModelObject().setBioCollectionUidTemplate(bioCollectionUidTemplate);
						bioCollectionUidExampleTxt = getBiocollectionUidExample();
					}
					else {
						containerForm.getModelObject().setBioCollectionUidTemplate(new BioCollectionUidTemplate());
						initBioCollectionUid();
					}

					// Set Biospecimen template
					BiospecimenUidTemplate parentBiospecimenUidTemplate = iArkCommonService.getBiospecimenUidTemplate(parentStudy);
					if (parentBiospecimenUidTemplate != null) {
						BiospecimenUidTemplate biospecimenUidTemplate = new BiospecimenUidTemplate();
						biospecimenUidTemplate.setBiospecimenUidPrefix(parentBiospecimenUidTemplate.getBiospecimenUidPrefix());
						biospecimenUidTemplate.setBiospecimenUidToken(parentBiospecimenUidTemplate.getBiospecimenUidToken());
						biospecimenUidTemplate.setBiospecimenUidPadChar(parentBiospecimenUidTemplate.getBiospecimenUidPadChar());
						biospecimenUidTemplate.setStudy(study);
						containerForm.getModelObject().setBiospecimenUidTemplate(biospecimenUidTemplate);
						biospecimenUidExampleTxt = getBiospecimenUidExample();
					}
					else {
						containerForm.getModelObject().setBiospecimenUidTemplate(new BiospecimenUidTemplate());
						initBiospecimenUid();
					}

					autoGenSubIdChkBox.setEnabled(false);
					subjectUidContainer.setEnabled(false);
					biospecimenUidContainer.setEnabled(false);
					bioCollectionUidContainer.setEnabled(false);
				}
				else {
					// Reset to defaults if "Choose One" selected again
					containerForm.getModelObject().getStudy().setAutoGenerateSubjectUid(new Boolean("false"));
					containerForm.getModelObject().getStudy().setSubjectUidPrefix(new String(""));
					containerForm.getModelObject().getStudy().setSubjectUidToken(new SubjectUidToken());
					containerForm.getModelObject().getStudy().setSubjectUidPadChar(new SubjectUidPadChar());
					containerForm.getModelObject().getStudy().setSubjectUidStart(null);
					initSubjectUid();

					containerForm.getModelObject().setBioCollectionUidTemplate(new BioCollectionUidTemplate());
					initBioCollectionUid();

					containerForm.getModelObject().setBiospecimenUidTemplate(new BiospecimenUidTemplate());
					initBiospecimenUid();

					autoGenSubIdChkBox.setEnabled(true);
					subjectUidContainer.setEnabled(true);
					subjectUidStartTxtFld.setEnabled(true);

					biospecimenUidContainer.setEnabled(true);
					bioCollectionUidContainer.setEnabled(true);
					subjectUidExampleLbl.setDefaultModelObject(new String(""));
					bioCollectionUidExampleLbl.setDefaultModelObject(new String(""));
					biospecimenUidExampleLbl.setDefaultModelObject(new String(""));
				}

				subjectUidContainer.addOrReplace(subjectUidPrefixTxtFld);
				subjectUidContainer.addOrReplace(subjectUidTokenTxtFld);
				subjectUidContainer.addOrReplace(subjectUidTokenDpChoices);
				subjectUidContainer.addOrReplace(subjectUidPadCharsDpChoices);
				subjectUidContainer.addOrReplace(subjectUidStartTxtFld);
				subjectUidContainer.addOrReplace(subjectUidExampleLbl);
				studyCrudVO.getDetailPanelFormContainer().addOrReplace(subjectUidContainer);

				bioCollectionUidContainer.addOrReplace(bioCollectionUidTokenDdc);
				bioCollectionUidContainer.addOrReplace(bioCollectionUidPrefixTxtFld);
				bioCollectionUidContainer.addOrReplace(bioCollectionUidPadCharDdc);
				bioCollectionUidContainer.addOrReplace(bioCollectionUidExampleLbl);
				studyCrudVO.getDetailPanelFormContainer().addOrReplace(bioCollectionUidContainer);

				biospecimenUidContainer.addOrReplace(biospecimenUidTokenDdc);
				biospecimenUidContainer.addOrReplace(biospecimenUidPrefixTxtFld);
				biospecimenUidContainer.addOrReplace(biospecimenUidPadCharDdc);
				biospecimenUidContainer.addOrReplace(biospecimenUidExampleLbl);
				studyCrudVO.getDetailPanelFormContainer().addOrReplace(biospecimenUidContainer);

				// Repaint
				target.add(autoGenSubIdChkBox);
				target.add(subjectUidContainer);
				target.add(subjectUidTokenDpChoices);
				target.add(subjectUidExampleLbl);
				target.add(bioCollectionUidContainer);
				target.add(bioCollectionUidExampleLbl);
				target.add(biospecimenUidContainer);
				target.add(biospecimenUidExampleLbl);
			}
		});

		parentStudyContainer = new WebMarkupContainer("parentStudyContainer");
		parentStudyContainer.setOutputMarkupId(true);
	}

	private void initStudyLogo() {
		// Set maximum logo image size to 100K
		setMaxSize(Bytes.kilobytes(Constants.STUDY_LOGO_FILESIZE_KB));

		if (containerForm.getModelObject().getStudy() != null && containerForm.getModelObject().getStudy().getStudyLogoBlob() != null) {
			final java.sql.Blob studyLogoBlob = containerForm.getModelObject().getStudy().getStudyLogoBlob();

			if (studyLogoBlob != null) {
				BlobImageResource blobImageResource = new BlobImageResource() {
					private static final long	serialVersionUID	= 1L;

					@Override
					protected Blob getBlob() {
						return studyLogoBlob;
					}
				};

				studyLogoImage = new NonCachingImage("study.studyLogoImage", blobImageResource);
			}
		}
		else {
			studyLogoImage = new NonCachingImage("study.studyLogoImage", new Model<String>("images/no_study_logo.gif"));
		}

		// Fix to handle quirk in Wicket 1.5.1 where image is hidden when parent component is not enabled
		studyLogoImage.setVisible(studyCrudVO.getDetailPanelFormContainer().isEnabled());
		studyLogoImage.setOutputMarkupPlaceholderTag(true);
		studyCrudVO.getDetailPanelFormContainer().addOrReplace(studyLogoImage);
	}

	public String getSubjectUidExample() {
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

		if (study.getSubjectUidPadChar() != null) {
			subjectUidPadChar = study.getSubjectUidPadChar().getName().trim();
		}

		if (study.getSubjectUidStart() != null)
			subjectUidStart = study.getSubjectUidStart().toString();

		int size = Integer.parseInt(subjectUidPadChar);
		subjectUidPaddedIncrementor = StringUtils.leftPad(subjectUidStart, size, "0");
		subjectUidExample = subjectUidPrefix + subjectUidToken + subjectUidPaddedIncrementor;

		return subjectUidExample;
	}

	public String getBiospecimenUidExample() {
		BiospecimenUidTemplate template = containerForm.getModelObject().getBiospecimenUidTemplate();
		StringBuffer sb = new StringBuffer();
		String biospecimenUidPadChar = new String("0");
		String uidStart = new String();
		String uidExample = new String();

		if (template != null && template.getBiospecimenUidPrefix() != null) {
			sb.append(template.getBiospecimenUidPrefix());
		}

		if (template != null && template.getBiospecimenUidToken() != null) {
			sb.append(template.getBiospecimenUidToken().getName());
		}

		if (template != null && template.getBiospecimenUidPadChar() != null) {
			biospecimenUidPadChar = template.getBiospecimenUidPadChar().getName();
		}

		int size = Integer.parseInt(biospecimenUidPadChar);
		String paddedIncrementor = StringUtils.leftPad(uidStart, size, "0");
		uidExample = sb.append(paddedIncrementor).toString();

		return uidExample;
	}

	public String getBiocollectionUidExample() {
		BioCollectionUidTemplate template = containerForm.getModelObject().getBioCollectionUidTemplate();
		StringBuffer sb = new StringBuffer();
		String uidPadChar = new String("0");
		String uidStart = new String();
		String uidExample = new String();

		if (template != null && template.getBioCollectionUidPrefix() != null) {
			sb.append(template.getBioCollectionUidPrefix());
		}

		if (template != null && template.getBioCollectionUidToken() != null) {
			sb.append(template.getBioCollectionUidToken().getName());
		}

		if (template != null && template.getBioCollectionUidPadChar() != null) {
			uidPadChar = template.getBioCollectionUidPadChar().getName();
		}

		int size = Integer.parseInt(uidPadChar);
		String paddedIncrementor = StringUtils.leftPad(uidStart, size, "0");
		uidExample = sb.append(paddedIncrementor).toString();
		return uidExample;
	}

	@SuppressWarnings("unchecked")
	private void initArkModulePalette() {
		CompoundPropertyModel<StudyModelVO> sm = (CompoundPropertyModel<StudyModelVO>) containerForm.getModel();
		IChoiceRenderer<String> renderer = new ChoiceRenderer<String>("name", "name");
		PropertyModel<Collection<ArkModule>> selectedModPm = new PropertyModel<Collection<ArkModule>>(sm, "selectedArkModules");
		PropertyModel<Collection<ArkModule>> availableModulesPm = new PropertyModel<Collection<ArkModule>>(sm, "availableArkModules");
		arkModulePalette = new ArkPalette("selectedArkModules", selectedModPm, availableModulesPm, renderer, au.org.theark.study.web.Constants.PALETTE_ROWS, false);
	}

	private void initStudyStatusDropDown(CompoundPropertyModel<StudyModelVO> studyCmpModel) {
		List<StudyStatus> studyStatusList = iArkCommonService.getListOfStudyStatus();
		ChoiceRenderer<StudyStatus> defaultChoiceRenderer = new ChoiceRenderer<StudyStatus>(Constants.NAME, Constants.STUDY_STATUS_KEY);
		studyStatusDpChoices = new DropDownChoice<StudyStatus>(Constants.STUDY_STATUS, studyStatusList, defaultChoiceRenderer);
	}

	private void initSubjectUidTokenDropDown() {
		List<SubjectUidToken> subjectUidTokenList = iArkCommonService.getListOfSubjectUidToken();
		ChoiceRenderer<SubjectUidToken> defaultChoiceRenderer = new ChoiceRenderer<SubjectUidToken>(Constants.NAME, Constants.STUDY_STATUS_KEY);
		subjectUidTokenDpChoices = new DropDownChoice<SubjectUidToken>(au.org.theark.study.web.Constants.SUBJECT_UID_TOKEN, subjectUidTokenList, defaultChoiceRenderer);
		subjectUidTokenDpChoices.add(new AjaxFormComponentUpdatingBehavior("onChange") {

			private static final long	serialVersionUID	= 1L;

			@Override
			protected void onUpdate(AjaxRequestTarget target) {
				subjectUidExampleTxt = getSubjectUidExample();
				target.add(subjectUidExampleLbl);
			}
		});
	}

	private void initSubjectUidPadCharsDropDown() {
		List<SubjectUidPadChar> subjectUidPadCharList = iArkCommonService.getListOfSubjectUidPadChar();
		ChoiceRenderer<SubjectUidPadChar> defaultChoiceRenderer = new ChoiceRenderer<SubjectUidPadChar>(Constants.NAME, Constants.STUDY_STATUS_KEY);
		subjectUidPadCharsDpChoices = new DropDownChoice<SubjectUidPadChar>(au.org.theark.study.web.Constants.SUBJECT_UID_PADCHAR, subjectUidPadCharList, defaultChoiceRenderer);
		subjectUidPadCharsDpChoices.add(new AjaxFormComponentUpdatingBehavior("onChange") {

			private static final long	serialVersionUID	= 1L;

			@Override
			protected void onUpdate(AjaxRequestTarget target) {
				subjectUidExampleTxt = getSubjectUidExample();
				target.add(subjectUidExampleLbl);
			}
		});
	}

	private void addComponents() {
		studyCrudVO.getSummaryContainer().add(totalSubjectsLabel);

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
		subjectUidContainer.add(subjectUidPadCharsDpChoices);
		subjectUidContainer.add(subjectUidStartTxtFld);
		subjectUidContainer.add(subjectUidExampleLbl);

		studyCrudVO.getDetailPanelFormContainer().add(subjectUidContainer);
		parentStudyContainer.add(parentStudyDdc);
		studyCrudVO.getDetailPanelFormContainer().add(parentStudyContainer);
		// studyCrudVO.getDetailPanelFormContainer().add(studyDdc);
		// AutoGenerateSubjectUID needs own container to be disabled on certain criteria
		autoSubjectUidContainer.add(autoGenSubIdChkBox);
		studyCrudVO.getDetailPanelFormContainer().add(autoSubjectUidContainer);
		studyCrudVO.getDetailPanelFormContainer().add(autoConsentChkBox);
		studyCrudVO.getDetailPanelFormContainer().add(arkModulePalette);
		studyCrudVO.getDetailPanelFormContainer().add(studyCrudVO.getStudyLogoUploadContainer());
		studyCrudVO.getSummaryContainer().add(studySummaryLabel);

		biospecimenUidContainer.add(biospecimenUidTokenDdc);
		biospecimenUidContainer.add(biospecimenUidPrefixTxtFld);
		biospecimenUidContainer.add(biospecimenUidPadCharDdc);
		biospecimenUidContainer.add(biospecimenUidExampleLbl);
		// biospecimentUidContainer.add(biospecimentUidTokenTxtFld);

		studyCrudVO.getDetailPanelFormContainer().add(biospecimenUidContainer);

		bioCollectionUidContainer.add(bioCollectionUidTokenDdc);
		bioCollectionUidContainer.add(bioCollectionUidPrefixTxtFld);
		bioCollectionUidContainer.add(bioCollectionUidPadCharDdc);
		bioCollectionUidContainer.add(bioCollectionUidExampleLbl);
		// bioCollectionUidContainer.add()
		studyCrudVO.getDetailPanelFormContainer().add(bioCollectionUidContainer);

		subjectFileUploadContainer.add(subjectFileUploadField);
		studyCrudVO.getDetailPanelFormContainer().add(subjectFileUploadContainer);

		studyCrudVO.getEditButtonContainer().add(newChildStudyButton);
		
		add(studyCrudVO.getDetailPanelFormContainer());
		add(studyCrudVO.getSummaryContainer());
	}

	@Override
	protected void attachValidators() {
		studyNameTxtFld.setRequired(true).setLabel(new StringResourceModel("error.study.name.required", this, new Model<String>("Study Name")));
		// TODO Have to stop the validator posting the content with the error message
		studyDescriptionTxtArea.add(StringValidator.lengthBetween(1, 255)).setLabel(new StringResourceModel("study.description.length.exceeded", this, new Model<String>("Study Synopsis")));
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
		logoFileUploadField.add(new StudyLogoValidator());
		
		// Make parentStudy drop-down required for Study Administrators and study in context
		Subject currentUser = SecurityUtils.getSubject();
		Long studyId = (Long) SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.core.Constants.STUDY_CONTEXT_ID);
		if(studyId != null) {
			try {
				if(!iArkCommonService.isSuperAdministrator(currentUser.getPrincipal().toString())) {
					parentStudyDdc.setRequired(true).setLabel(new StringResourceModel("study.parentStudy.required", this, new Model<String>("Parent Study")));
				}
			}
			catch (EntityNotFoundException e) {
				log.error(e.getMessage());
			}
		}
	}

	private boolean validateEstYearOfComp(Long yearOfCompletion, Date dateOfApplication, String message, AjaxRequestTarget target) {
		boolean validFlag = true;
		SimpleDateFormat simpleDateformat = new SimpleDateFormat("yyyy");

		if (dateOfApplication != null) {
			int dateOfApplicationYear = Integer.parseInt(simpleDateformat.format(dateOfApplication));

			if (yearOfCompletion < dateOfApplicationYear) {
				this.error(message);
				processErrors(target);
				validFlag = false;
			}
		}

		return validFlag;
	}

	@Override
	protected void onSave(Form<StudyModelVO> containerForm, AjaxRequestTarget target) {
		try {
			String message = "Estimated Year of Completion must be greater than Date Of Application";

			boolean customValidationFlag = false;

			if (containerForm.getModelObject().getStudy().getEstimatedYearOfCompletion() != null) {
				customValidationFlag = validateEstYearOfComp(containerForm.getModelObject().getStudy().getEstimatedYearOfCompletion(), containerForm.getModelObject().getStudy().getDateOfApplication(),
						message, target);
			}

			// Store Study logo image
			if (logoFileUploadField != null && logoFileUploadField.getFileUpload() != null) {
				// Retrieve file and store as Blob in databasse
				FileUpload fileUpload = logoFileUploadField.getFileUpload();

				// Copy file to Blob object
				Blob payload = util.createBlob(fileUpload.getInputStream(), fileUpload.getSize());
				containerForm.getModelObject().getStudy().setStudyLogoBlob(payload);
				containerForm.getModelObject().getStudy().setFilename(fileUpload.getClientFileName());
			}

			if (subjectFileUploadField != null && subjectFileUploadField.getFileUpload() != null) {
				FileUpload subjectFileUpload = subjectFileUploadField.getFileUpload();
				Collection<SubjectVO> selectedSubjects = iArkCommonService.matchSubjectsFromInputFile(subjectFileUpload, containerForm.getModelObject().getStudy().getParentStudy());
				// Link subjects in file to the selected study (where matched)
				containerForm.getModelObject().setSelectedSubjects(selectedSubjects);
			}

			if (containerForm.getModelObject().getStudy().getEstimatedYearOfCompletion() != null && customValidationFlag) {
				// If there was a value provided then upon successful validation proceed with save or update
				processSaveUpdate(containerForm.getModelObject(), target);
			}
			else if (containerForm.getModelObject().getStudy().getEstimatedYearOfCompletion() == null) {
				// If the value for estimate year of completion was not provided then we can proceed with save or update.
				processSaveUpdate(containerForm.getModelObject(), target);
			}
		}
		catch (EntityExistsException e) {
			this.error("The specified study already exists in the system.");
			log.error(e.getMessage());
		}
		catch (EntityCannotBeRemoved e) {
			this.error("The Study cannot be removed from the system.There are participants linked to the study");
			log.error(e.getMessage());
		}
		catch (IOException e) {
			this.error("There was an error transferring the specified Study logo image.");
			log.error(e.getMessage());
		}
		catch (ArkSystemException e) {
			this.error("A System exception has occurred. Please contact Support");
			log.error(e.getMessage());
		}
		catch (UnAuthorizedOperation e) {
			this.error("You (logged in user) is unauthorised to create/update or archive this study.");
			log.error(e.getMessage());
		}
		catch (CannotRemoveArkModuleException e) {
			this.error("You cannot remove the modules as part of the update. There are System Users who are associated with this study and modules.");
			log.error(e.getMessage());
		}
	}

	private void processSaveUpdate(StudyModelVO studyModel, AjaxRequestTarget target) throws EntityExistsException, UnAuthorizedOperation, ArkSystemException, EntityCannotBeRemoved,
			CannotRemoveArkModuleException {
		Collection<ModuleVO> moduleVoCollection = studyModel.getModulesSelected();
		// Convert to Set<String> this can be removed later by changing the interface
		Set<String> moduleList = new HashSet<String>();
		for (ModuleVO moduleVO : moduleVoCollection) {
			moduleList.add(moduleVO.getModule());
		}
		studyModel.setLmcSelectedApps(moduleList);

		if (studyModel.getStudy() != null && studyModel.getStudy().getId() == null) {
			Set<String> moduleNames = new HashSet<String>();
			for (ArkModule arkModule : studyModel.getSelectedArkModules()) {
				moduleNames.add(arkModule.getName());
			}
			// Always assign Study module
			if (moduleNames.isEmpty()) {
				moduleNames.add(ModuleConstants.ARK_MODULE_STUDY);
			}

			ArkUserVO arkUserVo = new ArkUserVO();
			String currentUser = (String) SecurityUtils.getSubject().getPrincipal();
			arkUserVo = iArkCommonService.getDefaultAdministratorRoles(currentUser, studyModel.getStudy(), moduleNames);
			if (!arkUserVo.getUserName().isEmpty()) {
				// Create new study and assign the current user
				iStudyService.createStudy(studyModel, arkUserVo);
				this.info("Study: " + studyModel.getStudy().getName().toUpperCase() + " has been saved with default Administrator roles to the modules selected.");
			}
			else {
				// Create new study by default, without assignment of user
				iStudyService.createStudy(studyModel);
				this.info("Study: " + studyModel.getStudy().getName().toUpperCase() + " has been saved.");
			}

			subjectUidExampleTxt = getSubjectUidExample();
			target.add(subjectUidExampleLbl);

			onSavePostProcess(target, studyCrudVO);
			studyCrudVO.getSummaryContainer().setVisible(true);// added as part of refactoring
		}
		else {
			// Update
			iStudyService.updateStudy(studyModel);
			subjectUidExampleTxt = getSubjectUidExample();
			target.add(subjectUidExampleLbl);

			this.info("Update of Study: " + studyModel.getStudy().getName().toUpperCase() + " was successful.");
			onSavePostProcess(target, studyCrudVO);
			studyCrudVO.getSummaryContainer().setVisible(true);
		}

		SecurityUtils.getSubject().getSession().setAttribute(au.org.theark.core.Constants.STUDY_CONTEXT_ID, studyModel.getStudy().getId());
		SecurityUtils.getSubject().getSession().removeAttribute(au.org.theark.core.Constants.PERSON_CONTEXT_ID);
		SecurityUtils.getSubject().getSession().removeAttribute(au.org.theark.core.Constants.PERSON_TYPE);
		containerForm.getModelObject().setStudy(studyModel.getStudy());

		// Set Study into context items
		ContextHelper contextHelper = new ContextHelper();
		contextHelper.resetContextLabel(target, studyCrudVO.getArkContextMarkup());
		contextHelper.setStudyContextLabel(target, studyModel.getStudy().getName(), studyCrudVO.getArkContextMarkup());

		// Refresh Study Logo in header
		studyHelper = new StudyHelper();
		studyHelper.setStudyLogo(studyModel.getStudy(), target, studyCrudVO.getStudyNameMarkup(), studyCrudVO.getStudyLogoMarkup());

		target.add(studyCrudVO.getDetailPanelContainer());
		target.add(studyCrudVO.getStudyLogoMarkup());

		// Update summary
		long totalSubjects = iArkCommonService.getCountOfSubjects(studyModel.getStudy());
		long totalSubjectsOfParent = iArkCommonService.getCountOfSubjects(studyModel.getStudy().getParentStudy());

		studyModel.setTotalSubjects(totalSubjects);
		studyModel.setTotalSubjectsOfParent(totalSubjectsOfParent);
		target.add(totalSubjectsLabel);

		target.add(subjectUidContainer);
		target.add(bioCollectionUidContainer);
		target.add(biospecimenUidContainer);
	}

	protected void onCancel(AjaxRequestTarget target) {
		containerForm.setModelObject(new StudyModelVO());
		studyCrudVO.getSummaryContainer().setVisible(true);
	}

	@Override
	protected void processErrors(AjaxRequestTarget target) {
		target.add(feedBackPanel);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see au.org.theark.core.web.form.AbstractArchiveDetailForm#isNew()
	 */
	@Override
	protected boolean isNew() {
		if (containerForm.getModelObject().getStudy().getId() == null) {
			return true;
		}
		else {
			return false;
		}
	}

	/**
	 * @return the studyLogoImage
	 */
	public NonCachingImage getStudyLogoImage() {
		return studyLogoImage;
	}

	private List<Study> getParentStudyList() {
		List<Study> parentStudyList = iArkCommonService.getParentStudyList();
		return parentStudyList;
	}

	public WebMarkupContainer getSubjectUidContainer() {
		return subjectUidContainer;
	}

	public WebMarkupContainer getAutoSubjectUidContainer() {
		return autoSubjectUidContainer;
	}

	public Label getSubjectUidExampleLbl() {
		return subjectUidExampleLbl;
	}

	public int getMode() {
		return mode;
	}

	public void setMode(int mode) {
		this.mode = mode;
	}

	public String getBiospecimenUidExampleTxt() {
		return biospecimenUidExampleTxt;
	}

	public void setBiospecimenUidExampleTxt(String biospecimenUidExampleTxt) {
		this.biospecimenUidExampleTxt = biospecimenUidExampleTxt;
	}

	public TextField<String> getBioCollectionUidTokenTxtFld() {
		return bioCollectionUidTokenTxtFld;
	}

	public void setBioCollectionUidTokenTxtFld(TextField<String> bioCollectionUidTokenTxtFld) {
		this.bioCollectionUidTokenTxtFld = bioCollectionUidTokenTxtFld;
	}

	public WebMarkupContainer getBiospecimenUidContainer() {
		return biospecimenUidContainer;
	}

	public void setBiospecimenUidContainer(WebMarkupContainer biospecimenUidContainer) {
		this.biospecimenUidContainer = biospecimenUidContainer;
	}

	public WebMarkupContainer getBioCollectionUidContainer() {
		return bioCollectionUidContainer;
	}

	public void setBioCollectionUidContainer(WebMarkupContainer bioCollectionUidContainer) {
		this.bioCollectionUidContainer = bioCollectionUidContainer;
	}

	/**
	 * @return the subjectFileUploadContainer
	 */
	public WebMarkupContainer getSubjectFileUploadContainer() {
		return subjectFileUploadContainer;
	}

	/**
	 * @param subjectFileUploadContainer
	 *           the subjectFileUploadContainer to set
	 */
	public void setSubjectFileUploadContainer(WebMarkupContainer subjectFileUploadContainer) {
		this.subjectFileUploadContainer = subjectFileUploadContainer;
	}

	public WebMarkupContainer getLinkedToStudyDDContainer() {
		return parentStudyContainer;
	}

	public void setLinkedToStudyDDContainer(WebMarkupContainer linkedToStudyDDContainer) {
		this.parentStudyContainer = linkedToStudyDDContainer;
	}
}
