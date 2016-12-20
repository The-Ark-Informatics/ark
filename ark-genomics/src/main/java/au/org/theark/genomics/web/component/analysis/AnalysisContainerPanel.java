package au.org.theark.genomics.web.component.analysis;

import java.util.List;

import org.apache.shiro.SecurityUtils;
import org.apache.wicket.ajax.markup.html.navigation.paging.AjaxPagingNavigator;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.list.PageableListView;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import au.org.theark.core.model.spark.entity.Analysis;
import au.org.theark.core.web.component.AbstractContainerPanel;
import au.org.theark.genomics.model.vo.AnalysisVo;
import au.org.theark.genomics.service.IGenomicService;
import au.org.theark.genomics.util.Constants;
import au.org.theark.genomics.web.component.analysis.form.ContainerForm;

public class AnalysisContainerPanel extends AbstractContainerPanel {

	private static final long				serialVersionUID	= 1L;

	// Panels
	private SearchPanel						searchComponentPanel;
	private SearchResultListPanel			searchResultPanel;
	private DetailPanel						detailsPanel;

	private PageableListView<Analysis>	pageableListView;

	private ContainerForm					containerForm;

	@SpringBean(name = Constants.GENOMIC_SERVICE)
	private IGenomicService iGenomicService;
	

	public AnalysisContainerPanel(String id) {
		super(id);

		/* Initialise the CPM */
		cpModel = new CompoundPropertyModel<AnalysisVo>(new AnalysisVo());

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
		searchResultPanel.setOutputMarkupId(true);

		iModel = new LoadableDetachableModel<Object>() {
			private static final long	serialVersionUID	= 1L;

			@Override
			protected Object load() {
				Long studyId = (Long) SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.core.Constants.STUDY_CONTEXT_ID);				
				List<Analysis> list = iGenomicService.searchAnalysis(containerForm.getModelObject().getAnalysis(), studyId);
				containerForm.getModelObject().setAnalysisList(list);
				pageableListView.removeAll();
				return containerForm.getModelObject().getAnalysisList();
				
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
