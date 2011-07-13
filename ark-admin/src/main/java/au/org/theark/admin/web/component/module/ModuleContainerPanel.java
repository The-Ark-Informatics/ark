package au.org.theark.admin.web.component.module;

import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.list.PageableListView;
import org.apache.wicket.markup.html.navigation.paging.PagingNavigator;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import au.org.theark.admin.model.vo.AdminVO;
import au.org.theark.admin.service.IAdminService;
import au.org.theark.admin.web.component.module.form.ContainerForm;
import au.org.theark.core.model.study.entity.ArkModule;
import au.org.theark.core.web.component.AbstractContainerPanel;

/**
 * @author cellis
 * 
 */
public class ModuleContainerPanel extends AbstractContainerPanel<AdminVO>
{
	/**
	 * 
	 */
	private static final long								serialVersionUID	= 442185554812824590L;
	private ContainerForm									containerForm;
	private SearchPanel										searchPanel;
	private DetailPanel										detailPanel;
	private SearchResultsPanel								searchResultsPanel;
	private PageableListView<ArkModule>					pageableListView;
	
	@SpringBean(name = au.org.theark.admin.service.Constants.ARK_ADMIN_SERVICE)
	private IAdminService<Void>		iAdminService;

	/**
	 * @param id
	 */
	public ModuleContainerPanel(String id)
	{
		super(id, true);
		/* Initialise the CPM */
		cpModel = new CompoundPropertyModel<AdminVO>(new AdminVO());
		
		initCrudContainerVO();
		
		/* Bind the CPM to the Form */
		containerForm = new ContainerForm("containerForm", cpModel);
		containerForm.add(initialiseFeedBackPanel());
		containerForm.add(initialiseDetailPanel());
		containerForm.add(initialiseSearchPanel());
		containerForm.add(initialiseSearchResults());

		add(containerForm);
	}

	@Override
	protected WebMarkupContainer initialiseDetailPanel()
	{
		detailPanel = new DetailPanel("detailPanel", feedBackPanel, containerForm, arkCrudContainerVO);
		detailPanel.initialisePanel();
		arkCrudContainerVO.getDetailPanelContainer().add(detailPanel);
		return arkCrudContainerVO.getDetailPanelContainer();
	}

	@Override
	protected WebMarkupContainer initialiseSearchPanel()
	{
		searchPanel = new SearchPanel("searchPanel", feedBackPanel, containerForm, cpModel, arkCrudContainerVO);
		searchPanel.initialisePanel();
		arkCrudContainerVO.getSearchPanelContainer().add(searchPanel);
		return arkCrudContainerVO.getSearchPanelContainer();
	}

	@Override
	protected WebMarkupContainer initialiseSearchResults()
	{
		searchResultsPanel = new SearchResultsPanel("searchResultsPanel", containerForm, arkCrudContainerVO);
		iModel = new LoadableDetachableModel<Object>()
		{
			private static final long	serialVersionUID	= 1L;

			@Override
			protected Object load()
			{
				List<ArkModule> arkModuleList = new ArrayList<ArkModule>(0);
				arkModuleList = iAdminService.getArkModuleList();
				containerForm.getModelObject().setArkModuleList(arkModuleList);
				pageableListView.removeAll();
				return arkModuleList;
			}
		};

		pageableListView = searchResultsPanel.buildPageableListView(iModel, searchResultPanelContainer);
		pageableListView.setReuseItems(true);
		PagingNavigator pageNavigator = new PagingNavigator("navigator", pageableListView);
		searchResultsPanel.add(pageNavigator);
		searchResultsPanel.add(pageableListView);
		arkCrudContainerVO.setMyListView(pageableListView);
		arkCrudContainerVO.getSearchResultPanelContainer().add(searchResultsPanel);
		return arkCrudContainerVO.getSearchResultPanelContainer();
	}
}