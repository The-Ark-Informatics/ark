package au.org.theark.study.web.component.managestudy;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.odlabs.wiquery.ui.themes.ThemeUiHelper;

import au.org.theark.core.exception.ArkSystemException;
import au.org.theark.core.exception.EntityCannotBeRemoved;
import au.org.theark.core.exception.EntityExistsException;
import au.org.theark.core.exception.StatusNotAvailableException;
import au.org.theark.core.exception.UnAuthorizedOperation;
import au.org.theark.study.model.vo.StudyModel;
import au.org.theark.study.service.IStudyService;
import au.org.theark.study.web.Constants;
import au.org.theark.study.web.component.managestudy.form.DetailForm;

public class Details  extends Panel{

	//A container for SearchResults
	private WebMarkupContainer listContainer;
	private WebMarkupContainer detailsContainer;
	private WebMarkupContainer summaryContainer;
	private WebMarkupContainer searchContainer;
	private WebMarkupContainer saveArchivebuttonContainer;
	private WebMarkupContainer editbuttonContainer;
	
	
	
	@SpringBean( name = Constants.STUDY_SERVICE)
	private IStudyService service;
	
	/* A reference of the Model from the Container in this case Search Panel */
	private CompoundPropertyModel<StudyModel> cpm;
	
	public CompoundPropertyModel<StudyModel> getCpm() {
		return cpm;
	}

	public void setCpm(CompoundPropertyModel<StudyModel> cpm) {
		this.cpm = cpm;
	}
	
	
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
	public Details(String id, final WebMarkupContainer listContainer, FeedbackPanel feedBackPanel, WebMarkupContainer detailsContainer, 
			WebMarkupContainer searchWebMarkupContainer, WebMarkupContainer saveArchBtnContainer, WebMarkupContainer editBtnContainer, WebMarkupContainer detailSumContainer) {
		super(id);
		this.listContainer = listContainer;
		this.feedBackPanel = feedBackPanel;
		this.detailsContainer = detailsContainer;
		this.searchContainer = searchWebMarkupContainer;
		this.saveArchivebuttonContainer = saveArchBtnContainer;
		this.editbuttonContainer = editBtnContainer;
		this.summaryContainer = detailSumContainer;
	}

	public void initialisePanel(){

		
		detailForm = new DetailForm("detailForm", this, listContainer,detailsContainer,searchContainer,saveArchivebuttonContainer, editbuttonContainer, summaryContainer){
			
			protected void onSave(StudyModel studyModel, AjaxRequestTarget target){
				
				try{
					if(studyModel.getStudy()!= null && studyModel.getStudy().getStudyKey() == null){
						service.createStudy(studyModel.getStudy(),studyModel.getLmcSelectedApps());
						this.info("Study: " + studyModel.getStudy().getName().toUpperCase() + " has been saved.");
						detailsContainer.setEnabled(false);
						saveArchivebuttonContainer.setVisible(false);
						editbuttonContainer.setVisible(false);
						summaryContainer.setVisible(true);
						target.addComponent(detailsContainer);
						target.addComponent(feedBackPanel);
						target.addComponent(saveArchivebuttonContainer);
						target.addComponent(editbuttonContainer);
						target.addComponent(summaryContainer);
					}else{
						//Update
						service.updateStudy(studyModel.getStudy(),studyModel.getLmcSelectedApps() );
						this.info("Update of Study: " + studyModel.getStudy().getName().toUpperCase() + " was Successful.");
						saveArchivebuttonContainer.setVisible(false);
						editbuttonContainer.setVisible(true);
						summaryContainer.setVisible(true);
						detailsContainer.setEnabled(false);
						target.addComponent(detailsContainer);
						target.addComponent(feedBackPanel);
						target.addComponent(saveArchivebuttonContainer);
						target.addComponent(editbuttonContainer);
						target.addComponent(summaryContainer);
					}
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
				detailForm.getCancelButton().setEnabled(true);
				detailForm.getSaveButton().setEnabled(true);
				detailForm.getArchiveButton().setEnabled(true);
			}
			
			protected void onArchive(StudyModel studyModel,AjaxRequestTarget target){
				try{
				
					service.archiveStudy(studyModel.getStudy());
					processArchive();
					
				
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
		
	protected void processArchive(){
		detailForm.getCancelButton().setEnabled(true);
		detailForm.getSaveButton().setEnabled(false);
		detailForm.getArchiveButton().setEnabled(false);
	}
}
