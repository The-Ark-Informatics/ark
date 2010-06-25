package au.org.theark.study.web.component.study;

import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import au.org.theark.study.model.entity.Study;
import au.org.theark.study.service.IStudyService;
import au.org.theark.study.web.Constants;
import au.org.theark.study.web.form.StudyForm;

public class Details extends Panel{

	private static final long serialVersionUID = 1L;

	//private transient Logger log = LoggerFactory.getLogger(Details.class);

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

	public Details(String id, Study study, Search searchPanel) {
		super(id);
		setSearchPanel(searchPanel);
		
		studyForm = new StudyForm("studyForm", study){
			private static final long serialVersionUID = 1L;
			
			
			protected  void onSave(Study study){
				
				//New
				try{
					if(study.getStudyKey() == null){
						//Validate if all is good then save it
						service.createStudy(study);
					}
				}catch(Exception ex){
					
				}
			}
			
			
		};
		
	}

}
