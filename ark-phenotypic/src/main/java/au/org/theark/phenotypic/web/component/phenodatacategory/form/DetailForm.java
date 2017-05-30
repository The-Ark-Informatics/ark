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
package au.org.theark.phenotypic.web.component.phenodatacategory.form;

import java.util.Collection;
import java.util.List;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.validation.validator.PatternValidator;
import org.apache.wicket.validation.validator.StringValidator;

import au.org.theark.core.exception.ArkRunTimeException;
import au.org.theark.core.exception.ArkRunTimeUniqueException;
import au.org.theark.core.exception.ArkSystemException;
import au.org.theark.core.exception.ArkUniqueException;
import au.org.theark.core.exception.EntityCannotBeRemoved;
import au.org.theark.core.exception.EntityNotFoundException;
import au.org.theark.core.model.pheno.entity.PhenoDataSetCategory;
import au.org.theark.core.model.study.entity.ArkFunction;
import au.org.theark.core.model.study.entity.Study;
import au.org.theark.core.service.IArkCommonService;
import au.org.theark.core.vo.ArkCrudContainerVO;
import au.org.theark.core.vo.PhenoDataSetCategoryVO;
import au.org.theark.core.web.behavior.ArkDefaultFormFocusBehavior;
import au.org.theark.core.web.component.audit.button.HistoryButtonPanel;
import au.org.theark.core.web.form.AbstractDetailForm;
import au.org.theark.phenotypic.service.IPhenotypicService;
import au.org.theark.phenotypic.web.component.phenodatacategory.Constants;

/**
 * CustomField's DetailForm
 * 
 * Follows a slightly different abstraction model again trying to improve on top of the existing CRUD abstraction classes. Of note: - Receive a
 * (compound property) model instead of the container form, which should make it lighter than passing the container form in. - We do not have to pass
 * in the container form's model/VO, but instead this can use an independent model for an independent VO.
 * 
 * @auther elam
 * 
 *         Does not use the containerForm under the revised abstraction.
 */
@SuppressWarnings( { "serial", "unchecked", "unused" })
public class DetailForm extends AbstractDetailForm<PhenoDataSetCategoryVO> {
	@SpringBean(name = au.org.theark.core.Constants.ARK_COMMON_SERVICE)
	private IArkCommonService<Void>				iArkCommonService;

	private int											mode;
	private TextField<String>						categoryIdTxtFld;
	private TextField<String>						categoryNameTxtFld;
	private TextArea<String>						categoryDescriptionTxtAreaFld;
	protected WebMarkupContainer					phenoDatasetCategoryDetailWMC;
	private Collection<PhenoDataSetCategory> 		phenoDatasetCategoryCollection;
	private HistoryButtonPanel 						historyButtonPanel;
	
	@SpringBean(name = Constants.PHENOTYPIC_SERVICE)
	private IPhenotypicService			iPhenotypicService;
	
	

	/**
	 * Constructor
	 * 
	 * @param id
	 * @param cpModel
	 * @param feedBackPanel
	 * @param arkCrudContainerVO
	 */
	public DetailForm(String id, CompoundPropertyModel<PhenoDataSetCategoryVO> cpModel, FeedbackPanel feedBackPanel, ArkCrudContainerVO arkCrudContainerVO){//,boolean unitTypeDropDownOn) {
		super(id, feedBackPanel, cpModel, arkCrudContainerVO);
		refreshEntityFromBackend();
		
	}

	protected void refreshEntityFromBackend()  {
		PhenoDataSetCategory cfFromBackend = null;
		// Refresh the entity from the backend
		if (getModelObject().getPhenoDataSetCategory().getId() != null) {
			try {
				cfFromBackend = iPhenotypicService.getPhenoDataSetCategory(getModelObject().getPhenoDataSetCategory().getId());
			} catch (EntityNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			getModelObject().setPhenoDataSetCategory(cfFromBackend);
		}
	}

	
	/**
	 * Call this after the constructor is finished
	 */
	public void initialiseDetailForm() {
		categoryIdTxtFld = new TextField<String>(Constants.FIELDVO_PHENODATASETCATEGORY_ID);
		categoryNameTxtFld = new TextField<String>(Constants.FIELDVO_PHENODATASETCATEGORY_NAME);
		categoryNameTxtFld.add(new ArkDefaultFormFocusBehavior());
		categoryDescriptionTxtAreaFld = new TextArea<String>(Constants.FIELDVO_PHENODATASETCATEGORY_DESCRIPTION);
		deleteButton.setEnabled(false);
		
		addDetailFormComponents();
		attachValidators();

		historyButtonPanel = new HistoryButtonPanel(this, arkCrudContainerVO.getEditButtonContainer(), arkCrudContainerVO.getDetailPanelFormContainer(),feedBackPanel);
	}
	
	/**
	 * Get custom field category collection from model.
	 * @return
	 */
	private Collection<PhenoDataSetCategory> getAvailableCategoryCollectionFromModel(){
		PhenoDataSetCategory phenoDataSetCategory=cpModel.getObject().getPhenoDataSetCategory(); 
		Study study=phenoDataSetCategory.getStudy();
		ArkFunction arkFunction=phenoDataSetCategory.getArkFunction();
		
		try {
			if(isNew()){
				phenoDatasetCategoryCollection = iPhenotypicService.getAvailableAllCategoryList(study, arkFunction);
			}else{
				phenoDatasetCategoryCollection= iPhenotypicService.getAvailableAllCategoryListExceptThis(study, arkFunction,phenoDataSetCategory);
			}
		} catch (ArkSystemException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return phenoDatasetCategoryCollection;
	}
	
	

	protected void attachValidators() {
		categoryNameTxtFld.setRequired(true);
		categoryNameTxtFld.add(new PatternValidator("[a-zA-Z0-9_-]+"));
		categoryNameTxtFld.add(StringValidator.maximumLength(au.org.theark.core.Constants.GENERAL_FIELD_NAME_MAX_LENGTH_50));
		categoryDescriptionTxtAreaFld.add(StringValidator.maximumLength(au.org.theark.core.Constants.GENERAL_FIELD_DESCRIPTIVE_MAX_LENGTH_255));
	}

	@Override
	protected void onSave(Form<PhenoDataSetCategoryVO> containerForm, AjaxRequestTarget target) {
		// NB: creating/updating the customFieldDisplay will be tied to the customField by the service
		if (getModelObject().getPhenoDataSetCategory().getId() == null) {
			// Save the Category
			try {
				iPhenotypicService.createPhenoDataSetCategory(getModelObject());
				this.info(new StringResourceModel("info.createSuccessMsg", this, null, new Object[] { getModelObject().getPhenoDataSetCategory().getName() }).getString());
				onSavePostProcess(target);
			}
			catch (ArkRunTimeException e) {
				//this.error(new StringResourceModel("error.nonCFMsg", this, null, new Object[] { getModelObject().getPhenoDataSetCategory().getOrderNumber()}).getString());
				e.printStackTrace();
			}
			catch (ArkRunTimeUniqueException e) {
				this.error(new StringResourceModel("error.nonUniqueCFMsg", this, null, new Object[] { getModelObject().getPhenoDataSetCategory().getName() }).getString());
				e.printStackTrace();
			}
			catch (ArkSystemException e) {
				this.error(new StringResourceModel("error.internalErrorMsg", this, null).getString());
				e.printStackTrace();
			}
			
			
			processErrors(target);
		}
		else {
			// Update the Category
			try {
				iPhenotypicService.updatePhenoDataSetCategory(getModelObject());
				this.info(new StringResourceModel("info.updateSuccessMsg", this, null, new Object[] { getModelObject().getPhenoDataSetCategory().getName() }).getString());
				onSavePostProcess(target);
			}
			catch (ArkSystemException e) {
				this.error(new StringResourceModel("error.internalErrorMsg", this, null).getString());
				e.printStackTrace();
			}
			catch (ArkUniqueException e) {
				this.error(new StringResourceModel("error.nonUniqueCFMsg", this, null, new Object[] { getModelObject().getPhenoDataSetCategory().getName() }).getString());
				e.printStackTrace();
			}
			processErrors(target);
		}
	}

	protected void onCancel(AjaxRequestTarget target) {

	}

	@Override
	protected void processErrors(AjaxRequestTarget target) {
		target.add(feedBackPanel);
	}

	protected void onDeleteConfirmed(AjaxRequestTarget target, String selection) {
		try {
			iPhenotypicService.deletePhenoDataSetCategory(getModelObject());
			this.info("Field " + getModelObject().getPhenoDataSetCategory().getName() + " was deleted successfully");
		}
		catch (ArkSystemException e) {
			this.error(e.getMessage());
		}
		catch (EntityCannotBeRemoved e) {
			this.error(e.getMessage());
		}

		// Display delete confirmation message
		target.add(feedBackPanel);
		// TODO Implement Exceptions in PhentoypicService
		// } catch (UnAuthorizedOperation e) { this.error("You are not authorised to manage study components for the given study " +
		// study.getName()); processFeedback(target); } catch (ArkSystemException e) {
		// this.error("A System error occured, we will have someone contact you."); processFeedback(target); }

		// Move focus back to Search form
		editCancelProcess(target); // this ends up calling onCancel(target)
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see au.org.theark.core.web.form.AbstractDetailForm#isNew()
	 */
	@Override
	protected boolean isNew() {
		if (getModelObject().getPhenoDataSetCategory().getId() == null) {
			return true;
		}
		else {
			return false;
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see au.org.theark.core.web.form.AbstractDetailForm#addDetailFormComponents()
	 */
	@Override
	protected void addDetailFormComponents() {
		phenoDatasetCategoryDetailWMC = new WebMarkupContainer("phenoDatasetCategoryDetailWMC");
		phenoDatasetCategoryDetailWMC.setOutputMarkupPlaceholderTag(true);
		phenoDatasetCategoryDetailWMC.add(categoryIdTxtFld.setEnabled(false)); // Disable ID field editing
		phenoDatasetCategoryDetailWMC.add(categoryNameTxtFld);
		phenoDatasetCategoryDetailWMC.add(categoryDescriptionTxtAreaFld);
		//parentPanel.add(parentCategoryDdc);
		//phenoDatasetCategoryDetailWMC.add(parentPanel);
		//phenoDatasetCategoryDetailWMC.add(categoryOrderNoTxtFld);

		// TODO: This 'addOrReplace' (instead of just 'add') is a temporary workaround due to the
		// detailPanelFormContainer being initialised only once at the top-level container panel.
		arkCrudContainerVO.getDetailPanelFormContainer().addOrReplace(phenoDatasetCategoryDetailWMC);
		//arkCrudContainerVO.getDetailPanelFormContainer().addOrReplace(customFieldDisplayDetailWMC);
		add(arkCrudContainerVO.getDetailPanelFormContainer());

	}

	

}
