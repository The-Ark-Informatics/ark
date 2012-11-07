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
import java.util.Date;
import java.util.List;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.extensions.markup.html.form.DateTextField;
import org.apache.wicket.extensions.yui.calendar.DateTimeField;
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
import org.apache.wicket.util.convert.converter.BigDecimalConverter;
import org.apache.wicket.validation.validator.MinimumValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import au.org.theark.core.exception.ArkSystemException;
import au.org.theark.core.exception.EntityNotFoundException;
import au.org.theark.core.model.lims.entity.BioCollection;
import au.org.theark.core.model.lims.entity.BioSampletype;
import au.org.theark.core.model.lims.entity.BioTransaction;
import au.org.theark.core.model.lims.entity.BioTransactionStatus;
import au.org.theark.core.model.lims.entity.Biospecimen;
import au.org.theark.core.model.lims.entity.BiospecimenAnticoagulant;
import au.org.theark.core.model.lims.entity.BiospecimenGrade;
import au.org.theark.core.model.lims.entity.BiospecimenProtocol;
import au.org.theark.core.model.lims.entity.BiospecimenQuality;
import au.org.theark.core.model.lims.entity.BiospecimenStatus;
import au.org.theark.core.model.lims.entity.BiospecimenStorage;
import au.org.theark.core.model.lims.entity.TreatmentType;
import au.org.theark.core.model.lims.entity.Unit;
import au.org.theark.core.model.study.entity.Study;
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

	private static final long									serialVersionUID	= 2727419197330261916L;
	private static final Logger								log					= LoggerFactory.getLogger(BiospecimenModalDetailForm.class);
	@SpringBean(name = au.org.theark.core.Constants.ARK_COMMON_SERVICE)
	private IArkCommonService<Void>							iArkCommonService;

	@SpringBean(name = au.org.theark.lims.web.Constants.LIMS_SERVICE)
	private ILimsService											iLimsService;

	@SpringBean(name = au.org.theark.lims.web.Constants.LIMS_INVENTORY_SERVICE)
	private IInventoryService									iInventoryService;

	private TextField<String>									idTxtFld;
	private TextField<String>									biospecimenUidTxtFld;
	private TextField<String>									parentUidTxtFld;
	private DropDownChoice<BioSampletype>					sampleTypeDdc;
	private DropDownChoice<BioCollection>					bioCollectionDdc;

	private DateTextField										sampleDateTxtFld;
	private DateTimeField										sampleTimeTxtFld;
	private DateTextField										processedDateTxtFld;
	private DateTimeField										processedTimeTxtFld;

	private TextArea<String>									commentsTxtAreaFld;

	private CheckBox												barcodedChkBox;
	private NonCachingImage										barcodeImage;

	private DropDownChoice<BiospecimenGrade>				gradeDdc;
	private DropDownChoice<BiospecimenStorage>			storedInDdc;
	private DropDownChoice<BiospecimenAnticoagulant>	anticoagDdc;
	private DropDownChoice<BiospecimenStatus>				statusDdc;
	private DropDownChoice<BiospecimenQuality>			qualityDdc;
	private DropDownChoice<BiospecimenProtocol>			biospecimenProtocol;
	private TextField<Number>									purity;

	private Label													quantityLbl;
	private Label													quantityNoteLbl;
	private TextField<Double>									quantityTxtFld;
	private TextField<Double>									bioTransactionQuantityTxtFld;
	private DropDownChoice<Unit>								unitDdc;
	private DropDownChoice<TreatmentType>					treatmentTypeDdc;

	private TextField<Number>									concentrationTxtFld;
	private Label													amountLbl;

	private Panel													biospecimenLocationPanel;
	private WebMarkupContainer									bioTransactionDetailWmc;
	private Panel													bioTransactionListPanel;

	private Panel													biospecimenCFDataEntryPanel;

	private BiospecimenButtonsPanel							biospecimenbuttonsPanel;
	private ModalWindow											modalWindow;

	private AjaxLink<Date>										useCollectionDate;

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
	}

	public void onBeforeRender() {
		refreshEntityFromBackend();
		Biospecimen biospecimen = this.getModelObject().getBiospecimen();
		if (biospecimen != null) {
			Study study = biospecimen.getStudy();
			if (study != null && !study.getAutoGenerateBiospecimenUid()) {
				biospecimenUidTxtFld.setEnabled(true);
			}
			else {
				biospecimenUidTxtFld.setEnabled(false);
			}
		}
		super.onBeforeRender();
	};

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

				// Get/set location details
				cpModel.getObject().setBiospecimenLocationVO(iInventoryService.getBiospecimenLocation(biospecimenFromDB));
				cpModel.getObject().setStudy(biospecimenFromDB.getStudy());
			}
			catch (EntityNotFoundException e) {
				this.error("Can not edit this record - it has been invalidated (e.g. deleted)");
				log.error(e.getMessage());
			}
			catch (ArkSystemException e) {
				log.error(e.getMessage());
			}
		}
	}

	private void initialiseBiospecimenButtonsPanel() {
		biospecimenbuttonsPanel = new BiospecimenButtonsPanel("biospecimenButtonPanel", new PropertyModel<Biospecimen>(cpModel.getObject(), "biospecimen")) {

			private static final long	serialVersionUID	= 1L;

			@Override
			public void onAliquot(AjaxRequestTarget target) {
				processOrAliquot(target, au.org.theark.lims.web.Constants.BIOSPECIMEN_PROCESSING_ALIQUOTING, "Sub-aliquot of: ");

				// Go straight into edit mode
				onViewEdit(target, BiospecimenModalDetailForm.this);
			}

			@Override
			public void onClone(AjaxRequestTarget target) {
				onCloneBiospecimen(target);
				
				// Go straight into edit mode
				onViewEdit(target, BiospecimenModalDetailForm.this);
			}

			@Override
			public void onPrintBarcode(AjaxRequestTarget target) {
				// Set barcoded flag to true, as barcode has been printed
				cpModel.getObject().getBiospecimen().setBarcoded(true);
				// Update/refresh

				try {
					iLimsService.updateBiospecimen(cpModel.getObject());
					target.add(barcodedChkBox);
					target.add(barcodeImage);
				}
				catch (ArkSystemException e) {
					this.error(e.getMessage());
				}
			}

			@Override
			public void onProcess(AjaxRequestTarget target) {
				processOrAliquot(target, au.org.theark.lims.web.Constants.BIOSPECIMEN_PROCESSING_PROCESSING, "Processed from: ");

				// Go straight into edit mode
				onViewEdit(target, BiospecimenModalDetailForm.this);
			}

			@Override
			public void onPrintStrawBarcode(AjaxRequestTarget target) {
				// Set barcoded flag to true, as straw barcode has been printed
				cpModel.getObject().getBiospecimen().setBarcoded(true);
				// Update/refresh
				try {
					iLimsService.updateBiospecimen(cpModel.getObject());
					target.add(barcodedChkBox);
				}
				catch (ArkSystemException e) {
					this.error(e.getMessage());
				}
			}
		};
		biospecimenbuttonsPanel.setVisible(getModelObject().getBiospecimen().getId() != null);

		// Disable Process/Aliquot buttons if new, or quantity available is 0
		boolean enabled = true;
		if (cpModel.getObject().getBiospecimen().getId() == null
				|| (cpModel.getObject().getBiospecimen().getQuantity() != null && cpModel.getObject().getBiospecimen().getQuantity().equals(new Double(0)))) {
			enabled = false;
		}
		biospecimenbuttonsPanel.setProcessButtonEnabled(enabled);
		biospecimenbuttonsPanel.setAliquotButtonEnabled(enabled);

		addOrReplace(biospecimenbuttonsPanel);
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
		// Cannot allocate a Biospecimen until saved

		Biospecimen biospecimen = cpModel.getObject().getBiospecimen();
		if (biospecimen.getId() == null) {
			// Handle for new Biospecimen being created
			biospecimenLocationPanel = new EmptyPanel("biospecimenLocationPanel");
			replacePanel = true;
		}
		else {
			if (!(biospecimenLocationPanel instanceof BioLocationDetailPanel)) {
				biospecimenLocationPanel = new BioLocationDetailPanel("biospecimenLocationPanel", cpModel);
				replacePanel = true;
			}
		}
		return replacePanel;
	}

	public void initialiseDetailForm() {
		idTxtFld = new TextField<String>("biospecimen.id");
		biospecimenUidTxtFld = new TextField<String>("biospecimen.biospecimenUid");
		biospecimenUidTxtFld.add(new AjaxFormComponentUpdatingBehavior("onchange") {
			/**
			 * 
			 */
			private static final long	serialVersionUID	= 1L;

			@Override
			protected void onUpdate(AjaxRequestTarget target) {
				// Check BiospecimenUID is unique
				String biospecimenUid = (getComponent().getDefaultModelObject().toString() != null ? getComponent().getDefaultModelObject().toString() : new String());
				Biospecimen biospecimen = iLimsService.getBiospecimenByUid(biospecimenUid, cpModel.getObject().getBiospecimen().getStudy());
				if (biospecimen != null && biospecimen.getId() != null) {
					error("Biospecimen UID must be unique. Please try again.");
					target.focusComponent(getComponent());
				}
				target.add(feedbackPanel);
			}
		});

		parentUidTxtFld = new TextField<String>("biospecimen.parentUid");
		commentsTxtAreaFld = new TextArea<String>("biospecimen.comments");
		sampleDateTxtFld = new DateTextField("biospecimen.sampleDate", au.org.theark.core.Constants.DD_MM_YYYY);
		useCollectionDate = new AjaxLink<Date>("useCollectionDate") {
			private static final long	serialVersionUID	= 1L;

			@Override
			public void onClick(AjaxRequestTarget target) {
				cpModel.getObject().getBiospecimen().setSampleDate(cpModel.getObject().getBiospecimen().getBioCollection().getCollectionDate());
				target.add(sampleDateTxtFld);
			}
		};

		sampleTimeTxtFld = new DateTimeField("biospecimen.sampleTime") {

			private static final long	serialVersionUID	= 1L;

			@Override
			protected void onBeforeRender() {
				this.getDateTextField().setVisibilityAllowed(false);
				super.onBeforeRender();
			}

			@Override
			protected void convertInput() {
				// Slight change to not default to today's date
				Date modelObject = (Date) getDefaultModelObject();
				getDateTextField().setConvertedInput(modelObject != null ? modelObject : null);
				super.convertInput();
			}
		};

		processedDateTxtFld = new DateTextField("biospecimen.processedDate", au.org.theark.core.Constants.DD_MM_YYYY);
		processedTimeTxtFld = new DateTimeField("biospecimen.processedTime") {

			private static final long	serialVersionUID	= 1L;

			@Override
			protected void onBeforeRender() {
				this.getDateTextField().setVisibilityAllowed(false);
				super.onBeforeRender();
			}

			@Override
			protected void convertInput() {
				// Slight change to not default to today's date
				Date modelObject = (Date) getDefaultModelObject();
				getDateTextField().setConvertedInput(modelObject != null ? modelObject : null);
				super.convertInput();
			}
		};

		ArkDatePicker sampleDatePicker = new ArkDatePicker();
		sampleDatePicker.bind(sampleDateTxtFld);
		sampleDateTxtFld.add(sampleDatePicker);

		ArkDatePicker processedDatePicker = new ArkDatePicker();
		processedDatePicker.bind(processedDateTxtFld);
		processedDateTxtFld.add(processedDatePicker);

		quantityTxtFld = new TextField<Double>("biospecimen.quantity") {

			private static final long	serialVersionUID	= 1L;

			@SuppressWarnings("unchecked")
			@Override
			public <C> IConverter<C> getConverter(Class<C> type) {
				BigDecimalConverter doubleConverter = new BigDecimalConverter();
				NumberFormat numberFormat = NumberFormat.getInstance();
				numberFormat.setMinimumFractionDigits(1);
				numberFormat.setMaximumFractionDigits(10);
				doubleConverter.setNumberFormat(getLocale(), numberFormat);
				return (IConverter<C>) doubleConverter;
			}
		};

		quantityTxtFld.setEnabled(false);
		bioTransactionQuantityTxtFld = new TextField<Double>("bioTransaction.quantity") {

			private static final long	serialVersionUID	= 1L;

			@SuppressWarnings("unchecked")
			@Override
			public <C> IConverter<C> getConverter(Class<C> type) {
				BigDecimalConverter doubleConverter = new BigDecimalConverter();
				NumberFormat numberFormat = NumberFormat.getInstance();
				numberFormat.setMinimumFractionDigits(1);
				numberFormat.setMaximumFractionDigits(10);
				doubleConverter.setNumberFormat(getLocale(), numberFormat);
				return (IConverter<C>) doubleConverter;
			}
		};
		bioTransactionQuantityTxtFld.add(new AjaxFormComponentUpdatingBehavior("onchange") {
			private static final long	serialVersionUID	= 1L;

			@Override
			protected void onUpdate(AjaxRequestTarget target) {
				target.add(amountLbl);
			}
		});

		concentrationTxtFld = new TextField<Number>("biospecimen.concentration");
		concentrationTxtFld.add(new AjaxFormComponentUpdatingBehavior("onchange") {
			private static final long	serialVersionUID	= 1L;

			@Override
			protected void onUpdate(AjaxRequestTarget target) {
				target.add(amountLbl);
			}
		});
		amountLbl = new Label("biospecimen.amount", new Model<Number>() {
			private static final long	serialVersionUID	= 1L;

			@Override
			public Number getObject() {
				Number concentration = ((concentrationTxtFld.getModelObject() == null) ? 0 : concentrationTxtFld.getModelObject());
				Number quantity = null;
				if (bioTransactionQuantityTxtFld.isVisible()) {
					quantity = ((bioTransactionQuantityTxtFld.getModelObject() == null) ? 0 : bioTransactionQuantityTxtFld.getModelObject());
				}
				else {
					quantity = ((quantityTxtFld.getModelObject() == null) ? 0 : quantityTxtFld.getModelObject());
				}
				Number amount = (concentration.doubleValue() * quantity.doubleValue());
				return amount;
			}
		});
		amountLbl.setOutputMarkupPlaceholderTag(true);

		setQuantityLabel();
		initSampleTypeDdc();
		initBioCollectionDdc();
		initUnitDdc();
		initTreatmentTypeDdc();
		initGradeDdc();
		initStoredInDdc();
		initAnticoagDdc();
		initStatusDdc();
		initQualityDdc();
		initBiospecimenProtocol();
		purity = new TextField<Number>("biospecimen.purity");

		barcodedChkBox = new CheckBox("biospecimen.barcoded");
		barcodedChkBox.setVisible(true);
		barcodedChkBox.setEnabled(false);

		initialiseBarcodeImage();

		initialiseBiospecimenCFDataEntry();
		initialiseBioTransactionListPanel();
		initialiseBiospecimenLocationPanel();
		initialiseBiospecimenButtonsPanel();

		attachValidators();
		addComponents();

		// Focus on Sample Type
		sampleTypeDdc.add(new ArkDefaultFormFocusBehavior());
	}

	private void initBiospecimenProtocol() {
		List<BiospecimenProtocol> list = iLimsService.getBiospecimenProtocolList();
		ChoiceRenderer<BiospecimenProtocol> choiceRenderer = new ChoiceRenderer<BiospecimenProtocol>(Constants.NAME, Constants.ID);
		biospecimenProtocol = new DropDownChoice<BiospecimenProtocol>("biospecimen.biospecimenProtocol", list, choiceRenderer);
		biospecimenProtocol.setNullValid(false);
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
			// Amended renderer to display BiocollectionUID rather than name
			ChoiceRenderer<BioCollection> choiceRenderer = new ChoiceRenderer<BioCollection>("biocollectionUid", Constants.ID);
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

	private void initGradeDdc() {
		List<BiospecimenGrade> list = iLimsService.getBiospecimenGradeList();
		ChoiceRenderer<BiospecimenGrade> choiceRenderer = new ChoiceRenderer<BiospecimenGrade>(Constants.NAME, Constants.ID);
		gradeDdc = new DropDownChoice<BiospecimenGrade>("biospecimen.grade", list, choiceRenderer);
		gradeDdc.setNullValid(false);
	}

	private void initStoredInDdc() {
		List<BiospecimenStorage> list = iLimsService.getBiospecimenStorageList();
		ChoiceRenderer<BiospecimenStorage> choiceRenderer = new ChoiceRenderer<BiospecimenStorage>(Constants.NAME, Constants.ID);
		storedInDdc = new DropDownChoice<BiospecimenStorage>("biospecimen.storedIn", list, choiceRenderer);
		storedInDdc.setNullValid(false);
	}

	private void initAnticoagDdc() {
		List<BiospecimenAnticoagulant> list = iLimsService.getBiospecimenAnticoagulantList();
		ChoiceRenderer<BiospecimenAnticoagulant> choiceRenderer = new ChoiceRenderer<BiospecimenAnticoagulant>(Constants.NAME, Constants.ID);
		anticoagDdc = new DropDownChoice<BiospecimenAnticoagulant>("biospecimen.anticoag", list, choiceRenderer);
		anticoagDdc.setNullValid(false);
	}

	private void initStatusDdc() {
		List<BiospecimenStatus> list = iLimsService.getBiospecimenStatusList();
		ChoiceRenderer<BiospecimenStatus> choiceRenderer = new ChoiceRenderer<BiospecimenStatus>(Constants.NAME, Constants.ID);
		statusDdc = new DropDownChoice<BiospecimenStatus>("biospecimen.status", list, choiceRenderer);
		statusDdc.setNullValid(false);
	}

	private void initQualityDdc() {
		List<BiospecimenQuality> list = iLimsService.getBiospecimenQualityList();
		ChoiceRenderer<BiospecimenQuality> choiceRenderer = new ChoiceRenderer<BiospecimenQuality>(Constants.NAME, Constants.ID);
		qualityDdc = new DropDownChoice<BiospecimenQuality>("biospecimen.quality", list, choiceRenderer);
		qualityDdc.setNullValid(false);
	}

	protected void attachValidators() {
		idTxtFld.setRequired(true);
		biospecimenUidTxtFld.setRequired(true).setLabel(new StringResourceModel("error.biospecimen.biospecimenId.required", this, new Model<String>("Name")));
		sampleTypeDdc.setRequired(true).setLabel(new StringResourceModel("error.biospecimen.sampleType.required", this, new Model<String>("Name")));
		bioCollectionDdc.setRequired(true).setLabel(new StringResourceModel("error.biospecimen.bioCollection.required", this, new Model<String>("Name")));

		// Initial BioTransaction detail
		bioTransactionQuantityTxtFld.setRequired(true).setLabel(new StringResourceModel("error.bioTransaction.quantity.required", this, new Model<String>("Name")));
		MinimumValidator<Double> minQuantityValidator = new MinimumValidator<Double>(new Double(0.0));
		bioTransactionQuantityTxtFld.add(minQuantityValidator);
		unitDdc.setRequired(true).setLabel(new StringResourceModel("error.biospecimen.unit.required", this, new Model<String>("Name")));
		treatmentTypeDdc.setRequired(true).setLabel(new StringResourceModel("error.biospecimen.treatmentType.required", this, new Model<String>("Name")));
	}

	private void addComponents() {
		arkCrudContainerVo.getDetailPanelFormContainer().add(idTxtFld.setEnabled(false));
		arkCrudContainerVo.getDetailPanelFormContainer().add(biospecimenUidTxtFld.setEnabled(false));
		arkCrudContainerVo.getDetailPanelFormContainer().add(parentUidTxtFld.setEnabled(false));
		arkCrudContainerVo.getDetailPanelFormContainer().add(sampleTypeDdc);
		arkCrudContainerVo.getDetailPanelFormContainer().add(sampleDateTxtFld);
		arkCrudContainerVo.getDetailPanelFormContainer().add(useCollectionDate);
		arkCrudContainerVo.getDetailPanelFormContainer().add(sampleTimeTxtFld);
		arkCrudContainerVo.getDetailPanelFormContainer().add(processedDateTxtFld);
		arkCrudContainerVo.getDetailPanelFormContainer().add(processedTimeTxtFld);
		arkCrudContainerVo.getDetailPanelFormContainer().add(sampleTimeTxtFld);
		arkCrudContainerVo.getDetailPanelFormContainer().add(commentsTxtAreaFld);
		arkCrudContainerVo.getDetailPanelFormContainer().add(bioCollectionDdc);
		arkCrudContainerVo.getDetailPanelFormContainer().add(barcodedChkBox);
		arkCrudContainerVo.getDetailPanelFormContainer().addOrReplace(barcodeImage);
		arkCrudContainerVo.getDetailPanelFormContainer().add(gradeDdc);
		arkCrudContainerVo.getDetailPanelFormContainer().add(storedInDdc);
		arkCrudContainerVo.getDetailPanelFormContainer().add(anticoagDdc);
		arkCrudContainerVo.getDetailPanelFormContainer().add(statusDdc);
		arkCrudContainerVo.getDetailPanelFormContainer().add(qualityDdc);
		arkCrudContainerVo.getDetailPanelFormContainer().add(biospecimenProtocol);
		arkCrudContainerVo.getDetailPanelFormContainer().add(purity);
		
		// Quantity label depends on new/existing Biospecimen
		bioTransactionDetailWmc.addOrReplace(quantityNoteLbl);
		bioTransactionDetailWmc.addOrReplace(quantityLbl);
		// initial BioTransaction detail
		bioTransactionDetailWmc.add(quantityTxtFld);
		bioTransactionDetailWmc.add(bioTransactionQuantityTxtFld);
		bioTransactionDetailWmc.add(unitDdc);
		bioTransactionDetailWmc.add(treatmentTypeDdc);

		arkCrudContainerVo.getDetailPanelFormContainer().add(concentrationTxtFld);
		arkCrudContainerVo.getDetailPanelFormContainer().add(amountLbl);

		arkCrudContainerVo.getDetailPanelFormContainer().add(bioTransactionDetailWmc);

		arkCrudContainerVo.getDetailPanelFormContainer().add(biospecimenCFDataEntryPanel);
		arkCrudContainerVo.getDetailPanelFormContainer().add(bioTransactionListPanel);
		arkCrudContainerVo.getDetailPanelFormContainer().add(biospecimenLocationPanel);

		add(arkCrudContainerVo.getDetailPanelFormContainer());
	}

	@Override
	protected void onSave(AjaxRequestTarget target) {
		boolean saveOk = true;
		org.apache.shiro.subject.Subject currentUser = SecurityUtils.getSubject();
		try {
			if (cpModel.getObject().getBiospecimen().getId() == null) {
				// Save/Process/Aliquot
				if (cpModel.getObject().getBiospecimenProcessing().isEmpty()) {
					// Normal Save functionality
					// Initial transaction detail
					currentUser = SecurityUtils.getSubject();
					cpModel.getObject().getBioTransaction().setRecorder(currentUser.getPrincipal().toString());

					iLimsService.createBiospecimen(cpModel.getObject());

					// Update location
					if (cpModel.getObject().getBiospecimenLocationVO().getIsAllocated()) {
						if (cpModel.getObject().getInvCell() != null && cpModel.getObject().getInvCell().getBiospecimen() != null) {
							iInventoryService.updateInvCell(cpModel.getObject().getInvCell());
						}
					}
					this.info("Biospecimen " + cpModel.getObject().getBiospecimen().getBiospecimenUid() + " was created successfully");
					setQuantityLabel();
				}
				else if (cpModel.getObject().getBiospecimenProcessing().equalsIgnoreCase(au.org.theark.lims.web.Constants.BIOSPECIMEN_PROCESSING_PROCESSING)) {
					// Process the biospecimen
					try {
						LimsVO limsVo = cpModel.getObject();
						Biospecimen biospecimen = limsVo.getBiospecimen();
						BioTransaction bioTransaction = limsVo.getBioTransaction();
						Biospecimen parentBiospecimen = iLimsService.getBiospecimen(biospecimen.getParent().getId());

						if (bioTransaction.getQuantity() > parentBiospecimen.getQuantity()) {
							StringBuffer errorMessage = new StringBuffer();

							errorMessage.append("Cannot process more than ");
							errorMessage.append(parentBiospecimen.getQuantity());
							errorMessage.append(parentBiospecimen.getUnit().getName());
							errorMessage.append(" of the parent biospecimen");

							this.error(errorMessage);
							saveOk = false;
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
							parentLimsVo.getBioTransaction().setQuantity(bioTransaction.getQuantity());
							parentLimsVo.getBioTransaction().setReason("Processed for: " + biospecimen.getBiospecimenUid());
							parentLimsVo.getBioTransaction().setRecorder(currentUser.getPrincipal().toString());

							// NOTE: Removing from parent is negative-value transaction
							parentLimsVo.getBioTransaction().setQuantity(bioTransaction.getQuantity() * -1);
							BioTransactionStatus bioTransactionStatus = iLimsService.getBioTransactionStatusByName("Processed");
							parentLimsVo.getBioTransaction().setStatus(bioTransactionStatus);
							iLimsService.createBioTransaction(parentLimsVo);

							this.info("Biospecimen " + cpModel.getObject().getBiospecimen().getBiospecimenUid() + " was created successfully");
							setQuantityLabel();
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
						Biospecimen parentBiospecimen = iLimsService.getBiospecimen(biospecimen.getParent().getId());

						if (bioTransaction.getQuantity() > parentBiospecimen.getQuantity()) {
							StringBuffer errorMessage = new StringBuffer();

							errorMessage.append("Cannot aliquot more than ");
							errorMessage.append(parentBiospecimen.getQuantity());
							errorMessage.append(parentBiospecimen.getUnit().getName());
							errorMessage.append(" of the parent biospecimen");

							this.error(errorMessage);
							saveOk = false;
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
							parentLimsVo.getBioTransaction().setReason("Sub-Aliquot for: " + biospecimen.getBiospecimenUid());
							parentLimsVo.getBioTransaction().setRecorder(currentUser.getPrincipal().toString());

							// NOTE: Removing from parent is negative-value transaction
							parentLimsVo.getBioTransaction().setQuantity(bioTransaction.getQuantity() * -1);
							BioTransactionStatus bioTransactionStatus = iLimsService.getBioTransactionStatusByName("Aliquoted");
							parentLimsVo.getBioTransaction().setStatus(bioTransactionStatus);
							iLimsService.createBioTransaction(parentLimsVo);

							this.info("Biospecimen " + cpModel.getObject().getBiospecimen().getBiospecimenUid() + " was created successfully");
							setQuantityLabel();
						}
					}
					catch (EntityNotFoundException e) {
						log.error(e.getMessage());
					}
				}
			}
			else {
				// Update location
				if (cpModel.getObject().getBiospecimenLocationVO().getIsAllocated()) {
					if (cpModel.getObject().getInvCell() != null && cpModel.getObject().getInvCell().getBiospecimen() != null) {
						iInventoryService.updateInvCell(cpModel.getObject().getInvCell());
					}
				}

				// Update qty avail
				cpModel.getObject().getBiospecimen().setQuantity(iLimsService.getQuantityAvailable(cpModel.getObject().getBiospecimen()));

				// Update biospecimen
				iLimsService.updateBiospecimen(cpModel.getObject());

				this.info("Biospecimen " + cpModel.getObject().getBiospecimen().getBiospecimenUid() + " was updated successfully");

				// Hide/show barcode image
				barcodeImage.setVisible(cpModel.getObject().getBiospecimen().getBarcoded());
				target.add(barcodeImage);
				setQuantityLabel();
			}

			// Allow the Biospecimen custom data to be saved any time save is performed
			if (biospecimenCFDataEntryPanel instanceof BiospecimenCustomDataDataViewPanel) {
				((BiospecimenCustomDataDataViewPanel) biospecimenCFDataEntryPanel).saveCustomData();
			}

			// refresh the custom field data entry panel (if necessary)
			if (initialiseBiospecimenCFDataEntry()) {
				arkCrudContainerVo.getDetailPanelFormContainer().addOrReplace(biospecimenCFDataEntryPanel);
			}

			// refresh the bio transactions (if necessary)
			if (initialiseBioTransactionListPanel()) {
				arkCrudContainerVo.getDetailPanelFormContainer().addOrReplace(bioTransactionListPanel);
			}

			// refresh the location panel
			if (initialiseBiospecimenLocationPanel()) {
				arkCrudContainerVo.getDetailPanelFormContainer().addOrReplace(biospecimenLocationPanel);
			}

			if (saveOk) {
				// Disable initial transaction details, and hide inital quantity text box
				bioTransactionDetailWmc.setEnabled(false);
				bioTransactionQuantityTxtFld.setVisible(false);
				quantityTxtFld.setVisible(true);
				// quantityTxtFld.setModelObject(bioTransactionQuantityTxtFld.getModelObject());
				target.add(bioTransactionDetailWmc);

				barcodeImage.setVisible(cpModel.getObject().getBiospecimen().getBarcoded());
				target.add(barcodeImage);

				// Enable/re-enable buttons panel
				initialiseBiospecimenButtonsPanel();

				target.add(biospecimenbuttonsPanel);

				onSavePostProcess(target);
			}
		}
		catch (ArkSystemException e) {
			this.error(e.getMessage());
		}
		processErrors(target);
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

	/**
	 * Takes all details of a biospecimen, copies to a new Biospecimen, then allows editing of details before save
	 * @param target
	 */
	protected void onCloneBiospecimen(AjaxRequestTarget target) {
		final Biospecimen clonedBiospecimen = cpModel.getObject().getBiospecimen();
		final String clonedBiospecimenUid = clonedBiospecimen.getBiospecimenUid();
		final Biospecimen biospecimen = new Biospecimen();

		try {
			// Copy parent biospecimen details to new biospecimen
			PropertyUtils.copyProperties(biospecimen, clonedBiospecimen);

			// Amend specific fields/detail
			biospecimen.setId(null);

			if (biospecimen.getStudy().getAutoGenerateBiospecimenUid()) {
				biospecimen.setBiospecimenUid(Constants.AUTO_GENERATED);
			}
			else {
				biospecimen.setBiospecimenUid(null);
			}

			// Cloning not a child biospecimen
			//biospecimen.setParent(clonedBiospecimen);
			//biospecimen.setParentUid(clonedBiospecimen.getBiospecimenUid());
			
			biospecimen.setSampleType(clonedBiospecimen.getSampleType());
			biospecimen.setBioCollection(clonedBiospecimen.getBioCollection());
			biospecimen.setQuantity(null);
			biospecimen.setUnit(clonedBiospecimen.getUnit());
			biospecimen.setComments("Clone of " +  clonedBiospecimenUid);
			biospecimen.setBarcoded(false);
			biospecimen.setQuantity(clonedBiospecimen.getQuantity());
			biospecimen.setUnit(clonedBiospecimen.getUnit());
			biospecimen.setTreatmentType(clonedBiospecimen.getTreatmentType());

			Study studyFromClone = clonedBiospecimen.getStudy();
			// There should be a study and only then do the rest of the code here
			if (studyFromClone != null) {
				Study study = iArkCommonService.getStudy(studyFromClone.getId());
				biospecimen.setStudy(study);
				// Reset the biospecimen detail
				cpModel.getObject().setBiospecimen(biospecimen);

				// Set the bioTransaction detail
				org.apache.shiro.subject.Subject currentUser = SecurityUtils.getSubject();
				cpModel.getObject().getBioTransaction().setRecorder(currentUser.getPrincipal().toString());
				cpModel.getObject().getBioTransaction().setQuantity(null);

				enableQuantityTreatment(target);

				// refresh the bioTransaction panel
				initialiseBioTransactionListPanel();
				arkCrudContainerVo.getDetailPanelFormContainer().addOrReplace(bioTransactionListPanel);

				// refresh the location panel
				cpModel.getObject().setBiospecimenLocationVO(new BiospecimenLocationVO());
				initialiseBiospecimenLocationPanel();
				arkCrudContainerVo.getDetailPanelFormContainer().addOrReplace(biospecimenLocationPanel);
				target.add(biospecimenLocationPanel);

				// Notify in progress
				this.info("Cloning biospecimen " + clonedBiospecimenUid + ", please save to confirm");
				target.add(feedbackPanel);

				// hide button panel
				biospecimenbuttonsPanel.setVisible(false);
				target.add(biospecimenbuttonsPanel);
			}
			else {
				log.error("Cannot find a study for the cloned biospecimen.");
			}
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
	 * Handle processing or aliquoting of a parent biospecimen.
	 * Process essentially changing the type of a parent biospecimen.
	 * Aliquot essentially taking an amount from a parent biospecimen.
	 * 
	 * @param target
	 *           AjxaxRequestTarget
	 * @param processOrAliquot
	 *           indication to whether a process or an aliquot
	 * @param comment
	 *           comment to add to biospecimen comments
	 */
	protected void processOrAliquot(AjaxRequestTarget target, String processOrAliquot, String comment) {
		final Biospecimen parentBiospecimen = cpModel.getObject().getBiospecimen();
		final String parentBiospecimenUid = parentBiospecimen.getBiospecimenUid();
		final Biospecimen biospecimen = new Biospecimen();

		try {
			// Copy parent biospecimen details to new biospecimen
			PropertyUtils.copyProperties(biospecimen, parentBiospecimen);

			// Amend specific fields/detail
			biospecimen.setId(null);

			if (biospecimen.getStudy().getAutoGenerateBiospecimenUid()) {
				biospecimen.setBiospecimenUid(Constants.AUTO_GENERATED);
			}

			biospecimen.setParent(parentBiospecimen);
			biospecimen.setParentUid(parentBiospecimen.getBiospecimenUid());
			biospecimen.setSampleType(parentBiospecimen.getSampleType());
			biospecimen.setBioCollection(parentBiospecimen.getBioCollection());
			biospecimen.setQuantity(null);
			biospecimen.setUnit(parentBiospecimen.getUnit());
			biospecimen.setComments(comment + parentBiospecimenUid);
			biospecimen.setBarcoded(false);
			biospecimen.setUnit(parentBiospecimen.getUnit());
			biospecimen.setTreatmentType(parentBiospecimen.getTreatmentType());

			Study studyFromParent = parentBiospecimen.getStudy();
			// There should be a study and only then do the rest of the code here
			if (studyFromParent != null) {
				Study study = iArkCommonService.getStudy(studyFromParent.getId());
				biospecimen.setStudy(study);
				// Reset the biospecimen detail
				cpModel.getObject().setBiospecimen(biospecimen);
				cpModel.getObject().setBiospecimenProcessing(processOrAliquot);

				// Set the bioTransaction detail
				org.apache.shiro.subject.Subject currentUser = SecurityUtils.getSubject();
				cpModel.getObject().getBioTransaction().setRecorder(currentUser.getPrincipal().toString());
				cpModel.getObject().getBioTransaction().setQuantity(null);

				enableQuantityTreatment(target);

				// refresh the bioTransaction panel
				initialiseBioTransactionListPanel();
				arkCrudContainerVo.getDetailPanelFormContainer().addOrReplace(bioTransactionListPanel);

				// refresh the location panel
				cpModel.getObject().setBiospecimenLocationVO(new BiospecimenLocationVO());
				initialiseBiospecimenLocationPanel();
				arkCrudContainerVo.getDetailPanelFormContainer().addOrReplace(biospecimenLocationPanel);
				target.add(biospecimenLocationPanel);

				// Notify in progress
				this.info(processOrAliquot + " biospecimen " + cpModel.getObject().getBiospecimen().getParentUid() + ", please save to confirm");
				target.add(feedbackPanel);

				// hide button panel
				biospecimenbuttonsPanel.setVisible(false);
				target.add(biospecimenbuttonsPanel);
			}
			else {
				log.error("Cannot find a study for the parent biospecimen.");
			}
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