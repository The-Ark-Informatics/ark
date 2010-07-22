package au.org.theark.study.web.component.study;

import java.util.List;

import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import au.org.theark.core.exception.ArkSystemException;
import au.org.theark.study.service.IStudyService;
import au.org.theark.study.web.Constants;
import au.org.theark.study.web.form.StudyForm;

@SuppressWarnings("serial")
public class Details extends Panel{


	@SpringBean( name = Constants.STUDY_SERVICE)
	private IStudyService service;
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

	/**
	 * Constructor
	 * @param id
	 * @param study
	 * @param searchPanel
	 */
	public Details(String id, StudyModel studyModel, final Search searchPanel) {
		
		super(id);
		setSearchPanel(searchPanel);
		
		studyForm = new StudyForm("studyForm", studyModel){
			
			protected void onSave(StudyModel studyModel){
				
				try{
					if(studyModel.getStudy()!= null && studyModel.getStudy().getStudyKey() == null){
						List<String> itemsSelected = studyModel.getLmcSelectedApps();
						service.createStudy(studyModel.getStudy(),itemsSelected);
						
					}else{
							//Update
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
		
			protected void onCancel(){
				searchPanel.setDetailsPanelVisible(false);
			}
		};
		
		add(studyForm); //Add the form to the panel
	}

}
