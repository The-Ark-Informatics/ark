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
package au.org.theark.study.web.component.subjectUpload;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Blob;
import java.util.Date;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.hibernate.Hibernate;

import au.org.theark.core.Constants;
import au.org.theark.core.model.geno.entity.Upload;
import au.org.theark.core.model.study.entity.StudyUpload;
import au.org.theark.core.service.IArkCommonService;
import au.org.theark.core.vo.UploadVO;
import au.org.theark.core.web.form.AbstractWizardForm;
import au.org.theark.core.web.form.AbstractWizardStepPanel;
import au.org.theark.study.job.StudyDataUploadExecutor;
import au.org.theark.study.service.IStudyService;
import au.org.theark.study.util.SubjectUploadReport;
import au.org.theark.study.web.component.subjectUpload.form.WizardForm;

/**
 * The 4th step of this wizard.
 */
public class SubjectUploadStep4 extends AbstractWizardStepPanel {
	/**
	 * 
	 */
	private static final long	serialVersionUID	= 2971945948091031160L;
	private Form<UploadVO>		containerForm;
	private WizardForm			wizardForm;

	@SpringBean(name = au.org.theark.core.Constants.ARK_COMMON_SERVICE)
	private IArkCommonService	iArkCommonService;

	@SpringBean(name = au.org.theark.core.Constants.STUDY_SERVICE)
	private IStudyService		iStudyService;

	/**
	 * Construct.
	 */
	public SubjectUploadStep4(String id, Form<UploadVO> containerForm, WizardForm wizardForm) {
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
		form.getNextButton().setEnabled(true);
		target.add(form.getNextButton());

		form.getArkExcelWorkSheetAsGrid().setVisible(false);
		target.add(form.getArkExcelWorkSheetAsGrid());
	}

	@Override
	public void onStepOutNext(AbstractWizardForm<?> form, AjaxRequestTarget target) {
		form.getNextButton().setEnabled(false);
		target.add(form.getNextButton());

		// Filename seems to be lost from model when moving between steps in wizard
		//travcontainerForm.getModelObject().getUpload().setFilename(wizardForm.getFileName());

		// Perform actual import of data
		containerForm.getModelObject().getUpload().setStartTime(new Date(System.currentTimeMillis()));

		String fileFormat = containerForm.getModelObject().getUpload().getFileFormat().getName();
		char delimiterChar = containerForm.getModelObject().getUpload().getDelimiterType().getDelimiterCharacter();
		//StringBuffer uploadReport = null;
		try {
	//		iArkCommonService.createUpload(containerForm.getModelObject().getUpload());
			

			
			//BELOW CODE MUST BE REINSTATED...Travis Is just trying some batch work now.
			InputStream inputStream = containerForm.getModelObject().getFileUpload().getInputStream();
			//uploadReport = iStudyService.uploadAndReportMatrixSubjectFile(inputStream, containerForm.getModelObject().getFileUpload().getSize(), fileFormat, delimiterChar);
			long size = containerForm.getModelObject().getFileUpload().getSize();
			Long uploadId = containerForm.getModelObject().getUpload().getId();
			//TODO ASAP remove this experimental else statemtnt, this is to test a batch call
			SubjectUploadReport report = updateUploadReport("");
			log.warn("..try a batch");
												
			log.warn("uploadId= " + uploadId + "size = " + size);
																							//com						study		uploadid, usr,study,fileformat,delim										
				StudyDataUploadExecutor task = new StudyDataUploadExecutor(iArkCommonService, iStudyService, inputStream, uploadId, //null user
									containerForm.getModelObject().getStudy(), fileFormat, delimiterChar, size, report);
				log.warn("..exectuter setup");

				task.run();
				
				log.warn("finished a batch?");
			
		}
		/*catch (IOException e) {
			e.printStackTrace();
		}*/
		catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		// Update the report
		//TODO ASAP put this back 
		//updateUploadReport(uploadReport.toString());

		// Save all objects to the database
		//save();
	}
	public SubjectUploadReport updateUploadReport(String importReport) {
		SubjectUploadReport subjectUploadReport = new SubjectUploadReport();
		subjectUploadReport.appendDetails(containerForm.getModelObject().getUpload());
		subjectUploadReport.append(importReport);
//		byte[] bytes = subjectUploadReport.getReport().toString().getBytes();
//		Blob uploadReportBlob = Hibernate.createBlob(bytes);
//		containerForm.getModelObject().getUpload().setUploadReport(uploadReportBlob);
		return subjectUploadReport;
	}

	/*
	private void save() {
		StudyUpload upload = containerForm.getModelObject().getUpload();

		iStudyService.refreshUpload(upload);
		upload.setFinishTime(new Date(System.currentTimeMillis()));
		upload.setArkFunction(iArkCommonService.getArkFunctionByName(Constants.FUNCTION_KEY_VALUE_SUBJECT_UPLOAD));
		iArkCommonService.updateUpload(upload);
	}*/
}
