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
package au.org.theark.phenotypic.web.component.phenodatauploader;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Blob;
import java.util.List;

import org.apache.shiro.SecurityUtils;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.upload.FileUpload;
import org.apache.wicket.markup.html.form.upload.FileUploadField;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.util.io.IOUtils;
import org.hibernate.Hibernate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import au.org.theark.core.model.pheno.entity.PhenoCollectionUpload;
import au.org.theark.core.model.study.entity.ArkFunction;
import au.org.theark.core.model.study.entity.CustomFieldGroup;
import au.org.theark.core.model.study.entity.DelimiterType;
import au.org.theark.core.model.study.entity.FileFormat;
import au.org.theark.core.model.study.entity.Study;
import au.org.theark.core.service.IArkCommonService;
import au.org.theark.core.web.form.AbstractWizardForm;
import au.org.theark.core.web.form.AbstractWizardStepPanel;
import au.org.theark.phenotypic.model.vo.PhenoFieldDataUploadVO;
import au.org.theark.phenotypic.service.IPhenotypicService;
import au.org.theark.phenotypic.web.Constants;
import au.org.theark.phenotypic.web.component.phenodatauploader.form.WizardForm;

/**
 * The first step of this wizard.
 */
public class PhenoDataUploadStep1 extends AbstractWizardStepPanel {
	/**
	 * 
	 */
	private static final long						serialVersionUID		= 4272918747277155957L;

	public java.util.Collection<String>			validationMessages	= null;

	@SpringBean(name = au.org.theark.core.Constants.ARK_COMMON_SERVICE)
	private IArkCommonService<Void>				iArkCommonService;

	@SpringBean(name = au.org.theark.phenotypic.service.Constants.PHENOTYPIC_SERVICE)
	private IPhenotypicService						iPhenotypicService;

	private transient Logger						log						= LoggerFactory.getLogger(PhenoDataUploadContainerPanel.class);

	private Form<PhenoFieldDataUploadVO> 	containerForm;
	private DropDownChoice<CustomFieldGroup>	questionnaireDdc;
	private FileUploadField							fileUploadField;
	// private UploadProgressBar uploadProgressBar;
	private DropDownChoice<DelimiterType>		delimiterTypeDdc;
	private WizardForm								wizardForm;

	public PhenoDataUploadStep1(String id) {
		super(id);
		initialiseDetailForm();
	}

	public PhenoDataUploadStep1(String id, Form<PhenoFieldDataUploadVO> containerForm, WizardForm wizardForm) {
		super(id, "Step 1/5: Select data file to upload", "Select the file containing data, the file type and the specified delimiter, click Next to continue.");

		this.containerForm = containerForm;
		this.setWizardForm(wizardForm);
		initialiseDetailForm();
	}

	private void initialiseDropDownChoices() {
		// Initialise Drop Down Choices
		java.util.Collection<DelimiterType> delimiterTypeCollection = iArkCommonService.getDelimiterTypes();
		ChoiceRenderer delimiterTypeRenderer = new ChoiceRenderer(au.org.theark.phenotypic.web.Constants.DELIMITER_TYPE_NAME, au.org.theark.phenotypic.web.Constants.DELIMITER_TYPE_ID);
		delimiterTypeDdc = new DropDownChoice<DelimiterType>(au.org.theark.phenotypic.web.Constants.UPLOADVO_UPLOAD_DELIMITER_TYPE, (List) delimiterTypeCollection, delimiterTypeRenderer);
		// Set to default delimiterType
//		containerForm.getModelObject().getUpload().setDelimiterType(iArkCommonService.getDelimiterType(new Long(1)));
		initQuestionnaireDdc();
	}

	
	private void initPhenoQuestionnaireDdc() {
		Long sessionStudyId = (Long) SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.core.Constants.STUDY_CONTEXT_ID);
		Study study = null;
		
		if (sessionStudyId != null && sessionStudyId > 0) {
			study = iArkCommonService.getStudy(sessionStudyId);
		}
	}
	
	private void initQuestionnaireDdc() {
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
		ChoiceRenderer<CustomFieldGroup> choiceRenderer = new ChoiceRenderer<CustomFieldGroup>(Constants.QUESTIONNAIRE_NAME, Constants.QUESTIONNAIRE_ID);
		questionnaireDdc = new DropDownChoice<CustomFieldGroup>("questionnaire", questionnaireList, choiceRenderer);
		questionnaireDdc.setNullValid(false);
	}

	public void initialiseDetailForm() {

		// fileUpload for payload
		fileUploadField = new FileUploadField(au.org.theark.phenotypic.web.Constants.UPLOADVO_UPLOAD_FILENAME);
		// Initialise Drop Down Choices
		initialiseDropDownChoices();

		attachValidators();
		addComponents();
	}

	protected void attachValidators() {
		// Field validation here
		questionnaireDdc.setRequired(true).setLabel(new StringResourceModel("error.collection.required", this, new Model<String>("Collection")));
		fileUploadField.setRequired(true).setLabel(new StringResourceModel("error.filename.required", this, new Model<String>("Filename")));
		delimiterTypeDdc.setRequired(true).setLabel(new StringResourceModel("error.delimiterType.required", this, new Model<String>("Delimiter")));
	}

	private void addComponents() {
		// Add components here
		add(questionnaireDdc);
		add(fileUploadField);
		add(delimiterTypeDdc);
	}

	@Override
	public void handleWizardState(AbstractWizardForm<?> form, AjaxRequestTarget target) {
	}

	@Override
	public void onStepOutNext(AbstractWizardForm<?> form, AjaxRequestTarget target) {
		saveFileInMemory();
	}

	public void setWizardForm(WizardForm wizardForm) {
		this.wizardForm = wizardForm;
	}

	public WizardForm getWizardForm() {
		return wizardForm;
	}

	private void saveFileInMemory() {
		// Set study in context
		Long studyId = (Long) SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.core.Constants.STUDY_CONTEXT_ID);
		Study study = iArkCommonService.getStudy(studyId);

		// Retrieve file and store as Blob in database
		// TODO: AJAX-ified and asynchronous and hit database
		FileUpload fileUpload = fileUploadField.getFileUpload();
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
		wizardForm.setFileName(fileUpload.getClientFileName());
	}
	
	private void saveFileInMemoryOrig() {
		// Set study in context
		Long studyId = (Long) SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.core.Constants.STUDY_CONTEXT_ID);
		Study study = iArkCommonService.getStudy(studyId);

		// Retrieve file and store as Blob in database
		// TODO: AJAX-ified and asynchronous and hit database
		FileUpload fileUpload = fileUploadField.getFileUpload();
//		containerForm.getModelObject().setFileUpload(fileUpload);

		try {
			// Copy file to BLOB object
			Blob payload = Hibernate.createBlob(fileUpload.getInputStream());
//			containerForm.getModelObject().getUpload().setPayload(payload);
		}
		catch (IOException ioe) {
			System.out.println("Failed to save the uploaded file: " + ioe);
		}

		// Set details of Upload object
//		containerForm.getModelObject().getUpload().setStudy(study);
//		String filename = containerForm.getModelObject().getFileUpload().getClientFileName();
//		String fileFormatName = filename.substring(filename.lastIndexOf('.') + 1).toUpperCase();
		FileFormat fileFormat = new FileFormat();
//		fileFormat = iPhenotypicService.getFileFormatByName(fileFormatName);
//		containerForm.getModelObject().getUpload().setFileFormat(fileFormat);

		byte[] byteArray = fileUpload.getMD5();
		String checksum = getHex(byteArray);
//		containerForm.getModelObject().getUpload().setChecksum(checksum);
//		containerForm.getModelObject().getUpload().setFilename(fileUpload.getClientFileName());
		wizardForm.setFileName(fileUpload.getClientFileName());

		// Set details of link table object
		PhenoCollectionUpload phenoCollectionUpload = new PhenoCollectionUpload();
//		PhenoCollection phenoCollection = iPhenotypicService.getPhenoCollection(containerForm.getModelObject().getPhenoCollection().getId());
//		phenoCollectionUpload.setCollection(phenoCollection);

//		phenoCollectionUpload.setUpload(containerForm.getModelObject().getUpload());
//		containerForm.getModelObject().setPhenoCollectionUpload(phenoCollectionUpload);
	}
}
