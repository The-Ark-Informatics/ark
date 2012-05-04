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
package au.org.theark.phenotypic.web.component.phenofielduploader.form;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.util.file.File;

import au.org.theark.core.vo.ArkCrudContainerVO;
import au.org.theark.core.web.form.AbstractWizardForm;
import au.org.theark.phenotypic.model.vo.PhenoFieldUploadVO;
import au.org.theark.phenotypic.web.component.phenofielduploader.FieldUploadStep1;
import au.org.theark.phenotypic.web.component.phenofielduploader.FieldUploadStep2;
import au.org.theark.phenotypic.web.component.phenofielduploader.FieldUploadStep3;
import au.org.theark.phenotypic.web.component.phenofielduploader.FieldUploadStep4;
import au.org.theark.phenotypic.web.component.phenofielduploader.FieldUploadStep5;

/**
 * @author cellis
 * @author elam
 */
public class WizardForm extends AbstractWizardForm<PhenoFieldUploadVO> {

	private static final long	serialVersionUID	= 5494062431372163197L;

	private File						file;
	private String						fileName;
	
	public WizardForm(String id, FeedbackPanel feedBackPanel, ContainerForm containerForm, ArkCrudContainerVO arkCrudContainerVO) {
		// TODO: Fix the AbstractWizardForm to use ArkCrudContainerVO
		super(id, feedBackPanel, arkCrudContainerVO.getSearchResultPanelContainer(),
				arkCrudContainerVO.getWizardPanelContainer(), arkCrudContainerVO.getWizardPanelFormContainer(), 
				arkCrudContainerVO.getSearchPanelContainer(), containerForm);
	}

	public void initialiseDetailForm() {
		initialiseSteps();
		addComponents();
	}

	public void initialiseSteps() {
		FieldUploadStep1 step1 = new FieldUploadStep1("step", containerForm, this);
		FieldUploadStep2 step2 = new FieldUploadStep2("step", containerForm, this);
		FieldUploadStep3 step3 = new FieldUploadStep3("step", containerForm, this);
		FieldUploadStep4 step4 = new FieldUploadStep4("step", containerForm, this);
		FieldUploadStep5 step5 = new FieldUploadStep5("step", containerForm);

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
	public void onFinish(AjaxRequestTarget target, Form<?> form) {
		this.info("Data upload of file: " + containerForm.getModelObject().getUpload().getFilename() + " was uploaded successfully");
		onCancel(target);
	}

	@Override
	protected void onCancel(AjaxRequestTarget target) {
		// Implement Cancel
		PhenoFieldUploadVO uploadVO = new PhenoFieldUploadVO();
		uploadVO.getUpload().setArkFunction(containerForm.getModelObject().getUpload().getArkFunction());
		containerForm.setModelObject(uploadVO);
		initialiseSteps();
		onCancelPostProcess(target);
	}

	@Override
	protected void processErrors(AjaxRequestTarget target) {
		target.add(feedBackPanel);
	}

	@Override
	public void onError(AjaxRequestTarget target, Form<?> form) {
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
