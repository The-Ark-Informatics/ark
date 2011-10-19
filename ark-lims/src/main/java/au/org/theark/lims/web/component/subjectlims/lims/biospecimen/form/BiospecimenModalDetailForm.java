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
package au.org.theark.lims.web.component.subjectlims.lims.biospecimen.form;

import java.lang.reflect.InvocationTargetException;
import java.text.NumberFormat;
import java.util.Calendar;
import java.util.List;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.extensions.markup.html.form.DateTextField;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.image.NonCachingImage;
import org.apache.wicket.markup.html.panel.EmptyPanel;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.util.convert.IConverter;
import org.apache.wicket.util.convert.converter.DoubleConverter;
import org.apache.wicket.validation.validator.MinimumValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import au.org.theark.core.exception.ArkSystemException;
import au.org.theark.core.exception.EntityNotFoundException;
import au.org.theark.core.model.lims.entity.BioCollection;
import au.org.theark.core.model.lims.entity.BioSampletype;
import au.org.theark.core.model.lims.entity.BioTransaction;
import au.org.theark.core.model.lims.entity.Biospecimen;
import au.org.theark.core.model.lims.entity.TreatmentType;
import au.org.theark.core.model.lims.entity.Unit;
import au.org.theark.core.service.IArkCommonService;
import au.org.theark.core.vo.ArkCrudContainerVO;
import au.org.theark.core.web.behavior.ArkDefaultFormFocusBehavior;
import au.org.theark.core.web.component.ArkDatePicker;
import au.org.theark.core.web.form.AbstractModalDetailForm;
import au.org.theark.lims.model.vo.BiospecimenCustomDataVO;
import au.org.theark.lims.model.vo.BiospecimenLocationVO;
import au.org.theark.lims.model.vo.LimsVO;
import au.org.theark.lims.service.IInventoryService;
import au.org.theark.lims.service.ILimsService;
import au.org.theark.lims.util.UniqueIdGenerator;
import au.org.theark.lims.util.barcode.DataMatrixBarcodeImage;
import au.org.theark.lims.web.Constants;
import au.org.theark.lims.web.component.biolocation.BioLocationDetailPanel;
import au.org.theark.lims.web.component.biospecimencustomdata.BiospecimenCustomDataDataViewPanel;
import au.org.theark.lims.web.component.biotransaction.BioTransactionListPanel;
import au.org.theark.lims.web.component.subjectlims.lims.biospecimen.BiospecimenButtonsPanel;

/**
 * Detail form for Biospecimen, as displayed within a modal window
 * 
 * @author elam
 * @author cellis
 */
public class BiospecimenModalDetailForm extends AbstractModalDetailForm<LimsVO> {
	/**
	 * 
	 */
	private static final long					serialVersionUID	= 2727419197330261916L;
	private static final Logger				log					= LoggerFactory.getLogger(BiospecimenModalDetailForm.class);
	@SpringBean(name = au.org.theark.core.Constants.ARK_COMMON_SERVICE)
	private IArkCommonService<Void>			iArkCommonService;

	@SpringBean(name = au.org.theark.lims.web.Constants.LIMS_SERVICE)
	private ILimsService							iLimsService;

	@SpringBean(name = au.org.theark.lims.web.Constants.LIMS_INVENTORY_SERVICE)
	private IInventoryService					iInventoryService;

	private TextField<String>					idTxtFld;
	private TextField<String>					biospecimenUidTxtFld;
	private TextField<String>					parentUidTxtFld;
	private TextArea<String>					commentsTxtAreaFld;
	private DateTextField						sampleDateTxtFld;
	private DropDownChoice<BioSampletype>	sampleTypeDdc;
	private DropDownChoice<BioCollection>	bioCollectionDdc;
	private TextField<Double>					quantityTxtFld;
	private CheckBox								barcodedChkBox;
	private NonCachingImage						barcodeImage;

	// Initial BioTransaction details
	private TextField<Double>					bioTransactionQuantityTxtFld;
	private DropDownChoice<Unit>				unitDdc;
	private DropDownChoice<TreatmentType>	treatmentTypeDdc;

	private Label									quantityLbl;
	private Label									quantityNoteLbl;

	private Panel									biospecimenCFDataEntryPanel;
	private Panel									bioTransactionListPanel;
	private Panel									biospecimenLocationPanel;
	private ModalWindow							modalWindow;

	private WebMarkupContainer					bioTransactionDetailWmc;

	/**
	 * Constructor
	 * 
	 * @param id
	 * @param feedBackPanel
	 * @param arkCrudContainerVo
	 * @param modalWindow
	 * @param listDetailPanel
	 */
	public BiospecimenModalDetailForm(String id, FeedbackPanel feedBackPanel, final ArkCrudContainerVO arkCrudContainerVo, final ModalWindow modalWindow, final CompoundPropertyModel<LimsVO> cpModel) {
		super(id, feedBackPanel, arkCrudContainerVo, cpModel);
		this.modalWindow = modalWindow;
		refreshEntityFromBackend();

		bioTransactionDetailWmc = new WebMarkupContainer("bioTransactionDetailWmc");
		bioTransactionDetailWmc.setOutputMarkupPlaceholderTag(true);
		bioTransactionDetailWmc.setEnabled(cpModel.getObject().getBiospecimen().getId() == null);

		BiospecimenButtonsPanel buttonsPanel = new BiospecimenButtonsPanel("biospecimenButtonPanel", new PropertyModel<Biospecimen>(cpModel.getObject(), "biospecimen")){
			/**
			 * 
			 */
			private static final long	serialVersionUID	= 1L;

			@Override
			public void onAliquot(AjaxRequestTarget target) {
				processOrAliquot(target, au.org.theark.lims.web.Constants.BIOSPECIMEN_PROCESSING_ALIQUOTING, "Sub-aliquot of: ");
				
				// Go straight into edit mode
				onViewEdit(target, BiospecimenModalDetailForm.this);
			}

			@Override
			public void onClone(AjaxRequestTarget target) {
				try {
					LimsVO oldlimsVo = cpModel.getObject();
					final String biospecimenUid = oldlimsVo.getBiospecimen().getBiospecimenUid();
					LimsVO limsVo = new LimsVO();
					PropertyUtils.copyProperties(limsVo, oldlimsVo);
					limsVo.getBiospecimen().setId(null);
					limsVo.getBiospecimen().setBiospecimenUid(UniqueIdGenerator.generateUniqueId());
					limsVo.getBiospecimen().setParentId(oldlimsVo.getBiospecimen().getId());
					limsVo.getBiospecimen().setComments("Clone of " + biospecimenUid);
					limsVo.getBiospecimen().setBarcoded(false);

					// Inital transaction detail (quantity grabbed from previous biospecimen)
					limsVo.getBioTransaction().setId(null);
					org.apache.shiro.subject.Subject currentUser = SecurityUtils.getSubject();
					limsVo.getBioTransaction().setQuantity(oldlimsVo.getBiospecimen().getQuantity());
					limsVo.getBioTransaction().setRecorder(currentUser.getPrincipal().toString());

					iLimsService.createBiospecimen(limsVo);
					cpModel.setObject(limsVo);
					this.info("Biospecimen " + limsVo.getBiospecimen().getBiospecimenUid() + " was cloned from " + biospecimenUid + " successfully");
					target.add(feedbackPanel);
					
					// Go straight into edit mode
					onViewEdit(target, BiospecimenModalDetailForm.this);
				}
				catch (IllegalAccessException e) {
					log.error(e.getMessage());
				}
				catch (InvocationTargetException e) {
					log.error(e.getMessage());
				}
				catch (NoSuchMethodException e) {
					log.error(e.getMessage());
				}
			}

			@Override
			public void onPrintBarcode(AjaxRequestTarget target) {
				// Set barcoded flag to true, as barcode has been printed
				cpModel.getObject().getBiospecimen().setBarcoded(true);
				// Update/refresh
				iLimsService.updateBiospecimen(cpModel.getObject());
				target.add(barcodedChkBox);
				target.add(barcodeImage);
			}

			@Override
			public void onProcess(AjaxRequestTarget target) {
				processOrAliquot(target, au.org.theark.lims.web.Constants.BIOSPECIMEN_PROCESSING_PROCESSING, "Processed from: ");

				// Go straight into edit mode
				onViewEdit(target, BiospecimenModalDetailForm.this);
			}
		};
		buttonsPanel.setVisible(getModelObject().getBiospecimen().getId() != null);
		addOrReplace(buttonsPanel);
	}

	/**
	 * Handle processing or aliquoting of a parent biospecimen
	 * @param target AjxaxRequestTarget
	 * @param processing indication to whether a process or an aliquot
	 * @param comment comment to add to biospecimen comments
	 */
	protected void processOrAliquot(AjaxRequestTarget target, String processing, String comment) {
		final Biospecimen parentBiospecimen = cpModel.getObject().getBiospecimen();
		final String parentBiospecimenUid = parentBiospecimen.getBiospecimenUid();
		final Biospecimen biospecimen = new Biospecimen();
		
		try {
			// Copy parent biospecimen details to new biospecimen
			PropertyUtils.copyProperties(biospecimen, parentBiospecimen);
			
			// Amend specific fields/detail
			biospecimen.setId(null);
			biospecimen.setBiospecimenUid(UniqueIdGenerator.generateUniqueId());
			biospecimen.setParentId(parentBiospecimen.getId());
			biospecimen.setParentUid(parentBiospecimen.getBiospecimenUid());
			biospecimen.setSampleType(parentBiospecimen.getSampleType());
			biospecimen.setBioCollection(parentBiospecimen.getBioCollection());
			biospecimen.setQuantity(null);
			biospecimen.setUnit(parentBiospecimen.getUnit());
			biospecimen.setComments(comment + parentBiospecimenUid);
			biospecimen.setBarcoded(false);
			biospecimen.setUnit(parentBiospecimen.getUnit());
			biospecimen.setTreatmentType(parentBiospecimen.getTreatmentType());
			
			// Reset the biospecimen detail
			cpModel.getObject().setBiospecimen(biospecimen);
			cpModel.getObject().setBiospecimenProcessing(processing);
			
			// Set the bioTransaction detail
			org.apache.shiro.subject.Subject currentUser = SecurityUtils.getSubject();
			cpModel.getObject().getBioTransaction().setRecorder(currentUser.getPrincipal().toString());
			cpModel.getObject().getBioTransaction().setQuantity(null);
			
			enableQuantityTreatment(target);
			
			// refresh the bioTransaction panel
			initialiseBioTransactionListPanel();
			arkCrudContainerVo.getDetailPanelFormContainer().addOrReplace(bioTransactionListPanel);
		}
		catch (IllegalAccessException e) {
			log.error(e.getMessage());
		}
		catch (InvocationTargetException e) {
			log.error(e.getMessage());
		}
		catch (NoSuchMethodException e) {
			log.error(e.getMessage());
		}
	}

	/**
	 * Enable quantity/treatment
	 */
	private void enableQuantityTreatment(AjaxRequestTarget target) {
		setQuantityLabel();
		
		treatmentTypeDdc.setEnabled(true);
		bioTransactionDetailWmc.setEnabled(true);
		
		target.add(treatmentTypeDdc);
		target.add(bioTransactionDetailWmc);
	}

	protected void refreshEntityFromBackend() {
		// Get the Biospecimen entity fresh from backend
		Biospecimen biospecimen = cpModel.getObject().getBiospecimen();

		if (biospecimen.getId() != null) {
			try {
				final Biospecimen biospecimenFromDB = iLimsService.getBiospecimen(biospecimen.getId());
				biospecimenFromDB.setQuantity(iLimsService.getQuantityAvailable(biospecimenFromDB));
				cpModel.getObject().setBiospecimen(biospecimenFromDB);
			}
			catch (EntityNotFoundException e) {
				this.error("Can not edit this record - it has been invalidated (e.g. deleted)");
				log.error(e.getMessage());
			}
		}
	}

	private boolean initialiseBiospecimenCFDataEntry() {
		boolean replacePanel = false;
		Biospecimen biospecimen = cpModel.getObject().getBiospecimen();
		if (!(biospecimenCFDataEntryPanel instanceof BiospecimenCustomDataDataViewPanel)) {
			CompoundPropertyModel<BiospecimenCustomDataVO> bioCFDataCpModel = new CompoundPropertyModel<BiospecimenCustomDataVO>(new BiospecimenCustomDataVO());
			bioCFDataCpModel.getObject().setBiospecimen(biospecimen);
			bioCFDataCpModel.getObject().setArkFunction(iArkCommonService.getArkFunctionByName(au.org.theark.core.Constants.FUNCTION_KEY_VALUE_BIOSPECIMEN));
			biospecimenCFDataEntryPanel = new BiospecimenCustomDataDataViewPanel("biospecimenCFDataEntryPanel", bioCFDataCpModel).initialisePanel(null);
			replacePanel = true;
		}
		return replacePanel;
	}

	private boolean initialiseBioTransactionListPanel() {
		boolean replacePanel = false;
		Biospecimen biospecimen = cpModel.getObject().getBiospecimen();
		if (biospecimen.getId() == null) {
			// Handle for new Biospecimen being created
			bioTransactionListPanel = new EmptyPanel("bioTransactionListPanel");
			replacePanel = true;
		}
		else {
			if (!(bioTransactionListPanel instanceof BioTransactionListPanel)) {
				// Make sure the bioTransaction in the cpModel has the biospecimen in context
				cpModel.getObject().getBioTransaction().setBiospecimen(biospecimen);
				bioTransactionListPanel = new BioTransactionListPanel("bioTransactionListPanel", feedbackPanel, cpModel).initialisePanel();
				replacePanel = true;
			}
		}
		return replacePanel;
	}

	private boolean initialiseBiospecimenLocationPanel() {
		boolean replacePanel = false;
		Biospecimen biospecimen = cpModel.getObject().getBiospecimen();
		if (biospecimen.getId() == null) {
			// Handle for new Biospecimen being created
			biospecimenLocationPanel = new EmptyPanel("biospecimenLocationPanel");
			replacePanel = true;
		}
		else {
			try {
				BiospecimenLocationVO biospecimenLocationVo = iInventoryService.locateBiospecimen(biospecimen);
				cpModel.getObject().setBiospecimenLocationVO(biospecimenLocationVo);
				biospecimenLocationPanel = new BioLocationDetailPanel("biospecimenLocationPanel", cpModel);
				replacePanel = true;
			}
			catch (ArkSystemException e) {
				log.error(e.getMessage());
				biospecimenLocationPanel = new EmptyPanel("biospecimenLocationPanel");
				replacePanel = true;
			}
		}

		return replacePanel;
	}

	public void initialiseDetailForm() {
		idTxtFld = new TextField<String>("biospecimen.id");
		biospecimenUidTxtFld = new TextField<String>("biospecimen.biospecimenUid");
		parentUidTxtFld = new TextField<String>("biospecimen.parentUid");
		commentsTxtAreaFld = new TextArea<String>("biospecimen.comments");
		sampleDateTxtFld = new DateTextField("biospecimen.sampleDate", au.org.theark.core.Constants.DD_MM_YYYY);

		quantityTxtFld = new TextField<Double>("biospecimen.quantity") {
			/**
			 * 
			 */
			private static final long	serialVersionUID	= 1L;

			@SuppressWarnings("unchecked")
			@Override
			public <C> IConverter<C> getConverter(Class<C> type) {
				DoubleConverter doubleConverter = new DoubleConverter();
				NumberFormat numberFormat = NumberFormat.getInstance();
				numberFormat.setMinimumFractionDigits(1);
				doubleConverter.setNumberFormat(getLocale(), numberFormat);
				return (IConverter<C>) doubleConverter;
			}
		};

		quantityTxtFld.setEnabled(false);
		bioTransactionQuantityTxtFld = new TextField<Double>("bioTransaction.quantity");

		setQuantityLabel();

		ArkDatePicker startDatePicker = new ArkDatePicker();
		startDatePicker.bind(sampleDateTxtFld);
		sampleDateTxtFld.add(startDatePicker);

		initSampleTypeDdc();
		initBioCollectionDdc();
		initUnitDdc();
		initTreatmentTypeDdc();

		barcodedChkBox = new CheckBox("biospecimen.barcoded");
		barcodedChkBox.setVisible(true);
		barcodedChkBox.setEnabled(false);
		
		initialiseBarcodeImage();

		initialiseBiospecimenCFDataEntry();
		initialiseBioTransactionListPanel();
		initialiseBiospecimenLocationPanel();

		attachValidators();
		addComponents();

		// Focus on Sample Type
		sampleTypeDdc.add(new ArkDefaultFormFocusBehavior());
	}

	private void initialiseBarcodeImage() {
		barcodeImage = new DataMatrixBarcodeImage("biospecimen.barcodeImage", new PropertyModel<String>(cpModel, "biospecimen.biospecimenUid"));
	}

	/**
	 * Show differing quantity label dependant on new/existing biospecimen
	 */
	private void setQuantityLabel() {
		if (cpModel.getObject().getBiospecimen().getId() == null) {
			quantityLbl = new Label("biospecimen.quantity.label", new ResourceModel("bioTransaction.quantity"));
		}
		else {
			quantityLbl = new Label("biospecimen.quantity.label", new ResourceModel("biospecimen.quantity"));
		}

		quantityNoteLbl = new Label("biospecimen.quantity.note", new ResourceModel("biospecimen.quantity.note"));
		quantityNoteLbl.setVisible(getModelObject().getBiospecimen().getId() != null);

		quantityTxtFld.setVisible(getModelObject().getBiospecimen().getId() != null);
		bioTransactionQuantityTxtFld.setVisible(getModelObject().getBiospecimen().getId() == null);

		bioTransactionDetailWmc.addOrReplace(quantityNoteLbl);
		bioTransactionDetailWmc.addOrReplace(quantityLbl);
	}

	private void initSampleTypeDdc() {
		List<BioSampletype> sampleTypeList = iLimsService.getBioSampleTypes();
		ChoiceRenderer<BioSampletype> choiceRenderer = new ChoiceRenderer<BioSampletype>(Constants.NAME, Constants.ID);
		sampleTypeDdc = new DropDownChoice<BioSampletype>("biospecimen.sampleType", (List<BioSampletype>) sampleTypeList, choiceRenderer);
	}

	private void initBioCollectionDdc() {
		// Get a list of collections for the subject in context by default
		BioCollection bioCollection = new BioCollection();
		bioCollection.setLinkSubjectStudy(cpModel.getObject().getBiospecimen().getLinkSubjectStudy());
		bioCollection.setStudy(cpModel.getObject().getBiospecimen().getLinkSubjectStudy().getStudy());
		try {
			cpModel.getObject().setBioCollectionList(iLimsService.searchBioCollection(bioCollection));

			ChoiceRenderer<BioCollection> choiceRenderer = new ChoiceRenderer<BioCollection>(Constants.NAME, Constants.ID);
			bioCollectionDdc = new DropDownChoice<BioCollection>("biospecimen.bioCollection", cpModel.getObject().getBioCollectionList(), choiceRenderer);
		}
		catch (ArkSystemException e) {
			log.error(e.getMessage());
			this.error("Operation could not be performed - if this persists, contact your Administrator or Support");
		}
	}

	private void initUnitDdc() {
		List<Unit> unitList = iLimsService.getUnits();
		ChoiceRenderer<Unit> choiceRenderer = new ChoiceRenderer<Unit>(Constants.NAME, Constants.ID);
		unitDdc = new DropDownChoice<Unit>("biospecimen.unit", (List<Unit>) unitList, choiceRenderer);
		unitDdc.setNullValid(false);
	}

	private void initTreatmentTypeDdc() {
		List<TreatmentType> treatmentTypeList = iLimsService.getTreatmentTypes();
		ChoiceRenderer<TreatmentType> choiceRenderer = new ChoiceRenderer<TreatmentType>(Constants.NAME, Constants.ID);
		treatmentTypeDdc = new DropDownChoice<TreatmentType>("biospecimen.treatmentType", (List<TreatmentType>) treatmentTypeList, choiceRenderer);
		treatmentTypeDdc.setNullValid(false);
	}

	protected void attachValidators() {
		idTxtFld.setRequired(true);
		biospecimenUidTxtFld.setRequired(true).setLabel(new StringResourceModel("error.biospecimen.biospecimenId.required", this, new Model<String>("Name")));
		sampleTypeDdc.setRequired(true).setLabel(new StringResourceModel("error.biospecimen.sampleType.required", this, new Model<String>("Name")));
		bioCollectionDdc.setRequired(true).setLabel(new StringResourceModel("error.biospecimen.bioCollection.required", this, new Model<String>("Name")));

		// Initial BioTransaction detail
		bioTransactionQuantityTxtFld.setRequired(true).setLabel(new StringResourceModel("error.bioTransaction.quantity.required", this, new Model<String>("Name")));
		MinimumValidator<Double> minQuantityValidator = new MinimumValidator<Double>(Double.MIN_VALUE);
		bioTransactionQuantityTxtFld.add(minQuantityValidator);
		unitDdc.setRequired(true).setLabel(new StringResourceModel("error.biospecimen.unit.required", this, new Model<String>("Name")));
		treatmentTypeDdc.setRequired(true).setLabel(new StringResourceModel("error.biospecimen.treatmentType.required", this, new Model<String>("Name")));
	}

	private void addComponents() {
		arkCrudContainerVo.getDetailPanelFormContainer().add(idTxtFld.setEnabled(false));
		arkCrudContainerVo.getDetailPanelFormContainer().add(biospecimenUidTxtFld.setEnabled(false));
		arkCrudContainerVo.getDetailPanelFormContainer().add(parentUidTxtFld.setEnabled(false));
		arkCrudContainerVo.getDetailPanelFormContainer().add(commentsTxtAreaFld);
		arkCrudContainerVo.getDetailPanelFormContainer().add(sampleDateTxtFld);
		arkCrudContainerVo.getDetailPanelFormContainer().add(sampleTypeDdc);
		arkCrudContainerVo.getDetailPanelFormContainer().add(bioCollectionDdc);
		arkCrudContainerVo.getDetailPanelFormContainer().add(barcodedChkBox);
		arkCrudContainerVo.getDetailPanelFormContainer().addOrReplace(barcodeImage);

		// Quantity label depends on new/existing Biospecimen
		bioTransactionDetailWmc.addOrReplace(quantityNoteLbl);
		bioTransactionDetailWmc.addOrReplace(quantityLbl);
		// initial BioTransaction detail
		bioTransactionDetailWmc.add(quantityTxtFld);
		bioTransactionDetailWmc.add(bioTransactionQuantityTxtFld);
		bioTransactionDetailWmc.add(unitDdc);
		bioTransactionDetailWmc.add(treatmentTypeDdc);

		arkCrudContainerVo.getDetailPanelFormContainer().add(bioTransactionDetailWmc);

		arkCrudContainerVo.getDetailPanelFormContainer().add(biospecimenCFDataEntryPanel);
		arkCrudContainerVo.getDetailPanelFormContainer().add(bioTransactionListPanel);
		arkCrudContainerVo.getDetailPanelFormContainer().add(biospecimenLocationPanel);

		add(arkCrudContainerVo.getDetailPanelFormContainer());
	}

	@Override
	protected void onSave(AjaxRequestTarget target) {
		if (cpModel.getObject().getBiospecimen().getId() == null) {
			// Save/Process/Aliquot
			if(cpModel.getObject().getBiospecimenProcessing().isEmpty()) {
				// Normal Save functionality
				// Inital transaction detail
				org.apache.shiro.subject.Subject currentUser = SecurityUtils.getSubject();
				cpModel.getObject().getBioTransaction().setRecorder(currentUser.getPrincipal().toString());
	
				iLimsService.createBiospecimen(cpModel.getObject());
				this.info("Biospecimen " + cpModel.getObject().getBiospecimen().getBiospecimenUid() + " was created successfully");
				processErrors(target);
			}
			else if (cpModel.getObject().getBiospecimenProcessing().equalsIgnoreCase(au.org.theark.lims.web.Constants.BIOSPECIMEN_PROCESSING_PROCESSING)) {
				// Process the biospecimen
				try {
					LimsVO limsVo = cpModel.getObject();
					Biospecimen biospecimen = limsVo.getBiospecimen();
					BioTransaction bioTransaction = limsVo.getBioTransaction();
					Biospecimen parentBiospecimen = iLimsService.getBiospecimen(biospecimen.getParentId());
					 
					if(bioTransaction.getQuantity() > parentBiospecimen.getQuantity()) {
						this.error("Cannot process more than the total amount of the parent biospecimen");
						processErrors(target);
					}
					else {
						// Process the biospecimen and it's parent
						iLimsService.createBiospecimen(limsVo);
						
						// Add parent transaction
						LimsVO parentLimsVo = new LimsVO();
						parentLimsVo.setBiospecimen(parentBiospecimen);
						parentLimsVo.getBioTransaction().setId(null);
						parentLimsVo.getBioTransaction().setBiospecimen(parentBiospecimen);
						parentLimsVo.getBioTransaction().setTransactionDate(Calendar.getInstance().getTime());
						parentLimsVo.getBioTransaction().setQuantity(biospecimen.getQuantity());
						parentLimsVo.getBioTransaction().setReason("Processed for: " + biospecimen.getBiospecimenUid());
						iLimsService.createBioTransaction(parentLimsVo);
					}
				}
				catch (EntityNotFoundException e) {
					log.error(e.getMessage());
				}
			}
			else if (cpModel.getObject().getBiospecimenProcessing().equalsIgnoreCase(au.org.theark.lims.web.Constants.BIOSPECIMEN_PROCESSING_ALIQUOTING)) {
				// Aliquot the biospecimen
				try {
					LimsVO limsVo = cpModel.getObject();
					Biospecimen biospecimen = limsVo.getBiospecimen();
					BioTransaction bioTransaction = limsVo.getBioTransaction();
					Biospecimen parentBiospecimen = iLimsService.getBiospecimen(biospecimen.getParentId());
					 
					if(bioTransaction.getQuantity() > parentBiospecimen.getQuantity()) {
						this.error("Cannot aliquot more than the total amount of the parent biospecimen");
						processErrors(target);
					}
					else {
						// Aliquot the biospecimen and it's parent
						iLimsService.createBiospecimen(limsVo);
						
						// Add parent transaction
						LimsVO parentLimsVo = new LimsVO();
						parentLimsVo.setBiospecimen(parentBiospecimen);
						parentLimsVo.getBioTransaction().setId(null);
						parentLimsVo.getBioTransaction().setBiospecimen(parentBiospecimen);
						parentLimsVo.getBioTransaction().setTransactionDate(Calendar.getInstance().getTime());
						parentLimsVo.getBioTransaction().setQuantity(biospecimen.getQuantity());
						parentLimsVo.getBioTransaction().setReason("Sub-Aliquot for: " + biospecimen.getBiospecimenUid());
						iLimsService.createBioTransaction(parentLimsVo);
					}
				}
				catch (EntityNotFoundException e) {
					log.error(e.getMessage());
				}
			}
			
			// Disable initial transaction details, and hide inital quantity text box
			bioTransactionDetailWmc.setEnabled(false);
			bioTransactionQuantityTxtFld.setVisible(false);
			quantityTxtFld.setVisible(true);
			quantityTxtFld.setModelObject(bioTransactionQuantityTxtFld.getModelObject());
			target.add(bioTransactionDetailWmc);

			barcodeImage.setVisible(cpModel.getObject().getBiospecimen().getBarcoded());
			target.add(barcodeImage);
		}
		else {
			// Update
			iLimsService.updateBiospecimen(cpModel.getObject());
			this.info("Biospecimen " + cpModel.getObject().getBiospecimen().getBiospecimenUid() + " was updated successfully");
			processErrors(target);

			// Hide/show barcode image
			barcodeImage.setVisible(cpModel.getObject().getBiospecimen().getBarcoded());
			target.add(barcodeImage);
		}
		// Allow the Biospecimen custom data to be saved any time save is performed
		if (biospecimenCFDataEntryPanel instanceof BiospecimenCustomDataDataViewPanel) {
			((BiospecimenCustomDataDataViewPanel) biospecimenCFDataEntryPanel).saveCustomData();
		}
		// refresh the CF data entry panel (if necessary)
		if (initialiseBiospecimenCFDataEntry() == true) {
			arkCrudContainerVo.getDetailPanelFormContainer().addOrReplace(biospecimenCFDataEntryPanel);
		}
		// refresh the bio transactions (if necessary)
		if (initialiseBioTransactionListPanel() == true) {
			arkCrudContainerVo.getDetailPanelFormContainer().addOrReplace(bioTransactionListPanel);
		}

		setQuantityLabel();
		onSavePostProcess(target);
	}

	@Override
	protected void onClose(AjaxRequestTarget target) {
		target.add(feedbackPanel);
		modalWindow.close(target);
	}

	@Override
	protected void onDeleteConfirmed(AjaxRequestTarget target, Form<?> form) {
		iLimsService.deleteBiospecimen(cpModel.getObject());
		this.info("Biospecimen " + cpModel.getObject().getBiospecimen().getBiospecimenUid() + " was deleted successfully");

		onClose(target);
	}

	@Override
	protected void processErrors(AjaxRequestTarget target) {
		target.add(feedbackPanel);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see au.org.theark.core.web.form.AbstractDetailForm#isNew()
	 */
	@Override
	protected boolean isNew() {
		if (cpModel.getObject().getBiospecimen().getId() == null) {
			return true;
		}
		else {
			return false;
		}
	}
	
	public CheckBox getBarcodedChkBox() {
		return barcodedChkBox;
	}

	public void setBarcodedChkBox(CheckBox barcodedChkBox) {
		this.barcodedChkBox = barcodedChkBox;
	}

	public NonCachingImage getBarcodeImage() {
		return barcodeImage;
	}

	public void setBarcodeImage(NonCachingImage barcodeImage) {
		this.barcodeImage = barcodeImage;
	}
}