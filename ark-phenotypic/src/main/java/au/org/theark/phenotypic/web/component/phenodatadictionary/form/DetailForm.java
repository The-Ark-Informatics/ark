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
import org.apache.wicket.markup.ComponentTag;
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
import au.org.theark.core.vo.ArkCrudContainerVO;
import au.org.theark.core.vo.CustomFieldCategoryVO;
import au.org.theark.core.vo.CustomFieldVO;
import au.org.theark.core.web.behavior.ArkDefaultFormFocusBehavior;
import au.org.theark.core.web.component.audit.button.HistoryButtonPanel;
import au.org.theark.core.web.component.customfield.dataentry.DateDataEntryPanel;
import au.org.theark.core.web.component.customfield.dataentry.NumberDataEntryPanel;
import au.org.theark.core.web.component.customfield.dataentry.StringDateModel;
import au.org.theark.core.web.component.customfield.dataentry.TextDataEntryPanel;
import au.org.theark.core.web.form.AbstractDetailForm;
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
public class DetailForm extends AbstractDetailForm<CustomFieldVO> {
	private static final Logger				log	= LoggerFactory.getLogger(DetailForm.class);
	@SpringBean(name = au.org.theark.core.Constants.ARK_COMMON_SERVICE)
	private IArkCommonService<Void>				iArkCommonService;

	private int											mode;

	private TextField<String>						fieldIdTxtFld;
	private TextField<String>						fieldNameTxtFld;
	private DropDownChoice<FieldType>				fieldTypeDdc;
	private TextArea<String>						fieldDescriptionTxtAreaFld;
	private DropDownChoice<UnitType>				fieldUnitTypeDdc;
	//private DropDownChoice<CustomFieldCategory>		customeFieldCategoryDdc;
	//private DropDownChoice<CustomFieldType>			customFieldTypeDdc;
	//Add unit type as text
	private TextField<String>						fieldUnitTypeTxtFld;
	//****************//
	private TextArea<String>						fieldEncodedValuesTxtFld;
	//private TextField<String>						fieldMissingValueTxtFld;

	private TextArea<String>						fieldLabelTxtAreaFld;
	private CheckBox								fieldDisplayRequiredChkBox;
	private CheckBox								fieldAllowMultiselectChkBox;
	private DropDownChoice<CustomFieldGroup>		fieldDisplayFieldGroupDdc;

	protected WebMarkupContainer					customFieldDetailWMC;
	protected WebMarkupContainer					minMaxValueEntryWMC;
	protected WebMarkupContainer					missingValueEntryWMC;
	
	protected WebMarkupContainer					customFieldDisplayDetailWMC;
	protected Panel									customFieldDisplayPositionPanel;
	protected Panel									minValueEntryPnl;
	protected Panel									maxValueEntryPnl;
	protected Panel									missingValueEntryPnl;

	private TextArea<String>						defaultValueTextArea;
	
	protected IModel<List<CustomFieldGroup>>		cfGroupDdcListModel;
	
	//New two webMarkupContainers to hold different unit types DropDown and Text.
	private WebMarkupContainer  					panelCustomUnitTypeDropDown;
	private WebMarkupContainer  					panelCustomUnitTypeText;
	//private WebMarkupContainer  					panelCustomFieldTypeDropDown;
	//private boolean 								unitTypeDropDownOn;
	//private boolean 								subjectCustomField;
	
	private HistoryButtonPanel 						historyButtonPanel;
	private TextField<Long>							customeFieldCategoryOrderNoTxtFld;
	//private  WebMarkupContainer						categoryPanel;
	//private  WebMarkupContainer						orderNumberPanel;
	private ArkModule 								arkModule;
	
	//private Collection<CustomFieldCategory> customFieldCategoryCollection;
	//protected  WebMarkupContainer 					phenoDataSetCategoryDetailWMC;
	//private Palette<CustomFieldCategory>			CustomFieldCategoryPalette;
	private Study study;
	private ArkFunction arkFunction;
	private CustomFieldType customFieldType;
	private List<CustomFieldCategory> allChildrenCategories=new ArrayList<CustomFieldCategory>();
	
	protected  WebMarkupContainer 					phenoDataSetListchoiceCategoryDetailWMC;
	private ListMultipleChoice<CustomFieldCategory> customFieldCategoryAvailableListChoice; 
	private Button addButtonFL,removeButtonFL,addButtonLL,removeButtonLL,upButton,downButton,leftButton,rightButton;
	private ListMultipleChoice<CustomFieldCategory> customFieldCategoryFirstLevelListChoice; 
	private ListMultipleChoice<CustomFieldCategory> customFieldCategoryLastLevelListChoice;
	
	
	/*= new ListMultipleChoice<String>(
			"number", new Model(selectedNumber), NUMBERS);
*/
	/**
	 * Constructor
	 * 
	 * @param id
	 * @param cpModel
	 * @param feedBackPanel
	 * @param arkCrudContainerVO
	 */
	public DetailForm(String id, CompoundPropertyModel<CustomFieldVO> cpModel, FeedbackPanel feedBackPanel, ArkCrudContainerVO arkCrudContainerVO){
		super(id, feedBackPanel, cpModel, arkCrudContainerVO);
		cfGroupDdcListModel = new ListModel<CustomFieldGroup>();
		refreshEntityFromBackend();
		Long sessionModuleId = (Long) SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.core.Constants.ARK_MODULE_KEY);
		
		study =cpModel.getObject().getCustomField().getStudy();
		arkFunction=iArkCommonService.getArkFunctionByName(au.org.theark.core.Constants.FUNCTION_KEY_VALUE_SUBJECT_CUSTOM_FIELD_CATEGORY);
		customFieldType=iArkCommonService.getCustomFieldTypeByName(au.org.theark.core.Constants.SUBJECT);
		
	}

	protected void refreshEntityFromBackend() {
		// Refresh the entity from the backend
		if (getModelObject().getCustomField().getId() != null) {
			CustomField cfFromBackend = iArkCommonService.getCustomField(getModelObject().getCustomField().getId());
			getModelObject().setCustomField(cfFromBackend);

			CustomFieldDisplay cfdFromBackend;
			cfdFromBackend = iArkCommonService.getCustomFieldDisplayByCustomField(cfFromBackend);
			getModelObject().setCustomFieldDisplay(cfdFromBackend);
		}
		if (getModelObject().isUseCustomFieldDisplay() == true) {
			// Ensure the customFieldDisplay.require is never NULL
			if (getModelObject().getCustomFieldDisplay().getRequired() == null) {
				getModelObject().getCustomFieldDisplay().setRequired(false);
			}
		}
	}
	/**
	 * initialise Field Types.
	 */
	private void initFieldTypeDdc() {
		List<FieldType> fieldTypeCollection = iArkCommonService.getFieldTypes();
		ChoiceRenderer fieldTypeRenderer = new ChoiceRenderer(Constants.FIELDTYPE_NAME, Constants.FIELDTYPE_ID);
		fieldTypeDdc = new DropDownChoice<FieldType>(Constants.FIELDVO_CUSTOMFIELD_FIELD_TYPE, fieldTypeCollection, fieldTypeRenderer) {
			@Override
			protected void onBeforeRender() {
				if (!isNew()) {
					// Disable fieldType if data exists for the field
					setEnabled(!cpModel.getObject().getCustomField().getCustomFieldHasData());
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
		List<CustomFieldCategory> customFieldCategories = new ArrayList<CustomFieldCategory>(0);
		customFieldCategories= CustomFieldCategoryOrderingHelper.getInstance().orderHierarchicalyCustomFieldCategories(getAvailableAllCategoryListInStudyByCustomFieldType());
		cpModel.getObject().setCustomFieldCategoryLst(customFieldCategories);
		IChoiceRenderer<String> renderer = new ChoiceRenderer("name", "id"){
			@Override
			public Object getDisplayValue(Object object) {
			CustomFieldCategory cuscat=(CustomFieldCategory)object;
				return CustomFieldCategoryOrderingHelper.getInstance().preTextDecider(cuscat)+ super.getDisplayValue(object);
			}
		};
		PropertyModel<List<CustomFieldCategory>> availableCategories = new PropertyModel<List<CustomFieldCategory>>(cpModel, "customFieldCategoryLst");
		PropertyModel<List<CustomFieldCategory>> selectedCategories = new PropertyModel<List<CustomFieldCategory>>(cpModel, "selectedCategories");
		PropertyModel<List<CustomFieldCategory>> firstLevelAvailableCategories = new PropertyModel<List<CustomFieldCategory>>(cpModel, "firstLevelAvailableCategories");
		PropertyModel<List<CustomFieldCategory>> firstLevelSelectedCategories = new PropertyModel<List<CustomFieldCategory>>(cpModel, "firstLevelSelectedCategories");
		PropertyModel<List<CustomFieldCategory>> lastLevelAvailableCategories = new PropertyModel<List<CustomFieldCategory>>(cpModel, "lastLevelAvailableCategories");
		PropertyModel<List<CustomFieldCategory>> lastLevelSelectedCategories = new PropertyModel<List<CustomFieldCategory>>(cpModel, "lastLevelSelectedCategories");
		
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
			PropertyModel<List<CustomFieldCategory>> availableCategories,
			PropertyModel<List<CustomFieldCategory>> selectedCategories) {
		customFieldCategoryAvailableListChoice=new ListMultipleChoice("customFieldCategoryLst", selectedCategories,availableCategories,renderer);
		customFieldCategoryAvailableListChoice.add(new AjaxFormComponentUpdatingBehavior("onchange") {
            protected void onUpdate(AjaxRequestTarget target) {
            	cpModel.getObject().setSelectedCategories(selectedCategories.getObject());
            	target.add(customFieldCategoryAvailableListChoice);
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
			PropertyModel<List<CustomFieldCategory>> firstLevelAvailableCategories,
			PropertyModel<List<CustomFieldCategory>> firstLevelSelectedCategories) {
		customFieldCategoryFirstLevelListChoice=new ListMultipleChoice("firstLevelAvailableCategories", firstLevelSelectedCategories,firstLevelAvailableCategories,renderer);
		customFieldCategoryFirstLevelListChoice.setOutputMarkupId(true);
		customFieldCategoryFirstLevelListChoice.add(new AjaxFormComponentUpdatingBehavior("onchange") {
            protected void onUpdate(AjaxRequestTarget target) {
            	cpModel.getObject().setFirstLevelSelectedCategories(firstLevelSelectedCategories.getObject());
            	target.add(customFieldCategoryFirstLevelListChoice);
            }
            });
	}
	
	private void lastLevelAvailableCategories(IChoiceRenderer<String> renderer,
			PropertyModel<List<CustomFieldCategory>> lastLevelAvailableCategories,
			PropertyModel<List<CustomFieldCategory>> lastLevelSelectedCategories) {
		customFieldCategoryLastLevelListChoice=new ListMultipleChoice("lastLevelAvailableCategories", lastLevelSelectedCategories,lastLevelAvailableCategories,renderer);
		customFieldCategoryLastLevelListChoice.add(new AjaxFormComponentUpdatingBehavior("onchange") {
            protected void onUpdate(AjaxRequestTarget target) {
            	cpModel.getObject().setLastLevelSelectedCategories(lastLevelSelectedCategories.getObject());
            	target.add(customFieldCategoryLastLevelListChoice);
            }
            });
		
	}
	
	/**
	 * First level add button.
	 * @param selectedCategories
	 */
	private void addButtonFirstLevel(
			PropertyModel<List<CustomFieldCategory>> selectedCategories) {
		addButtonFL = new AjaxButton("addButtonFL") {
			private static final long serialVersionUID = 1L;
			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
				List<CustomFieldCategory> clickAndSelectedCategories=selectedCategories.getObject();
				for (CustomFieldCategory customFieldCategory : clickAndSelectedCategories) {
            		allChildrenCategories=removeDuplicates(iArkCommonService.getAllChildrenCategoriedBelongToThisParent(study, arkFunction, customFieldType, customFieldCategory,allChildrenCategories));
				}
					List<CustomFieldCategory> alreadyInFirstLevelCategories=cpModel.getObject().getFirstLevelAvailableCategories();
            		List<CustomFieldCategory> allFirstLevel=addChildrenWithoutDuplicates(clickAndSelectedCategories, allChildrenCategories);
            		cpModel.getObject().setFirstLevelAvailableCategories(CustomFieldCategoryOrderingHelper.getInstance().orderHierarchicalyCustomFieldCategories(addChildrenWithoutDuplicates(alreadyInFirstLevelCategories, allFirstLevel)));
            		cpModel.getObject().getCustomFieldCategoryLst().removeAll(allFirstLevel);
            		allChildrenCategories.clear();
            	    target.add(customFieldCategoryAvailableListChoice);
                    target.add(customFieldCategoryFirstLevelListChoice);
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
			PropertyModel<List<CustomFieldCategory>> firstLevelSelectedCategories) {
		removeButtonFL = new AjaxButton("removeButtonFL") {
			private static final long serialVersionUID = 1L;
			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
				List<CustomFieldCategory> clickAndSelectedCategories=firstLevelSelectedCategories.getObject();
				for (CustomFieldCategory customFieldCategory : clickAndSelectedCategories) {
            		allChildrenCategories=removeDuplicates(iArkCommonService.getAllChildrenCategoriedBelongToThisParent(study, arkFunction, customFieldType, customFieldCategory,allChildrenCategories));
				}
					List<CustomFieldCategory> currentlyAvailable=cpModel.getObject().getCustomFieldCategoryLst();
					List<CustomFieldCategory> childAndAvailable=addChildrenWithoutDuplicates(currentlyAvailable,allChildrenCategories);
					List<CustomFieldCategory> allAvailable=addChildrenWithoutDuplicates(childAndAvailable,clickAndSelectedCategories);
					cpModel.getObject().setCustomFieldCategoryLst(CustomFieldCategoryOrderingHelper.getInstance().orderHierarchicalyCustomFieldCategories(allAvailable));
            		cpModel.getObject().getFirstLevelAvailableCategories().removeAll(allChildrenCategories);
            		cpModel.getObject().getFirstLevelAvailableCategories().removeAll(clickAndSelectedCategories);
            		allChildrenCategories.clear();
            	    target.add(customFieldCategoryAvailableListChoice);
                    target.add(customFieldCategoryFirstLevelListChoice);
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
			PropertyModel<List<CustomFieldCategory>> firstLevelSelectedCategories) {
		addButtonLL = new AjaxButton("addButtonLL") {
			private static final long serialVersionUID = 1L;
			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
					List<CustomFieldCategory> initialSelectedCategories=firstLevelSelectedCategories.getObject();
					List<CustomFieldCategory> currentlyAvailable=cpModel.getObject().getLastLevelAvailableCategories();
					List<CustomFieldCategory> allAvailable=addChildrenWithoutDuplicates(currentlyAvailable,initialSelectedCategories);
					cpModel.getObject().setLastLevelAvailableCategories(CustomFieldCategoryOrderingHelper.getInstance().orderHierarchicalyCustomFieldCategories(allAvailable));
		    		cpModel.getObject().getFirstLevelAvailableCategories().removeAll(initialSelectedCategories);
            	    target.add(customFieldCategoryFirstLevelListChoice);
            	    target.add(customFieldCategoryLastLevelListChoice);
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
			PropertyModel<List<CustomFieldCategory>> lastLevelSelectedCategories) {
		removeButtonLL = new AjaxButton("removeButtonLL") {
			private static final long serialVersionUID = 1L;
			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
				List<CustomFieldCategory> initialSelectedCategories=lastLevelSelectedCategories.getObject();
					List<CustomFieldCategory> currentlyAvailable=cpModel.getObject().getFirstLevelAvailableCategories();
					List<CustomFieldCategory> allAvailable=addChildrenWithoutDuplicates(currentlyAvailable,initialSelectedCategories);
					cpModel.getObject().setFirstLevelAvailableCategories(CustomFieldCategoryOrderingHelper.getInstance().orderHierarchicalyCustomFieldCategories(allAvailable));
            		cpModel.getObject().getLastLevelAvailableCategories().removeAll(initialSelectedCategories);
            	    target.add(customFieldCategoryFirstLevelListChoice);
                    target.add(customFieldCategoryLastLevelListChoice);
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
			PropertyModel<List<CustomFieldCategory>> firstLevelSelectedCategories) {
			upButton = new AjaxButton("upButton") {
			private static final long serialVersionUID = 1L;
			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
				List<CustomFieldCategory> initialSelectedCategories=firstLevelSelectedCategories.getObject();
				CustomFieldCategory selectedCategory = null;
				List<CustomFieldCategory> availableCats=cpModel.getObject().getFirstLevelAvailableCategories();
				if(initialSelectedCategories.size()==1){
					selectedCategory=initialSelectedCategories.get(0);
					if(selectedCategory.getParentCategory()!=null){
						List<CustomFieldCategory> sortedLst=getMySortedSiblingList(selectedCategory);
						if(sortedLst.indexOf(selectedCategory)!=0){
							List<CustomFieldCategory> updateList=upButtonUpdateCustomfieldCategoryOrder(sortedLst,selectedCategory);
								for (CustomFieldCategory item : updateList) {
									if(availableCats.contains(item)){
										availableCats.get(availableCats.indexOf(item)).setOrderNumber(item.getOrderNumber());
									}
								}
							cpModel.getObject().setFirstLevelAvailableCategories(CustomFieldCategoryOrderingHelper.getInstance().orderHierarchicalyCustomFieldCategories(availableCats));
						}else{
							//top level selected
						}
					}else{
						//parent selected
					}
				}else{
					//more than one selected.
				}	
				target.add(customFieldCategoryAvailableListChoice);
				target.add(customFieldCategoryFirstLevelListChoice);
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
			PropertyModel<List<CustomFieldCategory>> firstLevelSelectedCategories) {
		downButton = new AjaxButton("downButton") {
			private static final long serialVersionUID = 1L;
			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
				List<CustomFieldCategory> initialSelectedCategories=firstLevelSelectedCategories.getObject();
				CustomFieldCategory selectedCategory = null;
				List<CustomFieldCategory> availableCats=cpModel.getObject().getFirstLevelAvailableCategories();
				if(initialSelectedCategories.size()==1){
					selectedCategory=initialSelectedCategories.get(0);
					if(selectedCategory.getParentCategory()!=null){
						List<CustomFieldCategory> sortedLst=getMySortedSiblingList(selectedCategory);
						if(sortedLst.indexOf(selectedCategory)< (sortedLst.size()-1)){
							List<CustomFieldCategory> updateList=downButtonUpdateCustomfieldCategoryOrder(sortedLst,selectedCategory);
								for (CustomFieldCategory item : updateList) {
									if(availableCats.contains(item)){
										availableCats.get(availableCats.indexOf(item)).setOrderNumber(item.getOrderNumber());
									}
								}
							cpModel.getObject().setFirstLevelAvailableCategories(CustomFieldCategoryOrderingHelper.getInstance().orderHierarchicalyCustomFieldCategories(availableCats));
						}else{
							//bottom level selected
						}
					}else{
						//parent selected
					}
				}else{
					//more than one selected.
				}
				target.add(customFieldCategoryAvailableListChoice);
				target.add(customFieldCategoryFirstLevelListChoice);
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
			PropertyModel<List<CustomFieldCategory>> firstLevelSelectedCategories) {
			leftButton = new AjaxButton("leftButton") {
			private static final long serialVersionUID = 1L;
			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
				List<CustomFieldCategory> initialSelectedCategories=firstLevelSelectedCategories.getObject();
				CustomFieldCategory selectedCategory = null;
				List<CustomFieldCategory> availableCats=cpModel.getObject().getFirstLevelAvailableCategories();
				if(initialSelectedCategories.size()==1){
					selectedCategory=initialSelectedCategories.get(0);
					if(selectedCategory.getParentCategory()!=null){
						if(availableCats.indexOf(selectedCategory)!=0){
							if(selectedCategory.getParentCategory().getParentCategory()!=null)
								selectedCategory.setParentCategory(selectedCategory.getParentCategory().getParentCategory());
							else{
								selectedCategory.setParentCategory(null);
							}
							CustomFieldCategoryVO customFieldCategoryVO=new CustomFieldCategoryVO();
							customFieldCategoryVO.setCustomFieldCategory(selectedCategory);
							try {
								iArkCommonService.updateCustomFieldCategory(customFieldCategoryVO);
							} catch (ArkSystemException| ArkRunTimeUniqueException| ArkRunTimeException | ArkUniqueException e) {
								e.printStackTrace();
							}
						}
						
					}else{
						//no parent category so no left function.
					}
					cpModel.getObject().setFirstLevelAvailableCategories(CustomFieldCategoryOrderingHelper.getInstance().orderHierarchicalyCustomFieldCategories(availableCats));
				}else{
					//more than one selected.
				}	
				target.add(customFieldCategoryAvailableListChoice);
				target.add(customFieldCategoryFirstLevelListChoice);
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
			PropertyModel<List<CustomFieldCategory>> firstLevelSelectedCategories) {
			rightButton = new AjaxButton("rightButton") {
			private static final long serialVersionUID = 1L;
			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
				List<CustomFieldCategory> initialSelectedCategories=firstLevelSelectedCategories.getObject();
				CustomFieldCategory selectedCategory = null;
				List<CustomFieldCategory> availableCats=cpModel.getObject().getFirstLevelAvailableCategories();
				if(initialSelectedCategories.size()==1){
					selectedCategory=initialSelectedCategories.get(0);
					//if(selectedCategory.getParentCategory()!=null){
						if(availableCats.indexOf(selectedCategory)!=0){
							selectedCategory.setParentCategory(availableCats.get(availableCats.indexOf(selectedCategory)-1));
							CustomFieldCategoryVO customFieldCategoryVO=new CustomFieldCategoryVO();
							customFieldCategoryVO.setCustomFieldCategory(selectedCategory);
							try {
								iArkCommonService.updateCustomFieldCategory(customFieldCategoryVO);
							} catch (ArkSystemException| ArkRunTimeUniqueException| ArkRunTimeException | ArkUniqueException e) {
								e.printStackTrace();
							}
						}
						
				//	}else{
						//no parent category
				//	}
					cpModel.getObject().setFirstLevelAvailableCategories(CustomFieldCategoryOrderingHelper.getInstance().orderHierarchicalyCustomFieldCategories(availableCats));
						
				}else{
					//more than one selected.
				}	
				target.add(customFieldCategoryAvailableListChoice);
				target.add(customFieldCategoryFirstLevelListChoice);
				super.onSubmit(target, form);
			}
		};
		rightButton.setDefaultFormProcessing(false);
	}
	

	private void updateEncodedValueFld() {
		FieldType fieldType = getModelObject().getCustomField().getFieldType();
		if (fieldType != null && fieldType.getName().equals(Constants.CHARACTER_FIELD_TYPE_NAME)) {
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
		FieldType fieldType = getModelObject().getCustomField().getFieldType();
		if (fieldType != null && !fieldType.getName().equals(Constants.DATE_FIELD_TYPE_NAME)) {
			// Only allowed to use unitType when fieldType != DATE
			fieldUnitTypeDdc.setEnabled(true);
			fieldUnitTypeTxtFld.setEnabled(true);
		}
		else {
			fieldUnitTypeDdc.setEnabled(false);
			fieldUnitTypeTxtFld.setEnabled(false);
		}
	}


	private void updateCategoryDdc() {
		CustomFieldCategory category = getModelObject().getCustomField().getCustomFieldCategory();
		/*if (category != null && !category.getName().equals(Constants.DATE_FIELD_TYPE_NAME)) {
			// Only allowed to use unitType when fieldType != DATE
			fieldUnitTypeDdc.setEnabled(true);
			fieldUnitTypeTxtFld.setEnabled(true);
		}
		else {
			fieldUnitTypeDdc.setEnabled(false);
			fieldUnitTypeTxtFld.setEnabled(false);
		}*/
		//TODO dependencies?
		//have to figure out what dependencies exist
	}
	/**
	 * initialise max and min values.
	 */
	private void initMinMaxValuePnls() {
		FieldType fieldType = getModelObject().getCustomField().getFieldType();
		
			if (fieldType == null || fieldType.getName().equals(Constants.CHARACTER_FIELD_TYPE_NAME) 
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
				IModel<String> missingValueMdl = new PropertyModel<String>(getModelObject(), Constants.FIELDVO_CUSTOMFIELD_MISSING_VALUE);
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
			else if (fieldType.getName().equals(Constants.NUMBER_FIELD_TYPE_NAME)) {
				// NUMBER fieldType
				IModel<Double> minValueMdl = new PropertyModel<Double>(getModelObject(), Constants.FIELDVO_CUSTOMFIELD_MIN_VALUE);
				IModel<Double> maxValueMdl = new PropertyModel<Double>(getModelObject(), Constants.FIELDVO_CUSTOMFIELD_MAX_VALUE);
				IModel<Double> missingValueMdl = new PropertyModel<Double>(getModelObject(), Constants.FIELDVO_CUSTOMFIELD_MISSING_VALUE);
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
			else if (fieldType.getName().equals(Constants.DATE_FIELD_TYPE_NAME)) {
				// DATE fieldType
				IModel<Date> minValueMdl = new StringDateModel(new PropertyModel<String>(getModelObject(), Constants.FIELDVO_CUSTOMFIELD_MIN_VALUE), au.org.theark.core.Constants.DD_MM_YYYY);
				IModel<Date> maxValueMdl = new StringDateModel(new PropertyModel<String>(getModelObject(), Constants.FIELDVO_CUSTOMFIELD_MAX_VALUE), au.org.theark.core.Constants.DD_MM_YYYY);
				IModel<Date> missingValueMdl = new StringDateModel(new PropertyModel<String>(getModelObject(), Constants.FIELDVO_CUSTOMFIELD_MISSING_VALUE), au.org.theark.core.Constants.DD_MM_YYYY);
				
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
		unitTypeCriteria.setArkFunction(cpModel.getObject().getCustomField().getArkFunction());
		List<UnitType> unitTypeList = iArkCommonService.getUnitTypes(unitTypeCriteria);
		// assumes that if the unit.name will appear within the unit.description

		ChoiceRenderer unitTypeRenderer = new ChoiceRenderer(Constants.UNITTYPE_DESCRIPTION, Constants.UNITTYPE_ID);
		fieldUnitTypeDdc = new DropDownChoice<UnitType>(Constants.FIELDVO_CUSTOMFIELD_UNIT_TYPE, unitTypeList, unitTypeRenderer);
		fieldUnitTypeDdc.setNullValid(true); // null is ok for units
		fieldUnitTypeDdc.setOutputMarkupId(true); // unitTypeDdc can be enabled/disabled
		
		//Add the Unit type text.
		fieldUnitTypeTxtFld=new TextField<String>(Constants.FIELDVO_CUSTOMFIELD_UNIT_TYPE_TXT);
		fieldUnitTypeTxtFld.setOutputMarkupId(true);
		
	}

	/**
	 * Call this after the constructor is finished in detail panel.
	 */
	public void initialiseDetailForm() {
		Long sessionModuleId = (Long) SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.core.Constants.ARK_MODULE_KEY);
		arkModule=iArkCommonService.getArkModuleById(sessionModuleId);
		fieldIdTxtFld = new TextField<String>(Constants.FIELDVO_CUSTOMFIELD_ID);
		fieldNameTxtFld = new TextField<String>(Constants.FIELDVO_CUSTOMFIELD_NAME);
		fieldNameTxtFld.add(new ArkDefaultFormFocusBehavior());
		fieldDescriptionTxtAreaFld = new TextArea<String>(Constants.FIELDVO_CUSTOMFIELD_DESCRIPTION);
		fieldLabelTxtAreaFld = new TextArea<String>(Constants.FIELDVO_CUSTOMFIELD_FIELD_LABEL);
		//fieldMissingValueTxtFld = new TextField<String>(Constants.FIELDVO_CUSTOMFIELD_MISSING_VALUE);
		fieldDisplayRequiredChkBox = new CheckBox(Constants.FIELDVO_CUSTOMFIELDDISPLAY_REQUIRED);
		fieldAllowMultiselectChkBox = new CheckBox(Constants.FIELDVO_CUSTOMFIELD_ALLOW_MULTISELECT) {
		private static final long	serialVersionUID	= 1L;

			@Override
			public boolean isEnabled() {
				return (fieldEncodedValuesTxtFld.getModelObject() != null);
			}
		};

		if (getModelObject().isUseCustomFieldDisplay()) {
			// TODO: Have not implemented position support right now
			customFieldDisplayPositionPanel = new EmptyPanel("customFieldDisplayPositionPanel");
		}
		else {
			customFieldDisplayPositionPanel = new EmptyPanel("customFieldDisplayPositionPanel");
		}
		//customfield category order Number.
		customeFieldCategoryOrderNoTxtFld = new TextField<Long>(Constants.FIELDVO_CUSTOMFIELD_CUSTOEMFIELDCATEGORY_ORDERNUMBER);
		customeFieldCategoryOrderNoTxtFld.setOutputMarkupId(true);
		customeFieldCategoryOrderNoTxtFld.setEnabled(false);
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
		fieldEncodedValuesTxtFld = new TextArea<String>(Constants.FIELDVO_CUSTOMFIELD_ENCODED_VALUES);
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

		defaultValueTextArea = new TextArea<String>("customField.defaultValue");
		
		addDetailFormComponents();
		attachValidators();

		initMinMaxValuePnls();
		
		historyButtonPanel = new HistoryButtonPanel(this, arkCrudContainerVO.getEditButtonContainer(), arkCrudContainerVO.getDetailPanelFormContainer());
	}
	
	protected void attachValidators() {
		fieldNameTxtFld.setRequired(true).setLabel((new StringResourceModel("customField.name.required", this, new Model<String>("Custom Field Name"))));
		// Enforce particular characters for fieldName
		fieldNameTxtFld.add(new PatternValidator("[a-zA-Z0-9_-]+"));
		fieldTypeDdc.setRequired(true).setLabel((new StringResourceModel("customField.fieldType.required", this, new Model<String>("Custom Field Type"))));
		fieldLabelTxtAreaFld.add(StringValidator.maximumLength(255));

		// TODO: Add correct validator, possibly custom with better validation message
		fieldEncodedValuesTxtFld.add(new PatternValidator(au.org.theark.core.Constants.ENCODED_VALUES_PATTERN)).setLabel(
				new StringResourceModel("customField.encodedValues.validation", this, new Model<String>("Encoded Value definition")));
	}

	@Override
	protected void onSave(Form<CustomFieldVO> containerForm, AjaxRequestTarget target) {
		// NB: creating/updating the customFieldDisplay will be tied to the customField by the service
		if (getModelObject().getCustomField().getId() == null) {
			// Save the Field
			try {
				iArkCommonService.createCustomField(getModelObject());
				this.info(new StringResourceModel("info.createSuccessMsg", this, null, new Object[] { getModelObject().getCustomField().getName() }).getString());
				onSavePostProcess(target);
			}
			catch (ArkSystemException e) {
				this.error(new StringResourceModel("error.internalErrorMsg", this, null).getString());
				e.printStackTrace();
			}
			catch (ArkUniqueException e) {
				this.error(new StringResourceModel("error.nonUniqueCFMsg", this, null, new Object[] { getModelObject().getCustomField().getName() }).getString());
				e.printStackTrace();
			}
			processErrors(target);
		}
		else {
			// Update the Field
			try {
				iArkCommonService.updateCustomField(getModelObject());
				this.info(new StringResourceModel("info.updateSuccessMsg", this, null, new Object[] { getModelObject().getCustomField().getName() }).getString());
				onSavePostProcess(target);
			}
			catch (ArkSystemException e) {
				this.error(new StringResourceModel("error.internalErrorMsg", this, null).getString());
				e.printStackTrace();
			}
			catch (ArkUniqueException e) {
				this.error(new StringResourceModel("error.nonUniqueCFMsg", this, null, new Object[] { getModelObject().getCustomField().getName() }).getString());
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
			iArkCommonService.deleteCustomField(getModelObject());
			this.info("Field " + getModelObject().getCustomField().getName() + " was deleted successfully");
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
		if (getModelObject().getCustomField().getId() == null) {
			return true;
		}
		else {
			return false;
		}

	}

	// Allow the model for the CustomFieldGroups to be assessed (but not allow it be to be set)
	public IModel<List<CustomFieldGroup>> getCfGroupDdcListModel() {
		return cfGroupDdcListModel;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see au.org.theark.core.web.form.AbstractDetailForm#addDetailFormComponents()
	 */
	@Override
	protected void addDetailFormComponents() {
		customFieldDetailWMC = new WebMarkupContainer("customFieldDetailWMC");
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
		customFieldDetailWMC.setOutputMarkupPlaceholderTag(true);
		customFieldDetailWMC.add(fieldIdTxtFld.setEnabled(false)); // Disable ID field editing
		customFieldDetailWMC.add(fieldNameTxtFld);
		customFieldDetailWMC.add(fieldDescriptionTxtAreaFld);
		customFieldDetailWMC.add(fieldTypeDdc);
		
		//panelCustomFieldTypeDropDown.add(customFieldTypeDdc);
		//customFieldDetailWMC.add(panelCustomFieldTypeDropDown);
		//Add category and order number.
		//categoryPanel.add(customeFieldCategoryDdc);
		//customFieldDetailWMC.add(categoryPanel);
		//orderNumberPanel.add(customeFieldCategoryOrderNoTxtFld);
		//customFieldDetailWMC.add(orderNumberPanel);
		//Unit type changes
		panelCustomUnitTypeDropDown.add(fieldUnitTypeDdc);
		customFieldDetailWMC.add(panelCustomUnitTypeDropDown);
		panelCustomUnitTypeText.add(fieldUnitTypeTxtFld);
		customFieldDetailWMC.add(panelCustomUnitTypeText);
		//End of Unit type changes.
		customFieldDetailWMC.add(minMaxValueEntryWMC);
		customFieldDetailWMC.add(fieldEncodedValuesTxtFld);
		//customFieldDetailWMC.add(fieldMissingValueTxtFld);
		customFieldDetailWMC.add(missingValueEntryWMC);
		customFieldDetailWMC.add(fieldLabelTxtAreaFld);
		customFieldDetailWMC.add(defaultValueTextArea);
		customFieldDisplayDetailWMC = new WebMarkupContainer("customFieldDisplayDetailWMC");
		customFieldDisplayDetailWMC.add(customFieldDisplayPositionPanel);
		customFieldDisplayDetailWMC.add(fieldDisplayRequiredChkBox);
		customFieldDisplayDetailWMC.add(fieldAllowMultiselectChkBox);
		
		// customFieldDisplayDetailWMC.add(fieldDisplayRequireMsgTxtAreaFld);
		// Only show these fields if necessary...
		if (getModelObject().isUseCustomFieldDisplay() == false) {
			customFieldDisplayDetailWMC.setVisible(false);
		}
		
		/*phenoDataSetCategoryDetailWMC= new WebMarkupContainer("phenoDataSetCategoryDetailWMC");
		phenoDataSetCategoryDetailWMC.setOutputMarkupPlaceholderTag(true);
		phenoDataSetCategoryDetailWMC.add(CustomFieldCategoryPalette);*/
		
		phenoDataSetListchoiceCategoryDetailWMC=new WebMarkupContainer("phenoDataSetListchoiceCategoryDetailWMC");
		phenoDataSetListchoiceCategoryDetailWMC.setOutputMarkupPlaceholderTag(true);
		phenoDataSetListchoiceCategoryDetailWMC.add(customFieldCategoryAvailableListChoice);
		phenoDataSetListchoiceCategoryDetailWMC.add(addButtonFL);
		phenoDataSetListchoiceCategoryDetailWMC.add(removeButtonFL);
		phenoDataSetListchoiceCategoryDetailWMC.add(customFieldCategoryFirstLevelListChoice);
		phenoDataSetListchoiceCategoryDetailWMC.add(addButtonLL);
		phenoDataSetListchoiceCategoryDetailWMC.add(removeButtonLL);
		phenoDataSetListchoiceCategoryDetailWMC.add(upButton);
		phenoDataSetListchoiceCategoryDetailWMC.add(downButton);
		phenoDataSetListchoiceCategoryDetailWMC.add(leftButton);
		phenoDataSetListchoiceCategoryDetailWMC.add(rightButton);
		phenoDataSetListchoiceCategoryDetailWMC.add(customFieldCategoryLastLevelListChoice);
		// TODO: This 'addOrReplace' (instead of just 'add') is a temporary workaround due to the
		// detailPanelFormContainer being initialised only once at the top-level container panel.
		arkCrudContainerVO.getDetailPanelFormContainer().addOrReplace(customFieldDetailWMC);
		arkCrudContainerVO.getDetailPanelFormContainer().addOrReplace(customFieldDisplayDetailWMC);
		//arkCrudContainerVO.getDetailPanelFormContainer().addOrReplace(phenoDataSetCategoryDetailWMC);
		arkCrudContainerVO.getDetailPanelFormContainer().addOrReplace(phenoDataSetListchoiceCategoryDetailWMC);
		add(arkCrudContainerVO.getDetailPanelFormContainer());
	}
	
	/**
	 * Remove duplicates from list
	 * @param customFieldLst
	 * @return
	 */
	private  List<CustomFieldCategory> removeDuplicates(List<CustomFieldCategory> customFieldLst){
		Set<CustomFieldCategory> cusfieldCatSet=new HashSet<CustomFieldCategory>();
		List<CustomFieldCategory> cusfieldCatLst=new ArrayList<CustomFieldCategory>();
		cusfieldCatSet.addAll(customFieldLst);
		cusfieldCatLst.addAll(cusfieldCatSet);
				return cusfieldCatLst;
	}
	
	private  List<CustomFieldCategory> sortLst(List<CustomFieldCategory> customFieldLst){
		//sort by order number.
		Collections.sort(customFieldLst, new Comparator<CustomFieldCategory>(){
		    public int compare(CustomFieldCategory custFieldCategory1, CustomFieldCategory custFieldCatCategory2) {
		        return custFieldCategory1.getOrderNumber().compareTo(custFieldCatCategory2.getOrderNumber());
		    }
		});
				return customFieldLst;
	}
	
	/** This is for testing purpose please remove after that.
	 * Get custom field category collection from model.
	 * @return
	 */
	private List<CustomFieldCategory> getAvailableAllCategoryListInStudyByCustomFieldType(){
		
		Study study =cpModel.getObject().getCustomField().getStudy();
		ArkFunction arkFunction=iArkCommonService.getArkFunctionByName(au.org.theark.core.Constants.FUNCTION_KEY_VALUE_SUBJECT_CUSTOM_FIELD_CATEGORY);
		
		CustomFieldType customFieldType=iArkCommonService.getCustomFieldTypeByName(au.org.theark.core.Constants.SUBJECT);
		Collection<CustomFieldCategory> customFieldCategoryCollection = null;
		try {
			customFieldCategoryCollection =  iArkCommonService.getAvailableAllCategoryListInStudyByCustomFieldType(study,arkFunction, customFieldType);
		} catch (ArkSystemException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return (List<CustomFieldCategory>) customFieldCategoryCollection;
	}
	
	private List<CustomFieldCategory> addChildrenWithoutDuplicates(List<CustomFieldCategory> exsistingLst,List<CustomFieldCategory> childrenLst){
		for (CustomFieldCategory customFieldCategory : childrenLst) {
			if(!exsistingLst.contains(customFieldCategory)){
				exsistingLst.add(customFieldCategory);
			}
		}
		return exsistingLst;
		
		
	}
	
	/** This is for testing purpose please remove after that.
	 * Get custom field category collection from model.
	 * @return
	 */
	private List<CustomFieldCategory> getMySortedSiblingList(CustomFieldCategory customFieldCategory){
		
		Study study =cpModel.getObject().getCustomField().getStudy();
		ArkFunction arkFunction=iArkCommonService.getArkFunctionByName(au.org.theark.core.Constants.FUNCTION_KEY_VALUE_SUBJECT_CUSTOM_FIELD_CATEGORY);
		CustomFieldType customFieldType=iArkCommonService.getCustomFieldTypeByName(au.org.theark.core.Constants.SUBJECT);
		return sortLst(iArkCommonService.getSiblingList(study, arkFunction, customFieldType, customFieldCategory));
		
	}
	/** This is for testing purpose please remove after that.
	 * Get custom field category collection from model.
	 * @return
	 */
	private List<CustomFieldCategory> upButtonUpdateCustomfieldCategoryOrder(List<CustomFieldCategory> sortedSlibingLst,CustomFieldCategory customFieldCategory){
		CustomFieldCategory aboveSibling=sortedSlibingLst.get(sortedSlibingLst.indexOf(customFieldCategory)-1);
		List<CustomFieldCategory> updateList=new ArrayList<CustomFieldCategory>(0);
		Long aboveSiblingOrdNumber=aboveSibling.getOrderNumber();
			aboveSibling.setOrderNumber(customFieldCategory.getOrderNumber());
			customFieldCategory.setOrderNumber(aboveSiblingOrdNumber);
			CustomFieldCategoryVO updateCFCVoAbove = new CustomFieldCategoryVO();
			CustomFieldCategoryVO updateCFCVoBelow = new CustomFieldCategoryVO();
			updateCFCVoAbove.setCustomFieldCategory(aboveSibling);
			updateList.add(aboveSibling);
			try {
				iArkCommonService.mergeCustomFieldCategory(updateCFCVoAbove);
			} catch (ArkSystemException | ArkRunTimeUniqueException
					| ArkRunTimeException e) {
				e.printStackTrace();
			}
			updateCFCVoBelow.setCustomFieldCategory(customFieldCategory);
			updateList.add(customFieldCategory);
			try {
				iArkCommonService.mergeCustomFieldCategory(updateCFCVoBelow);
			} catch (ArkSystemException | ArkRunTimeUniqueException
					| ArkRunTimeException e) {
				e.printStackTrace();
			}
			return updateList;
	}
	/** This is for testing purpose please remove after that.
	 * Get custom field category collection from model.
	 * @return
	 */
	private List<CustomFieldCategory> downButtonUpdateCustomfieldCategoryOrder(List<CustomFieldCategory> sortedSlibingLst,CustomFieldCategory customFieldCategory){
		CustomFieldCategory benethSibling=sortedSlibingLst.get(sortedSlibingLst.indexOf(customFieldCategory)+1);
		List<CustomFieldCategory> updateList=new ArrayList<CustomFieldCategory>(0);
		Long benethSiblingOrdNumber=benethSibling.getOrderNumber();
			benethSibling.setOrderNumber(customFieldCategory.getOrderNumber());
			customFieldCategory.setOrderNumber(benethSiblingOrdNumber);
			CustomFieldCategoryVO updateCFCVoAbove = new CustomFieldCategoryVO();
			CustomFieldCategoryVO updateCFCVoBelow = new CustomFieldCategoryVO();
			updateCFCVoAbove.setCustomFieldCategory(benethSibling);
			updateList.add(benethSibling);
			try {
				iArkCommonService.mergeCustomFieldCategory(updateCFCVoAbove);
			} catch (ArkSystemException | ArkRunTimeUniqueException
					| ArkRunTimeException e) {
				e.printStackTrace();
			}
			updateCFCVoBelow.setCustomFieldCategory(customFieldCategory);
			updateList.add(customFieldCategory);
			try {
				iArkCommonService.mergeCustomFieldCategory(updateCFCVoBelow);
			} catch (ArkSystemException | ArkRunTimeUniqueException
					| ArkRunTimeException e) {
				e.printStackTrace();
			}
			return updateList;
	}
	
	
	

}
