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
package au.org.theark.phenotypic.web.component.phenodatadictionary.form;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.shiro.SecurityUtils;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.extensions.ajax.markup.html.autocomplete.AutoCompleteTextField;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import au.org.theark.core.exception.ArkSystemException;
import au.org.theark.core.model.pheno.entity.PhenoDataSetCategory;
import au.org.theark.core.model.pheno.entity.PhenoDataSetField;
import au.org.theark.core.model.study.entity.ArkFunction;
import au.org.theark.core.model.study.entity.ArkModule;
import au.org.theark.core.model.study.entity.FieldType;
import au.org.theark.core.model.study.entity.Study;
import au.org.theark.core.model.study.entity.UnitType;
import au.org.theark.core.service.IArkCommonService;
import au.org.theark.core.vo.ArkCrudContainerVO;
import au.org.theark.core.vo.PhenoDataSetFieldVO;
import au.org.theark.core.web.form.AbstractSearchForm;
import au.org.theark.phenotypic.service.IPhenotypicService;
import au.org.theark.phenotypic.web.component.phenodatadictionary.Constants;
import au.org.theark.phenotypic.web.component.phenodatadictionary.DetailPanel;



/**
 * @author elam
 * 
 */
@SuppressWarnings({ "serial", "unchecked" })
public class SearchForm extends AbstractSearchForm<PhenoDataSetFieldVO> {

	@SpringBean(name = au.org.theark.core.Constants.ARK_COMMON_SERVICE)
	private IArkCommonService iArkCommonService;

	private CompoundPropertyModel<PhenoDataSetFieldVO> cpModel;
	private ArkCrudContainerVO arkCrudContainerVO;
	private TextField<String> fieldIdTxtFld;
	private TextField<String> fieldNameTxtFld;
	private DropDownChoice<FieldType> fieldTypeDdc;
	
	private DropDownChoice<PhenoDataSetCategory>	phenoDataSetCategoryDdc;
	private TextField<Long>					phenoDataSetCategoryOrderNoTxtFld;
	private TextArea<String> fieldDescriptionTxtAreaFld;
	private TextField<String> fieldUnitsTxtFld;
	private TextField<String> fieldUnitsInTextTxtFld;
	private TextField<String> fieldMinValueTxtFld;
	private TextField<String> fieldMaxValueTxtFld;

	private WebMarkupContainer panelCustomUnitTypeDropDown;
	private WebMarkupContainer panelCustomUnitTypeText;
	private Collection<PhenoDataSetCategory> phenoDataSetFieldCategoryCollection;
	private WebMarkupContainer categoryPanel;
	//private WebMarkupContainer orderNumberPanel;
	private ArkModule arkModule;
	
	@SpringBean(name = Constants.PHENOTYPIC_SERVICE)
	private IPhenotypicService			iPhenotypicService;
	
	

	/**
	 * @param id
	 */
	public SearchForm(String id, CompoundPropertyModel<PhenoDataSetFieldVO> cpModel, FeedbackPanel feedBackPanel, ArkCrudContainerVO arkCrudContainerVO) {
		super(id, cpModel, feedBackPanel, arkCrudContainerVO);
		//this.unitTypeDropDownOn = unitTypeDropDownOn;
		this.cpModel = cpModel;
		this.feedbackPanel = feedBackPanel;
		this.arkCrudContainerVO = arkCrudContainerVO;
		Long sessionModuleId = (Long) SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.core.Constants.ARK_MODULE_KEY);
		arkModule=iArkCommonService.getArkModuleById(sessionModuleId);
		//this.subjectCustomField = subjectCustomField;
		initialiseFieldForm();
		Long sessionStudyId = (Long) SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.core.Constants.STUDY_CONTEXT_ID);
		disableSearchForm(sessionStudyId, "There is no study in context. Please select a study");
	}

	private void initFieldTypeDdc() {
		java.util.Collection<FieldType> fieldTypeCollection = iArkCommonService.getFieldTypes();
		ChoiceRenderer fieldTypeRenderer = new ChoiceRenderer(Constants.FIELDTYPE_NAME, Constants.FIELDTYPE_ID);
		fieldTypeDdc = new DropDownChoice<FieldType>(Constants.FIELDVO_PHENODATASET_FIELD_TYPE, (List) fieldTypeCollection, fieldTypeRenderer);
	}

	public void initialiseFieldForm() {
		fieldIdTxtFld = new TextField<String>(Constants.FIELDVO_PHENODATASET_ID);
		fieldNameTxtFld = new TextField<String>(Constants.FIELDVO_PHENODATASET_NAME);
		fieldDescriptionTxtAreaFld = new TextArea<String>(Constants.FIELDVO_PHENODATASET_DESCRIPTION);
		panelCustomUnitTypeDropDown = new WebMarkupContainer("panelCustomUnitTypeDropDown");
		panelCustomUnitTypeDropDown.setOutputMarkupId(true);
		panelCustomUnitTypeText = new WebMarkupContainer("panelCustomUnitTypeText");
		panelCustomUnitTypeText.setOutputMarkupId(true);
		fieldUnitsTxtFld = new AutoCompleteTextField<String>(Constants.FIELDVO_PHENODATASET_UNIT_TYPE+ ".name") {
			@Override
			protected Iterator getChoices(String input) {
				UnitType unitTypeCriteria = new UnitType();
				unitTypeCriteria.setName(input);
				unitTypeCriteria.setArkFunction(cpModel.getObject().getPhenoDataSetField().getArkFunction());
				return iArkCommonService.getUnitTypeNames(unitTypeCriteria, 10).iterator();
			}
		};

		fieldUnitsInTextTxtFld = new TextField<String>(Constants.FIELDVO_PHENODATASET_UNIT_TYPE_TXT);
		fieldMinValueTxtFld = new TextField<String>(Constants.FIELDVO_PHENODATASET_MIN_VALUE);
		fieldMaxValueTxtFld = new TextField<String>(Constants.FIELDVO_PHENODATASET_MAX_VALUE);
		//initCustomFieldTypeDdc();
		
		//customfield category order Number.
		//phenoDataSetCategoryOrderNoTxtFld = new TextField<Long>(Constants.FIELDVO_PHENODATASET_CUSTOEMFIELDCATEGORY_ORDERNUMBER);
		//phenoDataSetCategoryOrderNoTxtFld.setOutputMarkupId(true);
		//phenoDataSetCategoryOrderNoTxtFld.setEnabled(false);
		
		//initCustomeFieldCategoryDdc();
		initFieldTypeDdc();
		addFieldComponents();
	}

	private void addFieldComponents() {
		add(fieldIdTxtFld);
		//add(customFieldTypeDdc);
		//categoryPanel.add(phenoDataSetCategoryDdc);
		//add(categoryPanel);
		//orderNumberPanel.add(phenoDataSetCategoryOrderNoTxtFld);
		//add(orderNumberPanel);
		add(fieldNameTxtFld);
		add(fieldTypeDdc);
		add(fieldDescriptionTxtAreaFld);
		if (arkModule.getName().equals(au.org.theark.core.Constants.ARK_MODULE_STUDY)) {
			panelCustomUnitTypeDropDown.setVisible(true);
			panelCustomUnitTypeText.setVisible(false);
		} else {
			panelCustomUnitTypeDropDown.setVisible(false);
			panelCustomUnitTypeText.setVisible(true);
		}
		panelCustomUnitTypeDropDown.add(fieldUnitsTxtFld);
		add(panelCustomUnitTypeDropDown);
		panelCustomUnitTypeText.add(fieldUnitsInTextTxtFld);
		add(panelCustomUnitTypeText);
		add(fieldMinValueTxtFld);
		add(fieldMaxValueTxtFld);
	}
	
	
	/**
	 * 
	 * @param target
	 */
	private void customFieldCategoryChangeEvent(AjaxRequestTarget target) {
		//Here we have to pick all the pheno field category related custom fileds. 
		//cpModel.getObject().getPhenoDataSetField().setPhe(phenoDataSetCategoryDdc.getModelObject());
		//orderNumberPanel.remove(phenoDataSetCategoryOrderNoTxtFld);
		//phenoDataSetCategoryOrderNoTxtFld = new TextField<Long>(Constants.FIELDVO_PHENODATASET_CUSTOEMFIELDCATEGORY_ORDERNUMBER);
		//phenoDataSetCategoryOrderNoTxtFld.setOutputMarkupId(true);
		//phenoDataSetCategoryOrderNoTxtFld.setEnabled(false);
		//orderNumberPanel.add(phenoDataSetCategoryOrderNoTxtFld);
		//target.add(phenoDataSetCategoryOrderNoTxtFld);
		
	}
	
	
	
	/**
	 * Initialize Custom Field Categories.
	 */
	/*private void initCustomeFieldCategoryDdc() {
		Long sessionModuleId = (Long) SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.core.Constants.ARK_MODULE_KEY);
		arkModule=iArkCommonService.getArkModuleById(sessionModuleId);
		categoryPanel = new WebMarkupContainer("categoryPanel");
		categoryPanel.setOutputMarkupId(true);
		//orderNumberPanel = new WebMarkupContainer("orderNumberPanel");
		//orderNumberPanel.setOutputMarkupId(true);
		java.util.Collection<PhenoDataSetCategory> phenoDataSetFieldCategoryCollection=getCategoriesListInCustomFieldsByCustomFieldType();
		ChoiceRenderer customfieldCategoryRenderer = new ChoiceRenderer(Constants.PHENODATASETCATEGORY_NAME, Constants.PHENODATASETCATEGORY_ID);
		phenoDataSetCategoryDdc = new DropDownChoice<PhenoDataSetCategory>(Constants.FIELDVO_PHENODATASET_CUSTOEMFIELDCATEGORY, (List) phenoDataSetFieldCategoryCollection, customfieldCategoryRenderer);
		phenoDataSetCategoryDdc.setOutputMarkupId(true);
	}*/
	/**
	 * Get custom field category collection from model.
	 * @return
	 */
	private Collection<PhenoDataSetCategory> getCategoriesListInCustomFieldsByCustomFieldType(){
		PhenoDataSetField phenoDataSetField=cpModel.getObject().getPhenoDataSetField();
		Study study=phenoDataSetField.getStudy();
		/*Here I am changing the customfield type and the ark fuction to get all the 
		categories to display later we have to replace all the things with 
		PhenoDataDictionary*/
		ArkFunction arkFunction=iArkCommonService.getArkFunctionByName(au.org.theark.core.Constants.FUNCTION_KEY_VALUE_SUBJECT_CUSTOM_FIELD);
		/*try {
			//phenoDataSetFieldCategoryCollection=iPhenotypicService.getPhenoParentCategoryList(study, arkFunction);
			//phenoDataSetFieldCategoryCollection =  iArkCommonService.getCategoriesListInCustomFieldsByCustomFieldType(study, arkFunction, phenoDataSetFieldType);
			phenoDataSetFieldCategoryCollection=sortLst(remeoveDuplicates((List<PhenoDataSetCategory>)phenoDataSetFieldCategoryCollection));
			
		} catch (ArkSystemException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		return phenoDataSetFieldCategoryCollection;
	}

	@Override
	protected void onSearch(AjaxRequestTarget target) {
		target.add(feedbackPanel);
		final Long sessionStudyId = (Long) SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.core.Constants.STUDY_CONTEXT_ID);
		// Get a list of all Fields for the Study in context
		Study study = iArkCommonService.getStudy(sessionStudyId);
		getModelObject().getPhenoDataSetField().setStudy(study);

		long count = iPhenotypicService.getPhenoFieldCount(getModelObject().getPhenoDataSetField());
		if (count <= 0L) {
			this.info("No records match the specified criteria.");
			target.add(feedbackPanel);
		}
		arkCrudContainerVO.getSearchResultPanelContainer().setVisible(true);
		target.add(arkCrudContainerVO.getSearchResultPanelContainer());
	}

	// Reset button implemented in AbstractSearchForm

	@Override
	/**
	 * 
	 * @param target
	 */
	protected void onNew(AjaxRequestTarget target) {
		target.add(feedbackPanel);
		// Instead of having to reset the criteria, we just copy the criteria
		// across
		PhenoDataSetField phenoField = getModelObject().getPhenoDataSetField();
		CompoundPropertyModel<PhenoDataSetFieldVO> newModel = new CompoundPropertyModel<PhenoDataSetFieldVO>(new PhenoDataSetFieldVO());
		PhenoDataSetField newPhenoDataSet = newModel.getObject().getPhenoDataSetField();
		// Copy all the phenoDataSetField attributes across from the SearchForm
		newPhenoDataSet.setStudy(phenoField.getStudy());
		newPhenoDataSet.setArkFunction(phenoField.getArkFunction());
		newPhenoDataSet.setName(phenoField.getName());
		newPhenoDataSet.setFieldType(phenoField.getFieldType());
		newPhenoDataSet.setDescription(phenoField.getDescription());
		/*
		 * NB: Do NOT copy unitType across because it is a Textfield on the
		 * SearchForm. If you copy this through, then DetailForm will have
		 * transient error during onSave(..). Also, if the user chooses
		 * fieldType==DATE, this and unitType is not a valid combination (but
		 * unitTypeDdc will be disabled, so the user can't make it null for it
		 * to be valid).
		 */
		newPhenoDataSet.setMinValue(phenoField.getMinValue());
		newPhenoDataSet.setMaxValue(phenoField.getMaxValue());
		//newModel.getObject().setUsePhenoDataSetFieldDisplay(getModelObject().isUsePhenoDataSetFieldDisplay());

		DetailPanel detailPanel = new DetailPanel("detailPanel", feedbackPanel, newModel, arkCrudContainerVO);
				//this.unitTypeDropDownOn, this.subjectCustomField);
		arkCrudContainerVO.getDetailPanelContainer().addOrReplace(detailPanel);

		// Reset model's CF object (do NOT replace the CustomFieldVO in the
		// model)
		phenoField = new PhenoDataSetField();
		phenoField.setStudy(newPhenoDataSet.getStudy());
		phenoField.setArkFunction(newPhenoDataSet.getArkFunction());
		getModelObject().setPhenoDataSetField(phenoField);

		preProcessDetailPanel(target);
	}
	/**
	 * Sort custom field list according to the order number.
	 * @param phenoDataSetFieldLst
	 * @return
	 */
	private  List<PhenoDataSetCategory> sortLst(List<PhenoDataSetCategory> phenoDataSetFieldLst){
		//sort by order number.
		Collections.sort(phenoDataSetFieldLst, new Comparator<PhenoDataSetCategory>(){
		    public int compare(PhenoDataSetCategory custFieldCategory1, PhenoDataSetCategory custFieldCatCategory2) {
		        return custFieldCategory1.getName().compareTo(custFieldCatCategory2.getName());
		    }
		});
				return phenoDataSetFieldLst;
	}
	/**
	 * Remove duplicates from list
	 * @param phenoDataSetFieldLst
	 * @return
	 */
	private  List<PhenoDataSetCategory> remeoveDuplicates(List<PhenoDataSetCategory> phenoDataSetFieldLst){
		Set<PhenoDataSetCategory> cusfieldCatSet=new HashSet<PhenoDataSetCategory>();
		List<PhenoDataSetCategory> cusfieldCatLst=new ArrayList<PhenoDataSetCategory>();
		cusfieldCatSet.addAll(phenoDataSetFieldLst);
		cusfieldCatLst.addAll(cusfieldCatSet);
				return cusfieldCatLst;
	}
	

}
