package au.org.theark.study.web.component.study;

import java.util.Collection;
import java.util.List;

import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import au.org.theark.core.exception.ArkSystemException;
import au.org.theark.core.exception.EntityCannotBeRemoved;
import au.org.theark.core.exception.EntityExistsException;
import au.org.theark.core.exception.UnAuthorizedOperation;
import au.org.theark.core.vo.ModuleVO;
import au.org.theark.study.service.IStudyService;
import au.org.theark.study.service.IUserService;
import au.org.theark.study.web.Constants;
import au.org.theark.study.web.form.ModuleVo;
import au.org.theark.study.web.form.StudyForm;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.extensions.markup.html.form.palette.Palette;

@SuppressWarnings("serial")
public class Details extends Panel{

	//A container for SearchResults
	WebMarkupContainer listContainer;
	
	@SpringBean( name = Constants.STUDY_SERVICE)
	private IStudyService service;
	
	@SpringBean( name = "userService")
	private IUserService userService;
	
	
	/* A reference of the Model from the Container in this case Search Panel */
	private CompoundPropertyModel<StudyModel> cpm;
	
	public CompoundPropertyModel<StudyModel> getCpm() {
		return cpm;
	}

	public void setCpm(CompoundPropertyModel<StudyModel> cpm) {
		this.cpm = cpm;
	}
	
	
	public StudyForm getStudyForm() {
		return studyForm;
	}

	private StudyForm studyForm;
	
	private FeedbackPanel feedBackPanel;
	
	/**
	 * Sets the id and the container reference for the search results.
	 * @param id
	 * @param listContainer
	 */
	public Details(String id, final WebMarkupContainer listContainer, FeedbackPanel feedBackPanel) {
		super(id);
		this.listContainer = listContainer;
		this.feedBackPanel = feedBackPanel;
	}

	
	public void initialiseForm(){
		
		 studyForm = new StudyForm("studyForm", this, listContainer, feedBackPanel){
			
			protected void onSave(StudyModel studyModel, AjaxRequestTarget target){
				
				try{
					if(studyModel.getStudy()!= null && studyModel.getStudy().getStudyKey() == null){
						service.createStudy(studyModel.getStudy(),studyModel.getLmcSelectedApps());
						this.info("Study: " + studyModel.getStudy().getName().toUpperCase() + " has been saved.");
					}else{
						//Update
						service.updateStudy(studyModel.getStudy(),studyModel.getLmcSelectedApps() );
						this.info("Update of Study: " + studyModel.getStudy().getName().toUpperCase() + " was Successful.");
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
		
		
			protected void onCancel(AjaxRequestTarget target){
				
			}
			
			protected void onDelete(StudyModel studyModel,AjaxRequestTarget target){
				try{
				
					service.deleteStudy(studyModel.getStudy());	
				
				}catch(ArkSystemException arkException){
					this.error(arkException.getMessage());
				} catch (EntityCannotBeRemoved e) {
					this.error(e.getMessage());
				} catch (UnAuthorizedOperation e) {
					this.error(e.getMessage());
				}
			}
		};
		
		add(studyForm); //Add the form to the panel
	}

}
