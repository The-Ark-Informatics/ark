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
import au.org.theark.study.model.vo.StudyModel;
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
									studyContainerForm){
			
			protected void onSave(StudyModel studyModel, AjaxRequestTarget target){
				
				try{
					if(studyModel.getStudy()!= null && studyModel.getStudy().getStudyKey() == null){
						service.createStudy(studyModel.getStudy(),studyModel.getLmcSelectedApps());
						this.info("Study: " + studyModel.getStudy().getName().toUpperCase() + " has been saved.");
						postSaveUpdate(target);

					}else{
						//Update
						service.updateStudy(studyModel.getStudy(),studyModel.getLmcSelectedApps() );
						this.info("Update of Study: " + studyModel.getStudy().getName().toUpperCase() + " was Successful.");
						postSaveUpdate(target);
					}
					
					SecurityUtils.getSubject().getSession().setAttribute("studyId", studyModel.getStudy().getStudyKey());
					studyContainerForm.getModelObject().setStudy(studyModel.getStudy());

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
				studyContainerForm.setModelObject(new StudyModel());
				
				summaryContainer.setVisible(true);
				listContainer.setVisible(false);
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
			
			protected void onArchive(StudyModel studyModel,AjaxRequestTarget target){
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
