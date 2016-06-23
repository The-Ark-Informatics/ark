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

import au.org.theark.core.model.pheno.entity.PhenoDataSetField;
import au.org.theark.core.model.pheno.entity.PhenoDataSetFieldDisplay;
import au.org.theark.phenotypic.service.IPhenotypicService;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.markup.html.basic.Label;
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
import au.org.theark.core.model.report.entity.ConsentStatusField;
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

	@SpringBean(name = Constants.ARK_PHENO_DATA_SERVICE)
	private IPhenotypicService iPhenoService;

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
	private QueryFilterVO								queryFilterVoToCopy = new QueryFilterVO();
	private Boolean										copyQueryFilter = false;
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
				copyQueryFilter = false;
				listEditor.addItem(filter);
				listEditor.updateModel();
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
				else{
					log.info("failed validation so don't permit save and close");
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
				item.add(new Label("row", ""+(item.getIndex()+1)));
				
				if(copyQueryFilter) {
					item.getModelObject().setFieldCategory(queryFilterVoToCopy.getFieldCategory());
					item.getModelObject().setQueryFilter(queryFilterVoToCopy.getQueryFilter());
					item.getModelObject().getQueryFilter().setValue(queryFilterVoToCopy.getQueryFilter().getValue());
					item.getModelObject().getQueryFilter().setSecondValue(queryFilterVoToCopy.getQueryFilter().getSecondValue());
				}
				
				initFieldCategoryDdc(item);
				initFieldDdc(item);
				initOperatorDdc(item);
				
				valueTxtFld = new TextField<String>("value", new PropertyModel(item.getModelObject(), "queryFilter.value"));
				valueTxtFld.add(new AjaxFormComponentUpdatingBehavior("onchange"){
				    @Override
				    protected void onUpdate(AjaxRequestTarget target) {
				    	/* we may want to perform some live validation based on the type of field we are selecting */
				   	log.info("onchange of VALUE");
				    	target.add(feedbackPanel);
				    } 
				});

				item.add(valueTxtFld);
				
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
							PropertyUtils.copyProperties(queryFilterVO, getItem().getModelObject());
							PropertyUtils.copyProperties(queryFilterVoToCopy, getItem().getModelObject());
							queryFilterVoToCopy.getQueryFilter().setId(null);
							queryFilterVO.getQueryFilter().setId(null);
							copyQueryFilter = true;
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
						iArkCommonService.deleteQueryFilter(item.getModelObject().getQueryFilter());
						listEditor.removeItem(item);
						target.add(form);
					}
				}.setDefaultFormProcessing(false).setVisible(item.getIndex()>=0));


				
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
					fieldDdc.setRequired(true);
					fieldDdc.add(new AjaxFormComponentUpdatingBehavior("onchange") {

						/**
						 * 
						 */
						private static final long	serialVersionUID	= 1L;

						@Override
						protected void onUpdate(AjaxRequestTarget target) {
							queryFilterVoToCopy.getQueryFilter().setDemographicField((DemographicField) getComponent().getDefaultModelObject());
						}
					});
					break;
				}
	
				case BIOSPECIMEN_FIELD:{					
					Collection<BiospecimenField> BiospecimenFieldCategoryList = iArkCommonService.getAllBiospecimenFields();
					ChoiceRenderer<BiospecimenField> choiceRenderer = new ChoiceRenderer<BiospecimenField>(Constants.PUBLIC_FIELD_NAME, Constants.ID);
					fieldDdc = new DropDownChoice<BiospecimenField>("queryFilter.field", 
							new PropertyModel(item.getModelObject(), "queryFilter.biospecimenField"), 
							(List<BiospecimenField>) BiospecimenFieldCategoryList, choiceRenderer);
					fieldDdc.setRequired(true);
					fieldDdc.add(new AjaxFormComponentUpdatingBehavior("onchange") {

						/**
						 * 
						 */
						private static final long	serialVersionUID	= 1L;

						@Override
						protected void onUpdate(AjaxRequestTarget target) {
							queryFilterVoToCopy.getQueryFilter().setBiospecimenField((BiospecimenField) getComponent().getDefaultModelObject());
						}
					});
					break;
				}
	
				case BIOCOLLECTION_FIELD:{					
					Collection<BiocollectionField> biocollectionFieldCategoryList = iArkCommonService.getAllBiocollectionFields();
					ChoiceRenderer<BiocollectionField> choiceRenderer = new ChoiceRenderer<BiocollectionField>(Constants.PUBLIC_FIELD_NAME, Constants.ID);
					fieldDdc = new DropDownChoice<BiocollectionField>("queryFilter.field", 
							new PropertyModel(item.getModelObject(), "queryFilter.biocollectionField"), 
							(List<BiocollectionField>) biocollectionFieldCategoryList, choiceRenderer);
					fieldDdc.setRequired(true);
					fieldDdc.add(new AjaxFormComponentUpdatingBehavior("onchange") {

						/**
						 * 
						 */
						private static final long	serialVersionUID	= 1L;

						@Override
						protected void onUpdate(AjaxRequestTarget target) {
							queryFilterVoToCopy.getQueryFilter().setBiocollectionField((BiocollectionField) getComponent().getDefaultModelObject());
						}
					});
					break;
				}
				
				case SUBJECT_CFD:{		
					ArkFunction arkFunction = iArkCommonService.getArkFunctionByName(Constants.FUNCTION_KEY_VALUE_SUBJECT_CUSTOM_FIELD);
					
					List<CustomFieldDisplay> fieldCategoryList = iArkCommonService.getCustomFieldDisplaysIn(getModelObject().getStudy(), arkFunction);
					ChoiceRenderer<CustomFieldDisplay> choiceRenderer = new ChoiceRenderer<CustomFieldDisplay>(Constants.CUSTOM_FIELD_DOT_NAME, Constants.ID);
					fieldDdc = new DropDownChoice<CustomFieldDisplay>("queryFilter.field", 
							new PropertyModel(item.getModelObject(), "queryFilter.customFieldDisplay"), 
							(List<CustomFieldDisplay>) fieldCategoryList, choiceRenderer);
					fieldDdc.setRequired(true);
					fieldDdc.add(new AjaxFormComponentUpdatingBehavior("onchange") {

						/**
						 * 
						 */
						private static final long	serialVersionUID	= 1L;

						@Override
						protected void onUpdate(AjaxRequestTarget target) {
							queryFilterVoToCopy.getQueryFilter().setCustomFieldDisplay((CustomFieldDisplay) getComponent().getDefaultModelObject());
						}
					});
					break;
				}
	
				case PHENO_FD:{
					ArkFunction arkFunction = iArkCommonService.getArkFunctionByName(Constants.FUNCTION_KEY_VALUE_DATA_DICTIONARY);
					
					List<PhenoDataSetFieldDisplay> fieldCategoryList = iPhenoService.getPhenoFieldDisplaysIn(getModelObject().getStudy(), arkFunction);
					ChoiceRenderer<PhenoDataSetFieldDisplay> choiceRenderer = new ChoiceRenderer<PhenoDataSetFieldDisplay>("descriptiveNameIncludingCFGName", Constants.ID);
					fieldDdc = new DropDownChoice<PhenoDataSetFieldDisplay>("queryFilter.field",
							new PropertyModel(item.getModelObject(), "queryFilter.phenoDataSetFieldDisplay"),
							(List<PhenoDataSetFieldDisplay>) fieldCategoryList, choiceRenderer);
					fieldDdc.setRequired(true);
					fieldDdc.add(new AjaxFormComponentUpdatingBehavior("onchange") {

						/**
						 * 
						 */
						private static final long	serialVersionUID	= 1L;

						@Override
						protected void onUpdate(AjaxRequestTarget target) {
							queryFilterVoToCopy.getQueryFilter().setPhenoDataSetFieldDisplay((PhenoDataSetFieldDisplay) getComponent().getDefaultModelObject());
						}
					});
					break;
				}
	
				case BIOCOLLECTION_CFD:{
					ArkFunction arkFunction = iArkCommonService.getArkFunctionByName(Constants.FUNCTION_KEY_VALUE_LIMS_CUSTOM_FIELD);
					
					List<CustomFieldDisplay> fieldCategoryList = iArkCommonService.getCustomFieldDisplaysIn(getModelObject().getStudy(), arkFunction);
					ChoiceRenderer<CustomFieldDisplay> choiceRenderer = new ChoiceRenderer<CustomFieldDisplay>(Constants.CUSTOM_FIELD_DOT_NAME, Constants.ID);
					fieldDdc = new DropDownChoice<CustomFieldDisplay>("queryFilter.field", 
							new PropertyModel(item.getModelObject(), "queryFilter.customFieldDisplay"), 
							(List<CustomFieldDisplay>) fieldCategoryList, choiceRenderer);
					fieldDdc.setRequired(true);
					fieldDdc.add(new AjaxFormComponentUpdatingBehavior("onchange") {

						/**
						 * 
						 */
						private static final long	serialVersionUID	= 1L;

						@Override
						protected void onUpdate(AjaxRequestTarget target) {
							queryFilterVoToCopy.getQueryFilter().setCustomFieldDisplay((CustomFieldDisplay) getComponent().getDefaultModelObject());
						}
					});
					break;
				}
				
				case BIOSPECIMEN_CFD:{		
					ArkFunction arkFunction = iArkCommonService.getArkFunctionByName(Constants.FUNCTION_KEY_VALUE_BIOSPECIMEN);
					
					List<CustomFieldDisplay> fieldCategoryList = iArkCommonService.getCustomFieldDisplaysIn(getModelObject().getStudy(), arkFunction);
					ChoiceRenderer<CustomFieldDisplay> choiceRenderer = new ChoiceRenderer<CustomFieldDisplay>(Constants.CUSTOM_FIELD_DOT_NAME, Constants.ID);
					fieldDdc = new DropDownChoice<CustomFieldDisplay>("queryFilter.field", 
							new PropertyModel(item.getModelObject(), "queryFilter.customFieldDisplay"), 
							(List<CustomFieldDisplay>) fieldCategoryList, choiceRenderer);
					fieldDdc.setRequired(true);
					fieldDdc.add(new AjaxFormComponentUpdatingBehavior("onchange") {

						/**
						 * 
						 */
						private static final long	serialVersionUID	= 1L;

						@Override
						protected void onUpdate(AjaxRequestTarget target) {
							queryFilterVoToCopy.getQueryFilter().setCustomFieldDisplay((CustomFieldDisplay) getComponent().getDefaultModelObject());
						}
					});
					break;
				}
				
				case CONSENT_STATUS_FIELD: {
					Collection<ConsentStatusField> consentStatusFieldCategoryList = iArkCommonService.getAllConsentStatusFields();
					ChoiceRenderer<ConsentStatusField> choiceRenderer = new ChoiceRenderer<ConsentStatusField>(Constants.PUBLIC_FIELD_NAME, Constants.ID);
					fieldDdc = new DropDownChoice<ConsentStatusField>("queryFilter.field", 
							new PropertyModel(item.getModelObject(), "queryFilter.consentStatusField"),
							(List<ConsentStatusField>) consentStatusFieldCategoryList, choiceRenderer);
					fieldDdc.setRequired(true);
					fieldDdc.add(new AjaxFormComponentUpdatingBehavior("onchange") {

						/**
						 * 
						 */
						private static final long	serialVersionUID	= 1L;

						@Override
						protected void onUpdate(AjaxRequestTarget target) {
							queryFilterVoToCopy.getQueryFilter().setConsentStatusField((ConsentStatusField) getComponent().getDefaultModelObject());
						}
					});
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
			fieldDdc.setRequired(true);
			fieldDdc.add(new AjaxFormComponentUpdatingBehavior("onchange") {

				/**
				 * 
				 */
				private static final long	serialVersionUID	= 1L;

				@Override
				protected void onUpdate(AjaxRequestTarget target) {
					queryFilterVoToCopy.getQueryFilter().setDemographicField((DemographicField) getComponent().getDefaultModelObject());
				}
				
			});
	
		}
		fieldDdc.setOutputMarkupId(true);
		fieldDdc.setRequired(true);
	
		item.add(fieldDdc);
	}
	
	private void initOperatorDdc(final ListItem<QueryFilterVO> item){
		Collection<Operator> operatorList = Arrays.asList(Operator.values());
		//ChoiceRenderer<Operator> choiceRenderer = new ChoiceRenderer<Operator>(Constants.PUBLIC_FIELD_NAME, Constants.ID);
		operatorDdc = new DropDownChoice<Operator>("queryFilter.operator", 
				new PropertyModel(item.getModelObject(), "queryFilter.operator"), 
				(List<Operator>) operatorList, new EnumChoiceRenderer<Operator>(QueryFilterForm.this));

		operatorDdc.setOutputMarkupId(true);
		operatorDdc.setRequired(true);
		operatorDdc.add(new AjaxFormComponentUpdatingBehavior("onchange"){
			private static final long serialVersionUID = 1L;

			@Override
		    protected void onUpdate(AjaxRequestTarget target) {
				Operator operatorFromDDC = item.getModelObject().getQueryFilter().getOperator();
				item.get("secondValue").setEnabled(operatorFromDDC.equals(Operator.BETWEEN));
				target.add(item.get("secondValue"));
			}
		});
		item.add(operatorDdc);
		
		secondValueTxtFld = new TextField<String>("secondValue", new PropertyModel(item.getModelObject(), "queryFilter.secondValue"));
		secondValueTxtFld.add(new AjaxFormComponentUpdatingBehavior("onchange"){
		    @Override
		    protected void onUpdate(AjaxRequestTarget target) {
		    	/* we may want to perform some live validation based on the type of field we are selecting*/
		    	log.info("onchange of SECOND VALUE");
		    	target.add(feedbackPanel);
		    }
		});
		secondValueTxtFld.setOutputMarkupPlaceholderTag(true);
		secondValueTxtFld.setEnabled(false);
		item.add(secondValueTxtFld);
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
						fieldDdc.setRequired(true);
						item.getModelObject().getQueryFilter().setBiospecimenField(null);
						item.getModelObject().getQueryFilter().setBiocollectionField(null);
						item.getModelObject().getQueryFilter().setCustomFieldDisplay(null);
	//					item.getModelObject().getQueryFilter().setDemographicField(null);
						item.getModelObject().getQueryFilter().setConsentStatusField(null);
						item.getModelObject().getQueryFilter().setPhenoDataSetFieldDisplay(null);
						break;
					}

					case BIOSPECIMEN_FIELD:{					
						Collection<BiospecimenField> BiospecimenFieldCategoryList = iArkCommonService.getAllBiospecimenFields();
						ChoiceRenderer<BiospecimenField> choiceRenderer = new ChoiceRenderer<BiospecimenField>(Constants.PUBLIC_FIELD_NAME, Constants.ID);
						fieldDdc = new DropDownChoice<BiospecimenField>("queryFilter.field", 
								new PropertyModel(item.getModelObject(), "queryFilter.biospecimenField"), 
								(List<BiospecimenField>) BiospecimenFieldCategoryList, choiceRenderer);
						fieldDdc.setRequired(true);
						//item.getModelObject().getQueryFilter().setBiospecimenField(null);
						item.getModelObject().getQueryFilter().setBiocollectionField(null);
						item.getModelObject().getQueryFilter().setCustomFieldDisplay(null);
						item.getModelObject().getQueryFilter().setDemographicField(null);
						item.getModelObject().getQueryFilter().setConsentStatusField(null);
						item.getModelObject().getQueryFilter().setPhenoDataSetFieldDisplay(null);
						break;

					}

					case BIOCOLLECTION_FIELD:{					
						Collection<BiocollectionField> biocollectionFieldCategoryList = iArkCommonService.getAllBiocollectionFields();
						ChoiceRenderer<BiocollectionField> choiceRenderer = new ChoiceRenderer<BiocollectionField>(Constants.PUBLIC_FIELD_NAME, Constants.ID);
						fieldDdc = new DropDownChoice<BiocollectionField>("queryFilter.field", 
								new PropertyModel(item.getModelObject(), "queryFilter.biocollectionField"), 
								(List<BiocollectionField>) biocollectionFieldCategoryList, choiceRenderer);
						fieldDdc.setRequired(true);
						item.getModelObject().getQueryFilter().setBiospecimenField(null);
						//item.getModelObject().getQueryFilter().setBiocollectionField(null);
						item.getModelObject().getQueryFilter().setCustomFieldDisplay(null);
						item.getModelObject().getQueryFilter().setDemographicField(null);
						item.getModelObject().getQueryFilter().setConsentStatusField(null);
						item.getModelObject().getQueryFilter().setPhenoDataSetFieldDisplay(null);
						break;
					}
					
					case SUBJECT_CFD:{		
						ArkFunction arkFunction = iArkCommonService.getArkFunctionByName(Constants.FUNCTION_KEY_VALUE_SUBJECT_CUSTOM_FIELD);
						
						List<CustomFieldDisplay> fieldCategoryList = iArkCommonService.getCustomFieldDisplaysIn(getModelObject().getStudy(), arkFunction);
						ChoiceRenderer<CustomFieldDisplay> choiceRenderer = new ChoiceRenderer<CustomFieldDisplay>(Constants.CUSTOM_FIELD_DOT_NAME, Constants.ID);
						fieldDdc = new DropDownChoice<CustomFieldDisplay>("queryFilter.field", 
								new PropertyModel(item.getModelObject(), "queryFilter.customFieldDisplay"), 
								(List<CustomFieldDisplay>) fieldCategoryList, choiceRenderer);
						fieldDdc.setRequired(true);
						item.getModelObject().getQueryFilter().setBiospecimenField(null);
						item.getModelObject().getQueryFilter().setBiocollectionField(null);
						//item.getModelObject().getQueryFilter().setCustomFieldDisplay(null);
						item.getModelObject().getQueryFilter().setDemographicField(null);
						item.getModelObject().getQueryFilter().setConsentStatusField(null);
						item.getModelObject().getQueryFilter().setPhenoDataSetFieldDisplay(null);
						break;
					}

					case PHENO_FD:{
						ArkFunction arkFunction = iArkCommonService.getArkFunctionByName(Constants.FUNCTION_KEY_VALUE_DATA_DICTIONARY);
						
						List<PhenoDataSetFieldDisplay> fieldCategoryList = iPhenoService.getPhenoFieldDisplaysIn(getModelObject().getStudy(), arkFunction);
						ChoiceRenderer<PhenoDataSetFieldDisplay> choiceRenderer = new ChoiceRenderer<PhenoDataSetFieldDisplay>("descriptiveNameIncludingCFGName", Constants.ID);
						fieldDdc = new DropDownChoice<PhenoDataSetFieldDisplay>("queryFilter.field",
								new PropertyModel(item.getModelObject(), "queryFilter.phenoDataSetFieldDisplay"),
								(List<PhenoDataSetFieldDisplay>) fieldCategoryList, choiceRenderer);
						fieldDdc.setRequired(true);
						item.getModelObject().getQueryFilter().setBiospecimenField(null);
						item.getModelObject().getQueryFilter().setBiocollectionField(null);
						item.getModelObject().getQueryFilter().setCustomFieldDisplay(null);
						item.getModelObject().getQueryFilter().setDemographicField(null);
						item.getModelObject().getQueryFilter().setConsentStatusField(null);
						//item.getModelObject().getQueryFilter().setPhenoDataSetFieldDisplay(null);
						break;
					}

					case BIOCOLLECTION_CFD:{
						ArkFunction arkFunction = iArkCommonService.getArkFunctionByName(Constants.FUNCTION_KEY_VALUE_LIMS_CUSTOM_FIELD);
						
						List<CustomFieldDisplay> fieldCategoryList = iArkCommonService.getCustomFieldDisplaysIn(getModelObject().getStudy(), arkFunction);
						ChoiceRenderer<CustomFieldDisplay> choiceRenderer = new ChoiceRenderer<CustomFieldDisplay>(Constants.CUSTOM_FIELD_DOT_NAME, Constants.ID);
						fieldDdc = new DropDownChoice<CustomFieldDisplay>("queryFilter.field", 
								new PropertyModel(item.getModelObject(), "queryFilter.customFieldDisplay"), 
								(List<CustomFieldDisplay>) fieldCategoryList, choiceRenderer);
						fieldDdc.setRequired(true);
						item.getModelObject().getQueryFilter().setBiospecimenField(null);
						item.getModelObject().getQueryFilter().setBiocollectionField(null);
						//item.getModelObject().getQueryFilter().setCustomFieldDisplay(null);
						item.getModelObject().getQueryFilter().setDemographicField(null);
						item.getModelObject().getQueryFilter().setConsentStatusField(null);
						item.getModelObject().getQueryFilter().setPhenoDataSetFieldDisplay(null);
						break;
					}
					
					case BIOSPECIMEN_CFD:{		
						ArkFunction arkFunction = iArkCommonService.getArkFunctionByName(Constants.FUNCTION_KEY_VALUE_BIOSPECIMEN);
						
						List<CustomFieldDisplay> fieldCategoryList = iArkCommonService.getCustomFieldDisplaysIn(getModelObject().getStudy(), arkFunction);
						ChoiceRenderer<CustomFieldDisplay> choiceRenderer = new ChoiceRenderer<CustomFieldDisplay>(Constants.CUSTOM_FIELD_DOT_NAME, Constants.ID);
						fieldDdc = new DropDownChoice<CustomFieldDisplay>("queryFilter.field", 
								new PropertyModel(item.getModelObject(), "queryFilter.customFieldDisplay"), 
								(List<CustomFieldDisplay>) fieldCategoryList, choiceRenderer);
						fieldDdc.setRequired(true);
						item.getModelObject().getQueryFilter().setBiospecimenField(null);
						item.getModelObject().getQueryFilter().setBiocollectionField(null);
						//item.getModelObject().getQueryFilter().setCustomFieldDisplay(null);
						item.getModelObject().getQueryFilter().setDemographicField(null);
						item.getModelObject().getQueryFilter().setConsentStatusField(null);
						item.getModelObject().getQueryFilter().setPhenoDataSetFieldDisplay(null);
						break;
					}
					
					case CONSENT_STATUS_FIELD:{	
						Collection<ConsentStatusField> consentStatusFieldCategoryList = iArkCommonService.getAllConsentStatusFields();
						ChoiceRenderer<ConsentStatusField> choiceRenderer = new ChoiceRenderer<ConsentStatusField>(Constants.PUBLIC_FIELD_NAME, Constants.ID);
						fieldDdc = new DropDownChoice<ConsentStatusField>("queryFilter.field", 
								new PropertyModel<ConsentStatusField>(item.getModelObject(), "queryFilter.consentStatusField"),
								(List<ConsentStatusField>) consentStatusFieldCategoryList, choiceRenderer);
						fieldDdc.setRequired(true);
						item.getModelObject().getQueryFilter().setBiospecimenField(null);
						item.getModelObject().getQueryFilter().setBiocollectionField(null);
						item.getModelObject().getQueryFilter().setCustomFieldDisplay(null);
						item.getModelObject().getQueryFilter().setDemographicField(null);
						item.getModelObject().getQueryFilter().setPhenoDataSetFieldDisplay(null);
						//item.getModelObject().getQueryFilter().setConsentStatusField(null);
						break;
					}

				}

				fieldDdc.setOutputMarkupId(true);
				fieldDdc.setRequired(true);
				fieldDdc.add(new AjaxFormComponentUpdatingBehavior("onchange"){
				    /**
					 * 
					 */
					private static final long	serialVersionUID	= 1L;

					@Override
				    protected void onUpdate(AjaxRequestTarget target) {
				   	 log.debug("Change of fieldDDc");
				    } 
				    
				    @Override 
				   protected void onError(AjaxRequestTarget target, RuntimeException e) {
				   	target.add(feedbackPanel);
				   }
				});
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
				queryfilter.setSearch(getModelObject().getSearch());
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
					info("Query Filters created: " + getModelObject().getQueryFilterVOs().size());
					log.info("Attempting to create " + getModelObject().getQueryFilterVOs().size() + " filters");
				//TODO ASAP	iArkCommonService.createFilters(filterList);
				} catch (ArkSystemException e) {
					log.error("creation / object save failed: " + e.getMessage());
					error(e.getMessage());
					return false;
				}
			}
			
			return true;
		}
		else{
			return false;
		}
	}

	private boolean validatedList() {
		boolean ok = true;
		int filterRow = 0;
		for (QueryFilterVO queryFilterVO: getModelObject().getQueryFilterVOs()) {
				QueryFilter queryfilter = queryFilterVO.getQueryFilter();
				filterRow++;
				if (queryfilter.getOperator().equals(Operator.BETWEEN)) {
					//then both values cant be null valueOne and Value2
					//are certain values/fieldstypes valid for this operator?
					//are values needed or should they be ignored?
					
					if (queryfilter.getValue() == null || queryfilter.getSecondValue() == null) {
						error("Error on row " + filterRow + ": For values in a between range, Value and Value 2 is required");
						ok = false;
					}
					
					//if error i guess we return false and give back a list of errors?
				}
				else if (queryfilter.getOperator().equals(Operator.LIKE) || queryfilter.getOperator().equals(Operator.NOT_EQUAL)) {
					//then both values cant be null
					//are certain values/fieldstypes valid for this operator?
					//are values needed or should they be ignored?
					
					if (queryfilter.getValue() == null) {
						error("Error on row " + filterRow + ": A Value is required");
						ok = false;
					}
				}
				else if (queryfilter.getOperator().equals(Operator.EQUAL)) {
					//then both values cant be null
					//are certain values/fieldstypes valid for this operator?
					//are values needed or should they be ignored?
					if (queryfilter.getValue() == null) {
						error("Error on row " + filterRow + ": A Value is required");
						ok = false;
					}
					
				}
				else if (queryfilter.getOperator().equals(Operator.GREATER_THAN) || queryfilter.getOperator().equals(Operator.GREATER_THAN_OR_EQUAL)) {
					//then both values cant be null
					//are certain values/fieldstypes valid for this operator?
					//are values needed or should they be ignored?
					
					if (queryfilter.getValue() == null) {
						error("Error on row " + filterRow + ": A Value is required");
						ok = false;
					}
				}
				else if (queryfilter.getOperator().equals(Operator.LESS_THAN) || queryfilter.getOperator().equals(Operator.LESS_THAN_OR_EQUAL)) {
					//then both values cant be null
					//are certain values/fieldstypes valid for this operator?
					//are values needed or should they be ignored?
					
					if (queryfilter.getValue() == null) {
						error("Error on row " + filterRow + ": A Value is required");
						ok = false;
					}
				}
				else{
					log.info("different operator?  that can't happen - can it?  ");
				}
		}
		
				
		return ok;
	}
}