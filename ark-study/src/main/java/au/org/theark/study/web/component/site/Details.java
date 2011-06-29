package au.org.theark.study.web.component.site;

import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import au.org.theark.core.exception.ArkSystemException;
import au.org.theark.core.exception.EntityExistsException;
import au.org.theark.core.vo.SiteModelVO;
import au.org.theark.study.service.IStudyService;
import au.org.theark.study.web.Constants;
import au.org.theark.study.web.component.site.form.ContainerForm;
import au.org.theark.study.web.component.site.form.SiteForm;

public class Details extends Panel{
	
	private CompoundPropertyModel<SiteModelVO> cpm;
	
	@SpringBean(name ="studyService")
	private IStudyService studyService;
	
	private SiteForm siteForm;
	
	private FeedbackPanel feedBackPanel;
	private WebMarkupContainer listContainer;
	private WebMarkupContainer detailsContainer;
	private WebMarkupContainer searchPanelContainer;
	private WebMarkupContainer resultPanelContainer;
	private ContainerForm containerForm;

	public Details(String id, final WebMarkupContainer resultListContainer, FeedbackPanel feedBackPanel,
					WebMarkupContainer detailPanelContainer,WebMarkupContainer searchPanelContainer, ContainerForm siteContainerForm){
		super(id);
		this.feedBackPanel = feedBackPanel;
		this.listContainer = resultListContainer;
		this.detailsContainer = detailPanelContainer;
		this.searchPanelContainer = searchPanelContainer;
		this.resultPanelContainer = resultListContainer;
		containerForm = siteContainerForm;
	}
	
	public void initialisePanel(){
		
		siteForm = new SiteForm("siteForm",this,listContainer,detailsContainer,containerForm){
			
			protected void onSave(SiteModelVO siteModel, AjaxRequestTarget target){
				//Invoke the service layer to save the Site details in LDAP
				//Build the list of users who will be linked to this site
				List<String> siteMembers = new ArrayList<String>();
				siteMembers.add(au.org.theark.study.service.Constants.ARK_SYSTEM_USER);//Add rest of the members
				
			//	try{
					if(siteModel.getMode() == Constants.MODE_NEW){
						/* This will change a bit when we get the members from the front end*/
						siteModel.getSiteVo().setSiteMembers(siteMembers);
					//	studyService.createSite(siteModel.getSiteVo());
						this.info("The site " + siteModel.getSiteVo().getSiteName() + " was created sucessfully.");
						processFeedback(target);
					}else{
						//When users are added to the site then update it
						siteModel.getSiteVo().setSiteMembers(siteMembers);
						//studyService.updateSite(siteModel.getSiteVo());
						this.info("The site " + siteModel.getSiteVo().getSiteName() + " was updated sucessfully.");
						processFeedback(target);
					}
//				}catch(EntityExistsException exits){
//					this.error("The site you entered already exists in the system.");
//					processFeedback(target);
//				}catch(ArkSystemException arksystem){
//					this.error("A system error has occured. Please try after some time.");
//					processFeedback(target);
//				}
				
				System.out.println("Site Name ");
				target.addComponent(feedBackPanel);
			}
			
			protected void onCancel(AjaxRequestTarget target){
				SiteModelVO siteModelVO = new SiteModelVO();
				containerForm.setModelObject(siteModelVO);
				
				searchPanelContainer.setVisible(true);
				resultPanelContainer.setVisible(true);
				target.addComponent(searchPanelContainer);
				target.addComponent(resultPanelContainer);
			}
			
			protected void processFeedback(AjaxRequestTarget target){
				target.addComponent(feedBackPanel);
			}
		};
		
		siteForm.initialiseForm();
		add(siteForm);
	}

}
