package au.org.theark.study.web.component.study;

import org.apache.wicket.markup.html.panel.Panel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class StudyContainerPanel extends Panel{


	private static final long serialVersionUID = 1L;
	private transient Logger log = LoggerFactory.getLogger(StudyContainerPanel.class);
	
	
	//Child Components
	private Search searchStudyPanel;
	
	public StudyContainerPanel(String id) {
		super(id);
		log.info("StudyContainerPanel Constructor invoked.");
		searchStudyPanel = new Search("searchStudyPanel");
		searchStudyPanel.initialise("searchStudyPanel");
		add(searchStudyPanel);
	}

}
