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
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashSet;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.MultiLineLabel;
import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import au.org.theark.core.exception.ArkBaseException;
import au.org.theark.core.service.IArkCommonService;
import au.org.theark.core.util.CustomFieldCategoryImportValidator;
import au.org.theark.core.util.CustomFieldImportValidator;
import au.org.theark.core.util.ICustomImportValidator;
import au.org.theark.core.vo.CustomFieldUploadVO;
import au.org.theark.core.web.component.button.ArkDownloadAjaxButton;
import au.org.theark.core.web.component.customfieldupload.form.WizardForm;
import au.org.theark.core.web.component.worksheet.ArkExcelWorkSheetAsGrid;
import au.org.theark.core.web.component.worksheet.ArkGridCell;
import au.org.theark.core.web.form.AbstractWizardForm;
import au.org.theark.core.web.form.AbstractWizardStepPanel;

/**
 * The first step of this wizard.
 */
public class CustomFieldUploadStep3 extends AbstractWizardStepPanel {

	private static final long				serialVersionUID		= 5099768179441679542L;
	static Logger								log						= LoggerFactory.getLogger(CustomFieldUploadStep3.class);

	@SpringBean(name = au.org.theark.core.Constants.ARK_COMMON_SERVICE)
	private IArkCommonService				iArkCommonService;

	private Form<CustomFieldUploadVO>	containerForm;
	private String						validationMessage;
	public java.util.Collection<String>	validationMessages	= new ArrayList<String>();
	private WizardForm						wizardForm;
	private WebMarkupContainer				updateExistingDataContainer;
	private CheckBox						updateChkBox;
	private ICustomImportValidator 			iCustomFieldValidator;
	private ArkDownloadAjaxButton			downloadValMsgButton	= new ArkDownloadAjaxButton("downloadValMsg", null, null, "txt") {
		private static final long	serialVersionUID	= 1L;
		@Override
		protected void onError(AjaxRequestTarget target, Form<?> form) {
			this.error("Unexpected Error: Download request could not be processed");
		}
	};

	/**
	 * Construct.
	 */
	public CustomFieldUploadStep3(String id, Form<CustomFieldUploadVO> containerForm, WizardForm wizardForm) {
		super(id, "Step 3/5: Data Validation", "The data in the file is now validated, correct any errors and try again, otherwise, click Next to continue.");
		this.containerForm = containerForm;
		this.wizardForm = wizardForm;
		initialiseDetailForm();
	}

	private void initialiseDetailForm() {
		setValidationMessage(containerForm.getModelObject().getValidationMessagesAsString());
		addOrReplace(new MultiLineLabel("multiLineLabel", getValidationMessage()));

		add(downloadValMsgButton);

		updateExistingDataContainer = new WebMarkupContainer("updateExistingDataContainer");
		updateExistingDataContainer.setOutputMarkupId(true);
		updateChkBox = new CheckBox("updateChkBox");
		updateChkBox.setVisible(true);

		containerForm.getModelObject().setUpdateChkBox(false);

		updateChkBox.add(new AjaxFormComponentUpdatingBehavior("onChange") {

			private static final long	serialVersionUID	= -4514605801401294450L;

			@Override
			protected void onUpdate(AjaxRequestTarget target) {
				if (containerForm.getModelObject().getUpdateChkBox()) {
					wizardForm.getNextButton().setEnabled(true);
				}
				else {
					wizardForm.getNextButton().setEnabled(false);
				}
				target.add(wizardForm.getWizardButtonContainer());
			}
		});

		updateExistingDataContainer.add(updateChkBox);
		add(updateExistingDataContainer);
	}

	/**
	 * @param validationMessages
	 *           the validationMessages to set
	 */
	public void setValidationMessage(String validationMessage) {
		this.validationMessage = validationMessage;
	}

	/**
	 * @return the validationMessages
	 */
	public String getValidationMessage() {
		return validationMessage;
	}

	@Override
	public void handleWizardState(AbstractWizardForm<?> form, AjaxRequestTarget target) {

	}

	@Override
	public void onStepOutNext(AbstractWizardForm<?> form, AjaxRequestTarget target) {
		containerForm.getModelObject().setValidationMessages(null);
	}

	@Override
	public void onStepInNext(AbstractWizardForm<?> form, AjaxRequestTarget target) {
		String filename = containerForm.getModelObject().getFileUpload().getClientFileName();
		String fileFormat = filename.substring(filename.lastIndexOf('.') + 1).toUpperCase();
		char delimChar = containerForm.getModelObject().getUpload().getDelimiterType().getDelimiterCharacter();
		File temp = containerForm.getModelObject().getTempFile();
		if (temp != null && temp.exists()) {
			InputStream inputStream = null;
			try {
				// Field upload
				if(containerForm.getModelObject().getUpload().getUploadLevel().getName().equalsIgnoreCase(Constants.UPLOAD_LEVEL_FIELD)){
					iCustomFieldValidator= new CustomFieldImportValidator(iArkCommonService, containerForm.getModelObject());
				//Category upload	
				}
				if(containerForm.getModelObject().getUpload().getUploadLevel().getName().equalsIgnoreCase(Constants.UPLOAD_LEVEL_CATEGORY)){
					iCustomFieldValidator= new CustomFieldCategoryImportValidator(iArkCommonService, containerForm.getModelObject());
				}
				inputStream = new BufferedInputStream(new FileInputStream(temp));
				validationMessages = iCustomFieldValidator.validateDataDictionaryFileData(inputStream, fileFormat, delimChar);
				inputStream.close();
				inputStream = null;
				inputStream = setupExcellDisplayFormat(form, target,fileFormat, delimChar, temp);

			}
			catch (IOException ioe) {
				log.error("IOException " + ioe.getMessage());
				validationMessage = "Error attempting to display the file. Please check the file and try again.";
				validationMessages.add(validationMessage);
			}
			catch(ArkBaseException ark){
				log.error("ARK_BASE_EXCEPTION: " + ark.getMessage());
				validationMessage = ark.getMessage();
				validationMessages.add(validationMessage);
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
			}
			displayValidationMessagesAndButtons(form, target);
		}else {
			// Stop progress because of missing temp file
			error("Unexpected error: Can not proceed due to missing temporary file.");
			form.getNextButton().setEnabled(false);
		}
	}
	/**
	 * 
	 * @param form
	 * @param target
	 */
	private void displayValidationMessagesAndButtons(AbstractWizardForm<?> form, AjaxRequestTarget target) {
		containerForm.getModelObject().setValidationMessages(validationMessages);
		validationMessage = containerForm.getModelObject().getValidationMessagesAsString();
		addOrReplace(new MultiLineLabel("multiLineLabel", validationMessage));

		if (validationMessage != null && validationMessage.length() > 0) {
			form.getNextButton().setEnabled(false);
			target.add(form.getWizardButtonContainer());
			downloadValMsgButton = new ArkDownloadAjaxButton("downloadValMsg", "ValidationMessage", validationMessage, "txt") {

				private static final long	serialVersionUID	= 1L;

				@Override
				protected void onError(AjaxRequestTarget target, Form<?> form) {
					this.error("Unexpected Error: Download request could not be processed");
				}
			};
			addOrReplace(downloadValMsgButton);
			target.add(downloadValMsgButton);
		}
	}
	/**
	 * 
	 * @param form
	 * @param target
	 * @param fileFormat
	 * @param delimChar
	 * @param temp
	 * @return
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	private InputStream setupExcellDisplayFormat(AbstractWizardForm<?> form,AjaxRequestTarget target, String fileFormat, char delimChar,File temp) throws FileNotFoundException, IOException {
		InputStream inputStream;
		HashSet<Integer> insertRows = new HashSet<Integer>();
		HashSet<Integer> updateRows = new HashSet<Integer>();
		HashSet<ArkGridCell> insertCells = new HashSet<ArkGridCell>();
		HashSet<ArkGridCell> updateCells = new HashSet<ArkGridCell>();
		HashSet<ArkGridCell> warningCells = new HashSet<ArkGridCell>();
		HashSet<ArkGridCell> errorCells = new HashSet<ArkGridCell>();

		insertRows = iCustomFieldValidator.getInsertRows();
		updateRows = iCustomFieldValidator.getUpdateRows();
		insertCells = iCustomFieldValidator.getInsertCells();
		updateCells = iCustomFieldValidator.getUpdateCells();
		warningCells = iCustomFieldValidator.getWarningCells();
		errorCells = iCustomFieldValidator.getErrorCells();

		// Show file data (and key reference)
		inputStream = new BufferedInputStream(new FileInputStream(temp));
		ArkExcelWorkSheetAsGrid arkExcelWorkSheetAsGrid = new ArkExcelWorkSheetAsGrid("gridView", inputStream, fileFormat, delimChar, containerForm.getModelObject().getFileUpload(), insertRows,
				updateRows, insertCells, updateCells, warningCells, errorCells);
		inputStream.close();
		inputStream = null;
		arkExcelWorkSheetAsGrid.setOutputMarkupId(true);
		arkExcelWorkSheetAsGrid.getWizardDataGridKeyContainer().setVisible(true);
		form.setArkExcelWorkSheetAsGrid(arkExcelWorkSheetAsGrid);
		form.getWizardPanelFormContainer().addOrReplace(arkExcelWorkSheetAsGrid);

		// Repaint
		target.add(arkExcelWorkSheetAsGrid.getWizardDataGridKeyContainer());
		target.add(form.getWizardPanelFormContainer());
		if (updateCells.isEmpty()) {
			containerForm.getModelObject().setUpdateChkBox(true);
			updateExistingDataContainer.setVisible(false);
		}else {
			containerForm.getModelObject().setUpdateChkBox(false);
			updateExistingDataContainer.setVisible(true);
		}
		target.add(updateExistingDataContainer);
		if (!errorCells.isEmpty()) {
			updateExistingDataContainer.setVisible(false);
			target.add(updateExistingDataContainer);
			form.getNextButton().setEnabled(false);
			target.add(form.getWizardButtonContainer());
		}
		return inputStream;
	}
	/**
	 * @param updateChkBox
	 *           the updateChkBox to set
	 */
	public void setUpdateChkBox(CheckBox updateChkBox) {
		this.updateChkBox = updateChkBox;
	}

	/**
	 * @return the updateChkBox
	 */
	public CheckBox getUpdateChkBox() {
		return updateChkBox;
	}

	public WebMarkupContainer getUpdateExistingDataContainer() {
		return updateExistingDataContainer;
	}

	public void setUpdateExistingDataContainer(WebMarkupContainer updateExistingDataContainer) {
		this.updateExistingDataContainer = updateExistingDataContainer;
	}
}
