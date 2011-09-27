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
package au.org.theark.study.web.component.studycomponent.form;

import org.apache.shiro.SecurityUtils;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.validation.validator.StringValidator;

import au.org.theark.core.exception.ArkSystemException;
import au.org.theark.core.exception.EntityCannotBeRemoved;
import au.org.theark.core.exception.EntityExistsException;
import au.org.theark.core.exception.UnAuthorizedOperation;
import au.org.theark.core.model.study.entity.Study;
import au.org.theark.core.service.IArkCommonService;
import au.org.theark.core.vo.ArkCrudContainerVO;
import au.org.theark.core.web.behavior.ArkDefaultFormFocusBehavior;
import au.org.theark.core.web.form.AbstractDetailForm;
import au.org.theark.study.model.vo.StudyCompVo;
import au.org.theark.study.service.IStudyService;
import au.org.theark.study.web.Constants;

/**
 * @author nivedann
 * 
 */
public class DetailForm extends AbstractDetailForm<StudyCompVo> {

	@SpringBean(name = au.org.theark.core.Constants.ARK_COMMON_SERVICE)
	private IArkCommonService	iArkCommonService;

	@SpringBean(name = "studyService")
	private IStudyService		studyService;
	private Study					study;

	private TextField<String>	componentIdTxtFld;
	private TextField<String>	componentNameTxtFld;
	private TextArea<String>	componentDescription;
	private TextArea<String>	keywordTxtArea;
	
	private FeedbackPanel feedBackPanel;

	
	/**
	 * 
	 * @param id
	 * @param feedBackPanel
	 * @param arkCrudContainerVO
	 * @param containerForm
	 */
	public DetailForm(String id, FeedbackPanel feedBackPanel, ArkCrudContainerVO arkCrudContainerVO,ContainerForm containerForm){
		
		super(id,feedBackPanel,containerForm,arkCrudContainerVO);
		this.feedBackPanel = feedBackPanel;
		setMultiPart(false);
	}
	

	public void initialiseDetailForm() {

		componentIdTxtFld = new TextField<String>(Constants.STUDY_COMPONENT_ID);
		componentIdTxtFld.setEnabled(false);
		componentNameTxtFld = new TextField<String>(Constants.STUDY_COMPONENT_NAME);
		componentNameTxtFld.add(new ArkDefaultFormFocusBehavior());
		componentDescription = new TextArea<String>(Constants.STUDY_COMPONENT_DESCRIPTION);
		keywordTxtArea = new TextArea<String>(Constants.STUDY_COMPONENT_KEYWORD);
		addDetailFormComponents();
		attachValidators();
	}

	public void addDetailFormComponents() {
		arkCrudContainerVO.getDetailPanelFormContainer().add(componentIdTxtFld);
		arkCrudContainerVO.getDetailPanelFormContainer().add(componentNameTxtFld);
		arkCrudContainerVO.getDetailPanelFormContainer().add(componentDescription);
		arkCrudContainerVO.getDetailPanelFormContainer().add(keywordTxtArea);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see au.org.theark.core.web.form.AbstractDetailForm#attachValidators()
	 */
	@Override
	protected void attachValidators() {
		componentNameTxtFld.setRequired(true).setLabel(new StringResourceModel("error.study.component.name.required", componentNameTxtFld, new Model<String>("Study Component Name")));
		componentNameTxtFld.add(StringValidator.lengthBetween(3, 100)).setLabel(
				new StringResourceModel("error.study.component.name.length", componentNameTxtFld, new Model<String>("Study Component Name")));
		componentDescription.add(StringValidator.lengthBetween(5, 500)).setLabel(new StringResourceModel("error.study.component.description.length", this, new Model<String>("Description")));
		keywordTxtArea.add(StringValidator.lengthBetween(1, 255)).setLabel(new StringResourceModel("error.study.component.keywords.length", this, new Model<String>("Keywords")));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see au.org.theark.core.web.form.AbstractDetailForm#onCancel(org.apache.wicket.ajax.AjaxRequestTarget)
	 */
	@Override
	protected void onCancel(AjaxRequestTarget target) {

		StudyCompVo studyCompVo = new StudyCompVo();
		containerForm.setModelObject(studyCompVo);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see au.org.theark.core.web.form.AbstractDetailForm#onSave(org.apache.wicket.markup.html.form.Form, org.apache.wicket.ajax.AjaxRequestTarget)
	 */
	@Override
	protected void onSave(Form<StudyCompVo> containerForm, AjaxRequestTarget target) {
		
		target.add(arkCrudContainerVO.getDetailPanelContainer());
		try {

			Long studyId = (Long) SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.core.Constants.STUDY_CONTEXT_ID);
			study = iArkCommonService.getStudy(studyId);
			containerForm.getModelObject().getStudyComponent().setStudy(study);

			if (containerForm.getModelObject().getStudyComponent().getId() == null) {

				studyService.create(containerForm.getModelObject().getStudyComponent());
				this.info("Study Component " + containerForm.getModelObject().getStudyComponent().getName() + " was created successfully");
				processErrors(target);

			}
			else {

				studyService.update(containerForm.getModelObject().getStudyComponent());
				this.info("Study Component " + containerForm.getModelObject().getStudyComponent().getName() + " was updated successfully");
				processErrors(target);

			}
			
			onSavePostProcess(target);

		}
		catch (EntityExistsException e) {
			this.error("A Study Component with the same name already exists for this study.");
			processErrors(target);
		}
		catch (UnAuthorizedOperation e) {
			this.error("You are not authorised to manage study components for the given study " + study.getName());
			processErrors(target);
		}
		catch (ArkSystemException e) {
			this.error("A System error occured, we will have someone contact you.");
			processErrors(target);
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see au.org.theark.core.web.form.AbstractDetailForm#processErrors(org.apache.wicket.ajax.AjaxRequestTarget)
	 */
	@Override
	protected void processErrors(AjaxRequestTarget target) {
		target.add(feedBackPanel);

	}

	protected void onDeleteConfirmed(AjaxRequestTarget target, String selection) {
		try {
			studyService.delete(containerForm.getModelObject().getStudyComponent());
			StudyCompVo studyCompVo = new StudyCompVo();
			containerForm.setModelObject(studyCompVo);
			containerForm.info("The Study Component was deleted successfully.");
			editCancelProcess(target);
		}
		catch (UnAuthorizedOperation unAuthorisedexception) {
			containerForm.error("You are not authorised to delete this study component.");
			processErrors(target);
		}
		catch (EntityCannotBeRemoved cannotRemoveException) {
			containerForm.error("Cannot Delete this Study Component. This component is associated with a Subject");
			processErrors(target);
		}
		catch (ArkSystemException e) {
			containerForm.error("A System Error has occured please contact support.");
			processErrors(target);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see au.org.theark.core.web.form.AbstractDetailForm#isNew()
	 */
	@Override
	protected boolean isNew() {
		if (containerForm.getModelObject().getStudyComponent().getId() == null) {
			return true;
		}
		else {
			return false;
		}

	}

}
