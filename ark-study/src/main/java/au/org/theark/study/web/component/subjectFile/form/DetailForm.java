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
package au.org.theark.study.web.component.subjectFile.form;

import java.io.IOException;
import java.sql.Blob;
import java.util.List;

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
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.hibernate.Hibernate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
	/**
	 * 
	 */
	private static final long	serialVersionUID	= 8560698088787915274L;
	private transient Logger						log	= LoggerFactory.getLogger(DetailForm.class);
	@SpringBean(name = au.org.theark.study.web.Constants.STUDY_SERVICE)
	private IStudyService							studyService;

	@SuppressWarnings("unchecked")
	@SpringBean(name = au.org.theark.core.Constants.ARK_COMMON_SERVICE)
	private IArkCommonService						iArkCommonService;

	private TextField<String>						subjectFileIdTxtFld;
	private FileUploadField							fileSubjectFileField;
	private DropDownChoice<StudyComp>			studyComponentChoice;
	private TextArea<String>						commentsTxtArea;

	
	public DetailForm(String id, FeedbackPanel feedBackPanel, ArkCrudContainerVO arkCrudContainerVO,AbstractContainerForm<SubjectVO> containerForm){
		super(id,feedBackPanel,containerForm,arkCrudContainerVO);
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
		fileSubjectFileField.setRequired(true);
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
	}

	@Override
	protected void onSave(Form<SubjectVO> containerForm, AjaxRequestTarget target) {
		LinkSubjectStudy linkSubjectStudy = null;
		Long sessionPersonId = (Long) SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.core.Constants.PERSON_CONTEXT_ID);
		try {
			linkSubjectStudy = iArkCommonService.getSubject(sessionPersonId);
			containerForm.getModelObject().getSubjectFile().setLinkSubjectStudy(linkSubjectStudy);

			// Implement Save/Update
			if (containerForm.getModelObject().getSubjectFile().getId() == null) {
				// required for file uploads
				setMultiPart(true);

				// Retrieve file and store as Blob in database
				// TODO: AJAX-ified and asynchronous and hit database
				FileUpload fileSubjectFile = fileSubjectFileField.getFileUpload();

				try {
					// Copy file to BLOB object
					Blob payload = Hibernate.createBlob(fileSubjectFile.getInputStream());
					containerForm.getModelObject().getSubjectFile().setPayload(payload);
				}
				catch (IOException ioe) {
					log.error("Failed to save the uploaded file: " + ioe);
				}

				byte[] byteArray = fileSubjectFile.getMD5();
				String checksum = getHex(byteArray);

				// Set details of ConsentFile object
				containerForm.getModelObject().getSubjectFile().setChecksum(checksum);
				containerForm.getModelObject().getSubjectFile().setFilename(fileSubjectFile.getClientFileName());

				// Save
				studyService.create(containerForm.getModelObject().getSubjectFile());
				this.info("Attachment " + containerForm.getModelObject().getSubjectFile().getFilename() + " was created successfully");
				processErrors(target);
			}
			else {
				// Update
				studyService.update(containerForm.getModelObject().getSubjectFile());
				this.info("Attachment " + containerForm.getModelObject().getSubjectFile().getFilename() + " was updated successfully");
				processErrors(target);
			}

			onSavePostProcess(target);
			AjaxButton editButton = (AjaxButton) arkCrudContainerVO.getViewButtonContainer().get("edit");
			editButton.setEnabled(false);
			
		}
		catch (EntityNotFoundException e) {
			this.error("The record you tried to update is no longer available in the system");
			processErrors(target);
		}
		catch (ArkSystemException e) {
			this.error(e.getMessage());
			processErrors(target);
		}
	}

	static final String	HEXES	= "0123456789ABCDEF";

	public static String getHex(byte[] raw) {
		if (raw == null) {
			return null;
		}
		final StringBuilder hex = new StringBuilder(2 * raw.length);
		for (final byte b : raw) {
			hex.append(HEXES.charAt((b & 0xF0) >> 4)).append(HEXES.charAt((b & 0x0F)));
		}
		return hex.toString();
	}

	protected void onCancel(AjaxRequestTarget target) {
		SubjectVO subjectVo = new SubjectVO();
		LinkSubjectStudy linkSubjectStudy = null;
		Long sessionPersonId = (Long) SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.core.Constants.PERSON_CONTEXT_ID);
		try {
			linkSubjectStudy = iArkCommonService.getSubject(sessionPersonId);
		}
		catch (EntityNotFoundException e) {
			this.error("The Person/Subject in context does not exist in the system. Please contact support.");
			processErrors(target);
		}
		subjectVo.setLinkSubjectStudy(linkSubjectStudy);
		containerForm.setModelObject(subjectVo);
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

	/**
	 * 
	 */
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
		if (containerForm.getModelObject().getSubjectFile().getId() == null) {
			return true;
		}
		else {
			return false;
		}

	}

	/* (non-Javadoc)
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
