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
package au.org.theark.report.web.component.dataextraction.filter.form;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.EnumChoiceRenderer;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import au.org.theark.core.Constants;
import au.org.theark.core.exception.ArkSystemException;
import au.org.theark.core.model.report.entity.BiocollectionField;
import au.org.theark.core.model.report.entity.BiospecimenField;
import au.org.theark.core.model.report.entity.DemographicField;
import au.org.theark.core.model.report.entity.FieldCategory;
import au.org.theark.core.model.report.entity.Operator;
import au.org.theark.core.model.report.entity.QueryFilter;
import au.org.theark.core.model.study.entity.ArkFunction;
import au.org.theark.core.model.study.entity.CustomFieldDisplay;
import au.org.theark.core.model.study.entity.Study;
import au.org.theark.core.service.IArkCommonService;
import au.org.theark.core.vo.QueryFilterListVO;
import au.org.theark.core.vo.QueryFilterVO;
import au.org.theark.core.web.component.listeditor.AbstractListEditor;
import au.org.theark.core.web.component.listeditor.AjaxEditorButton;
import au.org.theark.core.web.component.listeditor.ListItem;
import au.org.theark.core.web.form.ArkFormVisitor;

/**
 * @author cellis
 * 
 */
@SuppressWarnings( { "unchecked" })
public class QueryFilterForm extends Form<QueryFilterListVO> {

	private static final long								serialVersionUID	= 1L;
	private static final Logger							log					= LoggerFactory.getLogger(QueryFilterForm.class);

	@SpringBean(name = Constants.ARK_COMMON_SERVICE)
	private IArkCommonService										iArkCommonService;

	// Add a visitor class for required field marking/validation/highlighting
	protected ArkFormVisitor					formVisitor	= new ArkFormVisitor();
	
	protected FeedbackPanel						feedbackPanel;
	private AbstractListEditor<QueryFilterVO>	listEditor;

	private TextField<String>					valueTxtFld;
	private TextField<String>					secondValueTxtFld;
	
/*	private Label										parentQtyLbl;
	private TextField<String>							biospecimenUidTxtFld;
	private TextField<Number>							numberToCreateTxtFld;
	private TextField<Double>							quantityTxtFld;
	*/private DropDownChoice<FieldCategory>				fieldCategoryDdc;
	private DropDownChoice								fieldDdc;
	private DropDownChoice								operatorDdc;
	//private TextField<Number>							concentrationTxtFld;
	
	protected ModalWindow 									modalWindow;

	public QueryFilterForm(String id, IModel<QueryFilterListVO> model, ModalWindow modalWindow) {
		super(id, model);
		Long studySessionId = (Long) SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.core.Constants.STUDY_CONTEXT_ID);
		final Study study = iArkCommonService.getStudy(studySessionId);
		model.getObject().setStudy(study);
		model.getObject().setQueryFilterVOs(iArkCommonService.getQueryFilterVOs(model.getObject().getSearch()));
		
		//model.getObject().setQueryFilterVOs(iArkCommonService.getQueryFilter(search));
		this.feedbackPanel = new FeedbackPanel("feedback");
		feedbackPanel.setOutputMarkupId(true);
		setMultiPart(true);
		this.modalWindow = modalWindow;
		add(feedbackPanel);
	}

	public void initialiseForm() {
		/*
		add(numberToCreateTxtFld);
		add(new AjaxButton("numberToCreateButton") {
			private static final long	serialVersionUID	= 1L;

			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
				int numberToCreate = ((Integer) numberToCreateTxtFld.getDefaultModelObject());
				for (int i = 0; i < numberToCreate; i++) {
					Biospecimen biospecimen= new Biospecimen();
					listEditor.addItem(biospecimen);
					listEditor.updateModel();
					target.add(form);	
				}	
			}

			@Override
			protected void onError(AjaxRequestTarget target, Form<?> form) {
				target.add(feedbackPanel);
			}
		});
		*/
		add(buildListEditor());
		/*
		add(new Label("parentBiospecimen.biospecimenUid", new PropertyModel(getModelObject(), "parentBiospecimen.biospecimenUid")));
		parentQtyLbl = new Label("parentBiospecimen.quantity", new PropertyModel(getModelObject(), "parentBiospecimen.quantity")){
			
			private static final long	serialVersionUID	= 1L;
		};
		add(parentQtyLbl);
		add(new Label("parentBiospecimen.unit.name", new PropertyModel(getModelObject(), "parentBiospecimen.unit.name")));
		*/
		add(new AjaxEditorButton(Constants.NEW) {
			private static final long	serialVersionUID	= 1L;

			@Override
			protected void onError(AjaxRequestTarget target, Form<?> form) {
				target.add(feedbackPanel);
			}

			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
				QueryFilterVO filter= new QueryFilterVO();
				listEditor.addItem(filter);
				target.add(form);
			}
		}.setDefaultFormProcessing(false));
		
		add(new AjaxButton(Constants.SAVE) {
			private static final long	serialVersionUID	= 1L;

			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
				onSave(target);
				target.add(feedbackPanel);
			}

			@Override
			protected void onError(AjaxRequestTarget target, Form<?> form) {
				target.add(feedbackPanel);
			}
		});
		
		add(new AjaxButton(Constants.SAVEANDCLOSE) {
			private static final long	serialVersionUID	= 1L;

			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
				if(onSave(target)) {
					modalWindow.close(target);
				}
				target.add(feedbackPanel);
			}

			@Override
			protected void onError(AjaxRequestTarget target, Form<?> form) {
				target.add(feedbackPanel);
			}
		});
		
		add(new AjaxButton(Constants.CANCEL) {
			private static final long	serialVersionUID	= 1L;

			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
				modalWindow.close(target);
			}

			@Override
			protected void onError(AjaxRequestTarget target, Form<?> form) {
				target.add(feedbackPanel);
			}
		}.setDefaultFormProcessing(false));
	}

	/**
	 * @return the listEditor of Biospecimens to aliquot
	 */
	public AbstractListEditor<QueryFilterVO> buildListEditor() {
		listEditor = new AbstractListEditor<QueryFilterVO>("queryFilterVOs", new PropertyModel(getModelObject(), "queryFilterVOs")) {

			private static final long	serialVersionUID	= 1L;

			@SuppressWarnings("serial")
			@Override
			protected void onPopulateItem(final ListItem<QueryFilterVO> item) {
				item.setOutputMarkupId(true);
				

				valueTxtFld = new TextField<String>("value", new PropertyModel(item.getModelObject(), "queryFilter.value"));

				item.add(valueTxtFld.add(new AjaxFormComponentUpdatingBehavior("onchange"){
				    @Override
				    protected void onUpdate(AjaxRequestTarget target) {
				    	/* we may want to perform some live validation based on the type of field we are selecting
				    	 * 
					   	if(!item.getModelObject().getStudy().getAutoGenerateBiospecimenUid()) {
						   	 // Check BiospecimenUID is unique
								String biospecimenUid = (getComponent().getDefaultModelObject().toString() != null ? getComponent().getDefaultModelObject().toString() : new String());
								Biospecimen biospecimen = iArkCommonService.getBiospecimenByUid(biospecimenUid, item.getModelObject().getStudy());
								if (biospecimen != null && biospecimen.getId() != null) {
									error("Biospecimen UID must be unique. Please try again.");
									target.focusComponent(getComponent());
								}
					   	}*/
				    	log.info("onchange of VALUE");
				    	target.add(feedbackPanel);
				    } 
				}));
				
				
				secondValueTxtFld = new TextField<String>("secondValue", new PropertyModel(item.getModelObject(), "queryFilter.secondValue")) {
					@Override
					public boolean isVisible() {
						boolean visible = (item.getModelObject().getQueryFilter()!=null && item.getModelObject().getQueryFilter().getOperator() != null) 
								&& item.getModelObject().getQueryFilter().getOperator().equals(Operator.BETWEEN);
						return visible;
					}
				};

				secondValueTxtFld.setOutputMarkupId(true);
				
				item.add(secondValueTxtFld.add(new AjaxFormComponentUpdatingBehavior("onchange"){
				    @Override
				    protected void onUpdate(AjaxRequestTarget target) {
				    	/* we may want to perform some live validation based on the type of field we are selecting
				    	 * 
				    	 * 
				   	if(!item.getModelObject().getStudy().getAutoGenerateBiospecimenUid()) {
					   	 // Check BiospecimenUID is unique
							String biospecimenUid = (getComponent().getDefaultModelObject().toString() != null ? getComponent().getDefaultModelObject().toString() : new String());
							Biospecimen biospecimen = iArkCommonService.getBiospecimenByUid(biospecimenUid, item.getModelObject().getStudy());
							if (biospecimen != null && biospecimen.getId() != null) {
								error("Biospecimen UID must be unique. Please try again.");
								target.focusComponent(getComponent());
							}
				   	}*/
				    	log.info("onchange of SECOND VALUE");
				    	target.add(feedbackPanel);
				    } 
				}));
				/*
				item.add(quantityTxtFld.add(new AjaxFormComponentUpdatingBehavior("onchange"){
				    @Override
				    protected void onUpdate(AjaxRequestTarget target) {
				   	 if(!totalQuantityLessThanParentQuantity()) {
				   		 target.focusComponent(getComponent());
				   	 }
				   	 target.add(feedbackPanel);
				    } 
				    
				    @Override
				   protected void onError(AjaxRequestTarget target, RuntimeException e) {
				   	target.add(feedbackPanel);
				   }
				}));
				item.add(treatmentTypeDdc.add(new AjaxFormComponentUpdatingBehavior("onchange"){
				    @Override
				    protected void onUpdate(AjaxRequestTarget target) {
				    } 
				}));
				item.add(concentrationTxtFld.add(new AjaxFormComponentUpdatingBehavior("onchange"){
				    @Override
				    protected void onUpdate(AjaxRequestTarget target) {
				    } 
				}));

				*/
				// Copy button allows entire row details to be copied
				item.add(new AjaxEditorButton(Constants.COPY) {
					private static final long	serialVersionUID	= 1L;

					@Override
					protected void onError(AjaxRequestTarget target, Form<?> form) {
						target.add(feedbackPanel);
					}

					@Override
					protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
						QueryFilterVO queryFilterVO = new QueryFilterVO();
						
						try {
							PropertyUtils.copyProperties(queryFilterVO, item.getModelObject());
							listEditor.addItem(queryFilterVO);
							target.add(form);
						}
						catch (IllegalAccessException e) {
							e.printStackTrace();
						}
						catch (InvocationTargetException e) {
							e.printStackTrace();
						}
						catch (NoSuchMethodException e) {
							e.printStackTrace();
						}
					}
				}.setDefaultFormProcessing(false));
				item.add(new AjaxEditorButton(Constants.DELETE) {
					private static final long	serialVersionUID	= 1L;

					@Override
					protected void onError(AjaxRequestTarget target, Form<?> form) {
						target.add(feedbackPanel);
					}

					@Override
					protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
						listEditor.removeItem(item);
						target.add(form);
					}
				}.setDefaultFormProcessing(false).setVisible(item.getIndex()>0));

				initFieldCategoryDdc(item);
				initFieldDdc(item);
				initOperatorDdc(item);
				
				item.add(new AttributeModifier(Constants.CLASS, new AbstractReadOnlyModel() {

					private static final long	serialVersionUID	= 1L;

					@Override
					public String getObject() {
						return (item.getIndex() % 2 == 1) ? Constants.EVEN : Constants.ODD;
					}
				}));
			
				
				}

		};
		return listEditor;
	}
	
	
	
	/**
	 * perform this after initfieldcategoryddc
	 * @param item
	 */
	private void initFieldDdc(ListItem<QueryFilterVO> item){
		if(item.getModelObject()!=null && item.getModelObject().getFieldCategory()!=null){
		
			FieldCategory catFromItem = item.getModelObject().getFieldCategory();
			
			switch (catFromItem){
	
				case DEMOGRAPHIC_FIELD:{					
					Collection<DemographicField> demographicFieldCategoryList = iArkCommonService.getAllDemographicFields();
					ChoiceRenderer<DemographicField> choiceRenderer = new ChoiceRenderer<DemographicField>(Constants.PUBLIC_FIELD_NAME, Constants.ID);
					fieldDdc = new DropDownChoice<DemographicField>("queryFilter.field", 
							new PropertyModel(item.getModelObject(), "queryFilter.demographicField"), 
							(List<DemographicField>) demographicFieldCategoryList, choiceRenderer);
					break;
				}
	
				case BIOSPECIMEN_FIELD:{					
					Collection<BiospecimenField> BiospecimenFieldCategoryList = iArkCommonService.getAllBiospecimenFields();
					ChoiceRenderer<BiospecimenField> choiceRenderer = new ChoiceRenderer<BiospecimenField>(Constants.PUBLIC_FIELD_NAME, Constants.ID);
					fieldDdc = new DropDownChoice<BiospecimenField>("queryFilter.field", 
							new PropertyModel(item.getModelObject(), "queryFilter.biospecimenField"), 
							(List<BiospecimenField>) BiospecimenFieldCategoryList, choiceRenderer);
					break;
				}
	
				case BIOCOLLECTION_FIELD:{					
					Collection<BiocollectionField> biocollectionFieldCategoryList = iArkCommonService.getAllBiocollectionFields();
					ChoiceRenderer<BiocollectionField> choiceRenderer = new ChoiceRenderer<BiocollectionField>(Constants.PUBLIC_FIELD_NAME, Constants.ID);
					fieldDdc = new DropDownChoice<BiocollectionField>("queryFilter.field", 
							new PropertyModel(item.getModelObject(), "queryFilter.biocollectionField"), 
							(List<BiocollectionField>) biocollectionFieldCategoryList, choiceRenderer);
					break;
				}
				
				case SUBJECT_CFD:{		
					ArkFunction arkFunction = iArkCommonService.getArkFunctionByName(Constants.FUNCTION_KEY_VALUE_SUBJECT_CUSTOM_FIELD);
					
					List<CustomFieldDisplay> fieldCategoryList = iArkCommonService.getCustomFieldDisplaysIn(getModelObject().getStudy(), arkFunction);
					ChoiceRenderer<CustomFieldDisplay> choiceRenderer = new ChoiceRenderer<CustomFieldDisplay>(Constants.CUSTOM_FIELD_DOT_NAME, Constants.ID);
					fieldDdc = new DropDownChoice<CustomFieldDisplay>("queryFilter.field", 
							new PropertyModel(item.getModelObject(), "queryFilter.customFieldDisplay"), 
							(List<CustomFieldDisplay>) fieldCategoryList, choiceRenderer);
					break;
				}
	
				case PHENO_CFD:{
					ArkFunction arkFunction = iArkCommonService.getArkFunctionByName(Constants.FUNCTION_KEY_VALUE_PHENO_COLLECTION);
					
					List<CustomFieldDisplay> fieldCategoryList = iArkCommonService.getCustomFieldDisplaysIn(getModelObject().getStudy(), arkFunction);
					ChoiceRenderer<CustomFieldDisplay> choiceRenderer = new ChoiceRenderer<CustomFieldDisplay>(Constants.CUSTOM_FIELD_DOT_NAME, Constants.ID);
					fieldDdc = new DropDownChoice<CustomFieldDisplay>("queryFilter.field", 
							new PropertyModel(item.getModelObject(), "queryFilter.customFieldDisplay"), 
							(List<CustomFieldDisplay>) fieldCategoryList, choiceRenderer);
					break;
				}
	
				case BIOCOLLECTION_CFD:{
					ArkFunction arkFunction = iArkCommonService.getArkFunctionByName(Constants.FUNCTION_KEY_VALUE_LIMS_COLLECTION);
					
					List<CustomFieldDisplay> fieldCategoryList = iArkCommonService.getCustomFieldDisplaysIn(getModelObject().getStudy(), arkFunction);
					ChoiceRenderer<CustomFieldDisplay> choiceRenderer = new ChoiceRenderer<CustomFieldDisplay>(Constants.CUSTOM_FIELD_DOT_NAME, Constants.ID);
					fieldDdc = new DropDownChoice<CustomFieldDisplay>("queryFilter.field", 
							new PropertyModel(item.getModelObject(), "queryFilter.customFieldDisplay"), 
							(List<CustomFieldDisplay>) fieldCategoryList, choiceRenderer);
					break;
				}
				
				case BIOSPECIMEN_CFD:{		
					ArkFunction arkFunction = iArkCommonService.getArkFunctionByName(Constants.FUNCTION_KEY_VALUE_BIOSPECIMEN);
					
					List<CustomFieldDisplay> fieldCategoryList = iArkCommonService.getCustomFieldDisplaysIn(getModelObject().getStudy(), arkFunction);
					ChoiceRenderer<CustomFieldDisplay> choiceRenderer = new ChoiceRenderer<CustomFieldDisplay>(Constants.CUSTOM_FIELD_DOT_NAME, Constants.ID);
					fieldDdc = new DropDownChoice<CustomFieldDisplay>("queryFilter.field", 
							new PropertyModel(item.getModelObject(), "queryFilter.customFieldDisplay"), 
							(List<CustomFieldDisplay>) fieldCategoryList, choiceRenderer);
					break;
				}
	
			}
		}
		else{ //this is a new item - set to default		
			Collection<DemographicField> demographicFieldCategoryList = iArkCommonService.getAllDemographicFields();
			ChoiceRenderer<DemographicField> choiceRenderer = new ChoiceRenderer<DemographicField>(Constants.PUBLIC_FIELD_NAME, Constants.ID);
			fieldDdc = new DropDownChoice<DemographicField>("queryFilter.field", 
					new PropertyModel(item.getModelObject(), "queryFilter.demographicField"), 
					(List<DemographicField>) demographicFieldCategoryList, choiceRenderer);
	
		}
		fieldDdc.setOutputMarkupId(true);
		item.add(fieldDdc);
	}
	
	private void initOperatorDdc(final ListItem<QueryFilterVO> item){
		Collection<Operator> operatorList = Arrays.asList(Operator.values());
		//ChoiceRenderer<Operator> choiceRenderer = new ChoiceRenderer<Operator>(Constants.PUBLIC_FIELD_NAME, Constants.ID);
		operatorDdc = new DropDownChoice<Operator>("queryFilter.operator", 
				new PropertyModel(item.getModelObject(), "queryFilter.operator"), 
				(List<Operator>) operatorList, new EnumChoiceRenderer<Operator>(QueryFilterForm.this));

		operatorDdc.setOutputMarkupId(true);
		item.add(operatorDdc);

		secondValueTxtFld.setOutputMarkupId(true);
		item.add(secondValueTxtFld);
		
		operatorDdc.add(new AjaxFormComponentUpdatingBehavior("onchange"){
			private static final long serialVersionUID = 1L;

			@Override
		    protected void onUpdate(AjaxRequestTarget target) {
				/*Operator operatorFromDDC = item.getModelObject().getQueryFilter().getOperator();
				
				switch (operatorFromDDC){

					case BETWEEN:{
						secondValueTxtFld.setVisible(true);
						break;
					}
					
					default:{
						secondValueTxtFld.setVisible(false);
						break;
					}
				}*/
				//item.addOrReplace(secondValueTxtFld);
				target.add(secondValueTxtFld);
			}
			
		});

	}
	
	private void initFieldCategoryDdc(final ListItem<QueryFilterVO> item) {


		List<FieldCategory> fieldCategoryList = Arrays.asList(FieldCategory.values()); 
		fieldCategoryDdc = new DropDownChoice<FieldCategory>("fieldCategory", 
				new PropertyModel(item.getModelObject(), "fieldCategory"), 
				(List<FieldCategory>) fieldCategoryList, new EnumChoiceRenderer<FieldCategory>(QueryFilterForm.this));
		fieldCategoryDdc.setNullValid(false);
		if(item.getModelObject()==null || item.getModelObject().getFieldCategory()==null){
			fieldCategoryDdc.setDefaultModelObject(FieldCategory.DEMOGRAPHIC_FIELD);
		}
		fieldCategoryDdc.add(new AjaxFormComponentUpdatingBehavior("onchange"){
			private static final long serialVersionUID = 1L;

			@Override
		    protected void onUpdate(AjaxRequestTarget target) {
				FieldCategory catFromDDC = item.getModelObject().getFieldCategory();
				
				switch (catFromDDC){

					case DEMOGRAPHIC_FIELD:{					
						Collection<DemographicField> demographicFieldCategoryList = iArkCommonService.getAllDemographicFields();
						ChoiceRenderer<DemographicField> choiceRenderer = new ChoiceRenderer<DemographicField>(Constants.PUBLIC_FIELD_NAME, Constants.ID);
						fieldDdc = new DropDownChoice<DemographicField>("queryFilter.field", 
								new PropertyModel(item.getModelObject(), "queryFilter.demographicField"), 
								(List<DemographicField>) demographicFieldCategoryList, choiceRenderer);
						break;
					}

					case BIOSPECIMEN_FIELD:{					
						Collection<BiospecimenField> BiospecimenFieldCategoryList = iArkCommonService.getAllBiospecimenFields();
						ChoiceRenderer<BiospecimenField> choiceRenderer = new ChoiceRenderer<BiospecimenField>(Constants.PUBLIC_FIELD_NAME, Constants.ID);
						fieldDdc = new DropDownChoice<BiospecimenField>("queryFilter.field", 
								new PropertyModel(item.getModelObject(), "queryFilter.biospecimenField"), 
								(List<BiospecimenField>) BiospecimenFieldCategoryList, choiceRenderer);
						break;
					}

					case BIOCOLLECTION_FIELD:{					
						Collection<BiocollectionField> biocollectionFieldCategoryList = iArkCommonService.getAllBiocollectionFields();
						ChoiceRenderer<BiocollectionField> choiceRenderer = new ChoiceRenderer<BiocollectionField>(Constants.PUBLIC_FIELD_NAME, Constants.ID);
						fieldDdc = new DropDownChoice<BiocollectionField>("queryFilter.field", 
								new PropertyModel(item.getModelObject(), "queryFilter.biocollectionField"), 
								(List<BiocollectionField>) biocollectionFieldCategoryList, choiceRenderer);
						break;
					}
					
					case SUBJECT_CFD:{		
						ArkFunction arkFunction = iArkCommonService.getArkFunctionByName(Constants.FUNCTION_KEY_VALUE_SUBJECT_CUSTOM_FIELD);
						
						List<CustomFieldDisplay> fieldCategoryList = iArkCommonService.getCustomFieldDisplaysIn(getModelObject().getStudy(), arkFunction);
						ChoiceRenderer<CustomFieldDisplay> choiceRenderer = new ChoiceRenderer<CustomFieldDisplay>(Constants.CUSTOM_FIELD_DOT_NAME, Constants.ID);
						fieldDdc = new DropDownChoice<CustomFieldDisplay>("queryFilter.field", 
								new PropertyModel(item.getModelObject(), "queryFilter.customFieldDisplay"), 
								(List<CustomFieldDisplay>) fieldCategoryList, choiceRenderer);
						break;
					}

					case PHENO_CFD:{
						ArkFunction arkFunction = iArkCommonService.getArkFunctionByName(Constants.FUNCTION_KEY_VALUE_PHENO_COLLECTION);
						
						List<CustomFieldDisplay> fieldCategoryList = iArkCommonService.getCustomFieldDisplaysIn(getModelObject().getStudy(), arkFunction);
						ChoiceRenderer<CustomFieldDisplay> choiceRenderer = new ChoiceRenderer<CustomFieldDisplay>(Constants.CUSTOM_FIELD_DOT_NAME, Constants.ID);
						fieldDdc = new DropDownChoice<CustomFieldDisplay>("queryFilter.field", 
								new PropertyModel(item.getModelObject(), "queryFilter.customFieldDisplay"), 
								(List<CustomFieldDisplay>) fieldCategoryList, choiceRenderer);
						break;
					}

					case BIOCOLLECTION_CFD:{
						ArkFunction arkFunction = iArkCommonService.getArkFunctionByName(Constants.FUNCTION_KEY_VALUE_LIMS_COLLECTION);
						
						List<CustomFieldDisplay> fieldCategoryList = iArkCommonService.getCustomFieldDisplaysIn(getModelObject().getStudy(), arkFunction);
						ChoiceRenderer<CustomFieldDisplay> choiceRenderer = new ChoiceRenderer<CustomFieldDisplay>(Constants.CUSTOM_FIELD_DOT_NAME, Constants.ID);
						fieldDdc = new DropDownChoice<CustomFieldDisplay>("queryFilter.field", 
								new PropertyModel(item.getModelObject(), "queryFilter.customFieldDisplay"), 
								(List<CustomFieldDisplay>) fieldCategoryList, choiceRenderer);
						break;
					}
					
					case BIOSPECIMEN_CFD:{		
						ArkFunction arkFunction = iArkCommonService.getArkFunctionByName(Constants.FUNCTION_KEY_VALUE_BIOSPECIMEN);
						
						List<CustomFieldDisplay> fieldCategoryList = iArkCommonService.getCustomFieldDisplaysIn(getModelObject().getStudy(), arkFunction);
						ChoiceRenderer<CustomFieldDisplay> choiceRenderer = new ChoiceRenderer<CustomFieldDisplay>(Constants.CUSTOM_FIELD_DOT_NAME, Constants.ID);
						fieldDdc = new DropDownChoice<CustomFieldDisplay>("queryFilter.field", 
								new PropertyModel(item.getModelObject(), "queryFilter.customFieldDisplay"), 
								(List<CustomFieldDisplay>) fieldCategoryList, choiceRenderer);
						break;
					}

				}

				fieldDdc.setOutputMarkupId(true);
				item.addOrReplace(fieldDdc);
				target.add(item);

				/*item.setOutputMarkupId(true);
				item.addOrReplace(fieldDdc);
				target.add(item);*/
							
		    } 
		});
		item.add(fieldCategoryDdc);
				
	}
	
	private boolean onSave(AjaxRequestTarget target) {
		List<QueryFilter> filterList = new ArrayList<QueryFilter>(0);
		if(validatedList()) {
		
			// Loop through entire list
			for (QueryFilterVO queryFilterVO: getModelObject().getQueryFilterVOs()) {

				QueryFilter queryfilter = queryFilterVO.getQueryFilter();

				filterList.add(queryfilter);
/*
				QueryFilter queryfilter = queryFilterVO.getQueryFilter();
				queryfilter.setOperator(getModelObject().getQueryFilterVOs().getBioCollection());
				queryFilterVO.setSampleType(getModelObject().getParentBiospecimen().getSampleType());
				queryFilterVO.setSampleDate(new Date());
				queryFilterVO.setStudy(getModelObject().getParentBiospecimen().getStudy());
				queryFilterVO.setLinkSubjectStudy(getModelObject().getParentBiospecimen().getLinkSubjectStudy());
				
				filterList.add(queryFilterVO);
		
				
				DO I NEED ANY OF THIS< OR DO I JUST TAKE  IT DIRECTLY...THE ONLY CRAP THING IS CHOSING WHICH FIELD TO FILL OUT
						(given it is a choice of one out of 4)
				
				
				*/
			}
		
			//StringBuffer message = new StringBuffer();
			//message.append("Created ");
			//message.append(getModelObject().getNumberToCreate());
			//message.append(" simple filters ");
			
			if(!filterList.isEmpty()) {
				try {
					iArkCommonService.createQueryFilters(filterList);
					info("Query Filters created:");
					log.info("Attempting to create " + getModelObject().getQueryFilterVOs().size() + " filters");
				//TODO ASAP	iArkCommonService.createFilters(filterList);
				} catch (ArkSystemException e) {
					log.error("creation / object save failed " + e.getMessage());
					error(e.getMessage());
				}
			}
			
			return true;
		}
		else{
			return false;
		}
	}

	private boolean validatedList() {
		boolean ok = true;/*
		boolean biospecimenUidError = false;
		boolean quantityError = false;
		boolean treatmentTypeError = false;
		boolean concentrationError = false;
		
		// Check for any empty required fields in list
		for (Biospecimen biospecimen: getModelObject().getAliquots()) {
			biospecimenUidError = (biospecimen.getBiospecimenUid() == null);
			quantityError = (biospecimen.getQuantity() == null);
			treatmentTypeError = (biospecimen.getTreatmentType() == null);
			// Concentration required?
			//concentrationError = (biospecimen.getConcentration() == null);
			
			if(biospecimenUidError || quantityError || treatmentTypeError || concentrationError) {
				break;
			}
		}
		
		if(biospecimenUidError) {
			error("Field 'Biospecimen UID' is required.");
			ok = false;
		}
		if(quantityError) {
			error("Field 'Quantity' is required.");
			ok = false;
		}
		if(treatmentTypeError) {
			error("Field 'Treatment' is required.");
			ok = false;
		}
		if(concentrationError) {
			error("Field 'Concentration' is required.");
			ok = false;
		}*/
		return ok;
	}
}