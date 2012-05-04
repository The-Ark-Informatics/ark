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

import java.io.IOException;
import java.io.InputStream;
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

import au.org.theark.core.service.IArkCommonService;
import au.org.theark.core.web.component.button.ArkDownloadAjaxButton;
import au.org.theark.core.web.component.worksheet.ArkExcelWorkSheetAsGrid;
import au.org.theark.core.web.component.worksheet.ArkGridCell;
import au.org.theark.core.web.form.AbstractWizardForm;
import au.org.theark.core.web.form.AbstractWizardStepPanel;
import au.org.theark.phenotypic.model.vo.UploadVO;
import au.org.theark.phenotypic.service.Constants;
import au.org.theark.phenotypic.service.IPhenotypicService;
import au.org.theark.phenotypic.util.PhenotypicValidator;
import au.org.theark.phenotypic.web.component.fieldDataUpload.form.WizardForm;

/**
 * The first step of this wizard.
 */
public class FieldDataUploadStep3 extends AbstractWizardStepPanel {

	private static final long				serialVersionUID		= 5099768179441679542L;
	static Logger								log						= LoggerFactory.getLogger(FieldDataUploadStep3.class);
	private Form<UploadVO>					containerForm;
	private String								validationMessage;
	public java.util.Collection<String>	validationMessages	= null;
	private WizardForm						wizardForm;
	@SpringBean(name = au.org.theark.core.Constants.ARK_COMMON_SERVICE)
	private IArkCommonService<Void>		iArkCommonService;
	@SpringBean(name = Constants.PHENOTYPIC_SERVICE)
	private IPhenotypicService				iPhenotypicService;
	private WebMarkupContainer				overrideDataValidationContainer;
	private CheckBox							overrideDataValidationChkBox;
	private WebMarkupContainer				updateExistingDataContainer;
	private CheckBox							updateChkBox;
	private ArkDownloadAjaxButton			downloadValMsgButton	= new ArkDownloadAjaxButton("downloadValMsg", null, null, "txt"){
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		@Override
		protected void onError(AjaxRequestTarget target, Form<?> form) {
			this.error("An error occured during donwnload. Please contact Administrator");
		}
	};

	/**
	 * Construct.
	 */
	public FieldDataUploadStep3(String id, Form<UploadVO> containerForm, WizardForm wizardForm) {
		super(id, "Step 3/5: Data Validation", "The data in the file is now validated, correct any errors and try again, otherwise, click Next to continue.<br>"
				+ "If data fails validation, but you still wish to import, you may override the validation. This data will be flagged as failed quality control<br>"
				+ "If new fields in the file are not in the current collection, they will be added accordingly");
		this.containerForm = containerForm;
		this.wizardForm = wizardForm;
		initialiseDetailForm();
	}

	@SuppressWarnings("serial")
	private void initialiseDetailForm() {
		setValidationMessage(containerForm.getModelObject().getValidationMessagesAsString());
		addOrReplace(new MultiLineLabel("multiLineLabel", getValidationMessage()));
		add(downloadValMsgButton);

		updateExistingDataContainer = new WebMarkupContainer("updateExistingDataContainer");
		updateExistingDataContainer.setOutputMarkupId(true);
		updateChkBox = new CheckBox("updateChkBox");
		updateChkBox.setVisible(true);

		overrideDataValidationContainer = new WebMarkupContainer("overrideDataValidationContainer");
		overrideDataValidationContainer.setOutputMarkupId(true);
		overrideDataValidationChkBox = new CheckBox("overrideDataValidationChkBox");
		overrideDataValidationChkBox.setOutputMarkupId(false);
		overrideDataValidationChkBox.setVisible(true);

		containerForm.getModelObject().setOverrideDataValidationChkBox(false);
		containerForm.getModelObject().setUpdateChkBox(false);

		updateChkBox.add(new AjaxFormComponentUpdatingBehavior("onChange") {

			private static final long	serialVersionUID	= -4514605801401294450L;

			@Override
			protected void onUpdate(AjaxRequestTarget target) {
				if (containerForm.getModelObject().getOverrideDataValidationChkBox() && containerForm.getModelObject().getUpdateChkBox()) {
					wizardForm.getNextButton().setEnabled(true);
				}
				else {
					wizardForm.getNextButton().setEnabled(false);
				}
				target.add(wizardForm.getWizardButtonContainer());
			}
		});

		overrideDataValidationChkBox.add(new AjaxFormComponentUpdatingBehavior("onChange") {
			@Override
			protected void onUpdate(AjaxRequestTarget target) {
				if (containerForm.getModelObject().getOverrideDataValidationChkBox() && containerForm.getModelObject().getUpdateChkBox()) {
					wizardForm.getNextButton().setEnabled(true);
				}
				else {
					wizardForm.getNextButton().setEnabled(false);
				}
				target.add(wizardForm.getWizardButtonContainer());
			}
		});

		overrideDataValidationContainer.add(overrideDataValidationChkBox);
		add(overrideDataValidationContainer);

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
		if (containerForm.getModelObject().getOverrideDataValidationChkBox())
			containerForm.getModelObject().setValidationMessages(null);
	}

	@Override
	public void onStepInNext(AbstractWizardForm<?> form, AjaxRequestTarget target) {
		validationMessage = containerForm.getModelObject().getValidationMessagesAsString();
		addOrReplace(new MultiLineLabel("multiLineLabel", validationMessage));

		if (validationMessage != null && validationMessage.length() > 0) {
			form.getNextButton().setEnabled(false);
			target.add(form.getWizardButtonContainer());
		}
		else {
			String filename = containerForm.getModelObject().getFileUpload().getClientFileName();
			String fileFormat = filename.substring(filename.lastIndexOf('.') + 1).toUpperCase();
			char delimChar = containerForm.getModelObject().getUpload().getDelimiterType().getDelimiterCharacter();
			InputStream inputStream;
			try {
				PhenotypicValidator phenotypicValidator = new PhenotypicValidator(iArkCommonService, iPhenotypicService, containerForm.getModelObject());
				inputStream = containerForm.getModelObject().getFileUpload().getInputStream();
				validationMessages = phenotypicValidator.validateMatrixPhenoFileData(inputStream, fileFormat, delimChar);

				HashSet<Integer> insertRows = new HashSet<Integer>();
				HashSet<Integer> updateRows = new HashSet<Integer>();
				HashSet<ArkGridCell> insertCells = new HashSet<ArkGridCell>();
				HashSet<ArkGridCell> updateCells = new HashSet<ArkGridCell>();
				HashSet<ArkGridCell> warningCells = new HashSet<ArkGridCell>();
				HashSet<ArkGridCell> errorCells = new HashSet<ArkGridCell>();

				insertRows = phenotypicValidator.getInsertRows();
				updateRows = phenotypicValidator.getUpdateRows();
				insertCells = phenotypicValidator.getInsertCells();
				updateCells = phenotypicValidator.getUpdateCells();
				warningCells = phenotypicValidator.getWarningCells();
				errorCells = phenotypicValidator.getErrorCells();
				inputStream.reset();

				// Show file data (and key reference)
				ArkExcelWorkSheetAsGrid arkExcelWorkSheetAsGrid = new ArkExcelWorkSheetAsGrid("gridView", inputStream, fileFormat, delimChar, containerForm.getModelObject().getFileUpload(), insertRows,
						updateRows, insertCells, updateCells, warningCells, errorCells, au.org.theark.core.Constants.ROWS_PER_PAGE);
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
					target.add(updateExistingDataContainer);
				}

				if (warningCells.isEmpty()) {
					containerForm.getModelObject().setOverrideDataValidationChkBox(true);
					overrideDataValidationContainer.setVisible(false);
					target.add(overrideDataValidationContainer);
				}

				if (!errorCells.isEmpty()) {
					overrideDataValidationContainer.setVisible(false);
					target.add(overrideDataValidationContainer);
					updateExistingDataContainer.setVisible(false);
					target.add(updateExistingDataContainer);
					form.getNextButton().setEnabled(false);
					target.add(form.getWizardButtonContainer());
				}
			}
			catch (IOException e1) {
				validationMessage = "Error attempting to display the file. Please check the file and try again.";
				addOrReplace(new MultiLineLabel("multiLineLabel", validationMessage));
			}

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
						this.error("An error occured during donwnload. Please contact Administrator");
					}
				};
				addOrReplace(downloadValMsgButton);
				target.add(downloadValMsgButton);
			}
		}
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

	/**
	 * @param overrideDataValidationContainer
	 *           the overrideDataValidationContainer to set
	 */
	public void setOverrideDataValidationContainer(WebMarkupContainer overrideDataValidationContainer) {
		this.overrideDataValidationContainer = overrideDataValidationContainer;
	}

	/**
	 * @return the overrideDataValidationContainer
	 */
	public WebMarkupContainer getOverrideDataValidationContainer() {
		return overrideDataValidationContainer;
	}
}
