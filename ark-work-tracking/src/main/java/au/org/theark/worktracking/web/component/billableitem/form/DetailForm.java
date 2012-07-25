package au.org.theark.worktracking.web.component.billableitem.form;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;

import org.apache.shiro.SecurityUtils;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.extensions.markup.html.form.DateTextField;
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
import org.apache.wicket.validation.validator.StringValidator;

import au.org.theark.core.model.worktracking.entity.BillableItemStatus;
import au.org.theark.core.model.worktracking.entity.BillableItemType;
import au.org.theark.core.model.worktracking.entity.BillableItemTypeStatus;
import au.org.theark.core.model.worktracking.entity.BillableSubject;
import au.org.theark.core.model.worktracking.entity.WorkRequest;
import au.org.theark.core.vo.ArkCrudContainerVO;
import au.org.theark.core.web.component.ArkDatePicker;
import au.org.theark.core.web.form.AbstractDetailForm;
import au.org.theark.worktracking.model.vo.BillableItemVo;
import au.org.theark.worktracking.service.IWorkTrackingService;
import au.org.theark.worktracking.util.Constants;

public class DetailForm extends AbstractDetailForm<BillableItemVo> {
	
	public static final long	serialVersionUID	= -8267651986631341353L;
	
	@SpringBean(name = Constants.WORK_TRACKING_SERVICE)
	private IWorkTrackingService iWorkTrackingService;
	
	private TextField<String>								billableItemIdTxtField;
	private TextField<String>								billableItemDescriptionTxtField;
	private TextField<String>								billableItemQuantityTxtField;
	private DateTextField									billableItemCommenceDateDp;
	
	private DropDownChoice<WorkRequest>		 				billableItemWorkRequests;
	private DropDownChoice<BillableItemType>		 		billableItemItemTypes;
	private DropDownChoice<String>							billableItemInvoiceStatuses;
	private DropDownChoice<BillableItemStatus>		 		billableItemStatuses;
	
	private FileUploadField									subjectsUploadField;
	
	private List<WorkRequest>								workRequestList;
	private List<BillableItemType>							billableItemTypeList;
	private List<BillableItemStatus>						billableItemStatusList;
	
	
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
		billableItemQuantityTxtField=new TextField<String>(Constants.BILLABLE_ITEM_QUANTITY);
		
		billableItemCommenceDateDp= new DateTextField(Constants.BILLABLE_ITEM_COMMENCE_DATE);
		ArkDatePicker datePicker = new ArkDatePicker();
		datePicker.bind(billableItemCommenceDateDp);
		billableItemCommenceDateDp.add(datePicker);
		
		subjectsUploadField= new FileUploadField(Constants.FILE_NAME);
		
		
		initWorkRequestDropDown();
		initBillableItemTypeDropDown();
		initInvoiceDropDown();
		
		initBillableItemStatusDropDown();
		
		addDetailFormComponents();
		attachValidators();
	}
	
	private void initInvoiceDropDown() {
		final LinkedHashMap<String, String> invoicePreferences = new LinkedHashMap();
		invoicePreferences.put(Constants.Y, Constants.YES);
		invoicePreferences.put(Constants.N, Constants.NO);

		IModel<Object> dropDownModel = new Model() {
		  public Serializable getObject() {
		    return new ArrayList(invoicePreferences.keySet());
		  }
		};

		billableItemInvoiceStatuses = new DropDownChoice(Constants.BILLABLE_ITEM_INVOICE, dropDownModel, new IChoiceRenderer<Object>() 
		{
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
		
		this.billableItemTypeList = iWorkTrackingService.searchBillableItemType(billableItemType);
		ChoiceRenderer defaultChoiceRenderer = new ChoiceRenderer(Constants.BIT_ITEM_NAME, Constants.ID);
		billableItemItemTypes = new DropDownChoice(Constants.BILLABLE_ITEM_BILLABLE_ITEM_TYPE,  this.billableItemTypeList, defaultChoiceRenderer);
	}

	private void initWorkRequestDropDown() {
		WorkRequest workRequest =new WorkRequest();
		Long studyId = (Long) SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.core.Constants.STUDY_CONTEXT_ID);
		workRequest.setStudyId(studyId);
		this.workRequestList = iWorkTrackingService.searchWorkRequest(workRequest);
		ChoiceRenderer defaultChoiceRenderer = new ChoiceRenderer(Constants.NAME, Constants.ID);
		billableItemWorkRequests = new DropDownChoice(Constants.BILLABLE_ITEM_WORK_REQUEST,  this.workRequestList, defaultChoiceRenderer);
	}
	
	private void initBillableItemStatusDropDown(){
		this.billableItemStatusList = iWorkTrackingService.getBillableItemStatusses();
		ChoiceRenderer defaultChoiceRenderer = new ChoiceRenderer(Constants.NAME, Constants.ID);
		billableItemStatuses = new DropDownChoice(Constants.BILLABLE_ITEM_ITEM_STATUS,  this.billableItemStatusList, defaultChoiceRenderer);
	}
	
	public void addDetailFormComponents() {		
		arkCrudContainerVO.getDetailPanelFormContainer().add(billableItemIdTxtField);
		arkCrudContainerVO.getDetailPanelFormContainer().add(billableItemDescriptionTxtField);
		arkCrudContainerVO.getDetailPanelFormContainer().add(billableItemQuantityTxtField);
		arkCrudContainerVO.getDetailPanelFormContainer().add(billableItemCommenceDateDp);
		arkCrudContainerVO.getDetailPanelFormContainer().add(billableItemWorkRequests);
		arkCrudContainerVO.getDetailPanelFormContainer().add(billableItemItemTypes);
		arkCrudContainerVO.getDetailPanelFormContainer().add(billableItemInvoiceStatuses);
		arkCrudContainerVO.getDetailPanelFormContainer().add(billableItemStatuses);
		arkCrudContainerVO.getDetailPanelFormContainer().add(subjectsUploadField);
		
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
		billableItemQuantityTxtField.setRequired(true).setLabel(new StringResourceModel(Constants.ERROR_BILLABLE_ITEM_QUANTITY_REQUIRED, billableItemQuantityTxtField, new Model<String>(Constants.BILLABLE_ITEM_QUANTITY_TAG)));
		billableItemWorkRequests.setRequired(true).setLabel(new StringResourceModel(Constants.ERROR_BILLABLE_ITEM_WORK_REQUEST_REQUIRED, billableItemWorkRequests, new Model<String>(Constants.BILLABLE_ITEM_WORK_REQUEST_TAG)));
		billableItemItemTypes.setRequired(true).setLabel(new StringResourceModel(Constants.ERROR_BILLABLE_ITEM_BILLABLE_ITEM_TYPE_REQUIRED, billableItemItemTypes, new Model<String>(Constants.BILLABLE_ITEM_BILLABLE_ITEM_TYPE_TAG)));
		billableItemInvoiceStatuses.setRequired(true).setLabel(new StringResourceModel(Constants.ERROR_BILLABLE_ITEM_INVOICE_REQUIRED, billableItemInvoiceStatuses, new Model<String>(Constants.BILLABLE_ITEM_INVOICE_TAG)));
		billableItemStatuses.setRequired(true).setLabel(new StringResourceModel(Constants.ERROR_BILLABLE_ITEM_ITEM_STATUS_REQUIRED, billableItemStatuses, new Model<String>(Constants.BILLABLE_ITEM_ITEM_STATUS_TAG)));
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
		int successSubjects=0;
		int ignoredSubjects=0;
		try {
			HashSet<BillableSubject> subjectSet=null;
			if(subjectsUploadField != null && subjectsUploadField.getFileUpload() != null){
				FileUpload fileUpload = subjectsUploadField.getFileUpload();
				BufferedReader br=new BufferedReader( new InputStreamReader(fileUpload.getInputStream())); 		
				String line;
				subjectSet = new HashSet<BillableSubject>();
				
		    	while ((line = br.readLine()) != null) {
		    		String array[]=line.split(",");
		    		if(array.length>1){
		    			BillableSubject billableSubject=new BillableSubject();
		    			billableSubject.setSubjectId(Long.valueOf(array[0]));
		    			billableSubject.setDescription(array[1]);
		    			subjectSet.add(billableSubject);
		    			++successSubjects;
		    		}else{
		    			++ignoredSubjects;
		    			continue;
		    		}
		    	} 			
			}
			
			containerForm.getModelObject().getBillableItem().setBillableSubjects(subjectSet);
			
			if (containerForm.getModelObject().getBillableItem().getId() == null) {
				Long studyId = (Long) SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.core.Constants.STUDY_CONTEXT_ID);
				containerForm.getModelObject().getBillableItem().setStudyId(studyId);
				
				containerForm.getModelObject().getBillableItem().setType(Constants.BILLABLE_ITEM_MANUAL);
				
				iWorkTrackingService.createBillableItem(containerForm.getModelObject().getBillableItem());
				this.info("Billable Item " + containerForm.getModelObject().getBillableItem().getDescription()  + " was created successfully with "+successSubjects+" subjects");
				processErrors(target);
			}
			else {
				iWorkTrackingService.updateBillableItem(containerForm.getModelObject().getBillableItem());
				this.info("Billable Item " + containerForm.getModelObject().getBillableItem().getDescription() + " was updated successfully with "+successSubjects+" subjects");
				processErrors(target);
			}

			onSavePostProcess(target);

		}
		catch(IOException ioException){
			this.error("Error reading the input subject list file");
			processErrors(target);
		}
		catch (Exception e) {
			this.error("A System error occured, we will have someone contact you.");
			processErrors(target);
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
			Long count = iWorkTrackingService.getBillableSubjectCount(containerForm.getModelObject().getBillableItem());
		if(count == 0){	
			iWorkTrackingService.deleteBillableItem(containerForm.getModelObject().getBillableItem());
			containerForm.info("The Billable Item was deleted successfully.");
			editCancelProcess(target);
		}else{
			containerForm.error("Cannot Delete this Billable Item Component. This Billable Item is associated with existing Billable Subjects ");
			processErrors(target);
		}	
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

}