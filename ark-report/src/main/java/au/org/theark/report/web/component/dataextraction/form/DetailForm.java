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
package au.org.theark.report.web.component.dataextraction.form;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.validation.validator.StringValidator;


import au.org.theark.core.Constants;
import au.org.theark.core.model.report.entity.Search;
import au.org.theark.core.vo.ArkCrudContainerVO;
import au.org.theark.core.vo.SearchVO;
import au.org.theark.core.web.behavior.ArkDefaultFormFocusBehavior;
import au.org.theark.core.web.form.AbstractDetailForm;

/**
 * @author nivedann
 * 
 */
public class DetailForm extends AbstractDetailForm<SearchVO> {


	private static final long	serialVersionUID	= -8267651986631341353L;

	@SuppressWarnings("unchecked")
//	@SpringBean(name = au.org.theark.core.Constants.ARK_COMMON_SERVICE)
//	private IArkCommonService	iArkCommonService;

	private TextField<String>	searchIdTxtFld;
	private TextField<String>	searchNameTxtFld;
	private FeedbackPanel		feedBackPanel;

	/**
	 * 
	 * @param id
	 * @param feedBackPanel
	 * @param arkCrudContainerVO
	 * @param containerForm
	 */
	public DetailForm(String id, FeedbackPanel feedBackPanel, ArkCrudContainerVO arkCrudContainerVO, ContainerForm containerForm) {
		//super()
		super(id, feedBackPanel, containerForm, arkCrudContainerVO);
		this.feedBackPanel = feedBackPanel;
	}

	public void onBeforeRender() {
		super.onBeforeRender();
		SearchVO searchVO = containerForm.getModelObject();
		Search search = searchVO.getSearch();
//  studyComponent = containerForm.getModelObject();
		//StudyComp component = studyComponent.getStudyComponent();
		//;
		if (search != null && search.getId() != null ) {
			deleteButton.setEnabled(false);
		}
		// If the given component is attached to a file/consents then disable the delete button

	}

	public void initialiseDetailForm() {

		searchIdTxtFld = new TextField<String>(Constants.SEARCH_ID);
		searchIdTxtFld.setEnabled(false);
		searchNameTxtFld = new TextField<String>(Constants.SEARCH_NAME);
		searchNameTxtFld.add(new ArkDefaultFormFocusBehavior());
	/*	componentIdTxtFld = new TextField<String>(Constants.STUDY_COMPONENT_ID);
		componentIdTxtFld.setEnabled(false);
		componentNameTxtFld = new TextField<String>(Constants.STUDY_COMPONENT_NAME);
		componentNameTxtFld.add(new ArkDefaultFormFocusBehavior());
		componentDescription = new TextArea<String>(Constants.STUDY_COMPONENT_DESCRIPTION);
		keywordTxtArea = new TextArea<String>(Constants.STUDY_COMPONENT_KEYWORD);
		addDetailFormComponents();
		attachValidators();
		
		*/
	}

	public void addDetailFormComponents() {
		arkCrudContainerVO.getDetailPanelFormContainer().add(searchIdTxtFld);
		arkCrudContainerVO.getDetailPanelFormContainer().add(searchNameTxtFld);
	//	arkCrudContainerVO.getDetailPanelFormContainer().add(componentDescription);
	//	arkCrudContainerVO.getDetailPanelFormContainer().add(keywordTxtArea);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see au.org.theark.core.web.form.AbstractDetailForm#attachValidators()
	 */
	@Override
	protected void attachValidators() {
		searchNameTxtFld.setRequired(true).setLabel(new StringResourceModel("error.search.name.required", searchNameTxtFld, new Model<String>("Search Name")));
		searchNameTxtFld.add(StringValidator.lengthBetween(1, 255)).setLabel(
				new StringResourceModel("error.search.name.length", searchNameTxtFld, new Model<String>("Search Name")));
		//componentDescription.add(StringValidator.lengthBetween(5, 500)).setLabel(new StringResourceModel("error.study.component.description.length", this, new Model<String>("Description")));
		//keywordTxtArea.add(StringValidator.lengthBetween(1, 255)).setLabel(new StringResourceModel("error.study.component.keywords.length", this, new Model<String>("Keywords")));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see au.org.theark.core.web.form.AbstractDetailForm#onCancel(org.apache.wicket.ajax.AjaxRequestTarget)
	 */
	@Override
	protected void onCancel(AjaxRequestTarget target) {

		SearchVO searchVO = new SearchVO();
		containerForm.setModelObject(searchVO);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see au.org.theark.core.web.form.AbstractDetailForm#onSave(org.apache.wicket.markup.html.form.Form, org.apache.wicket.ajax.AjaxRequestTarget)
	 */
	@Override
	protected void onSave(Form<SearchVO> containerForm, AjaxRequestTarget target) {

		target.add(arkCrudContainerVO.getDetailPanelContainer());
		/*
		try {

			Long studyId = (Long) SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.core.Constants.STUDY_CONTEXT_ID);
			study = iArkCommonService.getStudy(studyId);
			containerForm.getModelObject().getStudyComponent().setStudy(study);

			if (containerForm.getModelObject().getStudyComponent().getId() == null) {

				iStudyService.create(containerForm.getModelObject().getStudyComponent());
				this.info("Study Component " + containerForm.getModelObject().getStudyComponent().getName() + " was created successfully");
				processErrors(target);

			}
			else {

				iStudyService.update(containerForm.getModelObject().getStudyComponent());
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
		}*/

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
		/*
		try {
	//		iStudyService.delete(containerForm.getModelObject().getStudyComponent());
			SearchVO studyCompVo = new SearchVO();
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
		}*/
		
	}

	@Override
	protected boolean isNew() {
		// TODO Auto-generated method stub
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see au.org.theark.core.web.form.AbstractDetailForm#isNew()
	 *
	@Override
	protected boolean isNew() {
		if (containerForm.getModelObject().getId() == null) {
			return true;
		}
		else {
			return false;
		}

	}*/

}
