package au.org.theark.disease.web.component.affection;

import au.org.theark.core.Constants;
import au.org.theark.core.exception.EntityNotFoundException;
import au.org.theark.core.model.study.entity.LinkSubjectStudy;
import au.org.theark.core.model.study.entity.Study;
import au.org.theark.core.service.IArkCommonService;
import au.org.theark.core.web.component.AbstractContainerPanel;
import au.org.theark.core.web.component.ArkDataProvider;
import au.org.theark.disease.service.IArkDiseaseService;
import au.org.theark.disease.vo.AffectionVO;
import au.org.theark.disease.web.component.affection.form.ContainerForm;
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

import java.util.Iterator;
import java.util.List;

public class AffectionContainerPanel extends AbstractContainerPanel<AffectionVO> {

	private static final long serialVersionUID = 1L;
	private static final Logger log = LoggerFactory.getLogger(AffectionContainerPanel.class);
	
	private WebMarkupContainer	arkContextMarkup;
	private Long sessionStudyID;
	private Study study;
	private LinkSubjectStudy linkSubjectStudy;
	
	private ContainerForm containerForm;

	private SearchResultListPanel searchResultsPanel;
	private DetailPanel detailPanel;
	private SearchPanel searchPanel;
	private PageableListView<AffectionVO> pageableListView;
	private DataView<AffectionVO> dataView;
	
	private ArkDataProvider<AffectionVO, IArkDiseaseService> affectionProvider;
	
	protected WebMarkupContainer resultsWmc = new WebMarkupContainer("resultsWmc");

	
	@SpringBean(name = Constants.ARK_COMMON_SERVICE)
	private IArkCommonService iArkCommonService;
	
	@SpringBean(name = Constants.ARK_DISEASE_SERVICE)
	private IArkDiseaseService iArkDiseaseService;
	
	public AffectionContainerPanel(String id, WebMarkupContainer arkContextMarkup) {
		super(id);
		this.arkContextMarkup = arkContextMarkup;
		
		cpModel = new CompoundPropertyModel<AffectionVO>(new AffectionVO());
		
		sessionStudyID = (Long) SecurityUtils.getSubject().getSession().getAttribute(Constants.STUDY_CONTEXT_ID);
		if(sessionStudyID != null) {
			study = iArkCommonService.getStudy(sessionStudyID);
		}
		
		Long subjectSessionID = (Long) SecurityUtils.getSubject().getSession().getAttribute(Constants.PERSON_CONTEXT_ID);
		try {
			linkSubjectStudy = iArkCommonService.getSubject(subjectSessionID, study);
		}
		catch (EntityNotFoundException e) {
			e.printStackTrace();
		}
				
		containerForm = new ContainerForm("containerForm", cpModel);
		containerForm.add(initialiseFeedBackPanel());
		containerForm.add(initialiseDetailPanel());
		containerForm.add(initialiseSearchResults());
		containerForm.add(initialiseSearchPanel());
		
		add(containerForm);
	}
	
	protected boolean prerenderContextCheck() {
		Long sessionPersonId = (Long) SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.core.Constants.PERSON_CONTEXT_ID);
		Long sessionStudyId = (Long) SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.core.Constants.STUDY_CONTEXT_ID);

		boolean contextLoaded = false;
		if ((sessionStudyId != null) && (sessionPersonId != null)) {
			String sessionPersonType = (String) SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.core.Constants.PERSON_TYPE);			
			
			if (sessionPersonType.equals(au.org.theark.core.Constants.PERSON_CONTEXT_TYPE_SUBJECT)) {
				LinkSubjectStudy lss;
				try {
					Study study = iArkCommonService.getStudy(sessionStudyId);
					lss = iArkCommonService.getSubject(sessionPersonId, study);
					contextLoaded = true;
				}
				catch (EntityNotFoundException e) {
					log.error(e.getMessage());
				}
				if(contextLoaded) {
					arkCrudContainerVO.getDetailPanelContainer().setVisible(true);
				} 
			}
		}
		return contextLoaded;
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
		
		affectionProvider = new ArkDataProvider<AffectionVO, IArkDiseaseService>(iArkDiseaseService) {
			private static final long serialVersionUID = 1L;
			
			public long size() {
				long count = service.getAffectionCount(containerForm.getModelObject());
				return count;
			}

			public Iterator<? extends AffectionVO> iterator(long first, long count) {
				List<AffectionVO> affectionVOs = service.searchPageableAffections(containerForm.getModelObject(), first, count);
				return affectionVOs.iterator();
			}
		};
		affectionProvider.setModel(this.cpModel);
		
		dataView = searchResultsPanel.buildDataView(affectionProvider);
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
