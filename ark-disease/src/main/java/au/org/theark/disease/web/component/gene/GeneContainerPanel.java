package au.org.theark.disease.web.component.gene;

import au.org.theark.core.Constants;
import au.org.theark.core.model.study.entity.Study;
import au.org.theark.core.service.IArkCommonService;
import au.org.theark.core.web.component.AbstractContainerPanel;
import au.org.theark.core.web.component.ArkDataProvider;
import au.org.theark.disease.service.IArkDiseaseService;
import au.org.theark.disease.vo.GeneVO;
import au.org.theark.disease.web.component.gene.form.ContainerForm;
import org.apache.shiro.SecurityUtils;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.navigation.paging.AjaxPagingNavigator;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.list.PageableListView;
import org.apache.wicket.markup.repeater.data.DataView;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class GeneContainerPanel extends AbstractContainerPanel<GeneVO> {
	
	private static final long	serialVersionUID	= 1L;
	private static final Logger log = LoggerFactory.getLogger(GeneContainerPanel.class);
	
	private Long sessionStudyId;
	private WebMarkupContainer arkContextMarkup;
	private Study study = new Study();
	
	private ContainerForm containerForm;
	
	private SearchResultListPanel searchResultsPanel;
	private DetailPanel detailPanel;
	private SearchPanel searchPanel;
	private PageableListView<GeneVO> pageableListView;
	private DataView<GeneVO> dataView;
	
	private ArkDataProvider<GeneVO, IArkDiseaseService> geneProvider;
	
	protected WebMarkupContainer resultsWmc = new WebMarkupContainer("resultsWmc");
	
	@SpringBean(name = Constants.ARK_COMMON_SERVICE)
	private IArkCommonService iArkCommonService;
	
	@SpringBean(name = Constants.ARK_DISEASE_SERVICE)
	private IArkDiseaseService iArkDiseaseService;
	
	public GeneContainerPanel(String id, WebMarkupContainer arkContextMarkup) {
		super(id);
		this.arkContextMarkup = arkContextMarkup;
		cpModel = new CompoundPropertyModel<GeneVO>(new GeneVO());
		
		sessionStudyId = (Long) SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.core.Constants.STUDY_CONTEXT_ID);
		if(sessionStudyId != null) {
			study = iArkCommonService.getStudy(sessionStudyId);
		}
		
		containerForm = new ContainerForm("containerForm", cpModel);
		containerForm.add(initialiseFeedBackPanel());
		containerForm.add(initialiseDetailPanel());
		containerForm.add(initialiseSearchResults());
		containerForm.add(initialiseSearchPanel());
		
		add(containerForm);
	}

	@Override
	protected WebMarkupContainer initialiseSearchResults() {
		searchResultsPanel = new SearchResultListPanel("searchResults", arkContextMarkup, containerForm, arkCrudContainerVO);
		searchResultsPanel.setOutputMarkupId(true);
		
		geneProvider = new ArkDataProvider<GeneVO, IArkDiseaseService>(iArkDiseaseService) {
			private static final long serialVersionUID = 1L;
			
			public long size() {
				return service.getGeneCount(containerForm.getModelObject());
			}

			public Iterator<? extends GeneVO> iterator(long first, long count) {
				List<GeneVO> listGenes = new ArrayList<GeneVO>();
				listGenes = service.searchPageableGenes(model.getObject(), first, count);
				return listGenes.iterator();
			}
			
		};
		geneProvider.setModel(this.cpModel);
		
		dataView = searchResultsPanel.buildDataView(geneProvider);
		dataView.setItemsPerPage(iArkCommonService.getRowsPerPage());
		
		AjaxPagingNavigator pageNavigator = new AjaxPagingNavigator("navigator", dataView) {
			private static final long serialVersionUID = 1L;
			
			@Override
			protected void onAjaxEvent(AjaxRequestTarget target) {
				target.add(searchResultsPanel);
			}
		};
		resultsWmc.add(pageNavigator);
		
		resultsWmc.add(dataView);
		searchResultsPanel.add(resultsWmc);		
		arkCrudContainerVO.getSearchResultPanelContainer().add(searchResultsPanel);
		return arkCrudContainerVO.getSearchResultPanelContainer();
	}

	@Override
	protected WebMarkupContainer initialiseDetailPanel() {
		detailPanel = new DetailPanel("detailPanel", feedBackPanel, arkContextMarkup, containerForm, arkCrudContainerVO);
		
		detailPanel.initialisePanel();
		arkCrudContainerVO.getDetailPanelContainer().add(detailPanel);
		return arkCrudContainerVO.getDetailPanelContainer();
	}

	@Override
	protected WebMarkupContainer initialiseSearchPanel() {
		searchPanel = new SearchPanel("searchComponentPanel", feedBackPanel, pageableListView, arkCrudContainerVO, containerForm);

		searchPanel.initialisePanel(cpModel);
		arkCrudContainerVO.getSearchPanelContainer().add(searchPanel);
		return arkCrudContainerVO.getSearchPanelContainer();
	}

}
