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
import java.util.Iterator;
import java.util.List;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.MultiLineLabel;
import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import au.org.theark.core.service.IArkCommonService;
import au.org.theark.core.vo.UploadVO;
import au.org.theark.core.web.component.button.ArkDownloadAjaxButton;
import au.org.theark.core.web.component.worksheet.ArkExcelWorkSheetAsGrid;
import au.org.theark.core.web.component.worksheet.ArkGridCell;
import au.org.theark.core.web.form.AbstractWizardForm;
import au.org.theark.core.web.form.AbstractWizardStepPanel;
import au.org.theark.study.service.IStudyService;
import au.org.theark.study.util.CustomFieldUploadValidator;
import au.org.theark.study.util.PedigreeUploadValidator;
import au.org.theark.study.util.SubjectAttachmentValidator;
import au.org.theark.study.util.SubjectConsentUploadValidator;
import au.org.theark.study.util.SubjectUploadValidator;
import au.org.theark.study.web.Constants;
import au.org.theark.study.web.component.subjectUpload.form.WizardForm;

public class SubjectUploadStep3 extends AbstractWizardStepPanel {

	private static final long				serialVersionUID		= 2987959815074138750L;
	private Form<UploadVO>					containerForm;
	private String								validationMessage;
	public java.util.Collection<String>	validationMessages	= null;
	public java.util.Collection<String>	warningMessages	= null;
	private WizardForm						wizardForm;
	private WebMarkupContainer				updateExistingDataContainer;
	private CheckBox							updateChkBox;
	private WebMarkupContainer				continueDespiteBadDataContainer;
	protected Boolean						continueDespiteBadData = false;
	private CheckBox							continueChkBox;

	@SpringBean(name = au.org.theark.core.Constants.ARK_COMMON_SERVICE)
	private IArkCommonService				iArkCommonService;
	
	@SpringBean(name = Constants.STUDY_SERVICE)
	private IStudyService				iStudyService;
	

	private ArkDownloadAjaxButton			downloadValMsgButton	= new ArkDownloadAjaxButton("downloadValMsg", null, null, "txt") {

																					private static final long	serialVersionUID	= 1L;

																					@Override
																					protected void onError(AjaxRequestTarget target, Form<?> form) {
																						this.error("An unexpected error occurred. The download request could not be processed");
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
		
		continueDespiteBadDataContainer = new WebMarkupContainer("continueDespiteBadDataContainer");
		continueDespiteBadDataContainer.setOutputMarkupId(true);
		continueChkBox = new CheckBox("continueChkBox", new PropertyModel<Boolean>(this, "continueDespiteBadData"));
		continueChkBox.setVisible(true);

		continueChkBox.add(new AjaxFormComponentUpdatingBehavior("onChange") {

			private static final long	serialVersionUID	= -4514605801401294450L;

			@Override
			protected void onUpdate(AjaxRequestTarget target) {
				wizardForm.getNextButton().setEnabled(continueChkBox.getModelObject());
				target.add(wizardForm.getWizardButtonContainer());
			}
		});

		continueDespiteBadDataContainer.add(continueChkBox);
		add(continueDespiteBadDataContainer);
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
			List<String> listOfUidsToUpdate = new ArrayList<String>();
			if(containerForm.getModelObject().getUpload().getUploadType().getName().equalsIgnoreCase(Constants.SUBJECT_DEMOGRAPHIC_DATA)){
				SubjectUploadValidator subjectUploadValidator = new SubjectUploadValidator(iArkCommonService);
				validationMessages = subjectUploadValidator.validateSubjectFileData(containerForm.getModelObject(), listOfUidsToUpdate);
				containerForm.getModelObject().setUidsToUpload(listOfUidsToUpdate);
				insertRows = subjectUploadValidator.getInsertRows();
				updateRows = subjectUploadValidator.getUpdateRows();
				errorCells = subjectUploadValidator.getErrorCells();
			}
			// custom field data two types of validation subject type or family type.
			else if(containerForm.getModelObject().getUpload().getUploadType().getName().equalsIgnoreCase(Constants.STUDY_SPECIFIC_CUSTOM_DATA)){
				CustomFieldUploadValidator customFieldUploadValidator = new CustomFieldUploadValidator(iArkCommonService);
				validationMessages = customFieldUploadValidator.validateCustomFieldFileData(containerForm.getModelObject(), listOfUidsToUpdate);
				containerForm.getModelObject().setUidsToUpload(listOfUidsToUpdate);
				//TODO consider if we want alternative way to do this - and maybe a superclass of uploadvalidator which draws out commonalities
				insertRows = customFieldUploadValidator.getInsertRows();
				updateRows = customFieldUploadValidator.getUpdateRows();
				errorCells = customFieldUploadValidator.getErrorCells();

			}
			else if(containerForm.getModelObject().getUpload().getUploadType().getName().equalsIgnoreCase(Constants.SUBJECT_CONSENT_DATA)){
				SubjectConsentUploadValidator subjectConsentUploadValidator=new SubjectConsentUploadValidator(iArkCommonService);
				validationMessages = subjectConsentUploadValidator.validateSubjectConsentFileData(containerForm.getModelObject(), listOfUidsToUpdate);
				containerForm.getModelObject().setUidsToUpload(listOfUidsToUpdate);
				insertRows = subjectConsentUploadValidator.getInsertRows();
				updateRows = subjectConsentUploadValidator.getUpdateRows();
				errorCells = subjectConsentUploadValidator.getErrorCells();
			}
			else if(containerForm.getModelObject().getUpload().getUploadType().getName().equalsIgnoreCase(Constants.PEDIGREE_DATA)){
				PedigreeUploadValidator pedigreeUploadValidator=new PedigreeUploadValidator(iArkCommonService,iStudyService);
				validationMessages = pedigreeUploadValidator.validatePedigreeFileData(containerForm.getModelObject(), listOfUidsToUpdate);
				warningMessages =  pedigreeUploadValidator.getDataWarningMessages();
				containerForm.getModelObject().setUidsToUpload(listOfUidsToUpdate);
				insertRows = pedigreeUploadValidator.getInsertRows();
				updateRows = pedigreeUploadValidator.getUpdateRows();
				errorCells = pedigreeUploadValidator.getErrorCells();
			}
			else if(containerForm.getModelObject().getUpload().getUploadType().getName().equalsIgnoreCase(Constants.SUBJECT_ATTACHMENT_DATA)){
				SubjectAttachmentValidator subjectAttachmentValidator = new SubjectAttachmentValidator(iArkCommonService);
				validationMessages = subjectAttachmentValidator.validateSubjectAttachmentFileData(containerForm.getModelObject(),listOfUidsToUpdate);
				containerForm.getModelObject().setUidsToUpload(listOfUidsToUpdate);
				insertRows = subjectAttachmentValidator.getInsertRows();
				updateRows = subjectAttachmentValidator.getUpdateRows();
				errorCells = subjectAttachmentValidator.getErrorCells();
			}
			else{
				//TODO : Throw error back to user
			}
			
			this.containerForm.getModelObject().setValidationMessages(validationMessages);
			validationMessage = containerForm.getModelObject().getValidationMessagesAsString();
			addOrReplace(new MultiLineLabel("multiLineLabel", validationMessage));


			// Show file data (and key reference)
//			ArkExcelWorkSheetAsGrid arkExcelWorkSheetAsGrid = new ArkExcelWorkSheetAsGrid("gridView", inputStream, fileFormat, delimiterChar, 
//																		containerForm.getModelObject().getFileUpload(), insertRows, updateRows, errorCells, containerForm.getModelObject().getUpload().getUploadType());
		
			
			//Sanjaya comment on 2016-07-18 : 
			//I suspect due to this changes in the display the per page rows. There is a negligence on the coding of  insertRows, updateRows, errorCells.
			ArkExcelWorkSheetAsGrid arkExcelWorkSheetAsGrid=null; 
			/*if(Constants.PEDIGREE_DATA.equalsIgnoreCase(containerForm.getModelObject().getUpload().getUploadType().getName())){
				arkExcelWorkSheetAsGrid= new ArkExcelWorkSheetAsGrid("gridView", inputStream, fileFormat, delimiterChar, 
						containerForm.getModelObject().getFileUpload(), iArkCommonService.getRowsPerPage(), containerForm.getModelObject().getUpload().getUploadType(),false);
			}else{
				arkExcelWorkSheetAsGrid = new ArkExcelWorkSheetAsGrid("gridView", inputStream, fileFormat, delimiterChar, 
						containerForm.getModelObject().getFileUpload(), iArkCommonService.getRowsPerPage(), containerForm.getModelObject().getUpload().getUploadType());
			}*/
			if(Constants.PEDIGREE_DATA.equalsIgnoreCase(containerForm.getModelObject().getUpload().getUploadType().getName())){
				arkExcelWorkSheetAsGrid= new ArkExcelWorkSheetAsGrid("gridView", inputStream, fileFormat, delimiterChar, 
					containerForm.getModelObject().getFileUpload(), insertRows, updateRows, errorCells, iArkCommonService.getRowsPerPage(), containerForm.getModelObject().getUpload().getUploadType(), false);
			}else{
				arkExcelWorkSheetAsGrid = new ArkExcelWorkSheetAsGrid("gridView", inputStream, fileFormat, delimiterChar, 
					containerForm.getModelObject().getFileUpload(),insertRows, updateRows, errorCells, iArkCommonService.getRowsPerPage(), containerForm.getModelObject().getUpload().getUploadType());
			}
			arkExcelWorkSheetAsGrid.setOutputMarkupId(true);
			arkExcelWorkSheetAsGrid.getWizardDataGridKeyContainer().setVisible(true);
			form.setArkExcelWorkSheetAsGrid(arkExcelWorkSheetAsGrid);
			form.getWizardPanelFormContainer().addOrReplace(arkExcelWorkSheetAsGrid);
			// Repaint
			target.add(arkExcelWorkSheetAsGrid.getWizardDataGridKeyContainer());
			target.add(form.getWizardPanelFormContainer());

			if (updateRows.isEmpty() || containerForm.getModelObject().getUpload().getUploadType().getName().equalsIgnoreCase(Constants.STUDY_SPECIFIC_CUSTOM_DATA)) {
				updateExistingDataContainer.setVisible(false);
				target.add(updateExistingDataContainer);
				
			}else{
				updateExistingDataContainer.setVisible(true);
				target.add(updateExistingDataContainer);
			}

			if (!errorCells.isEmpty()) {
				updateExistingDataContainer.setVisible(false);
				target.add(updateExistingDataContainer);
				form.getNextButton().setEnabled(false);
				target.add(form.getWizardButtonContainer());
				
				//TODO: consider invalid custom data to be warning cells rather than error cells
				continueDespiteBadDataContainer.setVisible(containerForm.getModelObject().getUpload().getUploadType().getName().equalsIgnoreCase(Constants.STUDY_SPECIFIC_CUSTOM_DATA));
				target.add(continueDespiteBadDataContainer);

				this.containerForm.getModelObject().getUpload().setUploadStatus(iArkCommonService.getUploadStatusFor(au.org.theark.study.web.Constants.UPLOAD_STATUS_OF_ERROR_IN_DATA_VALIDATION));
				this.containerForm.getModelObject().getUpload().setFilename(filename);//have to reset this because the container has the file name...luckily it never changes 
				iArkCommonService.updateUpload(this.containerForm.getModelObject().getUpload());
			}
			else{
				continueDespiteBadDataContainer.setVisible(false);
				target.add(continueDespiteBadDataContainer);
				
				this.containerForm.getModelObject().getUpload().setUploadStatus(iArkCommonService.getUploadStatusFor(au.org.theark.study.web.Constants.UPLOAD_STATUS_OF_VALIDATED));
				this.containerForm.getModelObject().getUpload().setFilename(filename);//have to reset this because the container has the file name...luckily it never changes 
				iArkCommonService.updateUpload(this.containerForm.getModelObject().getUpload());
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
			form.getNextButton().setEnabled(continueChkBox.getModelObject());
			target.add(form.getWizardButtonContainer());
			downloadValMsgButton = new ArkDownloadAjaxButton("downloadValMsg", "ValidationMessage", validationMessage, "txt") {

				/**
				 * 
				 */
				private static final long	serialVersionUID	= 343293022422099247L;

				@Override
				protected void onError(AjaxRequestTarget target, Form<?> form) {
					this.error("An unexpected error occurred. The download request could not be processed.");
				}

			};
			addOrReplace(downloadValMsgButton);
			target.add(downloadValMsgButton);
		}
		
		String warningMessage=getWarningMessagesAsString();
		
		if(warningMessage !=null && warningMessage.length()>0){
			addOrReplace(new MultiLineLabel("multiLineLabel", validationMessage+"\n "+warningMessage));
			
			downloadValMsgButton = new ArkDownloadAjaxButton("downloadValMsg", "ValidationMessage", validationMessage+"\n"+warningMessage, "txt") {

				/**
				 * 
				 */
				private static final long	serialVersionUID	= 343293022422099247L;

				@Override
				protected void onError(AjaxRequestTarget target, Form<?> form) {
					this.error("An unexpected error occurred. The download request could not be processed.");
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
	
	public String getWarningMessagesAsString() {
		StringBuffer stringBuffer = new StringBuffer("");
		java.util.Collection<String> msgs = this.warningMessages;

		if (msgs != null) {
			for (Iterator<String> iterator = msgs.iterator(); iterator.hasNext();) {
				String string = (String) iterator.next();
				stringBuffer.append(string);
				stringBuffer.append("\n");
			}
		}
		return stringBuffer.toString();
	}
}
