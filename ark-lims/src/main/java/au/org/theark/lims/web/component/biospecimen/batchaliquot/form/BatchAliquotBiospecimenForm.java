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
package au.org.theark.lims.web.component.biospecimen.batchaliquot.form;

import java.lang.reflect.InvocationTargetException;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.util.convert.IConverter;
import org.apache.wicket.util.convert.converter.DoubleConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import au.org.theark.core.model.lims.entity.BioTransaction;
import au.org.theark.core.model.lims.entity.BioTransactionStatus;
import au.org.theark.core.model.lims.entity.Biospecimen;
import au.org.theark.core.model.lims.entity.TreatmentType;
import au.org.theark.core.web.component.listeditor.AbstractListEditor;
import au.org.theark.core.web.component.listeditor.AjaxEditorButton;
import au.org.theark.core.web.component.listeditor.ListItem;
import au.org.theark.core.web.form.ArkFormVisitor;
import au.org.theark.lims.model.vo.BatchBiospecimenAliquotsVO;
import au.org.theark.lims.service.ILimsService;
import au.org.theark.lims.web.Constants;

/**
 * @author cellis
 * 
 */
@SuppressWarnings( { "unchecked" })
public class BatchAliquotBiospecimenForm extends Form<BatchBiospecimenAliquotsVO> {

	private static final long								serialVersionUID	= 1L;
	private static final Logger							log					= LoggerFactory.getLogger(BatchAliquotBiospecimenForm.class);

	@SpringBean(name = Constants.LIMS_SERVICE)
	private ILimsService										iLimsService;

	// Add a visitor class for required field marking/validation/highlighting
	protected ArkFormVisitor		formVisitor			= new ArkFormVisitor();
	
	protected FeedbackPanel								feedbackPanel;
	private AbstractListEditor<Biospecimen>	listEditor;
	
	private Label												parentQtyLbl;
	private TextField<String>								biospecimenUidTxtFld;
	private TextField<Number>								numberToCreateTxtFld;
	private TextField<Double>								quantityTxtFld;
	private DropDownChoice<TreatmentType>				treatmentTypeDdc;
	private TextField<Number>								concentrationTxtFld;
	
	protected ModalWindow 									modalWindow;

	public BatchAliquotBiospecimenForm(String id, IModel<BatchBiospecimenAliquotsVO> model, ModalWindow modalWindow) {
		super(id, model);
		this.feedbackPanel = new FeedbackPanel("feedback");
		feedbackPanel.setOutputMarkupId(true);
		setMultiPart(true);
		this.modalWindow = modalWindow;
		add(feedbackPanel);
	}

	public void initialiseForm() {
		numberToCreateTxtFld = new TextField<Number>("numberToCreate", new PropertyModel(getModelObject(), "numberToCreate")){
			/**
			 * 
			 */
			private static final long	serialVersionUID	= 1L;

			@Override
			public boolean isVisible() {
				// Only visible on first instantation of form, once entered and saved, hidden by enclosure
				return getModelObject() == null;
			}
		};
		
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
		
		add(buildListEditor());
		
		add(new Label("parentBiospecimen.biospecimenUid", new PropertyModel(getModelObject(), "parentBiospecimen.biospecimenUid")));
		parentQtyLbl = new Label("parentBiospecimen.quantity", new PropertyModel(getModelObject(), "parentBiospecimen.quantity")){
			/**
			 * 
			 */
			private static final long	serialVersionUID	= 1L;

			@Override
			public <C> IConverter<C> getConverter(Class<C> type) {
				DoubleConverter doubleConverter = new DoubleConverter();
				NumberFormat numberFormat = NumberFormat.getInstance();
				numberFormat.setMinimumFractionDigits(1);
				doubleConverter.setNumberFormat(getLocale(), numberFormat);
				return (IConverter<C>) doubleConverter;
			}
		};
		add(parentQtyLbl);
		add(new Label("parentBiospecimen.unit.name", new PropertyModel(getModelObject(), "parentBiospecimen.unit.name")));
		
		add(new AjaxEditorButton(Constants.NEW) {
			private static final long	serialVersionUID	= 1L;

			@Override
			protected void onError(AjaxRequestTarget target, Form<?> form) {
				target.add(feedbackPanel);
			}

			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
				Biospecimen biospecimen= new Biospecimen();
				listEditor.addItem(biospecimen);
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
	 * 
	 * @return the listEditor of Biospecimens to aliquot
	 */
	public AbstractListEditor<Biospecimen> buildListEditor() {
		listEditor = new AbstractListEditor<Biospecimen>("aliquots", new PropertyModel(getModelObject(), "aliquots")) {

			private static final long	serialVersionUID	= 1L;

			@SuppressWarnings("serial")
			@Override
			protected void onPopulateItem(final ListItem<Biospecimen> item) {
				item.setOutputMarkupId(true);
				
				Biospecimen parentBiospecimen = BatchAliquotBiospecimenForm.this.getModelObject().getParentBiospecimen();
				try {
					PropertyUtils.copyProperties(item.getModelObject(), parentBiospecimen);
				}
				catch (IllegalAccessException e1) {
					log.error(e1.getMessage());
				}
				catch (InvocationTargetException e1) {
					log.error(e1.getMessage());
				}
				catch (NoSuchMethodException e1) {
					log.error(e1.getMessage());
				}
				item.getModelObject().setParent(parentBiospecimen);
				item.getModelObject().setParentUid(parentBiospecimen.getBiospecimenUid());
				item.getModelObject().setQuantity(null);
				item.getModelObject().setConcentration(null);
				
				
				biospecimenUidTxtFld = new TextField<String>("biospecimenUid", new PropertyModel(item.getModelObject(), "biospecimenUid"));
				if(parentBiospecimen.getStudy().getAutoGenerateBiospecimenUid()) {
					biospecimenUidTxtFld.setEnabled(false);
					biospecimenUidTxtFld.setModelObject(Constants.AUTO_GENERATED);
				}
				else {
					biospecimenUidTxtFld.setEnabled(true);
					biospecimenUidTxtFld.setModelObject(null);
				}

				quantityTxtFld = new TextField<Double>("quantity", new PropertyModel(item.getModelObject(), "quantity")) {
					private static final long	serialVersionUID	= 1L;

					@Override
					public <C> IConverter<C> getConverter(Class<C> type) {
						DoubleConverter doubleConverter = new DoubleConverter();
						NumberFormat numberFormat = NumberFormat.getInstance();
						numberFormat.setMinimumFractionDigits(1);
						doubleConverter.setNumberFormat(getLocale(), numberFormat);
						return (IConverter<C>) doubleConverter;
					}
				};
				
				initTreatmentTypeDdc(item);
				concentrationTxtFld = new TextField<Number>("concentration", new PropertyModel(item.getModelObject(), "concentration"));

				item.add(biospecimenUidTxtFld.add(new AjaxFormComponentUpdatingBehavior("onchange"){
				    @Override
				    protected void onUpdate(AjaxRequestTarget target) {
				   	if(!item.getModelObject().getStudy().getAutoGenerateBiospecimenUid()) {
					   	 // Check BiospecimenUID is unique
							String biospecimenUid = (getComponent().getDefaultModelObject().toString() != null ? getComponent().getDefaultModelObject().toString() : new String());
							Biospecimen biospecimen = iLimsService.getBiospecimenByUid(biospecimenUid, item.getModelObject().getStudy());
							if (biospecimen != null && biospecimen.getId() != null) {
								error("Biospecimen UID must be unique. Please try again.");
								target.focusComponent(getComponent());
							}
				   	}
				   	target.add(feedbackPanel);
				    } 
				}));
				
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

				// Copy button allows entire row details to be copied
				item.add(new AjaxEditorButton(Constants.COPY) {
					private static final long	serialVersionUID	= 1L;

					@Override
					protected void onError(AjaxRequestTarget target, Form<?> form) {
						target.add(feedbackPanel);
					}

					@Override
					protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
						Biospecimen biospecimen = new Biospecimen();
						try {
							PropertyUtils.copyProperties(biospecimen, item.getModelObject());
							listEditor.addItem(biospecimen);
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

	private void initTreatmentTypeDdc(ListItem<Biospecimen> item) {
		List<TreatmentType> treatmentTypeList = iLimsService.getTreatmentTypes();
		ChoiceRenderer<TreatmentType> choiceRenderer = new ChoiceRenderer<TreatmentType>(Constants.NAME, Constants.ID);
		treatmentTypeDdc = new DropDownChoice<TreatmentType>("treatmentType", new PropertyModel(item.getModelObject(), "treatmentType"), (List<TreatmentType>) treatmentTypeList, choiceRenderer);
		treatmentTypeDdc.setNullValid(false);
	}
	
	private boolean onSave(AjaxRequestTarget target) {
		List<Biospecimen> biospecimenList = new ArrayList<Biospecimen>(0);
		if(validatedList()) {
			
			if(totalQuantityLessThanParentQuantity()){
				// Loop through entire list
				for (Biospecimen biospecimen: getModelObject().getAliquots()) {
					biospecimen.setBioCollection(getModelObject().getParentBiospecimen().getBioCollection());
					biospecimen.setSampleType(getModelObject().getParentBiospecimen().getSampleType());
					biospecimen.setSampleDate(new Date());
					biospecimen.setStudy(getModelObject().getParentBiospecimen().getStudy());
					biospecimen.setLinkSubjectStudy(getModelObject().getParentBiospecimen().getLinkSubjectStudy());
					
					Set<BioTransaction> bioTransactions = new HashSet<BioTransaction>(0);
					
					// Inheriently create a transaction for the initial quantity
					BioTransaction bioTransaction = new BioTransaction();
					bioTransaction.setBiospecimen(biospecimen);
					bioTransaction.setTransactionDate(Calendar.getInstance().getTime());
					bioTransaction.setQuantity(biospecimen.getQuantity());
					bioTransaction.setReason(au.org.theark.lims.web.Constants.BIOTRANSACTION_STATUS_INITIAL_QUANTITY);
					bioTransaction.setRecorder(SecurityUtils.getSubject().getPrincipal().toString());
					
					BioTransactionStatus initialStatus = iLimsService.getBioTransactionStatusByName(au.org.theark.lims.web.Constants.BIOTRANSACTION_STATUS_INITIAL_QUANTITY);
					bioTransaction.setStatus(initialStatus);	//ensure that the initial transaction can be identified
					bioTransactions.add(bioTransaction);
					biospecimen.setBioTransactions(bioTransactions);
					
					if(biospecimen.getStudy().getAutoGenerateBiospecimenUid()) {
						biospecimen.setBiospecimenUid(iLimsService.getNextGeneratedBiospecimenUID(biospecimen.getStudy()));
					}
					
					biospecimenList.add(biospecimen);
				}
			}
			
			StringBuffer message = new StringBuffer();
			message.append("Created ");
			message.append(getModelObject().getNumberToCreate());
			message.append(" ");
			message.append("biospecimen aliquots");
			
			if(!biospecimenList.isEmpty()) {
				info("Batch aliquots created:");
				log.info("Attempting to create " + getModelObject().getAliquots().size() + " biospecimen aliquots");
				iLimsService.batchAliquotBiospecimens(biospecimenList);
			}
			
			return true;
		}
		else{
			return false;
		}
	}

	private boolean totalQuantityLessThanParentQuantity() {
		boolean ok = true;
		Double totalQuantity = new Double(0);
		for (Biospecimen biospecimen: getModelObject().getAliquots()) {
			if(biospecimen.getQuantity() != null) {
				totalQuantity = totalQuantity + biospecimen.getQuantity();
			}
		}
		
		if(totalQuantity > getModelObject().getParentBiospecimen().getQuantity()) {
			error("You cannot aliquot more than the parent's total amount of " + getModelObject().getParentBiospecimen().getQuantity() + getModelObject().getParentBiospecimen().getUnit().getName());
			ok = false;
		}
		
		return ok;
	}

	private boolean validatedList() {
		boolean ok = true;
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
		}
		return ok;
	}
}