package au.org.theark.study.web.component.study;

import java.util.List;

import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import au.org.theark.core.exception.ArkSystemException;
import au.org.theark.core.vo.ModuleVO;
import au.org.theark.study.service.IStudyService;
import au.org.theark.study.service.IUserService;
import au.org.theark.study.web.Constants;
import au.org.theark.study.web.form.StudyForm;
import org.apache.wicket.ajax.AjaxRequestTarget;

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
	
	/**
	 * Sets the id and the container reference for the search results.
	 * @param id
	 * @param listContainer
	 */
	public Details(String id, final WebMarkupContainer listContainer) {
		super(id);
		this.listContainer = listContainer;
	}

	
	public void initialiseForm(){
		
		StudyForm studyForm = new StudyForm("studyForm", this, listContainer){
			
			protected void onSave(StudyModel studyModel, AjaxRequestTarget target){
				
				try{
					if(studyModel.getStudy()!= null && studyModel.getStudy().getStudyKey() == null){
						
						service.createStudy(studyModel.getStudy(),studyModel.getLmcSelectedApps());
						this.info("Study: " + studyModel.getStudy().getName().toUpperCase() + " has been saved.");
						
					}else{
						//Update
						service.updateStudy(studyModel.getStudy(), null);
						this.info("Update of Study: " + studyModel.getStudy().getName().toUpperCase() + " was Successful.");
					}
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
			
			protected void onDelete(AjaxRequestTarget target){
				
			}
		};
		//TODO Add the application selector multi select pallete
		try {
			List<ModuleVO> modules = userService.getModules(true);
			ApplicationSelector applicationSelector = new ApplicationSelector("applicationSelector", studyForm.getModelObject(), modules);
			//applicationSelector.setupSelector();
			//studyForm.add(applicationSelector);
		} catch (ArkSystemException e) {
			e.printStackTrace();
		}
		
		add(studyForm); //Add the form to the panel
	}

}
