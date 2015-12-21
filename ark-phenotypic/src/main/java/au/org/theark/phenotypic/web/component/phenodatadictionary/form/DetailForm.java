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
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.shiro.SecurityUtils;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.extensions.markup.html.form.DateTextField;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.IChoiceRenderer;
import org.apache.wicket.markup.html.form.ListMultipleChoice;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.EmptyPanel;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.model.util.ListModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.validation.validator.PatternValidator;
import org.apache.wicket.validation.validator.StringValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import au.org.theark.core.exception.ArkRunTimeException;
import au.org.theark.core.exception.ArkRunTimeUniqueException;
import au.org.theark.core.exception.ArkSystemException;
import au.org.theark.core.exception.ArkUniqueException;
import au.org.theark.core.exception.EntityCannotBeRemoved;
import au.org.theark.core.model.pheno.entity.PhenoDataSetCategory;
import au.org.theark.core.model.pheno.entity.PhenoDataSetField;
import au.org.theark.core.model.pheno.entity.PhenoDataSetFieldDisplay;
import au.org.theark.core.model.pheno.entity.PhenoDataSetGroup;
import au.org.theark.core.model.study.entity.ArkFunction;
import au.org.theark.core.model.study.entity.ArkModule;
import au.org.theark.core.model.study.entity.CustomField;
import au.org.theark.core.model.study.entity.CustomFieldCategory;
import au.org.theark.core.model.study.entity.CustomFieldDisplay;
import au.org.theark.core.model.study.entity.CustomFieldGroup;
import au.org.theark.core.model.study.entity.CustomFieldType;
import au.org.theark.core.model.study.entity.FieldType;
import au.org.theark.core.model.study.entity.Study;
import au.org.theark.core.model.study.entity.UnitType;
import au.org.theark.core.service.IArkCommonService;
import au.org.theark.core.util.CharactorMissingAndEncodedValueValidator;
import au.org.theark.core.util.CustomFieldCategoryOrderingHelper;
import au.org.theark.core.util.DateFromToValidator;
import au.org.theark.core.util.DoubleMinimumToMaximumValidator;
import au.org.theark.core.util.MissingValueDateRangeValidator;
import au.org.theark.core.util.MissingValueDoubleRangeValidator;
import au.org.theark.core.util.PhenoDataSetCategoryOrderingHelper;
import au.org.theark.core.vo.ArkCrudContainerVO;
import au.org.theark.core.vo.CustomFieldCategoryVO;
import au.org.theark.core.vo.CustomFieldVO;
import au.org.theark.core.vo.PhenoDataSetCategoryVO;
import au.org.theark.core.vo.PhenoDataSetFieldVO;
import au.org.theark.core.web.behavior.ArkDefaultFormFocusBehavior;
import au.org.theark.core.web.component.audit.button.HistoryButtonPanel;
import au.org.theark.core.web.component.customfield.dataentry.DateDataEntryPanel;
import au.org.theark.core.web.component.customfield.dataentry.NumberDataEntryPanel;
import au.org.theark.core.web.component.customfield.dataentry.StringDateModel;
import au.org.theark.core.web.component.customfield.dataentry.TextDataEntryPanel;
import au.org.theark.core.web.form.AbstractDetailForm;
import au.org.theark.phenotypic.model.vo.PhenoFieldUploadVO;
import au.org.theark.phenotypic.service.IPhenotypicService;
import au.org.theark.phenotypic.web.component.phenodatadictionary.Constants;


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
public class DetailForm extends AbstractDetailForm<PhenoDataSetFieldVO> {
	private static final Logger				log	= LoggerFactory.getLogger(DetailForm.class);
	@SpringBean(name = au.org.theark.core.Constants.ARK_COMMON_SERVICE)
	private IArkCommonService<Void>				iArkCommonService;

	private int											mode;

	private TextField<String>						fieldIdTxtFld;
	private TextField<String>						fieldNameTxtFld;
	private DropDownChoice<FieldType>				fieldTypeDdc;
	private TextArea<String>						fieldDescriptionTxtAreaFld;
	private DropDownChoice<UnitType>				fieldUnitTypeDdc;
	private TextField<String>						fieldUnitTypeTxtFld;
	private TextArea<String>						fieldEncodedValuesTxtFld;

	private TextArea<String>						fieldLabelTxtAreaFld;
	private CheckBox								fieldDisplayRequiredChkBox;
	private CheckBox								fieldAllowMultiselectChkBox;
	private DropDownChoice<PhenoDataSetGroup>		fieldDisplayFieldGroupDdc;

	protected WebMarkupContainer					phenoDataSetDetailWMC;
	protected WebMarkupContainer					minMaxValueEntryWMC;
	protected WebMarkupContainer					missingValueEntryWMC;
	
	protected WebMarkupContainer					phenoDataSetDisplayDetailWMC;
	protected Panel									phenoDataSetDisplayPositionPanel;
	protected Panel									minValueEntryPnl;
	protected Panel									maxValueEntryPnl;
	protected Panel									missingValueEntryPnl;

	private TextArea<String>						defaultValueTextArea;
	
	protected IModel<List<PhenoDataSetGroup>>		cfGroupDdcListModel;
	
	//New two webMarkupContainers to hold different unit types DropDown and Text.
	private WebMarkupContainer  					panelCustomUnitTypeDropDown;
	private WebMarkupContainer  					panelCustomUnitTypeText;
	
	private HistoryButtonPanel 						historyButtonPanel;
	//private TextField<Long>							phenoDataSetCategoryOrderNoTxtFld;
	private ArkModule 								arkModule;
	
	private Study study;
	private ArkFunction arkFunction;
	private List<PhenoDataSetCategory> allChildrenCategories=new ArrayList<PhenoDataSetCategory>();
	
	protected  WebMarkupContainer 					phenoDataSetListchoiceCategoryDetailWMC;
	private ListMultipleChoice<PhenoDataSetCategory> phenoDataSetCategoryAvailableListChoice; 
	private Button addButtonFL,removeButtonFL,addButtonLL,removeButtonLL,upButton,downButton,leftButton,rightButton;
	private ListMultipleChoice<PhenoDataSetCategory> phenoDataSetCategoryFirstLevelListChoice; 
	private ListMultipleChoice<PhenoDataSetCategory> phenoDataSetCategoryLastLevelListChoice;
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
	public DetailForm(String id, CompoundPropertyModel<PhenoDataSetFieldVO> cpModel, FeedbackPanel feedBackPanel, ArkCrudContainerVO arkCrudContainerVO){
		super(id, feedBackPanel, cpModel, arkCrudContainerVO);
		cfGroupDdcListModel = new ListModel<PhenoDataSetGroup>();
		refreshEntityFromBackend();
		Long sessionModuleId = (Long) SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.core.Constants.ARK_MODULE_KEY);
		study =cpModel.getObject().getPhenoDataSetField().getStudy();
		arkFunction=iArkCommonService.getArkFunctionByName(au.org.theark.core.Constants.FUNCTION_KEY_VALUE_SUBJECT_CUSTOM_FIELD_CATEGORY);
	}

	protected void refreshEntityFromBackend() {
		// Refresh the entity from the backend
		if (getModelObject().getPhenoDataSetField().getId() != null) {
			PhenoDataSetField cfFromBackend = iPhenotypicService.getPhenoDataSetField(getModelObject().getPhenoDataSetField().getId());
			getModelObject().setPhenoDataSetField(cfFromBackend);

			PhenoDataSetFieldDisplay pdsdFromBackend;
			pdsdFromBackend = iPhenotypicService.getPhenoDataSetFieldDisplayByPhenoDataSet(getModelObject().getPhenoDataSetField());
			getModelObject().setPhenoDataSetFieldDisplay(pdsdFromBackend);
		}
		if (getModelObject().isUsePhenoDataSetFieldDisplay() == true) {
			// Ensure the phenoDataSetFieldDisplay.require is never NULL
			if (getModelObject().getPhenoDataSetFieldDisplay().getRequired() == null) {
				getModelObject().getPhenoDataSetFieldDisplay().setRequired(false);
			}
		}
	}
	/**
	 * initialise Field Types.
	 */
	private void initFieldTypeDdc() {
		List<FieldType> fieldTypeCollection = iArkCommonService.getFieldTypes();
		ChoiceRenderer fieldTypeRenderer = new ChoiceRenderer(Constants.FIELDTYPE_NAME, Constants.FIELDTYPE_ID);
		fieldTypeDdc = new DropDownChoice<FieldType>(Constants.FIELDVO_PHENODATASET_FIELD_TYPE, fieldTypeCollection, fieldTypeRenderer) {
			@Override
			protected void onBeforeRender() {
				if (!isNew()) {
					// Disable fieldType if data exists for the field
					setEnabled(!cpModel.getObject().getPhenoDataSetField().getPhenoFieldHasData());
				}
				super.onBeforeRender();
			}
		};
		fieldTypeDdc.add(new AjaxFormComponentUpdatingBehavior("onchange") {
			protected void onUpdate(AjaxRequestTarget target) {
				updateEncodedValueFld();
				updateUnitTypeDdc();
				initMinMaxValuePnls();
				target.add(minMaxValueEntryWMC);
				target.add(missingValueEntryWMC);
				target.add(fieldEncodedValuesTxtFld);
				target.add(fieldUnitTypeDdc);
				/*if(fieldCategoryDdc != null) {
					target.add(fieldCategoryDdc);
				}*/
				//Add field unite type as text
				target.add(fieldUnitTypeTxtFld);
				target.add(fieldAllowMultiselectChkBox);
			}
		});
	}
	
	
	private void initCategoryListChoiceContainer(){
		List<PhenoDataSetCategory> phenoDataSetFieldCategories = new ArrayList<PhenoDataSetCategory>(0);
		phenoDataSetFieldCategories= PhenoDataSetCategoryOrderingHelper.getInstance().orderHierarchicalyphenoDatasetCategories(getAvailableAllCategoryListInStudy());
		cpModel.getObject().setPhenoDataSetFieldCategoryLst(phenoDataSetFieldCategories);
		IChoiceRenderer<String> renderer = new ChoiceRenderer("name", "id"){
			@Override
			public Object getDisplayValue(Object object) {
				PhenoDataSetCategory cuscat=(PhenoDataSetCategory)object;
				return PhenoDataSetCategoryOrderingHelper.getInstance().preTextDecider(cuscat)+ super.getDisplayValue(object);
			}
		};
		PropertyModel<List<PhenoDataSetCategory>> availableCategories = new PropertyModel<List<PhenoDataSetCategory>>(cpModel, "phenoDataSetFieldCategoryLst");
		PropertyModel<List<PhenoDataSetCategory>> selectedCategories = new PropertyModel<List<PhenoDataSetCategory>>(cpModel, "selectedCategories");
		PropertyModel<List<PhenoDataSetCategory>> firstLevelAvailableCategories = new PropertyModel<List<PhenoDataSetCategory>>(cpModel, "firstLevelAvailableCategories");
		PropertyModel<List<PhenoDataSetCategory>> firstLevelSelectedCategories = new PropertyModel<List<PhenoDataSetCategory>>(cpModel, "firstLevelSelectedCategories");
		PropertyModel<List<PhenoDataSetCategory>> lastLevelAvailableCategories = new PropertyModel<List<PhenoDataSetCategory>>(cpModel, "lastLevelAvailableCategories");
		PropertyModel<List<PhenoDataSetCategory>> lastLevelSelectedCategories = new PropertyModel<List<PhenoDataSetCategory>>(cpModel, "lastLevelSelectedCategories");
		
		availableCategories(renderer, availableCategories, selectedCategories);
		firstLevelAvailableCategories(renderer, firstLevelAvailableCategories,firstLevelSelectedCategories);
		lastLevelAvailableCategories(renderer, lastLevelAvailableCategories,lastLevelSelectedCategories);
		
		addButtonFirstLevel(selectedCategories);
		removeButtonFirstLevel(firstLevelSelectedCategories);
		addButtonLastLevel(firstLevelSelectedCategories);
		removeButtonLastLevel(lastLevelSelectedCategories);
		upButton(firstLevelSelectedCategories);
		downButton(firstLevelSelectedCategories);
		leftButton(firstLevelSelectedCategories);
		rightButton(firstLevelSelectedCategories);
		
	}
	/**
	 * Available categories.
	 * @param renderer
	 * @param availableCategories
	 * @param selectedCategories
	 */
	private void availableCategories(IChoiceRenderer<String> renderer,
			PropertyModel<List<PhenoDataSetCategory>> availableCategories,PropertyModel<List<PhenoDataSetCategory>> selectedCategories) {
		phenoDataSetCategoryAvailableListChoice=new ListMultipleChoice("phenoDataSetCategoryLst", selectedCategories,availableCategories,renderer);
		phenoDataSetCategoryAvailableListChoice.add(new AjaxFormComponentUpdatingBehavior("onchange") {
            protected void onUpdate(AjaxRequestTarget target) {
            	cpModel.getObject().setSelectedCategories(selectedCategories.getObject());
            	target.add(phenoDataSetCategoryAvailableListChoice);
            }
            });
	}
	/**
	 * First Level available categories.
	 * @param renderer
	 * @param firstLevelAvailableCategories
	 * @param firstLevelSelectedCategories
	 */
	private void firstLevelAvailableCategories(IChoiceRenderer<String> renderer,
			PropertyModel<List<PhenoDataSetCategory>> firstLevelAvailableCategories,
			PropertyModel<List<PhenoDataSetCategory>> firstLevelSelectedCategories) {
		phenoDataSetCategoryFirstLevelListChoice=new ListMultipleChoice("firstLevelAvailableCategories", firstLevelSelectedCategories,firstLevelAvailableCategories,renderer);
		phenoDataSetCategoryFirstLevelListChoice.setOutputMarkupId(true);
		phenoDataSetCategoryFirstLevelListChoice.add(new AjaxFormComponentUpdatingBehavior("onchange") {
            protected void onUpdate(AjaxRequestTarget target) {
            	cpModel.getObject().setFirstLevelSelectedCategories(firstLevelSelectedCategories.getObject());
            	target.add(phenoDataSetCategoryFirstLevelListChoice);
            }
            });
	}
	
	private void lastLevelAvailableCategories(IChoiceRenderer<String> renderer,
			PropertyModel<List<PhenoDataSetCategory>> lastLevelAvailableCategories,
			PropertyModel<List<PhenoDataSetCategory>> lastLevelSelectedCategories) {
		phenoDataSetCategoryLastLevelListChoice=new ListMultipleChoice("lastLevelAvailableCategories", lastLevelSelectedCategories,lastLevelAvailableCategories,renderer);
		phenoDataSetCategoryLastLevelListChoice.add(new AjaxFormComponentUpdatingBehavior("onchange") {
            protected void onUpdate(AjaxRequestTarget target) {
            	cpModel.getObject().setLastLevelSelectedCategories(lastLevelSelectedCategories.getObject());
            	target.add(phenoDataSetCategoryLastLevelListChoice);
            }
            });
		
	}
	
	/**
	 * First level add button.
	 * @param selectedCategories
	 */
	private void addButtonFirstLevel(
			PropertyModel<List<PhenoDataSetCategory>> selectedCategories) {
		addButtonFL = new AjaxButton("addButtonFL") {
			private static final long serialVersionUID = 1L;
			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
				List<PhenoDataSetCategory> clickAndSelectedCategories=selectedCategories.getObject();
				for (PhenoDataSetCategory phenoDataSetFieldCategory : clickAndSelectedCategories) {
            		allChildrenCategories=removeDuplicates(iPhenotypicService.getAllChildrenCategoriedBelongToThisParent(study, arkFunction, phenoDataSetFieldCategory,allChildrenCategories));
				}
					List<PhenoDataSetCategory> alreadyInFirstLevelCategories=cpModel.getObject().getFirstLevelAvailableCategories();
            		List<PhenoDataSetCategory> allFirstLevel=addChildrenWithoutDuplicates(clickAndSelectedCategories, allChildrenCategories);
            		cpModel.getObject().setFirstLevelAvailableCategories(PhenoDataSetCategoryOrderingHelper.getInstance().orderHierarchicalyphenoDatasetCategories(addChildrenWithoutDuplicates(alreadyInFirstLevelCategories, allFirstLevel)));
            		cpModel.getObject().getPhenoDataSetFieldCategoryLst().removeAll(allFirstLevel);
            		allChildrenCategories.clear();
            	    target.add(phenoDataSetCategoryAvailableListChoice);
                    target.add(phenoDataSetCategoryFirstLevelListChoice);
                    super.onSubmit(target, form);
			}
		};
		addButtonFL.setDefaultFormProcessing(false);
	}
	/**
	 * First level remove button.
	 * @param firstLevelSelectedCategories
	 */
	private void removeButtonFirstLevel(
			PropertyModel<List<PhenoDataSetCategory>> firstLevelSelectedCategories) {
		removeButtonFL = new AjaxButton("removeButtonFL") {
			private static final long serialVersionUID = 1L;
			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
				List<PhenoDataSetCategory> clickAndSelectedCategories=firstLevelSelectedCategories.getObject();
				for (PhenoDataSetCategory phenoDataSetFieldCategory : clickAndSelectedCategories) {
            		allChildrenCategories=removeDuplicates(iPhenotypicService.getAllChildrenCategoriedBelongToThisParent(study, arkFunction, phenoDataSetFieldCategory,allChildrenCategories));
				}
					List<PhenoDataSetCategory> currentlyAvailable=cpModel.getObject().getPhenoDataSetFieldCategoryLst();
					List<PhenoDataSetCategory> childAndAvailable=addChildrenWithoutDuplicates(currentlyAvailable,allChildrenCategories);
					List<PhenoDataSetCategory> allAvailable=addChildrenWithoutDuplicates(childAndAvailable,clickAndSelectedCategories);
					cpModel.getObject().setPhenoDataSetFieldCategoryLst(PhenoDataSetCategoryOrderingHelper.getInstance().orderHierarchicalyphenoDatasetCategories(allAvailable));
            		cpModel.getObject().getFirstLevelAvailableCategories().removeAll(allChildrenCategories);
            		cpModel.getObject().getFirstLevelAvailableCategories().removeAll(clickAndSelectedCategories);
            		allChildrenCategories.clear();
            	    target.add(phenoDataSetCategoryAvailableListChoice);
                    target.add(phenoDataSetCategoryFirstLevelListChoice);
                    super.onSubmit(target, form);
			}
		};
		removeButtonFL.setDefaultFormProcessing(false);
	}
	
	/**
	 * Last level add button.
	 * @param selectedCategories
	 */
	private void addButtonLastLevel(
			PropertyModel<List<PhenoDataSetCategory>> firstLevelSelectedCategories) {
		addButtonLL = new AjaxButton("addButtonLL") {
			private static final long serialVersionUID = 1L;
			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
					List<PhenoDataSetCategory> initialSelectedCategories=firstLevelSelectedCategories.getObject();
					List<PhenoDataSetCategory> currentlyAvailable=cpModel.getObject().getLastLevelAvailableCategories();
					List<PhenoDataSetCategory> allAvailable=addChildrenWithoutDuplicates(currentlyAvailable,initialSelectedCategories);
					cpModel.getObject().setLastLevelAvailableCategories(PhenoDataSetCategoryOrderingHelper.getInstance().orderHierarchicalyphenoDatasetCategories(allAvailable));
		    		cpModel.getObject().getFirstLevelAvailableCategories().removeAll(initialSelectedCategories);
            	    target.add(phenoDataSetCategoryFirstLevelListChoice);
            	    target.add(phenoDataSetCategoryLastLevelListChoice);
                    super.onSubmit(target, form);
			}
		};
		addButtonLL.setDefaultFormProcessing(false);
	}
	/**
	 * Last level remove button.
	 * @param firstLevelSelectedCategories
	 */
	private void removeButtonLastLevel(
			PropertyModel<List<PhenoDataSetCategory>> lastLevelSelectedCategories) {
		removeButtonLL = new AjaxButton("removeButtonLL") {
			private static final long serialVersionUID = 1L;
			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
				List<PhenoDataSetCategory> initialSelectedCategories=lastLevelSelectedCategories.getObject();
					List<PhenoDataSetCategory> currentlyAvailable=cpModel.getObject().getFirstLevelAvailableCategories();
					List<PhenoDataSetCategory> allAvailable=addChildrenWithoutDuplicates(currentlyAvailable,initialSelectedCategories);
					cpModel.getObject().setFirstLevelAvailableCategories(PhenoDataSetCategoryOrderingHelper.getInstance().orderHierarchicalyphenoDatasetCategories(allAvailable));
            		cpModel.getObject().getLastLevelAvailableCategories().removeAll(initialSelectedCategories);
            	    target.add(phenoDataSetCategoryFirstLevelListChoice);
                    target.add(phenoDataSetCategoryLastLevelListChoice);
                    super.onSubmit(target, form);
			}
		};
		removeButtonLL.setDefaultFormProcessing(false);
	}
	
	/**
	 * up button.
	 * @param firstLevelSelectedCategories
	 */
	private void upButton(
			PropertyModel<List<PhenoDataSetCategory>> firstLevelSelectedCategories) {
			upButton = new AjaxButton("upButton") {
			private static final long serialVersionUID = 1L;
			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
				List<PhenoDataSetCategory> initialSelectedCategories=firstLevelSelectedCategories.getObject();
				PhenoDataSetCategory selectedCategory = null;
				List<PhenoDataSetCategory> availableCats=cpModel.getObject().getFirstLevelAvailableCategories();
				if(initialSelectedCategories.size()==1){
					selectedCategory=initialSelectedCategories.get(0);
					if(selectedCategory.getParentCategory()!=null){
						List<PhenoDataSetCategory> sortedLst=getMySortedSiblingList(selectedCategory);
						if(sortedLst.indexOf(selectedCategory)!=0){
							List<PhenoDataSetCategory> updateList=upButtonUpdatePhenoFieldCategoryOrder(sortedLst,selectedCategory);
								for (PhenoDataSetCategory item : updateList) {
									if(availableCats.contains(item)){
										availableCats.get(availableCats.indexOf(item)).setOrderNumber(item.getOrderNumber());
									}
								}
							cpModel.getObject().setFirstLevelAvailableCategories(PhenoDataSetCategoryOrderingHelper.getInstance().orderHierarchicalyphenoDatasetCategories(availableCats));
						}else{
							//top level selected
						}
					}else{
						//parent selected
					}
				}else{
					//more than one selected.
				}	
				target.add(phenoDataSetCategoryAvailableListChoice);
				target.add(phenoDataSetCategoryFirstLevelListChoice);
				super.onSubmit(target, form);
			}
		};
		upButton.setDefaultFormProcessing(false);
	}
	/**
	 * down button.
	 * @param firstLevelSelectedCategories
	 */
	private void downButton(
			PropertyModel<List<PhenoDataSetCategory>> firstLevelSelectedCategories) {
		downButton = new AjaxButton("downButton") {
			private static final long serialVersionUID = 1L;
			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
				List<PhenoDataSetCategory> initialSelectedCategories=firstLevelSelectedCategories.getObject();
				PhenoDataSetCategory selectedCategory = null;
				List<PhenoDataSetCategory> availableCats=cpModel.getObject().getFirstLevelAvailableCategories();
				if(initialSelectedCategories.size()==1){
					selectedCategory=initialSelectedCategories.get(0);
					if(selectedCategory.getParentCategory()!=null){
						List<PhenoDataSetCategory> sortedLst=getMySortedSiblingList(selectedCategory);
						if(sortedLst.indexOf(selectedCategory)< (sortedLst.size()-1)){
							List<PhenoDataSetCategory> updateList=downButtonUpdatePhenoFieldCategoryOrder(sortedLst,selectedCategory);
								for (PhenoDataSetCategory item : updateList) {
									if(availableCats.contains(item)){
										availableCats.get(availableCats.indexOf(item)).setOrderNumber(item.getOrderNumber());
									}
								}
							cpModel.getObject().setFirstLevelAvailableCategories(PhenoDataSetCategoryOrderingHelper.getInstance().orderHierarchicalyphenoDatasetCategories(availableCats));
						}else{
							//bottom level selected
						}
					}else{
						//parent selected
					}
				}else{
					//more than one selected.
				}
				target.add(phenoDataSetCategoryAvailableListChoice);
				target.add(phenoDataSetCategoryFirstLevelListChoice);
	            super.onSubmit(target, form);
			}
		};
		downButton.setDefaultFormProcessing(false);
	}
	
	
	/**
	 * left button for make sub categories of above category.
	 * @param firstLevelSelectedCategories
	 */
	private void leftButton(
			PropertyModel<List<PhenoDataSetCategory>> firstLevelSelectedCategories) {
			leftButton = new AjaxButton("leftButton") {
			private static final long serialVersionUID = 1L;
			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
				List<PhenoDataSetCategory> initialSelectedCategories=firstLevelSelectedCategories.getObject();
				PhenoDataSetCategory selectedCategory = null;
				List<PhenoDataSetCategory> availableCats=cpModel.getObject().getFirstLevelAvailableCategories();
				if(initialSelectedCategories.size()==1){
					selectedCategory=initialSelectedCategories.get(0);
					if(selectedCategory.getParentCategory()!=null){
						if(availableCats.indexOf(selectedCategory)!=0){
							if(selectedCategory.getParentCategory().getParentCategory()!=null)
								selectedCategory.setParentCategory(selectedCategory.getParentCategory().getParentCategory());
							else{
								selectedCategory.setParentCategory(null);
							}
							PhenoDataSetCategoryVO phenoDataSetCategoryVO=new PhenoDataSetCategoryVO();
							phenoDataSetCategoryVO.setPhenoDataSetCategory(selectedCategory);
							try {
								iPhenotypicService.updatePhenoDataSetCategory(phenoDataSetCategoryVO);
							} catch (ArkSystemException| ArkRunTimeUniqueException| ArkRunTimeException | ArkUniqueException e) {
								e.printStackTrace();
							}
						}
						
					}else{
						//no parent category so no left function.
					}
					cpModel.getObject().setFirstLevelAvailableCategories(PhenoDataSetCategoryOrderingHelper.getInstance().orderHierarchicalyphenoDatasetCategories(availableCats));
				}else{
					//more than one selected.
				}	
				target.add(phenoDataSetCategoryAvailableListChoice);
				target.add(phenoDataSetCategoryFirstLevelListChoice);
				super.onSubmit(target, form);
			}
		};
		leftButton.setDefaultFormProcessing(false);
	}
	
	
	/**
	 * right button for make sub categories of above category.
	 * @param firstLevelSelectedCategories
	 */
	private void rightButton(
			PropertyModel<List<PhenoDataSetCategory>> firstLevelSelectedCategories) {
			rightButton = new AjaxButton("rightButton") {
			private static final long serialVersionUID = 1L;
			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
				List<PhenoDataSetCategory> initialSelectedCategories=firstLevelSelectedCategories.getObject();
				PhenoDataSetCategory selectedCategory = null;
				List<PhenoDataSetCategory> availableCats=cpModel.getObject().getFirstLevelAvailableCategories();
				if(initialSelectedCategories.size()==1){
					selectedCategory=initialSelectedCategories.get(0);
					//if(selectedCategory.getParentCategory()!=null){
						if(availableCats.indexOf(selectedCategory)!=0){
							selectedCategory.setParentCategory(availableCats.get(availableCats.indexOf(selectedCategory)-1));
							PhenoDataSetCategoryVO phenoDataSetCategoryVO=new PhenoDataSetCategoryVO();
							phenoDataSetCategoryVO.setPhenoDataSetCategory(selectedCategory);
							try {
								iPhenotypicService.updatePhenoDataSetCategory(phenoDataSetCategoryVO);
							} catch (ArkSystemException| ArkRunTimeUniqueException| ArkRunTimeException | ArkUniqueException e) {
								e.printStackTrace();
							}
						}
						
				//	}else{
						//no parent category
				//	}
					cpModel.getObject().setFirstLevelAvailableCategories(PhenoDataSetCategoryOrderingHelper.getInstance().orderHierarchicalyphenoDatasetCategories(availableCats));
						
				}else{
					//more than one selected.
				}	
				target.add(phenoDataSetCategoryAvailableListChoice);
				target.add(phenoDataSetCategoryFirstLevelListChoice);
				super.onSubmit(target, form);
			}
		};
		rightButton.setDefaultFormProcessing(false);
	}
	

	private void updateEncodedValueFld() {
		FieldType fieldType = getModelObject().getPhenoDataSetField().getFieldType();
		if (fieldType != null && fieldType.getName().equals(Constants.FIELD_TYPE_CHARACTER)) {
			// Only allowed to use encodedValues when fieldType == CHARACTER
			fieldEncodedValuesTxtFld.setEnabled(true);
			fieldAllowMultiselectChkBox.setEnabled(true);
		}
		else {
			// Forcibly reset encoded values and allow multi when NUMBER or DATE
			String encVals = fieldEncodedValuesTxtFld.getDefaultModelObjectAsString();
			if (!encVals.isEmpty()) {
				fieldEncodedValuesTxtFld.setDefaultModelObject(null);
			}
			fieldEncodedValuesTxtFld.setEnabled(false);
			if(fieldAllowMultiselectChkBox.getDefaultModelObject() != null) {
				fieldAllowMultiselectChkBox.setDefaultModelObject(false);
			}
			fieldAllowMultiselectChkBox.setEnabled(false);
		}
	}

	private void updateUnitTypeDdc() {
		FieldType fieldType = getModelObject().getPhenoDataSetField().getFieldType();
		if (fieldType != null && !fieldType.getName().equals(Constants.FIELD_TYPE_DATE)) {
			// Only allowed to use unitType when fieldType != DATE
			fieldUnitTypeDdc.setEnabled(true);
			fieldUnitTypeTxtFld.setEnabled(true);
		}
		else {
			fieldUnitTypeDdc.setEnabled(false);
			fieldUnitTypeTxtFld.setEnabled(false);
		}
	}


	/*private void updateCategoryDdc() {
		CustomFieldCategory category = getModelObject().getPhenoDataSetField().getCustomFieldCategory();
		if (category != null && !category.getName().equals(Constants.DATE_FIELD_TYPE_NAME)) {
			// Only allowed to use unitType when fieldType != DATE
			fieldUnitTypeDdc.setEnabled(true);
			fieldUnitTypeTxtFld.setEnabled(true);
		}
		else {
			fieldUnitTypeDdc.setEnabled(false);
			fieldUnitTypeTxtFld.setEnabled(false);
		}
		//TODO dependencies?
		//have to figure out what dependencies exist
	}*/
	/**
	 * initialise max and min values.
	 */
	private void initMinMaxValuePnls() {
		FieldType fieldType = getModelObject().getPhenoDataSetField().getFieldType();
		
			if (fieldType == null || fieldType.getName().equals(Constants.FIELD_TYPE_CHARACTER) 
					|| fieldType.getName().equals(Constants.LOOKUP_FIELD_TYPE_NAME)) {
				// Create disabled min and max value entry panels for fieldType = unspecified (null) / CHARACTER
				// dummyModel is required to ensure Wicket doesn't try to find the textDateValue in the CompoundPropertyModel
				IModel<String> dummyModel = new IModel<String>() {
					public String getObject() {
						return null;
					}
					public void setObject(String object) {
					}
					public void detach() {
					}
				};
				IModel<String> missingValueMdl = new PropertyModel<String>(getModelObject(), Constants.FIELDVO_PHENODATASET_MISSING_VALUE);
				minValueEntryPnl = new TextDataEntryPanel("minValueEntryPanel", dummyModel, new Model<String>("MinValue"));
				minValueEntryPnl.setOutputMarkupPlaceholderTag(true);
				minValueEntryPnl.setEnabled(false);
				maxValueEntryPnl = new TextDataEntryPanel("maxValueEntryPanel", dummyModel, new Model<String>("MinValue"));
				maxValueEntryPnl.setOutputMarkupPlaceholderTag(true);
				maxValueEntryPnl.setEnabled(false);
				missingValueEntryPnl = new TextDataEntryPanel("missingValueEntryPanel", missingValueMdl, new Model<String>("MissingValue"));
				missingValueEntryPnl.setOutputMarkupPlaceholderTag(true);
				missingValueEntryPnl.setEnabled(true);
				TextField<?> missing= ((TextDataEntryPanel)missingValueEntryPnl).getDataValueTxtFld();
				this.add(new CharactorMissingAndEncodedValueValidator(fieldEncodedValuesTxtFld, missing, "Encoded Values","Missing Value"));
				
			}
			// Not supporting min and max value for CHARACTER fieldTypes
			else if (fieldType.getName().equals(Constants.FIELD_TYPE_CHARACTER)) {
				// NUMBER fieldType
				IModel<Double> minValueMdl = new PropertyModel<Double>(getModelObject(), Constants.FIELDVO_PHENODATASET_MIN_VALUE);
				IModel<Double> maxValueMdl = new PropertyModel<Double>(getModelObject(), Constants.FIELDVO_PHENODATASET_MAX_VALUE);
				IModel<Double> missingValueMdl = new PropertyModel<Double>(getModelObject(), Constants.FIELDVO_PHENODATASET_MISSING_VALUE);
				minValueEntryPnl = new NumberDataEntryPanel("minValueEntryPanel", minValueMdl, new Model<String>("MinValue"));
				minValueEntryPnl.setOutputMarkupPlaceholderTag(true);
				maxValueEntryPnl = new NumberDataEntryPanel("maxValueEntryPanel", maxValueMdl, new Model<String>("MaxValue"));
				maxValueEntryPnl.setOutputMarkupPlaceholderTag(true);
				missingValueEntryPnl = new NumberDataEntryPanel("missingValueEntryPanel", missingValueMdl, new Model<String>("MissingValue"));
				missingValueEntryPnl.setOutputMarkupPlaceholderTag(true);
				
				TextField<?> min= ((NumberDataEntryPanel)minValueEntryPnl).getDataValueTxtFld();
				TextField<?> max= ((NumberDataEntryPanel)maxValueEntryPnl).getDataValueTxtFld();
				TextField<?> missingDate= ((NumberDataEntryPanel)missingValueEntryPnl).getDataValueTxtFld();
				
				this.add(new DoubleMinimumToMaximumValidator(min, max, "Minimum Value", "Maximum Value"));
				this.add(new MissingValueDoubleRangeValidator(min,max,missingDate,"Minimum Value","Maximum Value","Missing Value"));
				
			}
			else if (fieldType.getName().equals(Constants.FIELD_TYPE_DATE)) {
				// DATE fieldType
				IModel<Date> minValueMdl = new StringDateModel(new PropertyModel<String>(getModelObject(), Constants.FIELDVO_PHENODATASET_MIN_VALUE), au.org.theark.core.Constants.DD_MM_YYYY);
				IModel<Date> maxValueMdl = new StringDateModel(new PropertyModel<String>(getModelObject(), Constants.FIELDVO_PHENODATASET_MAX_VALUE), au.org.theark.core.Constants.DD_MM_YYYY);
				IModel<Date> missingValueMdl = new StringDateModel(new PropertyModel<String>(getModelObject(), Constants.FIELDVO_PHENODATASET_MISSING_VALUE), au.org.theark.core.Constants.DD_MM_YYYY);
				
				minValueEntryPnl = new DateDataEntryPanel("minValueEntryPanel", minValueMdl, new Model<String>("MinValue"));
				minValueEntryPnl.setOutputMarkupPlaceholderTag(true);
				maxValueEntryPnl = new DateDataEntryPanel("maxValueEntryPanel", maxValueMdl, new Model<String>("MaxValue"));
				maxValueEntryPnl.setOutputMarkupPlaceholderTag(true);
				missingValueEntryPnl = new DateDataEntryPanel("missingValueEntryPanel", missingValueMdl, new Model<String>("MissingValue"));
				missingValueEntryPnl.setOutputMarkupPlaceholderTag(true);
				
				DateTextField fromDate= ((DateDataEntryPanel)minValueEntryPnl).getDataValueDateFld();
				DateTextField toDate= ((DateDataEntryPanel)maxValueEntryPnl).getDataValueDateFld();
				DateTextField missingDate= ((DateDataEntryPanel)missingValueEntryPnl).getDataValueDateFld();
				
				this.add(new DateFromToValidator(fromDate,toDate,"Minimum Date","Maximum Date"));
				this.add(new MissingValueDateRangeValidator(fromDate,toDate,missingDate,"Minimum Date","Maximum Date","Missing Date"));
			}
			minMaxValueEntryWMC.addOrReplace(minValueEntryPnl);
			minMaxValueEntryWMC.addOrReplace(maxValueEntryPnl);
			missingValueEntryWMC.addOrReplace(missingValueEntryPnl);
		
	}
	/**
	 * initialise unit types.
	 */
	private void initUnitTypeDdc() {
		UnitType unitTypeCriteria = new UnitType();
		unitTypeCriteria.setArkFunction(cpModel.getObject().getPhenoDataSetField().getArkFunction());
		List<UnitType> unitTypeList = iArkCommonService.getUnitTypes(unitTypeCriteria);
		// assumes that if the unit.name will appear within the unit.description

		ChoiceRenderer unitTypeRenderer = new ChoiceRenderer(Constants.UNITTYPE_DESCRIPTION, Constants.UNITTYPE_ID);
		fieldUnitTypeDdc = new DropDownChoice<UnitType>(Constants.FIELDVO_PHENODATASET_UNIT_TYPE, unitTypeList, unitTypeRenderer);
		fieldUnitTypeDdc.setNullValid(true); // null is ok for units
		fieldUnitTypeDdc.setOutputMarkupId(true); // unitTypeDdc can be enabled/disabled
		
		//Add the Unit type text.
		fieldUnitTypeTxtFld=new TextField<String>(Constants.FIELDVO_PHENODATASET_UNIT_TYPE_TXT);
		fieldUnitTypeTxtFld.setOutputMarkupId(true);
		
	}

	/**
	 * Call this after the constructor is finished in detail panel.
	 */
	public void initialiseDetailForm() {
		Long sessionModuleId = (Long) SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.core.Constants.ARK_MODULE_KEY);
		arkModule=iArkCommonService.getArkModuleById(sessionModuleId);
		fieldIdTxtFld = new TextField<String>(Constants.FIELDVO_PHENODATASET_ID);
		fieldNameTxtFld = new TextField<String>(Constants.FIELDVO_PHENODATASET_NAME);
		fieldNameTxtFld.add(new ArkDefaultFormFocusBehavior());
		fieldDescriptionTxtAreaFld = new TextArea<String>(Constants.FIELDVO_PHENODATASET_DESCRIPTION);
		fieldLabelTxtAreaFld = new TextArea<String>(Constants.FIELDVO_PHENODATASET_FIELD_LABEL);
		//fieldMissingValueTxtFld = new TextField<String>(Constants.FIELDVO_CUSTOMFIELD_MISSING_VALUE);
		fieldDisplayRequiredChkBox = new CheckBox(Constants.FIELDVO_PHENODATASETDISPLAY_REQUIRED);
		fieldAllowMultiselectChkBox = new CheckBox(Constants.FIELDVO_PHENODATASET_ALLOW_MULTISELECT) {
		private static final long	serialVersionUID	= 1L;

			@Override
			public boolean isEnabled() {
				return (fieldEncodedValuesTxtFld.getModelObject() != null);
			}
		};

		if (getModelObject().isUsePhenoDataSetFieldDisplay()) {
			// TODO: Have not implemented position support right now
			phenoDataSetDisplayPositionPanel = new EmptyPanel("phenoDataSetFieldDisplayPositionPanel");
		}
		else {
			phenoDataSetDisplayPositionPanel = new EmptyPanel("phenoDataSetFieldDisplayPositionPanel");
		}
		//phenoDataSetfield category order Number.
		//phenoDataSetCategoryOrderNoTxtFld = new TextField<Long>(Constants.FIELDVO_PHENODATASET_CUSTOEMFIELDCATEGORY_ORDERNUMBER);
		//phenoDataSetCategoryOrderNoTxtFld.setOutputMarkupId(true);
		//phenoDataSetCategoryOrderNoTxtFld.setEnabled(false);
		initCategoryListChoiceContainer();
		
		//initCategoryPalette();
		// Initialise Drop Down Choices
		initFieldTypeDdc();
		//initCustomFieldTypeDdc();
		//initCustomeFieldCategoryDdc();
		initUnitTypeDdc();

		// Min and Max Value panels rely on fieldTypeDdc being already established
		minMaxValueEntryWMC = new WebMarkupContainer("minMaxValueEntryWMC");
		minMaxValueEntryWMC.setOutputMarkupPlaceholderTag(true);
		missingValueEntryWMC= new WebMarkupContainer("missingValueEntryWMC");
		missingValueEntryWMC.setOutputMarkupPlaceholderTag(true);

		// unitType and encodedValues rely on fieldTypeDdc being already established
		fieldEncodedValuesTxtFld = new TextArea<String>(Constants.FIELDVO_PHENODATASET_ENCODED_VALUES);
		fieldEncodedValuesTxtFld.setOutputMarkupId(true);
		fieldEncodedValuesTxtFld.add(new AjaxFormComponentUpdatingBehavior("onchange") {
			@Override
			protected void onUpdate(AjaxRequestTarget target) {
				target.add(fieldAllowMultiselectChkBox);
			}
		});

		updateEncodedValueFld();
		updateUnitTypeDdc();

		// Have to Edit, before allowing delete
		deleteButton.setEnabled(false);

		defaultValueTextArea = new TextArea<String>("phenoDataSetField.defaultValue");
		
		addDetailFormComponents();
		attachValidators();

		initMinMaxValuePnls();
		
		historyButtonPanel = new HistoryButtonPanel(this, arkCrudContainerVO.getEditButtonContainer(), arkCrudContainerVO.getDetailPanelFormContainer());
	}
	
	protected void attachValidators() {
		fieldNameTxtFld.setRequired(true).setLabel((new StringResourceModel("phenoDataSetField.name.required", this, new Model<String>("Custom Field Name"))));
		// Enforce particular characters for fieldName
		fieldNameTxtFld.add(new PatternValidator("[a-zA-Z0-9_-]+"));
		fieldTypeDdc.setRequired(true).setLabel((new StringResourceModel("phenoDataSetField.fieldType.required", this, new Model<String>("Custom Field Type"))));
		fieldLabelTxtAreaFld.add(StringValidator.maximumLength(255));

		// TODO: Add correct validator, possibly phenoDataSet with better validation message
		fieldEncodedValuesTxtFld.add(new PatternValidator(au.org.theark.core.Constants.ENCODED_VALUES_PATTERN)).setLabel(
				new StringResourceModel("phenoDataSetField.encodedValues.validation", this, new Model<String>("Encoded Value definition")));
	}

	@Override
	protected void onSave(Form<PhenoDataSetFieldVO> containerForm, AjaxRequestTarget target) {
		// NB: creating/updating the phenoDataSetFieldDisplay will be tied to the phenoDataSetField by the service
		if (getModelObject().getPhenoDataSetField().getId() == null) {
			// Save the Field
			try {
				iPhenotypicService.createPhenoDataSetField(getModelObject());
				this.info(new StringResourceModel("info.createSuccessMsg", this, null, new Object[] { getModelObject().getPhenoDataSetField().getName() }).getString());
				onSavePostProcess(target);
			}
			catch (ArkSystemException e) {
				this.error(new StringResourceModel("error.internalErrorMsg", this, null).getString());
				e.printStackTrace();
			}
			catch (ArkUniqueException e) {
				this.error(new StringResourceModel("error.nonUniqueCFMsg", this, null, new Object[] { getModelObject().getPhenoDataSetField().getName() }).getString());
				e.printStackTrace();
			}
			processErrors(target);
		}
		else {
			// Update the Field
			try {
				iPhenotypicService.updatePhenoDataSetField(getModelObject());
				this.info(new StringResourceModel("info.updateSuccessMsg", this, null, new Object[] { getModelObject().getPhenoDataSetField().getName() }).getString());
				onSavePostProcess(target);
			}
			catch (ArkSystemException e) {
				this.error(new StringResourceModel("error.internalErrorMsg", this, null).getString());
				e.printStackTrace();
			}
			catch (ArkUniqueException e) {
				this.error(new StringResourceModel("error.nonUniqueCFMsg", this, null, new Object[] { getModelObject().getPhenoDataSetField().getName() }).getString());
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
			iPhenotypicService.deletePhenoDataSetField(getModelObject());
			this.info("Field " + getModelObject().getPhenoDataSetField().getName() + " was deleted successfully");
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
		if (getModelObject().getPhenoDataSetField().getId() == null) {
			return true;
		}
		else {
			return false;
		}

	}

	// Allow the model for the CustomFieldGroups to be assessed (but not allow it be to be set)
	public IModel<List<PhenoDataSetGroup>> getCfGroupDdcListModel() {
		return cfGroupDdcListModel;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see au.org.theark.core.web.form.AbstractDetailForm#addDetailFormComponents()
	 */
	@Override
	protected void addDetailFormComponents() {
		phenoDataSetDetailWMC = new WebMarkupContainer("phenoDataSetFieldDetailWMC");
		//Add new panels to handle the Unit Type changes.
		panelCustomUnitTypeDropDown=new WebMarkupContainer("panelCustomUnitTypeDropDown");
		panelCustomUnitTypeDropDown.setOutputMarkupId(true);
		panelCustomUnitTypeText=new WebMarkupContainer("panelCustomUnitTypeText");
		panelCustomUnitTypeText.setOutputMarkupId(true);
		//panelCustomFieldTypeDropDown = new WebMarkupContainer("paenlCustomFieldTypeDropDown");
		//panelCustomFieldTypeDropDown.setOutputMarkupId(true);
		if(arkModule.getName().equals(au.org.theark.core.Constants.ARK_MODULE_STUDY)){
			panelCustomUnitTypeDropDown.setVisible(false);
			panelCustomUnitTypeText.setVisible(true);
		}else{
			panelCustomUnitTypeDropDown.setVisible(true);
			panelCustomUnitTypeText.setVisible(false);
		}
		phenoDataSetDetailWMC.setOutputMarkupPlaceholderTag(true);
		phenoDataSetDetailWMC.add(fieldIdTxtFld.setEnabled(false)); // Disable ID field editing
		phenoDataSetDetailWMC.add(fieldNameTxtFld);
		phenoDataSetDetailWMC.add(fieldDescriptionTxtAreaFld);
		phenoDataSetDetailWMC.add(fieldTypeDdc);
		
		//panelCustomFieldTypeDropDown.add(phenoDataSetFieldTypeDdc);
		//phenoDataSetFieldDetailWMC.add(panelCustomFieldTypeDropDown);
		//Add category and order number.
		//categoryPanel.add(phenoDataSetCategoryDdc);
		//phenoDataSetFieldDetailWMC.add(categoryPanel);
		//orderNumberPanel.add(phenoDataSetCategoryOrderNoTxtFld);
		//phenoDataSetFieldDetailWMC.add(orderNumberPanel);
		//Unit type changes
		panelCustomUnitTypeDropDown.add(fieldUnitTypeDdc);
		phenoDataSetDetailWMC.add(panelCustomUnitTypeDropDown);
		panelCustomUnitTypeText.add(fieldUnitTypeTxtFld);
		phenoDataSetDetailWMC.add(panelCustomUnitTypeText);
		//End of Unit type changes.
		phenoDataSetDetailWMC.add(minMaxValueEntryWMC);
		phenoDataSetDetailWMC.add(fieldEncodedValuesTxtFld);
		//phenoDataSetFieldDetailWMC.add(fieldMissingValueTxtFld);
		phenoDataSetDetailWMC.add(missingValueEntryWMC);
		phenoDataSetDetailWMC.add(fieldLabelTxtAreaFld);
		phenoDataSetDetailWMC.add(defaultValueTextArea);
		phenoDataSetDisplayDetailWMC = new WebMarkupContainer("phenoDataSetFieldDisplayDetailWMC");
		phenoDataSetDisplayDetailWMC.add(phenoDataSetDisplayPositionPanel);
		phenoDataSetDisplayDetailWMC.add(fieldDisplayRequiredChkBox);
		phenoDataSetDisplayDetailWMC.add(fieldAllowMultiselectChkBox);
		
		// phenoDataSetFieldDisplayDetailWMC.add(fieldDisplayRequireMsgTxtAreaFld);
		// Only show these fields if necessary...
		if (getModelObject().isUsePhenoDataSetFieldDisplay() == false) {
			phenoDataSetDisplayDetailWMC.setVisible(false);
		}
		
		/*phenoDataSetCategoryDetailWMC= new WebMarkupContainer("phenoDataSetCategoryDetailWMC");
		phenoDataSetCategoryDetailWMC.setOutputMarkupPlaceholderTag(true);
		phenoDataSetCategoryDetailWMC.add(CustomFieldCategoryPalette);*/
		
		phenoDataSetListchoiceCategoryDetailWMC=new WebMarkupContainer("phenoDataSetListchoiceCategoryDetailWMC");
		phenoDataSetListchoiceCategoryDetailWMC.setOutputMarkupPlaceholderTag(true);
		phenoDataSetListchoiceCategoryDetailWMC.add(phenoDataSetCategoryAvailableListChoice);
		phenoDataSetListchoiceCategoryDetailWMC.add(addButtonFL);
		phenoDataSetListchoiceCategoryDetailWMC.add(removeButtonFL);
		phenoDataSetListchoiceCategoryDetailWMC.add(phenoDataSetCategoryFirstLevelListChoice);
		phenoDataSetListchoiceCategoryDetailWMC.add(addButtonLL);
		phenoDataSetListchoiceCategoryDetailWMC.add(removeButtonLL);
		phenoDataSetListchoiceCategoryDetailWMC.add(upButton);
		phenoDataSetListchoiceCategoryDetailWMC.add(downButton);
		phenoDataSetListchoiceCategoryDetailWMC.add(leftButton);
		phenoDataSetListchoiceCategoryDetailWMC.add(rightButton);
		phenoDataSetListchoiceCategoryDetailWMC.add(phenoDataSetCategoryLastLevelListChoice);
		// TODO: This 'addOrReplace' (instead of just 'add') is a temporary workaround due to the
		// detailPanelFormContainer being initialised only once at the top-level container panel.
		arkCrudContainerVO.getDetailPanelFormContainer().addOrReplace(phenoDataSetDetailWMC);
		arkCrudContainerVO.getDetailPanelFormContainer().addOrReplace(phenoDataSetDisplayDetailWMC);
		//arkCrudContainerVO.getDetailPanelFormContainer().addOrReplace(phenoDataSetCategoryDetailWMC);
		arkCrudContainerVO.getDetailPanelFormContainer().addOrReplace(phenoDataSetListchoiceCategoryDetailWMC);
		add(arkCrudContainerVO.getDetailPanelFormContainer());
	}
	
	/**
	 * Remove duplicates from list
	 * @param phenoDataSetFieldLst
	 * @return
	 */
	private  List<PhenoDataSetCategory> removeDuplicates(List<PhenoDataSetCategory> phenoDataSetFieldLst){
		Set<PhenoDataSetCategory> cusfieldCatSet=new HashSet<PhenoDataSetCategory>();
		List<PhenoDataSetCategory> cusfieldCatLst=new ArrayList<PhenoDataSetCategory>();
		cusfieldCatSet.addAll(phenoDataSetFieldLst);
		cusfieldCatLst.addAll(cusfieldCatSet);
				return cusfieldCatLst;
	}
	
	private  List<PhenoDataSetCategory> sortLst(List<PhenoDataSetCategory> phenoFieldLst){
		//sort by order number.
		Collections.sort(phenoFieldLst, new Comparator<PhenoDataSetCategory>(){
		    public int compare(PhenoDataSetCategory phenoFieldCat1, PhenoDataSetCategory phenoFieldFieldCat2) {
		        return phenoFieldCat1.getOrderNumber().compareTo(phenoFieldFieldCat2.getOrderNumber());
		    }
		});
				return phenoFieldLst;
	}
	
	/** This is for testing purpose please remove after that.
	 * Get phenoDataSet field category collection from model.
	 * @return
	 */
	private List<PhenoDataSetCategory> getAvailableAllCategoryListInStudy(){
		
		Study study =cpModel.getObject().getPhenoDataSetField().getStudy();
		ArkFunction arkFunction=iArkCommonService.getArkFunctionByName(au.org.theark.core.Constants.FUNCTION_KEY_VALUE_DATA_CATEGORY);
	
		Collection<PhenoDataSetCategory> phenoDataSetFieldCategoryCollection = null;
		try {
			phenoDataSetFieldCategoryCollection =  iPhenotypicService.getAvailableAllCategoryListInStudy(study,arkFunction);
		} catch (ArkSystemException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return (List<PhenoDataSetCategory>) phenoDataSetFieldCategoryCollection;
	}
	
	private List<PhenoDataSetCategory> addChildrenWithoutDuplicates(List<PhenoDataSetCategory> exsistingLst,List<PhenoDataSetCategory> childrenLst){
		for (PhenoDataSetCategory phenoDataSetCategory : childrenLst) {
			if(!exsistingLst.contains(phenoDataSetCategory)){
				exsistingLst.add(phenoDataSetCategory);
			}
		}
		return exsistingLst;
		
		
	}
	
	/** This is for testing purpose please remove after that.
	 * Get phenoDataSet field category collection from model.
	 * @return
	 */
	private List<PhenoDataSetCategory> getMySortedSiblingList(PhenoDataSetCategory phenoDataSetFieldCategory){
		
		Study study =cpModel.getObject().getPhenoDataSetField().getStudy();
		ArkFunction arkFunction=iArkCommonService.getArkFunctionByName(au.org.theark.core.Constants.FUNCTION_KEY_VALUE_DATA_CATEGORY);
		return sortLst(iPhenotypicService.getSiblingList(study, arkFunction, phenoDataSetFieldCategory));
		
	}
	/** This is for testing purpose please remove after that.
	 * Get pheno field category collection from model.
	 * @return
	 */
	private List<PhenoDataSetCategory> upButtonUpdatePhenoFieldCategoryOrder(List<PhenoDataSetCategory> sortedSlibingLst,PhenoDataSetCategory phenoDataSetCategory){
		PhenoDataSetCategory aboveSibling=sortedSlibingLst.get(sortedSlibingLst.indexOf(phenoDataSetCategory)-1);
		List<PhenoDataSetCategory> updateList=new ArrayList<PhenoDataSetCategory>(0);
		Long aboveSiblingOrdNumber=aboveSibling.getOrderNumber();
			aboveSibling.setOrderNumber(phenoDataSetCategory.getOrderNumber());
			phenoDataSetCategory.setOrderNumber(aboveSiblingOrdNumber);
			updateList.add(aboveSibling);
			try {
				iPhenotypicService.mergePhenoDataSetFieldCategory(aboveSibling);
			} catch (ArkSystemException | ArkRunTimeUniqueException
					| ArkRunTimeException e) {
				e.printStackTrace();
			}
			updateList.add(phenoDataSetCategory);
			try {
				iPhenotypicService.mergePhenoDataSetFieldCategory(phenoDataSetCategory);
			} catch (ArkSystemException | ArkRunTimeUniqueException
					| ArkRunTimeException e) {
				e.printStackTrace();
			}
			return updateList;
	}
	/** This is for testing purpose please remove after that.
	 * Get phenoDataSet field category collection from model.
	 * @return
	 */
	private List<PhenoDataSetCategory> downButtonUpdatePhenoFieldCategoryOrder(List<PhenoDataSetCategory> sortedSlibingLst,PhenoDataSetCategory phenoDataSetCategory){
		PhenoDataSetCategory benethSibling=sortedSlibingLst.get(sortedSlibingLst.indexOf(phenoDataSetCategory)+1);
		List<PhenoDataSetCategory> updateList=new ArrayList<PhenoDataSetCategory>(0);
		Long benethSiblingOrdNumber=benethSibling.getOrderNumber();
			benethSibling.setOrderNumber(phenoDataSetCategory.getOrderNumber());
			phenoDataSetCategory.setOrderNumber(benethSiblingOrdNumber);
			updateList.add(benethSibling);
			try {
				iPhenotypicService.mergePhenoDataSetFieldCategory(benethSibling);
			} catch (ArkSystemException | ArkRunTimeUniqueException
					| ArkRunTimeException e) {
				e.printStackTrace();
			}
			updateList.add(phenoDataSetCategory);
			try {
				iPhenotypicService.mergePhenoDataSetFieldCategory(phenoDataSetCategory);
			} catch (ArkSystemException | ArkRunTimeUniqueException
					| ArkRunTimeException e) {
				e.printStackTrace();
			}
			return updateList;
	}
	
	
	

}
