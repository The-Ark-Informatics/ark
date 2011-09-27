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
package au.org.theark.study.web.component.consentFile.form;

import java.io.IOException;
import java.sql.Blob;

import org.apache.shiro.SecurityUtils;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.form.upload.FileUpload;
import org.apache.wicket.markup.html.form.upload.FileUploadField;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.util.file.File;
import org.hibernate.Hibernate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import au.org.theark.core.exception.ArkSystemException;
import au.org.theark.core.exception.EntityNotFoundException;
import au.org.theark.core.model.study.entity.Consent;
import au.org.theark.core.service.IArkCommonService;
import au.org.theark.core.vo.ArkCrudContainerVO;
import au.org.theark.core.vo.ConsentVO;
import au.org.theark.core.web.form.AbstractContainerForm;
import au.org.theark.core.web.form.AbstractDetailForm;
import au.org.theark.study.service.IStudyService;

/**
 * @author cellis
 * 
 */
@SuppressWarnings({ "serial", "unused" })
public class DetailForm extends AbstractDetailForm<ConsentVO> {
	private transient Logger	log	= LoggerFactory.getLogger(DetailForm.class);
	@SpringBean(name = au.org.theark.study.web.Constants.STUDY_SERVICE)
	private IStudyService		studyService;

	@SpringBean(name = au.org.theark.core.Constants.ARK_COMMON_SERVICE)
	private IArkCommonService	iArkCommonService;

	private int						mode;

	private TextField<String>	consentFileIdTxtFld;
	private TextField<String>	consentFileFilenameTxtFld;
	private FileUploadField		fileConsentFileField;

	// private ConsentFileProgressBar uploadProgressBar;

	/**
	 * @param id
	 * @param feedBackPanel
	 * @param resultListContainer
	 * @param detailPanelContainer
	 * @param detailPanelFormContainer
	 * @param searchPanelContainer
	 * @param viewButtonContainer
	 * @param editButtonContainer
	 * @param containerForm
	 */
	public DetailForm(String id, FeedbackPanel feedBackPanel, ArkCrudContainerVO arkCrudContainerVO, AbstractContainerForm<ConsentVO> containerForm) {

		super(id, feedBackPanel,containerForm,arkCrudContainerVO);

		// Check consent in context
		if (containerForm.getModelObject().getConsent() == null) {
			log.debug("There is no consent in context");
			// disableDetailForm(null, "There is no consent in context");
		}
	}

	@SuppressWarnings("unchecked")
	private void initialiseDropDownChoices() {
		// Initialise Drop Down Choices
		// java.util.Collection<FileFormat> fieldFormatCollection = studyService.getFileFormats();
		// ChoiceRenderer fieldFormatRenderer = new ChoiceRenderer(au.org.theark.study.web.Constants.FILE_FORMAT_NAME,
		// au.org.theark.study.web.Constants.FILE_FORMAT_ID);
		// fileFormatDdc = new DropDownChoice<FileFormat>(au.org.theark.study.web.Constants.UPLOADVO_UPLOAD_FILE_FORMAT, (List) fieldFormatCollection,
		// fieldFormatRenderer);

		// java.util.Collection<DelimiterType> delimiterTypeCollection = studyService.getDelimiterTypes();
		// ChoiceRenderer delimiterTypeRenderer = new ChoiceRenderer(au.org.theark.study.web.Constants.DELIMITER_TYPE_NAME,
		// au.org.theark.study.web.Constants.DELIMITER_TYPE_ID);
		// delimiterTypeDdc = new DropDownChoice<DelimiterType>(au.org.theark.study.web.Constants.UPLOADVO_UPLOAD_DELIMITER_TYPE, (List)
		// delimiterTypeCollection, delimiterTypeRenderer);
	}

	public void initialiseDetailForm() {
		// Set up field on form here
		consentFileIdTxtFld = new TextField<String>(au.org.theark.study.web.Constants.CONSENT_FILE_ID);
		// uploadFilenameTxtFld = new TextField<String>(au.org.theark.study.web.Constants.UPLOADVO_UPLOAD_FILENAME);

		// progress bar for upload
		// uploadProgressBar = new ConsentFileProgressBar("progress", ajaxSimpleConsentFileForm);

		// fileConsentFile for payload (attached to filename key)
		fileConsentFileField = new FileUploadField(au.org.theark.study.web.Constants.CONSENT_FILE_FILENAME);

		// Initialise Drop Down Choices
		initialiseDropDownChoices();

		attachValidators();
		addComponents();
	}

	protected void attachValidators() {
		// Field validation here
		// fileConsentFileField.setRequired(true).setLabel(new StringResourceModel("error.filename.required", this, new Model<String>("Filename")));
		// fileFormatDdc.setRequired(true).setLabel(new StringResourceModel("error.fileFormat.required", this, new Model<String>("File Format")));
		// delimiterTypeDdc.setRequired(true).setLabel(new StringResourceModel("error.delimiterType.required", this, new Model<String>("Delimiter")));
	}

	private void addComponents() {
		// Add components
		consentFileIdTxtFld.setEnabled(false);
		consentFileIdTxtFld.setVisible(true);
		arkCrudContainerVO.getDetailPanelFormContainer().add(consentFileIdTxtFld);
		arkCrudContainerVO.getDetailPanelFormContainer().add(fileConsentFileField);
		// detailPanelFormContainer.add(fileFormatDdc);
		// detailPanelFormContainer.add(delimiterTypeDdc);

		// TODO: AJAXify the form to show progress bar
		// ajaxSimpleConsentFileForm.add(new ConsentFileProgressBar("progress", ajaxSimpleConsentFileForm));
		// add(ajaxSimpleConsentFileForm);

		add(arkCrudContainerVO.getDetailPanelFormContainer());
	}

	private void createDirectoryIfNeeded(String directoryName) {
		File theDir = new File(directoryName);

		// if the directory does not exist, create it
		if (!theDir.exists()) {
			log.debug("Creating directory: " + directoryName);
			theDir.mkdir();
		}
	}

	@Override
	protected void onSave(Form<ConsentVO> containerForm, AjaxRequestTarget target) {
		// Use consent in context
		Long sessionConsentId = (Long) SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.core.Constants.PERSON_CONTEXT_CONSENT_ID);
		Consent consent = new Consent();

		try {
			// Set consentFile.consent reference
			consent = studyService.getConsent(sessionConsentId);
			containerForm.getModelObject().getConsentFile().setConsent(consent);

			// Implement Save/Update
			if (containerForm.getModelObject().getConsentFile().getId() == null) {
				// required for file uploads
				setMultiPart(true);

				// Retrieve file and store as Blob in database
				// TODO: AJAX-ified and asynchronous and hit database
				FileUpload fileConsentFile = fileConsentFileField.getFileUpload();

				try {
					// Copy file to BLOB object
					Blob payload = Hibernate.createBlob(fileConsentFile.getInputStream());
					containerForm.getModelObject().getConsentFile().setPayload(payload);
				}
				catch (IOException ioe) {
					log.error("Failed to save the uploaded file: " + ioe);
				}

				byte[] byteArray = fileConsentFile.getMD5();
				String checksum = getHex(byteArray);

				// Set details of ConsentFile object
				containerForm.getModelObject().getConsentFile().setChecksum(checksum);
				containerForm.getModelObject().getConsentFile().setFilename(fileConsentFile.getClientFileName());

				// Save
				log.debug("Saving consentFile");
				studyService.create(containerForm.getModelObject().getConsentFile());
				this.info("Consent file " + containerForm.getModelObject().getConsentFile().getFilename() + " was created successfully");
				processErrors(target);
			}
			else {
				// Update
				log.debug("Updating consentFile");
				studyService.update(containerForm.getModelObject().getConsentFile());
				this.info("Consent file " + containerForm.getModelObject().getConsentFile().getFilename() + " was updated successfully");
				processErrors(target);
			}

			onSavePostProcess(target);
		}
		catch (EntityNotFoundException e) {
			this.error("The Consent file record you tried to update is no longer available in the system");
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
		ConsentVO consentVO = new ConsentVO();
		Consent consent = containerForm.getModelObject().getConsent();
		containerForm.setModelObject(consentVO);
		containerForm.getModelObject().setConsent(consent);
		onCancelPostProcess(target);
	}

	protected void onEditCancel(AjaxRequestTarget target) {
		ConsentVO consentVO = new ConsentVO();
		Consent consent = containerForm.getModelObject().getConsent();
		containerForm.setModelObject(consentVO);
		containerForm.getModelObject().setConsent(consent);
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
		// required for file uploads
		setMultiPart(true);

		// TODO:(CE) To handle Business and System Exceptions here
		// studyService.deleteConsentFile(containerForm.getModelObject().getConsentFile());
		// this.info("Consent file " + containerForm.getModelObject().getConsentFile().getFilename() + " was deleted successfully");

		// Display delete confirmation message
		target.add(feedBackPanel);
		// TODO Implement Exceptions in PhentoypicService
		// } catch (UnAuthorizedOperation e) { this.error("You are not authorised to manage study components for the given study " +
		// study.getName()); processFeedback(target); } catch (ArkSystemException e) {
		// this.error("A System error occurred, we will have someone contact you."); processFeedback(target); }

		// Move focus back to Search form
		ConsentVO consentVo = new ConsentVO();
		setModelObject(consentVo);
		onCancel(target);
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
