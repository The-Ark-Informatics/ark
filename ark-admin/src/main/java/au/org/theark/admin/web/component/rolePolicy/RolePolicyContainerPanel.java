package au.org.theark.admin.web.component.rolePolicy;

import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.list.PageableListView;
import org.apache.wicket.markup.html.navigation.paging.PagingNavigator;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import au.org.theark.admin.model.vo.AdminVO;
import au.org.theark.admin.web.component.rolePolicy.form.ContainerForm;
import au.org.theark.core.model.study.entity.ArkRolePolicyTemplate;
import au.org.theark.core.service.IArkCommonService;
import au.org.theark.core.web.component.AbstractContainerPanel;

/**
 * @author cellis
 * 
 */
public class RolePolicyContainerPanel extends AbstractContainerPanel<AdminVO>
{
	/**
	 * 
	 */
	private static final long								serialVersionUID	= 442185554812824590L;
	private ContainerForm									containerForm;
	private SearchPanel										searchPanel;
	private DetailPanel										detailPanel;
	private SearchResultsPanel								searchResultsPanel;
	private PageableListView<ArkRolePolicyTemplate>	pageableListView;

	@SpringBean(name = au.org.theark.core.Constants.ARK_COMMON_SERVICE)
	private IArkCommonService<Void>						iArkCommonService;

	/**
	 * @param id
	 */
	public RolePolicyContainerPanel(String id)
	{
		super(id);
		/* Initialise the CPM */
		cpModel = new CompoundPropertyModel<AdminVO>(new AdminVO());
		initCrudContainerVO();
		initialiseMarkupContainers();

		/* Bind the CPM to the Form */
		containerForm = new ContainerForm("containerForm", cpModel);
		containerForm.add(initialiseFeedBackPanel());
		containerForm.add(initialiseDetailPanel());
		containerForm.add(initialiseSearchResults());
		containerForm.add(initialiseSearchPanel());

		add(containerForm);
	}

	@Override
	protected WebMarkupContainer initialiseDetailPanel()
	{
		detailPanel = new DetailPanel("detailPanel", feedBackPanel, containerForm, arkCrudContainerVO);
		detailPanel.initialisePanel();
		detailPanelContainer.add(detailPanel);
		return detailPanelContainer;
	}

	@Override
	protected WebMarkupContainer initialiseSearchPanel()
	{
		searchPanel = new SearchPanel("searchPanel", feedBackPanel, containerForm, cpModel, arkCrudContainerVO);
		searchPanel.initialisePanel();
		searchPanelContainer.add(searchPanel);
		return searchPanelContainer;
	}

	@Override
	protected WebMarkupContainer initialiseSearchResults()
	{
		searchResultsPanel = new SearchResultsPanel("searchResults", containerForm, arkCrudContainerVO);
		iModel = new LoadableDetachableModel<Object>()
		{
			private static final long	serialVersionUID	= 1L;

			@Override
			protected Object load()
			{
				List<ArkRolePolicyTemplate> arkRolePolicyTemplateList = new ArrayList<ArkRolePolicyTemplate>(0);
				arkRolePolicyTemplateList = iArkCommonService.getArkRolePolicyTemplateList();
				pageableListView.removeAll();
				return arkRolePolicyTemplateList;
			}
		};

		pageableListView = searchResultsPanel.buildPageableListView(iModel, searchResultPanelContainer);
		pageableListView.setReuseItems(true);
		PagingNavigator pageNavigator = new PagingNavigator("navigator", pageableListView);
		searchResultsPanel.add(pageNavigator);
		searchResultsPanel.add(pageableListView);
		searchResultPanelContainer.add(searchResultsPanel);
		return searchResultPanelContainer;
	}
}