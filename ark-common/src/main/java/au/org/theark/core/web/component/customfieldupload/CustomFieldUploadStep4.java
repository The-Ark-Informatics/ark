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
package au.org.theark.core.web.component.customfieldupload;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.Date;

import jxl.Workbook;
import jxl.read.biff.BiffException;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.util.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import au.org.theark.core.Constants;
import au.org.theark.core.exception.ArkSystemException;
import au.org.theark.core.exception.FileFormatException;
import au.org.theark.core.model.study.entity.ArkFunction;
import au.org.theark.core.model.study.entity.ArkModule;
import au.org.theark.core.model.study.entity.CustomFieldCategory;
import au.org.theark.core.model.study.entity.CustomFieldCategoryUpload;
import au.org.theark.core.model.study.entity.CustomFieldUpload;
import au.org.theark.core.model.study.entity.FileFormat;
import au.org.theark.core.model.study.entity.Payload;
import au.org.theark.core.model.study.entity.Study;
import au.org.theark.core.model.study.entity.UploadLevel;
import au.org.theark.core.service.IArkCommonService;
import au.org.theark.core.util.CustomFieldCategoryImporter;
import au.org.theark.core.util.CustomFieldImporter;
import au.org.theark.core.util.ICustomImporter;
import au.org.theark.core.util.UploadReport;
import au.org.theark.core.vo.CustomFieldUploadVO;
import au.org.theark.core.web.component.customfieldupload.form.WizardForm;
import au.org.theark.core.web.form.AbstractWizardForm;
import au.org.theark.core.web.form.AbstractWizardStepPanel;

/**
 * The 4th step of this wizard.
 */
public class CustomFieldUploadStep4 extends AbstractWizardStepPanel {

	private static final long				serialVersionUID	= -2788948560672351760L;
	static Logger								log					= LoggerFactory.getLogger(CustomFieldUploadStep4.class);
	private Form<CustomFieldUploadVO>	containerForm;
	private WizardForm						wizardForm;
	@SpringBean(name = au.org.theark.core.Constants.ARK_COMMON_SERVICE)
	private IArkCommonService<Void>		iArkCommonService;
	private ICustomImporter iCustomImporter;

	public CustomFieldUploadStep4(String id, Form<CustomFieldUploadVO> containerForm, WizardForm wizardForm) {
		super(id, "Step 4/5: Confirm Upload", "Data will now be written to the database, click Next to continue, otherwise click Cancel.");
		this.containerForm = containerForm;
		this.wizardForm = wizardForm;
		initialiseDetailForm();
	}

	private void initialiseDetailForm() {
	}

	@Override
	public void handleWizardState(AbstractWizardForm<?> form, AjaxRequestTarget target) {
	}

	@Override
	public void onStepInNext(AbstractWizardForm<?> form, AjaxRequestTarget target) {
		form.getArkExcelWorkSheetAsGrid().setVisible(false);
		target.add(form.getArkExcelWorkSheetAsGrid());
		File temp = containerForm.getModelObject().getTempFile();
		if (temp != null && temp.exists()) {
		}
		else {
			// Stop progress because of missing temp file
			error("An unexpected error occurred. Can not proceed due to missing temporary file.");
			form.getNextButton().setEnabled(false);
		}
	}

	@Override
	public void onStepOutNext(AbstractWizardForm<?> form, AjaxRequestTarget target) {
		// Filename seems to be lost from model when moving between steps in wizard
		containerForm.getModelObject().getUpload().setFilename(wizardForm.getFileName());

		// Perform actual upload of data
		containerForm.getModelObject().getUpload().setStartTime(new Date(System.currentTimeMillis()));
		StringBuffer uploadReport = null;
		String filename = containerForm.getModelObject().getFileUpload().getClientFileName();
		String fileFormat = filename.substring(filename.lastIndexOf('.') + 1).toUpperCase();
		FileFormat fileFormatObj;
		fileFormatObj = iArkCommonService.getFileFormatByName(fileFormat);
		containerForm.getModelObject().getUpload().setFileFormat(fileFormatObj);

		char delimiterChar = containerForm.getModelObject().getUpload().getDelimiterType().getDelimiterCharacter();

		Subject currentUser = SecurityUtils.getSubject();
		Long studyId = (Long) currentUser.getSession().getAttribute(au.org.theark.core.Constants.STUDY_CONTEXT_ID);
		Study study = iArkCommonService.getStudy(studyId);
		Long sessionModuleId = (Long) SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.core.Constants.ARK_MODULE_KEY);
		ArkModule arkModule = iArkCommonService.getArkModuleById(sessionModuleId);
		//We have to decide the custom filed/category goes under which function to the DB.
		//At the moment if it is subject or the lims
		//Knowing the Module and the current fuction we can dicide that
		ArkFunction currentFunction=containerForm.getModelObject().getUpload().getArkFunction();
		UploadLevel uploadLevel=containerForm.getModelObject().getUpload().getUploadLevel();
		ArkFunction adjustedArkFunctionForCustomField=null;
		
		//Common custom field update split to study in here.
		if(arkModule.getName().equals(au.org.theark.core.Constants.ARK_MODULE_STUDY)&& 
		    currentFunction.getName().equals(au.org.theark.core.Constants.FUNCTION_KEY_VALUE_SUBJECT_CUSTOM_FIELD_UPLOAD)){
			// Field upload
			if(uploadLevel.getName().equalsIgnoreCase(au.org.theark.core.web.component.customfieldupload.Constants.UPLOAD_LEVEL_FIELD)){
				adjustedArkFunctionForCustomField=iArkCommonService.getArkFunctionByName(au.org.theark.core.Constants.FUNCTION_KEY_VALUE_SUBJECT_CUSTOM_FIELD);
				iCustomImporter = new CustomFieldImporter(study, adjustedArkFunctionForCustomField, iArkCommonService, fileFormat, delimiterChar);
			//Category upload		
			}else if(uploadLevel.getName().equalsIgnoreCase(au.org.theark.core.web.component.customfieldupload.Constants.UPLOAD_LEVEL_CATEGORY)){
				adjustedArkFunctionForCustomField=iArkCommonService.getArkFunctionByName(au.org.theark.core.Constants.FUNCTION_KEY_VALUE_SUBJECT_CUSTOM_FIELD_CATEGORY);
				iCustomImporter  = new CustomFieldCategoryImporter(study, adjustedArkFunctionForCustomField, iArkCommonService, fileFormat, delimiterChar);
			}
		}

		//Common custom field update split to lims in here.
		if(arkModule.getName().equals(au.org.theark.core.Constants.ARK_MODULE_LIMS)&& 
			    currentFunction.getName().equals(au.org.theark.core.Constants.FUNCTION_KEY_VALUE_LIMS_CUSTOM_FIELD_UPLOAD)){
			// Field upload
			if(uploadLevel.getName().equalsIgnoreCase(au.org.theark.core.web.component.customfieldupload.Constants.UPLOAD_LEVEL_FIELD)){
				adjustedArkFunctionForCustomField=iArkCommonService.getArkFunctionByName(au.org.theark.core.Constants.FUNCTION_KEY_VALUE_LIMS_CUSTOM_FIELD);
				iCustomImporter = new CustomFieldImporter(study, adjustedArkFunctionForCustomField, iArkCommonService, fileFormat, delimiterChar);
			//Category upload		
			}else if(uploadLevel.getName().equalsIgnoreCase(au.org.theark.core.web.component.customfieldupload.Constants.UPLOAD_LEVEL_CATEGORY)){
				adjustedArkFunctionForCustomField=iArkCommonService.getArkFunctionByName(au.org.theark.core.Constants.FUNCTION_KEY_VALUE_LIMS_CUSTOM_FIELD_CATEGORY);
				iCustomImporter  = new CustomFieldCategoryImporter(study, adjustedArkFunctionForCustomField, iArkCommonService, fileFormat, delimiterChar);
			}
		}
		//Need to persist the custom field category

		try {
			log.info("Uploading data dictionary file");
			InputStream inputStream = containerForm.getModelObject().getFileUpload().getInputStream();

			if (fileFormat.equalsIgnoreCase("XLS")) {
				Workbook w;
				try {
					w = Workbook.getWorkbook(inputStream);
					inputStream = iCustomImporter.convertXlsToCsv(w);
					inputStream.reset();
				}
				catch (BiffException e) {
					log.error(e.getMessage());
				}
				catch (IOException e) {
					log.error(e.getMessage());
				}
			}

			uploadReport = iCustomImporter.uploadAndReportMatrixDataDictionaryFile(inputStream, containerForm.getModelObject().getFileUpload().getSize());

			// Determined FieldUpload entities
			if(iCustomImporter instanceof CustomFieldCategoryImporter)
					containerForm.getModelObject().setCustomFieldUploadCategoryCollection(((CustomFieldCategoryImporter)iCustomImporter).getFieldUploadList());
			else if(iCustomImporter instanceof CustomFieldImporter){
					containerForm.getModelObject().setCustomFieldUploadCollection(((CustomFieldImporter)iCustomImporter).getFieldUploadList());
			}
		}
		catch (FileFormatException ffe) {
			log.error(Constants.FILE_FORMAT_EXCEPTION + ffe);
		}
		catch (IOException ioe) {
			log.error(ioe.getMessage());
		}
		catch (ArkSystemException ase) {
			log.error(ase.getMessage());
		}

		// Update the report
		if(uploadReport!=null){
			updateUploadReport(uploadReport.toString());
		}
		// Save all objects to the database
		save(iCustomImporter);
	}

	public void updateUploadReport(String importReport) {
		// Set Upload report
		UploadReport phenoUploadReport = new UploadReport();
		phenoUploadReport.appendDetails(containerForm.getModelObject().getUpload());
		phenoUploadReport.append(importReport);
		byte[] bytes = phenoUploadReport.getReport().toString().getBytes();
		containerForm.getModelObject().getUpload().setUploadReport(bytes);
	}

	private void save(ICustomImporter iCustomImporter) {
		File temp = containerForm.getModelObject().getTempFile();
		InputStream inputStream = null;
		try {
			inputStream = new BufferedInputStream(new FileInputStream(temp));
			byte[] bytes = IOUtils.toByteArray(inputStream);
			Payload payload = iArkCommonService.createPayload(bytes);
			containerForm.getModelObject().getUpload().setPayload(payload);

			containerForm.getModelObject().getUpload().setFinishTime(new Date(System.currentTimeMillis()));
			containerForm.getModelObject().getUpload().setUploadStatus(iArkCommonService.getUploadStatusFor(Constants.UPLOAD_STATUS_COMPLETED));		
			//TODO investigate if only one created
			iArkCommonService.createUpload(containerForm.getModelObject().getUpload());
			
			if (iCustomImporter instanceof CustomFieldCategoryImporter){
				Collection<CustomFieldCategoryUpload> cfcUploadLinks = containerForm.getModelObject().getCustomFieldUploadCategoryCollection();
				for (CustomFieldCategoryUpload cfcUpload : cfcUploadLinks) {
					cfcUpload.setUpload(containerForm.getModelObject().getUpload());
					iArkCommonService.createCustomFieldCategoryUpload(cfcUpload);
				}
			}else if(iCustomImporter instanceof CustomFieldImporter){
				Collection<CustomFieldUpload> cfUploadLinks = containerForm.getModelObject().getCustomFieldUploadCollection();
				for (CustomFieldUpload cfUpload : cfUploadLinks) {
					cfUpload.setUpload(containerForm.getModelObject().getUpload());
					iArkCommonService.createCustomFieldUpload(cfUpload);
				}
			}
		}
		catch ( Exception ioe) {
			log.error("Failed to save the uploaded file: " + ioe);
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
			if (temp != null) {
				// Attempt manual a delete
				temp.delete();
				temp = null;
				containerForm.getModelObject().setTempFile(temp);
			}
		}
	}
}
