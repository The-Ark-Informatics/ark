package au.org.theark.disease.web.component.disease;

import au.org.theark.core.Constants;
import au.org.theark.core.model.study.entity.Study;
import au.org.theark.core.service.IArkCommonService;
import au.org.theark.core.web.component.AbstractContainerPanel;
import au.org.theark.core.web.component.ArkDataProvider;
import au.org.theark.disease.service.IArkDiseaseService;
import au.org.theark.disease.vo.DiseaseVO;
import au.org.theark.disease.web.component.disease.form.ContainerForm;
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

public class DiseaseContainerPanel extends AbstractContainerPanel<DiseaseVO> {

	private static final long serialVersionUID = 1L;
	private static final Logger log = LoggerFactory.getLogger(DiseaseContainerPanel.class);
	
	private WebMarkupContainer	arkContextMarkup;
	private Long sessionStudyID;
	private Study study;
	
	private ContainerForm containerForm;

	private SearchResultListPanel searchResultsPanel;
	private DetailPanel detailPanel;
	private SearchPanel searchPanel;
	private PageableListView<DiseaseVO> pageableListView;
	private DataView<DiseaseVO> dataView;
	
	private ArkDataProvider<DiseaseVO, IArkDiseaseService> diseaseProvider;
	
	protected WebMarkupContainer resultsWmc = new WebMarkupContainer("resultsWmc");

	
	@SpringBean(name = Constants.ARK_COMMON_SERVICE)
	private IArkCommonService iArkCommonService;
	
	@SpringBean(name = Constants.ARK_DISEASE_SERVICE)
	private IArkDiseaseService iArkDiseaseService;
	
	public DiseaseContainerPanel(String id, WebMarkupContainer arkContextMarkup) {
		super(id);
		this.arkContextMarkup = arkContextMarkup;
		
		cpModel = new CompoundPropertyModel<DiseaseVO>(new DiseaseVO());
		
		sessionStudyID = (Long) SecurityUtils.getSubject().getSession().getAttribute(Constants.STUDY_CONTEXT_ID);
		if(sessionStudyID != null) {
			study = iArkCommonService.getStudy(sessionStudyID);
			cpModel.getObject().getDisease().setStudy(study);
		}
		
		containerForm = new ContainerForm("containerForm", cpModel);
		containerForm.add(initialiseFeedBackPanel());
		containerForm.add(initialiseDetailPanel());
		containerForm.add(initialiseSearchResults());
		containerForm.add(initialiseSearchPanel());
		
		add(containerForm);
		
	}
	
	protected void prerenderContextCheck() {
		
	}

	@Override
	protected WebMarkupContainer initialiseSearchPanel() {
		searchPanel = new SearchPanel("searchComponentPanel", feedBackPanel, pageableListView, arkCrudContainerVO, containerForm);

		searchPanel.initialisePanel(cpModel);
		arkCrudContainerVO.getSearchPanelContainer().add(searchPanel);
		return arkCrudContainerVO.getSearchPanelContainer();
	}
	
	@Override
	protected WebMarkupContainer initialiseSearchResults() {
		searchResultsPanel = new SearchResultListPanel("searchResults", arkContextMarkup, containerForm, arkCrudContainerVO);
		searchResultsPanel.setOutputMarkupId(true);
		
		diseaseProvider = new ArkDataProvider<DiseaseVO, IArkDiseaseService>(iArkDiseaseService) {
			private static final long serialVersionUID = 1L;
			
			public long size() {
				return service.getDiseaseCount(containerForm.getModelObject());
			}

			public Iterator<? extends DiseaseVO> iterator(long first, long count) {
				List<DiseaseVO> listDiseases = new ArrayList<DiseaseVO>();
				listDiseases = service.searchPageableDiseases(model.getObject(), first, count);
				return listDiseases.iterator();
			}
			
		};
		diseaseProvider.setModel(this.cpModel);
		
		dataView = searchResultsPanel.buildDataView(diseaseProvider);
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

	
	
}
