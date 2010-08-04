package au.org.theark.study.web.component.study;

import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import au.org.theark.study.model.entity.Study;
import au.org.theark.study.service.IStudyService;
import au.org.theark.study.web.Constants;
import au.org.theark.study.web.form.SearchStudyForm;

@SuppressWarnings("serial")
public class Search extends Panel {
	

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
	private IStudyService studyService;
	
	
	public Search(String id) {
		super(id);
	}
	
	public void process(String id){
		//Create a new instance of the details panel
		StudyModel studyModel = new StudyModel();
		studyModel.setStudy(new Study());
		
		detailsPanel = new Details("detailsPanel", this,Constants.MODE_NEW);
		detailsPanel.setOutputMarkupPlaceholderTag(true);
		detailsPanel.initialiseForm(studyModel);
		
		//Hide it since we have not looked up as yet
		setDetailsPanelVisible(false);
		
		// Uses an entirely new VO for the search so each time the search panel is loaded. The values provided will be refreshed.
		SearchStudyForm searchForm = new SearchStudyForm(Constants.SEARCH_FORM, new Study(), id,studyService.getListOfStudyStatus(), detailsPanel){
			/*When user has clicked on the Search Button*/
			protected  void onSearch(Study study){
				setDetailsPanelVisible(false);//Set the User Details panel to false/hide it
				List<Study> resultList = studyService.getStudy(study);
				if(resultList != null && resultList.size() == 0){
					this.info("Study with the specified criteria does not exist in the system.");	
				}
				
				//Render the Search Results
				//TODO NN Removing the panel is not efficient, I need to use another technique (Ajax)
				remove(searchResults);//Since we already have the panel.We need to partially update the list the panel uses  rather than action it at the panel level
				searchResults = new SearchResultList("searchResults", resultList,detailsPanel);
				add(searchResults);
			}
		
			protected void onNew(Study study){
//				detailsPanel.getStudyForm().setDefaultModelObject(new StudyModel());
//				detailsPanel.getStudyForm().getStudyIdTxtFld().setEnabled(false);
//				detailsPanel.getStudyForm().getStudyNameTxtFld().setEnabled(true);
				//detailsPanel.setVisible(true);
				StudyModel studyModel = new StudyModel();
				studyModel.setStudy(study);
				detailsPanel.getStudyForm().setModelObject(studyModel);
				detailsPanel.getStudyForm().getStudyIdTxtFld().setEnabled(false);
				detailsPanel.getStudyForm().getStudyNameTxtFld().setEnabled(true);
			
				detailsPanel.setVisible(true);
				searchResults.setVisible(false);
				
			}
		
		};

		//Add the Form to the Panel. The Form object that will contain the child or UI components that will be part of the search or be affected by the search.
		add(searchForm);
		add(feedBackPanel); //Add feedback panel
		searchForm.add(detailsPanel);
		//Just an empty list
		List<Study> studyList = new ArrayList<Study>();
		Study study = new Study();
		studyList.add(study);
		searchResults = new SearchResultList("searchResults",studyList,detailsPanel);
		searchForm.add(searchResults);
	}

}
