package au.org.theark.study.web.component.study;

import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import au.org.theark.study.model.entity.Study;
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
	public Details(String id, Study study, final Search searchPanel) {
		
		super(id);
		setSearchPanel(searchPanel);
		
		studyForm = new StudyForm("studyForm", study){
			protected void onSave(Study study){
				
				try{
					if(study.getStudyKey() == null){
						service.createStudy(study);
					}else{
							//Update
					}
				}catch(Exception ex){
						
				}
			}
		
			protected void onDelete(Study study){
				System.out.println("onDelete invoked.");
			}
		
			protected void onCancel(){
				searchPanel.setDetailsPanelVisible(false);
			}
		};
		
		add(studyForm); //Add the form to the panel
	}

}
