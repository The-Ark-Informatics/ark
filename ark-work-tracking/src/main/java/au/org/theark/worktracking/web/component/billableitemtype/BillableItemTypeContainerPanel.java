package au.org.theark.worktracking.web.component.billableitemtype;

import java.util.List;

import org.apache.shiro.SecurityUtils;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.list.PageableListView;
import org.apache.wicket.markup.html.navigation.paging.PagingNavigator;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import au.org.theark.core.model.worktracking.entity.BillableItemType;
import au.org.theark.core.model.worktracking.entity.BillableItemTypeStatus;
import au.org.theark.core.web.component.AbstractContainerPanel;
import au.org.theark.worktracking.model.vo.BillableItemTypeVo;
import au.org.theark.worktracking.service.IWorkTrackingService;
import au.org.theark.worktracking.util.Constants;
import au.org.theark.worktracking.web.component.billableitemtype.form.ContainerForm;

public class BillableItemTypeContainerPanel extends AbstractContainerPanel {
	private static final long				serialVersionUID	= 1L;

	// Panels
	private SearchPanel						searchComponentPanel;
	private SearchResultListPanel			searchResultPanel;
	private DetailPanel						detailsPanel;

	private PageableListView<BillableItemType>	pageableListView;

	private ContainerForm					containerForm;

	@SpringBean(name = Constants.WORK_TRACKING_SERVICE)
	private IWorkTrackingService iWorkTrackingService;
	

	public BillableItemTypeContainerPanel(String id) {
		super(id);

		/* Initialise the CPM */
		cpModel = new CompoundPropertyModel<BillableItemTypeVo>(new BillableItemTypeVo());

		/* Bind the CPM to the Form */
		containerForm = new ContainerForm("containerForm", cpModel);

		containerForm.add(initialiseFeedBackPanel());

		containerForm.add(initialiseDetailPanel());

		containerForm.add(initialiseSearchResults());

		containerForm.add(initialiseSearchPanel());

		add(containerForm);

	}

	protected WebMarkupContainer initialiseSearchResults() {

		searchResultPanel = new SearchResultListPanel("searchResults", arkCrudContainerVO, containerForm);
		final List<BillableItemTypeStatus> billableItemTypeStatusses=iWorkTrackingService.getBillableItemTypeStatuses();
		iModel = new LoadableDetachableModel<Object>() {
			private static final long	serialVersionUID	= 1L;
			@Override
			protected Object load() {
				Long studyId = (Long) SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.core.Constants.STUDY_CONTEXT_ID);
				containerForm.getModelObject().getBillableItemType().setStudyId(studyId);
				for(BillableItemTypeStatus status:billableItemTypeStatusses){
					if(Constants.BILLABLE_ITEM_TYPE_ACTIVE.equalsIgnoreCase(status.getName())){
						containerForm.getModelObject().getBillableItemType().setBillableItemTypeStatus(status);
						break;
					}
				}
				containerForm.getModelObject().setBillableItemTypeList(iWorkTrackingService.searchBillableItemType(containerForm.getModelObject().getBillableItemType()));					
				pageableListView.removeAll();
				return containerForm.getModelObject().getBillableItemTypeList();
			}
		};

		pageableListView = searchResultPanel.buildPageableListView(iModel);
		pageableListView.setReuseItems(true);
		PagingNavigator pageNavigator = new PagingNavigator("navigator", pageableListView);
		searchResultPanel.add(pageNavigator);
		searchResultPanel.add(pageableListView);
		arkCrudContainerVO.getSearchResultPanelContainer().add(searchResultPanel);
		return arkCrudContainerVO.getSearchResultPanelContainer();
	}

	protected WebMarkupContainer initialiseDetailPanel() {
		detailsPanel = new DetailPanel("detailPanel", feedBackPanel, arkCrudContainerVO, containerForm);
		detailsPanel.initialisePanel();
		arkCrudContainerVO.getDetailPanelContainer().add(detailsPanel);
		return arkCrudContainerVO.getDetailPanelContainer();
	}

	protected WebMarkupContainer initialiseSearchPanel() {
		searchComponentPanel = new SearchPanel("searchComponentPanel", arkCrudContainerVO, feedBackPanel, containerForm, pageableListView);
		searchComponentPanel.initialisePanel(cpModel);
		arkCrudContainerVO.getSearchPanelContainer().add(searchComponentPanel);
		return arkCrudContainerVO.getSearchPanelContainer();

	}
	
}
