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
package au.org.theark.study.web.component.correspondence.form;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.apache.shiro.SecurityUtils;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.extensions.markup.html.form.DateTextField;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.form.upload.FileUpload;
import org.apache.wicket.markup.html.form.upload.FileUploadField;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.util.lang.Bytes;
import org.apache.wicket.validation.validator.DateValidator;

import au.org.theark.core.exception.ArkSystemException;
import au.org.theark.core.exception.EntityNotFoundException;
import au.org.theark.core.model.study.entity.ArkUser;
import au.org.theark.core.model.study.entity.CorrespondenceDirectionType;
import au.org.theark.core.model.study.entity.CorrespondenceModeType;
import au.org.theark.core.model.study.entity.CorrespondenceOutcomeType;
import au.org.theark.core.model.study.entity.CorrespondenceStatusType;
import au.org.theark.core.model.study.entity.Person;
import au.org.theark.core.model.study.entity.Study;
import au.org.theark.core.service.IArkCommonService;
import au.org.theark.core.vo.ArkCrudContainerVO;
import au.org.theark.core.vo.CorrespondenceVO;
import au.org.theark.core.web.behavior.ArkDefaultFormFocusBehavior;
import au.org.theark.core.web.component.ArkDatePicker;
import au.org.theark.core.web.form.AbstractDetailForm;
import au.org.theark.study.service.IStudyService;
import au.org.theark.study.web.Constants;

public class DetailForm extends AbstractDetailForm<CorrespondenceVO> {

	private static final long										serialVersionUID	= 2900999695563378447L;
	
	@SpringBean(name = au.org.theark.core.Constants.ARK_COMMON_SERVICE)
	protected IArkCommonService<Void>							iArkCommonService;

	@SpringBean(name = Constants.STUDY_SERVICE)
	private IStudyService											studyService;

	private DropDownChoice<CorrespondenceStatusType>		statusTypeChoice;
	private DropDownChoice<ArkUser>								operatorChoice;
	private DateTextField											dateFld;
	private TextField<String>										timeTxtFld;
	private DropDownChoice<CorrespondenceModeType>			modeTypeChoice;
	private DropDownChoice<CorrespondenceDirectionType>	directionTypeChoice;
	private DropDownChoice<CorrespondenceOutcomeType>		outcomeTypeChoice;
	private TextArea<String>										reasonTxtArea;
	private TextArea<String>										detailsTxtArea;
	private TextArea<String>										commentsTxtArea;
	private FileUploadField											fileUploadField;

	public DetailForm(String id, FeedbackPanel feedBackPanel, ContainerForm containerForm, ArkCrudContainerVO arkCrudContainerVO) {
		super(id, feedBackPanel, containerForm, arkCrudContainerVO);
		setMultiPart(true);
	}

	public void initialiseDetailForm() {

		initialiseStatusTypeDropDown();
		initialiseOperatorDropDown();
		// create new DateTextField and assign date format
		dateFld = new DateTextField("correspondence.date", au.org.theark.core.Constants.DD_MM_YYYY);
		ArkDatePicker datePicker = new ArkDatePicker();
		datePicker.bind(dateFld);
		dateFld.add(datePicker);

		timeTxtFld = new TextField<String>("correspondence.time");
		initialiseModeTypeDropDown();
		initialiseDirectionTypeDropDown();
		initialiseOutcomeTypeDropDown();
		reasonTxtArea = new TextArea<String>("correspondence.reason");
		detailsTxtArea = new TextArea<String>("correspondence.details");
		commentsTxtArea = new TextArea<String>("correspondence.comments");

		// fileUploadField = new FileUploadField("correspondence.attachmentFilename", new Model<List<FileUpload>>());
		fileUploadField = new FileUploadField("correspondence.attachmentFilename");
		setMaxSize(Bytes.kilobytes(2048));

		addDetailFormComponents();
		attachValidators();
	}

	private void initialiseStatusTypeDropDown() {

		List<CorrespondenceStatusType> list = studyService.getCorrespondenceStatusTypes();
		ChoiceRenderer<CorrespondenceStatusType> defaultRenderer = new ChoiceRenderer<CorrespondenceStatusType>("name", "id");
		statusTypeChoice = new DropDownChoice<CorrespondenceStatusType>("correspondence.correspondenceStatusType", list, defaultRenderer);
		statusTypeChoice.add(new ArkDefaultFormFocusBehavior());
	}

	private void initialiseOperatorDropDown() {

		Long sessionStudyId = (Long) SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.core.Constants.STUDY_CONTEXT_ID);
		Study study = iArkCommonService.getStudy(sessionStudyId);

		Collection<ArkUser> coll = studyService.lookupArkUser(study);
		List<ArkUser> list = new ArrayList<ArkUser>(coll);

		ChoiceRenderer<ArkUser> defaultRenderer = new ChoiceRenderer<ArkUser>("ldapUserName", "id");
		operatorChoice = new DropDownChoice<ArkUser>("correspondence.operator", list, defaultRenderer);
	}

	private void initialiseModeTypeDropDown() {

		List<CorrespondenceModeType> list = studyService.getCorrespondenceModeTypes();
		ChoiceRenderer<CorrespondenceModeType> defaultRenderer = new ChoiceRenderer<CorrespondenceModeType>("name", "id");
		modeTypeChoice = new DropDownChoice<CorrespondenceModeType>("correspondence.correspondenceModeType", list, defaultRenderer);
	}

	private void initialiseDirectionTypeDropDown() {

		List<CorrespondenceDirectionType> list = studyService.getCorrespondenceDirectionTypes();
		ChoiceRenderer<CorrespondenceDirectionType> defaultRenderer = new ChoiceRenderer<CorrespondenceDirectionType>("name", "id");
		directionTypeChoice = new DropDownChoice<CorrespondenceDirectionType>("correspondence.correspondenceDirectionType", list, defaultRenderer);
	}

	private void initialiseOutcomeTypeDropDown() {

		List<CorrespondenceOutcomeType> list = studyService.getCorrespondenceOutcomeTypes();
		ChoiceRenderer<CorrespondenceOutcomeType> defaultRenderer = new ChoiceRenderer<CorrespondenceOutcomeType>("name", "id");
		outcomeTypeChoice = new DropDownChoice<CorrespondenceOutcomeType>("correspondence.correspondenceOutcomeType", list, defaultRenderer);
	}

	public void addDetailFormComponents() {

		arkCrudContainerVO.getDetailPanelFormContainer().add(statusTypeChoice);
		arkCrudContainerVO.getDetailPanelFormContainer().add(operatorChoice);
		arkCrudContainerVO.getDetailPanelFormContainer().add(dateFld);
		arkCrudContainerVO.getDetailPanelFormContainer().add(timeTxtFld);
		arkCrudContainerVO.getDetailPanelFormContainer().add(modeTypeChoice);
		arkCrudContainerVO.getDetailPanelFormContainer().add(directionTypeChoice);
		arkCrudContainerVO.getDetailPanelFormContainer().add(outcomeTypeChoice);
		arkCrudContainerVO.getDetailPanelFormContainer().add(reasonTxtArea);
		arkCrudContainerVO.getDetailPanelFormContainer().add(detailsTxtArea);
		arkCrudContainerVO.getDetailPanelFormContainer().add(commentsTxtArea);
		arkCrudContainerVO.getDetailPanelFormContainer().add(fileUploadField);
	}

	protected void attachValidators() {
		dateFld.add(DateValidator.maximum(new Date())).setLabel(new StringResourceModel("correspondence.date", this, null));
		dateFld.setRequired(true);
	}

	@Override
	protected void onCancel(AjaxRequestTarget target) {

		CorrespondenceVO correspondenceVO = new CorrespondenceVO();
		containerForm.setModelObject(correspondenceVO);
	}

	@Override
	protected void onDeleteConfirmed(AjaxRequestTarget target, String selection) {

		try {
			studyService.delete(containerForm.getModelObject().getCorrespondence());
			containerForm.info("The correspondence has been deleted successfully.");
			editCancelProcess(target);
		}
		catch (Exception ex) {
			this.error("An error occurred while processing the correspondence delete operation.");
			ex.printStackTrace();
		}
		onCancel(target);
	}

	@Override
	protected void onSave(Form<CorrespondenceVO> containerForm, AjaxRequestTarget target) {

		Long personSessionId = (Long) SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.core.Constants.PERSON_CONTEXT_ID);

		// get the person and set it on the correspondence object
		try {
			// set the Person in context
			Person person = studyService.getPerson(personSessionId);
			containerForm.getModelObject().getCorrespondence().setPerson(person);
			// set the Study in context
			Long studyId = (Long) SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.core.Constants.STUDY_CONTEXT_ID);
			Study study = iArkCommonService.getStudy(studyId);
			containerForm.getModelObject().getCorrespondence().setStudy(study);

			if (containerForm.getModelObject().getCorrespondence().getId() == null) {
				// store correspondence file attachment
				if (fileUploadField != null && fileUploadField.getFileUpload() != null) {
					// retrieve file and store as Blob in database
					FileUpload fileUpload = fileUploadField.getFileUpload();
					// copy file to Blob object
					//Blob payload = util.createBlob(fileUpload.getInputStream(), fileUpload.getSize());
					containerForm.getModelObject().getCorrespondence().setAttachmentPayload(fileUpload.getBytes());
					containerForm.getModelObject().getCorrespondence().setAttachmentFilename(fileUpload.getClientFileName());
				}

				// save
				studyService.create(containerForm.getModelObject().getCorrespondence());
				this.info("Correspondence was successfully added and linked to subject: " + person.getFirstName() + " " + person.getLastName());
				processErrors(target);
			}
			else {
				// store correspondence file attachment
				if (fileUploadField != null && fileUploadField.getFileUpload() != null) {
					// retrieve file and store as Blob in database
					FileUpload fileUpload = fileUploadField.getFileUpload();
					// copy file to Blob object
//					Blob payload = util.createBlob(fileUpload.getInputStream(), fileUpload.getSize());
					containerForm.getModelObject().getCorrespondence().setAttachmentPayload(fileUpload.getBytes());
					containerForm.getModelObject().getCorrespondence().setAttachmentFilename(fileUpload.getClientFileName());
				}

				studyService.update(containerForm.getModelObject().getCorrespondence());
				this.info("Correspondence was successfully updated and linked to subject: " + person.getFirstName() + " " + person.getLastName());
				processErrors(target);
			}
			// invoke backend to persist the correspondence
			onSavePostProcess(target);
		}
		catch (EntityNotFoundException ex) {
			ex.printStackTrace();
		}
		catch (ArkSystemException ex) {
			ex.printStackTrace();
		}
	/*	catch (IOException ex) {
			this.error("There was an error transferring the specified correspondence attachment.");
			ex.printStackTrace();
		}*/
	}

	@Override
	protected void processErrors(AjaxRequestTarget target) {
		target.add(feedBackPanel);
	}

	@Override
	protected boolean isNew() {

		if (containerForm.getModelObject().getCorrespondence().getId() == null) {
			return true;
		}
		else {
			return false;
		}
	}

}
