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
package au.org.theark.phenotypic.web.component.phenofielduploader;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Blob;
import java.util.Collection;
import java.util.Date;

import jxl.Workbook;
import jxl.read.biff.BiffException;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.hibernate.Hibernate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import au.org.theark.core.exception.ArkSystemException;
import au.org.theark.core.exception.FileFormatException;
import au.org.theark.core.model.study.entity.ArkFunction;
import au.org.theark.core.model.study.entity.CustomFieldUpload;
import au.org.theark.core.model.study.entity.FileFormat;
import au.org.theark.core.model.study.entity.Study;
import au.org.theark.core.service.IArkCommonService;
import au.org.theark.core.util.CustomFieldImporter;
import au.org.theark.core.web.form.AbstractWizardForm;
import au.org.theark.core.web.form.AbstractWizardStepPanel;
import au.org.theark.phenotypic.model.vo.PhenoFieldUploadVO;
import au.org.theark.phenotypic.service.Constants;
import au.org.theark.phenotypic.service.IPhenotypicService;
import au.org.theark.phenotypic.util.PhenoUploadReport;
import au.org.theark.phenotypic.web.component.phenofielduploader.form.WizardForm;

/**
 * The 4th step of this wizard.
 */
public class FieldUploadStep4 extends AbstractWizardStepPanel {
	/**
	 * 
	 */
	private static final long			serialVersionUID	= -2788948560672351760L;
	static Logger							log					= LoggerFactory.getLogger(FieldUploadStep4.class);
	private Form<PhenoFieldUploadVO>				containerForm;
	private WizardForm					wizardForm;
	@SpringBean(name = Constants.PHENOTYPIC_SERVICE)
	private IPhenotypicService			iPhenotypicService;
	@SpringBean(name = au.org.theark.core.Constants.ARK_COMMON_SERVICE)
	private IArkCommonService<Void>	iArkCommonService;

	/**
	 * Construct.
	 */
	public FieldUploadStep4(String id, Form<PhenoFieldUploadVO> containerForm, WizardForm wizardForm) {
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
		initialiseDetailForm();
		form.getArkExcelWorkSheetAsGrid().setVisible(false);
		target.add(form.getArkExcelWorkSheetAsGrid());
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
		ArkFunction arkFunction = iArkCommonService.getArkFunctionByName(au.org.theark.core.Constants.FUNCTION_KEY_VALUE_DATA_DICTIONARY);
		CustomFieldImporter fieldImporter = new CustomFieldImporter(study, arkFunction, iArkCommonService, fileFormat, delimiterChar);

		try {
			log.info("Uploading data dictionary file");
			InputStream inputStream = containerForm.getModelObject().getFileUpload().getInputStream();

			if (fileFormat.equalsIgnoreCase("XLS")) {
				Workbook w;
				try {
					w = Workbook.getWorkbook(inputStream);
					inputStream = fieldImporter.convertXlsToCsv(w);
					inputStream.reset();
				}
				catch (BiffException e) {
					log.error(e.getMessage());
				}
				catch (IOException e) {
					log.error(e.getMessage());
				}
			}

			uploadReport = fieldImporter.uploadAndReportMatrixDataDictionaryFile(inputStream, containerForm.getModelObject().getFileUpload().getSize());

			// Determined FieldUpload entities
			containerForm.getModelObject().setCustomFieldUploadCollection(fieldImporter.getFieldUploadList());
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
		updateUploadReport(uploadReport.toString());

		// Save all objects to the database
		save();
	}

	public void updateUploadReport(String importReport) {
		// Set Upload report
		PhenoUploadReport phenoUploadReport = new PhenoUploadReport();
		phenoUploadReport.appendDetails(containerForm.getModelObject().getUpload());
		phenoUploadReport.append(importReport);
		byte[] bytes = phenoUploadReport.getReport().toString().getBytes();
		Blob uploadReportBlob = Hibernate.createBlob(bytes);
		containerForm.getModelObject().getUpload().setUploadReport(uploadReportBlob);
	}

	private void save() {
		containerForm.getModelObject().getUpload().setFinishTime(new Date(System.currentTimeMillis()));
		ArkFunction arkFunction = iArkCommonService.getArkFunctionByName(au.org.theark.core.Constants.FUNCTION_KEY_VALUE_DATA_DICTIONARY);
		containerForm.getModelObject().getUpload().setArkFunction(arkFunction);
		iArkCommonService.createUpload(containerForm.getModelObject().getUpload());
		Collection<CustomFieldUpload> cfUploadLinks = containerForm.getModelObject().getCustomFieldUploadCollection();
		for (CustomFieldUpload cfUpload : cfUploadLinks) {
			cfUpload.setStudyUpload(containerForm.getModelObject().getUpload());
			iArkCommonService.createCustomFieldUpload(cfUpload);
		}
	}
}
