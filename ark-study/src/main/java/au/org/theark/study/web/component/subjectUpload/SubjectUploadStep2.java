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
import au.org.theark.study.util.CustomFieldUploadValidator;
import au.org.theark.study.util.PedigreeUploadValidator;
import au.org.theark.study.util.SubjectAttachmentValidator;
import au.org.theark.study.util.SubjectConsentUploadValidator;
import au.org.theark.study.util.SubjectUploadValidator;
import au.org.theark.study.web.Constants;
import au.org.theark.study.web.component.subjectUpload.form.WizardForm;

/**
 * The first step of this wizard.
 */
public class SubjectUploadStep2 extends AbstractWizardStepPanel {

	private static final long				serialVersionUID		= -4070515786803720370L;
	private Form<UploadVO>					containerForm;
	private String								validationMessage;
	public java.util.Collection<String>	validationMessages	= null;

	@SpringBean(name = au.org.theark.core.Constants.ARK_COMMON_SERVICE)
	private IArkCommonService				iArkCommonService;

	private ArkDownloadAjaxButton		downloadValMsgButton	= new ArkDownloadAjaxButton("downloadValMsg", null, null, "txt") {
													private static final long	serialVersionUID	= 1L;
													@Override
													protected void onError(AjaxRequestTarget target, Form<?> form) {
														this.error("An unexpected error occurred. The download request could not be processed.");
													}
												};

	public SubjectUploadStep2(String id) {
		super(id);
		initialiseDetailForm();
	}

	public SubjectUploadStep2(String id, Form<UploadVO> containerForm, WizardForm wizardForm) {
		super(id, "Step 2/5: File Format Validation", "The file format has been validated. If there are no errors, click Next to continue.");
		this.containerForm = containerForm;
		initialiseDetailForm();
	}

	private void initialiseDetailForm() {
		setValidationMessage(containerForm.getModelObject().getValidationMessagesAsString());
		addOrReplace(new MultiLineLabel("multiLineLabel", getValidationMessage()));
		add(downloadValMsgButton);
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
	public void onStepInNext(AbstractWizardForm<?> form, AjaxRequestTarget target) {
		try {
			FileUpload fileUpload = containerForm.getModelObject().getFileUpload();
			InputStream inputStream = fileUpload.getInputStream();//TODO : should something this big be thrown around model object in wicket?
			String filename = fileUpload.getClientFileName();
			String fileFormat = filename.substring(filename.lastIndexOf('.') + 1).toUpperCase();
			char delimChar = containerForm.getModelObject().getUpload().getDelimiterType().getDelimiterCharacter();

			// Only allow csv, txt, xls Or ped  TODO : if we are hardcoding things like this, why do we fetch file formats from db and store as a fk reference?
			if (!(fileFormat.equalsIgnoreCase("CSV") || fileFormat.equalsIgnoreCase("TXT") || fileFormat.equalsIgnoreCase("XLS") || fileFormat.equalsIgnoreCase("PED"))) {
				throw new FileFormatException();
			}
			
			//Create Upload
			iArkCommonService.createUpload(containerForm.getModelObject().getUpload());

			if(containerForm.getModelObject().getUpload().getUploadType().getName().equalsIgnoreCase(Constants.SUBJECT_DEMOGRAPHIC_DATA)){
				SubjectUploadValidator subjectUploadValidator = new SubjectUploadValidator(iArkCommonService);
				validationMessages = subjectUploadValidator.validateSubjectFileFormat(containerForm.getModelObject());				
			}
			else if(containerForm.getModelObject().getUpload().getUploadType().getName().equalsIgnoreCase(Constants.STUDY_SPECIFIC_CUSTOM_DATA)){
				//TODO : custom field validation
				CustomFieldUploadValidator customFieldUploadValidator = new CustomFieldUploadValidator(iArkCommonService);
				validationMessages = customFieldUploadValidator.validateCustomFieldFileFormat(containerForm.getModelObject());			
			}
			else if(containerForm.getModelObject().getUpload().getUploadType().getName().equalsIgnoreCase(Constants.SUBJECT_CONSENT_DATA)){
				SubjectConsentUploadValidator subjectConsentUploadValidator=new SubjectConsentUploadValidator();
				validationMessages = subjectConsentUploadValidator.validateSubjectConsentFileFormat(containerForm.getModelObject());
			}
			else if(containerForm.getModelObject().getUpload().getUploadType().getName().equalsIgnoreCase(Constants.PEDIGREE_DATA)){
				PedigreeUploadValidator subjectConsentUploadValidator=new PedigreeUploadValidator();
				validationMessages = subjectConsentUploadValidator.validatePedigreeFileFormat(containerForm.getModelObject());
			}
			else if(containerForm.getModelObject().getUpload().getUploadType().getName().equalsIgnoreCase(Constants.SUBJECT_ATTACHMENT_DATA)){
				SubjectAttachmentValidator subjectAttachmentValidator = new SubjectAttachmentValidator();
				validationMessages = subjectAttachmentValidator.validateSubjectAttachmentFileFormat(containerForm.getModelObject());
			}
			else{
				//TODO : Throw error back to user
			}

			ArkExcelWorkSheetAsGrid arkExcelWorkSheetAsGrid=null; 
			if(Constants.PEDIGREE_DATA.equalsIgnoreCase(containerForm.getModelObject().getUpload().getUploadType().getName())){
				arkExcelWorkSheetAsGrid= new ArkExcelWorkSheetAsGrid("gridView", inputStream, fileFormat, delimChar, 
					fileUpload, iArkCommonService.getRowsPerPage(), containerForm.getModelObject().getUpload().getUploadType(),false);
			}else{
				arkExcelWorkSheetAsGrid = new ArkExcelWorkSheetAsGrid("gridView", inputStream, fileFormat, delimChar, 
						fileUpload, iArkCommonService.getRowsPerPage(), containerForm.getModelObject().getUpload().getUploadType());
			}
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
				
				this.containerForm.getModelObject().getUpload().setUploadStatus(iArkCommonService.getUploadStatusFor(au.org.theark.study.web.Constants.UPLOAD_STATUS_OF_ERROR_IN_FILE_VALIDATION));
				this.containerForm.getModelObject().getUpload().setFilename(filename);//have to reset this because the container has the file name...luckily it never changes 
				iArkCommonService.updateUpload(this.containerForm.getModelObject().getUpload());

				log.warn("validation = " + validationMessage);
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
			/*else{
				this.containerForm.getModelObject().getUpload().setUploadStatus(iArkCommonService.getUploadStatusFor(au.org.theark.study.web.Constants.UPLOAD_STATUS_OF_VALIDATED));
				this.containerForm.getModelObject().getUpload().setFilename(filename);//have to reset this because the container has the file name...luckily it never changes 
				iArkCommonService.updateUpload(this.containerForm.getModelObject().getUpload());
			}*/
			
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
		catch(Exception e){
			log.error("unexpected exception, not shown to user...INVESTIGATE :\n", e);
			validationMessage = "Error attempting to validate the file. Please contact the system administrator.";
			addOrReplace(new MultiLineLabel("multiLineLabel", validationMessage));
			form.getNextButton().setEnabled(false);
			target.add(form.getWizardButtonContainer());
		}
		//TODO : finally?  close io?
	}

	@Override
	public void onStepOutNext(AbstractWizardForm<?> form, AjaxRequestTarget target) {
	}
}
