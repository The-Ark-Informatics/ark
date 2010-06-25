package au.org.theark.study.web.component.study;

import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import au.org.theark.study.model.entity.Study;

public class StudyContainerPanel extends Panel{


	private static final long serialVersionUID = 1L;
	private transient Logger log = LoggerFactory.getLogger(StudyContainerPanel.class);
	
	private Search searchStudyPanel;
	
	public StudyContainerPanel(String id) {
		super(id);
		log.info("StudyContainerPanel Constructor invoked.");
		searchStudyPanel = new Search("searchStudyPanel");
		add(searchStudyPanel);
	}
	
	public class SearchStudyForm extends Form<Study>{

		private static final long serialVersionUID = 1L;

		protected void onSave(Study study){}
		
		public SearchStudyForm(String id, Study study) {
			super(id, new CompoundPropertyModel<Study>(study));
			searchStudyPanel = new Search("searchStudyPanel");
			add(searchStudyPanel);
		}
		
	}

}
