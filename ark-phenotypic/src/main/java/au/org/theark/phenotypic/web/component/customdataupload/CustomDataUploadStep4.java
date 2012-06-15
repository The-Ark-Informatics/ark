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
package au.org.theark.phenotypic.web.component.customdataupload;

import java.io.InputStream;
import java.util.List;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.spring.injection.annot.SpringBean;

import au.org.theark.core.service.IArkCommonService;
import au.org.theark.core.vo.UploadVO;
import au.org.theark.core.web.form.AbstractWizardForm;
import au.org.theark.core.web.form.AbstractWizardStepPanel;
import au.org.theark.phenotypic.service.IPhenotypicService;
import au.org.theark.phenotypic.util.*;
import au.org.theark.phenotypic.web.component.customdataupload.form.WizardForm;
import au.org.theark.phenotypic.job.CustomDataUploadExecutor;
//import au.org.theark.phenotypic.job.StudyDataUploadExecutor;
//import au.org.theark.phenotypic.service.IStudyService;
//import au.org.theark.phenotypic.util.SubjectUploadReport;

public class CustomDataUploadStep4 extends AbstractWizardStepPanel {
	private static final long	serialVersionUID	= 2971945948091031160L;
	private Form<UploadVO>		containerForm;
	private WizardForm			wizardForm;

	@SpringBean(name = au.org.theark.core.Constants.ARK_COMMON_SERVICE)
	private IArkCommonService	iArkCommonService;

	@SpringBean(name = au.org.theark.core.Constants.STUDY_SERVICE)
	private IPhenotypicService		iPhenoService;

	public CustomDataUploadStep4(String id, Form<UploadVO> containerForm, WizardForm wizardForm) {
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
		// Filename seems to be lost from model when moving between steps in wizard?  is this a symptom of something greater?
		containerForm.getModelObject().getUpload().setFilename(wizardForm.getFileName());

		String fileFormat = containerForm.getModelObject().getUpload().getFileFormat().getName();
		char delimiterChar = containerForm.getModelObject().getUpload().getDelimiterType().getDelimiterCharacter();
		try {			
			List<String> uidsToUpload = containerForm.getModelObject().getUidsToUpload();
			log.info("________________________________________________________" + "about to try passing list of uids is of size " + uidsToUpload.size() );
			InputStream inputStream = containerForm.getModelObject().getFileUpload().getInputStream();
			long size = containerForm.getModelObject().getFileUpload().getSize();
			Long uploadId = containerForm.getModelObject().getUpload().getId();
			String report = generateInitialUploadReport();

			Subject currentUser = SecurityUtils.getSubject();
			Long studyId = (Long) currentUser.getSession().getAttribute(au.org.theark.core.Constants.STUDY_CONTEXT_ID);

		/*	if(containerForm.getModelObject().getUpload().getUploadType().getName().equalsIgnoreCase("Subject Demographic Data")){
				StudyDataUploadExecutor task = new StudyDataUploadExecutor(iArkCommonService, iPhenoService, inputStream, uploadId, //null user
							studyId, fileFormat, delimiterChar, size, report, uidsToUpload);
				task.run();
			}			else */
			if(containerForm.getModelObject().getUpload().getUploadType().getName().equalsIgnoreCase("Custom Data Sets")){
				CustomDataUploadExecutor task = new CustomDataUploadExecutor(iArkCommonService, iPhenoService, inputStream, uploadId, //null user
							studyId, fileFormat, delimiterChar, size, report, uidsToUpload);
				task.run();
			}
			
		}
		catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
	public String generateInitialUploadReport() {
		PhenoUploadReport subjectUploadReport = new PhenoUploadReport();
		subjectUploadReport.appendDetails(containerForm.getModelObject().getUpload());
		return subjectUploadReport.getReport().toString();
	}

}