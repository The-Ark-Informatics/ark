package au.org.theark.study.web.component.study;

import java.util.ArrayList;
import java.util.List;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.ThreadContext;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import au.org.theark.core.security.RoleConstants;
import au.org.theark.study.model.entity.Study;
import au.org.theark.study.service.IStudyService;
import au.org.theark.study.web.Constants;

public class Search extends Panel {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private transient Logger log = LoggerFactory.getLogger(Search.class);
	private SearchResultList searchResults;
	private Details detailsPanel;
	private FeedbackPanel feedBackPanel = new FeedbackPanel("feedbackMessage");

	public SearchResultList getSearchResults() {
		return searchResults;
	}

	public void setSearchResults(SearchResultList searchResults) {
		this.searchResults = searchResults;
	}
	
	public void setDetailsPanelVisible(boolean isVisible){
		detailsPanel.setVisible(isVisible);
	}
	
	@SpringBean( name = Constants.STUDY_SERVICE)
	private IStudyService service;
	
	public Search(String id) {
		
		super(id);
		//Create a new instance of the details panel
		detailsPanel = new Details("detailsPanel", new Study(), this);
		//Hide it since we have not looked up as yet
		setDetailsPanelVisible(false);
		
		// Uses an entirely new VO for the search so each time the search panel is loaded. The values provided will be refreshed.
		SearchForm searchForm = new SearchForm(Constants.SEARCH_FORM, new Study(), id){
			/*When user has clicked on the Search Button*/
			protected  void onSearch(Study study){
				log.info("Look up the user");
				setDetailsPanelVisible(false);//Set the User Details panel to false/hide it
				List<Study> resultList = service.getStudy(study);
				if(resultList != null && resultList.size() == 0){
					this.info("Study with the specified criteria does not exist in the system.");	
				}
				//Render the Search Results
				//TODO NN Removing the panel is not efficient, I need to use another technique (Ajax)
				remove(searchResults);//Since we already have the panel.We need to partially update the list the panel uses  rather than action it at the panel level
				searchResults = new SearchResultList("searchResults", resultList,detailsPanel);
				add(searchResults);
			}
		};

		//Add the Form to the Panel. The Form object that will contain the child or UI components that will be part of the search or be affected by the search.
		add(searchForm);
		add(feedBackPanel); //Add feedback panel
		/*
		 * Create an instance of the Details panel. When the mode is in lookup hide the panel.
		 * The details panel is only visible when user navigates an item via the ResultsList or when creating a New User.
		 */
		 /* Add the UserDetailsPanel into the scope of the SearchUserForm instance*/
		searchForm.add(detailsPanel);
		
		//Just an empty list
		List<Study> studyList = new ArrayList<Study>();
		Study study = new Study();
		studyList.add(study);
		searchResults = new SearchResultList("searchResults",studyList,detailsPanel);
		searchForm.add(searchResults);
	
	}
	
	public class SearchForm extends Form<Study>{

		TextField<String> studyIdTxtField =new TextField<String>(Constants.STUDY_KEY);
		TextField<String> studyNameTxtField = new TextField<String>(Constants.STUDY_NAME);
		TextField<String> studyStatusTxtField = new TextField<String>(Constants.STUDY_STATUS);
		TextField<String> dateOfApplicationTxtField = new TextField<String>(Constants.STUDY_DATE_OF_APPLICATION);//Change this to a calendar
		TextField<String> principalContactTxtField = new TextField<String>(Constants.STUDY_CONTACT);
		
		
		private void initFormFields(){
			//TODO
		}
		
		
		public SearchForm(String id, Study study, String panelId){

			super(id, new CompoundPropertyModel<Study>(study));
			add(studyIdTxtField);
			add(studyNameTxtField);
			add(studyStatusTxtField);
			add(dateOfApplicationTxtField);
			add(principalContactTxtField);
			
			add(new Button(Constants.SEARCH, new StringResourceModel("page.search", this, null))
			{
				public void onSubmit()
				{
					
					onSearch((Study) getForm().getModelObject());
				}
			});
			
			add(new Button(Constants.NEW, new StringResourceModel("page.new", this, null))
			{
				public void onSubmit()
				{
					//Go to Search users page
					//The mode will be new here
					Study study = new Study();
					onNew(study);
				}
				@Override
				public boolean isVisible(){
					
					SecurityManager securityManager =  ThreadContext.getSecurityManager();
					Subject currentUser = SecurityUtils.getSubject();		
					boolean flag = false;
					if(		securityManager.hasRole(currentUser.getPrincipals(), RoleConstants.ARK_SUPER_ADMIN) ||
							securityManager.hasRole(currentUser.getPrincipals(), RoleConstants.STUDY_ADMIN)){
						flag = true;
					}
					//if it is a Super or Study admin then make the new available
					return flag;
				}
			
			});
			

		}
		
		protected void onSearch(Study Study){
			
		}
		
		protected void onNew(Study study){
			detailsPanel.setVisible(true);
			detailsPanel.getStudyForm().setModelObject(study);
			searchResults.setVisible(false);
		}
	}



}
