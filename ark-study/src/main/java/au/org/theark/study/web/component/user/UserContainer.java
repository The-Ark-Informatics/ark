package au.org.theark.study.web.component.user;

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
import au.org.theark.core.vo.ArkUserVO;
import au.org.theark.study.service.IUserService;
import au.org.theark.study.web.component.IArkComponent;

import au.org.theark.study.web.component.user.form.ContainerForm;
/**
 * A top level container that will have members like a Search control component and a 
 * User Details panel.
 * @author nivedann
 *
 */
public class UserContainer extends Panel implements IArkComponent{
	
	private static final long serialVersionUID = 1L;

	private ContainerForm containerForm;
	
	private IModel<Object> iModel;
	private PageableListView<ArkUserVO> pageableListView;
	
	private Search searchUserPanel;
	private Details detailPanel;
	
	private WebMarkupContainer resultListContainer;//Search Results Container
	private WebMarkupContainer detailsContainer;
	private WebMarkupContainer detailFormContainer;
	private WebMarkupContainer searchWebMarkupContainer;
	
	private FeedbackPanel feedBackPanel;

	/*The object that will house the data for the site use case */
	private CompoundPropertyModel<ArkUserVO> arkUserModelCpm;
	
	/* 
	 * Spring injected reference for User service implementation. Use it to look up users or persist using the userService
	 * userService.create(EtaUserVo user);
	 **/
	@SpringBean( name = "userService")
	private IUserService userService;
	
	
	
	public void initialiseMarkupContainers(){
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
	
	}

	public FeedbackPanel initialiseFeedBackPanel(){
		/* Feedback Panel */
		feedBackPanel= new FeedbackPanel("feedbackMessage");
		feedBackPanel.setOutputMarkupId(true);
		return feedBackPanel;
	}
	
	public WebMarkupContainer initialiseSearchResults(){

		SearchResultList searchResultsPanel = new SearchResultList("resultsPanel",searchWebMarkupContainer,detailsContainer,containerForm,detailPanel);
		
		iModel = new LoadableDetachableModel<Object>() {
			private static final long serialVersionUID = 1L;

			@Override
			protected Object load() {
				return containerForm.getModelObject().getUserList(); 
			}
		};
		
		pageableListView = searchResultsPanel.buildUserPageableListView(iModel, resultListContainer);
		pageableListView.setReuseItems(true);
		
		PagingNavigator pageNavigator = new PagingNavigator("navigator", pageableListView);
		searchResultsPanel.add(pageNavigator);
		searchResultsPanel.add(pageableListView);
		
		resultListContainer.add(searchResultsPanel);
		return resultListContainer;
	}
	
	
	
	/**
	 * Constructor
	 * @param id
	 * @param userVO
	 */
	public UserContainer(String id, ArkUserVO userVO) {
		
		super(id);
		
		/*Initialise the CPM */
		arkUserModelCpm = new CompoundPropertyModel<ArkUserVO>(new ArkUserVO());
		
		containerForm = new ContainerForm("containerForm", arkUserModelCpm);
		
		initialiseMarkupContainers();
		
		containerForm.add(initialiseFeedBackPanel());
		
		containerForm.add(initialiseDetailPanel());
	
		containerForm.add(initialiseSearchResults());
		
		containerForm.add(initialiseSearchPanel());
		
		add(containerForm);
		
	}

	public WebMarkupContainer initialiseDetailPanel() {
		
		detailPanel = new Details("detailsPanel", resultListContainer, feedBackPanel, detailsContainer,searchWebMarkupContainer,containerForm);
		detailPanel.initialisePanel();
		detailsContainer.add(detailPanel);
		return detailsContainer;
	}


	public WebMarkupContainer initialiseSearchPanel() {
		
		
		Long sessionStudyId = (Long)SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.core.Constants.STUDY_CONTEXT_ID);
		
		
		ArkUserVO arkUserVO = new ArkUserVO();
		try{
			List<ArkUserVO> userResultList = new ArrayList<ArkUserVO>();
			if(sessionStudyId != null && sessionStudyId > 0){
				 userResultList = userService.searchUser(arkUserVO);	
			}
			
			
			containerForm.getModelObject().setUserList(userResultList);
			searchUserPanel = new Search(	"searchUserPanel",
											feedBackPanel,
											searchWebMarkupContainer,
											pageableListView,
											resultListContainer,
											detailsContainer,
											detailPanel,
											containerForm);
			
			searchUserPanel.initialisePanel(arkUserModelCpm);

			searchWebMarkupContainer.add(searchUserPanel);
			
		}catch(ArkSystemException arkException){
			
		}
		return searchWebMarkupContainer;
	}



}
