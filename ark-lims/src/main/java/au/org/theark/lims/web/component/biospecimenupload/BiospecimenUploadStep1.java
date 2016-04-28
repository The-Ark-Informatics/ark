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

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.shiro.SecurityUtils;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.upload.FileUpload;
import org.apache.wicket.markup.html.form.upload.FileUploadField;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import au.org.theark.core.Constants;
import au.org.theark.core.model.study.entity.DelimiterType;
import au.org.theark.core.model.study.entity.Payload;
import au.org.theark.core.model.study.entity.Study;
import au.org.theark.core.model.study.entity.UploadType;
import au.org.theark.core.service.IArkCommonService;
import au.org.theark.core.vo.UploadVO;
import au.org.theark.core.web.form.AbstractWizardForm;
import au.org.theark.core.web.form.AbstractWizardStepPanel;
import au.org.theark.lims.web.component.biospecimenupload.form.WizardForm;
/**
 * The first step of this wizard.
 */
public class BiospecimenUploadStep1 extends AbstractWizardStepPanel {


	private static final long					serialVersionUID		= -3267334731280446472L;

	public java.util.Collection<String>		validationMessages	= null;

	@SpringBean(name = au.org.theark.core.Constants.ARK_COMMON_SERVICE)
	private IArkCommonService<Void>			iArkCommonService;

	private Form<UploadVO>						containerForm;
	
	private DropDownChoice<Study>				studyDdc;
	private FileUploadField						fileUploadField;
	// private UploadProgressBar uploadProgressBar;
	private DropDownChoice<DelimiterType>	delimiterTypeDdc;
	private WizardForm							wizardForm;

	private DropDownChoice<UploadType>		uploadTypeDdc;
	
	public BiospecimenUploadStep1(String id) {
		super(id);
		initialiseDetailForm();
	}

	public BiospecimenUploadStep1(String id, Form<UploadVO> containerForm, WizardForm wizardForm) {
		super(id, "Step 1/5: Select data file to upload", "Select the file containing data, the Study and the specified delimiter, click Next to continue.");

		this.containerForm = containerForm;
		this.setWizardForm(wizardForm);
		initialiseDetailForm();
	}

	private void initialiseDropDownChoices() {
		// Initialise Drop Down Choices
		java.util.Collection<DelimiterType> delimiterTypeCollection = iArkCommonService.getDelimiterTypes();
		ChoiceRenderer<DelimiterType> choiceRenderer = new ChoiceRenderer<DelimiterType>(Constants.NAME, Constants.ID);
		delimiterTypeDdc = new DropDownChoice<DelimiterType>(Constants.UPLOADVO_UPLOAD_DELIMITER_TYPE, (List<DelimiterType>) delimiterTypeCollection, choiceRenderer);
		// Set to default delimiterType
		containerForm.getModelObject().getUpload().setDelimiterType(iArkCommonService.getDelimiterType(new Long(1)));//TODO:  This looks hardcoded...please check different types

		java.util.Collection<UploadType> uploadTypeCollection = iArkCommonService.getUploadTypesForLims();
		ChoiceRenderer uploadTypeRenderer = new ChoiceRenderer(Constants.UPLOAD_TYPE_NAME, Constants.UPLOAD_TYPE_ID);
		uploadTypeDdc = new DropDownChoice<UploadType>(Constants.UPLOADVO_UPLOAD_UPLOAD_TYPE, (List) uploadTypeCollection, uploadTypeRenderer);
		
		
		
		List<Study> studyListForUser = new ArrayList<Study>(0);
		Long sessionStudyId = (Long) SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.core.Constants.STUDY_CONTEXT_ID);
		Study study = null;
		if(sessionStudyId != null) {
			study = iArkCommonService.getStudy(sessionStudyId);
			studyListForUser.add(study);
		}
		ChoiceRenderer<Study> studyRenderer = new ChoiceRenderer<Study>(Constants.NAME, Constants.ID);
		PropertyModel<Study> pModel = new PropertyModel<Study>(containerForm.getModelObject(), "upload.study");
		studyDdc = new DropDownChoice<Study>(Constants.UPLOADVO_UPLOAD_STUDY, pModel, (List<Study>) studyListForUser, studyRenderer);
		containerForm.getModelObject().getUpload().setUploadType(iArkCommonService.getDefaultUploadType());
	}

	public void initialiseDetailForm() {
		// Set up field on form here

		// progress bar for upload
		// uploadProgressBar = new UploadProgressBar("progress",
		// ajaxSimpleUploadForm);

		// fileUpload for payload
		fileUploadField = new FileUploadField(Constants.UPLOADVO_UPLOAD_FILENAME);

		// Initialise Drop Down Choices
		initialiseDropDownChoices();

		attachValidators();
		addComponents();
	}

	protected void attachValidators() {
		// Field validation here
		studyDdc.setRequired(true).setLabel(new StringResourceModel("error.study.required", this, new Model<String>("Study")));
		fileUploadField.setRequired(true).setLabel(new StringResourceModel("error.filename.required", this, new Model<String>("Filename")));
		delimiterTypeDdc.setRequired(true).setLabel(new StringResourceModel("error.delimiterType.required", this, new Model<String>("Delimiter")));
	}

	private void addComponents() {
		// Add components here
		add(studyDdc);
		add(fileUploadField);
		add(delimiterTypeDdc);
		add(uploadTypeDdc);
	}

	@Override
	public void handleWizardState(AbstractWizardForm<?> form, AjaxRequestTarget target) {
	}

	@Override
	public void onStepOutNext(AbstractWizardForm<?> form, AjaxRequestTarget target) {
		saveFileInMemory();
	}

	public void setWizardForm(WizardForm wizardForm) {
		this.wizardForm = wizardForm;
	}

	public WizardForm getWizardForm() {
		return wizardForm;
	}

	private void saveFileInMemory() {
		// Retrieve file and store as Blob in database
		// TODO: AJAX-ified and asynchronous and hit database
		FileUpload fileUpload = fileUploadField.getFileUpload();
		containerForm.getModelObject().setFileUpload(fileUpload);
		Payload payload = iArkCommonService.createPayload(fileUpload.getBytes());
		containerForm.getModelObject().getUpload().setPayload(payload);

		// Set details of Upload object
		containerForm.getModelObject().getUpload().setStudy(studyDdc.getModelObject());
		String filename = containerForm.getModelObject().getFileUpload().getClientFileName();
		String fileFormatName = filename.substring(filename.lastIndexOf('.') + 1).toUpperCase();
		au.org.theark.core.model.study.entity.FileFormat fileFormat = new au.org.theark.core.model.study.entity.FileFormat();
		fileFormat = iArkCommonService.getFileFormatByName(fileFormatName);
		containerForm.getModelObject().getUpload().setFileFormat(fileFormat);

		byte[] byteArray = fileUpload.getMD5();
		String checksum = getHex(byteArray);
		containerForm.getModelObject().getUpload().setChecksum(checksum);
		containerForm.getModelObject().getUpload().setFilename(fileUpload.getClientFileName());
		containerForm.getModelObject().getUpload().setStartTime(new Date(System.currentTimeMillis()));
		containerForm.getModelObject().setUploadType(uploadTypeDdc.getModelObject()==null?"Unknown":uploadTypeDdc.getModelObject().getName());	
		containerForm.getModelObject().getUpload().setArkFunction(iArkCommonService.getArkFunctionByName(Constants.FUNCTION_KEY_VALUE_BIOSPECIMEN_UPLOAD));
		wizardForm.setFileName(fileUpload.getClientFileName());

		containerForm.getModelObject().getUpload().setUploadStatus(iArkCommonService.getUploadStatusFor(Constants.UPLOAD_STATUS_AWAITING_VALIDATION));		
		
		iArkCommonService.createUpload(containerForm.getModelObject().getUpload());
	}
}
