package au.org.theark.study.web.component.studycomponent;

import org.apache.shiro.SecurityUtils;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import au.org.theark.core.exception.ArkSystemException;
import au.org.theark.core.exception.UnAuthorizedOperation;
import au.org.theark.core.model.study.entity.Study;
import au.org.theark.study.model.vo.StudyCompVo;
import au.org.theark.study.service.IStudyService;
import au.org.theark.study.service.IUserService;
import au.org.theark.study.web.component.studycomponent.form.ContainerForm;
import au.org.theark.study.web.component.studycomponent.form.DetailsForm;

public class Details extends Panel{
	
	private CompoundPropertyModel<StudyCompVo> cpm;
	
	@SpringBean( name = "userService")
	private IUserService userService;
	
	@SpringBean(name ="studyService")
	private IStudyService studyService;
	
	private DetailsForm detailsForm;
	
	private FeedbackPanel feedBackPanel;
	private WebMarkupContainer listContainer;
	private WebMarkupContainer detailsContainer;
	private WebMarkupContainer searchPanelContainer;
	private ContainerForm containerForm;
	private Study study;

	public Details(	String id, 
					final WebMarkupContainer listContainer, 
					FeedbackPanel feedBackPanel,
					WebMarkupContainer detailsContainer,
					WebMarkupContainer searchPanelContainer,
					ContainerForm containerForm){
		
		super(id);
		this.feedBackPanel = feedBackPanel;
		this.listContainer = listContainer;
		this.detailsContainer = detailsContainer;
		this.containerForm = containerForm;
		this.searchPanelContainer = searchPanelContainer;
	}
	
	public void initialisePanel(){
		
		detailsForm = new DetailsForm("detailsForm", this, listContainer, detailsContainer, containerForm){
			
			protected void onSave(StudyCompVo studyCompVo, AjaxRequestTarget target){
				//Do the save Persist the Study component and the attached documents to the backend/upload the files and persist the file payload
				//Enable Unhide a panel that will display a list of Files that "have been uploaded along with a download option"
				try {
					Long studyId = (Long)SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.core.Constants.STUDY_CONTEXT_ID);
					study =studyService.getStudy(studyId);
					studyCompVo.getStudyComponent().setStudy(study);
					
					if(studyCompVo.getStudyComponent().getStudyCompKey() == null){
						
						studyService.create(studyCompVo.getStudyComponent());
						this.info("Study Component " + studyCompVo.getStudyComponent().getName() + " was created successfully" );
						processFeedback(target);
					
					}else{
					
						studyService.update(studyCompVo.getStudyComponent());
						this.info("Study Component " + studyCompVo.getStudyComponent().getName() + " was updated successfully" );
						processFeedback(target);
						
					}
					
				} catch (UnAuthorizedOperation e) {
					 this.error("You are not authorised to manage study components for the given study " + study.getName());
					 processFeedback(target);
				} catch (ArkSystemException e) {
					this.error("A System error occured, we will have someone contact you.");
					processFeedback(target);
				}
				
			}
			
			protected void onCancel(AjaxRequestTarget target){
				StudyCompVo studyCompVo = new StudyCompVo();
				containerForm.setModelObject(studyCompVo);
				searchPanelContainer.setVisible(true);
				target.addComponent(searchPanelContainer);
				target.addComponent(feedBackPanel);
			}

			protected void processFeedback(AjaxRequestTarget target){
				target.addComponent(feedBackPanel);
			}
			
		};
		
		detailsForm.initialiseForm();
		add(detailsForm);
	}

	public DetailsForm getDetailsForm() {
		return detailsForm;
	}

	public void setDetailsForm(DetailsForm detailsForm) {
		this.detailsForm = detailsForm;
	}

}
