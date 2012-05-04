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
package au.org.theark.lims.web.component.biospecimenupload;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Blob;
import java.util.Date;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.spring.injection.annot.SpringBean;
import au.org.theark.core.Constants;
import au.org.theark.core.dao.LobUtil;
import au.org.theark.core.service.IArkCommonService;
import au.org.theark.core.vo.UploadVO;
import au.org.theark.core.web.form.AbstractWizardForm;
import au.org.theark.core.web.form.AbstractWizardStepPanel;
import au.org.theark.lims.service.ILimsService;
import au.org.theark.lims.util.BiospecimenUploadReport;
import au.org.theark.lims.web.component.biospecimenupload.form.WizardForm;

/**
 * The 4th step of this wizard.
 */
public class BiospecimenUploadStep4 extends AbstractWizardStepPanel {

	@SpringBean(name = "lobUtil")
	private LobUtil			util;
	private static final long	serialVersionUID	= 2971945948091031160L;
	private Form<UploadVO>		containerForm;
	private WizardForm			wizardForm;
	
	@SuppressWarnings("unchecked")
	@SpringBean(name = au.org.theark.core.Constants.ARK_COMMON_SERVICE)
	private IArkCommonService						iArkCommonService;
	
	@SpringBean(name = au.org.theark.lims.web.Constants.LIMS_SERVICE)
	private ILimsService iLimsService;

	/**
	 * Construct.
	 */
	public BiospecimenUploadStep4(String id, Form<UploadVO> containerForm, WizardForm wizardForm) {
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
		containerForm.getModelObject().getUpload().setFilename(wizardForm.getFileName());

		// Perform actual import of data
		containerForm.getModelObject().getUpload().setStartTime(new Date(System.currentTimeMillis()));

		String fileFormat = containerForm.getModelObject().getUpload().getFileFormat().getName();
		char delimiterChar = containerForm.getModelObject().getUpload().getDelimiterType().getDelimiterCharacter();
		StringBuffer uploadReport = null;
		try {
			InputStream inputStream = containerForm.getModelObject().getFileUpload().getInputStream();
			uploadReport = iLimsService.uploadAndReportMatrixBiospecimenFile(containerForm.getModelObject().getUpload().getStudy(), inputStream, containerForm.getModelObject().getFileUpload().getSize(), fileFormat, delimiterChar);
		}
		catch (IOException e) {
			e.printStackTrace();
		}

		// Update the report
		updateUploadReport(uploadReport.toString());

		// Save all objects to the database
		save();
	}

	public void updateUploadReport(String importReport) {
		// Set Upload report
		BiospecimenUploadReport biospecimenUploadReport = new BiospecimenUploadReport();
		biospecimenUploadReport.appendDetails(containerForm.getModelObject().getUpload());
		biospecimenUploadReport.append(importReport);
		byte[] bytes = biospecimenUploadReport.getReport().toString().getBytes();
		//Blob uploadReportBlob = util.createBlob(bytes);
		containerForm.getModelObject().getUpload().setUploadReport(bytes);
	}

	private void save() {
		containerForm.getModelObject().getUpload().setFinishTime(new Date(System.currentTimeMillis()));
		containerForm.getModelObject().getUpload().setArkFunction(iArkCommonService.getArkFunctionByName(Constants.FUNCTION_KEY_VALUE_BIOSPECIMEN_UPLOAD));
		iArkCommonService.createUpload(containerForm.getModelObject().getUpload());
	}
}
