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
package au.org.theark.phenotypic.web.component.fieldUpload.form;

import java.io.IOException;
import java.sql.Blob;
import java.util.List;

import org.apache.shiro.SecurityUtils;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.form.upload.FileUpload;
import org.apache.wicket.markup.html.form.upload.FileUploadField;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.hibernate.Hibernate;

import au.org.theark.core.exception.ArkSystemException;
import au.org.theark.core.exception.EntityCannotBeRemoved;
import au.org.theark.core.model.pheno.entity.DelimiterType;
import au.org.theark.core.model.pheno.entity.FileFormat;
import au.org.theark.core.model.pheno.entity.PhenoCollectionUpload;
import au.org.theark.core.model.study.entity.Study;
import au.org.theark.core.service.IArkCommonService;
import au.org.theark.core.vo.ArkCrudContainerVO;
import au.org.theark.core.web.behavior.ArkDefaultFormFocusBehavior;
import au.org.theark.core.web.form.AbstractDetailForm;
import au.org.theark.phenotypic.model.vo.UploadVO;
import au.org.theark.phenotypic.service.Constants;
import au.org.theark.phenotypic.service.IPhenotypicService;

/**
 * @author cellis
 * 
 */
public class DetailForm extends AbstractDetailForm<UploadVO> {
	/**
	 * 
	 */
	private static final long					serialVersionUID	= -4510240384049368267L;

	@SpringBean(name = Constants.PHENOTYPIC_SERVICE)
	private IPhenotypicService					phenotypicService;

	@SpringBean(name = au.org.theark.core.Constants.ARK_COMMON_SERVICE)
	private IArkCommonService<Void>			iArkCommonService;

	private TextField<String>					uploadIdTxtFld;
	// private TextField<String> uploadFilenameTxtFld;
	private DropDownChoice<FileFormat>		fileFormatDdc;
	private FileUploadField						fileUploadField;
	// private UploadProgressBar uploadProgressBar;
	private DropDownChoice<DelimiterType>	delimiterTypeDdc;

	/**
	 * 
	 * @param id
	 * @param feedBackPanel
	 * @param containerForm
	 * @param arkCrudContainerVO
	 */
	public DetailForm(String id, FeedbackPanel feedBackPanel, ContainerForm containerForm, ArkCrudContainerVO arkCrudContainerVO) {
		super(id, feedBackPanel, containerForm, arkCrudContainerVO);
	}

	@SuppressWarnings("unchecked")
	private void initialiseDropDownChoices() {
		// Initialise Drop Down Choices
		java.util.Collection<FileFormat> fieldFormatCollection = phenotypicService.getFileFormats();
		ChoiceRenderer fieldFormatRenderer = new ChoiceRenderer(au.org.theark.phenotypic.web.Constants.FILE_FORMAT_NAME, au.org.theark.phenotypic.web.Constants.FILE_FORMAT_ID);
		fileFormatDdc = new DropDownChoice<FileFormat>(au.org.theark.phenotypic.web.Constants.UPLOADVO_UPLOAD_FILE_FORMAT, (List) fieldFormatCollection, fieldFormatRenderer);

		java.util.Collection<DelimiterType> delimiterTypeCollection = phenotypicService.getDelimiterTypes();
		ChoiceRenderer delimiterTypeRenderer = new ChoiceRenderer(au.org.theark.phenotypic.web.Constants.DELIMITER_TYPE_NAME, au.org.theark.phenotypic.web.Constants.DELIMITER_TYPE_ID);
		delimiterTypeDdc = new DropDownChoice<DelimiterType>(au.org.theark.phenotypic.web.Constants.UPLOADVO_UPLOAD_DELIMITER_TYPE, (List) delimiterTypeCollection, delimiterTypeRenderer);
	}

	public void initialiseDetailForm() {
		// Set up field on form here
		uploadIdTxtFld = new TextField<String>(au.org.theark.phenotypic.web.Constants.UPLOADVO_UPLOAD_ID);
		// uploadFilenameTxtFld = new TextField<String>(au.org.theark.phenotypic.web.Constants.UPLOADVO_UPLOAD_FILENAME);

		// progress bar for upload
		// uploadProgressBar = new UploadProgressBar("progress", ajaxSimpleUploadForm);

		// fileUpload for payload
		fileUploadField = new FileUploadField(au.org.theark.phenotypic.web.Constants.UPLOADVO_UPLOAD_FILENAME);
		fileUploadField.add(new ArkDefaultFormFocusBehavior());

		// Initialise Drop Down Choices
		initialiseDropDownChoices();

		attachValidators();
		addComponents();
	}

	protected void attachValidators() {
		// Field validation here
		fileUploadField.setRequired(true).setLabel(new StringResourceModel("error.filename.required", this, new Model<String>("Filename")));
		fileFormatDdc.setRequired(true).setLabel(new StringResourceModel("error.fileFormat.required", this, new Model<String>("File Format")));
		delimiterTypeDdc.setRequired(true).setLabel(new StringResourceModel("error.delimiterType.required", this, new Model<String>("Delimiter")));
	}

	private void addComponents() {
		arkCrudContainerVO.getDetailPanelContainer().add(uploadIdTxtFld.setEnabled(false));
		arkCrudContainerVO.getDetailPanelContainer().add(fileUploadField);
		arkCrudContainerVO.getDetailPanelContainer().add(fileFormatDdc);
		arkCrudContainerVO.getDetailPanelContainer().add(delimiterTypeDdc);
		add(arkCrudContainerVO.getDetailPanelContainer());
	}

	@Override
	protected void onSave(Form<UploadVO> containerForm, AjaxRequestTarget target) {
		// Implement Save/Update
		if (containerForm.getModelObject().getUpload().getId() == null) {
			setMultiPart(true); // multipart required for file uploads

			// Set study in context
			Long studyId = (Long) SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.core.Constants.STUDY_CONTEXT_ID);
			Study study = iArkCommonService.getStudy(studyId);

			// Get Collection in context
			Long sessionPhenoCollectionId = (Long) SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.phenotypic.web.Constants.SESSION_PHENO_COLLECTION_ID);

			// Retrieve file and store as Blob in databasse
			// TODO: AJAX-ified and asynchronous and hit database
			FileUpload fileUpload = fileUploadField.getFileUpload();

			try {
				// Copy file to BLOB object
				Blob payload = Hibernate.createBlob(fileUpload.getInputStream());
				containerForm.getModelObject().getUpload().setPayload(payload);
			}
			catch (IOException ioe) {
				System.out.println("Failed to save the uploaded file: " + ioe);
			}

			// Set details of Upload object
			containerForm.getModelObject().getUpload().setStudy(study);

			byte[] byteArray = fileUpload.getMD5();
			String checksum = getHex(byteArray);
			containerForm.getModelObject().getUpload().setChecksum(checksum);
			containerForm.getModelObject().getUpload().setFilename(fileUpload.getClientFileName());

			containerForm.getModelObject().setPhenoCollection(phenotypicService.getPhenoCollection(sessionPhenoCollectionId));

			// Set details of link table object
			PhenoCollectionUpload phenoCollectionUpload = new PhenoCollectionUpload();
			phenoCollectionUpload.setCollection(phenotypicService.getPhenoCollection(sessionPhenoCollectionId));
			phenoCollectionUpload.setUpload(containerForm.getModelObject().getUpload());
			containerForm.getModelObject().setPhenoCollectionUpload(phenoCollectionUpload);

			// Save
			phenotypicService.createUpload(containerForm.getModelObject());

			this.info("Data upload " + containerForm.getModelObject().getUpload().getFilename() + " was created successfully");
			processErrors(target);
		}
		else {
			// Update
			phenotypicService.updateUpload(containerForm.getModelObject().getUpload());
			this.info("Data upload " + containerForm.getModelObject().getUpload().getFilename() + " was updated successfully");
			processErrors(target);
		}

		onSavePostProcess(target);
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
		// Implement Cancel
		UploadVO uploadVO = new UploadVO();
		containerForm.setModelObject(uploadVO);
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
		setMultiPart(true); // multipart required for file uploads

		try {
			phenotypicService.deleteUpload(containerForm.getModelObject().getUpload());
			this.info("Upload file " + containerForm.getModelObject().getUpload().getFilename() + " was deleted successfully");
		}
		catch (ArkSystemException e) {
			this.error(e.getMessage());
		}
		catch (EntityCannotBeRemoved e) {
			this.error(e.getMessage());
		}

		// Display delete confirmation message
		target.add(feedBackPanel);
		
		// Move focus back to Search form
		UploadVO uploadVo = new UploadVO();
		setModelObject(uploadVo);
		onCancel(target);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see au.org.theark.core.web.form.AbstractDetailForm#isNew()
	 */
	@Override
	protected boolean isNew() {
		if (containerForm.getModelObject().getUpload().getId() == null) {
			return true;
		}
		else {
			return false;
		}

	}
}
