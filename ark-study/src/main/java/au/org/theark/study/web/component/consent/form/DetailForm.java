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
package au.org.theark.study.web.component.consent.form;

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
import org.apache.wicket.validation.validator.StringValidator;

import au.org.theark.core.exception.ArkSystemException;
import au.org.theark.core.exception.EntityNotFoundException;
import au.org.theark.core.model.audit.entity.ConsentHistory;
import au.org.theark.core.model.study.entity.ConsentStatus;
import au.org.theark.core.model.study.entity.ConsentType;
import au.org.theark.core.model.study.entity.Person;
import au.org.theark.core.model.study.entity.Study;
import au.org.theark.core.model.study.entity.StudyComp;
import au.org.theark.core.model.study.entity.StudyCompStatus;
import au.org.theark.core.model.study.entity.YesNo;
import au.org.theark.core.service.IArkCommonService;
import au.org.theark.core.vo.ArkCrudContainerVO;
import au.org.theark.core.vo.ConsentVO;
import au.org.theark.core.web.behavior.ArkDefaultFormFocusBehavior;
import au.org.theark.core.web.component.ArkDatePicker;
import au.org.theark.core.web.component.panel.collapsiblepanel.CollapsiblePanel;
import au.org.theark.core.web.form.AbstractDetailForm;
import au.org.theark.study.service.IStudyService;
import au.org.theark.study.web.Constants;
import au.org.theark.study.web.component.consenthistory.ConsentHistoryPanel;

/**
 * @author nivedann
 * @author cellis
 * 
 */
public class DetailForm extends AbstractDetailForm<ConsentVO> {

	/**
	 * 
	 */
	private static final long						serialVersionUID	= 1L;

	@SuppressWarnings("unchecked")
	@SpringBean(name = au.org.theark.core.Constants.ARK_COMMON_SERVICE)
	protected IArkCommonService					iArkCommonService;

	@SpringBean(name = Constants.STUDY_SERVICE)
	protected IStudyService							iStudyService;

	/**
	 * Form Components
	 */
	protected TextField<String>					consentedBy;
	protected DateTextField							consentedDatePicker;
	protected DateTextField							consentRequestedDtf;
	protected DateTextField							consentReceivedDtf;
	protected DateTextField							consentCompletedDtf;

	protected DropDownChoice<StudyComp>			studyComponentChoice;
	protected DropDownChoice<StudyCompStatus>	studyComponentStatusChoice;
	protected DropDownChoice<ConsentStatus>	consentStatusChoice;
	protected DropDownChoice<ConsentType>		consentTypeChoice;
	protected TextArea<String>						commentTxtArea;
	protected WebMarkupContainer					wmcPlain;
	protected WebMarkupContainer					wmcRequested;
	protected WebMarkupContainer					wmcRecieved;
	protected WebMarkupContainer					wmcCompleted;
	protected DropDownChoice<YesNo>				consentDownloadedDdc;
	protected CollapsiblePanel 					consentHistoryPanel;

	public DetailForm(String id, FeedbackPanel feedBackPanel, ContainerForm containerForm, ArkCrudContainerVO arkCrudContainerVO) {
		super(id, feedBackPanel, containerForm, arkCrudContainerVO);
	}

	public void initialiseDetailForm() {
		consentedBy = new TextField<String>(Constants.CONSENT_CONSENTED_BY);

		wmcPlain = new WebMarkupContainer(Constants.WMC_PLAIN);
		wmcPlain.setOutputMarkupPlaceholderTag(true);
		wmcPlain.setVisible(true);

		wmcRequested = new WebMarkupContainer(Constants.WMC_REQUESTED);
		wmcRequested.setOutputMarkupPlaceholderTag(true);
		wmcRequested.setVisible(false);

		wmcRecieved = new WebMarkupContainer(Constants.WMC_RECIEVED);
		wmcRecieved.setOutputMarkupPlaceholderTag(true);
		wmcRecieved.setVisible(false);

		wmcCompleted = new WebMarkupContainer(Constants.WMC_COMPLETED);
		wmcCompleted.setOutputMarkupPlaceholderTag(true);
		wmcCompleted.setVisible(false);

		consentedDatePicker = new DateTextField(Constants.CONSENT_CONSENT_DATE, au.org.theark.core.Constants.DD_MM_YYYY);
		ArkDatePicker arkDatePicker = new ArkDatePicker();
		arkDatePicker.bind(consentedDatePicker);
		consentedDatePicker.add(arkDatePicker);

		consentRequestedDtf = new DateTextField(Constants.CONSENT_REQUESTED_DATE, au.org.theark.core.Constants.DD_MM_YYYY);
		ArkDatePicker requestedDatePicker = new ArkDatePicker();
		requestedDatePicker.bind(consentRequestedDtf);
		consentRequestedDtf.add(requestedDatePicker);
		wmcRequested.add(consentRequestedDtf);

		consentReceivedDtf = new DateTextField(Constants.CONSENT_RECEIVED_DATE, au.org.theark.core.Constants.DD_MM_YYYY);
		ArkDatePicker recievedDatePicker = new ArkDatePicker();
		recievedDatePicker.bind(consentReceivedDtf);
		consentReceivedDtf.add(recievedDatePicker);
		wmcRecieved.add(consentReceivedDtf);

		consentCompletedDtf = new DateTextField(Constants.CONSENT_COMPLETED_DATE, au.org.theark.core.Constants.DD_MM_YYYY);
		ArkDatePicker completedDatePicker = new ArkDatePicker();
		completedDatePicker.bind(consentCompletedDtf);
		consentCompletedDtf.add(completedDatePicker);
		wmcCompleted.add(consentCompletedDtf);

		commentTxtArea = new TextArea<String>(Constants.CONSENT_CONSENT_COMMENT);
		initConsentTypeChoice();
		initConsentStatusChoice();
		initComponentChoice();
		initStudyComponentStatusChoice();
		initConsentDownloadChoice();
		initConsentHistoryPanel();
		addDetailFormComponents();
		attachValidators();
	}
	
	@SuppressWarnings("unchecked")
	protected void initStudyComponentStatusChoice() {
		List<StudyCompStatus> studyCompList = iArkCommonService.getStudyComponentStatus();
		ChoiceRenderer<StudyCompStatus> defaultChoiceRenderer = new ChoiceRenderer<StudyCompStatus>(Constants.NAME, Constants.ID);
		studyComponentStatusChoice = new DropDownChoice<StudyCompStatus>(Constants.CONSENT_STUDY_COMP_STATUS, studyCompList, defaultChoiceRenderer);
		studyComponentStatusChoice.add(new AjaxFormComponentUpdatingBehavior("onchange") {

			/**
			 * 
			 */
			private static final long	serialVersionUID	= 1L;

			@Override
			protected void onUpdate(AjaxRequestTarget target) {
				// Check what was selected and then toggle
				StudyCompStatus status = studyComponentStatusChoice.getModelObject();
				String statusName = status.getName();
				new FormHelper().updateStudyCompStatusDates(target, statusName, wmcPlain, wmcRequested, wmcRecieved, wmcCompleted);
			}
		});
	}

	@SuppressWarnings("unchecked")
	private void initConsentDownloadChoice() {
		Collection<YesNo> yesNoList = iArkCommonService.getYesNoList();
		ChoiceRenderer<YesNo> yesnoRenderer = new ChoiceRenderer<YesNo>(Constants.NAME, Constants.ID);
		consentDownloadedDdc = new DropDownChoice<YesNo>(Constants.CONSENT_CONSENT_DOWNLOADED, (List) yesNoList, yesnoRenderer);
	}

	/**
	 * Initialise the Consent Type Drop Down Choice Control
	 */
	@SuppressWarnings("unchecked")
	protected void initConsentTypeChoice() {
		List<ConsentType> consentTypeList = iArkCommonService.getConsentType();
		ChoiceRenderer<ConsentType> defaultChoiceRenderer = new ChoiceRenderer<ConsentType>(Constants.NAME, Constants.ID);
		consentTypeChoice = new DropDownChoice<ConsentType>(Constants.CONSENT_CONSENT_TYPE, consentTypeList, defaultChoiceRenderer);
	}

	/**
	 * Initialise the Consent Status Drop Down Choice Control
	 */
	@SuppressWarnings("unchecked")
	protected void initConsentStatusChoice() {
		List<ConsentStatus> consentStatusList = iArkCommonService.getRecordableConsentStatus();
		ChoiceRenderer<ConsentStatus> defaultChoiceRenderer = new ChoiceRenderer<ConsentStatus>(Constants.NAME, Constants.ID);
		consentStatusChoice = new DropDownChoice<ConsentStatus>(Constants.CONSENT_CONSENT_STATUS, consentStatusList, defaultChoiceRenderer);
	}

	/**
	 * Initialise the Consent StudyComp Drop Down Choice Control
	 */
	@SuppressWarnings("unchecked")
	protected void initComponentChoice() {
		Long sessionStudyId = (Long) SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.core.Constants.STUDY_CONTEXT_ID);
		Study study = iArkCommonService.getStudy(sessionStudyId);
		List<StudyComp> studyCompList = iArkCommonService.getStudyComponentByStudy(study);
		ChoiceRenderer<StudyComp> defaultChoiceRenderer = new ChoiceRenderer<StudyComp>(Constants.NAME, Constants.ID);
		studyComponentChoice = new DropDownChoice<StudyComp>(Constants.CONSENT_STUDY_COMP, studyCompList, defaultChoiceRenderer);
		studyComponentChoice.add(new ArkDefaultFormFocusBehavior());
		studyComponentChoice.add(new AjaxFormComponentUpdatingBehavior("onchange") {

			/**
			 * 
			 */
			private static final long	serialVersionUID	= 1L;

			@Override
			protected void onUpdate(AjaxRequestTarget target) {
				Long sessionStudyId = (Long) SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.core.Constants.STUDY_CONTEXT_ID);
				Long sessionPersonId = (Long) SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.core.Constants.PERSON_CONTEXT_ID);

				try {
					Study study = iArkCommonService.getStudy(sessionStudyId);
					Person subject = iStudyService.getPerson(sessionPersonId);
					boolean isConsented = iArkCommonService.isSubjectConsentedToComponent(studyComponentChoice.getModelObject(), subject, study);
					processErrors(target);
					if (isConsented) {
						StringBuffer sb = new StringBuffer();
						sb.append("Please choose another component. The Subject has already consented to Component: ");
						sb.append(studyComponentChoice.getModelObject().getName());
						containerForm.error(sb.toString());
						processErrors(target);
					}
				}
				catch (EntityNotFoundException e) {
					containerForm.error("The subject in context does not exist in system anymore.Please re-do the operation.");

				}
				catch (ArkSystemException e) {
					containerForm.error("There was a system error. Please contact support.");
				}
			}
		});
	}
	
	private void initConsentHistoryPanel() {
		consentHistoryPanel = new CollapsiblePanel("consentHistoryPanel", new Model<String>("Consent History"), false) {
			
			/**
			 * 
			 */
			private static final long	serialVersionUID	= 1L;

			@Override
			protected Panel getInnerPanel(String markupId) {
				ConsentHistoryPanel consentHistoryPanel = new ConsentHistoryPanel(markupId, new CompoundPropertyModel<ConsentHistory>(new ConsentHistory()));
				return consentHistoryPanel;
			}
		};
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see au.org.theark.core.web.form.AbstractDetailForm#attachValidators()
	 */
	@Override
	protected void attachValidators() {
		commentTxtArea.add(StringValidator.maximumLength(100)).setLabel(new StringResourceModel("comments.max.length", this, null));
		consentedBy.add(StringValidator.maximumLength(100)).setLabel(new StringResourceModel("consentedBy.max.length", this, null));
		studyComponentChoice.setRequired(true).setLabel(new StringResourceModel("study.component.choice.required", this, null));
		consentStatusChoice.setRequired(true).setLabel(new StringResourceModel("consent.status.required", this, null));
		consentTypeChoice.setRequired(true).setLabel(new StringResourceModel("consent.type.required", this, null));
		studyComponentStatusChoice.setRequired(true).setLabel(new StringResourceModel("studyComponent.status.required", this, null));
		consentedDatePicker.add(DateValidator.maximum(new Date())).setLabel(new StringResourceModel("consent.consentdate", this, null));
		consentedDatePicker.setRequired(true);
		consentCompletedDtf.add(DateValidator.maximum(new Date())).setLabel(new StringResourceModel("completed.date.DateValidator.maximum", this, null));
		consentRequestedDtf.add(DateValidator.maximum(new Date())).setLabel(new StringResourceModel("requested.date.DateValidator.maximum", this, null));
		consentReceivedDtf.add(DateValidator.maximum(new Date())).setLabel(new StringResourceModel("received.date.DateValidator.maximum", this, null));
	}

	public void addDetailFormComponents() {
		arkCrudContainerVO.getDetailPanelFormContainer().add(consentedBy);
		arkCrudContainerVO.getDetailPanelFormContainer().add(consentedDatePicker);

		arkCrudContainerVO.getDetailPanelFormContainer().add(wmcPlain);
		arkCrudContainerVO.getDetailPanelFormContainer().add(wmcRecieved);
		arkCrudContainerVO.getDetailPanelFormContainer().add(wmcRequested);
		arkCrudContainerVO.getDetailPanelFormContainer().add(wmcCompleted);

		arkCrudContainerVO.getDetailPanelFormContainer().add(studyComponentChoice);
		arkCrudContainerVO.getDetailPanelFormContainer().add(studyComponentStatusChoice);
		arkCrudContainerVO.getDetailPanelFormContainer().add(consentStatusChoice);
		arkCrudContainerVO.getDetailPanelFormContainer().add(consentTypeChoice);
		arkCrudContainerVO.getDetailPanelFormContainer().add(commentTxtArea);
		arkCrudContainerVO.getDetailPanelFormContainer().add(consentDownloadedDdc);
		arkCrudContainerVO.getDetailPanelFormContainer().add(consentHistoryPanel);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see au.org.theark.core.web.form.AbstractDetailForm#onCancel(org.apache.wicket.ajax.AjaxRequestTarget)
	 */
	@Override
	protected void onCancel(AjaxRequestTarget target) {
		ConsentVO consentVO = new ConsentVO();
		containerForm.setModelObject(consentVO);
		new FormHelper().setDatePickerDefaultMarkup(target, wmcPlain, wmcRequested, wmcRecieved, wmcCompleted);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see au.org.theark.core.web.form.AbstractDetailForm#onDeleteConfirmed(org.apache.wicket.ajax.AjaxRequestTarget, java.lang.String,
	 * org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow)
	 */
	@Override
	protected void onDeleteConfirmed(AjaxRequestTarget target, String selection) {
		try {
			iStudyService.delete(containerForm.getModelObject().getConsent());
			containerForm.info("The Consent has been deleted successfully.");
			editCancelProcess(target);
		}
		catch (EntityNotFoundException entityNotFoundException) {
			this.error("The consent you tried to delete does not exist");
		}
		catch (ArkSystemException e) {
			this.error("A system exception has occured during delete operation of the Consent");
		}
		ConsentVO consentVO = new ConsentVO();
		containerForm.setModelObject(consentVO);
		onCancel(target);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see au.org.theark.core.web.form.AbstractDetailForm#onSave(org.apache.wicket.markup.html.form.Form, org.apache.wicket.ajax.AjaxRequestTarget)
	 */
	@Override
	protected void onSave(Form<ConsentVO> containerForm, AjaxRequestTarget target) {
		// save new
		boolean isOkToSave = true;

		String status = containerForm.getModelObject().getConsent().getStudyComponentStatus().getName();

		if (status.equalsIgnoreCase(Constants.STUDY_STATUS_COMPLETED) && containerForm.getModelObject().getConsent().getCompletedDate() == null) {
			isOkToSave = false;
			this.error("Field 'Completed Date' is required.");
		}
		else if (status.equalsIgnoreCase(Constants.STUDY_STATUS_REQUESTED) && containerForm.getModelObject().getConsent().getRequestedDate() == null) {
			isOkToSave = false;
			this.error("Field 'Requested Date' is required.");
		}
		else if (status.equalsIgnoreCase(Constants.STUDY_STATUS_RECEIVED) && containerForm.getModelObject().getConsent().getReceivedDate() == null) {
			isOkToSave = false;
			this.error("Field 'Received Date' is required.");
		}
		else {
			isOkToSave = true;
		}

		if (isOkToSave) {
			try {
				// Study in Context
				Long studyId = (Long) SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.core.Constants.STUDY_CONTEXT_ID);
				Study study = iArkCommonService.getStudy(studyId);
				containerForm.getModelObject().getConsent().setStudy(study);

				if (containerForm.getModelObject().getConsent().getId() == null) {
					iStudyService.create(containerForm.getModelObject().getConsent());
					this.info("Consent was successfuly created for the Subject ");
					processErrors(target);
					// Store session object (used for history)
					SecurityUtils.getSubject().getSession().setAttribute(au.org.theark.core.Constants.PERSON_CONTEXT_CONSENT_ID, containerForm.getModelObject().getConsent().getId());
				}
				else {
					iStudyService.update(containerForm.getModelObject().getConsent());
					
					this.info("Consent was successfuly updated for the Subject ");
					processErrors(target);
				}
			}
			catch (EntityNotFoundException e) {
				this.error("The Consent record you tried to update is no longer available in the system");
				processErrors(target);
			}
			catch (ArkSystemException e) {
				this.error(e.getMessage());
				processErrors(target);
			}
			finally {
				onSavePostProcess(target);
			}
		}
		else {
			processErrors(target);
		}
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see au.org.theark.core.web.form.AbstractDetailForm#isNew()
	 */
	@Override
	protected boolean isNew() {
		if (containerForm.getModelObject().getConsent().getId() == null) {
			return true;
		}
		else {
			return false;
		}
	}
}
