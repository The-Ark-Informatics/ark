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
package au.org.theark.lims.web.component.biospecimen.batchcreate.form;

import java.lang.reflect.InvocationTargetException;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Calendar;
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
import org.apache.wicket.extensions.markup.html.form.DateTextField;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.util.convert.IConverter;
import org.apache.wicket.util.convert.converter.DoubleConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import au.org.theark.core.exception.ArkSystemException;
import au.org.theark.core.exception.EntityNotFoundException;
import au.org.theark.core.model.lims.entity.BioCollection;
import au.org.theark.core.model.lims.entity.BioSampletype;
import au.org.theark.core.model.lims.entity.BioTransaction;
import au.org.theark.core.model.lims.entity.BioTransactionStatus;
import au.org.theark.core.model.lims.entity.Biospecimen;
import au.org.theark.core.model.lims.entity.TreatmentType;
import au.org.theark.core.model.lims.entity.Unit;
import au.org.theark.core.model.study.entity.LinkSubjectStudy;
import au.org.theark.core.model.study.entity.Study;
import au.org.theark.core.service.IArkCommonService;
import au.org.theark.core.web.component.ArkDatePicker;
import au.org.theark.core.web.component.listeditor.AbstractListEditor;
import au.org.theark.core.web.component.listeditor.AjaxEditorButton;
import au.org.theark.core.web.component.listeditor.ListItem;
import au.org.theark.core.web.form.ArkFormVisitor;
import au.org.theark.lims.model.vo.BatchBiospecimenVO;
import au.org.theark.lims.model.vo.LimsVO;
import au.org.theark.lims.service.ILimsService;
import au.org.theark.lims.web.Constants;

/**
 * @author cellis
 * 
 */
@SuppressWarnings( { "unchecked" })
public class AutoGenBatchCreateBiospecimenForm extends Form<BatchBiospecimenVO> {

	private static final long								serialVersionUID	= 1L;
	private static final Logger							log					= LoggerFactory.getLogger(AutoGenBatchCreateBiospecimenForm.class);

	@SpringBean(name = au.org.theark.core.Constants.ARK_COMMON_SERVICE)
	private IArkCommonService<Void>						iArkCommonService;

	@SpringBean(name = Constants.LIMS_SERVICE)
	private ILimsService										iLimsService;

	// Add a visitor class for required field marking/validation/highlighting
	protected ArkFormVisitor		formVisitor			= new ArkFormVisitor();
	
	protected CompoundPropertyModel<LimsVO>			cpModel;
	protected FeedbackPanel								feedbackPanel;
	private AbstractListEditor<BatchBiospecimenVO>	listEditor;
	
	private TextField<Number>								numberToCreateTxtFld;
	private DropDownChoice<BioCollection>				bioCollectionDdc;
	private DropDownChoice<BioSampletype>				sampleTypeDdc;
	private DateTextField									sampleDateTxtFld;
	private TextField<Double>								quantityTxtFld;
	private DropDownChoice<Unit>							unitDdc;
	private DropDownChoice<TreatmentType>				treatmentTypeDdc;
	private TextField<Number>								concentrationTxtFld;
	
	private BatchBiospecimenVO								batchBiospecimenVO = new BatchBiospecimenVO();
	private List<BatchBiospecimenVO>						batchBiospecimenList = new ArrayList<BatchBiospecimenVO>();
	protected ModalWindow 									modalWindow;

	public AutoGenBatchCreateBiospecimenForm(String id, CompoundPropertyModel<LimsVO> cpModel, IModel<BatchBiospecimenVO> model, ModalWindow modalWindow) {
		super(id, model);
		this.feedbackPanel = new FeedbackPanel("feedback");
		feedbackPanel.setOutputMarkupId(true);
		this.cpModel = cpModel;
		setMultiPart(true);
		this.modalWindow = modalWindow;
		batchBiospecimenList.add(new BatchBiospecimenVO());
		add(feedbackPanel);
	}

	public void initialiseForm() {
		add(buildListEditor());
		add(new AjaxEditorButton(Constants.NEW) {
			private static final long	serialVersionUID	= 1L;

			@Override
			protected void onError(AjaxRequestTarget target, Form<?> form) {
				target.add(feedbackPanel);
			}

			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
				BatchBiospecimenVO item = new BatchBiospecimenVO();
				item.setBiospecimen(new Biospecimen());
				item.getBiospecimen().setLinkSubjectStudy(cpModel.getObject().getLinkSubjectStudy());
				item.getBiospecimen().setStudy(cpModel.getObject().getStudy());
				listEditor.addItem(item);
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
		}.setVisible(false));
		
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

	@Override
	public void onBeforeRender() {
		// Get session data (used for subject search)
		Long sessionStudyId = (Long) SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.core.Constants.STUDY_CONTEXT_ID);
		String sessionSubjectUID = (String) SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.core.Constants.SUBJECTUID);

		if ((sessionStudyId != null) && (sessionSubjectUID != null)) {
			LinkSubjectStudy linkSubjectStudy = null;
			Study study = null;
			boolean contextLoaded = false;
			try {
				study = iArkCommonService.getStudy(sessionStudyId);
				linkSubjectStudy = iArkCommonService.getSubjectByUID(sessionSubjectUID, study);
				if (study != null && linkSubjectStudy != null) {
					contextLoaded = true;
				}
			}
			catch (EntityNotFoundException e) {
				log.error(e.getMessage());
			}

			if (contextLoaded) {
				// Successfully loaded from backend
				cpModel.getObject().setLinkSubjectStudy(linkSubjectStudy);
				cpModel.getObject().getBiospecimen().setLinkSubjectStudy(linkSubjectStudy);
				cpModel.getObject().getBiospecimen().setStudy(study);
			}
		}

		super.onBeforeRender();
		visitChildren(formVisitor);
	}

	/**
	 * 
	 * @return the listEditor of BatchBiospecimen(s)
	 */
	public AbstractListEditor<BatchBiospecimenVO> buildListEditor() {
		listEditor = new AbstractListEditor<BatchBiospecimenVO>("batchBiospecimenList", new PropertyModel(this, "batchBiospecimenList")) {

			private static final long	serialVersionUID	= 1L;

			@SuppressWarnings("serial")
			@Override
			protected void onPopulateItem(final ListItem<BatchBiospecimenVO> item) {
				item.setOutputMarkupId(true);
				item.getModelObject().getBiospecimen().setLinkSubjectStudy(cpModel.getObject().getLinkSubjectStudy());
				item.getModelObject().getBiospecimen().setStudy(cpModel.getObject().getLinkSubjectStudy().getStudy());

				numberToCreateTxtFld = new TextField<Number>("numberToCreate", new PropertyModel(item.getModelObject(), "numberToCreate"));
				
				initBioCollectionDdc(item);
				initSampleTypeDdc(item);

				sampleDateTxtFld = new DateTextField("biospecimen.sampleDate", new PropertyModel(item.getModelObject(), "biospecimen.sampleDate"), au.org.theark.core.Constants.DD_MM_YYYY);
				ArkDatePicker sampleDatePicker = new ArkDatePicker();
				sampleDatePicker.bind(sampleDateTxtFld);
				sampleDateTxtFld.add(sampleDatePicker);

				quantityTxtFld = new TextField<Double>("biospecimen.quantity", new PropertyModel(item.getModelObject(), "biospecimen.quantity")) {
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
				initUnitDdc(item);
				initTreatmentTypeDdc(item);
				concentrationTxtFld = new TextField<Number>("biospecimen.concentration", new PropertyModel(item.getModelObject(), "biospecimen.concentration"));

				// Added onchange events to ensure model updated when any change made
				item.add(numberToCreateTxtFld.add(new AjaxFormComponentUpdatingBehavior("onchange"){
				    @Override
				    protected void onUpdate(AjaxRequestTarget target) {
				    } 
				}));
				item.add(bioCollectionDdc.add(new AjaxFormComponentUpdatingBehavior("onchange"){
				    @Override
				    protected void onUpdate(AjaxRequestTarget target) {
				    } 
				}));
				item.add(sampleTypeDdc.add(new AjaxFormComponentUpdatingBehavior("onchange"){
				    @Override
				    protected void onUpdate(AjaxRequestTarget target) {
				    } 
				}));
				item.add(sampleDateTxtFld.add(new AjaxFormComponentUpdatingBehavior("onchange"){
				    @Override
				    protected void onUpdate(AjaxRequestTarget target) {
				    } 
				}));
				item.add(quantityTxtFld.add(new AjaxFormComponentUpdatingBehavior("onchange"){
				    @Override
				    protected void onUpdate(AjaxRequestTarget target) {
				    } 
				}));
				item.add(unitDdc.add(new AjaxFormComponentUpdatingBehavior("onchange"){
				    @Override
				    protected void onUpdate(AjaxRequestTarget target) {
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
						BatchBiospecimenVO batchBiospecimenVo = new BatchBiospecimenVO();
						try {
							batchBiospecimenVo.setNumberToCreate(item.getModelObject().getNumberToCreate());
							PropertyUtils.copyProperties(batchBiospecimenVo.getBiospecimen(), item.getModelObject().getBiospecimen());
							listEditor.addItem(batchBiospecimenVo);
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

	private void initBioCollectionDdc(ListItem<BatchBiospecimenVO> item) {
		// Get a list of collections for the subject in context by default
		BioCollection bioCollection = new BioCollection();
		bioCollection.setLinkSubjectStudy(cpModel.getObject().getBiospecimen().getLinkSubjectStudy());
		bioCollection.setStudy(cpModel.getObject().getBiospecimen().getLinkSubjectStudy().getStudy());
		try {
			cpModel.getObject().setBioCollectionList(iLimsService.searchBioCollection(bioCollection));

			ChoiceRenderer<BioCollection> choiceRenderer = new ChoiceRenderer<BioCollection>("biocollectionUid", Constants.ID);
			bioCollectionDdc = new DropDownChoice<BioCollection>("biospecimen.bioCollection", new PropertyModel(item.getModelObject(), "biospecimen.bioCollection"), cpModel.getObject().getBioCollectionList(), choiceRenderer);
		}
		catch (ArkSystemException e) {
			log.error(e.getMessage());
			this.error("Operation could not be performed - if this persists, contact your Administrator or Support");
		}
	}

	private void initSampleTypeDdc(ListItem<BatchBiospecimenVO> item) {
		List<BioSampletype> sampleTypeList = iLimsService.getBioSampleTypes();
		ChoiceRenderer<BioSampletype> choiceRenderer = new ChoiceRenderer<BioSampletype>(Constants.NAME, Constants.ID);
		sampleTypeDdc = new DropDownChoice<BioSampletype>("biospecimen.sampleType", new PropertyModel(item.getModelObject(), "biospecimen.sampleType"), (List<BioSampletype>) sampleTypeList, choiceRenderer);
	}

	private void initTreatmentTypeDdc(ListItem<BatchBiospecimenVO> item) {
		List<TreatmentType> treatmentTypeList = iLimsService.getTreatmentTypes();
		ChoiceRenderer<TreatmentType> choiceRenderer = new ChoiceRenderer<TreatmentType>(Constants.NAME, Constants.ID);
		treatmentTypeDdc = new DropDownChoice<TreatmentType>("biospecimen.treatmentType", new PropertyModel(item.getModelObject(), "biospecimen.treatmentType"), (List<TreatmentType>) treatmentTypeList, choiceRenderer);
		treatmentTypeDdc.setNullValid(false);
	}

	private void initUnitDdc(ListItem<BatchBiospecimenVO> item) {
		List<Unit> unitList = iLimsService.getUnits();
		ChoiceRenderer<Unit> choiceRenderer = new ChoiceRenderer<Unit>(Constants.NAME, Constants.ID);
		unitDdc = new DropDownChoice<Unit>("biospecimen.unit", new PropertyModel(item.getModelObject(), "biospecimen.unit"), (List<Unit>) unitList, choiceRenderer);
		unitDdc.setNullValid(false);
	}
	
	private boolean onSave(AjaxRequestTarget target) {
		if(validatedList()) {
			info("Batch biospecimens created:");
			List<Biospecimen> biospecimenList = new ArrayList<Biospecimen>(0);

			// Loop through entire list
			for (BatchBiospecimenVO batchBiospecimenVO: batchBiospecimenList) {
				// Create multiple biospecimens per list row
				int i = 0;
				while ( i < batchBiospecimenVO.getNumberToCreate()) {
					Biospecimen biospecimen = new Biospecimen();
					biospecimen.setBioCollection(batchBiospecimenVO.getBiospecimen().getBioCollection());
					biospecimen.setSampleType(batchBiospecimenVO.getBiospecimen().getSampleType());
					biospecimen.setSampleDate(batchBiospecimenVO.getBiospecimen().getSampleDate());
					biospecimen.setQuantity(batchBiospecimenVO.getBiospecimen().getQuantity());
					biospecimen.setUnit(batchBiospecimenVO.getBiospecimen().getUnit());
					biospecimen.setTreatmentType(batchBiospecimenVO.getBiospecimen().getTreatmentType());
					biospecimen.setStudy(batchBiospecimenVO.getBiospecimen().getStudy());
					biospecimen.setLinkSubjectStudy(batchBiospecimenVO.getBiospecimen().getLinkSubjectStudy());
					biospecimen.setConcentration(batchBiospecimenVO.getBiospecimen().getConcentration());
					
					Set<BioTransaction> bioTransactions = new HashSet<BioTransaction>(0);
					
					// Inheriently create a transaction for the initial quantity
					BioTransaction bioTransaction = new BioTransaction();
					bioTransaction.setBiospecimen(biospecimen);
					bioTransaction.setTransactionDate(Calendar.getInstance().getTime());
					bioTransaction.setQuantity(biospecimen.getQuantity());
					bioTransaction.setReason(au.org.theark.lims.web.Constants.BIOTRANSACTION_STATUS_INITIAL_QUANTITY);
					
					BioTransactionStatus initialStatus = iLimsService.getBioTransactionStatusByName(au.org.theark.lims.web.Constants.BIOTRANSACTION_STATUS_INITIAL_QUANTITY);
					bioTransaction.setStatus(initialStatus);	//ensure that the initial transaction can be identified
					bioTransactions.add(bioTransaction);
					biospecimen.setBioTransactions(bioTransactions);
					
					if(biospecimen.getStudy().getAutoGenerateBiospecimenUid()) {
						biospecimen.setBiospecimenUid(iLimsService.getNextGeneratedBiospecimenUID(biospecimen.getStudy()));
					}
					
					biospecimenList.add(biospecimen);
					i++;
				}
				
				StringBuffer message = new StringBuffer();
				message.append("Created ");
				message.append(batchBiospecimenVO.getNumberToCreate());
				message.append(" ");
				message.append(batchBiospecimenVO.getBiospecimen().getSampleType().getName());
				message.append(" ");
				message.append("biospecimens");
			
				// Passes info messages to the session, so the parents feedback messages can be refreshed accordingly
				getSession().info(message);
			}
			
			log.info("Attempting to create " + biospecimenList.size() + " biospecimens");
			iLimsService.batchInsertBiospecimens(biospecimenList);
			return true;
		}
		else{
			return false;
		}
	}

	private boolean validatedList() {
		boolean ok = true;
		boolean numberToCreateError = false;
		boolean collectionError = false;
		boolean sampleTypeError = false;
		boolean quantityError = false;
		boolean unitError = false;
		boolean treatmentTypeError = false;
		
		// Check for any empty required fields in list
		for (BatchBiospecimenVO batchBiospecimenVO: batchBiospecimenList) {
			numberToCreateError = (batchBiospecimenVO.getNumberToCreate() == null);	
			collectionError = (batchBiospecimenVO.getBiospecimen().getBioCollection() == null);
			sampleTypeError = (batchBiospecimenVO.getBiospecimen().getSampleType() == null);
			quantityError = (batchBiospecimenVO.getBiospecimen().getQuantity() == null);
			unitError = (batchBiospecimenVO.getBiospecimen().getUnit() == null);
			treatmentTypeError = (batchBiospecimenVO.getBiospecimen().getTreatmentType() == null);
			
			if(numberToCreateError || collectionError || sampleTypeError || quantityError || unitError || treatmentTypeError) {
				break;
			}
		}
		
		if(numberToCreateError) {
			error("Field 'Number to Create' is required.");
			ok = false;
		}
		if(collectionError) {
			error("Field 'Collection' is required.");
			ok = false;
		}
		if(sampleTypeError) {
			error("Field 'Sample Type' is required.");
			ok = false;
		}
		if(quantityError) {
			error("Field 'Quantity' is required.");
			ok = false;
		}
		if(unitError) {
			error("Field 'Units' is required.");
			ok = false;
		}
		if(treatmentTypeError) {
			error("Field 'Treatment' is required.");
			ok = false;
		}
		return ok;
	}

	/**
	 * @param batchBiospecimenVO the batchBiospecimenVO to set
	 */
	public void setBatchBiospecimenVO(BatchBiospecimenVO batchBiospecimenVO) {
		this.batchBiospecimenVO = batchBiospecimenVO;
	}

	/**
	 * @return the batchBiospecimenVO
	 */
	public BatchBiospecimenVO getBatchBiospecimenVO() {
		return batchBiospecimenVO;
	}

	/**
	 * @param batchBiospecimenList the batchBiospecimenList to set
	 */
	public void setBatchBiospecimenList(List<BatchBiospecimenVO> batchBiospecimenList) {
		this.batchBiospecimenList = batchBiospecimenList;
	}

	/**
	 * @return the batchBiospecimenList
	 */
	public List<BatchBiospecimenVO> getBatchBiospecimenList() {
		return batchBiospecimenList;
	}
}