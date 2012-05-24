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
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.MultiLineLabel;
import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.spring.injection.annot.SpringBean;

import au.org.theark.core.service.IArkCommonService;
import au.org.theark.core.vo.UploadVO;
import au.org.theark.core.web.component.button.ArkDownloadAjaxButton;
import au.org.theark.core.web.component.worksheet.ArkExcelWorkSheetAsGrid;
import au.org.theark.core.web.component.worksheet.ArkGridCell;
import au.org.theark.core.web.form.AbstractWizardForm;
import au.org.theark.core.web.form.AbstractWizardStepPanel;
import au.org.theark.study.util.CustomFieldUploadValidator;
import au.org.theark.study.util.SubjectUploadValidator;
import au.org.theark.study.web.component.subjectUpload.form.WizardForm;

public class SubjectUploadStep3 extends AbstractWizardStepPanel {

	private static final long				serialVersionUID		= 2987959815074138750L;
	private Form<UploadVO>					containerForm;
	private String								validationMessage;
	public java.util.Collection<String>	validationMessages	= null;
	private WizardForm						wizardForm;
	private WebMarkupContainer				updateExistingDataContainer;
	private CheckBox							updateChkBox;

	@SpringBean(name = au.org.theark.core.Constants.ARK_COMMON_SERVICE)
	private IArkCommonService				iArkCommonService;

	private ArkDownloadAjaxButton			downloadValMsgButton	= new ArkDownloadAjaxButton("downloadValMsg", null, null, "txt") {

																					private static final long	serialVersionUID	= 1L;

																					@Override
																					protected void onError(AjaxRequestTarget target, Form<?> form) {
																						this.error("Unexpected Error: Download request could not be processed");
																					}

																				};

	public SubjectUploadStep3(String id, Form<UploadVO> containerForm, WizardForm wizardForm) {
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

	public void setValidationMessage(String validationMessage) {
		this.validationMessage = validationMessage;
	}

	public String getValidationMessage() {
		return validationMessage;
	}

	@Override
	public void handleWizardState(AbstractWizardForm<?> form, AjaxRequestTarget target) {

	}

	@Override
	public void onStepOutNext(AbstractWizardForm<?> form, AjaxRequestTarget target) {
	}

	@Override
	public void onStepInNext(AbstractWizardForm<?> form, AjaxRequestTarget target) {
		try {
			String filename = containerForm.getModelObject().getFileUpload().getClientFileName();
			String fileFormat = filename.substring(filename.lastIndexOf('.') + 1).toUpperCase();
			char delimiterChar = containerForm.getModelObject().getUpload().getDelimiterType().getDelimiterCharacter();
			InputStream inputStream = containerForm.getModelObject().getFileUpload().getInputStream();
			HashSet<Integer> insertRows = new HashSet<Integer>();
			HashSet<Integer> updateRows = new HashSet<Integer>();
			HashSet<ArkGridCell> errorCells = new HashSet<ArkGridCell>();

			//this is not the best way to do this fix TODO
			List<String> listOfUidsToUpdate = new ArrayList<String>();				//TODO remove hardcoding
			if(containerForm.getModelObject().getUpload().getUploadType().getName().equalsIgnoreCase("Subject Demographic Data")){
				SubjectUploadValidator subjectUploadValidator = new SubjectUploadValidator(iArkCommonService);
				validationMessages = subjectUploadValidator.validateSubjectFileData(containerForm.getModelObject(), listOfUidsToUpdate);
				containerForm.getModelObject().setUidsToUpload(listOfUidsToUpdate);
				insertRows = subjectUploadValidator.getInsertRows();
				updateRows = subjectUploadValidator.getUpdateRows();
				errorCells = subjectUploadValidator.getErrorCells();
			}																												//TODO remove hardcoding
			else if(containerForm.getModelObject().getUpload().getUploadType().getName().equalsIgnoreCase("Study-specific (custom) Data")){
				CustomFieldUploadValidator customFieldUploadValidator = new CustomFieldUploadValidator(iArkCommonService);
				validationMessages = customFieldUploadValidator.validateCustomFieldFileData(containerForm.getModelObject(), listOfUidsToUpdate);
				containerForm.getModelObject().setUidsToUpload(listOfUidsToUpdate);
				//TODO consider if we want alternative way to do this - and maybe a superclass of uploadvalidator which draws out commonalities
				insertRows = customFieldUploadValidator.getInsertRows();
				updateRows = customFieldUploadValidator.getUpdateRows();
				errorCells = customFieldUploadValidator.getErrorCells();

			}
			else{
				//TODO : Throw error back to user
			}
			
			this.containerForm.getModelObject().setValidationMessages(validationMessages);
			validationMessage = containerForm.getModelObject().getValidationMessagesAsString();
			addOrReplace(new MultiLineLabel("multiLineLabel", validationMessage));


			// Show file data (and key reference)
			ArkExcelWorkSheetAsGrid arkExcelWorkSheetAsGrid = new ArkExcelWorkSheetAsGrid("gridView", inputStream, fileFormat, delimiterChar, 
																		containerForm.getModelObject().getFileUpload(), insertRows, updateRows, errorCells);
			arkExcelWorkSheetAsGrid.setOutputMarkupId(true);
			arkExcelWorkSheetAsGrid.getWizardDataGridKeyContainer().setVisible(true);
			form.setArkExcelWorkSheetAsGrid(arkExcelWorkSheetAsGrid);
			form.getWizardPanelFormContainer().addOrReplace(arkExcelWorkSheetAsGrid);

			// Repaint
			target.add(arkExcelWorkSheetAsGrid.getWizardDataGridKeyContainer());
			target.add(form.getWizardPanelFormContainer());
																															//TODO remove hardcoding
			if (updateRows.isEmpty() || containerForm.getModelObject().getUpload().getUploadType().getName().equalsIgnoreCase("Study-specific (custom) Data")) {
				updateExistingDataContainer.setVisible(false);
				target.add(updateExistingDataContainer);
			}

			if (!errorCells.isEmpty()) {
				updateExistingDataContainer.setVisible(false);
				target.add(updateExistingDataContainer);
				form.getNextButton().setEnabled(false);
				target.add(form.getWizardButtonContainer());
			}
		}
		catch (IOException e) {
			validationMessage = "Error attempting to display the file. Please check the file and try again.";
			addOrReplace(new MultiLineLabel("multiLineLabel", validationMessage));
		}

		containerForm.getModelObject().setValidationMessages(validationMessages);
		validationMessage = containerForm.getModelObject().getValidationMessagesAsString();
		addOrReplace(new MultiLineLabel("multiLineLabel", validationMessage));

		if (validationMessage != null && validationMessage.length() > 0) {
			form.getNextButton().setEnabled(false);
			target.add(form.getWizardButtonContainer());
			downloadValMsgButton = new ArkDownloadAjaxButton("downloadValMsg", "ValidationMessage", validationMessage, "txt") {

				@Override
				protected void onError(AjaxRequestTarget target, Form<?> form) {
					this.error("Unexpected Error: Download request could not be processed");
				}

			};
			addOrReplace(downloadValMsgButton);
			target.add(downloadValMsgButton);
		}
	}

	public void setUpdateChkBox(CheckBox updateChkBox) {
		this.updateChkBox = updateChkBox;
	}

	public CheckBox getUpdateChkBox() {
		return updateChkBox;
	}

	public void setUpdateExistingDataContainer(WebMarkupContainer updateExistingDataContainer) {
		this.updateExistingDataContainer = updateExistingDataContainer;
	}

	public WebMarkupContainer getUpdateExistingDataContainer() {
		return updateExistingDataContainer;
	}
}
