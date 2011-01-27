package au.org.theark.study.web.component.managestudy;

import org.apache.shiro.SecurityUtils;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import au.org.theark.core.exception.ArkSystemException;
import au.org.theark.core.exception.EntityCannotBeRemoved;
import au.org.theark.core.exception.EntityExistsException;
import au.org.theark.core.exception.StatusNotAvailableException;
import au.org.theark.core.exception.UnAuthorizedOperation;
import au.org.theark.core.util.ContextHelper;
import au.org.theark.core.vo.StudyModelVO;
import au.org.theark.study.service.IStudyService;
import au.org.theark.study.web.Constants;
import au.org.theark.study.web.component.managestudy.form.Container;
import au.org.theark.study.web.component.managestudy.form.DetailForm;

public class Details  extends Panel{

	//A container for SearchResults
	private WebMarkupContainer listContainer;
	private WebMarkupContainer detailsContainer;
	private WebMarkupContainer summaryContainer;
	private WebMarkupContainer searchContainer;
	private WebMarkupContainer saveArchivebuttonContainer;
	private WebMarkupContainer editbuttonContainer;
	private WebMarkupContainer detailFormContainer;
	private WebMarkupContainer studyLogoImageContainer;
	private WebMarkupContainer studyNameMarkup;
	private WebMarkupContainer studyLogoMarkup;
	private WebMarkupContainer studyLogoUploadMarkup;
	WebMarkupContainer arkContextMarkup;
	private Container studyContainerForm;
	
	
	@SpringBean( name = Constants.STUDY_SERVICE)
	private IStudyService service;
	
	
	public DetailForm getStudyForm() {
		return detailForm;
	}

	private DetailForm detailForm;
	
	private FeedbackPanel feedBackPanel;
	
	/**
	 * Sets the id and the container reference for the search results.
	 * @param id
	 * @param listContainer
	 */
	public Details(	String id, 
					final WebMarkupContainer listContainer, 
					FeedbackPanel feedBackPanel, 
					WebMarkupContainer detailsContainer, 
					WebMarkupContainer searchWebMarkupContainer, 
					WebMarkupContainer saveArchBtnContainer, 
					WebMarkupContainer editBtnContainer,
					WebMarkupContainer detailSumContainer,
					WebMarkupContainer detailFormContainer,
					WebMarkupContainer studyNameMarkup,
					WebMarkupContainer studyLogoMarkup,
					WebMarkupContainer studyLogoImageContainer,
					WebMarkupContainer studyLogoUploadMarkup,
					WebMarkupContainer arkContextMarkup,
					Container studyContainerForm) {
		super(id);
		this.listContainer = listContainer;
		this.feedBackPanel = feedBackPanel;
		this.detailsContainer = detailsContainer;
		this.searchContainer = searchWebMarkupContainer;
		this.saveArchivebuttonContainer = saveArchBtnContainer;
		this.editbuttonContainer = editBtnContainer;
		this.summaryContainer = detailSumContainer;
		this.detailFormContainer = detailFormContainer;
		this.studyContainerForm = studyContainerForm;
		this.studyNameMarkup = studyNameMarkup;
		this.studyLogoMarkup = studyLogoMarkup;
		this.studyLogoImageContainer = studyLogoImageContainer;
		this.studyLogoUploadMarkup = studyLogoUploadMarkup; 
	}

	
	private void postSaveUpdate(AjaxRequestTarget target){
		detailFormContainer.setEnabled(false);
		saveArchivebuttonContainer.setVisible(false);
		editbuttonContainer.setVisible(true);
		summaryContainer.setVisible(true);
		target.addComponent(detailFormContainer);
		target.addComponent(feedBackPanel);
		target.addComponent(saveArchivebuttonContainer);
		target.addComponent(editbuttonContainer);
		target.addComponent(summaryContainer);
	}
	
	@SuppressWarnings("serial")
	public void initialisePanel(){
		
		detailForm = new DetailForm("detailForm", 
									detailsContainer,
									saveArchivebuttonContainer, 
									editbuttonContainer, 
									summaryContainer,
									detailFormContainer,
									studyNameMarkup,
									studyLogoMarkup,
									studyLogoImageContainer,
									arkContextMarkup,
									studyContainerForm
									){
			
			protected void onSave(StudyModelVO studyModel, AjaxRequestTarget target){
				
				try{
					if(studyModel.getStudy()!= null && studyModel.getStudy().getId() == null){
						service.createStudy(studyModel.getStudy(),studyModel.getLmcSelectedApps());
						this.info("Study: " + studyModel.getStudy().getName().toUpperCase() + " has been saved.");
						postSaveUpdate(target);

					}else{
						//Update
						service.updateStudy(studyModel.getStudy(),studyModel.getLmcSelectedApps() );
						this.info("Update of Study: " + studyModel.getStudy().getName().toUpperCase() + " was Successful.");
						postSaveUpdate(target);
					}
					
					SecurityUtils.getSubject().getSession().setAttribute("studyId", studyModel.getStudy().getId());
					SecurityUtils.getSubject().getSession().removeAttribute(au.org.theark.core.Constants.PERSON_CONTEXT_ID);
					SecurityUtils.getSubject().getSession().removeAttribute(au.org.theark.core.Constants.PERSON_TYPE);
					studyContainerForm.getModelObject().setStudy(studyModel.getStudy());
					
					// Set Study into context items
					ContextHelper contextHelper = new ContextHelper();
					contextHelper.resetContextLabel(target, arkContextMarkup);
					contextHelper.setStudyContextLabel(target, studyModel.getStudy().getName(), arkContextMarkup);

				}
				catch(EntityExistsException eee){
					this.error(eee.getMessage());
				}
				catch(EntityCannotBeRemoved ecbr){
					
					this.error(ecbr.getMessage());
				}
				catch(ArkSystemException arkSystemExeption){
					this.error(arkSystemExeption.getMessage());
				}catch(Exception generalException){
					generalException.getStackTrace();
					this.error(generalException.getMessage());
				}
				
			}
			
			protected void processErrors(AjaxRequestTarget target){
				target.addComponent(feedBackPanel);
			}
		
		
			protected void onCancel(AjaxRequestTarget target){
				
				//Reset the model object.This is very important at this event.
				studyContainerForm.setModelObject(new StudyModelVO());
				
				summaryContainer.setVisible(true);
				listContainer.setVisible(true);
				detailsContainer.setVisible(false);
				searchContainer.setVisible(true);
				editbuttonContainer.setVisible(true);
				target.addComponent(summaryContainer);
				target.addComponent(detailsContainer);
				target.addComponent(listContainer);
				target.addComponent(searchContainer);
				target.addComponent(editbuttonContainer);
				target.addComponent(feedBackPanel);
			}
			
			protected void onArchive(StudyModelVO studyModel,AjaxRequestTarget target){
				try{
					
					service.archiveStudy(studyModel.getStudy());
					this.info("The study " + studyModel.getStudy().getName() + " has been archived. You cannot edit this study anymore.");
					postSaveUpdate(target);
					detailForm.getEditButton().setEnabled(false);
				}catch(StatusNotAvailableException statusNotAvailable){
					
					this.error("The study cannot be archived at the moment.An administrator will get back to you.");
				}
				catch(ArkSystemException arkException){
					this.error(arkException.getMessage());
				} catch (UnAuthorizedOperation e) {
					this.error(e.getMessage());
				}
				target.addComponent(feedBackPanel);
			}
		};
		add(detailForm); //Add the form to the panel
	}
		
}
