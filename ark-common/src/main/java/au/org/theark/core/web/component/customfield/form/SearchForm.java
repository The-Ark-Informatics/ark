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
package au.org.theark.core.web.component.customfield.form;

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
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
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
import au.org.theark.core.model.study.entity.ArkFunction;
import au.org.theark.core.model.study.entity.ArkModule;
import au.org.theark.core.model.study.entity.CustomField;
import au.org.theark.core.model.study.entity.CustomFieldCategory;
import au.org.theark.core.model.study.entity.CustomFieldType;
import au.org.theark.core.model.study.entity.FieldType;
import au.org.theark.core.model.study.entity.Study;
import au.org.theark.core.model.study.entity.UnitType;
import au.org.theark.core.service.IArkCommonService;
import au.org.theark.core.vo.ArkCrudContainerVO;
import au.org.theark.core.vo.CustomFieldVO;
import au.org.theark.core.web.component.customfield.Constants;
import au.org.theark.core.web.component.customfield.DetailPanel;
import au.org.theark.core.web.form.AbstractSearchForm;


/**
 * @author elam
 * 
 */
@SuppressWarnings({ "serial", "unchecked" })
public class SearchForm extends AbstractSearchForm<CustomFieldVO> {

	@SpringBean(name = au.org.theark.core.Constants.ARK_COMMON_SERVICE)
	private IArkCommonService iArkCommonService;

	private CompoundPropertyModel<CustomFieldVO> cpModel;
	private ArkCrudContainerVO arkCrudContainerVO;
	private TextField<String> fieldIdTxtFld;
	private TextField<String> fieldNameTxtFld;
	private DropDownChoice<FieldType> fieldTypeDdc;
	//Add custom field categories
	private DropDownChoice<CustomFieldType>		customFieldTypeDdc;
	private DropDownChoice<CustomFieldCategory>	customeFieldCategoryDdc;
	private TextField<Long>					customeFieldCategoryOrderNoTxtFld;
	//**************************************//
	private TextArea<String> fieldDescriptionTxtAreaFld;
	private TextField<String> fieldUnitsTxtFld;
	private TextField<String> fieldUnitsInTextTxtFld;
	private TextField<String> fieldMinValueTxtFld;
	private TextField<String> fieldMaxValueTxtFld;

	private WebMarkupContainer panelCustomUnitTypeDropDown;
	private WebMarkupContainer panelCustomUnitTypeText;
	//private boolean unitTypeDropDownOn;
	//private boolean subjectCustomField;
	private Collection<CustomFieldCategory> customFieldCategoryCollection;
	private WebMarkupContainer categoryPanel;
	private WebMarkupContainer orderNumberPanel;
	private ArkModule arkModule;
	
	

	/**
	 * @param id
	 */
	public SearchForm(String id, CompoundPropertyModel<CustomFieldVO> cpModel, FeedbackPanel feedBackPanel, ArkCrudContainerVO arkCrudContainerVO) {
		super(id, cpModel, feedBackPanel, arkCrudContainerVO);
		//this.unitTypeDropDownOn = unitTypeDropDownOn;
		this.cpModel = cpModel;
		this.feedbackPanel = feedBackPanel;
		this.arkCrudContainerVO = arkCrudContainerVO;
		//this.subjectCustomField = subjectCustomField;
		initialiseFieldForm();
		Long sessionStudyId = (Long) SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.core.Constants.STUDY_CONTEXT_ID);
		disableSearchForm(sessionStudyId, "There is no study in context. Please select a study");
	}

	private void initFieldTypeDdc() {
		java.util.Collection<FieldType> fieldTypeCollection = iArkCommonService.getFieldTypes();
		ChoiceRenderer fieldTypeRenderer = new ChoiceRenderer(Constants.FIELDTYPE_NAME, Constants.FIELDTYPE_ID);
		fieldTypeDdc = new DropDownChoice<FieldType>(Constants.FIELDVO_CUSTOMFIELD_FIELD_TYPE, (List) fieldTypeCollection, fieldTypeRenderer);
	}

	public void initialiseFieldForm() {
		fieldIdTxtFld = new TextField<String>(Constants.FIELDVO_CUSTOMFIELD_ID);
		fieldNameTxtFld = new TextField<String>(Constants.FIELDVO_CUSTOMFIELD_NAME);
		fieldDescriptionTxtAreaFld = new TextArea<String>(Constants.FIELDVO_CUSTOMFIELD_DESCRIPTION);
		panelCustomUnitTypeDropDown = new WebMarkupContainer("panelCustomUnitTypeDropDown");
		panelCustomUnitTypeDropDown.setOutputMarkupId(true);
		panelCustomUnitTypeText = new WebMarkupContainer("panelCustomUnitTypeText");
		panelCustomUnitTypeText.setOutputMarkupId(true);
		fieldUnitsTxtFld = new AutoCompleteTextField<String>(Constants.FIELDVO_CUSTOMFIELD_UNIT_TYPE + ".name") {
			@Override
			protected Iterator getChoices(String input) {
				UnitType unitTypeCriteria = new UnitType();
				unitTypeCriteria.setName(input);
				unitTypeCriteria.setArkFunction(cpModel.getObject().getCustomField().getArkFunction());
				return iArkCommonService.getUnitTypeNames(unitTypeCriteria, 10).iterator();
			}
		};

		fieldUnitsInTextTxtFld = new TextField<String>(Constants.FIELDVO_CUSTOMFIELD_UNIT_TYPE_TXT);
		fieldMinValueTxtFld = new TextField<String>(Constants.FIELDVO_CUSTOMFIELD_MIN_VALUE);
		fieldMaxValueTxtFld = new TextField<String>(Constants.FIELDVO_CUSTOMFIELD_MAX_VALUE);
		initCustomFieldTypeDdc();
		
		//customfield category order Number.
		customeFieldCategoryOrderNoTxtFld = new TextField<Long>(Constants.FIELDVO_CUSTOMFIELD_CUSTOEMFIELDCATEGORY_ORDERNUMBER);
		customeFieldCategoryOrderNoTxtFld.setOutputMarkupId(true);
		customeFieldCategoryOrderNoTxtFld.setEnabled(false);
		
		initCustomeFieldCategoryDdc();
		initFieldTypeDdc();
		addFieldComponents();
	}

	private void addFieldComponents() {
		add(fieldIdTxtFld);
		add(customFieldTypeDdc);
		categoryPanel.add(customeFieldCategoryDdc);
		add(categoryPanel);
		orderNumberPanel.add(customeFieldCategoryOrderNoTxtFld);
		add(orderNumberPanel);
		add(fieldNameTxtFld);
		add(fieldTypeDdc);
		add(fieldDescriptionTxtAreaFld);
		panelCustomUnitTypeDropDown.setVisible(!isModuleStudy());
		panelCustomUnitTypeText.setVisible(isModuleStudy());
		panelCustomUnitTypeDropDown.add(fieldUnitsTxtFld);
		add(panelCustomUnitTypeDropDown);
		panelCustomUnitTypeText.add(fieldUnitsInTextTxtFld);
		add(panelCustomUnitTypeText);
		add(fieldMinValueTxtFld);
		add(fieldMaxValueTxtFld);
	}
	
	/**
	 * initialize Custom Filed Types.
	 */
	private void initCustomFieldTypeDdc() {
		Long sessionModuleId = (Long) SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.core.Constants.ARK_MODULE_KEY);
		arkModule=iArkCommonService.getArkModuleById(sessionModuleId);
		java.util.Collection<CustomFieldType> customFieldTypeCollection = iArkCommonService.getCustomFieldTypes(arkModule);
		ChoiceRenderer customfieldTypeRenderer = new ChoiceRenderer(Constants.CUSTOM_FIELD_TYPE_NAME, Constants.CUSTOM_FIELD_TYPE_ID);
		customFieldTypeDdc = new DropDownChoice<CustomFieldType>(Constants.FIELDVO_CUSTOMFIELD_CUSTOM_FIELD_TYPE, (List) customFieldTypeCollection, customfieldTypeRenderer);
		customFieldTypeDdc.add(new AjaxFormComponentUpdatingBehavior("onchange") {
		private static final long serialVersionUID = 1L;
				@Override
			    protected void onUpdate(AjaxRequestTarget target) {
					cpModel.getObject().getCustomField().setCustomFieldType(customFieldTypeDdc.getModelObject());
					Collection<CustomFieldCategory> customFieldCategoryCollection=getCategoriesListInCustomFieldsByCustomFieldType();
					categoryPanel.remove(customeFieldCategoryDdc);
					ChoiceRenderer customfieldCategoryRenderer = new ChoiceRenderer(Constants.CUSTOMFIELDCATEGORY_NAME, Constants.CUSTOMFIELDCATEGORY_ID);
					customeFieldCategoryDdc = new DropDownChoice<CustomFieldCategory>(Constants.FIELDVO_CUSTOMFIELD_CUSTOEMFIELDCATEGORY, (List) customFieldCategoryCollection, customfieldCategoryRenderer);
					customeFieldCategoryDdc.setOutputMarkupId(true);
					customeFieldCategoryDdc.add(new AjaxFormComponentUpdatingBehavior("onchange") {
						@Override
						protected void onUpdate(AjaxRequestTarget target) {
							customFieldCategoryChangeEvent(target);
						}
					});
					customFieldCategoryChangeEvent(target);
					categoryPanel.add(customeFieldCategoryDdc);
			    	target.add(customeFieldCategoryDdc);
			    	target.add(categoryPanel);
			    }
			});
	}
	/**
	 * 
	 * @param target
	 */
	private void customFieldCategoryChangeEvent(AjaxRequestTarget target) {
		cpModel.getObject().getCustomField().setCustomFieldCategory(customeFieldCategoryDdc.getModelObject());
		orderNumberPanel.remove(customeFieldCategoryOrderNoTxtFld);
		customeFieldCategoryOrderNoTxtFld = new TextField<Long>(Constants.FIELDVO_CUSTOMFIELD_CUSTOEMFIELDCATEGORY_ORDERNUMBER);
		customeFieldCategoryOrderNoTxtFld.setOutputMarkupId(true);
		customeFieldCategoryOrderNoTxtFld.setEnabled(false);
		orderNumberPanel.add(customeFieldCategoryOrderNoTxtFld);
		target.add(customeFieldCategoryOrderNoTxtFld);
		target.add(orderNumberPanel);
	}
	
	
	
	/**
	 * Initialize Custom Field Categories.
	 */
	private void initCustomeFieldCategoryDdc() {
		categoryPanel = new WebMarkupContainer("categoryPanel");
		categoryPanel.setOutputMarkupId(true);
		orderNumberPanel = new WebMarkupContainer("orderNumberPanel");
		orderNumberPanel.setOutputMarkupId(true);
		java.util.Collection<CustomFieldCategory> customFieldCategoryCollection=getCategoriesListInCustomFieldsByCustomFieldType();
		ChoiceRenderer customfieldCategoryRenderer = new ChoiceRenderer(Constants.CUSTOMFIELDCATEGORY_NAME, Constants.CUSTOMFIELDCATEGORY_ID);
		customeFieldCategoryDdc = new DropDownChoice<CustomFieldCategory>(Constants.FIELDVO_CUSTOMFIELD_CUSTOEMFIELDCATEGORY, (List) customFieldCategoryCollection, customfieldCategoryRenderer);
		customeFieldCategoryDdc.setOutputMarkupId(true);
	}
	/**
	 * Get custom field category collection from model.
	 * @return
	 */
	private Collection<CustomFieldCategory> getCategoriesListInCustomFieldsByCustomFieldType(){
		CustomField customField=cpModel.getObject().getCustomField();
		Study study=customField.getStudy();
		ArkFunction arkFunction=customField.getArkFunction();
		CustomFieldType customFieldType=customField.getCustomFieldType();
		try {
			customFieldCategoryCollection =  iArkCommonService.getCategoriesListInCustomFieldsByCustomFieldType(study, arkFunction, customFieldType);
			customFieldCategoryCollection=sortLst(remeoveDuplicates((List<CustomFieldCategory>)customFieldCategoryCollection));
			
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
		getModelObject().getCustomField().setStudy(study);

		long count = iArkCommonService.getCustomFieldCount(getModelObject().getCustomField());
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
		CustomField cf = getModelObject().getCustomField();
		CompoundPropertyModel<CustomFieldVO> newModel = new CompoundPropertyModel<CustomFieldVO>(new CustomFieldVO());
		CustomField newCF = newModel.getObject().getCustomField();
		// Copy all the customField attributes across from the SearchForm
		newCF.setStudy(cf.getStudy());
		newCF.setArkFunction(cf.getArkFunction());
		newCF.setCustomFieldType(cf.getCustomFieldType());
		newCF.setCustomFieldCategory(cf.getCustomFieldCategory());
		newCF.setName(cf.getName());
		newCF.setFieldType(cf.getFieldType());
		newCF.setDescription(cf.getDescription());
		/*
		 * NB: Do NOT copy unitType across because it is a Textfield on the
		 * SearchForm. If you copy this through, then DetailForm will have
		 * transient error during onSave(..). Also, if the user chooses
		 * fieldType==DATE, this and unitType is not a valid combination (but
		 * unitTypeDdc will be disabled, so the user can't make it null for it
		 * to be valid).
		 */
		newCF.setMinValue(cf.getMinValue());
		newCF.setMaxValue(cf.getMaxValue());
		newModel.getObject().setUseCustomFieldDisplay(getModelObject().isUseCustomFieldDisplay());

		DetailPanel detailPanel = new DetailPanel("detailPanel", feedbackPanel, newModel, arkCrudContainerVO);
				//this.unitTypeDropDownOn, this.subjectCustomField);
		arkCrudContainerVO.getDetailPanelContainer().addOrReplace(detailPanel);

		// Reset model's CF object (do NOT replace the CustomFieldVO in the
		// model)
		cf = new CustomField();
		cf.setStudy(newCF.getStudy());
		cf.setArkFunction(newCF.getArkFunction());
		getModelObject().setCustomField(cf);

		preProcessDetailPanel(target);
	}
	/**
	 * Sort custom field list according to the order number.
	 * @param customFieldLst
	 * @return
	 */
	private  List<CustomFieldCategory> sortLst(List<CustomFieldCategory> customFieldLst){
		//sort by order number.
		Collections.sort(customFieldLst, new Comparator<CustomFieldCategory>(){
		    public int compare(CustomFieldCategory custFieldCategory1, CustomFieldCategory custFieldCatCategory2) {
		        return custFieldCategory1.getName().compareTo(custFieldCatCategory2.getName());
		    }
		});
				return customFieldLst;
	}
	/**
	 * Remove duplicates from list
	 * @param customFieldLst
	 * @return
	 */
	private  List<CustomFieldCategory> remeoveDuplicates(List<CustomFieldCategory> customFieldLst){
		Set<CustomFieldCategory> cusfieldCatSet=new HashSet<CustomFieldCategory>();
		List<CustomFieldCategory> cusfieldCatLst=new ArrayList<CustomFieldCategory>();
		cusfieldCatSet.addAll(customFieldLst);
		cusfieldCatLst.addAll(cusfieldCatSet);
				return cusfieldCatLst;
	}
	
	private boolean isModuleStudy(){
		return arkModule.getName().equals(au.org.theark.core.Constants.ARK_MODULE_STUDY);
	}
	

}
