package au.org.theark.worktracking.web.component.billableitemtype.form;

import java.util.List;

import org.apache.shiro.SecurityUtils;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.list.PageableListView;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import au.org.theark.core.model.worktracking.entity.BillableItemType;
import au.org.theark.core.model.worktracking.entity.BillableItemTypeStatus;
import au.org.theark.core.vo.ArkCrudContainerVO;
import au.org.theark.core.web.form.AbstractSearchForm;
import au.org.theark.worktracking.model.vo.BillableItemTypeVo;
import au.org.theark.worktracking.service.IWorkTrackingService;
import au.org.theark.worktracking.util.Constants;


public class SearchForm  extends AbstractSearchForm<BillableItemTypeVo> {
	private static final long				serialVersionUID	= 1L;
	
	@SpringBean(name = Constants.WORK_TRACKING_SERVICE)
	private IWorkTrackingService iWorkTrackingService;
	
	private ArkCrudContainerVO				arkCrudContainerVO;

	private TextField<String>				billableItemTypeIdTxtField;
	private TextField<String>				billableItemTypeItemNameTxtField;
	private TextField<String>				billableItemTypeQuantityPerUnitTxtField;
	private TextField<String>				billableItemTypeUnitPriceTxtField;
	
	private PageableListView<BillableItemType>	listView;
	
	private CompoundPropertyModel<BillableItemTypeVo>	cpmModel;
		
	private List<BillableItemTypeStatus> billableItemTypeStatusses;
	

	/**
	 * 
	 * @param id
	 * @param cpmModel
	 * @param arkCrudContainerVO
	 * @param feedBackPanel
	 * @param listView
	 */
	public SearchForm(String id, CompoundPropertyModel<BillableItemTypeVo> cpmModel, ArkCrudContainerVO arkCrudContainerVO, FeedbackPanel feedBackPanel, PageableListView<BillableItemType> listView) {

		super(id, cpmModel, feedBackPanel, arkCrudContainerVO);
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
		add(billableItemTypeIdTxtField);
		add(billableItemTypeItemNameTxtField);
		add(billableItemTypeQuantityPerUnitTxtField);
		add(billableItemTypeUnitPriceTxtField);
	}

	protected void initialiseSearchForm() {
		billableItemTypeStatusses= iWorkTrackingService.getBillableItemTypeStatuses();		
		billableItemTypeIdTxtField=new TextField<String>(Constants.BILLABLE_ITEM_TYPE_ID);             
		billableItemTypeItemNameTxtField=new TextField<String>(Constants.BILLABLE_ITEM_TYPE_ITEM_NAME);       
		billableItemTypeQuantityPerUnitTxtField=new TextField<String>(Constants.BILLABLE_ITEM_TYPE_QUANTITY_PER_UNIT);
		billableItemTypeUnitPriceTxtField=new TextField<String>(Constants.BILLABLE_ITEM_TYPE_UNIT_PRICE);                    		
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see au.org.theark.core.form.AbstractSearchForm#onNew(org.apache.wicket.ajax.AjaxRequestTarget)
	 */
	@Override
	protected void onNew(AjaxRequestTarget target) {
		setModelObject(new BillableItemTypeVo());
		getModelObject().setMode(Constants.MODE_NEW);
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
			getModelObject().getBillableItemType().setStudyId(studyId);
			
			for(BillableItemTypeStatus status:billableItemTypeStatusses){
				if(Constants.BILLABLE_ITEM_TYPE_ACTIVE.equalsIgnoreCase(status.getName())){
					getModelObject().getBillableItemType().setBillableItemTypeStatus(status);
					break;
				}
			}
			
			List<BillableItemType> resultList= iWorkTrackingService.searchBillableItemType(getModelObject().getBillableItemType());
			
			if(resultList != null && resultList.size() == 0){
				this.info("Billable Item Type with the specified criteria does not exist in the system.");
				target.add(feedbackPanel);
			}
			
			getModelObject().setBillableItemTypeList(resultList);
			listView.removeAll();
			arkCrudContainerVO.getSearchResultPanelContainer().setVisible(true);
			target.add(arkCrudContainerVO.getSearchResultPanelContainer());
	}
}