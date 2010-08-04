package au.org.theark.study.web.component.study;

import java.util.List;

import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import au.org.theark.core.exception.ArkSystemException;
import au.org.theark.core.vo.ModuleVO;
import au.org.theark.study.service.IStudyService;
import au.org.theark.study.service.IUserService;
import au.org.theark.study.web.Constants;
import au.org.theark.study.web.form.StudyForm;

@SuppressWarnings("serial")
public class Details extends Panel{

	@SpringBean( name = Constants.STUDY_SERVICE)
	private IStudyService service;
	
	@SpringBean( name = "userService")
	private IUserService userService;
	
	
	private StudyForm studyForm;
	private Search searchPanel;
	
	public Search getSearchPanel() {
		return searchPanel;
	}

	public void setSearchPanel(Search searchPanel) {
		this.searchPanel = searchPanel;
	}

	public StudyForm getStudyForm() {
		return studyForm;
	}

	public void setStudyForm(StudyForm studyForm) {
		this.studyForm = studyForm;
	}
	private int mode;

	/**
	 * Constructor
	 * @param id
	 * @param study
	 * @param searchPanel
	 */
	public Details(String id, final Search searchPanel, int mode) {
		super(id);
		setSearchPanel(searchPanel);
		this.mode = mode;
	}
	
	public void initialiseForm(StudyModel studyModel){
		
		studyForm = new StudyForm("studyForm", studyModel){
			
			protected void onSave(StudyModel studyModel){
				
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
		
			protected void onDelete(StudyModel studyModel){
				System.out.println("onDelete invoked.");
			}
		
			protected void onCancel(StudyModel studyModel){
				studyModel = new StudyModel();
				searchPanel.setDetailsPanelVisible(false);
			}
		};
		try {
			List<ModuleVO> modules = userService.getModules(true);
			ApplicationSelector applicationSelector = new ApplicationSelector("applicationSelector", studyForm.getModelObject(), modules);
			applicationSelector.setupSelector();
			studyForm.add(applicationSelector);
		} catch (ArkSystemException e) {
			e.printStackTrace();
		}
		
		add(studyForm); //Add the form to the panel
	}

}
