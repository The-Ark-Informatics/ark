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
import java.sql.Blob;
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
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import au.org.theark.core.Constants;
import au.org.theark.core.dao.LobUtil;
import au.org.theark.core.model.study.entity.DelimiterType;
import au.org.theark.core.model.study.entity.Study;
import au.org.theark.core.service.IArkCommonService;
import au.org.theark.core.vo.UploadVO;
import au.org.theark.core.web.form.AbstractWizardForm;
import au.org.theark.core.web.form.AbstractWizardStepPanel;
import au.org.theark.study.web.component.subjectUpload.form.WizardForm;

/**
 * The first step of this wizard.
 */
public class SubjectUploadStep1 extends AbstractWizardStepPanel {

	private static final long					serialVersionUID		= -3267334731280446472L;

	public java.util.Collection<String>		validationMessages	= null;

	@SpringBean(name = au.org.theark.core.Constants.ARK_COMMON_SERVICE)
	private IArkCommonService<Void>			iArkCommonService;

	@SpringBean(name = "lobUtil")
	private LobUtil			util;

	private Form<UploadVO>						containerForm;
	private FileUploadField						fileUploadField;
	// private UploadProgressBar uploadProgressBar;
	private DropDownChoice<DelimiterType>	delimiterTypeDdc;
	private WizardForm							wizardForm;

	public SubjectUploadStep1(String id) {
		super(id);
		initialiseDetailForm();
	}

	public SubjectUploadStep1(String id, Form<UploadVO> containerForm, WizardForm wizardForm) {
		super(id, "Step 1/5: Select data file to upload", "Select the file containing data, the file type and the specified delimiter, click Next to continue.");

		this.containerForm = containerForm;
		this.setWizardForm(wizardForm);
		initialiseDetailForm();
	}

	@SuppressWarnings( { "unchecked" })
	private void initialiseDropDownChoices() {
		// Initialise Drop Down Choices
		java.util.Collection<DelimiterType> delimiterTypeCollection = iArkCommonService.getDelimiterTypes();
		ChoiceRenderer delimiterTypeRenderer = new ChoiceRenderer(au.org.theark.study.web.Constants.DELIMITER_TYPE_NAME, au.org.theark.study.web.Constants.DELIMITER_TYPE_ID);
		delimiterTypeDdc = new DropDownChoice<DelimiterType>(au.org.theark.study.web.Constants.UPLOADVO_UPLOAD_DELIMITER_TYPE, (List) delimiterTypeCollection, delimiterTypeRenderer);
		containerForm.getModelObject().getUpload().setDelimiterType(iArkCommonService.getDelimiterType(new Long(1)));
	}

	public void initialiseDetailForm() {
		// Set up field on form here   
		// uploadProgressBar = new UploadProgressBar("progress", ajaxSimpleUploadForm);
		fileUploadField = new FileUploadField(au.org.theark.study.web.Constants.UPLOADVO_UPLOAD_FILENAME);
		initialiseDropDownChoices();
		attachValidators();
		addComponents();
	}

	protected void attachValidators() {
		// Field validation here
		fileUploadField.setRequired(true).setLabel(new StringResourceModel("error.filename.required", this, new Model<String>("Filename")));
		delimiterTypeDdc.setRequired(true).setLabel(new StringResourceModel("error.delimiterType.required", this, new Model<String>("Delimiter")));
	}

	private void addComponents() {
		// Add components here
		add(fileUploadField);
		add(delimiterTypeDdc);
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
		Long studyId = (Long) SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.core.Constants.STUDY_CONTEXT_ID);
		Study study = iArkCommonService.getStudy(studyId);

		FileUpload fileUpload = fileUploadField.getFileUpload();
		containerForm.getModelObject().setFileUpload(fileUpload);	//TODO analyze why VO pattern throughout code, is it always necessary in attion to entity/detached-entity concepts

		try {
			Blob payload = util.createBlob(fileUpload.getInputStream(), fileUpload.getSize());
			containerForm.getModelObject().getUpload().setPayload(payload);
		}
		catch (IOException ioe) {
			log.error("Failed to save the uploaded file: " + ioe);
		}
		String filename = containerForm.getModelObject().getFileUpload().getClientFileName();
		String fileFormatName = filename.substring(filename.lastIndexOf('.') + 1).toUpperCase();
		au.org.theark.core.model.study.entity.FileFormat fileFormat = new au.org.theark.core.model.study.entity.FileFormat();
		fileFormat = iArkCommonService.getFileFormatByName(fileFormatName);
		byte[] byteArray = fileUpload.getMD5();
		String checksum = getHex(byteArray);
		
		containerForm.getModelObject().getUpload().setStudy(study);//TODO: analyze costs of repeated containerForm.getModelObject().getUpload()
		containerForm.getModelObject().getUpload().setFileFormat(fileFormat);
		containerForm.getModelObject().getUpload().setChecksum(checksum);
		//containerForm.getModelObject().getUpload().;
		containerForm.getModelObject().getUpload().setFilename(filename);
		containerForm.getModelObject().getUpload().setStartTime(new Date(System.currentTimeMillis()));
		containerForm.getModelObject().getUpload().setArkFunction(iArkCommonService.getArkFunctionByName(Constants.FUNCTION_KEY_VALUE_SUBJECT_UPLOAD));

		wizardForm.setFileName(filename);

		//TODO analyse how many times this is saved and where it should be saved
		iArkCommonService.createUpload(containerForm.getModelObject().getUpload());
	}
}
