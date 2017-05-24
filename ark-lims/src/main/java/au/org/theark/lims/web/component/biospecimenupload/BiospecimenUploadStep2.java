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

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.MultiLineLabel;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.upload.FileUpload;
import org.apache.wicket.spring.injection.annot.SpringBean;

import au.org.theark.core.exception.FileFormatException;
import au.org.theark.core.service.IArkCommonService;
import au.org.theark.core.vo.UploadVO;
import au.org.theark.core.web.component.button.ArkDownloadAjaxButton;
import au.org.theark.core.web.component.worksheet.ArkExcelWorkSheetAsGrid;
import au.org.theark.core.web.form.AbstractWizardForm;
import au.org.theark.core.web.form.AbstractWizardStepPanel;
import au.org.theark.lims.service.IInventoryService;
import au.org.theark.lims.service.ILimsService;
import au.org.theark.lims.util.BioCollectionSpecimenUploadValidator;
import au.org.theark.lims.web.Constants;
import au.org.theark.lims.web.component.biospecimenupload.form.WizardForm;

/**
 * The first step of this wizard.
 */
public class BiospecimenUploadStep2 extends AbstractWizardStepPanel {

	private static final long				serialVersionUID		= -4070515786803720370L;
	private Form<UploadVO>					containerForm;
	private String								validationMessage;
	public java.util.Collection<String>	validationMessages	= null;

	@SuppressWarnings("unchecked")
	@SpringBean(name = au.org.theark.core.Constants.ARK_COMMON_SERVICE)
	private IArkCommonService				iArkCommonService;
	
	@SpringBean(name = au.org.theark.lims.web.Constants.LIMS_SERVICE)
	private ILimsService iLimsService;
	
	@SpringBean(name = au.org.theark.lims.web.Constants.LIMS_INVENTORY_SERVICE)
	private IInventoryService iInventoryService;
	

	private ArkDownloadAjaxButton			downloadValMsgButton	= new ArkDownloadAjaxButton("downloadValMsg", null, null, "txt") {
		private static final long	serialVersionUID	= 1L;

		@Override
		protected void onError(AjaxRequestTarget target, Form<?> form) {
			this.error("An unexpected error occurred. The download request could not be processed.");
		}
		
	};

	public BiospecimenUploadStep2(String id) {
		super(id);
		initialiseDetailForm();
	}

	public BiospecimenUploadStep2(String id, Form<UploadVO> containerForm, WizardForm wizardForm) {
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
			InputStream inputStream = containerForm.getModelObject().getFileUpload().getInputStream();
			FileUpload fileUpload = containerForm.getModelObject().getFileUpload();
			String uploadType = containerForm.getModelObject().getUploadType();
			log.info("so what is the upload type =" + uploadType);
			
			String filename = containerForm.getModelObject().getFileUpload().getClientFileName();
			String fileFormat = filename.substring(filename.lastIndexOf('.') + 1).toUpperCase();
			char delimChar = containerForm.getModelObject().getUpload().getDelimiterType().getDelimiterCharacter();

			// Only allow csv, txt or xls
			if (!(fileFormat.equalsIgnoreCase("CSV") || fileFormat.equalsIgnoreCase("TXT") || fileFormat.equalsIgnoreCase("XLS"))) {
				throw new FileFormatException();
			}
			BioCollectionSpecimenUploadValidator bioCollectionSpecimenUploadValidator = new BioCollectionSpecimenUploadValidator(containerForm.getModelObject().getUpload().getStudy(), iArkCommonService, iLimsService, iInventoryService);
			if(uploadType.equalsIgnoreCase(Constants.UPLOAD_TYPE_FOR_BIOCOLLECTION)){
				validationMessages = bioCollectionSpecimenUploadValidator.validateLimsFileFormatAndHeaderDetail(containerForm.getModelObject(),au.org.theark.core.Constants.BIOCOLLECTION_TEMPLATE_HEADER);
			}else if(uploadType.equalsIgnoreCase(Constants.UPLOAD_TYPE_FOR_BIOSPECIMEN_INVENTARY)){
				validationMessages = bioCollectionSpecimenUploadValidator.validateLimsFileFormatAndHeaderDetail(containerForm.getModelObject(),au.org.theark.core.Constants.BIOSPECIMEN_INVENTORY_TEMPLATE_HEADER);
			}
			else if(uploadType.equalsIgnoreCase(Constants.UPLOAD_TYPE_FOR_BIOSPECIMEN)){
				validationMessages = bioCollectionSpecimenUploadValidator.validateLimsFileFormatAndHeaderDetail(containerForm.getModelObject(),au.org.theark.core.Constants.BIOSPECIMEN_TEMPLATE_HEADER);
			}
			
			containerForm.getModelObject().setValidationMessages(validationMessages);
			validationMessage = containerForm.getModelObject().getValidationMessagesAsString();
			addOrReplace(new MultiLineLabel("multiLineLabel", validationMessage));

			ArkExcelWorkSheetAsGrid arkExcelWorkSheetAsGrid = new ArkExcelWorkSheetAsGrid("gridView", inputStream, fileFormat, delimChar, fileUpload, iArkCommonService.getRowsPerPage());
			arkExcelWorkSheetAsGrid.setOutputMarkupId(true);
			WebMarkupContainer wizardDataGridKeyContainer = new WebMarkupContainer("wizardDataGridKeyContainer");
			wizardDataGridKeyContainer.setVisible(false);
			wizardDataGridKeyContainer.setOutputMarkupId(true);
			form.setArkExcelWorkSheetAsGrid(arkExcelWorkSheetAsGrid);
			form.getWizardPanelFormContainer().addOrReplace(arkExcelWorkSheetAsGrid);
			form.setArkExcelWorkSheetAsGrid(arkExcelWorkSheetAsGrid);
			form.getWizardPanelFormContainer().addOrReplace(wizardDataGridKeyContainer);
			target.add(form.getWizardPanelFormContainer());

			containerForm.getModelObject().setValidationMessages(validationMessages);
			validationMessage = containerForm.getModelObject().getValidationMessagesAsString();
			addOrReplace(new MultiLineLabel("multiLineLabel", validationMessage));

			if (validationMessage != null && validationMessage.length() > 0) {

				this.containerForm.getModelObject().getUpload().setUploadStatus(iArkCommonService.getUploadStatusFor(au.org.theark.lims.web.Constants.UPLOAD_STATUS_OF_ERROR_IN_FILE_VALIDATION));
				this.containerForm.getModelObject().getUpload().setFilename(filename);//have to reset this because the container has the file name...luckily it never changes 
				iArkCommonService.updateUpload(this.containerForm.getModelObject().getUpload());

				form.getNextButton().setEnabled(false);
				target.add(form.getWizardButtonContainer());
				downloadValMsgButton = new ArkDownloadAjaxButton("downloadValMsg", "ValidationMessage", validationMessage, "txt") {

					private static final long	serialVersionUID	= 1L;

					@Override
					protected void onError(AjaxRequestTarget target, Form<?> form) {
						this.error("An unexpected error occurred. The download request could not be processed.");
					}
					
				};
				addOrReplace(downloadValMsgButton);
				target.add(downloadValMsgButton);
			}
		}
		catch (IOException e) {
			validationMessage = "Error attempting to display the file. Please check the file and try again.";
			addOrReplace(new MultiLineLabel("multiLineLabel", validationMessage));
			form.getNextButton().setEnabled(false);
			target.add(form.getWizardButtonContainer());
		}
		catch (FileFormatException ffe) {
			validationMessage = "Error uploading file. You can only upload files of type: CSV (comma separated values), TXT (text), or XLS (Microsoft Excel file)";
			addOrReplace(new MultiLineLabel("multiLineLabel", validationMessage));
			form.getNextButton().setEnabled(false);
			target.add(form.getWizardButtonContainer());
		}
	}

}
