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
package au.org.theark.study.web.component.subjectUpload.form;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.util.file.File;

import au.org.theark.core.service.IArkCommonService;
import au.org.theark.core.vo.UploadVO;
import au.org.theark.core.web.form.AbstractWizardForm;
import au.org.theark.study.service.IStudyService;
import au.org.theark.study.web.component.subjectUpload.SubjectUploadStep1;
import au.org.theark.study.web.component.subjectUpload.SubjectUploadStep2;
import au.org.theark.study.web.component.subjectUpload.SubjectUploadStep3;
import au.org.theark.study.web.component.subjectUpload.SubjectUploadStep4;
import au.org.theark.study.web.component.subjectUpload.SubjectUploadStep5;
import au.org.theark.study.web.component.subjectUpload.WizardPanel;

/**
 * @author cellis
 * 
 */
@SuppressWarnings({ "serial", "unused" })
public class WizardForm extends AbstractWizardForm<UploadVO> {
	@SpringBean(name = au.org.theark.core.Constants.STUDY_SERVICE)
	private IStudyService		studyService;

	@SpringBean(name = au.org.theark.core.Constants.ARK_COMMON_SERVICE)
	private IArkCommonService	iArkCommonService;

	private File					file;
	private String					fileName;

	/**
	 * Constructor
	 * 
	 * @param id
	 * @param feedBackPanel
	 * @param wizardStep
	 * @param listContainer
	 * @param wizardContainer
	 * @param containerForm
	 * @param wizardButtonContainer
	 * @param wizardFormContainer
	 * @param searchPanelContainer
	 */
	public WizardForm(String id, FeedbackPanel feedBackPanel, WizardPanel wizardPanel, WebMarkupContainer listContainer, WebMarkupContainer wizardContainer, Form<UploadVO> containerForm,
			WebMarkupContainer wizardButtonContainer, WebMarkupContainer wizardFormContainer, WebMarkupContainer searchPanelContainer) {
		super(id, feedBackPanel, listContainer, wizardContainer, wizardFormContainer, searchPanelContainer, containerForm);
	}

	public void initialiseDetailForm() {
		initialiseSteps();
		addComponents();
	}

	public void initialiseSteps() {
		SubjectUploadStep1 step1 = new SubjectUploadStep1("step", containerForm, this);
		SubjectUploadStep2 step2 = new SubjectUploadStep2("step", containerForm, this);
		SubjectUploadStep3 step3 = new SubjectUploadStep3("step", containerForm, this);
		SubjectUploadStep4 step4 = new SubjectUploadStep4("step", containerForm, this);
		SubjectUploadStep5 step5 = new SubjectUploadStep5("step", containerForm);

		step1.setNextStep(step2);
		step2.setNextStep(step3);
		step3.setPreviousStep(step2);
		step3.setNextStep(step4);
		step4.setPreviousStep(step3);
		step4.setNextStep(step5);

		wizardPanelFormContainer.addOrReplace(step1);
	}

	private void addComponents() {
		add(wizardPanelFormContainer);
	}

	@Override
	public void onFinish(AjaxRequestTarget target, Form form) {
		this.info("Data upload of file: " + containerForm.getModelObject().getUpload().getFilename() + " was uploaded successfully");
		onCancel(target);
	}

	@Override
	protected void onCancel(AjaxRequestTarget target) {
		// Implement Cancel
		UploadVO uploadVO = new UploadVO();
		containerForm.setModelObject(uploadVO);
		initialiseSteps();
		onCancelPostProcess(target);
	}

	@Override
	protected void processErrors(AjaxRequestTarget target) {
		target.addComponent(feedBackPanel);
	}

	@Override
	public void onError(AjaxRequestTarget target, Form form) {
		processErrors(target);
	}

	public File getFile() {
		return file;
	}

	public void setFile(File file) {
		this.file = file;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
}
