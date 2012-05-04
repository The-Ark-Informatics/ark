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



import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import org.apache.shiro.SecurityUtils;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.basic.MultiLineLabel;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.upload.FileUpload;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import au.org.theark.core.exception.FileFormatException;
import au.org.theark.core.model.pheno.entity.PhenoCollection;
import au.org.theark.core.model.study.entity.Study;
import au.org.theark.core.service.IArkCommonService;
import au.org.theark.core.web.component.button.ArkDownloadAjaxButton;
import au.org.theark.core.web.component.worksheet.ArkExcelWorkSheetAsGrid;
import au.org.theark.core.web.form.AbstractWizardForm;
import au.org.theark.core.web.form.AbstractWizardStepPanel;
import au.org.theark.phenotypic.job.PhenoDataUploadExecutor;
import au.org.theark.phenotypic.model.vo.UploadVO;
import au.org.theark.phenotypic.service.Constants;
import au.org.theark.phenotypic.service.IPhenotypicService;
import au.org.theark.phenotypic.util.PhenotypicValidator;
import au.org.theark.phenotypic.web.component.fieldDataUpload.form.WizardForm;

/**
 * The first step of this wizard.
 */
public class FieldDataUploadStep2 extends AbstractWizardStepPanel {


	private static final long				serialVersionUID		= -6923277221441497110L;
	static Logger								log						= LoggerFactory.getLogger(FieldDataUploadStep2.class);
	private Form<UploadVO>					containerForm;
	private String								validationMessage;
	public java.util.Collection<String>	validationMessages	= null;

	@SpringBean(name = au.org.theark.core.Constants.ARK_COMMON_SERVICE)
	private IArkCommonService<Void>		iArkCommonService;

	@SpringBean(name = Constants.PHENOTYPIC_SERVICE)
	private IPhenotypicService				iPhenotypicService;

//	@SpringBean(name = au.org.theark.core.Constants.ARK_CSV_LOADER_SERVICE)
//	private ICSVLoaderService				iCSVLoaderService;

	private ArkDownloadAjaxButton			downloadValMsgButton	= new ArkDownloadAjaxButton("downloadValMsg", null, null, "txt"){

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		@Override
		protected void onError(AjaxRequestTarget target, Form<?> form) {
			this.error("An error occured during download. Contact Administrator");
		}
		
	};

	public FieldDataUploadStep2(String id) {
		super(id);
		initialiseDetailForm();
	}

	public FieldDataUploadStep2(String id, Form<UploadVO> containerForm, WizardForm wizardForm) {
		super(id, "Step 2/5: File Format Validation", "The file format has been validated. If there are no errors, click Next to continue.");

		this.containerForm = containerForm;
		initialiseDetailForm();
	}

	private void initialiseDetailForm() {
		setValidationMessage(containerForm.getModelObject().getValidationMessagesAsString());
		addOrReplace(new MultiLineLabel("multiLineLabel", getValidationMessage()));
		add(downloadValMsgButton);
	}

	/**
	 * @param validationMessage
	 *           the validationMessages to set
	 */
	public void setValidationMessage(String validationMessage) {
		this.validationMessage = validationMessage;
	}

	/**
	 * @return the validationMessage
	 */
	public String getValidationMessage() {
		return validationMessage;
	}

	@Override
	public void handleWizardState(AbstractWizardForm<?> form, AjaxRequestTarget target) {

	}

	@Override
	public void onStepInNext(AbstractWizardForm<?> form, AjaxRequestTarget target) {
		try {
			String filename = containerForm.getModelObject().getFileUpload().getClientFileName();
			String fileFormat = filename.substring(filename.lastIndexOf('.') + 1).toUpperCase();
			char delimChar = containerForm.getModelObject().getUpload().getDelimiterType().getDelimiterCharacter();
			InputStream inputStream;

			// Only allow csv, txt or xls
			if (!(fileFormat.equalsIgnoreCase("CSV") || fileFormat.equalsIgnoreCase("TXT") || fileFormat.equalsIgnoreCase("XLS"))) {
				throw new FileFormatException();
			}

			PhenotypicValidator phenotypicValidator = new PhenotypicValidator(iArkCommonService, iPhenotypicService, containerForm.getModelObject());
			inputStream = containerForm.getModelObject().getFileUpload().getInputStream();
			validationMessages = phenotypicValidator.validateMatrixPhenoFileFormat(inputStream, fileFormat, delimChar);

			containerForm.getModelObject().setValidationMessages(validationMessages);
			validationMessage = containerForm.getModelObject().getValidationMessagesAsString();
			addOrReplace(new MultiLineLabel("multiLineLabel", validationMessage));

			if (validationMessage != null && validationMessage.length() > 0) {
				form.getNextButton().setEnabled(false);
				target.add(form.getWizardButtonContainer());
				downloadValMsgButton = new ArkDownloadAjaxButton("downloadValMsg", "ValidationMessage", validationMessage, "txt"){

					private static final long serialVersionUID = 1L;

					@Override
					protected void onError(AjaxRequestTarget target,Form<?> form) {
						this.error("There was an error while Downloading. Please contact Administrator");
					}
					
				};
				addOrReplace(downloadValMsgButton);
				target.add(downloadValMsgButton);
			}

			// Show file data
			FileUpload fileUpload = containerForm.getModelObject().getFileUpload();

			String currentUser = SecurityUtils.getSubject().getPrincipal().toString();
			Long sessionStudyId = (Long) SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.core.Constants.STUDY_CONTEXT_ID);
			Study study = iArkCommonService.getStudy(sessionStudyId);
			PhenoCollection phenoCollection = containerForm.getModelObject().getPhenoCollection();
			File file = fileUpload.writeToTempFile();

			// Save the Upload, for acces via background job
			containerForm.getModelObject().getUpload().setUploadType("FIELD_DATA");
			iPhenotypicService.createUpload(containerForm.getModelObject());
			
			Long uploadId = containerForm.getModelObject().getUpload().getId();
			PhenoDataUploadExecutor phenoDataUploadExecutor = new PhenoDataUploadExecutor(iArkCommonService, iPhenotypicService, uploadId, currentUser, study, phenoCollection, file, delimChar);
			try {
				phenoDataUploadExecutor.run();
			}
			catch (Exception e) {
				log.error(e.getMessage());
			}

			/*
			StopWatch timer = new StopWatch();
			// test temp file/table creation
			LoadCsvFileHelper loadCsvFileHelper = new LoadCsvFileHelper(iCSVLoaderService, "study", delimChar);
			DecimalFormat decimalFormat = new DecimalFormat("0.00");
			timer.start();
			log.info("Started loadCsvFileHelper ");
			loadCsvFileHelper.setTemporaryTableName("tmp_table");
			loadCsvFileHelper.setTemporaryFileName(Session.get().getId());
			loadCsvFileHelper.convertToCSVAndWriteToFile(fileUpload, delimChar);
			loadCsvFileHelper.createTemporaryTable();
			timer.stop();
			log.info("Finished loadCsvFileHelper ");
			log.info("Total elapsed time: " + timer.getTime() + " ms or " + decimalFormat.format(timer.getTime() / 1000.0) + " s");
			*/

			inputStream = fileUpload.getInputStream();
			ArkExcelWorkSheetAsGrid arkExcelWorkSheetAsGrid = new ArkExcelWorkSheetAsGrid("gridView", inputStream, fileFormat, delimChar, fileUpload, au.org.theark.core.Constants.ROWS_PER_PAGE);
			arkExcelWorkSheetAsGrid.setOutputMarkupId(true);
			form.setArkExcelWorkSheetAsGrid(arkExcelWorkSheetAsGrid);
			form.getWizardPanelFormContainer().addOrReplace(arkExcelWorkSheetAsGrid);
			target.add(form.getWizardPanelFormContainer());
		}
		catch (NullPointerException npe) {
			log.error("NullPointer " + npe.getMessage());
			validationMessage = "Error attempting to display the file. Please check the file and try again.";
			addOrReplace(new MultiLineLabel("multiLineLabel", validationMessage));
			form.getNextButton().setEnabled(false);
			target.add(form.getWizardButtonContainer());
		}
		catch (IOException ioe) {
			log.error("IOException " + ioe.getMessage());
			validationMessage = "Error attempting to display the file. Please check the file and try again.";
			addOrReplace(new MultiLineLabel("multiLineLabel", validationMessage));
			form.getNextButton().setEnabled(false);
			target.add(form.getWizardButtonContainer());
		}
		catch (FileFormatException ffe) {
			log.error("FileFormatException " + ffe.getMessage());
			validationMessage = "Error uploading file. You can only upload files of type: CSV (comma separated values), TXT (text), or XLS (Microsoft Excel file)";
			addOrReplace(new MultiLineLabel("multiLineLabel", validationMessage));
			form.getNextButton().setEnabled(false);
			target.add(form.getWizardButtonContainer());
		}
	}

	@Override
	public void onStepOutNext(AbstractWizardForm<?> form, AjaxRequestTarget target) {
	}
}
