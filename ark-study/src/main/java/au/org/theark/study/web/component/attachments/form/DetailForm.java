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
package au.org.theark.study.web.component.attachments.form;



import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.apache.shiro.SecurityUtils;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.form.upload.FileUpload;
import org.apache.wicket.markup.html.form.upload.FileUploadField;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.util.io.IOUtils;
import org.apache.wicket.util.lang.Bytes;
import org.apache.wicket.util.upload.FileUploadBase.SizeLimitExceededException;
import org.apache.wicket.util.upload.FileUploadException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import au.org.theark.core.exception.ArkBaseException;
import au.org.theark.core.exception.ArkFileNotFoundException;
import au.org.theark.core.exception.ArkSystemException;
import au.org.theark.core.exception.EntityNotFoundException;
import au.org.theark.core.model.study.entity.LinkSubjectStudy;
import au.org.theark.core.model.study.entity.Study;
import au.org.theark.core.model.study.entity.StudyComp;
import au.org.theark.core.service.IArkCommonService;
import au.org.theark.core.vo.ArkCrudContainerVO;
import au.org.theark.core.vo.SubjectVO;
import au.org.theark.core.web.behavior.ArkDefaultFormFocusBehavior;
import au.org.theark.core.web.form.AbstractContainerForm;
import au.org.theark.core.web.form.AbstractDetailForm;
import au.org.theark.study.service.IStudyService;
import au.org.theark.study.web.Constants;

/**
 * @author cellis
 * 
 */
public class DetailForm extends AbstractDetailForm<SubjectVO> {

	private static final long				serialVersionUID	= 8560698088787915274L;
	private transient Logger				log					= LoggerFactory.getLogger(DetailForm.class);
	@SpringBean(name = au.org.theark.study.web.Constants.STUDY_SERVICE)
	private IStudyService					iStudyService;

	@SuppressWarnings("unchecked")
	@SpringBean(name = au.org.theark.core.Constants.ARK_COMMON_SERVICE)
	private IArkCommonService				iArkCommonService;
	
	private TextField<String>				subjectFileIdTxtFld;
	private FileUploadField					fileSubjectFileField;
	private DropDownChoice<StudyComp>	studyComponentChoice;
	private TextArea<String>				commentsTxtArea;

	public DetailForm(String id, FeedbackPanel feedBackPanel, ArkCrudContainerVO arkCrudContainerVO, AbstractContainerForm<SubjectVO> containerForm) {
		super(id, feedBackPanel, containerForm, arkCrudContainerVO);
	}

	@SuppressWarnings("unchecked")
	private void initialiseDropDownChoices() {
		// Initialise Drop Down Choices
		Long studyId = (Long) SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.core.Constants.STUDY_CONTEXT_ID);
		Study studyInContext = iArkCommonService.getStudy(studyId);
		List<StudyComp> studyCompList = iArkCommonService.getStudyComponentByStudy(studyInContext);
		ChoiceRenderer<StudyComp> defaultChoiceRenderer = new ChoiceRenderer<StudyComp>(Constants.NAME, Constants.ID);
		studyComponentChoice = new DropDownChoice<StudyComp>(Constants.SUBJECT_FILE_STUDY_COMP, studyCompList, defaultChoiceRenderer);
	}

	public void initialiseDetailForm() {
		// Set up field on form here
		subjectFileIdTxtFld = new TextField<String>(au.org.theark.study.web.Constants.SUBJECT_FILE_ID);
		// fileSubjectFile for payload (attached to filename key)
		fileSubjectFileField = new FileUploadField(au.org.theark.study.web.Constants.SUBJECT_FILE_FILENAME);
		//fileSubjectFileField.setRequired(true);
		fileSubjectFileField.add(new ArkDefaultFormFocusBehavior());

		// Initialise Drop Down Choices
		initialiseDropDownChoices();

		commentsTxtArea = new TextArea<String>(au.org.theark.study.web.Constants.SUBJECT_FILE_COMMENTS);

		attachValidators();
		addDetailFormComponents();
	}

	protected void attachValidators() {
		// Field validation here
		fileSubjectFileField.setRequired(true).setLabel(new StringResourceModel("subjectFile.filename.required", this, null));
		// max upload size, 200MB
		
	}

	@Override
	protected void onSave(Form<SubjectVO> containerForm, AjaxRequestTarget target) {
		LinkSubjectStudy linkSubjectStudy = null;
		Long studyId = (Long) SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.core.Constants.STUDY_CONTEXT_ID);
		Study study = iArkCommonService.getStudy(studyId);
		Long sessionPersonId = (Long) SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.core.Constants.PERSON_CONTEXT_ID);
		String userId = SecurityUtils.getSubject().getPrincipal().toString();
		FileUpload fileSubjectFile = fileSubjectFileField.getFileUpload();
			try {
				linkSubjectStudy = iArkCommonService.getSubject(sessionPersonId, study);
				containerForm.getModelObject().getSubjectFile().setLinkSubjectStudy(linkSubjectStudy);
				if(au.org.theark.core.Constants.MAXIMUM_PERMISSABLE_FILE_SIZE > fileSubjectFile.getSize()){
				// Implement Save/Update
				if (containerForm.getModelObject().getSubjectFile().getId() == null) {
					// required for file uploads
					setMultiPart(true);
					
					// Retrieve file and store as Blob in database
					// TODO: AJAX-ified and asynchronous and hit database
					try {
						containerForm.getModelObject().getSubjectFile().setPayload(IOUtils.toByteArray(fileSubjectFile.getInputStream()));
					}
					catch (IOException e) {
						log.error("IOException trying to convert inputstream" + e);
					}
	
					byte[] byteArray = fileSubjectFile.getMD5();
					String checksum = getHex(byteArray);
		
					// Set details of ConsentFile object
					containerForm.getModelObject().getSubjectFile().setChecksum(checksum);
					containerForm.getModelObject().getSubjectFile().setFilename(fileSubjectFile.getClientFileName());
					containerForm.getModelObject().getSubjectFile().setUserId(userId);
	
					// Save
					iStudyService.create(containerForm.getModelObject().getSubjectFile(),Constants.ARK_SUBJECT_ATTACHEMENT_DIR);
					//this.info("Attachment " + containerForm.getModelObject().getSubjectFile().getFilename() + " was created successfully");
					this.saveInformation();
					//processErrors(target);
			}else{
					//FileUpload fileSubjectFile = fileSubjectFileField.getFileUpload();
	
					try {
						containerForm.getModelObject().getSubjectFile().setPayload(IOUtils.toByteArray(fileSubjectFile.getInputStream()));
					}
					catch (IOException e) {
						log.error("IOException trying to convert inputstream" + e);
					}
	
					byte[] byteArray = fileSubjectFile.getMD5();
					String checksum = getHex(byteArray);
	
					// Set details of ConsentFile object
					//containerForm.getModelObject().getSubjectFile().setChecksum(checksum);
					containerForm.getModelObject().getSubjectFile().setFilename(fileSubjectFile.getClientFileName());
					containerForm.getModelObject().getSubjectFile().setUserId(userId);
					
					// Update
					try {
						iStudyService.update(containerForm.getModelObject().getSubjectFile(),checksum);
					} catch (ArkFileNotFoundException e) {
						this.error("Couldn't find the file.");
					}
					this.updateInformation();
					//this.info("Attachment " + containerForm.getModelObject().getSubjectFile().getFilename() + " was updated successfully");
				}
				}else{
					this.error("The file size is larger than the maximum allowed size. Maximum allowed size ="+ (int)(au.org.theark.core.Constants.MAXIMUM_PERMISSABLE_FILE_SIZE/Math.pow(10, 6))+ 
							   " MB and the file you tried to upload is "+(int)(fileSubjectFile.getSize()/Math.pow(10, 6))+ " MB");
				}	
			//processErrors(target);
			}
			catch (EntityNotFoundException e) {
				this.error("The record you tried to update is no longer available in the system.");
			}
			catch (ArkSystemException e) {
				this.error("An error occurred while saving the file attachment. Please contact the system administrator.");
			}finally {
				processErrors(target);
				onSavePostProcess(target);
				AjaxButton deleteButton = (AjaxButton) arkCrudContainerVO.getEditButtonContainer().get("delete");
				deleteButton.setEnabled(false);
		}
	}

	protected void onCancel(AjaxRequestTarget target) {
		SubjectVO subjectVo = new SubjectVO();
		LinkSubjectStudy linkSubjectStudy = null;
		Long sessionPersonId = (Long) SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.core.Constants.PERSON_CONTEXT_ID);
		Long studyId = (Long) SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.core.Constants.STUDY_CONTEXT_ID);
		Study study = iArkCommonService.getStudy(studyId);
		try {
			linkSubjectStudy = iArkCommonService.getSubject(sessionPersonId, study);
		}
		catch (EntityNotFoundException e) {
			this.error("The Person/Subject selected does not exist in the system. Please contact the system administrator.");
		}
		subjectVo.setLinkSubjectStudy(linkSubjectStudy);
		containerForm.setModelObject(subjectVo);
		processErrors(target);
	}

	@Override
	protected void processErrors(AjaxRequestTarget target) {
		target.add(feedBackPanel);
	}

	public AjaxButton getDeleteButton() {
		return deleteButton;
	}

	public void setDeleteButton(AjaxButton deleteButton) {
		this.deleteButton = deleteButton;
	}


	protected void onDeleteConfirmed(AjaxRequestTarget target, String selection) {
		// Display delete confirmation message
		target.add(feedBackPanel);
		// Move focus back to Search form
		SubjectVO subjectVO = new SubjectVO();
		setModelObject(subjectVO);
		onCancel(target);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see au.org.theark.core.web.form.AbstractDetailForm#isNew()
	 */
	@Override
	protected boolean isNew() {
			return containerForm.getModelObject().getSubjectFile().getId() == null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see au.org.theark.core.web.form.AbstractDetailForm#addDetailFormComponents()
	 */
	@Override
	protected void addDetailFormComponents() {
		// Add components
		subjectFileIdTxtFld.setEnabled(false);
		subjectFileIdTxtFld.setVisible(true);

		arkCrudContainerVO.getDetailPanelFormContainer().add(subjectFileIdTxtFld);
		arkCrudContainerVO.getDetailPanelFormContainer().add(fileSubjectFileField);
		arkCrudContainerVO.getDetailPanelFormContainer().add(studyComponentChoice);
		arkCrudContainerVO.getDetailPanelFormContainer().add(commentsTxtArea);

		// TODO: AJAXify the form to show progress bar
		// ajaxSimpleConsentFileForm.add(new UploadProgressBar("progress", ajaxSimpleConsentFileForm));
		// add(ajaxSimpleConsentFileForm);
		add(arkCrudContainerVO.getDetailPanelFormContainer());

	}
}
