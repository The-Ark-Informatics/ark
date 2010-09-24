package au.org.theark.study.web.component.studycomponent;

import java.util.List;

import org.apache.shiro.SecurityUtils;
import org.apache.wicket.ajax.AjaxRequestTarget;
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
import au.org.theark.study.service.IUserService;
import au.org.theark.study.web.Constants;
import au.org.theark.study.web.component.studycomponent.SearchResultList;
import au.org.theark.study.web.form.SearchStudyCompForm;

public class Search extends Panel{

	public Search(String id) {
		super(id);
		// TODO Auto-generated constructor stub
	}
	
	private FeedbackPanel feedBackPanel;
	private CompoundPropertyModel<StudyCompVo> cpm;
	private PageableListView<StudyComp> listView;
	private SearchResultList searchResults;
	
	//The container to wrap the Search Result List
	private WebMarkupContainer listContainer;
	//The Container to wrap the details panel
	private WebMarkupContainer detailsContainer;
	//private Details detailsPanel;
	
	//The type of object returned by the List
	private IModel<Object> iModel;
	
	@SpringBean( name = Constants.STUDY_SERVICE)
	private IStudyService studyService;
	
	@SpringBean( name = "userService")
	private IUserService userService;

	public void initialise(){
		
		feedBackPanel= new FeedbackPanel("feedbackMessage");
		feedBackPanel.setOutputMarkupId(true);
		
		cpm = new CompoundPropertyModel<StudyCompVo>(new StudyCompVo());
		
		//The wrapper for ResultsList panel that will contain a ListView
		listContainer = new WebMarkupContainer("resultListContainer");
		listContainer.setOutputMarkupPlaceholderTag(true);
		listContainer.setVisible(true);
		
		detailsContainer = new WebMarkupContainer("detailsContainer");
		detailsContainer.setOutputMarkupPlaceholderTag(true);
		detailsContainer.setVisible(false);
		
		//Initialise the Details Panel	
//		detailsPanel = new Details("detailsPanel", listContainer,feedBackPanel,detailsContainer);
//		detailsPanel.setCpm(cpm);
//		detailsPanel.initialisePanel();
//		detailsContainer.add(detailsPanel);
		
		iModel = new LoadableDetachableModel<Object>() {
			private static final long serialVersionUID = 1L;

			@Override
			protected Object load() {
				return cpm.getObject().getStudyCompList();
			}
		};
		
		initialiseSearchResults();
		
		//Get the study id from the session and get the study
		Long sessionStudyId = (Long)SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.core.Constants.STUDY_CONTEXT_ID);
		
		
//		if(sessionStudyId != null){
//			Study study = studyService.getStudy(sessionStudyId);
//			//For the given study get a list of applications.
//		}
		
		SearchStudyCompForm searchStudyCompForm = new SearchStudyCompForm(Constants.SEARCH_FORM, cpm){
			
			protected  void onSearch(AjaxRequestTarget target){
				
				//Get a list of StudyComponents with the given criteria
				try{
					List<StudyComp> resultList = studyService.searchStudyComp(cpm.getObject().getStudyComponent());
					
					
					if(resultList != null && resultList.size() == 0){
						this.info("Site with the specified criteria does not exist in the system.");	
					}
					
					this.setModelObject(new StudyCompVo());
					cpm = (CompoundPropertyModel<StudyCompVo>)this.getModel();////reset the original one
					cpm.getObject().setStudyCompList(resultList);//Place the results into the model
					listView.removeAll();
					listContainer.setVisible(true);//Make the WebMarkupContainer that houses the search results visible
					target.addComponent(listContainer);//For ajax this is required so 
				}catch(ArkSystemException arkEx){
					this.error("A system error has occured. Please try after sometime.");
				}
				
			}
			
			protected void onNew(AjaxRequestTarget target){
				// Show the details panel name and description
				StudyCompVo studyCompVo = new StudyCompVo();
				studyCompVo.setMode(Constants.MODE_NEW);
				this.setModelObject(studyCompVo);
				cpm = (CompoundPropertyModel<StudyCompVo>)this.getModel();
				//Any pre-population do it here
				//detailsPanel.setCpm(cpm);
				// List of users linked to the study
				processDetail(target, Constants.MODE_NEW);
			}
		};
		
		searchStudyCompForm.add(listContainer);
		searchStudyCompForm.add(detailsContainer);
		add(searchStudyCompForm);
		add(feedBackPanel);
		
	}
	
	public void processDetail(AjaxRequestTarget target, int mode){
		//Enable the name field of the site here
		//detailsPanel.setCpm(cpm);
		detailsContainer.setVisible(true);
		listContainer.setVisible(false);
		target.addComponent(detailsContainer);
		target.addComponent(listContainer);
	}
		
	private void initialiseSearchResults(){
		
		searchResults = new SearchResultList("searchResults",detailsContainer);
		searchResults.setCpm(cpm);

		listView  = searchResults.buildPageableListView(iModel, listContainer);
		listView.setReuseItems(true);

		PagingNavigator pageNavigator = new PagingNavigator("navigator", listView);
		
		searchResults.add(pageNavigator);
		searchResults.add(listView);
		listContainer.add(searchResults);
	}

}
