package au.org.theark.study.web.component.managestudy;

import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.list.PageableListView;
import org.apache.wicket.markup.html.navigation.paging.PagingNavigator;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import au.org.theark.core.model.study.entity.Study;
import au.org.theark.core.service.IArkCommonService;
import au.org.theark.core.vo.StudyModelVO;
import au.org.theark.study.service.IStudyService;
import au.org.theark.study.web.Constants;
import au.org.theark.study.web.component.managestudy.form.Container;

public class StudyContainer extends Panel{


	private static final long serialVersionUID = 1L;

	private Container containerForm;

	
//	@SpringBean( name = Constants.STUDY_SERVICE)
//	private IStudyService studyService;
	
	@SpringBean( name = au.org.theark.core.Constants.ARK_COMMON_SERVICE)
	private IArkCommonService iArkCommonService;
	
	private IModel<Object> iModel;
	private PageableListView<Study> pageableListView;
	private WebMarkupContainer resultListContainer;//Search Results Container
	
	//Child Components
	private Search searchStudyPanel;
	private WebMarkupContainer detailsContainer;
	private WebMarkupContainer detailFormContainer;
	private WebMarkupContainer searchWebMarkupContainer;
	private WebMarkupContainer saveArchivebuttonContainer;
	private WebMarkupContainer editbuttonContainer;
	private WebMarkupContainer summaryContainer;
	
	private Details detailsPanel;
	
	private FeedbackPanel feedBackPanel;
	
	
	private void initialiseMarkupContainers(){
		/* The markup container for search panel */
		searchWebMarkupContainer = new WebMarkupContainer("searchContainer");
		searchWebMarkupContainer.setOutputMarkupPlaceholderTag(true);
		
		detailsContainer = new WebMarkupContainer("detailsContainer");
		detailsContainer.setOutputMarkupPlaceholderTag(true);
		detailsContainer.setVisible(false);

		//Contains the controls of the details
		detailFormContainer = new WebMarkupContainer("detailFormContainer");
		detailFormContainer.setOutputMarkupPlaceholderTag(true);
		detailFormContainer.setEnabled(false);
		
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

		summaryContainer = new WebMarkupContainer("summaryPanel");
		summaryContainer.setOutputMarkupPlaceholderTag(true);
		summaryContainer.setVisible(false);

	}
	
	private WebMarkupContainer initialiseDetailPanel(){
		detailsPanel = new Details("detailsPanel", 
									resultListContainer,
									feedBackPanel,
									detailsContainer,
									searchWebMarkupContainer,
									saveArchivebuttonContainer,
									editbuttonContainer,
									summaryContainer,
									detailFormContainer,
									containerForm);//Need to pass feedback panel
				
				detailsPanel.initialisePanel();
				detailsContainer.add(detailsPanel);
				return detailsContainer;
	}
	
	private WebMarkupContainer initialiseSearchPanel(){
		
		searchStudyPanel = new Search(	"searchStudyPanel",
										feedBackPanel,
										iArkCommonService.getListOfStudyStatus(),
										//studyService.getListOfStudyStatus(),
										searchWebMarkupContainer,
										pageableListView,
										resultListContainer,
										detailsContainer,
										detailsPanel,
										saveArchivebuttonContainer,
										editbuttonContainer,
										detailFormContainer,
										containerForm);
		
		searchStudyPanel.initialisePanel();
		searchWebMarkupContainer.add(searchStudyPanel);
		return searchWebMarkupContainer;
	}
	
	private FeedbackPanel initialiseFeedBackPanel(){
		/* Feedback Panel */
		feedBackPanel= new FeedbackPanel("feedbackMessage");
		feedBackPanel.setOutputMarkupId(true);
		return feedBackPanel;
	}
	
	private WebMarkupContainer initialiseSearchResults(){
		
		//Initialise with a default list of study	
		Study study = new Study();
		
		//Invoke the Common Service to look up the Study
		containerForm.getModelObject().setStudyList(iArkCommonService.getStudy(study));
		
		//Pass this to the SearchResultListView control that will use this to render its values.
		//Also pass in the markup container for searchPanel i.e the searchContainer 
		SearchResults searchResultsPanel = new SearchResults(	"resultsPanel",
																searchWebMarkupContainer,
																detailsContainer,
																saveArchivebuttonContainer,
																editbuttonContainer, 
																summaryContainer,
																detailFormContainer,
																containerForm);
		iModel = new LoadableDetachableModel<Object>() {
			private static final long serialVersionUID = 1L;

			@Override
			protected Object load() {
				return containerForm.getModelObject().getStudyList(); 
			}
		};
		
		pageableListView = searchResultsPanel.buildPageableListView(iModel,resultListContainer);
		pageableListView.setReuseItems(true);
		
		PagingNavigator pageNavigator = new PagingNavigator("navigator", pageableListView);
		searchResultsPanel.add(pageNavigator);
		searchResultsPanel.add(pageableListView);
		
		resultListContainer.add(searchResultsPanel);
		return resultListContainer;

	}
	
	
	public StudyContainer(String id) {
	
		super(id);

		initialiseMarkupContainers();
		//Create the form that will hold the other controls
		containerForm = new Container("containerForm",new CompoundPropertyModel<StudyModelVO>(new StudyModelVO()));
		
		containerForm.add(initialiseFeedBackPanel());
		containerForm.add(initialiseDetailPanel());		
		containerForm.add(initialiseSearchResults());
		containerForm.add(initialiseSearchPanel());
		
		add(containerForm);
		
	}

}
