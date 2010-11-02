package au.org.theark.study.web.component.managestudy;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.CompoundPropertyModel;

import au.org.theark.core.vo.StudyModelVO;



public class Summary extends Panel{
	
	
	/* A reference of the Model from the Container in this case Search Panel */
	private CompoundPropertyModel<StudyModelVO> cpm;
	
	public CompoundPropertyModel<StudyModelVO> getCpm() {
		return cpm;
	}

	public void setCpm(CompoundPropertyModel<StudyModelVO> cpm) {
		this.cpm = cpm;
	}
	
	public Summary(String id, CompoundPropertyModel<StudyModelVO> cpm){
		super(id);
		Label studySummaryLabel = new Label("studySummaryLabel","You have selected the Study " + cpm.getObject().getStudy().getName() + ".Summary of the study follows:");
		add(studySummaryLabel);
	}

}
