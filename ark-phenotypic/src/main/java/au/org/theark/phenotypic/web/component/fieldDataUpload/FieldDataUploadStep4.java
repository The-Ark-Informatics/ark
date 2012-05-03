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
package au.org.theark.phenotypic.web.component.fieldDataUpload;

import java.sql.Blob;
import java.util.Date;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.spring.injection.annot.SpringBean;

import au.org.theark.core.dao.LobUtil;
import au.org.theark.core.web.form.AbstractWizardForm;
import au.org.theark.core.web.form.AbstractWizardStepPanel;
import au.org.theark.phenotypic.model.vo.UploadVO;
import au.org.theark.phenotypic.service.Constants;
import au.org.theark.phenotypic.service.IPhenotypicService;
import au.org.theark.phenotypic.util.PhenoUploadReport;
import au.org.theark.phenotypic.web.component.fieldDataUpload.form.WizardForm;

/**
 * The 4th step of this wizard.
 */
public class FieldDataUploadStep4 extends AbstractWizardStepPanel {

	@SpringBean(name = "lobUtil")
	private LobUtil			util;
	
	private static final long	serialVersionUID	= -2788948560672351760L;
	private Form<UploadVO>		containerForm;
	private WizardForm			wizardForm;
	@SpringBean(name = Constants.PHENOTYPIC_SERVICE)
	private IPhenotypicService	iPhenotypicService;

	/**
	 * Construct.
	 */
	public FieldDataUploadStep4(String id, Form<UploadVO> containerForm, WizardForm wizardForm) {
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
	}

	@Override
	public void onStepOutNext(AbstractWizardForm<?> form, AjaxRequestTarget target) {
		// Filename seems to be lost from model when moving between steps in wizard
		containerForm.getModelObject().getUpload().setFilename(wizardForm.getFileName());

		containerForm.getModelObject().getUpload().setStartTime(new Date(System.currentTimeMillis()));
		StringBuffer uploadReport = null;

		// Perform actual upload of data
		uploadReport = iPhenotypicService.uploadAndReportPhenotypicDataFile(containerForm.getModelObject());

		// Update the report
		updateUploadReport(uploadReport.toString());

		// Save all objects to the database
		save();
	}

	public void updateUploadReport(String uploadReport) {
		// Set Upload report
		PhenoUploadReport phenoUploadReport = new PhenoUploadReport();
		phenoUploadReport.appendDetails(containerForm.getModelObject().getUpload());
		phenoUploadReport.appendAndNewLine("Collection: " + containerForm.getModelObject().getPhenoCollection().getName());
		phenoUploadReport.append(uploadReport);
		byte[] bytes = phenoUploadReport.getReport().toString().getBytes();
		Blob uploadReportBlob = util.createBlob(bytes);
		containerForm.getModelObject().getUpload().setUploadReport(uploadReportBlob);
	}

	private void save() {
		containerForm.getModelObject().getUpload().setFinishTime(new Date(System.currentTimeMillis()));
		containerForm.getModelObject().getUpload().setUploadType("FIELD_DATA");
		iPhenotypicService.createUpload(containerForm.getModelObject());
	}
}
