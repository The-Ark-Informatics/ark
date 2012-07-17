package au.org.theark.worktracking.web.component.billableitem.form;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import org.apache.shiro.SecurityUtils;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.IChoiceRenderer;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.list.PageableListView;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import au.org.theark.core.model.worktracking.entity.BillableItem;
import au.org.theark.core.model.worktracking.entity.WorkRequest;
import au.org.theark.core.vo.ArkCrudContainerVO;
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
	private TextField<String>								billableItemQuantityTxtField;
	
	private DropDownChoice<WorkRequest>		 				workRequests;
	private DropDownChoice<String>							invoiceStatuses;
	
	private PageableListView<BillableItem>					listView;
	
	private CompoundPropertyModel<BillableItemVo>			cpmModel;
		
	private List<WorkRequest>								workRequestList;
	

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
		disableSearchForm(sessionStudyId, "There is no study in context. Please select a Study.");
	}

	protected void addSearchComponentsToForm() {		
		add(billableItemIdTxtField);
		add(billableItemDescriptionTxtField);
		add(billableItemQuantityTxtField);
		add(workRequests);
		add(invoiceStatuses);
	}

	protected void initialiseSearchForm() {
		
		billableItemIdTxtField=new TextField<String>(Constants.BILLABLE_ITEM_ID);
		billableItemDescriptionTxtField=new TextField<String>(Constants.BILLABLE_ITEM_DESCRIPTION);
		billableItemQuantityTxtField=new TextField<String>(Constants.BILLABLE_ITEM_QUANTITY);
		
		CompoundPropertyModel<BillableItemVo> billableItemCmpModel = (CompoundPropertyModel<BillableItemVo>) cpmModel;
		
		// Create a propertyModel to bind the components of this form, the root which is WorkRequest Container
		PropertyModel<BillableItem> pm = new PropertyModel<BillableItem>(billableItemCmpModel, "billableItem");	
		Long studyId = (Long) SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.core.Constants.STUDY_CONTEXT_ID);
		WorkRequest workRequest =new WorkRequest();
		workRequest.setStudyId(studyId);
		this.workRequestList=this.iWorkTrackingService.searchWorkRequest(workRequest );
		
		PropertyModel<WorkRequest> pmWorkRequest = new PropertyModel<WorkRequest>(pm, "workRequest");
		initWorkRequestDropDown(pmWorkRequest);
		initInvoiceDropDown();
		
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

		invoiceStatuses = new DropDownChoice(Constants.BILLABLE_ITEM_INVOICE, dropDownModel, new IChoiceRenderer<Object>() 
		{
		  public String getDisplayValue(Object object) {
		    return invoicePreferences.get(object);
		  }
		  public String getIdValue(Object object, int index) {
		    return object.toString();
		  }
		});
	}

	private void initWorkRequestDropDown(
			PropertyModel<WorkRequest> pmWorkRequest) {
		ChoiceRenderer defaultChoiceRenderer = new ChoiceRenderer(Constants.NAME, Constants.ID);
		workRequests = new DropDownChoice(Constants.BILLABLE_ITEM_WORK_REQUEST ,  this.workRequestList, defaultChoiceRenderer);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see au.org.theark.core.form.AbstractSearchForm#onNew(org.apache.wicket.ajax.AjaxRequestTarget)
	 */
	@Override
	protected void onNew(AjaxRequestTarget target) {
		getModelObject().setMode(Constants.MODE_NEW);
		getModelObject().getBillableItem().setId(null);
		preProcessDetailPanel(target);
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
			
			List<BillableItem> resultList= iWorkTrackingService.searchBillableItem(getModelObject().getBillableItem());
			
			if(resultList != null && resultList.size() == 0){
				this.info("Billable Item with the specified criteria does not exist in the system.");
				target.add(feedbackPanel);
			}
			
			getModelObject().setBillableItemList(resultList);
			listView.removeAll();
			arkCrudContainerVO.getSearchResultPanelContainer().setVisible(true);
			target.add(arkCrudContainerVO.getSearchResultPanelContainer());
	}
}