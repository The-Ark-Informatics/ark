package au.org.theark.study.web.component.site;

import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.list.PageableListView;
import org.apache.wicket.markup.html.navigation.paging.PagingNavigator;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import au.org.theark.core.vo.SiteModelVO;
import au.org.theark.core.vo.SiteVO;
import au.org.theark.study.service.IStudyService;
import au.org.theark.study.web.Constants;
import au.org.theark.study.web.component.site.form.ContainerForm;

public class SiteContainerPanel extends Panel{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private transient Logger log = LoggerFactory.getLogger(SiteContainerPanel.class);
	private FeedbackPanel feedBackPanel;
	
	/*The object that will house the data for the site use case */
	private CompoundPropertyModel<SiteModelVO> siteModelCpm;
	
	private WebMarkupContainer searchPanelContainer;
	private WebMarkupContainer saveArchivebuttonContainer;
	private WebMarkupContainer editbuttonContainer;
	
	//The type of object returned by the List
	private IModel<Object> iModel;
	private PageableListView<SiteVO> listView;
	private SearchResultList searchResultPanel;
	private WebMarkupContainer resultListContainer;
	
	private WebMarkupContainer detailPanelContainer;
	private WebMarkupContainer detailPanelFormContainer;
	private Details detailPanel;
	private ContainerForm siteContainerForm;	
	//Search Site Panel
	Search searchSitesPanel;
	
	
	@SpringBean( name = Constants.STUDY_SERVICE)
	private IStudyService studyService;
	
	private void initialiseMarkupContainers(){
		
		searchPanelContainer = new WebMarkupContainer("searchContainer");
		searchPanelContainer.setOutputMarkupPlaceholderTag(true);
		
		detailPanelContainer = new WebMarkupContainer("detailsContainer");
		detailPanelContainer.setOutputMarkupPlaceholderTag(true);
		detailPanelContainer.setVisible(false);

		//Contains the controls of the details
		detailPanelFormContainer = new WebMarkupContainer("detailFormContainer");
		detailPanelFormContainer.setOutputMarkupPlaceholderTag(true);
		detailPanelFormContainer.setEnabled(false);
		
		//The wrapper for ResultsList panel that will contain a ListView
		resultListContainer = new WebMarkupContainer("resultListContainer");
		resultListContainer.setOutputMarkupPlaceholderTag(true);
		resultListContainer.setVisible(true);
	
		saveArchivebuttonContainer = new WebMarkupContainer("saveArchContainer");
		saveArchivebuttonContainer.setOutputMarkupPlaceholderTag(true);
		saveArchivebuttonContainer.setVisible(false);
		
		editbuttonContainer = new WebMarkupContainer("editButtonContainer");
		editbuttonContainer.setOutputMarkupPlaceholderTag(true);
		editbuttonContainer.setVisible(false);
		
	}
	
	private WebMarkupContainer initialiseDetailPanel(){
		
		detailPanel = new Details("detailsPanel", resultListContainer, feedBackPanel, detailPanelContainer,searchPanelContainer,siteContainerForm);
		detailPanel.initialisePanel();
		detailPanelContainer.add(detailPanel);
		return detailPanelContainer;
		
	}
	
	private WebMarkupContainer initialiseSearchPanel(){
		SiteVO siteVo = new SiteVO();
		List<SiteVO> resultList = new ArrayList<SiteVO>(); 
			//studyService.getSite(siteVo);
		//List<SiteVO> resultList = studyService.getSite(containerForm.getModelObject().getSiteVo());
		siteContainerForm.getModelObject().setSiteVoList(resultList);
		searchSitesPanel = new Search("searchSitePanel",feedBackPanel,searchPanelContainer,listView, resultListContainer,detailPanelContainer,siteContainerForm);
		searchSitesPanel.initialisePanel(siteModelCpm);
		searchPanelContainer.add(searchSitesPanel);
		return searchPanelContainer;
	}
	
	private WebMarkupContainer initialiseFeedBackPanel(){
		/* Feedback Panel */
		feedBackPanel= new FeedbackPanel("feedbackMessage");
		feedBackPanel.setOutputMarkupId(true);
		return feedBackPanel;
	}
	

	private WebMarkupContainer initialiseSearchResults(){
	
		searchResultPanel = new SearchResultList("searchResults",detailPanelContainer,searchPanelContainer,resultListContainer,siteContainerForm);
		
		iModel = new LoadableDetachableModel<Object>() {
			private static final long serialVersionUID = 1L;

			@Override
			protected Object load() {
				SiteVO siteVo = new SiteVO();
				List<SiteVO> resultList = new ArrayList<SiteVO>(); 
				siteContainerForm.getModelObject().setSiteVoList(resultList);
				return siteContainerForm.getModelObject().getSiteVoList();
			}
		};
		
		//Build a Pageable List
		listView  = searchResultPanel.buildPageableListView(iModel);
		listView.setReuseItems(true);
		PagingNavigator pageNavigator = new PagingNavigator("navigator", listView);
		searchResultPanel.add(pageNavigator);
		searchResultPanel.add(listView);
		resultListContainer.add(searchResultPanel);
		return resultListContainer;
	}
	
	public SiteContainerPanel(String id) {
		super(id);
		
		/*Initialise the CPM */
		siteModelCpm = new CompoundPropertyModel<SiteModelVO>(new SiteModelVO());
		
	
		initialiseMarkupContainers();
		
		/*Bind the CPM to the Form */
		siteContainerForm = new ContainerForm("containerForm", siteModelCpm);
		log.info("SiteContainerPanel initialised. Creating the Search Panel for Sites..");
		siteContainerForm.add(initialiseFeedBackPanel());
	
		siteContainerForm.add(initialiseDetailPanel());
	
		siteContainerForm.add(initialiseSearchResults());
		
		siteContainerForm.add(initialiseSearchPanel());
		
		
		add(siteContainerForm);
		
	}
	

}
