package au.org.theark.study.web.component.site;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.apache.shiro.SecurityUtils;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import au.org.theark.study.model.entity.LinkSiteContact;
import au.org.theark.study.model.entity.LinkStudyStudysite;
import au.org.theark.study.model.entity.Person;
import au.org.theark.study.model.entity.Study;
import au.org.theark.study.service.IStudyService;
import au.org.theark.study.web.Constants;
import au.org.theark.study.web.form.SearchSiteForm;
public class Search extends Panel{

	public Search(String id) {
		super(id);
	}
	
	private FeedbackPanel feedBackPanel;
	private CompoundPropertyModel<SiteModel> cpm;
	private WebMarkupContainer listContainer;
	private WebMarkupContainer detailsContainer;
	private IModel<Object> iModel;
	@SpringBean( name = Constants.STUDY_SERVICE)
	private IStudyService studyService;

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
		//TODO
		
		initialiseSearchResults();
		//Get the study id from the session and get the study
		
		Long sessionStudyId = (Long)SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.core.Constants.STUDY_CONTEXT_ID);
		List<Person> availablePersons = new ArrayList<Person>();
		if(sessionStudyId != null){
			Study study = studyService.getStudy(sessionStudyId);
			//A study can have more than one site for operation
			//each site can have one or more persons
			//To list all persons linked to the sites we need to gather a distinct of each site person and add to a list
			
			Set<LinkStudyStudysite> availableStudySites = study.getLinkStudyStudysites();
			
			
			for (LinkStudyStudysite linkStudySite : availableStudySites) {
				
				Set<LinkSiteContact> contacts = linkStudySite.getStudySite().getLinkSiteContacts();
				
				for (LinkSiteContact linkSiteContact : contacts) {
					
					availablePersons.add(linkSiteContact.getPerson());
				}
			}
			
			System.out.println("Total number of contacts linked to this study's sites are: " + availablePersons.size());
			
		}
		
		SearchSiteForm searchSiteForm = new SearchSiteForm(Constants.SEARCH_FORM, cpm,availablePersons){
			
			protected  void onSearch(AjaxRequestTarget target){
				
			}
			
			protected void onNew(AjaxRequestTarget target){
				
			}
		};
		
		//searchSiteForm.add(listContainer);
		//searchSiteForm.add(detailsContainer);
		add(searchSiteForm);
		add(feedBackPanel);
		
	}
	
	private void initialiseSearchResults(){
		
	}
}
