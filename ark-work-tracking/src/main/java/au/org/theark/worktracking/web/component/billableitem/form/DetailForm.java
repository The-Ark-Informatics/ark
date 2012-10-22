package au.org.theark.worktracking.web.component.billableitem.form;

import java.io.Serializable;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import org.apache.shiro.SecurityUtils;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.extensions.markup.html.form.DateTextField;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.IChoiceRenderer;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.form.upload.FileUpload;
import org.apache.wicket.markup.html.form.upload.FileUploadField;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.util.convert.IConverter;
import org.apache.wicket.util.convert.converter.DoubleConverter;
import org.apache.wicket.validation.IValidatable;
import org.apache.wicket.validation.validator.PatternValidator;
import org.apache.wicket.validation.validator.StringValidator;

import au.org.theark.core.model.worktracking.entity.BillableItem;
import au.org.theark.core.model.worktracking.entity.BillableItemType;
import au.org.theark.core.model.worktracking.entity.BillableItemTypeStatus;
import au.org.theark.core.model.worktracking.entity.WorkRequest;
import au.org.theark.core.vo.ArkCrudContainerVO;
import au.org.theark.core.web.component.ArkDatePicker;
import au.org.theark.core.web.form.AbstractDetailForm;
import au.org.theark.worktracking.model.vo.BillableItemVo;
import au.org.theark.worktracking.service.IWorkTrackingService;
import au.org.theark.worktracking.util.BillableItemCostCalculator;
import au.org.theark.worktracking.util.Constants;
import au.org.theark.worktracking.util.NumberValidatable;
import au.org.theark.worktracking.util.ValidatableItemType;

public class DetailForm extends AbstractDetailForm<BillableItemVo> {
	
	public static final long	serialVersionUID	= -8267651986631341353L;
	
	@SpringBean(name = Constants.WORK_TRACKING_SERVICE)
	private IWorkTrackingService iWorkTrackingService;
	
	private TextField<String>								billableItemIdTxtField;
	private TextField<String>								billableItemDescriptionTxtField;
	private TextField<String>								billableItemQuantityTxtField;
	private TextField<String>								billableItemItemCostTxtField;
	private DateTextField									billableItemCommenceDateDp;
		
	private DropDownChoice<WorkRequest>		 				billableItemWorkRequests;
	private DropDownChoice<BillableItemType>		 		billableItemItemTypes;
	private DropDownChoice<String>							billableItemInvoiceStatuses;
	
	private FileUploadField									subjectsUploadField;
	private AjaxButton										clearButton;
	private AjaxButton										deleteButton;
	private Label											fileNameLbl;
	
	private Label							    			researcherLbl;
	private Label											workRequestLbl;
	private Label											totalCostLbl;
	
	private Label											gstLbl;
	
	private FeedbackPanel		feedBackPanel;

	/**
	 * 
	 * @param id
	 * @param feedBackPanel
	 * @param arkCrudContainerVO
	 * @param containerForm
	 */
	public DetailForm(String id, FeedbackPanel feedBackPanel, ArkCrudContainerVO arkCrudContainerVO, ContainerForm containerForm) {

		super(id, feedBackPanel, containerForm, arkCrudContainerVO);
		this.feedBackPanel = feedBackPanel;
		this.setMultiPart(true);
	}

	public void onBeforeRender() {
		super.onBeforeRender();
	}

	public void initialiseDetailForm() {
		billableItemIdTxtField=new TextField<String>(Constants.BILLABLE_ITEM_ID);
		billableItemIdTxtField.setEnabled(false);
		billableItemDescriptionTxtField=new TextField<String>(Constants.BILLABLE_ITEM_DESCRIPTION);
		billableItemQuantityTxtField=new TextField<String>(Constants.BILLABLE_ITEM_QUANTITY){
			private static final long serialVersionUID = 1L;

			@Override
			public <C> IConverter<C> getConverter(Class<C> type) {
				  	DoubleConverter converter = (DoubleConverter)DoubleConverter.INSTANCE;
					NumberFormat format = converter.getNumberFormat(getLocale());
					format.setMinimumFractionDigits(2);
					converter.setNumberFormat(getLocale(), format);
					return (IConverter<C>) converter; 
			}
		};  
		
		billableItemQuantityTxtField.add(new AjaxFormComponentUpdatingBehavior("onkeyup"){
			private static final long serialVersionUID = 1L;

			@Override
			protected void onUpdate(AjaxRequestTarget target) {
				BillableItem billableItem = getFormModelObject().getBillableItem();
				double totalCost=BillableItemCostCalculator.calculateItemCost(billableItem);			
				getFormModelObject().getBillableItem().setTotalCost(totalCost);
				target.add(totalCostLbl);	
			}
			
			@Override
			protected void onError(AjaxRequestTarget target, RuntimeException e) {
				// TODO Auto-generated method stub
				getFormModelObject().getBillableItem().setTotalCost(0d);
				target.add(totalCostLbl);
			}
		});
		
		billableItemCommenceDateDp= new DateTextField(Constants.BILLABLE_ITEM_COMMENCE_DATE, au.org.theark.core.Constants.DD_MM_YYYY);
		initDataPicker(billableItemCommenceDateDp);		
		subjectsUploadField= new FileUploadField(Constants.FILE_NAME);
		
		researcherLbl = new Label(Constants.BILLABLE_ITEM_RESEARCHER_FULL_NAME);
		researcherLbl.setOutputMarkupId(true);
		researcherLbl.setVisible(true);

		workRequestLbl = new Label(Constants.BILLABLE_ITEM_WORK_REQUEST_DESCRIPTION);
		workRequestLbl.setOutputMarkupId(true);
		workRequestLbl.setVisible(true);
		
		totalCostLbl =new Label(Constants.BILLABLE_ITEM_TOTAL_COST);
		totalCostLbl.setOutputMarkupId(true);
		totalCostLbl.setVisible(true);
		
		gstLbl =new Label(Constants.BILLABLE_ITEM_GST);
		gstLbl.setOutputMarkupId(true);                       
		gstLbl.setVisible(true);  
		
		clearButton = new AjaxButton("clearButton") {			
			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
				subjectsUploadField.clearInput();
				target.add(subjectsUploadField);
			}
			
			@Override
			protected void onError(AjaxRequestTarget target, Form<?> form) {
				subjectsUploadField.clearInput();
				target.add(subjectsUploadField);
			}
		};
		clearButton.add(new AttributeModifier("title", new Model<String>("Clear Attachment")));
		
		deleteButton = new AjaxButton("deleteButton") {
			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
				BillableItem billableItem=getFormModelObject().getBillableItem();
				billableItem.setAttachmentPayload(null);
				billableItem.setAttachmentFilename(null);
				this.setVisible(false);
				target.add(fileNameLbl);
				target.add(this);
			}
			
			@Override
			protected void onError(AjaxRequestTarget target, Form<?> form) {
				BillableItem billableItem=getFormModelObject().getBillableItem();
				billableItem.setAttachmentPayload(null);
				billableItem.setAttachmentFilename(null);
				this.setVisible(false);
				target.add(fileNameLbl);
				target.add(this);
			}
		};
		deleteButton.add(new AttributeModifier("title", new Model<String>("Delete Attachment")));
		deleteButton.setVisible(false);
		
		billableItemItemCostTxtField =  new TextField<String>(Constants.BILLABLE_ITEM_ITEM_COST);
		billableItemItemCostTxtField.add(new AjaxFormComponentUpdatingBehavior("onkeyup"){
			private static final long serialVersionUID = 1L;

			@Override
			protected void onUpdate(AjaxRequestTarget target) {
				BillableItem billableItem = getFormModelObject().getBillableItem();
				double totalCost=BillableItemCostCalculator.calculateItemCost(billableItem);
				getFormModelObject().getBillableItem().setTotalCost(totalCost);
				target.add(totalCostLbl);
				
			}
			
			@Override
			protected void onError(AjaxRequestTarget target, RuntimeException e) {

				getFormModelObject().getBillableItem().setTotalCost(0d);
				target.add(totalCostLbl);
			}
		});
		
		fileNameLbl=new Label("billableItem.attachmentFilename");
		fileNameLbl.setOutputMarkupId(true);                       
		fileNameLbl.setVisible(true);     
		
		initWorkRequestDropDown();
		initBillableItemTypeDropDown();
		initInvoiceDropDown();
		addDetailFormComponents();
		attachValidators();
	}
	
	private void initDataPicker(DateTextField dateTextField){
		ArkDatePicker datePicker = new ArkDatePicker();
		datePicker.bind(dateTextField);
		dateTextField.add(datePicker);
	}
	
	private void initInvoiceDropDown() {
		final LinkedHashMap<String, String> invoicePreferences = new LinkedHashMap();
		invoicePreferences.put(Constants.Y, Constants.YES);
		invoicePreferences.put(Constants.N, Constants.NO);

		IModel<Object> dropDownModel = new Model() {
			private static final long serialVersionUID = 1L;

		public Serializable getObject() {
		    return new ArrayList(invoicePreferences.keySet());
		  }
		};

		billableItemInvoiceStatuses = new DropDownChoice(Constants.BILLABLE_ITEM_INVOICE, dropDownModel, new IChoiceRenderer<Object>() 
		{
			private static final long serialVersionUID = 1L;
		public String getDisplayValue(Object object) {
		    return invoicePreferences.get(object);
		  }
		  public String getIdValue(Object object, int index) {
		    return object.toString();
		  }
		});	
	}
	
	private void initBillableItemTypeDropDown() {
		
		BillableItemType billableItemType=new BillableItemType();
		Long studyId = (Long) SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.core.Constants.STUDY_CONTEXT_ID);
		billableItemType.setStudyId(studyId);
		List<BillableItemTypeStatus> billableItemTypeStatusses = iWorkTrackingService.getBillableItemTypeStatuses();
		
		for(BillableItemTypeStatus status:billableItemTypeStatusses){
			if(Constants.BILLABLE_ITEM_TYPE_ACTIVE.equalsIgnoreCase(status.getName())){
				billableItemType.setBillableItemTypeStatus(status);
				break;
			}
		}
		
		List<BillableItemType> billableItemTypeList = iWorkTrackingService.searchBillableItemType(billableItemType);
		ChoiceRenderer defaultChoiceRenderer = new ChoiceRenderer(Constants.BIT_ITEM_NAME, Constants.ID);
		billableItemItemTypes = new DropDownChoice(Constants.BILLABLE_ITEM_BILLABLE_ITEM_TYPE,  billableItemTypeList, defaultChoiceRenderer);
		billableItemItemTypes.add(new AjaxFormComponentUpdatingBehavior("onChange") {

			private static final long	serialVersionUID	= 1L;
			
			@Override
			protected void onUpdate(AjaxRequestTarget target) {
				BillableItem billableItem = getFormModelObject().getBillableItem();
				
				if(getFormModelObject().getMode()==Constants.MODE_NEW){	
				
					billableItem.setItemCost(billableItem.getBillableItemType().getUnitPrice());

					double totalCost=BillableItemCostCalculator.calculateItemCost(billableItem);
					getFormModelObject().getBillableItem().setTotalCost(totalCost);
					
					target.add(billableItemItemCostTxtField);
					target.add(totalCostLbl);
				
				}
				else{
					
					double totalCost=BillableItemCostCalculator.calculateItemCost(billableItem);
					getFormModelObject().getBillableItem().setTotalCost(totalCost);
					target.add(totalCostLbl);
				}
			}
			
			@Override
			protected void onError(AjaxRequestTarget target, RuntimeException e) {
				getFormModelObject().getBillableItem().setTotalCost(0d);
				target.add(totalCostLbl);
			}
		});

	}

	private void initWorkRequestDropDown() {
		WorkRequest workRequest = new WorkRequest();
		Long studyId = (Long) SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.core.Constants.STUDY_CONTEXT_ID);
		workRequest.setStudyId(studyId);
		List<WorkRequest> workRequestList = iWorkTrackingService.searchWorkRequest(workRequest);
		ChoiceRenderer defaultChoiceRenderer = new ChoiceRenderer(Constants.NAME, Constants.ID);
		billableItemWorkRequests = new DropDownChoice(Constants.BILLABLE_ITEM_WORK_REQUEST,  workRequestList, defaultChoiceRenderer);
				
		billableItemWorkRequests.add(new AjaxFormComponentUpdatingBehavior("onChange") {

			private static final long	serialVersionUID	= 1L;

			@Override
			protected void onUpdate(AjaxRequestTarget target) {
				target.add(researcherLbl);
				target.add(workRequestLbl);
				target.add(gstLbl);
				//Re-calculate the cost
				BillableItem billableItem = getFormModelObject().getBillableItem();
				double totalCost=BillableItemCostCalculator.calculateItemCost(billableItem);			
				getFormModelObject().getBillableItem().setTotalCost(totalCost);
				target.add(totalCostLbl);
			}
			
			@Override
			protected void onError(AjaxRequestTarget target, RuntimeException e) {
				// TODO Auto-generated method stub
				getFormModelObject().getBillableItem().setWorkRequest(null);
				target.add(researcherLbl);
				target.add(workRequestLbl);
				target.add(gstLbl);
				
				BillableItem billableItem = getFormModelObject().getBillableItem();
				double totalCost=BillableItemCostCalculator.calculateItemCost(billableItem);			
				getFormModelObject().getBillableItem().setTotalCost(totalCost);
				target.add(totalCostLbl);
			}
		});
	}
	
	public void addDetailFormComponents() {		
		arkCrudContainerVO.getDetailPanelFormContainer().add(billableItemIdTxtField);
		arkCrudContainerVO.getDetailPanelFormContainer().add(billableItemDescriptionTxtField);
		arkCrudContainerVO.getDetailPanelFormContainer().add(billableItemQuantityTxtField);
		arkCrudContainerVO.getDetailPanelFormContainer().add(billableItemCommenceDateDp);
		arkCrudContainerVO.getDetailPanelFormContainer().add(billableItemWorkRequests);
		arkCrudContainerVO.getDetailPanelFormContainer().add(billableItemItemTypes);
		arkCrudContainerVO.getDetailPanelFormContainer().add(billableItemInvoiceStatuses);
		arkCrudContainerVO.getDetailPanelFormContainer().add(subjectsUploadField);
		
		arkCrudContainerVO.getDetailPanelFormContainer().add(researcherLbl);
		arkCrudContainerVO.getDetailPanelFormContainer().add(workRequestLbl);
		arkCrudContainerVO.getDetailPanelFormContainer().add(totalCostLbl);
		
		arkCrudContainerVO.getDetailPanelFormContainer().add(billableItemItemCostTxtField);
		
		arkCrudContainerVO.getDetailPanelFormContainer().add(gstLbl);
		arkCrudContainerVO.getDetailPanelFormContainer().add(fileNameLbl);
		arkCrudContainerVO.getDetailPanelFormContainer().add(clearButton);
		arkCrudContainerVO.getDetailPanelFormContainer().add(deleteButton);
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see au.org.theark.core.web.form.AbstractDetailForm#attachValidators()
	 */
	@Override
	protected void attachValidators() {
		billableItemDescriptionTxtField.setRequired(true).setLabel(new StringResourceModel(Constants.ERROR_BILLABLE_ITEM_DESCRIPTION_REQUIRED, billableItemDescriptionTxtField, new Model<String>(Constants.BILLABLE_ITEM_DESCRIPTION_TAG)));
		billableItemDescriptionTxtField.add(StringValidator.lengthBetween(1, 255)).setLabel(
				new StringResourceModel(Constants.ERROR_BILLABLE_ITEM_DESCRIPTION_LENGTH, billableItemDescriptionTxtField, new Model<String>(Constants.BILLABLE_ITEM_DESCRIPTION_TAG)));
		billableItemWorkRequests.setRequired(true).setLabel(new StringResourceModel(Constants.ERROR_BILLABLE_ITEM_WORK_REQUEST_REQUIRED, billableItemWorkRequests, new Model<String>(Constants.BILLABLE_ITEM_WORK_REQUEST_TAG)));
		billableItemItemTypes.setRequired(true).setLabel(new StringResourceModel(Constants.ERROR_BILLABLE_ITEM_BILLABLE_ITEM_TYPE_REQUIRED, billableItemItemTypes, new Model<String>(Constants.BILLABLE_ITEM_BILLABLE_ITEM_TYPE_TAG)));
		billableItemInvoiceStatuses.setRequired(true).setLabel(new StringResourceModel(Constants.ERROR_BILLABLE_ITEM_INVOICE_REQUIRED, billableItemInvoiceStatuses, new Model<String>(Constants.BILLABLE_ITEM_INVOICE_TAG)));
		billableItemCommenceDateDp.setRequired(true).setLabel(new StringResourceModel(Constants.ERROR_BILLABLE_ITEM_COMMENCE_DATE_REQUIRED, billableItemCommenceDateDp, new Model<String>(Constants.BILLABLE_ITEM_COMMENCE_DATE_TAG)));
		
		billableItemQuantityTxtField.add(new PatternValidator(Constants.TWO_DECIMAL_PATTERN){
			private static final long serialVersionUID = 1L;

			@Override
			protected void onValidate(IValidatable<String> validatable) {
				super.onValidate(new NumberValidatable(validatable,ValidatableItemType.UNIT_QUANTITY));
			}
		});	
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see au.org.theark.core.web.form.AbstractDetailForm#onCancel(org.apache.wicket.ajax.AjaxRequestTarget)
	 */
	@Override
	protected void onCancel(AjaxRequestTarget target) {
		BillableItemVo billableItemVo = new BillableItemVo();
		containerForm.setModelObject(billableItemVo);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see au.org.theark.core.web.form.AbstractDetailForm#onSave(org.apache.wicket.markup.html.form.Form, org.apache.wicket.ajax.AjaxRequestTarget)
	 */
	@Override
	protected void onSave(Form<BillableItemVo> containerForm, AjaxRequestTarget target) {
		target.add(arkCrudContainerVO.getDetailPanelContainer());
		if(!isCommencedWorkRequest(target)){
			return;
		}
		try {
			
			if (subjectsUploadField != null && subjectsUploadField.getFileUpload() != null) {
				FileUpload fileUpload = subjectsUploadField.getFileUpload();
				containerForm.getModelObject().getBillableItem().setAttachmentPayload(fileUpload.getBytes());
				containerForm.getModelObject().getBillableItem().setAttachmentFilename(fileUpload.getClientFileName());
			}

			
			
			containerForm.getModelObject().getBillableItem().setTotalCost(BillableItemCostCalculator.calculateItemCost(containerForm.getModelObject().getBillableItem()));
			containerForm.getModelObject().getBillableItem().setTotalGST(BillableItemCostCalculator.calculateItemGST(containerForm.getModelObject().getBillableItem()));
			
			if (containerForm.getModelObject().getBillableItem().getId() == null) {
				Long studyId = (Long) SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.core.Constants.STUDY_CONTEXT_ID);
				containerForm.getModelObject().getBillableItem().setStudyId(studyId);
				
				containerForm.getModelObject().getBillableItem().setType(Constants.BILLABLE_ITEM_MANUAL);
				
				iWorkTrackingService.createBillableItem(containerForm.getModelObject().getBillableItem());
				this.info("Billable Item " + containerForm.getModelObject().getBillableItem().getDescription()  + " was created successfully");
				processErrors(target);
			}
			else {
				iWorkTrackingService.updateBillableItem(containerForm.getModelObject().getBillableItem());
				this.info("Billable Item " + containerForm.getModelObject().getBillableItem().getDescription() + " was updated successfully");
				processErrors(target);
			}

			this.billableItemOnSavePostProcess( target, containerForm.getModelObject());
		}
		catch (Exception e) {
			this.error("A System error occured, we will have someone contact you.");
			processErrors(target);
		}

	}
	
	private boolean isCommencedWorkRequest(final AjaxRequestTarget target){
		boolean result=true;
		WorkRequest workRequest= containerForm.getModelObject().getBillableItem().getWorkRequest();
		if(!"Commenced".equalsIgnoreCase(workRequest.getRequestStatus().getName())){
			this.error("Selected work request has to be commenced state");
			processErrors(target);
			result=false;
		}
		return result;
	}
	
	/**
	 * Disable the delete button for automated billable items.
	 * @param target
	 * @param billableItemVo
	 * 
	 * @see #onSavePostProcess(AjaxRequestTarget)
	 */
	private void billableItemOnSavePostProcess(AjaxRequestTarget target,BillableItemVo billableItemVo){
		super.onSavePostProcess(target);
		if(Constants.BILLABLE_ITEM_AUTOMATED.equalsIgnoreCase(billableItemVo.getBillableItem().getType())){
			AjaxButton ajaxButton = (AjaxButton) arkCrudContainerVO.getEditButtonContainer().get("delete");
			ajaxButton.setEnabled(false);
		}
		if(Constants.Y.equalsIgnoreCase(billableItemVo.getBillableItem().getInvoice())){
			AjaxButton ajaxButton = (AjaxButton) arkCrudContainerVO.getEditButtonContainer().get("save");
			ajaxButton.setEnabled(false);
		}
		
		if(billableItemVo.getBillableItem().getAttachmentFilename() !=null){
			arkCrudContainerVO.getDetailPanelFormContainer().get("deleteButton").setVisible(true);
		}
		else{
			arkCrudContainerVO.getDetailPanelFormContainer().get("deleteButton").setVisible(false);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see au.org.theark.core.web.form.AbstractDetailForm#processErrors(org.apache.wicket.ajax.AjaxRequestTarget)
	 */
	@Override
	protected void processErrors(AjaxRequestTarget target) {
		target.add(feedBackPanel);

	}

	protected void onDeleteConfirmed(AjaxRequestTarget target, String selection) {
		try {
				iWorkTrackingService.deleteBillableItem(containerForm.getModelObject().getBillableItem());
				containerForm.info("The Billable Item was deleted successfully.");
				editCancelProcess(target);
		}
		catch (Exception e) {
			containerForm.error("A System Error has occured please contact support.");
			processErrors(target);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see au.org.theark.core.web.form.AbstractDetailForm#isNew()
	 */
	@Override
	protected boolean isNew() {
		if (containerForm.getModelObject().getBillableItem().getId() == null) {
			return true;
		}
		else {
			return false;
		}

	}
	
	private BillableItemVo getFormModelObject(){
		return containerForm.getModelObject();
	}

}