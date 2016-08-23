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
package au.org.theark.core.web.component.customfieldcategory.form;

import java.util.Collection;
import java.util.List;

import org.apache.shiro.SecurityUtils;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
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
import au.org.theark.core.model.study.entity.ArkFunction;
import au.org.theark.core.model.study.entity.ArkModule;
import au.org.theark.core.model.study.entity.CustomFieldCategory;
import au.org.theark.core.model.study.entity.CustomFieldType;
import au.org.theark.core.model.study.entity.Study;
import au.org.theark.core.service.IArkCommonService;
import au.org.theark.core.vo.ArkCrudContainerVO;
import au.org.theark.core.vo.CustomFieldCategoryVO;
import au.org.theark.core.web.component.customfieldcategory.Constants;
import au.org.theark.core.web.component.customfieldcategory.DetailPanel;
import au.org.theark.core.web.form.AbstractSearchForm;

/**
 * @author elam
 * 
 */
@SuppressWarnings({ "serial", "unchecked" })
public class SearchForm extends AbstractSearchForm<CustomFieldCategoryVO> {

	@SpringBean(name = au.org.theark.core.Constants.ARK_COMMON_SERVICE)
	private IArkCommonService					iArkCommonService;

	private CompoundPropertyModel<CustomFieldCategoryVO>	cpModel;
	private ArkCrudContainerVO 					arkCrudContainerVO;
	private TextField<String>					categoryIdTxtFld;
	private TextField<String>					categoryNameTxtFld;
	private TextArea<String>					categoryDescriptionTxtAreaFld;
	private DropDownChoice<CustomFieldType>		customFieldTypeDdc;
	private DropDownChoice<CustomFieldCategory>	parentCategoryDdc;
	private TextField<Long>					categoryOrderNoTxtFld;
	private Collection<CustomFieldCategory> customFieldCategoryCollection;
	private WebMarkupContainer categoryPanel;
	
	
	/**
	 * @param id
	 */
	public SearchForm(String id, CompoundPropertyModel<CustomFieldCategoryVO> cpModel, FeedbackPanel feedBackPanel, ArkCrudContainerVO arkCrudContainerVO){

		super(id, cpModel, feedBackPanel, arkCrudContainerVO);
		this.cpModel = cpModel;
		this.feedbackPanel = feedBackPanel;
		this.arkCrudContainerVO = arkCrudContainerVO;
		initialiseFieldForm();
		Long sessionStudyId = (Long) SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.core.Constants.STUDY_CONTEXT_ID);
		disableSearchForm(sessionStudyId, "There is no study in context. Please select a study");
	}

	

	public void initialiseFieldForm() {
		categoryIdTxtFld = new TextField<String>(Constants.FIELDVO_CUSTOMFIELDCATEGORY_ID);
		categoryNameTxtFld = new TextField<String>(Constants.FIELDVO_CUSTOMFIELDCATEGORY_NAME);
		categoryDescriptionTxtAreaFld = new TextArea<String>(Constants.FIELDVO_CUSTOMFIELDCATEGORY_DESCRIPTION);
		initCustomFieldTypeDdc();
		initParentCategoryDdc();
		categoryOrderNoTxtFld=new TextField<Long>(Constants.FIELDVO_CUSTOMFIELDCATEGORY_ORDERNUMBER);
		addFieldComponents();
		addValidators();
	}

	private void addFieldComponents() {
		add(categoryIdTxtFld);
		add(categoryNameTxtFld);
		add(categoryDescriptionTxtAreaFld);
		add(customFieldTypeDdc);
		categoryPanel.add(parentCategoryDdc);
		add(categoryPanel);
		add(categoryOrderNoTxtFld);
	}
	private void addValidators(){
		categoryNameTxtFld.add(new PatternValidator("[a-zA-Z0-9_-]+"));
		categoryDescriptionTxtAreaFld.add(new PatternValidator("[a-zA-Z0-9_-]+"));
	}
	/**
	 * initialize Custom Filed Types.
	 */
	private void initCustomFieldTypeDdc() {
		Long sessionModuleId = (Long) SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.core.Constants.ARK_MODULE_KEY);
		ArkModule arkModule=iArkCommonService.getArkModuleById(sessionModuleId);
		java.util.Collection<CustomFieldType> customFieldTypeCollection = iArkCommonService.getCustomFieldTypes(arkModule);;
		ChoiceRenderer customfieldTypeRenderer = new ChoiceRenderer(Constants.CUSTOM_FIELD_TYPE_NAME, Constants.CUSTOM_FIELD_TYPE_ID);
		customFieldTypeDdc = new DropDownChoice<CustomFieldType>(Constants.FIELDVO_CUSTOMFIELDCATEGORY_CUSTOM_FIELD_TYPE, (List) customFieldTypeCollection, customfieldTypeRenderer);
		customFieldTypeDdc.add(new AjaxFormComponentUpdatingBehavior("onchange") {
		private static final long serialVersionUID = 1L;
				@Override
			    protected void onUpdate(AjaxRequestTarget target) {
					cpModel.getObject().getCustomFieldCategory().setCustomFieldType(customFieldTypeDdc.getModelObject());
					Collection<CustomFieldCategory> customFieldCategoryCollection=getParentCategoryCollectionFromModel();
					categoryPanel.remove(parentCategoryDdc);
					ChoiceRenderer customfieldCategoryRenderer = new ChoiceRenderer(Constants.CUSTOMFIELDCATEGORY_NAME, Constants.CUSTOMFIELDCATEGORY_ID);
					parentCategoryDdc = new DropDownChoice<CustomFieldCategory>(Constants.FIELDVO_CUSTOMFIELDCATEGORY_PARENTCATEGORY, (List) customFieldCategoryCollection, customfieldCategoryRenderer);
					parentCategoryDdc.setOutputMarkupId(true);
					categoryPanel.add(parentCategoryDdc);
			    	target.add(parentCategoryDdc);
			    	target.add(categoryPanel);
			    }
			});
		
	}
	/**
	 * Initialize Custom Field Categories.
	 */
	private void initParentCategoryDdc() {
		categoryPanel = new WebMarkupContainer("categoryPanel");
		categoryPanel.setOutputMarkupId(true);
		Collection<CustomFieldCategory> customFieldCategoryCollection=getParentCategoryCollectionFromModel();
		ChoiceRenderer customfieldCategoryRenderer = new ChoiceRenderer(Constants.CUSTOMFIELDCATEGORY_NAME, Constants.CUSTOMFIELDCATEGORY_ID);
		parentCategoryDdc = new DropDownChoice<CustomFieldCategory>(Constants.FIELDVO_CUSTOMFIELDCATEGORY_PARENTCATEGORY, (List) customFieldCategoryCollection, customfieldCategoryRenderer);
		parentCategoryDdc.setOutputMarkupId(true);
	}
	/**
	 * Get custom field category collection from model.
	 * @return
	 */
	private Collection<CustomFieldCategory> getParentCategoryCollectionFromModel(){
		CustomFieldCategory customFieldCategory=cpModel.getObject().getCustomFieldCategory(); 
		Study study=customFieldCategory.getStudy();
		ArkFunction arkFunction=customFieldCategory.getArkFunction();
		CustomFieldType customFieldType=customFieldCategory.getCustomFieldType();
		try {
			customFieldCategoryCollection = iArkCommonService.getParentCategoryListByCustomFieldType(study, arkFunction, customFieldType);
		} catch (ArkSystemException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return customFieldCategoryCollection;
	}

	@Override
	protected void onSearch(AjaxRequestTarget target) {
		target.add(feedbackPanel);
		final Long sessionStudyId = (Long) SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.core.Constants.STUDY_CONTEXT_ID);
		// Get a list of all Fields for the Study in context
		Study study = iArkCommonService.getStudy(sessionStudyId);
		getModelObject().getCustomFieldCategory().setStudy(study);

		long count = iArkCommonService.getCustomFieldCategoryCount(getModelObject().getCustomFieldCategory());
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
		CustomFieldCategory cf = getModelObject().getCustomFieldCategory();
		CompoundPropertyModel<CustomFieldCategoryVO> newModel = new CompoundPropertyModel<CustomFieldCategoryVO>(new CustomFieldCategoryVO());
		CustomFieldCategory newCF = newModel.getObject().getCustomFieldCategory();
		// Copy all the customField attributes across from the SearchForm
		newCF.setStudy(cf.getStudy());
		newCF.setArkFunction(cf.getArkFunction());
		newCF.setName(cf.getName());
		//newCF.setFieldType(cf.getFieldType());
		newCF.setDescription(cf.getDescription());
		/* 
		 * NB: Do NOT copy unitType across because it is a Textfield on the SearchForm.
		 * If you copy this through, then DetailForm will have transient error during onSave(..).
		 * Also, if the user chooses fieldType==DATE, this and unitType is not a valid combination
		 * (but unitTypeDdc will be disabled, so the user can't make it null for it to be valid).
		 */
		//newCF.setMinValue(cf.getMinValue());
		//newCF.setMaxValue(cf.getMaxValue());
		newCF.setOrderNumber(cf.getOrderNumber());
		newCF.setParentCategory(cf.getParentCategory());
		//newModel.getObject().setUseCustomFieldCategoryDisplay(getModelObject().isUseCustomFieldCategoryDisplay());
		
		DetailPanel detailPanel = new DetailPanel("detailPanel", feedbackPanel, newModel, arkCrudContainerVO);
		arkCrudContainerVO.getDetailPanelContainer().addOrReplace(detailPanel);
		
		// Reset model's CF object (do NOT replace the CustomFieldVO in the model)
		cf = new CustomFieldCategory();
		cf.setStudy(newCF.getStudy());
		cf.setArkFunction(newCF.getArkFunction());
		getModelObject().setCustomFieldCategory(cf);
		
		preProcessDetailPanel(target);
	}

}
