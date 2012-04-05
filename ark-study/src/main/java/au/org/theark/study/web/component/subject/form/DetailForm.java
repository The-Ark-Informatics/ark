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
package au.org.theark.study.web.component.subject.form;

import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.apache.shiro.SecurityUtils;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.extensions.markup.html.form.DateTextField;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.validation.validator.DateValidator;
import org.apache.wicket.validation.validator.EmailAddressValidator;

import au.org.theark.core.exception.ArkSubjectInsertException;
import au.org.theark.core.exception.ArkUniqueException;
import au.org.theark.core.model.audit.entity.LssConsentHistory;
import au.org.theark.core.model.study.entity.ConsentOption;
import au.org.theark.core.model.study.entity.ConsentStatus;
import au.org.theark.core.model.study.entity.ConsentType;
import au.org.theark.core.model.study.entity.GenderType;
import au.org.theark.core.model.study.entity.MaritalStatus;
import au.org.theark.core.model.study.entity.PersonContactMethod;
import au.org.theark.core.model.study.entity.Study;
import au.org.theark.core.model.study.entity.SubjectStatus;
import au.org.theark.core.model.study.entity.TitleType;
import au.org.theark.core.model.study.entity.VitalStatus;
import au.org.theark.core.model.study.entity.YesNo;
import au.org.theark.core.service.IArkCommonService;
import au.org.theark.core.util.ContextHelper;
import au.org.theark.core.vo.ArkCrudContainerVO;
import au.org.theark.core.vo.SubjectVO;
import au.org.theark.core.web.behavior.ArkDefaultFormFocusBehavior;
import au.org.theark.core.web.component.ArkDatePicker;
import au.org.theark.core.web.component.panel.collapsiblepanel.CollapsiblePanel;
import au.org.theark.core.web.form.AbstractDetailForm;
import au.org.theark.study.service.IStudyService;
import au.org.theark.study.web.Constants;
import au.org.theark.study.web.component.consenthistory.LinkSubjectStudyConsentHistoryPanel;
import au.org.theark.study.web.component.subject.ChildStudyPalettePanel;

/**
 * @author nivedann
 * 
 */
public class DetailForm extends AbstractDetailForm<SubjectVO> {

	/**
	 * 
	 */
	private static final long								serialVersionUID	= -9196914684971413116L;

	@SpringBean(name = Constants.STUDY_SERVICE)
	private IStudyService									studyService;

	@SuppressWarnings("unchecked")
	@SpringBean(name = au.org.theark.core.Constants.ARK_COMMON_SERVICE)
	private IArkCommonService								iArkCommonService;

	private WebMarkupContainer								arkContextMarkupContainer;

	protected TextField<String>							subjectUIDTxtFld;
	protected TextField<String>							firstNameTxtFld;
	protected TextField<String>							middleNameTxtFld;
	protected TextField<String>							lastNameTxtFld;
	protected TextField<String>							previousLastNameTxtFld;
	protected TextField<String>							preferredNameTxtFld;

	protected DateTextField									dateOfBirthTxtFld;
	protected DateTextField									dateOfDeathTxtFld;
	protected DateTextField									dateLastKnownAliveTxtFld;
	protected TextField<String>							causeOfDeathTxtFld;
	protected TextArea<String>								commentTxtAreaFld;
	protected TextField<String>							heardAboutStudyTxtFld;
	protected DropDownChoice<YesNo>						consentDownloadedChoice;

	// Consents at Subject Study Level
	protected DropDownChoice<ConsentOption>			consentToActiveContactDdc;
	protected DropDownChoice<ConsentOption>			consentToUseDataDdc;
	protected DropDownChoice<ConsentOption>			consentToPassDataGatheringDdc;

	// Address Stuff comes here
	protected TextField<String>							preferredEmailTxtFld;
	protected TextField<String>							otherEmailTxtFld;

	// Reference Data
	protected DropDownChoice<TitleType>					titleTypeDdc;
	protected DropDownChoice<VitalStatus>				vitalStatusDdc;
	protected DropDownChoice<GenderType>				genderTypeDdc;
	protected DropDownChoice<SubjectStatus>			subjectStatusDdc;
	protected DropDownChoice<MaritalStatus>			maritalStatusDdc;
	protected DropDownChoice<PersonContactMethod>	personContactMethodDdc;

	// Study Level Consent Controls
	protected DropDownChoice<ConsentStatus>			consentStatusChoice;
	protected DropDownChoice<ConsentType>				consentTypeChoice;
	protected DateTextField									consentDateTxtFld;
	protected CollapsiblePanel								consentHistoryPanel;

	// Webmarkup for Ajax refreshing of items based on particular criteria
	protected WebMarkupContainer							wmcPreferredEmailContainer;
	protected WebMarkupContainer							wmcDeathDetailsContainer;

	protected ChildStudyPalettePanel<SubjectVO>		childStudyPalettePanel;

	protected Study											study;

	public DetailForm(String id, FeedbackPanel feedBackPanel, WebMarkupContainer arkContextContainer, ContainerForm containerForm, ArkCrudContainerVO arkCrudContainerVO) {

		super(id, feedBackPanel, containerForm, arkCrudContainerVO);
		this.arkContextMarkupContainer = arkContextContainer;
	}

	@Override
	public void onBeforeRender() {
		childStudyPalettePanel = new ChildStudyPalettePanel<SubjectVO>("childStudyPalette", containerForm.getModel());
		arkCrudContainerVO.getDetailPanelFormContainer().addOrReplace(childStudyPalettePanel);
		super.onBeforeRender();
	}

	@SuppressWarnings("unchecked")
	public void initialiseDetailForm() {
		subjectUIDTxtFld = new TextField<String>(Constants.SUBJECT_UID) {
			/**
			 * 
			 */
			private static final long	serialVersionUID	= 1L;

			@Override
			protected void onBeforeRender() {
				boolean isNew = isNew();
				boolean autoGenerate = containerForm.getModelObject().getLinkSubjectStudy().getStudy().getAutoGenerateSubjectUid();
				if (isNew && !autoGenerate) {
					setEnabled(true);
				}
				else {
					setEnabled(false);
				}
				super.onBeforeRender();
			}
		};
		subjectUIDTxtFld.setOutputMarkupId(true);

		firstNameTxtFld = new TextField<String>(Constants.PERSON_FIRST_NAME);
		middleNameTxtFld = new TextField<String>(Constants.PERSON_MIDDLE_NAME);
		lastNameTxtFld = new TextField<String>(Constants.PERSON_LAST_NAME);
		previousLastNameTxtFld = new TextField<String>(Constants.SUBJECT_PREVIOUS_LAST_NAME) {
			/**
			 * 
			 */
			private static final long	serialVersionUID	= 1L;

			@Override
			protected void onBeforeRender() {
				setEnabled(isNew());
				super.onBeforeRender();
			}
		};
		preferredNameTxtFld = new TextField<String>(Constants.PERSON_PREFERRED_NAME);

		preferredEmailTxtFld = new TextField<String>(Constants.PERSON_PREFERRED_EMAIL);
		otherEmailTxtFld = new TextField<String>(Constants.PERSON_OTHER_EMAIL);

		heardAboutStudyTxtFld = new TextField<String>(Constants.SUBJECT_HEARD_ABOUT_STUDY_FROM);
		dateOfBirthTxtFld = new DateTextField(Constants.PERSON_DOB, au.org.theark.core.Constants.DD_MM_YYYY);
		ArkDatePicker dobDatePicker = new ArkDatePicker();
		dobDatePicker.bind(dateOfBirthTxtFld);
		dateOfBirthTxtFld.add(dobDatePicker);

		dateLastKnownAliveTxtFld = new DateTextField("linkSubjectStudy.person.dateLastKnownAlive", au.org.theark.core.Constants.DD_MM_YYYY);
		ArkDatePicker dateLastKnownAlivePicker = new ArkDatePicker();
		dateLastKnownAlivePicker.bind(dateLastKnownAliveTxtFld);
		dateLastKnownAliveTxtFld.add(dateLastKnownAlivePicker);

		dateOfDeathTxtFld = new DateTextField(Constants.PERSON_DOD, au.org.theark.core.Constants.DD_MM_YYYY);
		causeOfDeathTxtFld = new TextField<String>(Constants.PERSON_CAUSE_OF_DEATH);
		ArkDatePicker dodDatePicker = new ArkDatePicker();
		dodDatePicker.bind(dateOfDeathTxtFld);
		dateOfDeathTxtFld.add(dodDatePicker);

		commentTxtAreaFld = new TextArea<String>(Constants.PERSON_COMMENT);

		wmcDeathDetailsContainer = new WebMarkupContainer("deathDetailsContainer");
		wmcDeathDetailsContainer.setOutputMarkupId(true);

		// Default death details to disabled (enable onChange of vitalStatus)
		setDeathDetailsContainer();

		// Initialise Drop Down Choices
		// We can also have the reference data populated on Application start
		// and refer to a static list instead of hitting the database

		// Title
		Collection<TitleType> titleTypeList = iArkCommonService.getTitleType();
		ChoiceRenderer<TitleType> defaultChoiceRenderer = new ChoiceRenderer<TitleType>(Constants.NAME, Constants.ID);
		titleTypeDdc = new DropDownChoice<TitleType>(Constants.PERSON_TYTPE_TYPE, (List) titleTypeList, defaultChoiceRenderer);
		titleTypeDdc.add(new ArkDefaultFormFocusBehavior());

		// Vital Status
		Collection<VitalStatus> vitalStatusList = iArkCommonService.getVitalStatus();
		ChoiceRenderer<VitalStatus> vitalStatusRenderer = new ChoiceRenderer<VitalStatus>(Constants.NAME, Constants.ID);
		vitalStatusDdc = new DropDownChoice<VitalStatus>(Constants.PERSON_VITAL_STATUS, (List<VitalStatus>) vitalStatusList, vitalStatusRenderer);
		vitalStatusDdc.add(new AjaxFormComponentUpdatingBehavior("onchange") {
			/**
			 * 
			 */
			private static final long	serialVersionUID	= 1L;

			@Override
			protected void onUpdate(AjaxRequestTarget target) {
				setDeathDetailsContainer();
				target.add(wmcDeathDetailsContainer);
			}
		});

		// Gender Type
		Collection<GenderType> genderTypeList = iArkCommonService.getGenderTypes();
		ChoiceRenderer<GenderType> genderTypeRenderer = new ChoiceRenderer<GenderType>(Constants.NAME, Constants.ID);
		genderTypeDdc = new DropDownChoice<GenderType>(Constants.PERSON_GENDER_TYPE, (List<GenderType>) genderTypeList, genderTypeRenderer);

		// Subject Status
		List<SubjectStatus> subjectStatusList = iArkCommonService.getSubjectStatus();
		ChoiceRenderer<SubjectStatus> subjectStatusRenderer = new ChoiceRenderer<SubjectStatus>(Constants.NAME, Constants.SUBJECT_STATUS_ID);
		subjectStatusDdc = new DropDownChoice<SubjectStatus>(Constants.SUBJECT_STATUS, subjectStatusList, subjectStatusRenderer);

		// Marital Status
		Collection<MaritalStatus> maritalStatusList = iArkCommonService.getMaritalStatus();
		ChoiceRenderer<MaritalStatus> maritalStatusRender = new ChoiceRenderer<MaritalStatus>(Constants.NAME, Constants.ID);
		maritalStatusDdc = new DropDownChoice<MaritalStatus>(Constants.PERSON_MARITAL_STATUS, (List) maritalStatusList, maritalStatusRender);

		// Container for preferredEmail (required when Email selected as preferred contact)
		wmcPreferredEmailContainer = new WebMarkupContainer("preferredEmailContainer");
		wmcPreferredEmailContainer.setOutputMarkupPlaceholderTag(true);
		// Depends on preferredContactMethod
		setPreferredEmailContainer();

		// Person Contact Method
		List<PersonContactMethod> contactMethodList = iArkCommonService.getPersonContactMethodList();
		ChoiceRenderer<PersonContactMethod> contactMethodRender = new ChoiceRenderer<PersonContactMethod>(Constants.NAME, Constants.ID);
		personContactMethodDdc = new DropDownChoice<PersonContactMethod>(Constants.PERSON_CONTACT_METHOD, (List<PersonContactMethod>) contactMethodList, contactMethodRender);
		personContactMethodDdc.add(new AjaxFormComponentUpdatingBehavior("onchange") {
			/**
			 * 
			 */
			private static final long	serialVersionUID	= 1L;

			@Override
			protected void onUpdate(AjaxRequestTarget target) {
				// Check what was selected and then toggle
				setPreferredEmailContainer();
				target.add(wmcPreferredEmailContainer);
			}
		});

		initConsentFields();

		attachValidators();
		addDetailFormComponents();

		deleteButton.setVisible(false);
	}

	/**
	 * Initialise the Consent Status Drop Down Choice Control
	 */
	@SuppressWarnings("unchecked")
	protected void initialiseConsentStatusChoice() {
		List<ConsentStatus> consentStatusList = iArkCommonService.getRecordableConsentStatus();
		ChoiceRenderer<ConsentStatus> defaultChoiceRenderer = new ChoiceRenderer<ConsentStatus>(Constants.NAME, Constants.ID);
		consentStatusChoice = new DropDownChoice<ConsentStatus>(Constants.SUBJECT_CONSENT_STATUS, (List<ConsentStatus>) consentStatusList, defaultChoiceRenderer);
	}

	@SuppressWarnings("unchecked")
	protected void initialiseConsentTypeChoice() {
		List<ConsentType> consentTypeList = iArkCommonService.getConsentType();
		ChoiceRenderer defaultChoiceRenderer = new ChoiceRenderer(Constants.NAME, Constants.ID);
		consentTypeChoice = new DropDownChoice<ConsentType>(Constants.SUBJECT_CONSENT_TYPE, (List<ConsentType>) consentTypeList, defaultChoiceRenderer);
	}

	// Death details dependent on Vital Status selected to "Deceased"
	private void setDeathDetailsContainer() {
		VitalStatus vitalStatus = containerForm.getModelObject().getLinkSubjectStudy().getPerson().getVitalStatus();
		if (vitalStatus != null) {
			String vitalStatusName = vitalStatus.getName();

			if (vitalStatusName.equalsIgnoreCase("DECEASED")) {
				wmcDeathDetailsContainer.setEnabled(true);
			}
			else {
				wmcDeathDetailsContainer.setEnabled(false);
				;
			}
		}
		else {
			wmcDeathDetailsContainer.setEnabled(false);
		}
	}

	// Email required when preferred contact set to "Email"
	private void setPreferredEmailContainer() {
		PersonContactMethod personContactMethod = containerForm.getModelObject().getLinkSubjectStudy().getPerson().getPersonContactMethod();

		if (personContactMethod != null) {
			String personContactMethodName = personContactMethod.getName();
			if (personContactMethodName.equalsIgnoreCase("EMAIL")) {
				preferredEmailTxtFld.setRequired(true).setLabel(new StringResourceModel("subject.preferredEmail.required", null));
			}
			else {
				preferredEmailTxtFld.setRequired(false);

			}
		}
	}

	@SuppressWarnings("unchecked")
	private void initConsentFields() {
		consentDateTxtFld = new DateTextField(Constants.PERSON_CONSENT_DATE, au.org.theark.core.Constants.DD_MM_YYYY);
		ArkDatePicker consentDatePicker = new ArkDatePicker();
		consentDatePicker.bind(consentDateTxtFld);
		consentDateTxtFld.add(consentDatePicker);

		List<YesNo> yesNoListSource = iArkCommonService.getYesNoList();
		ChoiceRenderer<YesNo> yesNoRenderer = new ChoiceRenderer<YesNo>(Constants.NAME, Constants.ID);
		consentDownloadedChoice = new DropDownChoice<YesNo>(Constants.PERSON_CONSENT_DOWNLOADED, yesNoListSource, yesNoRenderer);

		List<ConsentOption> consentOptionList = iArkCommonService.getConsentOptionList();
		ChoiceRenderer<ConsentOption> consentOptionRenderer = new ChoiceRenderer<ConsentOption>(Constants.NAME, Constants.ID);

		consentToActiveContactDdc = new DropDownChoice<ConsentOption>(Constants.SUBJECT_CONSENT_TO_ACTIVE_CONTACT, (List) consentOptionList, consentOptionRenderer);
		consentToUseDataDdc = new DropDownChoice<ConsentOption>(Constants.SUBJECT_CONSENT_TO_USEDATA, (List) consentOptionList, consentOptionRenderer);
		consentToPassDataGatheringDdc = new DropDownChoice<ConsentOption>(Constants.SUBJECT_CONSENT_PASSIVE_DATA_GATHER, (List) consentOptionList, consentOptionRenderer);

		initialiseConsentStatusChoice();
		initialiseConsentTypeChoice();

		consentHistoryPanel = new CollapsiblePanel("consentHistoryPanel", new Model<String>("Consent History"), false) {

			/**
			 * 
			 */
			private static final long	serialVersionUID	= 1L;

			@Override
			protected Panel getInnerPanel(String markupId) {
				LinkSubjectStudyConsentHistoryPanel consentHistoryPanel = new LinkSubjectStudyConsentHistoryPanel(markupId, new CompoundPropertyModel<LssConsentHistory>(new LssConsentHistory()));
				return consentHistoryPanel;
			}
		};
	}

	public void addDetailFormComponents() {

		arkCrudContainerVO.getDetailPanelFormContainer().add(subjectUIDTxtFld);
		arkCrudContainerVO.getDetailPanelFormContainer().add(titleTypeDdc);
		arkCrudContainerVO.getDetailPanelFormContainer().add(firstNameTxtFld);
		arkCrudContainerVO.getDetailPanelFormContainer().add(middleNameTxtFld);
		arkCrudContainerVO.getDetailPanelFormContainer().add(lastNameTxtFld);
		arkCrudContainerVO.getDetailPanelFormContainer().add(previousLastNameTxtFld);
		arkCrudContainerVO.getDetailPanelFormContainer().add(preferredNameTxtFld);
		arkCrudContainerVO.getDetailPanelFormContainer().add(dateOfBirthTxtFld);
		arkCrudContainerVO.getDetailPanelFormContainer().add(dateLastKnownAliveTxtFld);
		arkCrudContainerVO.getDetailPanelFormContainer().add(commentTxtAreaFld);
		arkCrudContainerVO.getDetailPanelFormContainer().add(heardAboutStudyTxtFld);
		arkCrudContainerVO.getDetailPanelFormContainer().add(vitalStatusDdc);

		// Death details only be edited when vital status set to deceased
		wmcDeathDetailsContainer.add(dateOfDeathTxtFld);
		wmcDeathDetailsContainer.add(causeOfDeathTxtFld);
		arkCrudContainerVO.getDetailPanelFormContainer().add(wmcDeathDetailsContainer);

		arkCrudContainerVO.getDetailPanelFormContainer().add(genderTypeDdc);
		arkCrudContainerVO.getDetailPanelFormContainer().add(subjectStatusDdc);
		arkCrudContainerVO.getDetailPanelFormContainer().add(maritalStatusDdc);
		arkCrudContainerVO.getDetailPanelFormContainer().add(personContactMethodDdc);

		// Preferred email becomes required when selected as preferred contact method
		wmcPreferredEmailContainer.add(preferredEmailTxtFld);
		arkCrudContainerVO.getDetailPanelFormContainer().add(wmcPreferredEmailContainer);
		arkCrudContainerVO.getDetailPanelFormContainer().add(otherEmailTxtFld);

		// Add consent fields into the form container.
		arkCrudContainerVO.getDetailPanelFormContainer().add(consentToActiveContactDdc);
		arkCrudContainerVO.getDetailPanelFormContainer().add(consentToUseDataDdc);
		arkCrudContainerVO.getDetailPanelFormContainer().add(consentToPassDataGatheringDdc);
		arkCrudContainerVO.getDetailPanelFormContainer().add(consentStatusChoice);
		arkCrudContainerVO.getDetailPanelFormContainer().add(consentTypeChoice);
		arkCrudContainerVO.getDetailPanelFormContainer().add(consentDateTxtFld);
		arkCrudContainerVO.getDetailPanelFormContainer().add(consentDownloadedChoice);

		arkCrudContainerVO.getDetailPanelFormContainer().add(consentHistoryPanel);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see au.org.theark.core.web.form.AbstractDetailForm#processErrors(org.apache.wicket.ajax.AjaxRequestTarget)
	 */
	@Override
	protected void processErrors(AjaxRequestTarget target) {
		target.add(feedBackPanel);
	}

	protected void onCancel(AjaxRequestTarget target) {
		subjectUIDTxtFld.setEnabled(true);
		SubjectVO subjectVO = new SubjectVO();

		// Set study in conext
		Long sessionStudyId = (Long) SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.core.Constants.STUDY_CONTEXT_ID);
		Study study = iArkCommonService.getStudy(sessionStudyId);
		subjectVO.getLinkSubjectStudy().setStudy(study);
		containerForm.setModelObject(subjectVO);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see au.org.theark.core.web.form.AbstractDetailForm#attachValidators()
	 */
	@Override
	protected void attachValidators() {
		subjectUIDTxtFld.setRequired(true).setLabel(new StringResourceModel("subject.uid.required", this, null));
		dateOfBirthTxtFld.setLabel(new StringResourceModel("linkSubjectStudy.person.dateOfBirth.DateValidator.maximum", this, null));

		// studyApproachDate.add(DateValidator.maximum(new Date())).setLabel(new
		// StringResourceModel("linkSubjectStudy.studyApproachDate.DateValidator.maximum", this, null));

		subjectStatusDdc.setRequired(true).setLabel(new StringResourceModel("subject.status.required", this, null));
		consentDateTxtFld.setLabel(new StringResourceModel("consentDate", this, null));
		consentDateTxtFld.add(DateValidator.maximum(new Date())).setLabel(new StringResourceModel("linkSubjectStudy.consentDate.DateValidator.maximum", this, null));
		dateLastKnownAliveTxtFld.add(DateValidator.maximum(new Date())).setLabel(new StringResourceModel("linkSubjectStudy.person.dateLastKnownAlive.DateValidator.maximum", this, null));
		// DateValidator.maximum(new Date())).setLabel(new StringResourceModel("phone.dateReceived.DateValidator.maximum", this, null));
		preferredEmailTxtFld.add(EmailAddressValidator.getInstance());
		otherEmailTxtFld.add(EmailAddressValidator.getInstance());
	}

	@SuppressWarnings("unused")
	private boolean validateCustomFields(Long fieldToValidate, String message, AjaxRequestTarget target) {
		boolean validFlag = true;
		Calendar calendar = Calendar.getInstance();
		int calYear = calendar.get(Calendar.YEAR);
		if (fieldToValidate > calYear) {
			validFlag = false;
			this.error(message);
			processErrors(target);
		}

		return validFlag;
	}

	private void saveUpdateProcess(SubjectVO subjectVO, AjaxRequestTarget target) {

		if (subjectVO.getLinkSubjectStudy().getPerson().getId() == null || containerForm.getModelObject().getLinkSubjectStudy().getPerson().getId() == 0) {

			subjectVO.getLinkSubjectStudy().setStudy(study);
			try {
				studyService.createSubject(subjectVO);
				StringBuffer sb = new StringBuffer();
				sb.append("The Subject with Subject UID: ");
				sb.append(subjectVO.getLinkSubjectStudy().getSubjectUID());
				sb.append(" has been created successfully and linked to the study in context: ");
				sb.append(study.getName());
				sb.append(".");

				if (study.getAutoConsent()) {
					sb.append(" The Subject has been automatically consented to the Study.");
				}

				onSavePostProcess(target);
				this.info(sb.toString());

				// Set new Subject into context
				SecurityUtils.getSubject().getSession().setAttribute(au.org.theark.core.Constants.SUBJECTUID, subjectVO.getLinkSubjectStudy().getSubjectUID());
				SecurityUtils.getSubject().getSession().setAttribute(au.org.theark.core.Constants.PERSON_CONTEXT_ID, subjectVO.getLinkSubjectStudy().getPerson().getId());
				SecurityUtils.getSubject().getSession().setAttribute(au.org.theark.core.Constants.PERSON_TYPE, au.org.theark.core.Constants.PERSON_CONTEXT_TYPE_SUBJECT);
			}
			catch (ArkUniqueException ex) {
				this.error("Subject UID must be unique.");
			}
			catch (ArkSubjectInsertException ex) {
				this.error(ex.getMessage());
			}

		}
		else {

			try {
				studyService.updateSubject(subjectVO);
				StringBuffer sb = new StringBuffer();
				sb.append("The Subject with Subject UID: ");
				sb.append(subjectVO.getLinkSubjectStudy().getSubjectUID());
				sb.append(" has been updated successfully and linked to the study in context ");
				sb.append(study.getName());
				onSavePostProcess(target);
				this.info(sb.toString());
			}
			catch (ArkUniqueException e) {
				this.error("Subject UID must be unique.");
			}
		}
		processErrors(target);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see au.org.theark.core.web.form.AbstractDetailForm#onSave(org.apache.wicket.markup.html.form.Form, org.apache.wicket.ajax.AjaxRequestTarget)
	 */
	@Override
	protected void onSave(Form<SubjectVO> containerForm, AjaxRequestTarget target) {

		target.add(arkCrudContainerVO.getDetailPanelContainer());

		Long studyId = (Long) SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.core.Constants.STUDY_CONTEXT_ID);
		if (studyId == null) {
			// No study in context
			this.error("There is no study in Context. Please select a study to manage a subject.");
			processErrors(target);
		}
		else {

			study = iArkCommonService.getStudy(studyId);
			saveUpdateProcess(containerForm.getModelObject(), target);
			// String subjectPreviousLastname = iArkCommonService.getPreviousLastname(containerForm.getModelObject().getSubjectStudy().getPerson());
			// containerForm.getModelObject().setSubjectPreviousLastname(subjectPreviousLastname);
			ContextHelper contextHelper = new ContextHelper();
			contextHelper.resetContextLabel(target, arkContextMarkupContainer);
			contextHelper.setStudyContextLabel(target, study.getName(), arkContextMarkupContainer);
			contextHelper.setSubjectContextLabel(target, containerForm.getModelObject().getLinkSubjectStudy().getSubjectUID(), arkContextMarkupContainer);

			SecurityUtils.getSubject().getSession().setAttribute(au.org.theark.core.Constants.PERSON_CONTEXT_ID, containerForm.getModelObject().getLinkSubjectStudy().getPerson().getId());
			// We specify the type of person here as Subject
			SecurityUtils.getSubject().getSession().setAttribute(au.org.theark.core.Constants.PERSON_TYPE, au.org.theark.core.Constants.PERSON_CONTEXT_TYPE_SUBJECT);
			arkCrudContainerVO.getDetailPanelContainer().setVisible(true);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see au.org.theark.core.web.form.AbstractDetailForm#onDeleteConfirmed(org.apache.wicket.ajax.AjaxRequestTarget, java.lang.String,
	 * org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow)
	 */
	@Override
	protected void onDeleteConfirmed(AjaxRequestTarget target, String selection) {
		onCancel(target);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see au.org.theark.core.web.form.AbstractDetailForm#isNew()
	 */
	@Override
	protected boolean isNew() {
		if (containerForm.getModelObject().getLinkSubjectStudy().getId() == null) {
			return true;
		}
		else {
			return false;
		}

	}

	public TextField<String> getSubjectUIDTxtFld() {
		return subjectUIDTxtFld;
	}

	public void setSubjectUIDTxtFld(TextField<String> subjectUIDTxtFld) {
		this.subjectUIDTxtFld = subjectUIDTxtFld;
	}

	/**
	 * @return the childStudyPalettePanel
	 */
	public ChildStudyPalettePanel<SubjectVO> getChildStudyPalettePanel() {
		return childStudyPalettePanel;
	}

	/**
	 * @param childStudyPalettePanel
	 *           the childStudyPalettePanel to set
	 */
	public void setChildStudyPalettePanel(ChildStudyPalettePanel<SubjectVO> childStudyPalettePanel) {
		this.childStudyPalettePanel = childStudyPalettePanel;
	}
}
