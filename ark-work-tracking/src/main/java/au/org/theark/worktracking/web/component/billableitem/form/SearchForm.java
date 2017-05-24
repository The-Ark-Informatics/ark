package au.org.theark.worktracking.web.component.billableitem.form;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.datetime.PatternDateConverter;
import org.apache.wicket.datetime.markup.html.form.DateTextField;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.IChoiceRenderer;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.list.PageableListView;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import au.org.theark.core.model.worktracking.entity.BillableItem;
import au.org.theark.core.model.worktracking.entity.BillableItemType;
import au.org.theark.core.model.worktracking.entity.BillableItemTypeStatus;
import au.org.theark.core.model.worktracking.entity.Researcher;
import au.org.theark.core.model.worktracking.entity.WorkRequest;
import au.org.theark.core.vo.ArkCrudContainerVO;
import au.org.theark.core.web.component.ArkDatePicker;
import au.org.theark.core.web.component.button.AjaxInvoiceButton;
import au.org.theark.core.web.form.AbstractSearchForm;
import au.org.theark.worktracking.model.vo.BillableItemVo;
import au.org.theark.worktracking.service.IWorkTrackingService;
import au.org.theark.worktracking.util.Constants;



public class SearchForm  extends AbstractSearchForm<BillableItemVo> {
	private static final long				serialVersionUID	= 1L;
	
	@SpringBean(name = Constants.WORK_TRACKING_SERVICE)
	private IWorkTrackingService iWorkTrackingService;
	
	private ArkCrudContainerVO								arkCrudContainerVO;
	
	private TextField<String>								billableItemIdTxtField;
	private TextField<String>								billableItemDescriptionTxtField;
	
	private DateTextField									billableItemCommenceDateDp;
	
	private DropDownChoice<WorkRequest>		 				workRequests;
	private DropDownChoice<String>							invoiceStatuses;
	
	private PageableListView<BillableItem>					listView;
	
	private CompoundPropertyModel<BillableItemVo>			cpmModel;
		
	private List<WorkRequest>								workRequestList;
	
	private AjaxButton										invoiceButton;
	
	private DropDownChoice<Researcher>		 		 		workRequestResearchers;
	
	private DropDownChoice<BillableItemType>		 		billableItemItemTypes;

	/**
	 * 
	 * @param id
	 * @param cpmModel
	 * @param arkCrudContainerVO
	 * @param feedBackPanel
	 * @param listView
	 */
	public SearchForm(String id, CompoundPropertyModel<BillableItemVo> cpmModel, ArkCrudContainerVO arkCrudContainerVO, FeedbackPanel feedBackPanel, PageableListView<BillableItem> listView) {

		super(id, cpmModel, feedBackPanel, arkCrudContainerVO);
		this.setMultiPart(true);
		this.arkCrudContainerVO = arkCrudContainerVO;
		this.feedbackPanel = feedBackPanel;
		this.listView = listView;
		
		this.cpmModel=cpmModel;

		initialiseSearchForm();
		addSearchComponentsToForm();
		Long sessionStudyId = (Long) SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.core.Constants.STUDY_CONTEXT_ID);
		disableSearchForm(sessionStudyId, "There is no study selected. Please select a study.");
	}

	protected void addSearchComponentsToForm() {		
		add(billableItemIdTxtField);
		add(billableItemDescriptionTxtField);
		add(workRequests);
		add(invoiceStatuses);
		add(invoiceButton);
		add(billableItemCommenceDateDp);
		add(workRequestResearchers);
		add(billableItemItemTypes);
	}

	protected void initialiseSearchForm() {
		
		billableItemIdTxtField=new TextField<String>(Constants.BILLABLE_ITEM_ID);
		billableItemDescriptionTxtField=new TextField<String>(Constants.BILLABLE_ITEM_DESCRIPTION);
		
		CompoundPropertyModel<BillableItemVo> billableItemCmpModel = (CompoundPropertyModel<BillableItemVo>) cpmModel;
		
		PropertyModel<Researcher> rpm = new PropertyModel<Researcher>(billableItemCmpModel, "researcher");
		initResearcherDropDown(rpm);
		
		// Create a propertyModel to bind the components of this form, the root which is WorkRequest Container
		PropertyModel<BillableItem> pm = new PropertyModel<BillableItem>(billableItemCmpModel, "billableItem");	
		Long studyId = (Long) SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.core.Constants.STUDY_CONTEXT_ID);
		WorkRequest workRequest =new WorkRequest();
		workRequest.setStudyId(studyId);
		this.workRequestList=this.iWorkTrackingService.searchWorkRequest(workRequest );
		
		PropertyModel<WorkRequest> pmWorkRequest = new PropertyModel<WorkRequest>(pm, "workRequest");
		initWorkRequestDropDown(pmWorkRequest);
		initBillableItemTypeDropDown();
		initInvoiceDropDown();
		
		billableItemCommenceDateDp= new DateTextField(Constants.BILLABLE_ITEM_COMMENCE_DATE, new PatternDateConverter(au.org.theark.core.Constants.DD_MM_YYYY,false));
		initDataPicker(billableItemCommenceDateDp);
		
		
		invoiceButton=new AjaxInvoiceButton(Constants.INVOICE, new StringResourceModel("confirmInvoice", this, null), new StringResourceModel(Constants.INVOICE, this, null)) {	

			private static final long serialVersionUID = 1L;

			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
				List<BillableItem> itemList = getFormModelObject().getBillableItemList();
				List<BillableItem> updateBillableItemList=new ArrayList<BillableItem>(0);
				try{
					for(BillableItem item:itemList){
						if(Constants.BILLABLE_ITEM_MANUAL.equalsIgnoreCase(item.getType()) 
								&& !Constants.Y.equalsIgnoreCase(item.getType())){
							item.setInvoice(Constants.Y);
							updateBillableItemList.add(item);
						}
					}
					iWorkTrackingService.updateAllBillableItems(updateBillableItemList);
					Long studyId = (Long) SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.core.Constants.STUDY_CONTEXT_ID);
					getFormModelObject().getBillableItem().setStudyId(studyId);
					List<BillableItem> resultList = iWorkTrackingService.searchBillableItem(getFormModelObject());
					getFormModelObject().setBillableItemList(resultList);
					listView.removeAll();
					arkCrudContainerVO.getSearchResultPanelContainer().setVisible(true);
					target.add(arkCrudContainerVO.getSearchResultPanelContainer());
				}catch(Exception ex){
					this.error("A system error occurred in the invoice update.");
				}
			}
			@Override
			protected void onError(AjaxRequestTarget target, Form<?> form) {
				
			}
		};
		Subject currentUser = SecurityUtils.getSubject();
		if(currentUser!=null && currentUser.isPermitted(au.org.theark.core.Constants.PERMISSION_UPDATE)){
			invoiceButton.setVisible(true);
		}
		else{
			invoiceButton.setVisible(false);
		}
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

		invoiceStatuses = new DropDownChoice(Constants.BILLABLE_ITEM_INVOICE, dropDownModel, new IChoiceRenderer<Object>() 
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
	
	private void initResearcherDropDown(
			PropertyModel<Researcher> workRequestResearcher) {
		
		Researcher researcher=new Researcher();
		Long studyId = (Long) SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.core.Constants.STUDY_CONTEXT_ID);
		researcher.setStudyId(studyId);
		List<Researcher> researcherList=iWorkTrackingService.searchResearcher(researcher);
		
		IChoiceRenderer<Researcher> customChoiceRenderer = new IChoiceRenderer<Researcher>() {

			private static final long serialVersionUID = 1L;

			public Object getDisplayValue(Researcher researcher) {
				return researcher.getFirstName()+" "+researcher.getLastName();
			}

			public String getIdValue(Researcher researcher, int index) {
				return researcher.getId().toString();
			}
			
		};
		workRequestResearchers = new DropDownChoice(Constants.BILLABLE_ITEM_RESEARCHER, researcherList, customChoiceRenderer);
		workRequestResearchers.setModel(workRequestResearcher);
		workRequestResearchers.add(new AjaxFormComponentUpdatingBehavior("onChange") {

			private static final long	serialVersionUID	= 1L;

			@Override
			protected void onUpdate(AjaxRequestTarget target) {
				getFormModelObject().getBillableItem().setWorkRequest(null);
				target.add(workRequests);
	
				
			}
			
			@Override
			protected void onError(AjaxRequestTarget target, RuntimeException e) {
				// TODO Auto-generated method stub
				getFormModelObject().getBillableItem().setWorkRequest(null);
				target.add(workRequests);
			}
		});
				
	}

	private void initWorkRequestDropDown(
			PropertyModel<WorkRequest> pmWorkRequest) {
		ChoiceRenderer defaultChoiceRenderer = new ChoiceRenderer(Constants.NAME, Constants.ID);
		workRequests = new DropDownChoice(Constants.BILLABLE_ITEM_WORK_REQUEST ,  this.workRequestList, defaultChoiceRenderer);
		workRequests.add(new AjaxFormComponentUpdatingBehavior("onChange") {

			private static final long	serialVersionUID	= 1L;

			@Override
			protected void onUpdate(AjaxRequestTarget target) {
				getFormModelObject().setResearcher(null);
				target.add(workRequestResearchers);
			}
			
			@Override
			protected void onError(AjaxRequestTarget target, RuntimeException e) {
				// TODO Auto-generated method stub
				getFormModelObject().setResearcher(null);
				target.add(workRequestResearchers);
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
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see au.org.theark.core.form.AbstractSearchForm#onNew(org.apache.wicket.ajax.AjaxRequestTarget)
	 */
	@Override
	protected void onNew(AjaxRequestTarget target) {
		setModelObject(new BillableItemVo());
		getModelObject().setMode(Constants.MODE_NEW);
		getModelObject().getBillableItem().setCommenceDate(new Date());
		preProcessDetailPanel(target);
		arkCrudContainerVO.getEditButtonContainer().get(au.org.theark.core.Constants.SAVE).setEnabled(true);
		arkCrudContainerVO.getDetailPanelFormContainer().get("deleteButton").setVisible(false);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see au.org.theark.core.form.AbstractSearchForm#onSearch(org.apache.wicket.ajax.AjaxRequestTarget)
	 */
	@Override
	protected void onSearch(AjaxRequestTarget target) {

			target.add(feedbackPanel);
			Long studyId = (Long) SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.core.Constants.STUDY_CONTEXT_ID);
			getModelObject().getBillableItem().setStudyId(studyId);
			
//			List<BillableItem> resultList = iWorkTrackingService.searchBillableItem(getModelObject().getBillableItem());
			
			List<BillableItem> resultList = iWorkTrackingService.searchBillableItem(getModelObject());
			
			if(resultList != null && resultList.size() == 0){
				this.info("Billable Item with the specified search criteria does not exist in the system.");
				target.add(feedbackPanel);
			}
			
			getModelObject().setBillableItemList(resultList);
			listView.removeAll();
			arkCrudContainerVO.getSearchResultPanelContainer().setVisible(true);
			target.add(arkCrudContainerVO.getSearchResultPanelContainer());
	}
	
	private BillableItemVo getFormModelObject(){
		return getModelObject();
	}
}