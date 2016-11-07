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

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.apache.shiro.SecurityUtils;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.extensions.markup.html.form.DateTextField;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.form.upload.FileUpload;
import org.apache.wicket.markup.html.form.upload.FileUploadField;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.util.io.IOUtils;
import org.apache.wicket.util.resource.FileResourceStream;
import org.apache.wicket.util.resource.IResourceStream;
import org.apache.wicket.validation.validator.DateValidator;
import org.apache.wicket.validation.validator.StringValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import au.org.theark.core.exception.ArkCheckSumNotSameException;
import au.org.theark.core.exception.ArkFileNotFoundException;
import au.org.theark.core.exception.ArkSystemException;
import au.org.theark.core.exception.EntityNotFoundException;
import au.org.theark.core.model.audit.entity.ConsentHistory;
import au.org.theark.core.model.study.entity.Consent;
import au.org.theark.core.model.study.entity.ConsentStatus;
import au.org.theark.core.model.study.entity.ConsentType;
import au.org.theark.core.model.study.entity.LinkSubjectStudy;
import au.org.theark.core.model.study.entity.Person;
import au.org.theark.core.model.study.entity.Study;
import au.org.theark.core.model.study.entity.StudyComp;
import au.org.theark.core.model.study.entity.StudyCompStatus;
import au.org.theark.core.model.study.entity.SubjectFile;
import au.org.theark.core.model.study.entity.YesNo;
import au.org.theark.core.service.IArkCommonService;
import au.org.theark.core.util.AJAXDownload;

import au.org.theark.core.vo.ArkCrudContainerVO;
import au.org.theark.core.vo.ConsentVO;
import au.org.theark.core.web.behavior.ArkDefaultFormFocusBehavior;
import au.org.theark.core.web.component.ArkDatePicker;
import au.org.theark.core.web.component.audit.button.HistoryButtonPanel;
import au.org.theark.core.web.component.link.ArkBusyAjaxLink;
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
	private static final long						serialVersionUID	= 1L;
	private transient Logger				log					= LoggerFactory.getLogger(DetailForm.class);

	@SuppressWarnings("unchecked")
	@SpringBean(name = au.org.theark.core.Constants.ARK_COMMON_SERVICE)
	protected IArkCommonService					iArkCommonService;

	@SpringBean(name = Constants.STUDY_SERVICE)
	protected IStudyService							iStudyService;

	/**
	 * Form Components
	 */
	protected TextField<String>						consentedBy;
	protected DateTextField							consentedDatePicker;
	protected DateTextField							consentRequestedDtf;
	protected DateTextField							consentReceivedDtf;
	protected DateTextField							consentCompletedDtf;

	protected DropDownChoice<StudyComp>				studyComponentChoice;
	protected DropDownChoice<StudyCompStatus>		studyComponentStatusChoice;
	protected DropDownChoice<ConsentStatus>			consentStatusChoice;
	protected DropDownChoice<ConsentType>			consentTypeChoice;
	protected TextArea<String>						commentTxtArea;
	protected WebMarkupContainer					wmcPlain;
	protected WebMarkupContainer					wmcRequested;
	protected WebMarkupContainer					wmcRecieved;
	protected WebMarkupContainer					wmcCompleted;
	protected DropDownChoice<YesNo>					consentDownloadedDdc;
	protected CollapsiblePanel						consentHistoryPanel;
	protected FileUploadField						fileUploadField;
	private AjaxButton								clearButton;
	protected HistoryButtonPanel					historyButtonPanel;
	private Label									fileNameLbl;
	private ArkBusyAjaxLink<String>  				fileNameLnk;
	private AJAXDownload                            ajaxDownload;
	


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
		// fileSubjectFile for consent file payload (attached to filename key)
		fileUploadField = new FileUploadField(Constants.FILE);
		fileNameLbl = new Label(au.org.theark.study.web.Constants.SUBJECT_FILE_FILENAME);
		fileNameLbl.setOutputMarkupId(true);
		ajaxDownload = new AJAXDownload()
		{
			 @Override
	            protected IResourceStream getResourceStream()
	            {     
				 	SubjectFile subjectFile=containerForm.getModelObject().getSubjectFile();
					Long studyId =subjectFile.getLinkSubjectStudy().getStudy().getId();
					String subjectUID = subjectFile.getLinkSubjectStudy().getSubjectUID();
					String fileId = subjectFile.getFileId();
					String checksum = subjectFile.getChecksum();
					File file = null;
					IResourceStream resStream =null;
						try {
							file=iArkCommonService.retriveArkFileAttachmentAsFile(studyId,subjectUID,au.org.theark.study.web.Constants.ARK_SUBJECT_CONSENT_DIR,fileId,checksum);
							resStream = new FileResourceStream(file);
							if(resStream==null){
								containerForm.error("Unexpected error: Download request could not be fulfilled.");
							}
						} catch (ArkSystemException e) {
							containerForm.error("Unexpected error: Download request could not be fulfilled.");
							log.error(e.getMessage());
						} catch (ArkFileNotFoundException e) {
							containerForm.error("File not found:"+e.getMessage());
							log.error(e.getMessage());
						} catch (ArkCheckSumNotSameException e) {
							containerForm.error("Check sum error:"+e.getMessage());
							log.error(e.getMessage());
						}
						return resStream;
		        }
	            @Override
	            protected String getFileName() {
	                return containerForm.getModelObject().getSubjectFile().getFilename();
	            }
		};
		fileNameLnk=new ArkBusyAjaxLink<String>(au.org.theark.study.web.Constants.SUBJECT_FILE_FILENAMELINK) {
		@Override
		public void onClick(AjaxRequestTarget target) {
			ajaxDownload.initiate(target);
			processErrors(target);
		}
			
		};
		fileNameLnk.add(fileNameLbl);
		clearButton = new AjaxButton("clearButton") {			
			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
				fileUploadField.clearInput();
				target.add(fileUploadField);
			}
			
			@Override
			protected void onError(AjaxRequestTarget target, Form<?> form) {
				fileUploadField.clearInput();
				target.add(fileUploadField);
			}
		};
		clearButton.add(new AttributeModifier("title", new Model<String>("Clear Attachment")));
		deleteButton = new AjaxButton("deleteButton") {
			private static final long	serialVersionUID	= 1L;

			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
			try {
				iStudyService.delete(containerForm.getModelObject().getSubjectFile(),Constants.ARK_SUBJECT_CONSENT_DIR);
				containerForm.info("The file has been deleted successfully.");
				containerForm.getModelObject().getSubjectFile().setFilename(null);
			 }catch (EntityNotFoundException e) {
					containerForm.error("The subject consent attachment does not exist in system anymore.Please re-do the operation.");
			 }catch (ArkSystemException e) {
					containerForm.error("System error occure:"+e.getMessage());
			 } catch (ArkFileNotFoundException e) {
				 containerForm.error("File not found:"+e.getMessage());
			}finally{
				 //containerForm.getModelObject().getSubjectFile().setFilename(null);
				this.setVisible(false);
				target.add(fileNameLnk);
				target.add(this);
				processErrors(target);
			 }
			}
			@Override
			protected void onError(AjaxRequestTarget target, Form<?> form) {
				containerForm.getModelObject().getSubjectFile().setPayload(null);
				containerForm.getModelObject().getSubjectFile().setFilename(null);
				this.setVisible(false);
				target.add(fileNameLnk);
				target.add(this);
				containerForm.error("Error occur during the file delete process.");
				processErrors(target);
			}
			
			@Override
			public boolean isVisible() {
				return (containerForm.getModelObject().getSubjectFile()!=null && containerForm.getModelObject().getSubjectFile().getFilename() != null) && !containerForm.getModelObject().getSubjectFile().getFilename().isEmpty();
			}
		};
		deleteButton.add(new AttributeModifier("title", new Model<String>("Delete Attachment")));
		deleteButton.setOutputMarkupId(true);
		initStudyComponentChoice();
		initConsentTypeChoice();
		initConsentStatusChoice();
		initStudyComponentStatusChoice();
		initConsentDownloadChoice();
		initConsentHistoryPanel();
		
		addDetailFormComponents();
		attachValidators();

		historyButtonPanel = new HistoryButtonPanel(containerForm, arkCrudContainerVO.getEditButtonContainer(), arkCrudContainerVO.getDetailPanelFormContainer());
	}

	/**
	 * Initialise the Consent StudyComp Drop Down Choice Control
	 */
	@SuppressWarnings("unchecked")
	protected void initStudyComponentChoice() {
		Long sessionStudyId = (Long) SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.core.Constants.STUDY_CONTEXT_ID);
		Long sessionPersonId = (Long) SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.core.Constants.PERSON_CONTEXT_ID);
		Study study = iArkCommonService.getStudy(sessionStudyId);
		
		LinkSubjectStudy linkSubjectStudy=null;
		
		try {
			if(sessionPersonId!=null){
				linkSubjectStudy = iStudyService.getSubjectLinkedToStudy(sessionPersonId, study);
			}else{
				throw new EntityNotFoundException("The subject in context does not exist in system.");
			}
		 }catch (EntityNotFoundException e) {
				containerForm.error("The subject in context does not exist in system anymore.Please re-do the operation.");
		 }catch (ArkSystemException e) {
				containerForm.error("There was a system error. Please contact support.");
		}
		List<StudyComp> studyCompList = iArkCommonService.getStudyComponentByStudy(study);
		//Used a different approch on 2016-08-16
		//List<StudyComp> studyCompList = iArkCommonService.getStudyComponentsNotInThisSubject(study,linkSubjectStudy);
		ChoiceRenderer<StudyComp> defaultChoiceRenderer = new ChoiceRenderer<StudyComp>(Constants.NAME, Constants.ID);
		studyComponentChoice = new DropDownChoice<StudyComp>(Constants.CONSENT_STUDY_COMP, studyCompList, defaultChoiceRenderer){
		private static final long	serialVersionUID	= 1L;
			@Override
			protected void onBeforeRender() {
			if(isNew()){
				setEnabled(true);
			}else{
				setEnabled(false);
			}
				super.onBeforeRender();
			}
		};
		studyComponentChoice.add(new ArkDefaultFormFocusBehavior());
		studyComponentChoice.add(new AjaxFormComponentUpdatingBehavior("onchange") {


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
						//Stopping save with exsisting components.
						arkCrudContainerVO.getEditButtonContainer().get("save").setEnabled(false);
						target.add(arkCrudContainerVO.getEditButtonContainer());
						processErrors(target);
					}else{
						arkCrudContainerVO.getEditButtonContainer().get("save").setEnabled(true);
						target.add(arkCrudContainerVO.getEditButtonContainer());
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
	
	@SuppressWarnings("unchecked")
	protected void initStudyComponentStatusChoice() {
		List<StudyCompStatus> studyCompList = iArkCommonService.getStudyComponentStatus();
		ChoiceRenderer<StudyCompStatus> defaultChoiceRenderer = new ChoiceRenderer<StudyCompStatus>(Constants.NAME, Constants.ID);
		studyComponentStatusChoice = new DropDownChoice<StudyCompStatus>(Constants.CONSENT_STUDY_COMP_STATUS, studyCompList, defaultChoiceRenderer);
		studyComponentStatusChoice.add(new AjaxFormComponentUpdatingBehavior("onchange") {


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

	

	private void initConsentHistoryPanel() {
		consentHistoryPanel = new CollapsiblePanel("consentHistoryPanel", new Model<String>("Consent History"), false) {


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
		commentTxtArea.add(StringValidator.maximumLength(au.org.theark.core.Constants.GENERAL_FIELD_COMMENTS_MAX_LENGTH_500)).setLabel(new StringResourceModel("consent.comments.StringValidator.maximum", this, null));
		consentedBy.add(StringValidator.maximumLength(au.org.theark.core.Constants.GENERAL_FIELD_MAX_LENGTH_100)).setLabel(new StringResourceModel("consent.consentedBy.StringValidator.maximum",this,null));
		studyComponentChoice.setRequired(true).setLabel(new StringResourceModel("study.component.choice.required", this, null));
		consentStatusChoice.setRequired(true).setLabel(new StringResourceModel("consent.status.required", this, null));
		consentTypeChoice.setRequired(true).setLabel(new StringResourceModel("consent.type.required", this, null));
		studyComponentStatusChoice.setRequired(true).setLabel(new StringResourceModel("studyComponent.status.required", this, null));
		consentedDatePicker.add(DateValidator.maximum(new Date())).setLabel(new StringResourceModel("consent.consentDate.DateValidator.maximum", this, null));
		consentCompletedDtf.add(DateValidator.maximum(new Date())).setLabel(new StringResourceModel("consent.completedDate.DateValidator.maximum", this, null));
		consentRequestedDtf.add(DateValidator.maximum(new Date())).setLabel(new StringResourceModel("consent.requestedDate.DateValidator.maximum", this, null));
		consentReceivedDtf.add(DateValidator.maximum(new Date())).setLabel(new StringResourceModel("consent.receivedDate.DateValidator.maximum", this, null));

		consentCompletedDtf.setRequired(true).setLabel(new StringResourceModel("consent.completedDate.choice.required", this, null));
		consentRequestedDtf.setRequired(true).setLabel(new StringResourceModel("consent.requestedDate.choice.required", this, null));
		consentReceivedDtf.setRequired(true).setLabel(new StringResourceModel("consent.receivedDate.choice.required", this, null));
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
		arkCrudContainerVO.getDetailPanelFormContainer().add(fileUploadField);
		arkCrudContainerVO.getDetailPanelFormContainer().add(clearButton);
		//arkCrudContainerVO.getDetailPanelFormContainer().add(fileNameLbl);
		arkCrudContainerVO.getDetailPanelFormContainer().add(fileNameLnk);
		arkCrudContainerVO.getDetailPanelFormContainer().add(deleteButton);
		arkCrudContainerVO.getDetailPanelFormContainer().add(ajaxDownload);
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
		arkCrudContainerVO.getEditButtonContainer().get("save").setEnabled(true);
		target.add(arkCrudContainerVO.getEditButtonContainer());
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
		boolean isOkToSave = true;
		boolean isOkToSaveOnAttachment= true;
		 Consent exsistingConsent=containerForm.getModelObject().getConsent();
		 if(!isRequestedReceivedCompletedInOrder(exsistingConsent)){
			isOkToSave = false;
		 }
		 //Check for the saving ability or update the consent according to the attached file.
		 //Check for already attached file
		 if(iStudyService.isAlreadyHasFileAttached(exsistingConsent.getLinkSubjectStudy(), exsistingConsent.getStudyComp())){
			 //New filed attached
			 if(isNewFileAttached()){
				 // if attached check the new file and db recorded one equal and let them to save
				 if(isSameCheckSumOfAttachedAgainstDbRecorded(exsistingConsent.getLinkSubjectStudy(), exsistingConsent.getStudyComp())){
					 isOkToSaveOnAttachment=true;
				//else do not allow to save	 
				 }else{
					 isOkToSaveOnAttachment=false;
					 this.error("The Consent record you tried to update has already assigned attachment.");
					 processErrors(target);
				 }
			// if not new attached let them to save with other conditions	 
			 }else{
				 isOkToSaveOnAttachment=true;
			 }
		//if not already attached file in the db let them to save.	 
		 }else {
			 isOkToSaveOnAttachment=true;
		 }
		if (isOkToSave && isOkToSaveOnAttachment) {
				try {
					// Study in Context
					Long studyId = (Long) SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.core.Constants.STUDY_CONTEXT_ID);
					Study study = iArkCommonService.getStudy(studyId);
					containerForm.getModelObject().getConsent().setStudy(study);
					if (containerForm.getModelObject().getConsent().getId() == null) {
						iStudyService.create(containerForm.getModelObject().getConsent());
						this.info("Consent was successfuly created for the Subject ");
						createConsentFile();
						processErrors(target);
						// Store session object (used for history)
						SecurityUtils.getSubject().getSession().setAttribute(au.org.theark.core.Constants.PERSON_CONTEXT_CONSENT_ID, containerForm.getModelObject().getConsent().getId());
					}else{
						boolean consentFile=fileUploadField.getFileUpload()!=null;
						iStudyService.update(containerForm.getModelObject().getConsent(),consentFile);
						if(isNewFileAttached() && !iStudyService.isAlreadyHasFileAttached(exsistingConsent.getLinkSubjectStudy(), exsistingConsent.getStudyComp())){
							createConsentFile();
						}
						//Check for consent file upload	
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
		}else {
			processErrors(target);
		}
	}

	private void createConsentFile() throws ArkSystemException {
		FileUpload fileSubjectFile = fileUploadField.getFileUpload();
		if(fileSubjectFile != null) {
			LinkSubjectStudy linkSubjectStudy = null;
			Long studyId = (Long) SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.core.Constants.STUDY_CONTEXT_ID);
			Study study = iArkCommonService.getStudy(studyId);
			Long sessionPersonId = (Long) SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.core.Constants.PERSON_CONTEXT_ID);

			try {
				linkSubjectStudy = iArkCommonService.getSubject(sessionPersonId, study);
				if(containerForm.getModelObject().getSubjectFile()==null){
					containerForm.getModelObject().setSubjectFile(new SubjectFile());
				}
				containerForm.getModelObject().getSubjectFile().setLinkSubjectStudy(linkSubjectStudy);
				containerForm.getModelObject().getSubjectFile().setPayload(IOUtils.toByteArray(fileSubjectFile.getInputStream()));
				
				byte[] byteArray = fileSubjectFile.getMD5();
				String checksum = getHex(byteArray);
		
				// Set details of Consent File object
				containerForm.getModelObject().getSubjectFile().setStudyComp(containerForm.getModelObject().getConsent().getStudyComp());
				containerForm.getModelObject().getSubjectFile().setComments(containerForm.getModelObject().getConsent().getComments());
				containerForm.getModelObject().getSubjectFile().setChecksum(checksum);
				containerForm.getModelObject().getSubjectFile().setFilename(fileSubjectFile.getClientFileName());
				containerForm.getModelObject().getSubjectFile().setUserId(SecurityUtils.getSubject().getPrincipals().getPrimaryPrincipal().toString());
		
				// Save
				iStudyService.create(containerForm.getModelObject().getSubjectFile(),Constants.ARK_SUBJECT_CONSENT_DIR);
				this.info("Consent file: " + containerForm.getModelObject().getSubjectFile().getFilename() + " was created successfully");
			}
			catch (IOException ioe) {
				log.error("Failed to save the uploaded file: " + ioe);
			}
			catch (EntityNotFoundException e) {
				log.error(e.getMessage());
			}
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
		return (containerForm.getModelObject().getConsent().getId() == null);
	}
	
	@Override
	public void onBeforeRender() {
		super.onBeforeRender();
		consentHistoryPanel.setVisible(!isNew());
		historyButtonPanel.setVisible(!isNew());
	}
	/**
	 * Function to check for the order dates of the consent.
	 * @param consent
	 * @return
	 */
	private boolean isRequestedReceivedCompletedInOrder(Consent consent){
		Date requestedDate= null;
		Date receivedDate= null;
		Date completedDate = null;
		if(consent!=null && consent.getRequestedDate()!=null){requestedDate=consent.getRequestedDate();}
		if(consent!=null && consent.getReceivedDate()!=null){receivedDate=consent.getReceivedDate();}
		if(consent!=null && consent.getCompletedDate()!=null){completedDate=consent.getCompletedDate();}
		
		if(requestedDate!=null && receivedDate!=null && completedDate!=null){
			if((requestedDate.before(receivedDate) ||requestedDate.equals(receivedDate)) &&
					(receivedDate.before(completedDate) || receivedDate.equals(completedDate))){
				return true;
			}else{
				this.error("Please ensure chronological order of consent requested,received & complete dates.");
				return false;
			}
		}else if(requestedDate!=null && receivedDate!=null){
			if(requestedDate.before(receivedDate) ||requestedDate.equals(receivedDate)){
				return true;
			}else{
				this.error("Consent requested date cannot be greater than received date.");
				return false;
			}
		}else if(receivedDate!=null && completedDate!=null){
			if(receivedDate.before(completedDate) || receivedDate.equals(completedDate)){
				return true;
			}else{
				this.error("Consent received date cannot be greater than completed date.");
				return false;
			}
		}else if(requestedDate!=null && completedDate!=null){
			if(requestedDate.before(completedDate) || requestedDate.equals(completedDate)){
				return true;
			}else{
				this.error("Consent requested date cannot be greater than completed date.");
				return false;
			}
		}else if(requestedDate!=null || receivedDate!=null || completedDate!=null){
			return true;
		}else if(requestedDate==null && receivedDate==null && completedDate==null){
			return true;
		}else{
			this.error("Please ensure chronological order of consent requested,received & complete dates.");
			return false;
		}
	}
	/**
	 * 
	 * @param linkSubjectStudy
	 * @param studyComp
	 * @return
	 */
	private boolean isSameCheckSumOfAttachedAgainstDbRecorded(LinkSubjectStudy linkSubjectStudy,StudyComp studyComp){
		FileUpload fileSubjectFile = fileUploadField.getFileUpload();
		byte[] byteArray = fileSubjectFile.getMD5();
		String checksum = getHex(byteArray);
		SubjectFile subjectFile=iStudyService.getSubjectFileParticularConsent(linkSubjectStudy, studyComp);
		return subjectFile.getChecksum().equals(checksum);
	}
	/**
	 * 
	 * @return
	 */
	private boolean isNewFileAttached(){
		return (fileUploadField.getFileUpload()!=null);
	}
	
}
