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
import au.org.theark.study.web.Constants;
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
					if(siteModel.getMode() == Constants.MODE_NEW){
						/* This will change a bit when we get the members from the front end*/
						siteModel.getSiteVo().setSiteMembers(siteMembers);
						studyService.createSite(siteModel.getSiteVo());
						cpm.getObject().setMode(Constants.MODE_EDIT);
						this.info("The site " + siteModel.getSiteVo().getSiteName() + " was created sucessfully.");
						processFeedback(target);
					}else{
						//When users are added to the site then update it
						siteModel.getSiteVo().setSiteMembers(siteMembers);
						studyService.updateSite(siteModel.getSiteVo());
						this.info("The site " + siteModel.getSiteVo().getSiteName() + " was updated sucessfully.");
						processFeedback(target);
					}
				}catch(EntityExistsException exits){
					this.error("The site you entered already exists in the system.");
					processFeedback(target);
				}catch(ArkSystemException arksystem){
					this.error("A system error has occured. Please try after some time.");
					processFeedback(target);
				}
				
				System.out.println("Site Name ");
				target.addComponent(feedBackPanel);
			}
			
			protected void processFeedback(AjaxRequestTarget target){
				target.addComponent(feedBackPanel);
			}
		};
		
		siteForm.initialiseForm();
		add(siteForm);
	}

}
