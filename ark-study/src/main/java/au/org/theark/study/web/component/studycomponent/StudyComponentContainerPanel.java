package au.org.theark.study.web.component.studycomponent;

import java.util.ArrayList;
import java.util.List;

import org.apache.shiro.SecurityUtils;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.list.PageableListView;
import org.apache.wicket.markup.html.navigation.paging.PagingNavigator;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import au.org.theark.core.exception.ArkSystemException;
import au.org.theark.study.model.entity.StudyComp;
import au.org.theark.study.model.vo.StudyCompVo;
import au.org.theark.study.service.IStudyService;
import au.org.theark.study.web.Constants;
import au.org.theark.study.web.component.studycomponent.form.ContainerForm;


public class StudyComponentContainerPanel extends Panel{

	private static final long serialVersionUID = 1L;

	private FeedbackPanel feedBackPanel;
	
	//Panels
	private Search searchComponentPanel;
	private SearchResultList searchResultPanel;
	private Details detailsPanel;
	
	private CompoundPropertyModel<StudyCompVo> studyCompModel;

	private IModel<Object> iModel;
	private PageableListView<StudyComp> listView;

	//Mark-up Containers
	private WebMarkupContainer searchPanelContainer;
	private WebMarkupContainer resultListContainer;
	private WebMarkupContainer detailPanelContainer;
	private WebMarkupContainer detailPanelFormContainer;
	
	private ContainerForm containerForm;

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
	
	}
	
	public StudyComponentContainerPanel(String id) {
		super(id);
		
		/*Initialise the CPM */
		studyCompModel = new CompoundPropertyModel<StudyCompVo>(new StudyCompVo());
		
	
		initialiseMarkupContainers();
		
		/*Bind the CPM to the Form */
		containerForm = new ContainerForm("containerForm", studyCompModel);
		
		containerForm.add(initialiseFeedBackPanel());
	
		containerForm.add(initialiseDetailPanel());
	
		containerForm.add(initialiseSearchResults());
		
		containerForm.add(initialiseSearchPanel());
		
		add(containerForm);
		
	}
	
	private WebMarkupContainer initialiseFeedBackPanel(){
		/* Feedback Panel */
		feedBackPanel= new FeedbackPanel("feedbackMessage");
		feedBackPanel.setOutputMarkupId(true);
		return feedBackPanel;
	}
	
	
	private WebMarkupContainer initialiseSearchResults(){
		
		searchResultPanel = new SearchResultList("searchResults",detailPanelContainer,searchPanelContainer,containerForm,resultListContainer,detailsPanel);
		
		iModel = new LoadableDetachableModel<Object>() {
			private static final long serialVersionUID = 1L;

			@Override
			protected Object load() {
				return containerForm.getModelObject().getStudyCompList();
			}
		};

		listView  = searchResultPanel.buildPageableListView(iModel);
		listView.setReuseItems(true);
		PagingNavigator pageNavigator = new PagingNavigator("navigator", listView);
		searchResultPanel.add(pageNavigator);
		searchResultPanel.add(listView);
		resultListContainer.add(searchResultPanel);
		return resultListContainer;
	}
	
	
	private WebMarkupContainer initialiseDetailPanel(){
		
		detailsPanel = new Details("detailsPanel", resultListContainer, feedBackPanel, detailPanelContainer,searchPanelContainer,containerForm);
		detailsPanel.initialisePanel();
		detailPanelContainer.add(detailsPanel);
		return detailPanelContainer;
		
	}
	
	private WebMarkupContainer initialiseSearchPanel(){
		StudyCompVo studyCompVo = new StudyCompVo();
		
		//Get a result-set by default
		List<StudyComp> resultList = new ArrayList<StudyComp>();
		Long sessionStudyId = (Long)SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.core.Constants.STUDY_CONTEXT_ID);
		try {
			if(sessionStudyId != null && sessionStudyId > 0){
				resultList = studyService.searchStudyComp(studyCompVo.getStudyComponent());	
			}
			
		} catch (ArkSystemException e) {
			this.error("A System error occured  while initializing Search Panel");
		}
		
		studyCompModel.getObject().setStudyCompList(resultList);
		
		searchComponentPanel = new Search("searchComponentPanel", 
											feedBackPanel, 
											searchPanelContainer, 
											listView,
											resultListContainer,
											detailPanelContainer,
											detailsPanel,
											containerForm);
		
		searchComponentPanel.initialisePanel(studyCompModel);
		searchPanelContainer.add(searchComponentPanel);
		return searchPanelContainer;
	}
	

}
