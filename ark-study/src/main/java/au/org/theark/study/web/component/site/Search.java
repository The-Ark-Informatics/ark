package au.org.theark.study.web.component.site;

import java.util.ArrayList;
import java.util.List;

import org.apache.shiro.SecurityUtils;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import au.org.theark.study.model.entity.Person;
import au.org.theark.study.model.entity.Study;
import au.org.theark.study.service.IStudyService;
import au.org.theark.study.service.IUserService;
import au.org.theark.study.web.Constants;
import au.org.theark.study.web.form.SearchSiteForm;
public class Search extends Panel{

	public Search(String id) {
		super(id);
	}
	
	private FeedbackPanel feedBackPanel;
	private CompoundPropertyModel<SiteModel> cpm;
	
	//The container to wrap the Search Result List
	private WebMarkupContainer listContainer;
	//The Container to wrap the details panel
	private WebMarkupContainer detailsContainer;
	private Details detailsPanel;
	
	//The type of object returned by the List
	private IModel<Object> iModel;
	
	@SpringBean( name = Constants.STUDY_SERVICE)
	private IStudyService studyService;
	
	@SpringBean( name = "userService")
	private IUserService userService;

	public void initialise(){
		
		feedBackPanel= new FeedbackPanel("feedbackMessage");
		feedBackPanel.setOutputMarkupId(true);
		
		cpm = new CompoundPropertyModel<SiteModel>(new SiteModel());
		
		//The wrapper for ResultsList panel that will contain a ListView
		listContainer = new WebMarkupContainer("resultListContainer");
		listContainer.setOutputMarkupPlaceholderTag(true);
		listContainer.setVisible(true);
		
		detailsContainer = new WebMarkupContainer("detailsContainer");
		detailsContainer.setOutputMarkupPlaceholderTag(true);
		detailsContainer.setVisible(false);
		
		//Initialise the Details Panel	
		detailsPanel = new Details("detailsPanel", listContainer,feedBackPanel,detailsContainer);
		detailsPanel.setCpm(cpm);
		detailsPanel.initialisePanel();
		detailsContainer.add(detailsPanel);
		
		//iModel = get a list of sites here
		
		
		initialiseSearchResults();
		//Get the study id from the session and get the study
		Long sessionStudyId = (Long)SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.core.Constants.STUDY_CONTEXT_ID);
		
		List<Person> availablePersons = new ArrayList<Person>();
		if(sessionStudyId != null){
			Study study = studyService.getStudy(sessionStudyId);
			//For the given study get a list of applications.
		}
		
		SearchSiteForm searchSiteForm = new SearchSiteForm(Constants.SEARCH_FORM, cpm,availablePersons){
			
			protected  void onSearch(AjaxRequestTarget target){
				
			}
			
			protected void onNew(AjaxRequestTarget target){
				// Show the details panel name and description
				this.setModelObject(new SiteModel());
				cpm = (CompoundPropertyModel<SiteModel>)this.getModel();
				//Any pre-population do it here
				detailsPanel.setCpm(cpm);
				// List of users linked to the study
				processDetail(target, Constants.MODE_NEW);
			}
		};
		
		searchSiteForm.add(listContainer);
		searchSiteForm.add(detailsContainer);
		add(searchSiteForm);
		add(feedBackPanel);
		
	}
	
	public void processDetail(AjaxRequestTarget target, int mode){
		//Enable the name field of the site here
		detailsPanel.setCpm(cpm);
		detailsContainer.setVisible(true);
		listContainer.setVisible(false);
		target.addComponent(detailsContainer);
		target.addComponent(listContainer);
	}
		
	private void initialiseSearchResults(){
		
	}
}
