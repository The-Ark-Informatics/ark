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

import org.apache.shiro.SecurityUtils;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.validation.validator.PatternValidator;

import au.org.theark.core.exception.ArkSystemException;
import au.org.theark.core.model.pheno.entity.PhenoDataSetCategory;
import au.org.theark.core.model.study.entity.ArkFunction;
import au.org.theark.core.model.study.entity.CustomFieldCategory;
import au.org.theark.core.model.study.entity.CustomFieldType;
import au.org.theark.core.model.study.entity.Study;
import au.org.theark.core.service.IArkCommonService;
import au.org.theark.core.vo.ArkCrudContainerVO;
import au.org.theark.core.vo.CustomFieldCategoryVO;
import au.org.theark.core.vo.PhenoDataSetCategoryVO;
import au.org.theark.core.web.form.AbstractSearchForm;
import au.org.theark.phenotypic.service.IPhenotypicService;
import au.org.theark.phenotypic.web.component.phenodatacategory.Constants;
import au.org.theark.phenotypic.web.component.phenodatacategory.DetailPanel;

/**
 * @author elam
 * 
 */
@SuppressWarnings({ "serial", "unchecked" })
public class SearchForm extends AbstractSearchForm<PhenoDataSetCategoryVO> {

	@SpringBean(name = au.org.theark.core.Constants.ARK_COMMON_SERVICE)
	private IArkCommonService					iArkCommonService;

	private CompoundPropertyModel<PhenoDataSetCategoryVO>	cpModel;
	private ArkCrudContainerVO 						arkCrudContainerVO;
	private TextField<String>						categoryIdTxtFld;
	private TextField<String>						categoryNameTxtFld;
	private TextArea<String>						categoryDescriptionTxtAreaFld;
	private DropDownChoice<PhenoDataSetCategory>	parentCategoryDdc;
	private TextField<Long>							categoryOrderNoTxtFld;
	private Collection<PhenoDataSetCategory> phenoDataSetCategoryCollection;
	private WebMarkupContainer categoryPanel;
	@SpringBean(name = Constants.PHENOTYPIC_SERVICE)
	private IPhenotypicService			iPhenotypicService;
	
	/**
	 * @param id
	 */
	public SearchForm(String id, CompoundPropertyModel<PhenoDataSetCategoryVO> cpModel, FeedbackPanel feedBackPanel, ArkCrudContainerVO arkCrudContainerVO){

		super(id, cpModel, feedBackPanel, arkCrudContainerVO);
		this.cpModel = cpModel;
		this.feedbackPanel = feedBackPanel;
		this.arkCrudContainerVO = arkCrudContainerVO;
		initialiseFieldForm();
		Long sessionStudyId = (Long) SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.core.Constants.STUDY_CONTEXT_ID);
		disableSearchForm(sessionStudyId, "There is no study in context. Please select a study");
	}

	

	public void initialiseFieldForm() {
		categoryIdTxtFld = new TextField<String>(Constants.FIELDVO_PHENODATASETCATEGORY_ID);
		categoryNameTxtFld = new TextField<String>(Constants.FIELDVO_PHENODATASETCATEGORY_NAME);
		categoryDescriptionTxtAreaFld = new TextArea<String>(Constants.FIELDVO_PHENODATASETCATEGORY_DESCRIPTION);
		//initParentCategoryDdc();
		//categoryOrderNoTxtFld=new TextField<Long>(Constants.FIELDVO_PHENODATASETCATEGORY_ORDERNUMBER);
		addFieldComponents();
		addValidators();
	}

	private void addFieldComponents() {
		add(categoryIdTxtFld);
		add(categoryNameTxtFld);
		add(categoryDescriptionTxtAreaFld);
		//categoryPanel.add(parentCategoryDdc);
		//add(categoryPanel);
		//add(categoryOrderNoTxtFld);
	}
	private void addValidators(){
		categoryNameTxtFld.add(new PatternValidator("[a-zA-Z0-9_-]+"));
		categoryDescriptionTxtAreaFld.add(new PatternValidator("[a-zA-Z0-9_-]+"));
	}
	
	/**
	 * Initialize Custom Field Categories.
	 */
	/*private void initParentCategoryDdc() {
		categoryPanel = new WebMarkupContainer("categoryPanel");
		categoryPanel.setOutputMarkupId(true);
		Collection<PhenoDataSetCategory> phenoDatasetCollection=getParentCategoryCollectionFromModel();
		ChoiceRenderer phenoDatasetCategoryRenderer = new ChoiceRenderer(Constants.PHENODATASETCATEGORY_NAME, Constants.PHENODATASETCATEGORY_ID);
		parentCategoryDdc = new DropDownChoice<PhenoDataSetCategory>(Constants.FIELDVO_PHENODATASETCATEGORY_PARENTCATEGORY, (List) phenoDatasetCollection, phenoDatasetCategoryRenderer);
		parentCategoryDdc.setOutputMarkupId(true);
	}*/
	/**
	 * Get custom field category collection from model.
	 * @return
	 */
	/*private Collection<PhenoDataSetCategory> getParentCategoryCollectionFromModel(){
		PhenoDataSetCategory phenoDataSetCategory=cpModel.getObject().getPhenoDataSetCategory(); 
		Study study=phenoDataSetCategory.getStudy();
		ArkFunction arkFunction=phenoDataSetCategory.getArkFunction();
		try {
			//phenoDataSetCategoryCollection = iPhenotypicService.getPhenoParentCategoryList(study, arkFunction);
		} catch (ArkSystemException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return phenoDataSetCategoryCollection;
	}*/

	@Override
	protected void onSearch(AjaxRequestTarget target) {
		target.add(feedbackPanel);
		final Long sessionStudyId = (Long) SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.core.Constants.STUDY_CONTEXT_ID);
		// Get a list of all Fields for the Study in context
		Study study = iArkCommonService.getStudy(sessionStudyId);
		getModelObject().getPhenoDataSetCategory().setStudy(study);

		long count = iPhenotypicService.getPhenoDataSetCategoryCount(getModelObject().getPhenoDataSetCategory());
		if (count <= 0L) {
			this.info("No records match the specified criteria.");
			target.add(feedbackPanel);
		}

		arkCrudContainerVO.getSearchResultPanelContainer().setVisible(true);// Make the WebMarkupContainer that houses the search results visible
		target.add(arkCrudContainerVO.getSearchResultPanelContainer());
	}

	// Reset button implemented in AbstractSearchForm

	@Override
	protected void onNew(AjaxRequestTarget target) {
		target.add(feedbackPanel);
		// Instead of having to reset the criteria, we just copy the criteria across
		PhenoDataSetCategory pdscat = getModelObject().getPhenoDataSetCategory();
		CompoundPropertyModel<PhenoDataSetCategoryVO> newModel = new CompoundPropertyModel<PhenoDataSetCategoryVO>(new PhenoDataSetCategoryVO());
		PhenoDataSetCategory newModelPDSC = newModel.getObject().getPhenoDataSetCategory();
		// Copy all the customField attributes across from the SearchForm
		newModelPDSC.setStudy(pdscat.getStudy());
		newModelPDSC.setArkFunction(pdscat.getArkFunction());
		newModelPDSC.setName(pdscat.getName());
		//newCF.setFieldType(cf.getFieldType());
		newModelPDSC.setDescription(pdscat.getDescription());
		/* 
		 * NB: Do NOT copy unitType across because it is a Textfield on the SearchForm.
		 * If you copy this through, then DetailForm will have transient error during onSave(..).
		 * Also, if the user chooses fieldType==DATE, this and unitType is not a valid combination
		 * (but unitTypeDdc will be disabled, so the user can't make it null for it to be valid).
		 */
		//newCF.setMinValue(cf.getMinValue());
		//newCF.setMaxValue(cf.getMaxValue());
		//newModelPDSC.setOrderNumber(pdscat.getOrderNumber());
		//newModelPDSC.setParentCategory(pdscat.getParentCategory());
		//newModel.getObject().setUseCustomFieldCategoryDisplay(getModelObject().isUseCustomFieldCategoryDisplay());
		
		DetailPanel detailPanel = new DetailPanel("detailPanel", feedbackPanel, newModel, arkCrudContainerVO);
		arkCrudContainerVO.getDetailPanelContainer().addOrReplace(detailPanel);
		
		// Reset model's CF object (do NOT replace the CustomFieldVO in the model)
		pdscat = new PhenoDataSetCategory();
		pdscat.setStudy(newModelPDSC.getStudy());
		pdscat.setArkFunction(newModelPDSC.getArkFunction());
		getModelObject().setPhenoDataSetCategory(pdscat);
		
		preProcessDetailPanel(target);
	}

}
