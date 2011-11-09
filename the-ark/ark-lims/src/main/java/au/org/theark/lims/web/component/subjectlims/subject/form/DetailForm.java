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
package au.org.theark.lims.web.component.subjectlims.subject.form;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.List;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.extensions.markup.html.form.DateTextField;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.EmptyPanel;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import au.org.theark.core.exception.EntityNotFoundException;
import au.org.theark.core.model.study.entity.ArkModule;
import au.org.theark.core.model.study.entity.ArkUser;
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
import au.org.theark.core.vo.ArkCrudContainerVO;
import au.org.theark.core.vo.ArkUserVO;
import au.org.theark.core.web.behavior.ArkDefaultFormFocusBehavior;
import au.org.theark.core.web.component.ArkDatePicker;
import au.org.theark.core.web.form.AbstractDetailForm;
import au.org.theark.lims.model.vo.LimsVO;
import au.org.theark.lims.web.Constants;

/**
 * @author cellis
 * 
 */
public class DetailForm extends AbstractDetailForm<LimsVO> {

	/**
	 * 
	 */
	private static final long								serialVersionUID	= 6510243238571556231L;
	protected static final Logger							log					= LoggerFactory.getLogger(DetailForm.class);

	@SpringBean(name = au.org.theark.core.Constants.ARK_COMMON_SERVICE)
	private IArkCommonService<Void>						iArkCommonService;
	
	protected TextField<String>							subjectUIDTxtFld;
	protected TextField<String>							firstNameTxtFld;
	protected TextField<String>							middleNameTxtFld;
	protected TextField<String>							lastNameTxtFld;
	protected TextField<String>							previousLastNameTxtFld;
	protected TextField<String>							preferredNameTxtFld;

	protected DateTextField									dateOfBirthTxtFld;
	protected DateTextField									dateOfDeathTxtFld;
	protected TextField<String>							causeOfDeathTxtFld;

	// Custom Fields and Consents at Subject Study Level
	protected TextField<String>							amdrifIdTxtFld;
	protected DateTextField									studyApproachDate;
	protected TextField<Long>								yearOfFirstMamogramTxtFld;
	protected TextField<String>							yearOfRecentMamogramTxtFld;
	protected TextField<String>							totalNumberOfMamogramsTxtFld;
	protected DropDownChoice<YesNo>						consentToActiveContactDdc;
	protected DropDownChoice<YesNo>						consentToUseDataDdc;
	protected DropDownChoice<YesNo>						consentToPassDataGatheringDdc;

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
	// Webmarkup for Ajax refreshing of items based on particular criteria
	protected WebMarkupContainer							wmcPreferredEmailContainer;
	protected WebMarkupContainer							wmcDeathDetailsContainer;
	protected ContainerForm									containerForm;

	protected Study											study;
	
	public DetailForm(String id, FeedbackPanel feedBackPanel, WebMarkupContainer arkContextContainer, ContainerForm containerForm, ArkCrudContainerVO arkCrudContainerVO) {

		super(id, feedBackPanel, containerForm, arkCrudContainerVO);
		this.containerForm = containerForm;
		
		// Disable editing of Subject details in LIMS
		editButton = new AjaxButton(au.org.theark.core.Constants.EDIT) {

			/**
			 * 
			 */
			private static final long	serialVersionUID	= 1L;

			@Override
			public boolean isVisible() {
				return false;
			}

			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
				// Should never get here since Edit button should never be enabled for Subject Details via LIMS
				log.error("Incorrect application workflow - tried to edit Subject Details via LIMS");
			}

			@Override
			protected void onError(AjaxRequestTarget target, Form<?> form) {
				// Should never get here since Edit button should never be enabled for Subject Details via LIMS
				log.error("Incorrect application workflow - tried to edit Subject Details via LIMS and error occurred");
			}
		};
		arkCrudContainerVO.getViewButtonContainer().addOrReplace(editButton);
	}

	@SuppressWarnings("unchecked")
	public void initialiseDetailForm() {
		subjectUIDTxtFld = new TextField<String>(Constants.SUBJECT_UID);
		subjectUIDTxtFld.setOutputMarkupId(true);

		firstNameTxtFld = new TextField<String>(Constants.PERSON_FIRST_NAME);
		middleNameTxtFld = new TextField<String>(Constants.PERSON_MIDDLE_NAME);
		lastNameTxtFld = new TextField<String>(Constants.PERSON_LAST_NAME);

		preferredNameTxtFld = new TextField<String>(Constants.PERSON_PREFERRED_NAME);

		preferredEmailTxtFld = new TextField<String>(Constants.PERSON_PREFERRED_EMAIL);
		otherEmailTxtFld = new TextField<String>(Constants.PERSON_OTHER_EMAIL);

		dateOfBirthTxtFld = new DateTextField(Constants.PERSON_DOB, au.org.theark.core.Constants.DD_MM_YYYY);
		ArkDatePicker dobDatePicker = new ArkDatePicker();
		dobDatePicker.bind(dateOfBirthTxtFld);
		dateOfBirthTxtFld.add(dobDatePicker);

		consentDateTxtFld = new DateTextField(Constants.PERSON_CONSENT_DATE, au.org.theark.core.Constants.DD_MM_YYYY);
		ArkDatePicker consentDatePicker = new ArkDatePicker();
		consentDatePicker.bind(consentDateTxtFld);
		consentDateTxtFld.add(consentDatePicker);

		dateOfDeathTxtFld = new DateTextField(Constants.PERSON_DOD, au.org.theark.core.Constants.DD_MM_YYYY);

		causeOfDeathTxtFld = new TextField<String>(Constants.PERSON_CAUSE_OF_DEATH);
		ArkDatePicker dodDatePicker = new ArkDatePicker();
		dodDatePicker.bind(dateOfDeathTxtFld);
		dateOfDeathTxtFld.add(dodDatePicker);

		wmcDeathDetailsContainer = new WebMarkupContainer("deathDetailsContainer");
		wmcDeathDetailsContainer.setOutputMarkupId(true);

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

		// Gender Type
		Collection<GenderType> genderTypeList = iArkCommonService.getGenderType();
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

		// Person Contact Method
		Collection<PersonContactMethod> contactMethodList = iArkCommonService.getPersonContactMethodList();
		ChoiceRenderer<PersonContactMethod> contactMethodRender = new ChoiceRenderer<PersonContactMethod>(Constants.NAME, Constants.ID);
		personContactMethodDdc = new DropDownChoice<PersonContactMethod>(Constants.PERSON_CONTACT_METHOD, (List) contactMethodList, contactMethodRender);

		initialiseConsentStatusChoice();
		initialiseConsentTypeChoice();
		attachValidators();
		addDetailFormComponents();
	}

	/**
	 * Initialise the Consent Status Drop Down Choice Control
	 */
	protected void initialiseConsentStatusChoice() {
		List<ConsentStatus> consentStatusList = iArkCommonService.getRecordableConsentStatus();
		ChoiceRenderer<ConsentStatus> defaultChoiceRenderer = new ChoiceRenderer<ConsentStatus>(Constants.NAME, Constants.ID);
		consentStatusChoice = new DropDownChoice<ConsentStatus>(Constants.SUBJECT_CONSENT_STATUS, consentStatusList, defaultChoiceRenderer);
	}

	/**
	 * Initialise the Consent Type Drop Down Choice Control
	 */
	protected void initialiseConsentTypeChoice() {
		List<ConsentType> consentTypeList = iArkCommonService.getConsentType();
		ChoiceRenderer<ConsentType> defaultChoiceRenderer = new ChoiceRenderer<ConsentType>(Constants.NAME, Constants.ID);
		consentTypeChoice = new DropDownChoice<ConsentType>(Constants.SUBJECT_CONSENT_TYPE, consentTypeList, defaultChoiceRenderer);
	}

	public void addDetailFormComponents() {
		arkCrudContainerVO.getDetailPanelFormContainer().add(subjectUIDTxtFld);
		arkCrudContainerVO.getDetailPanelFormContainer().add(titleTypeDdc);
		arkCrudContainerVO.getDetailPanelFormContainer().add(firstNameTxtFld);
		arkCrudContainerVO.getDetailPanelFormContainer().add(lastNameTxtFld);
		arkCrudContainerVO.getDetailPanelFormContainer().add(dateOfBirthTxtFld);
		arkCrudContainerVO.getDetailPanelFormContainer().add(vitalStatusDdc);

		// Death details only be edited when vital status set to deceased
		wmcDeathDetailsContainer.add(dateOfDeathTxtFld);
		wmcDeathDetailsContainer.add(causeOfDeathTxtFld);
		arkCrudContainerVO.getDetailPanelFormContainer().add(wmcDeathDetailsContainer);

		arkCrudContainerVO.getDetailPanelFormContainer().add(genderTypeDdc);
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
		LimsVO limsVo = new LimsVO();
		
		// Set study in context back to limsVo.study
		Long sessionStudyId = (Long) SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.core.Constants.STUDY_CONTEXT_ID);
		if(sessionStudyId != null) {
			containerForm.setModelObject(limsVo);

			// Refresh the contextUpdateTarget (remove)
			if (containerForm.getContextUpdateLimsWMC() != null) {
				Panel limsContainerPanel = new EmptyPanel("limsContainerPanel");
				limsContainerPanel.setOutputMarkupPlaceholderTag(true);
				containerForm.getContextUpdateLimsWMC().addOrReplace(limsContainerPanel);
				target.add(containerForm.getContextUpdateLimsWMC());
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see au.org.theark.core.web.form.AbstractDetailForm#attachValidators()
	 */
	@Override
	protected void attachValidators() {
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

	@SuppressWarnings("unused")
	private void saveUpdateProcess(LimsVO subjectVO, AjaxRequestTarget target) {
		// Should never get here since edit should never be enabled for Subject Details via LIMS
		log.error("Incorrect application workflow - tried to save/edit Subject Details via LIMS");
	}

	@Override
	protected void onSave(Form<LimsVO> containerForm, AjaxRequestTarget target) {
		// Should never get here since edit should never be enabled for Subject Details via LIMS
		log.error("Incorrect application workflow - tried to save/edit Subject Details via LIMS");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see au.org.theark.core.web.form.AbstractDetailForm#onDeleteConfirmed(org.apache.wicket.ajax.AjaxRequestTarget, java.lang.String,
	 * org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow)
	 */
	@Override
	protected void onDeleteConfirmed(AjaxRequestTarget target, String selection) {
		// This should never happen for Subject Management because the Delete button
		// should never be visible/disabled
		log.error("Incorrect application workflow - tried to delete Subject via LIMS");
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
	 * Returns a list of Studies the user is permitted to access
	 * 
	 * @return
	 */
	public List<Study> getStudyListForUser() {
		List<Study> studyList = new ArrayList<Study>(0);
		try {
			Subject currentUser = SecurityUtils.getSubject();
			ArkUser arkUser = iArkCommonService.getArkUser(currentUser.getPrincipal().toString());
			ArkUserVO arkUserVo = new ArkUserVO();
			arkUserVo.setArkUserEntity(arkUser);
			
			Long sessionArkModuleId = (Long) SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.core.Constants.ARK_MODULE_KEY);
			ArkModule arkModule = null;
			arkModule = iArkCommonService.getArkModuleById(sessionArkModuleId);
			studyList = iArkCommonService.getStudyListForUserAndModule(arkUserVo, arkModule);
		}
		catch (EntityNotFoundException e) {
			log.error(e.getMessage());
		}
		return studyList;
	}
}