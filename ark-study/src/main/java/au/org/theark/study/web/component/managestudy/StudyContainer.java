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
import org.odlabs.wiquery.ui.themes.ThemeUiHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import au.org.theark.study.model.entity.Study;
import au.org.theark.study.service.IStudyService;
import au.org.theark.study.web.Constants;
import au.org.theark.study.web.component.managestudy.form.Container;
import au.org.theark.study.web.component.study.StudyModel;

public class StudyContainer extends Panel{


	private static final long serialVersionUID = 1L;
	private transient Logger log = LoggerFactory.getLogger(StudyContainer.class);
	
	/* Model */
	private CompoundPropertyModel<StudyModel> cpm;
	
	@SpringBean( name = Constants.STUDY_SERVICE)
	private IStudyService studyService;
	
	private IModel<Object> iModel;
	private PageableListView<Study> pageableListView;
	private WebMarkupContainer resultListContainer;//Search Results Container
	
	//Child Components
	private Search searchStudyPanel;
	private WebMarkupContainer detailsContainer;
	private WebMarkupContainer searchWebMarkupContainer;
	private WebMarkupContainer saveArchivebuttonContainer;
	private WebMarkupContainer editbuttonContainer;
	private WebMarkupContainer summaryContainer;
	
	private Details detailsPanel;
	
	private FeedbackPanel feedBackPanel;
	
	public StudyContainer(String id) {
	
		super(id);

		//The Model is defined here
		cpm = new CompoundPropertyModel<StudyModel>(new StudyModel());

		
		feedBackPanel= new FeedbackPanel("feedbackMessage");
		feedBackPanel.setOutputMarkupId(true);
		
		//Create the form that will hold the other controls
		Container containerForm = new Container("containerForm",cpm);

		/* The markup container for search panel */
		searchWebMarkupContainer = new WebMarkupContainer("searchContainer");
		searchWebMarkupContainer.setOutputMarkupPlaceholderTag(true);
		
		detailsContainer = new WebMarkupContainer("detailsContainer");
		detailsContainer.setOutputMarkupPlaceholderTag(true);
		detailsContainer.setVisible(false);

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
		
		detailsPanel = new Details("detailsPanel", resultListContainer,feedBackPanel,detailsContainer,searchWebMarkupContainer,saveArchivebuttonContainer,editbuttonContainer, summaryContainer);//Need to pass feedback panle too
		detailsPanel.setCpm(cpm);
		detailsPanel.initialisePanel();
		detailsContainer.add(detailsPanel);
		
		//Pass this to the SearchResultListView control that will use this to render its values.
		//Also pass in the markup container for searchPanel i.e the searchContainer 
		SearchResults searchResultsPanel = new SearchResults("resultsPanel",searchWebMarkupContainer,detailsContainer,saveArchivebuttonContainer,editbuttonContainer, summaryContainer);
		searchResultsPanel.setCpm(cpm);

		
		//Initialise the Details Panel	
		Study study = new Study();
		cpm.getObject().setStudyList(studyService.getStudy(study));
		
		iModel = new LoadableDetachableModel<Object>() {
			private static final long serialVersionUID = 1L;

			@Override
			protected Object load() {
				return cpm.getObject().getStudyList();
			}
		};
		
		pageableListView = searchResultsPanel.buildPageableListView(iModel,resultListContainer);
		pageableListView.setReuseItems(true);
		PagingNavigator pageNavigator = new PagingNavigator("navigator", pageableListView);
		
		searchStudyPanel = new Search("searchStudyPanel",
										studyService.getListOfStudyStatus(),
										cpm,
										pageableListView,
										resultListContainer,
										searchWebMarkupContainer,
										detailsContainer,
										detailsPanel,saveArchivebuttonContainer,editbuttonContainer);
		
	
		
		
		searchStudyPanel.initialisePanel();
		searchWebMarkupContainer.add(searchStudyPanel);
		
		searchResultsPanel.add(pageNavigator);
		searchResultsPanel.add(pageableListView);
		
		
		resultListContainer.add(searchResultsPanel);
		
		
		
		//Add the search control into the searchContainer
		containerForm.add(searchWebMarkupContainer);
		containerForm.add(resultListContainer);
		//containerForm.add(summaryWebMarkupContainer);
		//Add the details into the containerForm
		containerForm.add(detailsContainer);
		add(containerForm);
	}

}
