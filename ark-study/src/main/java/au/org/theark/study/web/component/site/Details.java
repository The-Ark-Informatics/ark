package au.org.theark.study.web.component.site;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import au.org.theark.core.exception.ArkSystemException;
import au.org.theark.core.exception.EntityExistsException;
import au.org.theark.study.service.IStudyService;
import au.org.theark.study.service.IUserService;
import au.org.theark.study.web.form.SiteForm;
import java.util.*;

public class Details extends Panel{
	
	private CompoundPropertyModel<SiteModel> cpm;
	
	@SpringBean( name = "userService")
	private IUserService userService;
	
	@SpringBean(name ="studyService")
	private IStudyService studyService;
	
	private SiteForm siteForm;
	
	private FeedbackPanel feedBackPanel;
	private WebMarkupContainer listContainer;
	private WebMarkupContainer detailsContainer;
	
	public CompoundPropertyModel<SiteModel> getCpm() {
		return cpm;
	}

	public void setCpm(CompoundPropertyModel<SiteModel> cpm) {
		this.cpm = cpm;
	}

	public Details(String id, final WebMarkupContainer listContainer, FeedbackPanel feedBackPanel, WebMarkupContainer detailsContainer){
		super(id);
		this.feedBackPanel = feedBackPanel;
		this.listContainer = listContainer;
		this.detailsContainer = detailsContainer;
	}
	
	public void initialisePanel(){
		
		siteForm = new SiteForm("siteForm",this,listContainer,detailsContainer){
			
			protected void onSave(SiteModel siteModel, AjaxRequestTarget target){
				//Invoke the service layer to save the Site details in LDAP
				//Build the list of users who will be linked to this site
				List<String> siteMembers = new ArrayList<String>();
				siteMembers.add(au.org.theark.study.service.Constants.ARK_SYSTEM_USER);//Add rest of the members
				try{
					studyService.createSite(siteModel.getSiteName(), siteModel.getSiteDescription(), siteMembers);	
				}catch(EntityExistsException exits){
					this.error("The site you entered already exists in the system.");
					processErrors(target);
				}catch(ArkSystemException arksystem){
					this.error("A system error has occured. Please try after some time.");
					processErrors(target);
				}
				
				System.out.println("Site Name ");
				target.addComponent(feedBackPanel);
			}
			
			protected void processErrors(AjaxRequestTarget target){
				target.addComponent(feedBackPanel);
			}
		};
		
		siteForm.initialiseForm();
		add(siteForm);
	}

}
