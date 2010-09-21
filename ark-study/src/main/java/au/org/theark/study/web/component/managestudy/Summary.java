package au.org.theark.study.web.component.managestudy;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.CompoundPropertyModel;

import au.org.theark.study.model.vo.StudyModel;

public class Summary extends Panel{
	
	
	/* A reference of the Model from the Container in this case Search Panel */
	private CompoundPropertyModel<StudyModel> cpm;
	
	public CompoundPropertyModel<StudyModel> getCpm() {
		return cpm;
	}

	public void setCpm(CompoundPropertyModel<StudyModel> cpm) {
		this.cpm = cpm;
	}
	
	public Summary(String id, CompoundPropertyModel<StudyModel> cpm){
		super(id);
		Label studySummaryLabel = new Label("studySummaryLabel","You have selected the Study " + cpm.getObject().getStudy().getName() + ".Summary of the study follows:");
		add(studySummaryLabel);
	}

}
