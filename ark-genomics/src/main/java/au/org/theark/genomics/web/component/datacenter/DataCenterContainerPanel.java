package au.org.theark.genomics.web.component.datacenter;

import org.apache.wicket.ajax.markup.html.navigation.paging.AjaxPagingNavigator;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.list.PageableListView;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import au.org.theark.core.web.component.AbstractContainerPanel;
import au.org.theark.genomics.model.vo.DataCenterVo;
import au.org.theark.genomics.model.vo.DataSourceVo;
import au.org.theark.genomics.service.IGenomicService;
import au.org.theark.genomics.util.Constants;
import au.org.theark.genomics.web.component.datacenter.form.ContainerForm;

public class DataCenterContainerPanel extends AbstractContainerPanel<DataCenterVo>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private SearchPanel						searchPanel;
	private SearchResultListPanel			searchResultPanel;
	
	private PageableListView<DataSourceVo>	pageableListView;

	private ContainerForm					containerForm;

	@SpringBean(name = Constants.GENOMIC_SERVICE)
	private IGenomicService iGenomicService;
	
	public DataCenterContainerPanel(String id) {
		super(id);
		
		cpModel = new CompoundPropertyModel<DataCenterVo>(new DataCenterVo());

		/* Bind the CPM to the Form */
		containerForm = new ContainerForm("containerForm", cpModel);

		containerForm.add(initialiseFeedBackPanel());

		containerForm.add(initialiseSearchResults());

		containerForm.add(initialiseSearchPanel());

		add(containerForm);
	}

	@Override
	protected WebMarkupContainer initialiseSearchResults() {
		searchResultPanel = new SearchResultListPanel("searchResults", arkCrudContainerVO, containerForm);
		searchResultPanel.setOutputMarkupId(true);
		
		iModel = new LoadableDetachableModel<Object>() {
			private static final long	serialVersionUID	= 1L;

			@Override
			protected Object load() {
//				containerForm.getModelObject().setMicroServiceList(iGenomicService.searchMicroService((containerForm.getModelObject().getMicroService())));
				pageableListView.removeAll();
				return containerForm.getModelObject().getDataSourceList();
			}
		};

		pageableListView = searchResultPanel.buildPageableListView(iModel);
		pageableListView.setReuseItems(true);
		AjaxPagingNavigator pageNavigator = new AjaxPagingNavigator("navigator", pageableListView);
		searchResultPanel.add(pageNavigator);
		searchResultPanel.add(pageableListView);
		arkCrudContainerVO.getSearchResultPanelContainer().add(searchResultPanel);
		return arkCrudContainerVO.getSearchResultPanelContainer();
	}

	@Override
	protected WebMarkupContainer initialiseDetailPanel() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected WebMarkupContainer initialiseSearchPanel() {
		searchPanel = new SearchPanel("searchComponentPanel", arkCrudContainerVO, feedBackPanel, containerForm, pageableListView);
		searchPanel.initialisePanel(cpModel);
		arkCrudContainerVO.getSearchPanelContainer().add(searchPanel);
		return arkCrudContainerVO.getSearchPanelContainer();
	}

}
