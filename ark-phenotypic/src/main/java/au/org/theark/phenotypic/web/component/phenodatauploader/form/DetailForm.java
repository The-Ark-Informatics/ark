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
package au.org.theark.phenotypic.web.component.phenodatauploader.form;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.apache.shiro.SecurityUtils;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.upload.FileUpload;
import org.apache.wicket.markup.html.form.upload.FileUploadField;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.util.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import au.org.theark.core.exception.FileFormatException;
import au.org.theark.core.exception.PhenotypicSystemException;
import au.org.theark.core.model.pheno.entity.DelimiterType;
import au.org.theark.core.model.study.entity.ArkFunction;
import au.org.theark.core.model.study.entity.CustomFieldGroup;
import au.org.theark.core.model.study.entity.FileFormat;
import au.org.theark.core.model.study.entity.Study;
import au.org.theark.core.service.IArkCommonService;
import au.org.theark.core.vo.ArkCrudContainerVO;
import au.org.theark.core.web.form.AbstractDetailForm;
import au.org.theark.phenotypic.model.vo.PhenoFieldDataUploadVO;
import au.org.theark.phenotypic.service.Constants;
import au.org.theark.phenotypic.service.IPhenotypicService;
import au.org.theark.phenotypic.util.PhenoDataImportValidator;

/**
 * @author cellis
 * @author elam
 * 
 */
public class DetailForm extends AbstractDetailForm<PhenoFieldDataUploadVO> {
	/**
	 * 
	 */
	private static final long	serialVersionUID	= -8266132080909805310L;

	@SpringBean(name = Constants.PHENOTYPIC_SERVICE)
	private IPhenotypicService					phenotypicService;

	@SpringBean(name = au.org.theark.core.Constants.ARK_COMMON_SERVICE)
	private IArkCommonService<Void>			iArkCommonService;
	private DropDownChoice<CustomFieldGroup>	questionnaireDdc;
	private FileUploadField						fileUploadField;
	private DropDownChoice<DelimiterType>		delimiterTypeDdc;
	/* Markup and checkbox */
	private WebMarkupContainer				overrideDataValidationContainer;
	private CheckBox						overrideDataValidationChkBox;
	
	private transient Logger					log						= LoggerFactory.getLogger(DetailForm.class);
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
	
	public void onBeforeRender() {
		super.onBeforeRender();
		deleteButton.setVisible(false);
		cancelButton.setVisible(false);
		arkCrudContainerVO.getSearchResultPanelContainer().setVisible(true);
	}
	
	public void initialiseDetailForm() {

		overrideDataValidationContainer = new WebMarkupContainer("overrideDataValidationContainer");
		overrideDataValidationContainer.setOutputMarkupId(true);
		overrideDataValidationChkBox = new CheckBox("overrideDataValidationChkBox");
		overrideDataValidationChkBox.setOutputMarkupId(false);
		overrideDataValidationChkBox.setVisible(true);
		containerForm.getModelObject().setOverrideDataValidationChkBox(false);
		
		overrideDataValidationChkBox.add(new AjaxFormComponentUpdatingBehavior("onChange") {
			@Override
			protected void onUpdate(AjaxRequestTarget target) {
				//TODO
				
			}
		});
		
		// fileUpload for payload
		fileUploadField = new FileUploadField(au.org.theark.phenotypic.web.Constants.UPLOADVO_UPLOAD_FILENAME);
		// Initialise Drop Down Choices
		initialiseDropDownChoices();

		attachValidators();
		
		addDetailFormComponents();
	}
	

	private void initialiseDropDownChoices() {
		// Initialise Drop Down Choices
		java.util.Collection<au.org.theark.core.model.study.entity.DelimiterType> delimiterTypeCollection = iArkCommonService.getDelimiterTypes();
		ChoiceRenderer delimiterTypeRenderer = new ChoiceRenderer(au.org.theark.phenotypic.web.Constants.DELIMITER_TYPE_NAME, au.org.theark.phenotypic.web.Constants.DELIMITER_TYPE_ID);
		delimiterTypeDdc = new DropDownChoice<DelimiterType>(au.org.theark.phenotypic.web.Constants.UPLOADVO_UPLOAD_DELIMITER_TYPE, (List) delimiterTypeCollection, delimiterTypeRenderer);
	
		// Get a list of questionnaires for the subject in context by default
		CustomFieldGroup cfgForStudyCriteria = new CustomFieldGroup(); 
		//Get the Study From Context and set the correct ArkFunction
		Long studyId = (Long) SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.core.Constants.STUDY_CONTEXT_ID);
		Study studyInContext = iArkCommonService.getStudy(studyId);
		cfgForStudyCriteria.setStudy(studyInContext);
		ArkFunction function = iArkCommonService.getArkFunctionByName(au.org.theark.core.Constants.FUNCTION_KEY_VALUE_DATA_DICTIONARY);
		cfgForStudyCriteria.setArkFunction(function);
		cfgForStudyCriteria.setPublished(true);	//make sure that we don't return non-published Questionnaires
	
		List<CustomFieldGroup> questionnaireList = iArkCommonService.getCustomFieldGroups(cfgForStudyCriteria, 0, Integer.MAX_VALUE);
		ChoiceRenderer<CustomFieldGroup> choiceRenderer = new ChoiceRenderer<CustomFieldGroup>(au.org.theark.phenotypic.web.Constants.QUESTIONNAIRE_NAME, au.org.theark.phenotypic.web.Constants.QUESTIONNAIRE_ID);
		questionnaireDdc = new DropDownChoice<CustomFieldGroup>("questionnaire", questionnaireList, choiceRenderer);
		questionnaireDdc.setNullValid(false);
	}

	protected void attachValidators() {
		// Field validation here
		questionnaireDdc.setRequired(true).setLabel(new StringResourceModel("error.collection.required", this, new Model<String>("Collection")));
		fileUploadField.setRequired(true).setLabel(new StringResourceModel("error.filename.required", this, new Model<String>("Filename")));
		delimiterTypeDdc.setRequired(true).setLabel(new StringResourceModel("error.delimiterType.required", this, new Model<String>("Delimiter")));
	}


	@Override
	protected void onSave(Form<PhenoFieldDataUploadVO> containerForm, AjaxRequestTarget target) {
		
		saveFileInMemory();
		containerForm.getModelObject().getUpload().setStartTime(new Date(System.currentTimeMillis()));
		containerForm.getModelObject().getUpload().setFinishTime(new Date(System.currentTimeMillis()));
		PhenoFieldDataUploadVO vo = containerForm.getModelObject();
		//Do the validation
		PhenoDataImportValidator validator = new PhenoDataImportValidator(iArkCommonService, phenotypicService, vo);
		try {
			
			InputStream stream = containerForm.getModelObject().getFileUpload().getInputStream();
		    Collection<String> validationMessages = validator.validatePhenoDataImportFileFormat(stream, stream.toString().length());
		    Boolean overrideValidation = overrideDataValidationChkBox.getModelObject();
			if( !validator.getErrorCells().isEmpty()){
				//Stop from further processing display an error message to the user or down-load an error report
				log.info("There were errors cannot proceed with upload of data.");
			}
			else{
				
				if(validator.getWarningCells().isEmpty()) {
					log.info("Proceed to upload/import data.No warnings or errrors in validation.");
				}
				else if(!validator.getWarningCells().isEmpty() && overrideValidation){
					log.info("Proceed to upload/import data with warnings.");
				}else{
					log.info("There are warnings and user has not specified to override validation. Aborting upload/import data.");
				}
			}
			//TODO: Why do we need the type? Is it necessary
			//containerForm.getModelObject().getUpload().setUploadType("FIELD_DATA");
		}catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (PhenotypicSystemException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FileFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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
		PhenoFieldDataUploadVO uploadVO = new PhenoFieldDataUploadVO();
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

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see au.org.theark.core.web.form.AbstractDetailForm#isNew()
	 */
	@Override
	protected boolean isNew() {
		
		return true;
	}

	/* (non-Javadoc)
	 * @see au.org.theark.core.web.form.AbstractDetailForm#addDetailFormComponents()
	 */
	@Override
	protected void addDetailFormComponents() {
		arkCrudContainerVO.getDetailPanelFormContainer().add(questionnaireDdc);
		arkCrudContainerVO.getDetailPanelFormContainer().add(fileUploadField);
		arkCrudContainerVO.getDetailPanelFormContainer().add(delimiterTypeDdc);
		
		overrideDataValidationContainer.add(overrideDataValidationChkBox);
		arkCrudContainerVO.getDetailPanelFormContainer().add(overrideDataValidationContainer);
	}
	
	private void saveFileInMemory() {
		// Set study in context
		Long studyId = (Long) SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.core.Constants.STUDY_CONTEXT_ID);
		Study study = iArkCommonService.getStudy(studyId);

		// Retrieve file and store as Blob in database
		// TODO: AJAX-ified and asynchronous and hit database
		FileUpload fileUpload = fileUploadField.getFileUpload();
		PhenoFieldDataUploadVO vo = containerForm.getModelObject();
		containerForm.getModelObject().setFileUpload(fileUpload);
		
		InputStream inputStream = null;
		BufferedOutputStream outputStream = null;
		File temp = null;
		try {
			// Copy all the contents of the file upload to a temp file (to read multiple times)...
			inputStream = containerForm.getModelObject().getFileUpload().getInputStream();
			// Create temp file 
			temp = File.createTempFile("phenoFieldUploadBlob", ".tmp");
			containerForm.getModelObject().setTempFile(temp);
			// Delete temp file when program exits (just in case manual delete fails later)
			temp.deleteOnExit();
			// Write to temp file
			outputStream = new BufferedOutputStream(new FileOutputStream(temp));
			IOUtils.copy(inputStream, outputStream);
		}
		catch (IOException ioe) {
			log.error("IOException " + ioe.getMessage());
			// Something failed, so there is temp file is not valid (step 2 should check for null)
			temp.delete();
			temp = null;
			containerForm.getModelObject().setTempFile(temp);
		}
		finally {
			if (inputStream != null) {
				try {
					inputStream.close();
				}
				catch (IOException e) {
					log.error("Unable to close inputStream: " + e.getMessage());
				}
			}
			if (outputStream != null) {
				try {
					outputStream.flush();
					outputStream.close();
				}
				catch (IOException e) {
					log.error("Unable to close outputStream: " + e.getMessage());
				}
			}
		}

		// Set details of Upload object
		containerForm.getModelObject().getUpload().setStudy(study);
		String filename = containerForm.getModelObject().getFileUpload().getClientFileName();
		String fileFormatName = filename.substring(filename.lastIndexOf('.') + 1).toUpperCase();
		FileFormat fileFormat = new FileFormat();
		fileFormat = iArkCommonService.getFileFormatByName(fileFormatName);
		containerForm.getModelObject().getUpload().setFileFormat(fileFormat);

		byte[] byteArray = fileUpload.getMD5();
		String checksum = getHex(byteArray);
		containerForm.getModelObject().getUpload().setChecksum(checksum);
		containerForm.getModelObject().getUpload().setFilename(fileUpload.getClientFileName());
	}
}
